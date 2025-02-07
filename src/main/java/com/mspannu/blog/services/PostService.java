package com.mspannu.blog.services;

import com.mspannu.blog.domain.CreatePostRequest;
import com.mspannu.blog.domain.dtos.UpdatePostRequest;
import com.mspannu.blog.domain.entities.Post;
import com.mspannu.blog.domain.entities.User;
import com.mspannu.blog.repositories.PostRepository;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts(UUID categoryId, UUID tagId);

    Post getPost(UUID postId);

    List<Post> getDraftPosts(User user);

    Post createPost(User user, CreatePostRequest createPostRequest);

    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);

    void deletePost(UUID id);
}
