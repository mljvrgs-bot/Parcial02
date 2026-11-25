const express = require('express');
const { db } = require('../config/database');
const { isValidDate, now, newId } = require('../utils');
const router = express.Router();

/** @openapi
 * /api/trips:
 *   get:
 *     summary: Consultar todos los viajes
 *     tags: [Viajes]
 *     responses:
 *       200: { description: Lista de viajes }
 *   post:
 *     summary: Crear un viaje
 *     tags: [Viajes]
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required: [name, destination, startDate, endDate]
 *             properties:
 *               name: { type: string, example: Europa 2026 }
 *               destination: { type: string, example: Italia }
 *               startDate: { type: string, format: date, example: '2026-06-10' }
 *               endDate: { type: string, format: date, example: '2026-06-20' }
 *     responses:
 *       201: { description: Viaje creado }
 *       400: { description: Datos inválidos }
 */
router.get('/', (req, res) => {
  const trips = db.prepare('SELECT * FROM trips ORDER BY startDate ASC').all();
  res.json(trips);
});

router.post('/', (req, res) => {
  const { name, destination, startDate, endDate } = req.body;
  if (![name, destination, startDate, endDate].every(value => typeof value === 'string' && value.trim())) {
    return res.status(400).json({ message: 'name, destination, startDate y endDate son obligatorios.' });
  }
  if (!isValidDate(startDate) || !isValidDate(endDate)) {
    return res.status(400).json({ message: 'startDate y endDate deben tener el formato YYYY-MM-DD y ser fechas válidas.' });
  }
  if (endDate < startDate) {
    return res.status(400).json({ message: 'La fecha de fin debe ser igual o posterior a la fecha de inicio.' });
  }
  const timestamp = now();
  const trip = { id: newId(), name: name.trim(), destination: destination.trim(), startDate, endDate, createdAt: timestamp, updatedAt: timestamp };
  db.prepare('INSERT INTO trips (id, name, destination, startDate, endDate, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?)')
    .run(trip.id, trip.name, trip.destination, trip.startDate, trip.endDate, trip.createdAt, trip.updatedAt);
  res.status(201).json(trip);
});

/** @openapi
 * /api/trips/{id}:
 *   get:
 *     summary: Consultar el detalle de un viaje y sus lugares por visitar
 *     tags: [Viajes]
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema: { type: string }
 *     responses:
 *       200: { description: Viaje con sus lugares }
 *       404: { description: Viaje no encontrado }
 */
router.get('/:id', (req, res) => {
  const trip = db.prepare('SELECT * FROM trips WHERE id = ?').get(req.params.id);
  if (!trip) return res.status(404).json({ message: 'Viaje no encontrado.' });
  const places = db.prepare(`SELECT p.id, p.tripId, p.name, p.description, p.city, p.categoryId,
    p.tentativeDate, p.createdAt, p.updatedAt,
    c.name AS categoryName, c.description AS categoryDescription
    FROM places p JOIN categories c ON c.id = p.categoryId
    WHERE p.tripId = ? ORDER BY p.tentativeDate ASC`).all(trip.id)
    .map(place => ({ ...place, category: { id: place.categoryId, name: place.categoryName, description: place.categoryDescription }, categoryName: undefined, categoryDescription: undefined }));
  res.json({ ...trip, places, placesCount: places.length });
});

module.exports = router;
