declare global {
  interface Window {
    __API_BASE_URL__?: string;
  }
}

function resolveApiBaseUrl(): string {
  if (typeof window === 'undefined') {
    return 'http://localhost:8080';
  }

  if (window.__API_BASE_URL__) {
    return window.__API_BASE_URL__;
  }

  const { hostname, protocol } = window.location;
  const apiProtocol = protocol === 'https:' ? 'https:' : 'http:';
  const apiHost = hostname || 'localhost';

  return `${apiProtocol}//${apiHost}:8080`;
}

export const API_BASE_URL = resolveApiBaseUrl();
