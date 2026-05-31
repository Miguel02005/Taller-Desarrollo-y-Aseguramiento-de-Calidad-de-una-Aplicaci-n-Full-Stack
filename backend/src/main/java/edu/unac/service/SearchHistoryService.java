package edu.unac.service;

import edu.unac.domain.SearchHistory;
import edu.unac.repository.SearchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SearchHistoryService {

    @Autowired
    private SearchHistoryRepository repository;

    public List<SearchHistory> getAllSearches() {
        return repository.findAll();
    }

    public void clearAllSearches() {
        repository.deleteAll();
    }

    public SearchHistory saveSearch(String title, String author,
                                    String language, Integer publishedAfter) {
        SearchHistory search = new SearchHistory();
        search.setTitle(title);
        search.setAuthor(author);
        search.setLanguage(language);
        search.setPublishedAfter(publishedAfter);
        search.setSearchDate(LocalDateTime.now());
        return repository.save(search);
    }
}