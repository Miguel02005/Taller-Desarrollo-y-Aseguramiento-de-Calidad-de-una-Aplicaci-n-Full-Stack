package edu.unac.controller;

import edu.unac.domain.FavoriteBook;
import edu.unac.service.FavoriteBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteBookController {

    @Autowired
    private FavoriteBookService service;

    @GetMapping
    public List<FavoriteBook> getFavorites() {
        return service.getAllFavorites();
    }

    @PostMapping
    public FavoriteBook saveFavorite(@RequestParam String key) {
        return service.saveFavorite(key);
    }

    @DeleteMapping("/{id}")
    public void deleteFavorite(@PathVariable Long id) {
        service.deleteFavorite(id);
    }
}