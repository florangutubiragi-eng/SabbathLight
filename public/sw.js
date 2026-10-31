// Sabbath Life PWA Service Worker - v1.0.0
const CACHE_NAME = 'sabbath-life-shell-v1';

// Essential App Shell assets to pre-cache
const PRECACHE_URLS = [
  '/',
  '/index.html',
  '/manifest.webmanifest',
  '/app.css',
  '/app.js',
  '/icons/icon.svg',
  '/icons/icon-192.png',
  '/icons/icon-512.png',
  '/images/sabbath_hero.jpg',
  '/images/hope_channel_banner.jpg'
];

// Install Event: pre-cache static app shell
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(PRECACHE_URLS);
    }).then(() => {
      return self.skipWaiting();
    })
  );
});

// Activate Event: cleanup old caches
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames.map((cacheName) => {
          if (cacheName !== CACHE_NAME) {
            return caches.delete(cacheName);
          }
        })
      );
    }).then(() => {
      return self.clients.claim();
    })
  );
});

// Fetch Event: Cache First for App Shell, Network First with Offline Fallback for other requests
self.addEventListener('fetch', (event) => {
  const url = new URL(event.request.url);

  // Exclude external video streams, YouTube links, and cross-origin heavy media from caching
  if (url.origin !== self.location.origin) {
    // For external audio / stream / image requests, let browser handle them directly
    return;
  }

  // Handle navigation requests (HTML pages)
  if (event.request.mode === 'navigate') {
    event.respondWith(
      fetch(event.request).catch(() => {
        return caches.match('/index.html');
      })
    );
    return;
  }

  // App Shell Cache First Strategy
  event.respondWith(
    caches.match(event.request).then((cachedResponse) => {
      if (cachedResponse) {
        // Fetch in background to update cache (stale-while-revalidate for local assets)
        fetch(event.request).then((networkResponse) => {
          if (networkResponse && networkResponse.status === 200) {
            caches.open(CACHE_NAME).then((cache) => {
              cache.put(event.request, networkResponse);
            });
          }
        }).catch(() => {/* Offline */});

        return cachedResponse;
      }

      // If not in cache, try network
      return fetch(event.request).then((response) => {
        // Cache successful responses for local assets
        if (response && response.status === 200 && response.type === 'basic') {
          const responseToCache = response.clone();
          caches.open(CACHE_NAME).then((cache) => {
            cache.put(event.request, responseToCache);
          });
        }
        return response;
      }).catch(() => {
        // If image failed and offline, fallback or return empty
        if (event.request.headers.get('accept')?.includes('image')) {
          return caches.match('/icons/icon-192.png');
        }
      });
    })
  );
});
