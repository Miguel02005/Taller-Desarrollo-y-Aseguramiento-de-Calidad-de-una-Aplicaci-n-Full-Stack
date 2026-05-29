const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

/**
 * @param {string} endpoint - Endpoint de la API (ej: '/api/books/search')
 * @param {object} options - Opciones de fetch (method, headers, body, etc.)
 * @returns {Promise<any>} - Respuesta del servidor en formato JSON o null para respuestas vacías
 */
async function apiRequest(endpoint, options = {}) {
    const path = endpoint.startsWith('/') ? endpoint : `/${endpoint}`;
    const url = `${API_BASE_URL}${path}`;

    const defaultHeaders = {
        'Content-Type': 'application/json',
    };

    const config = {
        ...options,
        headers: {
            ...defaultHeaders,
            ...options.headers,
        },
    };

    try {
        const response = await fetch(url, config);

        if (!response.ok) {
            let errorData;
            try {
                errorData = await response.json();
            } catch {
                errorData = { error: `HTTP ${response.status}: ${response.statusText}` };
            }
            throw new Error(errorData.error || `HTTP ${response.status}: ${response.statusText}`);
        }

        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            return await response.json();
        }
        return null;

    } catch (error) {
        console.error(`API Error [${path}]:`, error.message);
        throw error;
    }
}

export default apiRequest;
export { API_BASE_URL };