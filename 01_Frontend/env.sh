#!/bin/sh
echo "window.env = {" > /usr/share/nginx/html/env.js

# Add any runtime variables here
[ ! -z "$API_URL" ] && echo "  API_URL: \"$API_URL\"," >> /usr/share/nginx/html/env.js

echo "};" >> /usr/share/nginx/html/env.js

exec "$@"
