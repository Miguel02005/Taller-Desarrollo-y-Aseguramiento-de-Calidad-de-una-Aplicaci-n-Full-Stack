package edu.unac.external;

public class BookResponseDTO {

    private String workKey;
    private String id;
    private String title;
    private String author;
    private Integer publishYear;
    private Integer editions;
    private String coverUrl;

    public BookResponseDTO() {}

    public BookResponseDTO(String workKey, String id, String title, String author,
                           Integer publishYear, Integer editions, String coverUrl) {
        this.workKey = workKey;
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
        this.editions = editions;
        this.coverUrl = coverUrl;
    }

    public String getWorkKey() { return workKey; }
    public void setWorkKey(String workKey) { this.workKey = workKey; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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