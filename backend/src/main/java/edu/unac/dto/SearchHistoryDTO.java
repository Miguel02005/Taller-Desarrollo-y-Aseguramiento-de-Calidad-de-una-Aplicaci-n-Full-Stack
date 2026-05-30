package edu.unac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryDTO {
    private Long id;
    private String title;
    private String author;
    private String language;
    private Integer publishedAfter;
    private LocalDateTime searchDate;
}