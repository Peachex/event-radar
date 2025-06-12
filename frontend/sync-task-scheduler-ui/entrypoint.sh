#!/bin/sh
set -e

# Generate config.js from template
envsubst < /usr/share/nginx/html/public/assets/config.template.js > /usr/share/nginx/html/public/assets/config.js

# Start Nginx
exec "$@"
