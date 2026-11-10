import { mkdir, writeFile } from 'node:fs/promises';

const file = new URL('../public/runtime-config.js', import.meta.url);
const apiUrl = process.env.API_URL || '/api';
const authUrl = process.env.AUTH_URL || '/auth/api';
await mkdir(new URL('../public/', import.meta.url), { recursive: true });
await writeFile(file, `window.__CLINICA_CONFIG__ = ${JSON.stringify({ apiUrl, authUrl })};\n`);
