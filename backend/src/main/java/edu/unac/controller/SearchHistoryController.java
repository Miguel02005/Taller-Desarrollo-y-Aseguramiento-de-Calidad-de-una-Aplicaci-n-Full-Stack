package edu.unac.controller;

import edu.unac.domain.SearchHistory;
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
    public List<SearchHistory> getHistory() {
        return service.getAllSearches();
    }

    @DeleteMapping
    public void clearHistory() {
        service.clearAllSearches();
    }
}