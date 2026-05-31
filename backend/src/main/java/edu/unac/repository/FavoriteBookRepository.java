package edu.unac.repository;

import edu.unac.domain.FavoriteBook;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {
    Optional<FavoriteBook> findByBookKey(String bookKey);
}