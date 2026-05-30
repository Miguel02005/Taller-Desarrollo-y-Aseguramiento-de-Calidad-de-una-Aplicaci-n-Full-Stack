package edu.unac.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.unac.dto.BookResponseDTO;
import edu.unac.exception.InvalidSearchException;
import edu.unac.external.OpenLibraryClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    @Autowired
    private OpenLibraryClient client;

    @Autowired
    private SearchHistoryService historyService;

    private final ObjectMapper mapper = new ObjectMapper();

    public List<BookResponseDTO> searchBooks(
            String title,
            String author,
            String language,
            Integer publishedAfter
    ) {

        validateSearch(title, author, language, publishedAfter);

        historyService.saveSearch(title, author, language, publishedAfter);

        String response = client.searchBooks(title, author);

        try {

            JsonNode root = mapper.readTree(response);

            JsonNode docs = root.get("docs");

            if (docs == null || !docs.isArray()) {
                return List.of();
            }

            List<BookResponseDTO> results = new ArrayList<>();

            for (JsonNode book : docs) {

                BookResponseDTO dto = mapToDTO(book);

                // Filtrar por idioma si se especifica
                if (language != null && !language.isBlank()) {
                    String bookLang = book.path("language").asText("");
                    if (!bookLang.isEmpty() && !bookLang.toLowerCase().contains(language.toLowerCase())) {
                        continue; // Saltar este libro si no coincide el idioma
                    }
                }

                // Filtrar por año de publicación si se especifica
                if (publishedAfter != null && dto.getPublishYear() != null) {
                    if (dto.getPublishYear() < publishedAfter) {
                        continue; // Saltar este libro si es anterior
                    }
                }

                results.add(dto);
            }

            return results.stream()
                    .limit(10)
                    .toList();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Error processing book search: " + e.getMessage()
            );
        }
    }

    private BookResponseDTO mapToDTO(JsonNode book) {

        // Extraer la workKey de OpenLibrary (ej: "/works/OL12345W")
        String workKey = book.path("key").asText(null);
        // Generar ID único basado en la workKey
        String id = workKey != null ? UUID.nameUUIDFromBytes(workKey.getBytes()).toString() : UUID.randomUUID().toString();

        String title = book.path("title").asText(null);

        // author_name es un array, tomamos el primero
        String author = null;
        JsonNode authorNode = book.get("author_name");
        if (authorNode != null && !authorNode.isEmpty()) {
            author = authorNode.get(0).asText();
        }

        // Año de publicación más antiguo (primera edición)
        Integer publishYear = null;
        JsonNode publishYears = book.get("publish_year");
        if (publishYears != null && !publishYears.isEmpty()) {
            int minYear = Integer.MAX_VALUE;
            for (JsonNode y : publishYears) {
                int yr = y.asInt();
                if (yr < minYear) minYear = yr;
            }
            publishYear = minYear;
        }

        // Número de ediciones
        Integer editions = book.path("edition_count").asInt(0);

        // Imagen de portada usando cover_i (cover id)
        String coverUrl = null;
        JsonNode coverId = book.get("cover_i");
        if (coverId != null && !coverId.isNull()) {
            coverUrl = "https://covers.openlibrary.org/b/id/" + coverId.asInt() + "-M.jpg";
        }

        return new BookResponseDTO(workKey, id, title, author, publishYear, editions, coverUrl);
    }

    private void validateSearch(
            String title,
            String author,
            String language,
            Integer publishedAfter
    ) {

        if ((title == null || title.isBlank()) &&
                (author == null || author.isBlank())) {

            throw new InvalidSearchException(
                    "Title or author is required."
            );
        }

        if (publishedAfter != null) {
            int currentYear = Year.now().getValue();
            if (publishedAfter > currentYear) {
                throw new InvalidSearchException(
                        "publishedAfter cannot be greater than current year."
                );
            }
        }

        List<String> validLanguages = List.of(
                "english", "spanish", "portuguese", "french", "german"
        );

        if (language != null &&
                !language.isBlank() &&
                !validLanguages.contains(language.toLowerCase())) {
            throw new InvalidSearchException("Invalid language. Valid: english, spanish, portuguese, french, german");
        }
    }
}