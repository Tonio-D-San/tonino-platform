#!/bin/sh
set -eu
# Runs only for an empty PostgreSQL data volume.
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" \
  --set=people_user="$DB_PEOPLE_USER" --set=people_password="$DB_PEOPLE_PASSWORD" \
  --set=people_db="$DB_PEOPLE_NAME" --set=kc_user="$KC_DB_USER" \
  --set=kc_password="$KC_DB_PASSWORD" --set=kc_db="$KC_DB_NAME" <<'SQL'
CREATE USER :"people_user" WITH PASSWORD :'people_password';
CREATE DATABASE :"people_db" WITH OWNER :"people_user";
CREATE USER :"kc_user" WITH PASSWORD :'kc_password';
CREATE DATABASE :"kc_db" WITH OWNER :"kc_user";
SQL
