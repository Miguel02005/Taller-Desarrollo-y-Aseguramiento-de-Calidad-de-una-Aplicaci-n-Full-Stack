package edu.unac.service;

import edu.unac.domain.SearchHistory;
import edu.unac.repository.SearchHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchHistoryServiceTest {

    private SearchHistoryRepository repository;

    private SearchHistoryService service;

    @BeforeEach
    void setUp() {

        repository = Mockito.mock(SearchHistoryRepository.class);

        service = new SearchHistoryService();

        try {
            var field = SearchHistoryService.class.getDeclaredField("repository");
            field.setAccessible(true);
            field.set(service, repository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnAllSearches() {

        SearchHistory search1 = new SearchHistory();
        search1.setTitle("Clean Code");

        SearchHistory search2 = new SearchHistory();
        search2.setTitle("Design Patterns");

        when(repository.findAll())
                .thenReturn(List.of(search1, search2));

        List<SearchHistory> result = service.getAllSearches();

        assertEquals(2, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());

        verify(repository).findAll();
    }

    @Test
    void shouldDeleteAllSearches() {

        service.clearAllSearches();

        verify(repository).deleteAll();
    }

    @Test
    void shouldSaveSearch() {

        SearchHistory saved = new SearchHistory();
        saved.setTitle("Java");
        saved.setAuthor("Oracle");

        when(repository.save(any(SearchHistory.class)))
                .thenReturn(saved);

        SearchHistory result = service.saveSearch(
                "Java",
                "Oracle",
                "english",
                2020
        );

        assertNotNull(result);
        assertEquals("Java", result.getTitle());
        assertEquals("Oracle", result.getAuthor());

        verify(repository).save(any(SearchHistory.class));
    }

    @Test
    void shouldSetSearchDateWhenSavingSearch() {

        SearchHistory saved = new SearchHistory();

        when(repository.save(any(SearchHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SearchHistory result = service.saveSearch(
                "Spring Boot",
                "Craig Walls",
                "english",
                2022
        );

        assertNotNull(result.getSearchDate());
        assertEquals("Spring Boot", result.getTitle());
        assertEquals("Craig Walls", result.getAuthor());
        assertEquals("english", result.getLanguage());
        assertEquals(2022, result.getPublishedAfter());
    }
}