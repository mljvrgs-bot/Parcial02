const { randomUUID } = require('node:crypto');

function isValidDate(value) {
  return typeof value === 'string' && /^\d{4}-\d{2}-\d{2}$/.test(value)
    && !Number.isNaN(Date.parse(`${value}T00:00:00Z`))
    && new Date(`${value}T00:00:00Z`).toISOString().slice(0, 10) === value;
}

function now() {
  return new Date().toISOString();
}

function newId() {
  return randomUUID();
}

module.exports = { isValidDate, now, newId };
