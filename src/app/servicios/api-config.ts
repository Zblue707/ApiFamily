declare global {
  interface Window {
    __API_BASE_URL__?: string;
  }
}

const RAILWAY_API_BASE_URL = 'https://apifamily-production.up.railway.app';

function normalizeUrl(url: string): string {
  return url.replace(/\/+$/, '');
}

function isLocalHostname(hostname: string): boolean {
  return (
    hostname === 'localhost' ||
    hostname === '127.0.0.1' ||
    hostname === '0.0.0.0' ||
    hostname.endsWith('.local') ||
    /^10\./.test(hostname) ||
    /^192\.168\./.test(hostname) ||
    /^172\.(1[6-9]|2\d|3[0-1])\./.test(hostname)
  );
}

function resolveApiBaseUrl(): string {
  if (typeof window === 'undefined') {
    return 'http://localhost:8080';
  }

  if (window.__API_BASE_URL__) {
    return normalizeUrl(window.__API_BASE_URL__);
  }

  const { hostname, protocol } = window.location;
  if (isLocalHostname(hostname)) {
    const apiProtocol = protocol === 'https:' ? 'https:' : 'http:';
    const apiHost = hostname || 'localhost';

    return `${apiProtocol}//${apiHost}:8080`;
  }

  return RAILWAY_API_BASE_URL;
}

export const API_BASE_URL = resolveApiBaseUrl();
