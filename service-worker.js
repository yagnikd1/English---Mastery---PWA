const APP_CACHE = 'english-mastery-app-v16';
const AUDIO_CACHE = 'english-mastery-audio-v11';
const APP_SHELL = [
  './',
  './index.html',
  './styles.css',
  './app.js',
  './manifest.json',
  './offline.html',
  './android-test.js',
  './update-manager.js',
  './functional-test-runner.js',
  './content/english-file-audio-reference-map.json',
  './content/audio-catalog.json',
  './content/30-day-course-index.json'
];

self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(APP_CACHE)
      .then(cache => cache.addAll(APP_SHELL))
      .then(() => self.skipWaiting())
  );
});

self.addEventListener('activate', event => {
  event.waitUntil(
    caches.keys()
      .then(keys => Promise.all(
        keys
          .filter(key => key.startsWith('english-mastery-app-') && key !== APP_CACHE)
          .map(key => caches.delete(key))
      ))
      .then(() => self.clients.claim())
  );
});

self.addEventListener('message', event => {
  if (event.data?.type === 'SKIP_WAITING') self.skipWaiting();
});

self.addEventListener('fetch', event => {
  if (event.request.method !== 'GET') return;

  if (event.request.mode === 'navigate') {
    event.respondWith(
      fetch(event.request)
        .then(response => {
          const copy = response.clone();
          caches.open(APP_CACHE).then(cache => cache.put('./index.html', copy));
          return response;
        })
        .catch(async () =>
          (await caches.match('./index.html')) ||
          (await caches.match('./offline.html')) ||
          new Response('Offline.', {status: 503})
        )
    );
    return;
  }

  event.respondWith(
    caches.match(event.request).then(cached => {
      const network = fetch(event.request)
        .then(response => {
          if (response && response.ok && response.type !== 'opaque') {
            const copy = response.clone();
            caches.open(APP_CACHE).then(cache => cache.put(event.request, copy));
          }
          return response;
        })
        .catch(() => null);

      if (cached) {
        event.waitUntil(network);
        return cached;
      }

      return network.then(response =>
        response || new Response('Offline resource unavailable.', {
          status: 503,
          headers: {'Content-Type': 'text/plain; charset=utf-8'}
        })
      );
    })
  );
});
