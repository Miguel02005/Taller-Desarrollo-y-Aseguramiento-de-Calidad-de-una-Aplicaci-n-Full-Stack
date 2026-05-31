package edu.unac.repository;

import edu.unac.domain.FavoriteBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FavoriteBookRepositoryTest {

    @Autowired
    private FavoriteBookRepository repository;

    @Test
    void shouldSaveAndFindById() {
        FavoriteBook book = new FavoriteBook();
        book.setBookKey("/works/OL1W");
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");
        FavoriteBook saved = repository.save(book);
        Optional<FavoriteBook> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Clean Code", found.get().getTitle());
    }

    @Test
    void shouldFindByBookKey() {
        FavoriteBook book = new FavoriteBook();
        book.setBookKey("/works/OL999W");
        book.setTitle("Design Patterns");
        repository.save(book);
        Optional<FavoriteBook> found = repository.findByBookKey("/works/OL999W");
        assertTrue(found.isPresent());
        assertEquals("Design Patterns", found.get().getTitle());
    }

    @Test
    void shouldReturnEmptyWhenBookKeyNotFound() {
        Optional<FavoriteBook> found = repository.findByBookKey("/works/NOTEXIST");
        assertFalse(found.isPresent());
    }

    @Test
    void shouldReturnAllFavorites() {
        FavoriteBook book1 = new FavoriteBook();
        book1.setBookKey("/works/OL1W");
        book1.setTitle("Book One");
        FavoriteBook book2 = new FavoriteBook();
        book2.setBookKey("/works/OL2W");
        book2.setTitle("Book Two");
        repository.save(book1);
        repository.save(book2);
        List<FavoriteBook> all = repository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void shouldDeleteById() {
        FavoriteBook book = new FavoriteBook();
        book.setBookKey("/works/OL5W");
        book.setTitle("To Delete");
        FavoriteBook saved = repository.save(book);
        repository.deleteById(saved.getId());
        assertFalse(repository.findById(saved.getId()).isPresent());
    }

    @Test
    void shouldReturnEmptyListWhenNoFavorites() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldPersistAllFields() {
        FavoriteBook book = new FavoriteBook();
        book.setBookKey("/works/OL10W");
        book.setTitle("Refactoring");
        book.setAuthor("Martin Fowler");
        book.setPublishYear(1999);
        book.setEditions(3);
        book.setCoverUrl("https://covers.openlibrary.org/b/id/999-M.jpg");
        FavoriteBook saved = repository.save(book);
        assertEquals("Refactoring", saved.getTitle());
        assertEquals("Martin Fowler", saved.getAuthor());
        assertEquals(1999, saved.getPublishYear());
    }

    @Test
    void shouldGenerateIdAutomatically() {
        FavoriteBook book = new FavoriteBook();
        book.setBookKey("/works/OL20W");
        book.setTitle("Auto ID Book");
        FavoriteBook saved = repository.save(book);
        assertNotNull(saved.getId());
    }
}