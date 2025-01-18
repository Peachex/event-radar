#!/bin/bash

# Ensure that required environment variables are set.
if [ -z "$MONGO_HOST" ] || [ -z "$MONGO_PORT" ] || [ -z "$MONGO_USER" ] || [ -z "$MONGO_PASSWORD" ]; then
  echo "Please set MONGO_HOST, MONGO_PORT, MONGO_USER, and MONGO_PASSWORD environment variables."
  exit 1
fi

# Default role if not set (root).
MONGO_ROLE="${MONGO_ROLE:-root}"

# Ensure MongoDB is ready before running the commands.
echo "Waiting for MongoDB at $MONGO_HOST:$MONGO_PORT to start and become ready..."

sleep 30

# Execute the MongoDB commands to create user.
echo "Running MongoDB initialization..."

mongosh --host "$MONGO_HOST" --port "$MONGO_PORT" <<EOF
use admin;
db.createUser({
  user: '$MONGO_USER',
  pwd: '$MONGO_PASSWORD',
  roles: [ '$MONGO_ROLE' ]
});
print('User $MONGO_USER created successfully');
EOF