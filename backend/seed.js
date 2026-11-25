const { db, databasePath } = require('./config/database');
const { now, newId } = require('./utils');

// Reinicia únicamente los datos de demostración. El catálogo de categorías
// se conserva porque pertenece a una tabla independiente.
db.exec('DELETE FROM places; DELETE FROM trips;');
const timestamp = now();
const tripId = newId();
db.prepare('INSERT INTO trips (id, name, destination, startDate, endDate, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?)')
  .run(tripId, 'Europa 2026', 'Italia', '2026-06-10', '2026-06-20', timestamp, timestamp);

const categoryIdByName = name => {
  const category = db.prepare('SELECT id FROM categories WHERE name = ?').get(name);
  if (!category) throw new Error(`No se encontró la categoría ${name}`);
  return category.id;
};

const insertPlace = db.prepare(`INSERT INTO places
  (id, tripId, name, description, city, categoryId, tentativeDate, createdAt, updatedAt)
  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`);
[
  ['Coliseo Romano', 'Anfiteatro romano, uno de los principales atractivos de Roma.', 'Roma', 'Histórico', '2026-06-12'],
  ['Fontana di Trevi', 'Fuente monumental ubicada en el centro de Roma.', 'Roma', 'Histórico', '2026-06-13'],
  ['Vaticano', 'Lugar de interés histórico, artístico y religioso.', 'Ciudad del Vaticano', 'Religioso', '2026-06-15']
].forEach(([name, description, city, categoryId, tentativeDate]) => {
  insertPlace.run(newId(), tripId, name, description, city, categoryIdByName(categoryId), tentativeDate, timestamp, timestamp);
});

console.log('Datos de demostración creados correctamente.');
console.log('ID del viaje:', tripId);
console.log('Archivo SQLite:', databasePath);
db.close();
