package com.englishmastery.offline;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class LocalWebServer {
    private final ContentResolver resolver;
    private final Uri treeUri;
    private final int port;
    private final Map<String, FileEntry> files = new ConcurrentHashMap<>();
    private String basePrefix = "";
    private volatile boolean running;
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private Thread acceptThread;

    LocalWebServer(Context context, Uri treeUri, int port) {
        this.resolver = context.getApplicationContext().getContentResolver();
        this.treeUri = treeUri;
        this.port = port;
    }

    void prepare() throws Exception {
        files.clear();
        String rootId = DocumentsContract.getTreeDocumentId(treeUri);
        indexDirectory(rootId, "");

        if (files.containsKey("index.html")) {
            basePrefix = "";
            return;
        }

        List<String> candidates = new ArrayList<>();
        for (String path : files.keySet()) {
            if (path.endsWith("/index.html")) candidates.add(path);
        }
        if (candidates.isEmpty()) {
            throw new IllegalArgumentException("index.html was not found");
        }
        candidates.sort((a, b) -> Integer.compare(a.length(), b.length()));
        String selected = candidates.get(0);
        basePrefix = selected.substring(0, selected.length() - "index.html".length());
    }

    private void indexDirectory(String documentId, String parentPath) throws Exception {
        Uri childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, documentId);
        String[] projection = {
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_SIZE
        };

        try (Cursor cursor = resolver.query(childrenUri, projection, null, null, null)) {
            if (cursor == null) return;
            while (cursor.moveToNext()) {
                String childId = cursor.getString(0);
                String name = cursor.getString(1);
                String mime = cursor.getString(2);
                long size = cursor.isNull(3) ? -1L : cursor.getLong(3);
                if (name == null || name.isEmpty()) continue;
                String path = parentPath.isEmpty() ? name : parentPath + "/" + name;
                if (DocumentsContract.Document.MIME_TYPE_DIR.equals(mime)) {
                    indexDirectory(childId, path);
                } else {
                    Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, childId);
                    files.put(path, new FileEntry(documentUri, mime, size));
                }
            }
        }
    }

    void start() throws IOException {
        if (running) return;
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port));
        executor = Executors.newCachedThreadPool();
        running = true;
        acceptThread = new Thread(() -> {
            while (running) {
                try {
                    Socket socket = serverSocket.accept();
                    executor.execute(() -> handleClient(socket));
                } catch (IOException error) {
                    if (running) error.printStackTrace();
                }
            }
        }, "EnglishMastery-Accept");
        acceptThread.start();
    }

    void stop() {
        running = false;
        if (serverSocket != null) {
            try { serverSocket.close(); } catch (IOException ignored) { }
            serverSocket = null;
        }
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
        if (acceptThread != null) {
            acceptThread.interrupt();
            acceptThread = null;
        }
    }

    private void handleClient(Socket socket) {
        try (Socket client = socket;
             InputStream input = new BufferedInputStream(client.getInputStream());
             OutputStream output = new BufferedOutputStream(client.getOutputStream())) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.ISO_8859_1));
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) {
                sendText(output, 400, "Bad Request", "Bad request");
                return;
            }

            String method = requestParts[0].toUpperCase(Locale.ROOT);
            String rawTarget = requestParts[1];
            Map<String, String> headers = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                int colon = line.indexOf(':');
                if (colon > 0) {
                    headers.put(line.substring(0, colon).trim().toLowerCase(Locale.ROOT), line.substring(colon + 1).trim());
                }
            }

            if (!"GET".equals(method) && !"HEAD".equals(method)) {
                sendText(output, 405, "Method Not Allowed", "Only GET and HEAD are supported");
                return;
            }

            String path = normalizePath(rawTarget);
            if (path.isEmpty()) path = "index.html";
            FileEntry entry = files.get(basePrefix + path);
            if (entry == null && path.endsWith("/")) entry = files.get(basePrefix + path + "index.html");
            if (entry == null) {
                sendText(output, 404, "Not Found", "File not found: " + path);
                return;
            }

            serveEntry(output, method, headers, path, entry);
        } catch (Exception ignored) {
            // One failed request must not stop the local server.
        }
    }

    private void serveEntry(OutputStream output, String method, Map<String, String> headers, String path, FileEntry entry) throws Exception {
        long total = entry.size >= 0 ? entry.size : querySize(entry.uri);
        String mime = chooseMime(path, entry.mimeType);
        Range range = parseRange(headers.get("range"), total);
        long start = range == null ? 0L : range.start;
        long end = range == null ? total - 1L : range.end;
        long length = Math.max(0L, end - start + 1L);
        int status = range == null ? 200 : 206;
        String reason = range == null ? "OK" : "Partial Content";

        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 ").append(status).append(' ').append(reason).append("\r\n");
        response.append("Content-Type: ").append(mime).append("\r\n");
        response.append("Content-Length: ").append(length).append("\r\n");
        response.append("Accept-Ranges: bytes\r\n");
        response.append("X-Content-Type-Options: nosniff\r\n");
        response.append("Access-Control-Allow-Origin: *\r\n");
        if (range != null) {
            response.append("Content-Range: bytes ").append(start).append('-').append(end).append('/').append(total).append("\r\n");
        }
        if (mime.contains("text/html") || mime.contains("javascript") || mime.contains("text/css") || mime.contains("json")) {
            response.append("Cache-Control: no-cache\r\n");
        } else {
            response.append("Cache-Control: public, max-age=31536000\r\n");
        }
        response.append("Connection: close\r\n\r\n");
        output.write(response.toString().getBytes(StandardCharsets.ISO_8859_1));

        if ("HEAD".equals(method) || length == 0L) {
            output.flush();
            return;
        }

        try (ParcelFileDescriptor descriptor = resolver.openFileDescriptor(entry.uri, "r")) {
            if (descriptor == null) throw new IOException("Unable to open " + path);
            try (FileInputStream file = new FileInputStream(descriptor.getFileDescriptor())) {
                file.getChannel().position(start);
                byte[] buffer = new byte[64 * 1024];
                long remaining = length;
                while (remaining > 0) {
                    int read = file.read(buffer, 0, (int)Math.min(buffer.length, remaining));
                    if (read < 0) break;
                    output.write(buffer, 0, read);
                    remaining -= read;
                }
            }
        }
        output.flush();
    }

    private long querySize(Uri uri) throws Exception {
        try (Cursor cursor = resolver.query(uri, new String[]{DocumentsContract.Document.COLUMN_SIZE}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst() && !cursor.isNull(0)) return cursor.getLong(0);
        }
        try (ParcelFileDescriptor descriptor = resolver.openFileDescriptor(uri, "r")) {
            if (descriptor == null) return 0L;
            return descriptor.getStatSize();
        }
    }

    private static Range parseRange(String value, long total) {
        if (value == null || total <= 0 || !value.startsWith("bytes=")) return null;
        try {
            String first = value.substring(6).split(",", 2)[0].trim();
            String[] bounds = first.split("-", 2);
            long start;
            long end;
            if (bounds[0].isEmpty()) {
                long suffix = Long.parseLong(bounds[1]);
                start = Math.max(0L, total - suffix);
                end = total - 1L;
            } else {
                start = Long.parseLong(bounds[0]);
                end = bounds.length < 2 || bounds[1].isEmpty() ? total - 1L : Long.parseLong(bounds[1]);
            }
            if (start < 0 || start >= total) return null;
            end = Math.min(end, total - 1L);
            if (end < start) return null;
            return new Range(start, end);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String normalizePath(String rawTarget) throws Exception {
        String target = rawTarget;
        int query = target.indexOf('?');
        if (query >= 0) target = target.substring(0, query);
        int hash = target.indexOf('#');
        if (hash >= 0) target = target.substring(0, hash);
        if (target.startsWith("http://") || target.startsWith("https://")) {
            Uri absolute = Uri.parse(target);
            target = absolute.getEncodedPath();
        }
        target = URLDecoder.decode(target == null ? "" : target, "UTF-8");
        while (target.startsWith("/")) target = target.substring(1);
        while (target.contains("//")) target = target.replace("//", "/");
        if (target.contains("..") || target.contains("\\")) throw new SecurityException("Invalid path");
        return target;
    }

    private static String chooseMime(String path, String providerMime) {
        if (providerMime != null && !providerMime.isEmpty() && !"application/octet-stream".equals(providerMime)) {
            return providerMime;
        }
        int dot = path.lastIndexOf('.');
        String ext = dot >= 0 ? path.substring(dot + 1).toLowerCase(Locale.ROOT) : "";
        String known = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        if (known != null) return known;
        if ("js".equals(ext)) return "application/javascript";
        if ("json".equals(ext)) return "application/json";
        if ("svg".equals(ext)) return "image/svg+xml";
        if ("webmanifest".equals(ext)) return "application/manifest+json";
        return "application/octet-stream";
    }

    private static void sendText(OutputStream output, int status, String reason, String text) throws IOException {
        byte[] body = text.getBytes(StandardCharsets.UTF_8);
        String header = "HTTP/1.1 " + status + " " + reason + "\r\n" +
                "Content-Type: text/plain; charset=utf-8\r\n" +
                "Content-Length: " + body.length + "\r\n" +
                "Connection: close\r\n\r\n";
        output.write(header.getBytes(StandardCharsets.ISO_8859_1));
        output.write(body);
        output.flush();
    }

    private static final class FileEntry {
        final Uri uri;
        final String mimeType;
        final long size;

        FileEntry(Uri uri, String mimeType, long size) {
            this.uri = uri;
            this.mimeType = mimeType;
            this.size = size;
        }
    }

    private static final class Range {
        final long start;
        final long end;

        Range(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }
}
