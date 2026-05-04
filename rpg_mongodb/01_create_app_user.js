const databaseName = process.env.MONGO_INITDB_DATABASE || "rpg_db";
const appUsername = process.env.MONGO_APP_USERNAME || "rpg_app";
const appPassword = process.env.MONGO_APP_PASSWORD || "rpg_app_password";
const adminUsername = process.env.MONGO_ADMIN_USERNAME || "rpg_admin";
const adminPassword = process.env.MONGO_ADMIN_PASSWORD || "rpg_admin_password";

const database = db.getSiblingDB(databaseName);

if (!database.getUser(adminUsername)) {
  database.createUser({
    user: adminUsername,
    pwd: adminPassword,
    roles: [
      {
        role: "dbOwner",
        db: databaseName
      }
    ]
  });
}

if (!database.getUser(appUsername)) {
  database.createUser({
    user: appUsername,
    pwd: appPassword,
    roles: [
      {
        role: "readWrite",
        db: databaseName
      }
    ]
  });
}
