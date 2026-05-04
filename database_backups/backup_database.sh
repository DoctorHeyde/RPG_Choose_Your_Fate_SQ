#!/usr/bin/env bash
set -euo pipefail

CONTAINER_NAME="${CONTAINER_NAME:-rpg_mysql}"
DATABASE_NAME="${DATABASE_NAME:-choose_your_fate}"
DB_USERNAME="${DB_USERNAME:-root}"
DB_PASSWORD="${DB_PASSWORD:-123456}"
BACKUP_DIRECTORY="${BACKUP_DIRECTORY:-$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)}"

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker is not installed or is not available in PATH." >&2
  exit 1
fi

mkdir -p "$BACKUP_DIRECTORY"

timestamp="$(date +"%Y%m%d-%H%M%S")"
backup_file="$BACKUP_DIRECTORY/${DATABASE_NAME}_${timestamp}.sql"
container_backup="/tmp/${DATABASE_NAME}_${timestamp}.sql"

docker exec "$CONTAINER_NAME" mysqldump \
  "--user=$DB_USERNAME" \
  "--password=$DB_PASSWORD" \
  --databases "$DATABASE_NAME" \
  --routines \
  --triggers \
  --events \
  --single-transaction \
  --add-drop-database \
  --add-drop-table \
  "--result-file=$container_backup"

docker cp "$CONTAINER_NAME:$container_backup" "$backup_file"
docker exec "$CONTAINER_NAME" rm "$container_backup" >/dev/null

echo "Database backup created:"
echo "$backup_file"
