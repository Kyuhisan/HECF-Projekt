#!/bin/sh

# Wait until Mongo is ready
until mongo --host mongo --username "$MONGO_INITDB_ROOT_USERNAME" --password "$MONGO_INITDB_ROOT_PASSWORD" --authenticationDatabase admin --eval "db.adminCommand('ping')" &>/dev/null; do
  echo "Waiting for MongoDB..."
  sleep 2
done

echo "Importing to Listings-Filtered..."
mongoimport \
  --host mongo \
  --db Listings \
  --collection "Listings-Filtered" \
  --file /mongo-init/Listings-Filtered.json \
  --jsonArray \
  --username "$MONGO_INITDB_ROOT_USERNAME" \
  --password "$MONGO_INITDB_ROOT_PASSWORD" \
  --authenticationDatabase admin

echo "Import complete."
