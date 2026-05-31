package services;

import edu.unac.exception.InvalidSearchException;
import edu.unac.external.BookResponseDTO;
import edu.unac.external.OpenLibraryClient;
import edu.unac.service.BookService;
import edu.unac.service.SearchHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private OpenLibraryClient client;

    @Mock
    private SearchHistoryService historyService;

    @InjectMocks
    private BookService service;

    @Test
    void shouldThrowExceptionWhenTitleAndAuthorAreMissing() {

        assertThrows(
                InvalidSearchException.class,
                () -> service.searchBooks(
                        null,
                        null,
                        null,
                        null
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenPublishedAfterIsFutureYear() {

        int nextYear = Year.now().getValue() + 1;

        assertThrows(
                InvalidSearchException.class,
                () -> service.searchBooks(
                        "Harry Potter",
                        null,
                        null,
                        nextYear
                )
        );
    }

    @Test
    void shouldThrowExceptionForInvalidLanguage() {

        assertThrows(
                InvalidSearchException.class,
                () -> service.searchBooks(
                        "Harry Potter",
                        null,
                        "italian",
                        null
                )
        );
    }

    @Test
    void shouldReturnBooksFromOpenLibrary() {

        String json = """
            {
              "docs": [
                {
                  "key": "/works/OL1W",
                  "title": "Clean Code",
                  "author_name": ["Robert Martin"],
                  "publish_year": [2008],
                  "edition_count": 5,
                  "cover_i": 123
                }
              ]
            }
            """;

        when(client.searchBooks("Clean Code", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "Clean Code",
                        null,
                        null,
                        null
                );

        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());

        verify(historyService)
                .saveSearch("Clean Code", null, null, null);

        verify(client)
                .searchBooks("Clean Code", null);
    }

    @Test
    void shouldFilterBooksByPublishedYear() {

        String json = """
            {
              "docs": [
                {
                  "key": "/works/OL1W",
                  "title": "Old Book",
                  "author_name": ["Author"],
                  "publish_year": [1990],
                  "edition_count": 1
                },
                {
                  "key": "/works/OL2W",
                  "title": "New Book",
                  "author_name": ["Author"],
                  "publish_year": [2020],
                  "edition_count": 1
                }
              ]
            }
            """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        null,
                        2000
                );

        assertEquals(1, result.size());
        assertEquals("New Book", result.get(0).getTitle());
    }

    @Test
    void shouldReturnEmptyListWhenDocsDoesNotExist() {

        String json = """
            {
              "numFound": 0
            }
            """;

        when(client.searchBooks("test", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "test",
                        null,
                        null,
                        null
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenJsonIsInvalid() {

        when(client.searchBooks("test", null))
                .thenReturn("invalid-json");

        assertThrows(
                RuntimeException.class,
                () -> service.searchBooks(
                        "test",
                        null,
                        null,
                        null
                )
        );
    }

    @Test
    void shouldFilterBooksByLanguage() {

        String json = """
        {
          "docs": [
            {
              "key": "/works/OL1W",
              "title": "English Book",
              "author_name": ["Author"],
              "language": "english"
            },
            {
              "key": "/works/OL2W",
              "title": "Spanish Book",
              "author_name": ["Author"],
              "language": "spanish"
            }
          ]
        }
        """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        "english",
                        null
                );

        assertEquals(1, result.size());
        assertEquals("English Book", result.get(0).getTitle());
    }
    @Test
    void shouldKeepBookWhenLanguageFieldIsMissing() {

        String json = """
        {
          "docs": [
            {
              "key": "/works/OL1W",
              "title": "Book",
              "author_name": ["Author"]
            }
          ]
        }
        """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        "english",
                        null
                );

        assertEquals(1, result.size());
    }
    @Test
    void shouldKeepBookWhenPublishYearIsMissing() {

        String json = """
        {
          "docs": [
            {
              "key": "/works/OL1W",
              "title": "Book",
              "author_name": ["Author"]
            }
          ]
        }
        """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        null,
                        2020
                );

        assertEquals(1, result.size());
    }
    @Test
    void shouldHandleBookWithoutAuthor() {

        String json = """
        {
          "docs": [
            {
              "key": "/works/OL1W",
              "title": "Book"
            }
          ]
        }
        """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        null,
                        null
                );

        assertNull(result.get(0).getAuthor());
    }
    @Test
    void shouldHandleBookWithoutCover() {

        String json = """
        {
          "docs": [
            {
              "key": "/works/OL1W",
              "title": "Book",
              "author_name": ["Author"]
            }
          ]
        }
        """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        null,
                        null
                );

        assertNull(result.get(0).getCoverUrl());
    }
    @Test
    void shouldLimitResultsToTenBooks() {

        StringBuilder json = new StringBuilder("""
        {
          "docs": [
        """);

        for (int i = 1; i <= 12; i++) {

            json.append("""
            {
              "key": "/works/OL%dW",
              "title": "Book%d",
              "author_name": ["Author"],
              "publish_year": [2020],
              "edition_count": 1
            }
            """.formatted(i, i));

            if (i < 12) {
                json.append(",");
            }
        }

        json.append("]}");

        when(client.searchBooks("book", null))
                .thenReturn(json.toString());

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        null,
                        null
                );

        assertEquals(10, result.size());
    }

    @Test
    void shouldHandleBookWithoutPublishYear() {

        String json = """
        {
          "docs": [
            {
              "key": "/works/OL1W",
              "title": "Book",
              "author_name": ["Author"]
            }
          ]
        }
        """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        null,
                        null
                );

        assertNull(result.get(0).getPublishYear());
    }

    @Test
    void shouldSearchWhenOnlyTitleProvided() {

        when(client.searchBooks("Java", null))
                .thenReturn("{\"docs\":[]}");

        List<BookResponseDTO> result =
                service.searchBooks(
                        "Java",
                        null,
                        null,
                        null
                );

        assertTrue(result.isEmpty());
    }
    @Test
    void shouldAllowBlankLanguage() {

        when(client.searchBooks("Java", null))
                .thenReturn("{\"docs\":[]}");

        List<BookResponseDTO> result =
                service.searchBooks(
                        "Java",
                        null,
                        "",
                        null
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFilterBookByLanguage() {

        String json = """
    {
      "docs":[
        {
          "key":"/works/OL1W",
          "title":"Book",
          "language":"english"
        }
      ]
    }
    """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        "spanish",
                        null
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldKeepBookWhenLanguageMatches() {

        String json = """
    {
      "docs":[
        {
          "key":"/works/OL1W",
          "title":"Book",
          "language":"english"
        }
      ]
    }
    """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        "english",
                        null
                );

        assertEquals(1, result.size());
    }
    @Test
    void shouldKeepBookWhenPublishYearEqualsFilter() {

        String json = """
    {
      "docs":[
        {
          "key":"/works/OL1W",
          "title":"Book",
          "publish_year":[2020]
        }
      ]
    }
    """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        null,
                        2020
                );

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenDocsIsNotArray() {

        String json = """
    {
      "docs": {}
    }
    """;

        when(client.searchBooks("test", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "test",
                        null,
                        null,
                        null
                );

        assertTrue(result.isEmpty());
    }
    @Test
    void shouldKeepBookWhenLanguageFieldMissing() {

        String json = """
    {
      "docs": [
        {
          "key": "/works/OL1W",
          "title": "Book"
        }
      ]
    }
    """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        "english",
                        null
                );

        assertEquals(1, result.size());
    }
    @Test
    void shouldKeepBookWithoutPublishYearWhenFilteringByYear() {

        String json = """
    {
      "docs": [
        {
          "key": "/works/OL1W",
          "title": "Book"
        }
      ]
    }
    """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        null,
                        2020
                );

        assertEquals(1, result.size());
    }
    @Test
    void shouldGenerateRandomIdWhenWorkKeyMissing() {

        String json = """
    {
      "docs": [
        {
          "title": "Book"
        }
      ]
    }
    """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        null,
                        null
                );

        assertNotNull(result.get(0).getId());
    }
    @Test
    void shouldUseOldestPublishYear() {

        String json = """
    {
      "docs": [
        {
          "key": "/works/OL1W",
          "title": "Book",
          "publish_year": [2020, 1990, 2010]
        }
      ]
    }
    """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        null,
                        null
                );

        assertEquals(1990,
                result.get(0).getPublishYear());
    }
    @Test
    void shouldHandleNullCoverField() {

        String json = """
    {
      "docs": [
        {
          "key": "/works/OL1W",
          "title": "Book",
          "cover_i": null
        }
      ]
    }
    """;

        when(client.searchBooks("book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "book",
                        null,
                        null,
                        null
                );

        assertNull(result.get(0).getCoverUrl());
    }

    @Test
    void shouldAllowSearchWhenOnlyAuthorIsProvided() {

        when(client.searchBooks(null, "Tolkien"))
                .thenReturn("{\"docs\":[]}");

        List<BookResponseDTO> result =
                service.searchBooks(
                        null,
                        "Tolkien",
                        null,
                        null
                );

        assertNotNull(result);
    }
    @Test
    void shouldAllowCurrentYearAsPublishedAfter() {

        int currentYear = Year.now().getValue();

        when(client.searchBooks("book", null))
                .thenReturn("{\"docs\":[]}");

        assertDoesNotThrow(() ->
                service.searchBooks(
                        "book",
                        null,
                        null,
                        currentYear
                )
        );
    }
    @Test
    void shouldGenerateIdWhenWorkKeyIsMissing() {

        String json = """
        {
          "docs":[
            {
              "title":"Book"
            }
          ]
        }
        """;

        when(client.searchBooks("Book", null))
                .thenReturn(json);

        List<BookResponseDTO> result =
                service.searchBooks(
                        "Book",
                        null,
                        null,
                        null
                );

        assertNotNull(result.get(0).getId());
    }

}