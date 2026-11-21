// Nome da versão do cache (trocado para v2 para forçar o navegador a baixar os arquivos novos)
const CACHE_NAME = 'stoki-v2';

// Lista de todos os arquivos que serão salvos no cache para funcionar offline
const RECURSOS_PARA_CACHEAR = [
    './',
    './index.html',
    './base.css',
    './secoes.css',
    './script.js',
    './manifest.webmanifest',
    './icons/logo.svg',
    './icons/icon-192.png',
    './icons/icon-512.png'
];

// Instalação do Service Worker e gravação dos arquivos no cache
self.addEventListener('install', (event) => {
    event.waitUntil(
        caches.open(CACHE_NAME).then((cache) => {
            return cache.addAll(RECURSOS_PARA_CACHEAR);
        })
    );
    self.skipWaiting();
});

// Ativação do novo Service Worker e limpeza de caches antigos (ex: stoki-v1)
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

// Interceptação das requisições para responder com o cache quando estiver sem internet
self.addEventListener('fetch', (event) => {
    event.respondWith(
        caches.match(event.request).then((respostaEmCache) => {
            return respostaEmCache || fetch(event.request);
        })
    );
});