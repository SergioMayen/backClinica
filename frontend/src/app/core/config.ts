declare global {
  interface Window { __CLINICA_CONFIG__?: { apiUrl?: string; authUrl?: string; } }
}

const config = window.__CLINICA_CONFIG__ ?? {};
export const apiUrl = (config.apiUrl || '/api').replace(/\/$/, '');
export const authUrl = (config.authUrl || '/auth/api').replace(/\/$/, '');

export {};
