#!/bin/sh
set -eu
# Runs only for an empty PostgreSQL data volume.
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" \
  --set=identity_user="$DB_IDENTITY_USER" --set=identity_password="$DB_IDENTITY_PASSWORD" \
  --set=identity_db="$DB_IDENTITY_NAME" --set=kc_user="$KC_DB_USER" \
  --set=kc_password="$KC_DB_PASSWORD" --set=kc_db="$KC_DB_NAME" <<'SQL'
CREATE USER :"identity_user" WITH PASSWORD :'identity_password';
CREATE DATABASE :"identity_db" WITH OWNER :"identity_user";
CREATE USER :"kc_user" WITH PASSWORD :'kc_password';
CREATE DATABASE :"kc_db" WITH OWNER :"kc_user";
SQL
