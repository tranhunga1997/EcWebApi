package com.example.useraccessdivide.product.services;

import com.example.useraccessdivide.common.utils.CommonUtils;
import com.example.useraccessdivide.product.entities.Brand;
import com.example.useraccessdivide.product.repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    @Autowired
    BrandRepository brandRepository;

    public Optional<Brand> findById(long id){
        return brandRepository.findById(id);
    }

    public Optional<Brand> findbySlug(String slug){
        return brandRepository.findBySlug(slug);
    }

    public Brand save(Brand entity){
        entity.setSlug(CommonUtils.toSlug(entity.getName()));
        return brandRepository.save(entity);
    }

    public List<Brand> saveAll(Iterable<Brand> entities){
        entities.forEach(e -> {
            e.setSlug(CommonUtils.toSlug(e.getName()));
        });
        return brandRepository.saveAllAndFlush(entities);
    }
}
