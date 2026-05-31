package edu.unac.controller;

import edu.unac.exception.GlobalExceptionHandler;
import edu.unac.exception.InvalidSearchException;
import edu.unac.exception.NotEnoughResultsException;
import edu.unac.external.BookResponseDTO;
import edu.unac.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService service;

    @InjectMocks
    private BookController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnBooksWhenSearchIsValid() throws Exception {

        BookResponseDTO book = new BookResponseDTO(
                "/works/OL1W", "uuid-1", "Clean Code",
                "Robert Martin", 2008, 5,
                "https://covers.openlibrary.org/b/id/123-M.jpg"
        );

        when(service.searchBooks("Clean Code", null, null, null))
                .thenReturn(List.of(book));

        mockMvc.perform(get("/api/books/search")
                        .param("title", "Clean Code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"))
                .andExpect(jsonPath("$[0].author").value("Robert Martin"));
    }

    @Test
    void shouldReturn400WhenTitleAndAuthorAreMissing() throws Exception {

        when(service.searchBooks(null, null, null, null))
                .thenThrow(new InvalidSearchException("Title or author is required."));

        mockMvc.perform(get("/api/books/search"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_SEARCH"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturn400WhenLanguageIsInvalid() throws Exception {

        when(service.searchBooks("book", null, "klingon", null))
                .thenThrow(new InvalidSearchException("Invalid language."));

        mockMvc.perform(get("/api/books/search")
                        .param("title", "book")
                        .param("language", "klingon"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_SEARCH"));
    }

    @Test
    void shouldReturn400WhenPublishedAfterIsFutureYear() throws Exception {

        when(service.searchBooks("book", null, null, 9999))
                .thenThrow(new InvalidSearchException("publishedAfter cannot be greater than current year."));

        mockMvc.perform(get("/api/books/search")
                        .param("title", "book")
                        .param("publishedAfter", "9999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_SEARCH"));
    }

    @Test
    void shouldReturn404WhenLessThanThreeResults() throws Exception {

        when(service.searchBooks("rare", null, null, null))
                .thenThrow(new NotEnoughResultsException("Not enough results."));

        mockMvc.perform(get("/api/books/search")
                        .param("title", "rare"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_ENOUGH_RESULTS"));
    }

    @Test
    void shouldReturnEmptyListWhenNoResults() throws Exception {

        when(service.searchBooks("xyznotfound", null, null, null))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/books/search")
                        .param("title", "xyznotfound"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldSearchWithOnlyTitle() throws Exception {

        when(service.searchBooks("Java", null, null, null))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/books/search")
                        .param("title", "Java"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSearchWithOnlyAuthor() throws Exception {

        when(service.searchBooks(null, "Tolkien", null, null))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/books/search")
                        .param("author", "Tolkien"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBooksWithAllFilters() throws Exception {

        when(service.searchBooks("Java", "Oracle", "english", 2020))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/books/search")
                        .param("title", "Java")
                        .param("author", "Oracle")
                        .param("language", "english")
                        .param("publishedAfter", "2020"))
                .andExpect(status().isOk());
    }
}
