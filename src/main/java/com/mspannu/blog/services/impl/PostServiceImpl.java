package com.mspannu.blog.services.impl;

import com.mspannu.blog.domain.CreatePostRequest;
import com.mspannu.blog.domain.PostStatus;
import com.mspannu.blog.domain.dtos.UpdatePostRequest;
import com.mspannu.blog.domain.entities.Category;
import com.mspannu.blog.domain.entities.Post;
import com.mspannu.blog.domain.entities.Tag;
import com.mspannu.blog.domain.entities.User;
import com.mspannu.blog.repositories.PostRepository;
import com.mspannu.blog.repositories.TagRepository;
import com.mspannu.blog.services.CategoryService;
import com.mspannu.blog.services.PostService;
import com.mspannu.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final TagService tagService;
    private final CategoryService categoryService;
    private final TagRepository tagRepository;
    private static final int wpm = 200;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagRepository.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );
        } else if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        } else if (tagId != null) {
            Tag tag = tagRepository.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED,
                    tag
            );
        } else{
            return postRepository.findAllByStatus(PostStatus.PUBLISHED);
        }
    }

    @Override
    public Post getPost(UUID postId) {
        return postRepository.findById(postId).orElseThrow(()-> new EntityNotFoundException("Post not found with id " + postId));
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(PostStatus.PUBLISHED);
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));
        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);
        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post existingPost =  postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Post not found with id: " + id)
        );

        String postContent = updatePostRequest.getContent();
        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();

        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(postContent);
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(postContent));

        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)){
            Category newCategory = categoryService.getCategoryById(updatePostRequestCategoryId);
            existingPost.setCategory(newCategory);
        }

        Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> newTagIds = updatePostRequest.getTagIds();
        if(!existingTagIds.equals(newTagIds)){
            List<Tag> newTags = tagService.getTagByIds(newTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }
        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(UUID id) {
        Post post = getPost(id);
        postRepository.delete(post);
    }

    private Integer calculateReadingTime(String content){
        if(content  == null || content.isEmpty()){
            return 0;
        }

        int numWords = content.trim().split("\\s+").length;

        return (int) Math.ceil((double) numWords/wpm);

    }
}
