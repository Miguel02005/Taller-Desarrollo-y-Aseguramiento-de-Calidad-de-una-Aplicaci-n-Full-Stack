import api from './api.js';

/**
 * @returns {Promise<Array>} Lista de libros favoritos
 */
export async function getFavorites() {
    return api('/api/favorites');
}

/**
 * @param {string} key - Clave del libro en OpenLibrary (ej: "/works/OL12345W")
 * @returns {Promise<Object>} Libro agregado como favorito
 */
export async function addFavorite(key) {
    return api(`/api/favorites?key=${encodeURIComponent(key)}`, {
        method: 'POST',
    });
}

/**
 * @param {number} id - ID del favorito a eliminar
 * @returns {Promise<null>}
 */
export async function removeFavorite(id) {
    return api(`/api/favorites/${id}`, {
        method: 'DELETE',
    });
}

export default { getFavorites, addFavorite, removeFavorite };