package edu.unac.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_books")
public class FavoriteBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookKey;
    private String title;
    private String author;
    private Integer publishYear;
    private Integer editions;
    private String coverUrl;

    public FavoriteBook() {}

    public FavoriteBook(Long id, String bookKey, String title, String author,
                        Integer publishYear, Integer editions, String coverUrl) {
        this.id = id;
        this.bookKey = bookKey;
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
        this.editions = editions;
        this.coverUrl = coverUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBookKey() { return bookKey; }
    public void setBookKey(String bookKey) { this.bookKey = bookKey; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public Integer getPublishYear() { return publishYear; }
    public void setPublishYear(Integer publishYear) { this.publishYear = publishYear; }
    public Integer getEditions() { return editions; }
    public void setEditions(Integer editions) { this.editions = editions; }
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
}