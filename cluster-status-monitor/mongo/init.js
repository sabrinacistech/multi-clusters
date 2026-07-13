// =====================================================================
// Inicializacion MongoDB - Cluster Status Monitor (WRITER)
// SDD 13.3.1 (colecciones + indices) y 13.3.3 (usuarios).
// Ejecutar con: mongosh <uri> mongo/init.js
// =====================================================================

// ---------- Base compartida: estados y alias ----------
const shared = db.getSiblingDB('paas_shared');

// app_cluster_status
shared.createCollection('app_cluster_status');
shared.app_cluster_status.createIndex({ codigo: 1 }, { name: 'ix_codigo' });
shared.app_cluster_status.createIndex(
  { codigo: 1, app: 1, cluster: 1 },
  { name: 'ux_codigo_app_cluster', unique: true }
);

// alias_status
shared.createCollection('alias_status');
shared.alias_status.createIndex({ alias: 1 }, { name: 'ux_alias', unique: true });

// ---------- Base de cache: TTL ----------
const cache = db.getSiblingDB('paas_cache');

cache.createCollection('app_status_cache');
cache.app_status_cache.createIndex({ cacheKey: 1 }, { name: 'ux_cacheKey', unique: true });
// TTL: el documento se elimina en cuanto expiresAt queda en el pasado.
cache.app_status_cache.createIndex({ expiresAt: 1 }, { name: 'ttl_expiresAt', expireAfterSeconds: 0 });

// ---------- Usuarios (13.3.3) ----------
// acs_reader: solo lectura (usado por el READER active-cluster-service).
shared.createUser({
  user: 'acs_reader',
  pwd: 'CHANGEME_READER',
  roles: [
    { role: 'read', db: 'paas_shared' },
    { role: 'read', db: 'paas_cache' }
  ]
});

// csm_writer: lectura/escritura (usado por este WRITER).
shared.createUser({
  user: 'csm_writer',
  pwd: 'CHANGEME_WRITER',
  roles: [
    { role: 'readWrite', db: 'paas_shared' },
    { role: 'readWrite', db: 'paas_cache' }
  ]
});

print('Inicializacion CSM completada.');
