const CACHE_NAME = 'stoki-v1';
const RECURSOS_PARA_CACHEAR = [
  './',
  './index.html',
  './base.css',
  './secoes.css',
  './script.js',
  './manifest.webmanifest',
  './icons/logo.svg'
];


self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(RECURSOS_PARA_CACHEAR);
    })
  );
  self.skipWaiting();
});


self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) => {
      return Promise.all(
        keys.map((key) => {
          if (key !== CACHE_NAME) {
            return caches.delete(key);
          }
        })
      );
    })
  );
  self.clients.claim();
});


self.addEventListener('fetch', (event) => {
  event.respondWith(
    caches.match(event.request).then((respostaEmCache) => {
      return respostaEmCache || fetch(event.request);
    })
  );
});