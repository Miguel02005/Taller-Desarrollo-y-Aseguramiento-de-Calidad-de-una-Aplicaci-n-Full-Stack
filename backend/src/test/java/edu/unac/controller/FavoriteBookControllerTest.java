package edu.unac.controller;

import edu.unac.domain.FavoriteBook;
import edu.unac.exception.GlobalExceptionHandler;
import edu.unac.service.FavoriteBookService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FavoriteBookControllerTest {

    @Mock
    private FavoriteBookService service;

    @InjectMocks
    private FavoriteBookController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnEmptyListWhenNoFavorites() throws Exception {

        when(service.getAllFavorites()).thenReturn(List.of());

        mockMvc.perform(get("/api/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldReturnFavoritesList() throws Exception {

        FavoriteBook book = new FavoriteBook();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");
        book.setBookKey("/works/OL123W");

        when(service.getAllFavorites()).thenReturn(List.of(book));

        mockMvc.perform(get("/api/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"))
                .andExpect(jsonPath("$[0].author").value("Robert Martin"));
    }

    @Test
    void shouldSaveFavorite() throws Exception {

        FavoriteBook book = new FavoriteBook();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setBookKey("/works/OL123W");

        when(service.saveFavorite("/works/OL123W")).thenReturn(book);

        mockMvc.perform(post("/api/favorites")
                        .param("key", "/works/OL123W"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.bookKey").value("/works/OL123W"));
    }

    @Test
    void shouldReturn500WhenSaveFails() throws Exception {

        when(service.saveFavorite("/works/OL123W"))
                .thenThrow(new RuntimeException("Error saving favorite book"));

        mockMvc.perform(post("/api/favorites")
                        .param("key", "/works/OL123W"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"));
    }

    @Test
    void shouldDeleteFavorite() throws Exception {

        doNothing().when(service).deleteFavorite(1L);

        mockMvc.perform(delete("/api/favorites/1"))
                .andExpect(status().isOk());

        verify(service).deleteFavorite(1L);
    }
}
