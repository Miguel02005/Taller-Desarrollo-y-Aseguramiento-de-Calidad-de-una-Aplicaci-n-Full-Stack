package edu.unac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    private String workKey;     // OpenLibrary key (ej: "/works/OL82563W")
    private String id;          // ID único interno
    private String title;
    private String author;
    private Integer publishYear;
    private Integer editions;
    private String coverUrl;
}