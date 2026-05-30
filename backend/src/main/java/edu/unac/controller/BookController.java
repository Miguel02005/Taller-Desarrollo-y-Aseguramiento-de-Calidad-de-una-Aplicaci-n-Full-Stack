package edu.unac.controller;

import edu.unac.dto.BookResponseDTO;
import edu.unac.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService service;

    @GetMapping("/search")
    public List<BookResponseDTO> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer publishedAfter
    ) {
        return service.searchBooks(title, author, language, publishedAfter);
    }
}
