import type { NextConfig } from 'next';

const withPWA = require("@ducanh2912/next-pwa").default({
  dest: "public",
  // Enable cache strategies for assets, but be cautious with dynamic content
  buildExcludes: [/middleware-manifest.json$/],
  // Enable debugging
  debug: process.env.NODE_ENV === 'development',
  disable: process.env.NODE_ENV === "development",
  // register: true,
  // scope: "/app",
  // sw: "service-worker.js",
  // ...
  runtimeCaching: [
    {
      urlPattern: /^https:\/\/example\.com\/.*$/, // Match the URLs you want to cache
      handler: "CacheFirst", // Use CacheFirst or another caching strategy
      options: {
        cacheName: "example-cache",
        plugins: [
          {
            cacheWillUpdate: async () => null, // Disables the cacheWillUpdate lifecycle
          },
        ],
      },
    },
  ],
});

const withTM = require('next-transpile-modules')([
  // Add the modules you want to transpile, e.g., tailwind
]);

const config: NextConfig = {
  reactStrictMode: true,
  // any other Next.js specific configuration
  webpack(config) {
    config.resolve.extensions.push('.ts', '.tsx');
    return config;
  },
};

// Apply the PWA plugin to the config
module.exports = withPWA(withTM(config));
