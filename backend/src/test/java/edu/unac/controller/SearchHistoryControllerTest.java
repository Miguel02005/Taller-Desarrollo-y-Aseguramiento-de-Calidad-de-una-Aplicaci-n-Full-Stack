package edu.unac.controller;

import edu.unac.domain.SearchHistory;
import edu.unac.exception.GlobalExceptionHandler;
import edu.unac.service.SearchHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SearchHistoryControllerTest {

    @Mock
    private SearchHistoryService service;

    @InjectMocks
    private SearchHistoryController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnEmptyHistoryWhenNoSearches() throws Exception {

        when(service.getAllSearches()).thenReturn(List.of());

        mockMvc.perform(get("/api/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldReturnSearchHistory() throws Exception {

        SearchHistory search = new SearchHistory();
        search.setId(1L);
        search.setTitle("Clean Code");
        search.setAuthor("Robert Martin");
        search.setLanguage("english");
        search.setPublishedAfter(2000);
        search.setSearchDate(LocalDateTime.of(2024, 1, 1, 12, 0));

        when(service.getAllSearches()).thenReturn(List.of(search));

        mockMvc.perform(get("/api/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"))
                .andExpect(jsonPath("$[0].author").value("Robert Martin"))
                .andExpect(jsonPath("$[0].language").value("english"));
    }

    @Test
    void shouldClearHistory() throws Exception {

        doNothing().when(service).clearAllSearches();

        mockMvc.perform(delete("/api/history"))
                .andExpect(status().isOk());

        verify(service).clearAllSearches();
    }

    @Test
    void shouldReturnMultipleSearches() throws Exception {

        SearchHistory s1 = new SearchHistory();
        s1.setTitle("Java");

        SearchHistory s2 = new SearchHistory();
        s2.setTitle("Spring Boot");

        when(service.getAllSearches()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/api/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
