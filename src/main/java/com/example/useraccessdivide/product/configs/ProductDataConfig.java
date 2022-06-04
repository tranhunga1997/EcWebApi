package com.example.useraccessdivide.product.configs;

import com.example.useraccessdivide.common.utils.CommonUtils;
import com.example.useraccessdivide.product.entities.BrandEntity;
import com.example.useraccessdivide.product.entities.CategoryEntity;
import com.example.useraccessdivide.product.services.BrandService;
import com.example.useraccessdivide.product.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class ProductDataConfig {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
//    @PostConstruct
    void categoryInit(){
        List<CategoryEntity> categoryEntityList = new ArrayList<>();
        for (int i=1; i<=3; i++){
            CategoryEntity entity = new CategoryEntity();
            entity.setName("category "+i);
            entity.setTax(0.01f*2);
            entity.setLevelId(1);
            entity.setOrderId(i);
            entity.setCreateDatetime(LocalDateTime.now());
            entity.setUpdateDatetime(LocalDateTime.now());
            categoryEntityList.add(entity);
        }
        categoryService.saveAll(categoryEntityList);
    }

//    @PostConstruct
    void brandInit(){
        List<BrandEntity> brandEntityList = new ArrayList<>();
        BrandEntity lv = new BrandEntity();
        lv.setName("LV");
        lv.setCreateDatetime(LocalDateTime.now());
        lv.setUpdateDatetime(LocalDateTime.now());
        BrandEntity channel = new BrandEntity();
        channel.setName("Channel");
        channel.setCreateDatetime(LocalDateTime.now());
        channel.setUpdateDatetime(LocalDateTime.now());
        BrandEntity ysl = new BrandEntity();
        ysl.setName("YSL");
        ysl.setCreateDatetime(LocalDateTime.now());
        ysl.setUpdateDatetime(LocalDateTime.now());

        brandEntityList.addAll(Arrays.asList(lv, channel, ysl));
        brandService.saveAll(brandEntityList);
    }
}
