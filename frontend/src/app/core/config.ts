import { runtimeConfig } from './config.generated';

export const apiUrl = runtimeConfig.apiUrl.replace(/\/$/, '');
export const authUrl = runtimeConfig.authUrl.replace(/\/$/, '');
