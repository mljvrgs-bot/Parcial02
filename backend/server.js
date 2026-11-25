const express = require('express');
const cors = require('cors');
const swaggerUi = require('swagger-ui-express');
const swaggerJsdoc = require('swagger-jsdoc');
const { databasePath } = require('./config/database');

const tripsRouter = require('./routes/trips');
const placesRouter = require('./routes/places');
const categoriesRouter = require('./routes/categories');

const app = express();
app.use(cors());
app.use(express.json());

const swaggerSpec = swaggerJsdoc({
  definition: {
    openapi: '3.0.0',
    info: { title: 'API Planificador de viajes', version: '2.0.0', description: 'API REST para gestionar viajes y lugares por visitar. Almacenamiento local SQLite.' },
    servers: [{ url: 'http://localhost:3000' }]
  },
  apis: ['./routes/*.js']
});
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerSpec));
app.get('/api/health', (req, res) => res.json({ status: 'ok', service: 'api-planificador-viajes', database: 'SQLite' }));
app.use('/api/trips', tripsRouter);
app.use('/api/places', placesRouter);
app.use('/api/categories', categoriesRouter);
app.use((req, res) => res.status(404).json({ message: 'Endpoint no encontrado.' }));
app.use((error, req, res, next) => {
  console.error(error);
  res.status(500).json({ message: 'Error interno del servidor.' });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`API disponible en http://localhost:${PORT}`);
  console.log(`Base de datos SQLite: ${databasePath}`);
});
