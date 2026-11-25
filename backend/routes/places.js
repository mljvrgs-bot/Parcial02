const express = require('express');
const { db } = require('../config/database');
const { isValidDate, now, newId } = require('../utils');
const router = express.Router();

const selectPlaces = `SELECT p.id, p.tripId, p.name, p.description, p.city, p.categoryId,
  p.tentativeDate, p.createdAt, p.updatedAt,
  c.name AS categoryName, c.description AS categoryDescription
  FROM places p JOIN categories c ON c.id = p.categoryId`;
const withCategory = place => ({
  ...place,
  category: { id: place.categoryId, name: place.categoryName, description: place.categoryDescription },
  categoryName: undefined,
  categoryDescription: undefined
});

/** @openapi
 * /api/places:
 *   post:
 *     summary: Agregar un lugar a visitar dentro de un viaje
 *     tags: [Lugares]
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required: [tripId, name, description, city, categoryId, tentativeDate]
 *             properties:
 *               tripId: { type: string }
 *               name: { type: string, example: Coliseo Romano }
 *               description: { type: string, example: Anfiteatro romano, uno de los principales atractivos de Roma. }
 *               city: { type: string, example: Roma }
 *               categoryId: { type: integer, example: 1 }
 *               tentativeDate: { type: string, format: date, example: '2026-06-12' }
 *     responses:
 *       201: { description: Lugar creado }
 *       400: { description: Datos inválidos }
 *       404: { description: Viaje no encontrado }
 *   get:
 *     summary: Consultar lugares (opcionalmente filtrados por viaje)
 *     tags: [Lugares]
 *     parameters:
 *       - in: query
 *         name: tripId
 *         schema: { type: string }
 *     responses:
 *       200: { description: Lista de lugares }
 */
router.post('/', (req, res) => {
  const { tripId, name, description, city, categoryId, tentativeDate } = req.body;
  if (![tripId, name, description, city, tentativeDate].every(value => typeof value === 'string' && value.trim()) || !Number.isInteger(categoryId) || categoryId <= 0) {
    return res.status(400).json({ message: 'Todos los campos son obligatorios: tripId, name, description, city, categoryId y tentativeDate.' });
  }
  const trip = db.prepare('SELECT * FROM trips WHERE id = ?').get(tripId);
  if (!trip) return res.status(404).json({ message: 'No existe el viaje indicado.' });
  const category = db.prepare('SELECT id FROM categories WHERE id = ?').get(categoryId);
  if (!category) {
    return res.status(400).json({ message: 'El categoryId no es válido. Consulta GET /api/categories.' });
  }
  if (!isValidDate(tentativeDate)) return res.status(400).json({ message: 'tentativeDate debe ser una fecha válida con formato YYYY-MM-DD.' });
  if (tentativeDate < trip.startDate || tentativeDate > trip.endDate) {
    return res.status(400).json({ message: 'La fecha tentativa debe estar dentro del rango de fechas del viaje.' });
  }

  const timestamp = now();
  const place = {
    id: newId(), tripId, name: name.trim(), description: description.trim(), city: city.trim(),
    categoryId, tentativeDate, createdAt: timestamp, updatedAt: timestamp
  };
  db.prepare(`INSERT INTO places
    (id, tripId, name, description, city, categoryId, tentativeDate, createdAt, updatedAt)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`)
    .run(place.id, place.tripId, place.name, place.description, place.city, place.categoryId, place.tentativeDate, place.createdAt, place.updatedAt);

  const created = db.prepare(`${selectPlaces} WHERE p.id = ?`).get(place.id);
  res.status(201).json(withCategory(created));
});

router.get('/', (req, res) => {
  if (req.query.tripId && !db.prepare('SELECT id FROM trips WHERE id = ?').get(req.query.tripId)) {
    return res.status(404).json({ message: 'No existe el viaje indicado.' });
  }
  const places = req.query.tripId
    ? db.prepare(`${selectPlaces} WHERE p.tripId = ? ORDER BY p.tentativeDate ASC`).all(req.query.tripId)
    : db.prepare(`${selectPlaces} ORDER BY p.tentativeDate ASC`).all();
  res.json(places.map(withCategory));
});

module.exports = router;
