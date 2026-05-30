package edu.unac.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.unac.domain.FavoriteBook;
import edu.unac.dto.FavoriteBookDTO;
import edu.unac.external.OpenLibraryClient;
import edu.unac.repository.FavoriteBookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteBookService {

    @Autowired
    private FavoriteBookRepository repository;

    @Autowired
    private OpenLibraryClient client;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Obtiene todos los favoritos como DTOs
     */
    public List<FavoriteBookDTO> getAllFavoritesDTO() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Versión original que retorna la entidad (mantenida por compatibilidad)
     */
    public List<FavoriteBook> getAllFavorites() {
        return repository.findAll();
    }

    /**
     * Guarda un favorito y retorna el DTO
     */
    public FavoriteBookDTO saveFavoriteDTO(String key) {
        FavoriteBook favorite = saveFavorite(key);
        return toDTO(favorite);
    }

    /**
     * Versión original que retorna la entidad (mantenida por compatibilidad)
     */
    public FavoriteBook saveFavorite(String key) {

        try {

            String response = client.getBookByKey(key);

            JsonNode book = mapper.readTree(response);

            FavoriteBook favorite = new FavoriteBook();

            favorite.setBookKey(key);

            favorite.setTitle(
                    book.path("title").asText("Título desconocido")
            );

            // Obtener autor desde authors
            JsonNode authors = book.path("authors");
            if (authors != null && authors.isArray() && !authors.isEmpty()) {
                JsonNode firstAuthor = authors.get(0);
                if (firstAuthor.has("key")) {
                    // Intentar obtener nombre del autor
                    try {
                        String authorResponse = client.getBookByKey(firstAuthor.get("key").asText());
                        JsonNode authorData = mapper.readTree(authorResponse);
                        favorite.setAuthor(authorData.path("name").asText("Autor desconocido"));
                    } catch (Exception e) {
                        favorite.setAuthor("Autor desconocido");
                    }
                }
            } else {
                favorite.setAuthor("Autor desconocido");
            }

            // Manejar caso donde covers es null o vacío
            JsonNode covers = book.path("covers");
            if (covers != null && covers.isArray() && covers.size() > 0) {
                favorite.setCoverUrl(
                        "https://covers.openlibrary.org/b/id/" +
                                covers.get(0).asInt() +
                                "-M.jpg"
                );
            }

            return repository.save(favorite);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error saving favorite book: " + e.getMessage()
            );
        }
    }

    public void deleteFavorite(Long id) {
        repository.deleteById(id);
    }

    /**
     * Convierte una entidad FavoriteBook a DTO
     */
    private FavoriteBookDTO toDTO(FavoriteBook favorite) {
        return new FavoriteBookDTO(
                favorite.getId(),
                favorite.getBookKey(),
                favorite.getTitle(),
                favorite.getAuthor(),
                favorite.getPublishYear(),
                favorite.getCoverUrl()
        );
    }
}