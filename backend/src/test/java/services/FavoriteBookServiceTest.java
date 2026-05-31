package services;

import edu.unac.domain.FavoriteBook;
import edu.unac.external.OpenLibraryClient;
import edu.unac.repository.FavoriteBookRepository;
import edu.unac.service.FavoriteBookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteBookServiceTest {

    @Mock
    private FavoriteBookRepository repository;

    @Mock
    private OpenLibraryClient client;

    @InjectMocks
    private FavoriteBookService service;

    @Test
    void shouldReturnAllFavorites() {

        FavoriteBook book = new FavoriteBook();
        book.setTitle("Clean Code");

        when(repository.findAll())
                .thenReturn(List.of(book));

        List<FavoriteBook> result = service.getAllFavorites();

        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());

        verify(repository).findAll();
    }

    @Test
    void shouldSaveFavoriteBook() {

        String bookJson = """
            {
                "title": "Clean Code",
                "authors": [
                    {
                        "key": "/authors/OL1A"
                    }
                ],
                "covers": [12345]
            }
            """;

        String authorJson = """
            {
                "name": "Robert C. Martin"
            }
            """;

        when(client.getBookByKey("/works/OL123W"))
                .thenReturn(bookJson);

        when(client.getBookByKey("/authors/OL1A"))
                .thenReturn(authorJson);

        when(repository.save(any(FavoriteBook.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FavoriteBook result =
                service.saveFavorite("/works/OL123W");

        assertEquals("/works/OL123W", result.getBookKey());
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert C. Martin", result.getAuthor());

        verify(repository).save(any(FavoriteBook.class));
    }

    @Test
    void shouldUseUnknownAuthorWhenAuthorLookupFails() {

        String bookJson = """
            {
                "title": "Clean Code",
                "authors": [
                    {
                        "key": "/authors/OL1A"
                    }
                ]
            }
            """;

        when(client.getBookByKey("/works/OL123W"))
                .thenReturn(bookJson);

        when(client.getBookByKey("/authors/OL1A"))
                .thenThrow(new RuntimeException("Author API error"));

        when(repository.save(any(FavoriteBook.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FavoriteBook result =
                service.saveFavorite("/works/OL123W");

        assertEquals("Autor desconocido", result.getAuthor());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenBookRequestFails() {

        when(client.getBookByKey("/works/OL123W"))
                .thenThrow(new RuntimeException("API error"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.saveFavorite("/works/OL123W")
        );

        assertTrue(
                exception.getMessage()
                        .contains("Error saving favorite book")
        );
    }

    @Test
    void shouldDeleteFavorite() {

        service.deleteFavorite(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void shouldUseUnknownAuthorWhenAuthorsArrayDoesNotExist() {

        String bookJson = """
        {
            "title": "Clean Code"
        }
        """;

        when(client.getBookByKey("/works/OL123W"))
                .thenReturn(bookJson);

        when(repository.save(any(FavoriteBook.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FavoriteBook result =
                service.saveFavorite("/works/OL123W");

        assertEquals("Autor desconocido", result.getAuthor());
    }
    @Test
    void shouldUseUnknownAuthorWhenAuthorsArrayIsEmpty() {

        String bookJson = """
        {
            "title": "Clean Code",
            "authors": []
        }
        """;

        when(client.getBookByKey("/works/OL123W"))
                .thenReturn(bookJson);

        when(repository.save(any(FavoriteBook.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FavoriteBook result =
                service.saveFavorite("/works/OL123W");

        assertEquals("Autor desconocido", result.getAuthor());
    }
    @Test
    void shouldNotCrashWhenAuthorHasNoKey() {

        String bookJson = """
        {
            "title": "Clean Code",
            "authors": [
                {
                    "name": "Robert Martin"
                }
            ]
        }
        """;

        when(client.getBookByKey("/works/OL123W"))
                .thenReturn(bookJson);

        when(repository.save(any(FavoriteBook.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FavoriteBook result =
                service.saveFavorite("/works/OL123W");

        assertNull(result.getAuthor());
    }
    @Test
    void shouldSetCoverUrlWhenCoverExists() {

        String bookJson = """
        {
            "title": "Clean Code",
            "authors": [],
            "covers": [12345]
        }
        """;

        when(client.getBookByKey("/works/OL123W"))
                .thenReturn(bookJson);

        when(repository.save(any(FavoriteBook.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FavoriteBook result =
                service.saveFavorite("/works/OL123W");

        assertEquals(
                "https://covers.openlibrary.org/b/id/12345-M.jpg",
                result.getCoverUrl()
        );
    }
    @Test
    void shouldLeaveCoverNullWhenBookHasNoCover() {

        String bookJson = """
        {
            "title": "Clean Code",
            "authors": []
        }
        """;

        when(client.getBookByKey("/works/OL123W"))
                .thenReturn(bookJson);

        when(repository.save(any(FavoriteBook.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FavoriteBook result =
                service.saveFavorite("/works/OL123W");

        assertNull(result.getCoverUrl());
    }
    @Test
    void shouldThrowRuntimeExceptionWhenJsonIsInvalid() {

        when(client.getBookByKey("/works/OL123W"))
                .thenReturn("invalid-json");

        assertThrows(
                RuntimeException.class,
                () -> service.saveFavorite("/works/OL123W")
        );
    }
    @Test
    void shouldUseUnknownAuthorWhenAuthorsIsNotArray() {

        String bookJson = """
        {
            "title":"Clean Code",
            "authors":"Robert Martin"
        }
        """;

        when(client.getBookByKey("/works/OL123W"))
                .thenReturn(bookJson);

        when(repository.save(any(FavoriteBook.class)))
                .thenAnswer(i -> i.getArgument(0));

        FavoriteBook result =
                service.saveFavorite("/works/OL123W");

        assertEquals("Autor desconocido", result.getAuthor());
    }
    @Test
    void shouldIgnoreCoverWhenCoversIsNotArray() {

        String bookJson = """
        {
            "title":"Clean Code",
            "authors":[],
            "covers":"12345"
        }
        """;

        when(client.getBookByKey("/works/OL123W"))
                .thenReturn(bookJson);

        when(repository.save(any(FavoriteBook.class)))
                .thenAnswer(i -> i.getArgument(0));

        FavoriteBook result =
                service.saveFavorite("/works/OL123W");

        assertNull(result.getCoverUrl());
    }
    @Test
    void shouldIgnoreCoverWhenCoverArrayIsEmpty() {

        String bookJson = """
        {
            "title":"Clean Code",
            "authors":[],
            "covers":[]
        }
        """;

        when(client.getBookByKey("/works/OL123W"))
                .thenReturn(bookJson);

        when(repository.save(any(FavoriteBook.class)))
                .thenAnswer(i -> i.getArgument(0));

        FavoriteBook result =
                service.saveFavorite("/works/OL123W");

        assertNull(result.getCoverUrl());
    }
    @Test
    void shouldUseDefaultTitleWhenTitleDoesNotExist() {

        String bookJson = """
        {
            "authors":[]
        }
        """;

        when(client.getBookByKey("/works/OL123W"))
                .thenReturn(bookJson);

        when(repository.save(any(FavoriteBook.class)))
                .thenAnswer(i -> i.getArgument(0));

        FavoriteBook result =
                service.saveFavorite("/works/OL123W");

        assertEquals("Título desconocido", result.getTitle());
    }
    @Test
    void shouldUseUnknownAuthorWhenAuthorJsonIsInvalid() {

        String bookJson = """
        {
            "title":"Clean Code",
            "authors":[
                {
                    "key":"/authors/OL1A"
                }
            ]
        }
        """;

        when(client.getBookByKey("/works/OL123W"))
                .thenReturn(bookJson);

        when(client.getBookByKey("/authors/OL1A"))
                .thenReturn("invalid-json");

        when(repository.save(any(FavoriteBook.class)))
                .thenAnswer(i -> i.getArgument(0));

        FavoriteBook result =
                service.saveFavorite("/works/OL123W");

        assertEquals("Autor desconocido", result.getAuthor());
    }
    @Test
    void shouldHandleAuthorsAsString() {

        String bookJson = """
    {
        "title":"Book",
        "authors":"Robert Martin"
    }
    """;

        when(client.getBookByKey(anyString()))
                .thenReturn(bookJson);

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        service.saveFavorite("/works/test");
    }
}