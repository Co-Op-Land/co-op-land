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

    // 자연어 모드 검색
    @Query(value = "SELECT * FROM post WHERE MATCH(title, content) AGAINST(:keyword IN NATURAL LANGUAGE MODE)",
            nativeQuery = true)
    List<Post> searchByKeyword(@Param("keyword") String keyword);

    // Boolean 모드 검색 (더 복잡한 검색 지원)
    @Query(value = "SELECT * FROM post WHERE MATCH(title, content) AGAINST(:keyword IN BOOLEAN MODE)",
            nativeQuery = true)
    List<Post> searchByKeywordBoolean(@Param("keyword") String keyword);

    // 카테고리별 검색 (Boolean 모드)
    @Query(value = "SELECT * FROM post WHERE category = :category AND MATCH(title, content) " +
            "AGAINST(:keyword IN BOOLEAN MODE)",
            nativeQuery = true)
    List<Post> searchByKeywordAndCategory(@Param("keyword") String keyword,
                                          @Param("category") String category);

    // 페이징 처리가 포함된 검색
    @Query(value = "SELECT * FROM post WHERE MATCH(title, content) AGAINST(:keyword IN BOOLEAN MODE)",
            countQuery = "SELECT COUNT(*) FROM post WHERE MATCH(title, content) AGAINST(:keyword IN BOOLEAN MODE)",
            nativeQuery = true)
    Page<Post> searchByKeywordWithPaging(@Param("keyword") String keyword, Pageable pageable);
}
