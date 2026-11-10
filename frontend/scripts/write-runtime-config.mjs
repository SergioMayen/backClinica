import { writeFile } from 'node:fs/promises';

const file = new URL('../src/app/core/config.generated.ts', import.meta.url);
const apiUrl = process.env.API_URL || '/api';
const authUrl = process.env.AUTH_URL || '/auth/api';
const turnstileSiteKey = process.env.TURNSTILE_SITE_KEY || '';
await writeFile(file, `export const runtimeConfig = ${JSON.stringify({ apiUrl, authUrl, turnstileSiteKey })} as const;\n`);
