# API Planificador de viajes

Backend REST para la aplicación de planificación de viajes, desarrollado con **Node.js, Express y SQLite**.

La base de datos es un archivo local (`data/viajes.db`). No requiere instalar MongoDB, SQLite Server ni configurar otro servicio. Se utiliza `node:sqlite`, incluido en Node.js.

## 1. Requisitos

- Node.js **22.13.0 o superior**.
- npm.

Comprueba las versiones:

```bash
node --version
npm --version
```

## 2. Instalación y ejecución

Descomprime el proyecto y abre una terminal en la carpeta `viajes_api`:

```bash
npm install
npm start
```

La base de datos y sus tablas se crean automáticamente en el primer inicio. El catálogo inicial de categorías también se inserta automáticamente.

Para cargar los datos de demostración (viaje Europa 2026 y tres lugares):

```bash
npm run seed
```

**Atención:** `npm run seed` elimina los viajes y lugares existentes y vuelve a cargar los datos de ejemplo. No lo ejecutes si deseas conservar los registros.

- API: http://localhost:3000
- Swagger: http://localhost:3000/api-docs
- Estado: http://localhost:3000/api/health

## 3. Estructura de datos

La información se almacena en tres tablas relacionadas:

- **trips**: contiene los datos propios de cada viaje.
- **categories**: catálogo independiente de categorías (`id`, `name`, `description`).
- **places**: contiene los lugares y almacena `tripId` y `categoryId` como claves foráneas.

Cada lugar guarda el identificador numérico de la categoría seleccionada. Las relaciones entre tablas se controlan mediante claves foráneas de SQLite.

Relaciones:

```text
trips (1) -------- (N) places (N) -------- (1) categories
```

`config/categories.js` contiene los datos iniciales usados para poblar la tabla `categories`. En las consultas, el catálogo se lee desde SQLite.

## 4. Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/trips` | Consultar todos los viajes |
| POST | `/api/trips` | Guardar un viaje |
| GET | `/api/trips/:id` | Consultar el detalle de un viaje y sus lugares |
| GET | `/api/places?tripId=:id` | Consultar lugares, opcionalmente filtrados por viaje |
| POST | `/api/places` | Crear un lugar para un viaje |
| GET | `/api/categories` | Consultar categorías desde SQLite |
| GET | `/api/health` | Verificar el estado de la API |

## 5. Ejemplos de consumo

### Crear viaje — `POST /api/trips`

```json
{
  "name": "Europa 2026",
  "destination": "Italia",
  "startDate": "2026-06-10",
  "endDate": "2026-06-20"
}
```

La respuesta incluye el `id` generado para el viaje. Se utiliza como `tripId` al registrar lugares.

### Consultar categorías — `GET /api/categories`

Respuesta de ejemplo:

```json
[
  { "id": 1, "name": "Histórico", "description": "Monumentos, sitios y patrimonio histórico." },
  { "id": 2, "name": "Cultural", "description": "Museos, teatros, galerías y expresiones culturales." }
]
```

La API devuelve todas las categorías existentes en la tabla `categories`.

### Crear lugar — `POST /api/places`

```json
{
  "tripId": "ID_DEVUELTO_AL_CREAR_EL_VIAJE",
  "name": "Coliseo Romano",
  "description": "Anfiteatro romano, uno de los principales atractivos de Roma.",
  "city": "Roma",
  "categoryId": 1,
  "tentativeDate": "2026-06-12"
}
```

El cliente debe consultar primero `/api/categories` y enviar el `id` numérico de la categoría seleccionada. La respuesta del lugar incluye `categoryId` y el objeto `category` con sus datos descriptivos.

### Consultar detalle de viaje — `GET /api/trips/:id`

Devuelve el viaje, el arreglo `places` con sus lugares y `placesCount`. Cada lugar incluye su categoría relacionada.

## 6. Validaciones

- Todos los campos requeridos deben estar presentes.
- La fecha final del viaje no puede ser anterior a la fecha inicial.
- El lugar debe estar asociado a un viaje existente.
- `categoryId` debe ser un número entero y existir en la tabla `categories`.
- La fecha tentativa del lugar debe estar dentro del período del viaje.
- SQLite refuerza las relaciones mediante claves foráneas.

## 7. Estructura

```text
viajes_api/
├── config/
│   ├── categories.js  # Datos iniciales del catálogo
│   └── database.js    # Conexión, esquema y migración SQLite
├── routes/
│   ├── categories.js
│   ├── places.js
│   └── trips.js
├── data/              # Se crea automáticamente
│   └── viajes.db      # Base de datos local
├── package.json
├── seed.js
├── server.js
└── utils.js
```

## 8. Conexión desde Android

- Emulador Android: `http://10.0.2.2:3000`.
- Teléfono físico: utiliza la IP local del computador donde corre Node.js; ambos dispositivos deben estar en la misma red Wi-Fi.

La API está preparada para consumirse desde Android con Kotlin, Retrofit y MVVM.
