#!/bin/sh
echo "window.env = {" > /usr/share/nginx/html/env.js

# Add any runtime variables here
[ ! -z "$VITE_BACKEND_URL" ] && echo "  VITE_BACKEND_URL: \"$VITE_BACKEND_URL\"," >> /usr/share/nginx/html/env.js

echo "};" >> /usr/share/nginx/html/env.js

exec "$@"
