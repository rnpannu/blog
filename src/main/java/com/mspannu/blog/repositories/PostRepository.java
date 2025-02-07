package com.mspannu.blog.repositories;

import com.mspannu.blog.domain.PostStatus;
import com.mspannu.blog.domain.entities.Category;
import com.mspannu.blog.domain.entities.Post;
import com.mspannu.blog.domain.entities.Tag;
import com.mspannu.blog.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus status, Category category, Tag tag);

    List<Post> findAllByStatusAndCategory(PostStatus status, Category category);

    List<Post> findAllByStatusAndTagsContaining(PostStatus status, Tag tag);

    List<Post> findAllByStatus(PostStatus status);

    List<Post> findAllByAuthorAndStatus(User author, PostStatus status);

//    void deleteById(UUID id);
//
//    void delete(Post post);

}
