import api from './api.js';

/**
 * @param {object} params - Parámetros de búsqueda
 * @param {string} [params.title] - Título del libro
 * @param {string} [params.author] - Autor del libro
 * @param {string} [params.language] - Idioma (english, spanish, etc.)
 * @param {number} [params.publishedAfter] - Año mínimo de publicación
 * @returns {Promise<Array>} Lista de libros encontrados
 */
export async function searchBooks({ title, author, language, publishedAfter } = {}) {
    const params = new URLSearchParams();
    if (title) params.append('title', title);
    if (author) params.append('author', author);
    if (language) params.append('language', language);
    if (publishedAfter) params.append('publishedAfter', publishedAfter);

    console.log('[bookService] Request URL:', `/api/books/search?${params.toString()}`);

    const result = await api(`/api/books/search?${params.toString()}`);

    console.log('[bookService] Raw response from API:', result);
    console.log('[bookService] Response type:', typeof result);
    console.log('[bookService] Is array:', Array.isArray(result));

    return result;
}

export default { searchBooks };