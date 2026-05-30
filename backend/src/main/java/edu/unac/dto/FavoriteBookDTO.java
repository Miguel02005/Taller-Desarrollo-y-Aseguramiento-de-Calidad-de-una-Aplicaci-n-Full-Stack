package edu.unac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteBookDTO {
    private Long id;
    private String bookKey;
    private String title;
    private String author;
    private Integer publishYear;
    private String coverUrl;
}