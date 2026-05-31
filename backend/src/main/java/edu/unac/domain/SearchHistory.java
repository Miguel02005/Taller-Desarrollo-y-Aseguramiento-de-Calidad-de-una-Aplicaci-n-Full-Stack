package edu.unac.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String language;
    private Integer publishedAfter;
    private LocalDateTime searchDate;

    public SearchHistory() {}

    public SearchHistory(Long id, String title, String author,
                         String language, Integer publishedAfter, LocalDateTime searchDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.language = language;
        this.publishedAfter = publishedAfter;
        this.searchDate = searchDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public Integer getPublishedAfter() { return publishedAfter; }
    public void setPublishedAfter(Integer publishedAfter) { this.publishedAfter = publishedAfter; }
    public LocalDateTime getSearchDate() { return searchDate; }
    public void setSearchDate(LocalDateTime searchDate) { this.searchDate = searchDate; }
}