package edu.unac.repository;

import edu.unac.domain.SearchHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SearchHistoryRepositoryTest {

    @Autowired
    private SearchHistoryRepository repository;

    @Test
    void shouldSaveAndFindById() {
        SearchHistory search = new SearchHistory();
        search.setTitle("Clean Code");
        search.setAuthor("Robert Martin");
        search.setSearchDate(LocalDateTime.now());
        SearchHistory saved = repository.save(search);
        Optional<SearchHistory> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Clean Code", found.get().getTitle());
    }

    @Test
    void shouldReturnAllSearches() {
        SearchHistory s1 = new SearchHistory();
        s1.setTitle("Java");
        s1.setSearchDate(LocalDateTime.now());
        SearchHistory s2 = new SearchHistory();
        s2.setTitle("Spring Boot");
        s2.setSearchDate(LocalDateTime.now());
        repository.save(s1);
        repository.save(s2);
        assertEquals(2, repository.findAll().size());
    }

    @Test
    void shouldDeleteAll() {
        SearchHistory s1 = new SearchHistory();
        s1.setTitle("Java");
        s1.setSearchDate(LocalDateTime.now());
        repository.save(s1);
        repository.deleteAll();
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNoSearches() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldPersistAllFields() {
        SearchHistory search = new SearchHistory();
        search.setTitle("Design Patterns");
        search.setAuthor("Gang of Four");
        search.setLanguage("english");
        search.setPublishedAfter(1994);
        search.setSearchDate(LocalDateTime.now());
        SearchHistory saved = repository.save(search);
        assertEquals("Design Patterns", saved.getTitle());
        assertEquals("english", saved.getLanguage());
        assertEquals(1994, saved.getPublishedAfter());
    }

    @Test
    void shouldGenerateIdAutomatically() {
        SearchHistory search = new SearchHistory();
        search.setTitle("Auto ID");
        search.setSearchDate(LocalDateTime.now());
        assertNotNull(repository.save(search).getId());
    }

    @Test
    void shouldDeleteById() {
        SearchHistory search = new SearchHistory();
        search.setTitle("To Delete");
        search.setSearchDate(LocalDateTime.now());
        SearchHistory saved = repository.save(search);
        repository.deleteById(saved.getId());
        assertFalse(repository.findById(saved.getId()).isPresent());
    }
}