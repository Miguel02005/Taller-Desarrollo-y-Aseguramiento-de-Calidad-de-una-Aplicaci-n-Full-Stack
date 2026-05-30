package edu.unac.service;

import edu.unac.domain.SearchHistory;
import edu.unac.dto.SearchHistoryDTO;
import edu.unac.repository.SearchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchHistoryService {

    @Autowired
    private SearchHistoryRepository repository;

    /**
     * Obtiene todos los historiales de búsqueda como DTOs
     */
    public List<SearchHistoryDTO> getAllSearchesDTO() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Versión original que retorna la entidad (mantenida por compatibilidad)
     */
    public List<SearchHistory> getAllSearches() {
        return repository.findAll();
    }

    /**
     * Limpia todo el historial de búsquedas
     */
    public void clearAllSearches() {
        repository.deleteAll();
    }

    public SearchHistory saveSearch(
            String title,
            String author,
            String language,
            Integer publishedAfter
    ) {

        SearchHistory search = new SearchHistory();

        search.setTitle(title);
        search.setAuthor(author);
        search.setLanguage(language);
        search.setPublishedAfter(publishedAfter);
        search.setSearchDate(LocalDateTime.now());

        return repository.save(search);
    }

    /**
     * Convierte una entidad SearchHistory a DTO
     */
    private SearchHistoryDTO toDTO(SearchHistory search) {
        return new SearchHistoryDTO(
                search.getId(),
                search.getTitle(),
                search.getAuthor(),
                search.getLanguage(),
                search.getPublishedAfter(),
                search.getSearchDate()
        );
    }
}