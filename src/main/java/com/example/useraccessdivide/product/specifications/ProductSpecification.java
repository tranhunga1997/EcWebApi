package com.example.useraccessdivide.product.specifications;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.useraccessdivide.common.Pagingation;
import com.example.useraccessdivide.product.entities.Brand;
import com.example.useraccessdivide.product.entities.Category;
import com.example.useraccessdivide.product.entities.Product;
import com.example.useraccessdivide.product.entities.meta.ProductSearch_;
import com.example.useraccessdivide.product.forms.ProductSearchForm;
import com.example.useraccessdivide.product.services.BrandService;
import com.example.useraccessdivide.product.services.CategoryService;

@Component
public final class ProductSpecification {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;

    public List<Product> findAll(){
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Product> criteriaQuery = builder.createQuery(Product.class);
            Root<Product> root = criteriaQuery.from(Product.class);
            criteriaQuery.select(root).orderBy(builder.desc(root.get("id")));

            TypedQuery<Product> result = entityManager.createQuery(criteriaQuery);
            return result.getResultList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public Pagingation<Product> filter(ProductSearchForm form, int currentPage){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        List<Predicate> conditionList = new ArrayList<>();

        if(form != null) {
            if (StringUtils.hasText(form.getName())) {
                conditionList.add(builder.like(root.get(ProductSearch_.NAME), "%" + form.getName() + "%"));
            }

            if (0 != form.getMinPrice() && 0 != form.getMaxPrice()) {
                Predicate ge = builder.ge(root.get(ProductSearch_.PRICE), form.getMinPrice());
                Predicate le = builder.le(root.get(ProductSearch_.PRICE), form.getMaxPrice());
                conditionList.add(builder.and(ge, le));
            } else if (0 != form.getMinPrice() && 0 == form.getMaxPrice()) {
                Predicate ge = builder.ge(root.get(ProductSearch_.PRICE), form.getMinPrice());
                conditionList.add(ge);
            } else if (0 == form.getMinPrice() && 0 != form.getMaxPrice()) {
                Predicate le = builder.le(root.get(ProductSearch_.PRICE), form.getMaxPrice());
                conditionList.add(le);
            }

            if (StringUtils.hasText(form.getCategorySlug())) {
                Category entity = categoryService.findBySlug(form.getCategorySlug()).get();
                conditionList.add(builder.equal(root.get(ProductSearch_.CATEGORY), entity));
            }

            if (StringUtils.hasText(form.getBrandSlug())) {
                Brand entity = brandService.findbySlug(form.getBrandSlug()).get();
                conditionList.add(builder.equal(root.get(ProductSearch_.BRAND), entity));
            }
        }
        conditionList.add(builder.or(builder.isNull(root.get(ProductSearch_.ENDDATETIME)), builder.and(builder.lessThanOrEqualTo(root.get(ProductSearch_.STARTDATETIME), LocalDateTime.now()),builder.greaterThanOrEqualTo(root.get(ProductSearch_.ENDDATETIME), LocalDateTime.now()))));
        if(null != form && null != form.getPriceOrder() && "desc".equalsIgnoreCase(form.getPriceOrder())) {
            query.select(root).where(builder.and(conditionList.toArray(new Predicate[0]))).orderBy(builder.desc(root.get(ProductSearch_.PRICE)));
        }else if(null != form && null != form.getPriceOrder() && "asc".equalsIgnoreCase(form.getPriceOrder())){
            query.select(root).where(builder.and(conditionList.toArray(new Predicate[0]))).orderBy(builder.asc(root.get(ProductSearch_.PRICE)));
        }else {
        	query.select(root).where(builder.and(conditionList.toArray(new Predicate[0])));
        }
        int limit = 2;
    	int offSet = (currentPage - 1)*limit;
        TypedQuery<Product> result = entityManager.createQuery(query);
        List<Product> resultList = result.getResultList();
        
        Pagingation<Product> page = new Pagingation<Product>();
        page.setDatas(resultList.stream().skip(offSet).limit(limit).collect(Collectors.toList()));
        page.setTotalElement(resultList.size());
        page.setTotalPage(Math.round(resultList.size()/limit));
        return page;
    }
}
