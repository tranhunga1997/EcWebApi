package com.example.useraccessdivide.user.specifications;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.useraccessdivide.user.entities.User;
import com.example.useraccessdivide.user.entities.meta.User_;
import com.example.useraccessdivide.user.forms.UserForm;

/**
 * Class xử lý filter search thông tin tài khoản
 * @author tranh
 *
 */
@Component
public final class UserSpecification {
    @Autowired
    private EntityManager em;
    
    /**
     * Tìm thông tin tài khoản theo điều kiện
     * @param form điều kiện
     * @return List<UserDto> danh sách thông tin tài khoản
     */
    public Page<User> filter(UserForm form, Pageable pageable){
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        List<Predicate> conditionList = new ArrayList<>();
        
        if(null == form) {
        	throw new IllegalArgumentException("user form is null");
        }
        
        if(StringUtils.hasText(form.getUsername())) {
        	// tìm kiếm theo username (like)
            conditionList.add(builder.like(root.get(User_.USERNAME), form.getUsername()+"%" ));
        }
        if (StringUtils.hasText(form.getFirstName())) {
        	// tìm kiếm theo first name (like)
            conditionList.add(builder.like(root.get(User_.FIRSTNAME),"%"+form.getFirstName()+"%" ));
        }
        if(StringUtils.hasText(form.getLastName())) {
        	// tìm kiếm theo last name (like)
        	conditionList.add(builder.like(root.get(User_.LASTNAME), "%"+form.getLastName()+"%"));
        }
        if(StringUtils.hasText(form.getEmail())) {
        	// tìm kiếm theo email (equal)
            conditionList.add(builder.equal(root.get(User_.EMAIL), form.getEmail()));
        }
        // thiết lập điều kiện (where)
        // conditionList.add(builder.or(builder.equal(root.get(User_.ENABLE),true),builder.equal(root.get(User_.ENABLE),false)));
        criteriaQuery.select(root).where(builder.and(conditionList.toArray(new Predicate[0])));

        // thiết lập offset và limit
        long totalElements = getTotalUser();
        TypedQuery<User> result = em.createQuery(criteriaQuery)
        		.setFirstResult((int) pageable.getOffset())
        		.setMaxResults(pageable.getPageSize());
        // lấy dữ liệu
        List<User> data = result.getResultList();
        return new PageImpl<>(data, pageable, totalElements);
    }
    
    /**
     * Lấy tổng số data
     * @return
     */
    private long getTotalUser() {
    	BigInteger result = (BigInteger) em.createNativeQuery("SELECT COUNT(*) FROM users").getSingleResult();
        return result.longValue() ;
    }
}
