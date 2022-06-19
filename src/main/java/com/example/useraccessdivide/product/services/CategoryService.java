package com.example.useraccessdivide.product.services;

import com.example.useraccessdivide.common.utils.CommonUtils;
import com.example.useraccessdivide.product.entities.Category;
import com.example.useraccessdivide.product.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Optional<Category> findById(long id){
        return categoryRepository.findById(id);
    }

    public Optional<Category> findBySlug(String slug){
        return categoryRepository.findBySlug(slug);
    }

    public Category save(Category entity){
        entity.setSlug(CommonUtils.toSlug(entity.getName()));
        return categoryRepository.saveAndFlush(entity);
    }

    public List<Category> saveAll(Iterable<Category> entities){
        entities.forEach(e -> {
            e.setSlug(CommonUtils.toSlug(e.getName()));
        });
        return categoryRepository.saveAllAndFlush(entities);
    }
}
