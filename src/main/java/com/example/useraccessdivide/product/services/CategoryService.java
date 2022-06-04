package com.example.useraccessdivide.product.services;

import com.example.useraccessdivide.common.utils.CommonUtils;
import com.example.useraccessdivide.product.entities.CategoryEntity;
import com.example.useraccessdivide.product.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Optional<CategoryEntity> findById(long id){
        return categoryRepository.findById(id);
    }

    public Optional<CategoryEntity> findBySlug(String slug){
        return categoryRepository.findBySlug(slug);
    }

    public CategoryEntity save(CategoryEntity entity){
        entity.setSlug(CommonUtils.toSlug(entity.getName()));
        return categoryRepository.saveAndFlush(entity);
    }

    public List<CategoryEntity> saveAll(Iterable<CategoryEntity> entities){
        entities.forEach(e -> {
            e.setSlug(CommonUtils.toSlug(e.getName()));
        });
        return categoryRepository.saveAllAndFlush(entities);
    }
}
