// Hook simple para que BookCard verifique si un libro es favorito
// NO hace llamadas HTTP - usa el estado global de FavoritesContext
import { useFavorites } from '../context/FavoritesContext.jsx';

export function useBookFavorite(workKey) {
    const { isFavorite, getFavoriteId, favorites } = useFavorites();

    // Retorna info del favorito para este libro específico
    return {
        isFav: isFavorite(workKey),
        favoriteId: getFavoriteId(workKey),
        // Contador de favoritos para detectar cambios
        favoriteCount: favorites.length,
    };
}

export default useBookFavorite;