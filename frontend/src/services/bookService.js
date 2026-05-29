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

    return api(`/api/books/search?${params.toString()}`);
}

export default { searchBooks };