package com.coop.domain.post.repository;

import com.coop.domain.post.entity.Post;
import com.coop.domain.post.enums.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p " +
            "WHERE :category IS NULL OR p.category = :category")
    Page<Post> findAllByCategory(Pageable pageable, @Param("category") PostCategory category);

    Optional<Post> findByIdAndMemberId(Long postId, Long aLong);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    List<Post> findByKeyword(@Param("keyword") String keyword);
}
