# Mandatory-one-database

To run the container detached

docker compose up -d

With the spring application running: http://localhost:8080/swagger-ui/index.html#/

Neo4j migration can be triggered with:

POST http://localhost:8080/api/migrations/neo4j?clearExisting=true

MySQL seed data is available in `rpg_mysql/06_seed_data.sql`.
If the MySQL Docker volume already exists, reload the schema and seed data with:

docker compose down -v
docker compose up -d

