package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository JPA dédié à la gestion
 * des commentaires.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment>  findByContent(String string);

    List<Comment> findByArticleId(Long articleId);

    List<Comment> findByArticleId(Long articleId, Sort sort);

}
