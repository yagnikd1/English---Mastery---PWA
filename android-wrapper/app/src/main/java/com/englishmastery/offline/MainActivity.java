package com.englishmastery.offline;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public final class MainActivity extends Activity {
    private static final int REQUEST_TREE = 4101;
    private static final int REQUEST_AUDIO_PERMISSION = 4102;
    private static final int REQUEST_OPEN_JSON = 4103;
    private static final int REQUEST_SAVE_JSON = 4104;
    private static final String PREFS = "english_mastery_android";
    private static final String KEY_TREE_URI = "course_tree_uri";
    private static final int PORT = 8765;

    private LocalWebServer server;
    private WebView webView;
    private PermissionRequest pendingPermissionRequest;
    private ValueCallback<Uri[]> pendingFileChooser;
    private String pendingDownloadJson;
    private String pendingDownloadName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(8, 12, 20));
        getWindow().setNavigationBarColor(Color.rgb(8, 12, 20));

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String saved = prefs.getString(KEY_TREE_URI, null);
        if (saved == null) {
            showFolderSetup("Select the extracted English Mastery folder once. After that, this app opens the course automatically without Simple HTTP Server.");
        } else {
            launchCourse(Uri.parse(saved));
        }
    }

    private void showFolderSetup(String message) {
        stopServer();
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(28), dp(28), dp(28), dp(28));
        root.setBackgroundColor(Color.rgb(8, 12, 20));

        TextView title = new TextView(this);
        title.setText("English Mastery");
        title.setTextColor(Color.WHITE);
        title.setTextSize(34);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(title.getTypeface(), 1);

        TextView body = new TextView(this);
        body.setText(message);
        body.setTextColor(Color.rgb(190, 201, 219));
        body.setTextSize(17);
        body.setGravity(Gravity.CENTER);
        body.setPadding(0, dp(18), 0, dp(24));

        Button choose = new Button(this);
        choose.setText("Choose English Mastery folder");
        choose.setAllCaps(false);
        choose.setTextSize(16);
        choose.setOnClickListener(v -> chooseFolder());

        root.addView(title, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(body, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(choose, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(54)));
        setContentView(root);
    }

    private void showLoading(String text) {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(24), dp(24), dp(24), dp(24));
        root.setBackgroundColor(Color.rgb(8, 12, 20));

        ProgressBar progress = new ProgressBar(this);
        TextView label = new TextView(this);
        label.setText(text);
        label.setTextColor(Color.WHITE);
        label.setTextSize(17);
        label.setGravity(Gravity.CENTER);
        label.setPadding(0, dp(18), 0, 0);

        root.addView(progress);
        root.addView(label, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(root);
    }

    private void chooseFolder() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION |
                Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_TREE);
    }

    private void launchCourse(Uri treeUri) {
        showLoading("Preparing your offline course…");
        new Thread(() -> {
            try {
                stopServer();
                LocalWebServer newServer = new LocalWebServer(this, treeUri, PORT);
                newServer.prepare();
                newServer.start();
                server = newServer;
                runOnUiThread(this::showWebView);
            } catch (Exception error) {
                runOnUiThread(() -> showFolderSetup(
                        "This folder does not contain a usable English Mastery index.html file. Select the extracted folder that directly contains index.html.\n\nDetails: " + error.getMessage()
                ));
            }
        }, "EnglishMastery-Prepare").start();
    }

    private void showWebView() {
        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(8, 12, 20));

        webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setBackgroundColor(Color.rgb(8, 12, 20));
        webView.addJavascriptInterface(new AndroidBridge(), "AndroidBridge");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.evaluateJavascript(buildAndroidPatch(), null);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith("http://127.0.0.1:" + PORT)) {
                    return false;
                }
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception ignored) {
                    Toast.makeText(MainActivity.this, "Unable to open this link.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                runOnUiThread(() -> handleWebPermission(request));
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (pendingFileChooser != null) pendingFileChooser.onReceiveValue(null);
                pendingFileChooser = filePathCallback;
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/json");
                try {
                    startActivityForResult(intent, REQUEST_OPEN_JSON);
                    return true;
                } catch (ActivityNotFoundException error) {
                    pendingFileChooser = null;
                    return false;
                }
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }
        });

        root.addView(webView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(root);
        webView.loadUrl("http://127.0.0.1:" + PORT + "/");
    }

    private String buildAndroidPatch() {
        return """
                (function(){
                  if(window.__englishMasteryAndroidFixed)return;
                  window.__englishMasteryAndroidFixed=true;

                  window.download=function(data,name){
                    AndroidBridge.saveJson(JSON.stringify(data,null,2),name||'english-mastery-backup.json');
                  };

                  var style=document.getElementById('android-v101-fixes');
                  if(!style){
                    style=document.createElement('style');
                    style.id='android-v101-fixes';
                    style.textContent=`
                      .topbar{padding-top:6px!important}
                      body.light{--muted:#465467!important;--line:#c7d0dc!important;--card2:#e8edf5!important;--accent2:#087f74!important}
                      body.light .pill,body.light .tag{background:#dcf5f0!important;color:#087f74!important;border-color:#97d8cd!important}
                      body.light .module-no,body.light .eyebrow,body.light .nav-item.active{color:#087f74!important}
                      body.light .secondary{color:#101828!important;border-color:#b7c2d0!important}
                      body.light .daily-task p,body.light .muted,body.light .empty-review,body.light .weekly-summary-grid span,body.light .daily-plan-summary span{color:#465467!important}
                      #androidAppPanel .button-stack{margin-top:12px}
                    `;
                    document.head.appendChild(style);
                  }

                  function forceAndroidStatus(){
                    var network=document.getElementById('networkBadge');
                    var storage=document.getElementById('storageBadge');
                    if(network&&network.textContent!=='OFFLINE APP')network.textContent='OFFLINE APP';
                    if(storage&&storage.textContent!=='ON-DEVICE FILES')storage.textContent='ON-DEVICE FILES';
                  }

                  function reconcileDailyPlan(){
                    var settings=typeof getDailyPlanSettings==='function'?getDailyPlanSettings():null;
                    var target=settings&&Number(settings.minutesPerDay);
                    if(!target)return;

                    var summary=document.querySelector('#dailyPlan .daily-plan-summary');
                    if(summary&&summary.children[0]){
                      var value=summary.children[0].querySelector('b');
                      var label=summary.children[0].querySelector('span');
                      if(value&&value.textContent!==String(target))value.textContent=String(target);
                      if(label&&label.textContent!=='daily target')label.textContent='daily target';
                    }

                    document.querySelectorAll('#dailyPlan .daily-task:not(.compact) p').forEach(function(p){
                      if(!p.dataset.totalMinutes){
                        var match=p.textContent.match(/^(Guided lesson\\s+\\d+)\\s+·\\s+about\\s+(\\d+)\\s+minutes$/);
                        if(match){
                          p.dataset.lessonPrefix=match[1];
                          p.dataset.totalMinutes=match[2];
                        }
                      }
                      if(p.dataset.totalMinutes){
                        var wanted=p.dataset.lessonPrefix+' · '+p.dataset.totalMinutes+' min total · '+target+' min today';
                        if(p.textContent!==wanted)p.textContent=wanted;
                      }
                    });
                  }

                  function addAndroidSettings(){
                    var settingsScreen=document.querySelector('[data-screen="settings"]');
                    if(!settingsScreen||document.getElementById('androidAppPanel'))return;
                    var panel=document.createElement('section');
                    panel.className='panel';
                    panel.id='androidAppPanel';
                    panel.innerHTML='<div class="section-head"><div><p class="muted">Native Android controls</p><h3>Android App</h3></div><span class="pill">v1.0.1</span></div><p class="muted">English Mastery runs fully on this device. No separate server app is required.</p><div class="button-stack"><button class="secondary" id="changeCourseFolderButton">Change course folder</button><button class="secondary" id="reloadAndroidCourseButton">Reload course</button></div>';
                    settingsScreen.appendChild(panel);
                    document.getElementById('changeCourseFolderButton').onclick=function(){AndroidBridge.chooseCourseFolder();};
                    document.getElementById('reloadAndroidCourseButton').onclick=function(){location.reload();};
                  }

                  function hideInstallControls(){
                    var button=document.getElementById('installAppButton');
                    if(button)button.style.display='none';
                    var status=document.getElementById('installStatus');
                    if(status&&status.textContent!=='Installed as the English Mastery Android app.')status.textContent='Installed as the English Mastery Android app.';
                  }

                  var applying=false;
                  function applyFixes(){
                    if(applying)return;
                    applying=true;
                    try{
                      forceAndroidStatus();
                      reconcileDailyPlan();
                      addAndroidSettings();
                      hideInstallControls();
                    }finally{
                      applying=false;
                    }
                  }

                  applyFixes();
                  setTimeout(applyFixes,250);
                  setTimeout(applyFixes,900);
                  new MutationObserver(function(){requestAnimationFrame(applyFixes);}).observe(document.documentElement,{subtree:true,childList:true,characterData:true,attributes:true,attributeFilter:['class']});
                })();
                """;
    }

    private void handleWebPermission(PermissionRequest request) {
        boolean wantsAudio = false;
        for (String resource : request.getResources()) {
            if (PermissionRequest.RESOURCE_AUDIO_CAPTURE.equals(resource)) wantsAudio = true;
        }
        if (!wantsAudio) {
            request.deny();
            return;
        }
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            request.grant(new String[]{PermissionRequest.RESOURCE_AUDIO_CAPTURE});
        } else {
            pendingPermissionRequest = request;
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_AUDIO_PERMISSION && pendingPermissionRequest != null) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pendingPermissionRequest.grant(new String[]{PermissionRequest.RESOURCE_AUDIO_CAPTURE});
            } else {
                pendingPermissionRequest.deny();
            }
            pendingPermissionRequest = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TREE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            int flags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            try {
                getContentResolver().takePersistableUriPermission(uri, flags);
            } catch (SecurityException ignored) {
                try {
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (SecurityException ignoredAgain) {
                    // The temporary grant can still be used for this session.
                }
            }
            getSharedPreferences(PREFS, MODE_PRIVATE).edit().putString(KEY_TREE_URI, uri.toString()).apply();
            launchCourse(uri);
            return;
        }

        if (requestCode == REQUEST_OPEN_JSON) {
            if (pendingFileChooser != null) {
                Uri result = resultCode == RESULT_OK && data != null ? data.getData() : null;
                pendingFileChooser.onReceiveValue(result == null ? null : new Uri[]{result});
                pendingFileChooser = null;
            }
            return;
        }

        if (requestCode == REQUEST_SAVE_JSON) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null && pendingDownloadJson != null) {
                try (OutputStream output = getContentResolver().openOutputStream(data.getData(), "w")) {
                    if (output == null) throw new IllegalStateException("Unable to open destination");
                    output.write(pendingDownloadJson.getBytes(StandardCharsets.UTF_8));
                    Toast.makeText(this, "Backup saved.", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Toast.makeText(this, "Backup could not be saved: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            pendingDownloadJson = null;
            pendingDownloadName = null;
        }
    }

    private final class AndroidBridge {
        @android.webkit.JavascriptInterface
        public void saveJson(String json, String fileName) {
            pendingDownloadJson = json;
            pendingDownloadName = sanitizeFileName(fileName);
            runOnUiThread(() -> {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/json");
                intent.putExtra(Intent.EXTRA_TITLE, pendingDownloadName);
                startActivityForResult(intent, REQUEST_SAVE_JSON);
            });
        }

        @android.webkit.JavascriptInterface
        public void chooseCourseFolder() {
            runOnUiThread(MainActivity.this::chooseFolder);
        }
    }

    private static String sanitizeFileName(String name) {
        String cleaned = name == null ? "english-mastery-backup.json" : name.replaceAll("[\\\\/:*?\"<>|]", "_");
        return cleaned.endsWith(".json") ? cleaned : cleaned + ".json";
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            moveTaskToBack(true);
        }
    }

    private void stopServer() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    @Override
    protected void onDestroy() {
        stopServer();
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
