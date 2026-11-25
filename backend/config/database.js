const fs = require('node:fs');
const path = require('node:path');
const { DatabaseSync } = require('node:sqlite');
const initialCategories = require('./categories');

const dataDirectory = path.join(__dirname, '..', 'data');
fs.mkdirSync(dataDirectory, { recursive: true });
const databasePath = process.env.DB_PATH
  ? path.resolve(process.env.DB_PATH)
  : path.join(dataDirectory, 'viajes.db');

const db = new DatabaseSync(databasePath);
db.exec('PRAGMA foreign_keys = ON;');

// Si se actualiza desde la versión previa con categorías de texto, se elimina
// únicamente el esquema anterior de lugares/categorías para recrearlo con IDs enteros.
const categoryInfo = db.prepare("PRAGMA table_info('categories')").all();
if (categoryInfo.length && categoryInfo.find(column => column.name === 'id')?.type?.toUpperCase() !== 'INTEGER') {
  db.exec('DROP TABLE IF EXISTS places; DROP TABLE IF EXISTS categories;');
}

db.exec(`
  CREATE TABLE IF NOT EXISTS trips (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL CHECK(length(name) <= 100),
    destination TEXT NOT NULL CHECK(length(destination) <= 120),
    startDate TEXT NOT NULL,
    endDate TEXT NOT NULL,
    createdAt TEXT NOT NULL,
    updatedAt TEXT NOT NULL,
    CHECK(endDate >= startDate)
  );

  CREATE TABLE IF NOT EXISTS categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE CHECK(length(name) <= 80),
    description TEXT NOT NULL CHECK(length(description) <= 250)
  );

  CREATE TABLE IF NOT EXISTS places (
    id TEXT PRIMARY KEY,
    tripId TEXT NOT NULL,
    name TEXT NOT NULL CHECK(length(name) <= 120),
    description TEXT NOT NULL CHECK(length(description) <= 1000),
    city TEXT NOT NULL CHECK(length(city) <= 100),
    categoryId INTEGER NOT NULL,
    tentativeDate TEXT NOT NULL,
    createdAt TEXT NOT NULL,
    updatedAt TEXT NOT NULL,
    FOREIGN KEY (tripId) REFERENCES trips(id) ON DELETE CASCADE,
    FOREIGN KEY (categoryId) REFERENCES categories(id)
  );
`);

const insertCategory = db.prepare('INSERT OR IGNORE INTO categories (name, description) VALUES (?, ?)');
for (const category of initialCategories) {
  insertCategory.run(category.name, category.description);
}

db.exec('CREATE INDEX IF NOT EXISTS idx_places_trip_date ON places(tripId, tentativeDate);');
module.exports = { db, databasePath };
