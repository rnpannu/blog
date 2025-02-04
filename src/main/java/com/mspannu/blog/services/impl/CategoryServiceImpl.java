package com.mspannu.blog.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.mspannu.blog.domain.entities.Category;
import com.mspannu.blog.repositories.CategoryRepository;
import com.mspannu.blog.services.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional // One transaction
    public Category createCategory(Category category) {
        String categoryName = category.getName();
        if(categoryRepository.existsByNameIgnoreCase(category.getName())){
            throw new IllegalArgumentException("Category already exists with name: " + categoryName);
        }

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()){
            if(category.get().getPosts().size() > 0){
                throw new IllegalStateException("Category has posts asssociated with it");
            }

            categoryRepository.deleteById(id);
        }
    }
}
