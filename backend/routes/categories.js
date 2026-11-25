const express = require('express');
const { db } = require('../config/database');
const router = express.Router();

/** @openapi
 * /api/categories:
 *   get:
 *     summary: Consultar las categorías disponibles para los lugares
 *     tags: [Categorías]
 *     responses:
 *       200:
 *         description: Lista de categorías almacenadas en SQLite
 */
router.get('/', (req, res) => {
  const categories = db.prepare('SELECT id, name, description FROM categories ORDER BY name ASC').all();
  res.json(categories);
});

module.exports = router;
