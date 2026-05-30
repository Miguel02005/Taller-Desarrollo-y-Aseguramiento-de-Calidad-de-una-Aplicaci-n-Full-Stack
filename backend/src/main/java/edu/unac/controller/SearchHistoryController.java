package edu.unac.controller;

import edu.unac.dto.SearchHistoryDTO;
import edu.unac.service.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class SearchHistoryController {

    @Autowired
    private SearchHistoryService service;

    @GetMapping
    public List<SearchHistoryDTO> getHistory() {
        return service.getAllSearchesDTO();
    }

    @DeleteMapping
    public void clearHistory() {
        service.clearAllSearches();
    }
}