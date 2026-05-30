package edu.unac.repository;

import edu.unac.domain.FavoriteBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {

}