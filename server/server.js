const http = require('http');
const { URL } = require('url');

const PORT = process.env.PORT || 3000;
const ALLOWED_ORIGINS = [
  'https://ute-5a-proyecto1.netlify.app',
  'https://ute-5a-proyecto1.netlify.app/',
  'https://apifamily-production.up.railway.app',
  'https://zblue707.github.io',
];

const db = {
  noticias: [
    {
      id: 1,
      titulo: 'Cumpleanos de Abdiel',
      descripcion: 'Se acerca el cumpleanos',
      fecha: '30/04/2026',
      tipo: 'Cumpleanos',
    },
  ],
  familia: [
    {
      id: 1,
      nombre: 'Abdiel Isai',
      apellido: 'Mendoza Martinez',
      fecha_nac: '30/04/2007',
      signo_zod: 'Tauro',
    },
    {
      id: 2,
      nombre: 'Jesus Alfonso',
      apellido: 'Mendoza Martinez',
      fecha_nac: '16/09/2003',
      signo_zod: 'Virgo',
    },
    {
      id: 3,
      nombre: 'Eunice Sarahy',
      apellido: 'Mendoza Martinez',
      fecha_nac: '23/05/2002',
      signo_zod: '',
    },
    {
      id: 4,
      nombre: 'Maria de Jesus',
      apellido: 'Martinez Rincon',
      fecha_nac: '22/02/1971',
      signo_zod: '',
    },
    {
      id: 5,
      nombre: 'Alfonso',
      apellido: 'Mendoza Mendez',
      fecha_nac: '31/01/1974',
      signo_zod: '',
    },
  ],
  acercade: [
    {
      id: 1,
      titulo: 'Cumpleanos',
      descripcion: 'Se acerca el cumpleanos de Abdiel y Eunice.',
    },
    {
      id: 2,
      titulo: 'Nueva mascota familiar',
      descripcion:
        'Un periquito llego a la casa, le compramos una jaula con comida, agua y juguetes.',
    },
  ],
};

function setCorsHeaders(req, res) {
  const origin = req.headers.origin;

  if (origin && ALLOWED_ORIGINS.includes(origin)) {
    res.setHeader('Access-Control-Allow-Origin', origin);
  }

  res.setHeader('Vary', 'Origin');
  res.setHeader('Access-Control-Allow-Methods', 'GET,POST,PUT,DELETE,PATCH,OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');
}

function sendJson(req, res, statusCode, payload) {
  setCorsHeaders(req, res);
  res.writeHead(statusCode, {
    'Content-Type': 'application/json; charset=utf-8',
  });
  res.end(JSON.stringify(payload));
}

function collectBody(req) {
  return new Promise((resolve, reject) => {
    let body = '';
    req.on('data', (chunk) => {
      body += chunk;
    });
    req.on('end', () => {
      if (!body) return resolve({});
      try {
        resolve(JSON.parse(body));
      } catch (error) {
        reject(new Error('JSON invalido'));
      }
    });
    req.on('error', reject);
  });
}

function resolveCollection(pathname) {
  const match = pathname.match(/^\/api\/(noticias|familia|acercade)(?:\/(\d+))?$/);
  if (!match) return null;
  return {
    name: match[1],
    id: match[2] ? Number(match[2]) : null,
  };
}

function getNextId(items) {
  if (items.length === 0) return 1;
  return Math.max(...items.map((item) => item.id || 0)) + 1;
}

const server = http.createServer(async (req, res) => {
  if (req.method === 'OPTIONS') {
    setCorsHeaders(req, res);
    res.writeHead(204);
    return res.end();
  }

  const { pathname } = new URL(req.url, `http://localhost:${PORT}`);

  if (pathname === '/api/health' && req.method === 'GET') {
    return sendJson(req, res, 200, { ok: true, date: new Date().toISOString() });
  }

  const target = resolveCollection(pathname);
  if (!target) {
    return sendJson(req, res, 404, { error: 'Ruta no encontrada' });
  }

  const items = db[target.name];
  const itemIndex = items.findIndex((item) => item.id === target.id);

  if (req.method === 'GET') {
    if (target.id === null) return sendJson(req, res, 200, items);
    if (itemIndex === -1) return sendJson(req, res, 404, { error: 'Elemento no encontrado' });
    return sendJson(req, res, 200, items[itemIndex]);
  }

  if (req.method === 'POST') {
    try {
      const body = await collectBody(req);
      const newItem = { ...body, id: getNextId(items) };
      items.push(newItem);
      return sendJson(req, res, 201, newItem);
    } catch (error) {
      return sendJson(req, res, 400, { error: error.message });
    }
  }

  if (req.method === 'PUT') {
    if (target.id === null || itemIndex === -1) {
      return sendJson(req, res, 404, { error: 'Elemento no encontrado' });
    }

    try {
      const body = await collectBody(req);
      items[itemIndex] = { ...items[itemIndex], ...body, id: target.id };
      return sendJson(req, res, 200, items[itemIndex]);
    } catch (error) {
      return sendJson(req, res, 400, { error: error.message });
    }
  }

  if (req.method === 'DELETE') {
    if (target.id === null || itemIndex === -1) {
      return sendJson(req, res, 404, { error: 'Elemento no encontrado' });
    }
    const [removed] = items.splice(itemIndex, 1);
    return sendJson(req, res, 200, removed);
  }

  return sendJson(req, res, 405, { error: 'Metodo no permitido' });
});

server.listen(PORT, () => {
  console.log(`API lista en http://localhost:${PORT}`);
});
