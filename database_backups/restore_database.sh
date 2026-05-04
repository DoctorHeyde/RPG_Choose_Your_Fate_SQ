#!/usr/bin/env bash
set -euo pipefail

CONTAINER_NAME="${CONTAINER_NAME:-rpg_mysql}"
DB_USERNAME="${DB_USERNAME:-root}"
DB_PASSWORD="${DB_PASSWORD:-123456}"

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <backup-file.sql>" >&2
  exit 1
fi

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker is not installed or is not available in PATH." >&2
  exit 1
fi

backup_file="$1"

if [ ! -f "$backup_file" ]; then
  echo "Backup file not found: $backup_file" >&2
  exit 1
fi

container_backup="/tmp/restore_choose_your_fate.sql"

docker cp "$backup_file" "$CONTAINER_NAME:$container_backup"
docker exec "$CONTAINER_NAME" mysql \
  "--user=$DB_USERNAME" \
  "--password=$DB_PASSWORD" \
  "--execute=source $container_backup"
docker exec "$CONTAINER_NAME" rm "$container_backup" >/dev/null

echo "Database restored from:"
echo "$backup_file"
