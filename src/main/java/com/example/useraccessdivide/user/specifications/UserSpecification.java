package com.example.useraccessdivide.user.specifications;

import com.example.useraccessdivide.user.dtos.UserDto;
import com.example.useraccessdivide.user.entities.User;
import com.example.useraccessdivide.user.entities.meta.User_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Class xử lý filter search thông tin tài khoản
 * @author tranh
 *
 */
@Component
public final class UserSpecification {
    @Autowired
    private EntityManager entityManager;

    /**
     * Tìm tất cả thông tin tài khoản
     * @return List<UserDto> danh sách thông tin tài khoản
     */
    public List<UserDto> findAll(){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);

        TypedQuery<User> result = entityManager.createQuery(criteriaQuery);
        List<UserDto> userDtoList = new ArrayList<>();
        result.getResultList().forEach(u -> {
            userDtoList.add(new UserDto().convert(u));
        });
        return userDtoList;
    }

    /**
     * Tìm thông tin tài khoản theo điều kiện
     * @param userdto điều kiện
     * @return List<UserDto> danh sách thông tin tài khoản
     */
    public List<UserDto> filter(UserDto userdto){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        List<Predicate> conditionList = new ArrayList<>();
        
        if(null != userdto && StringUtils.hasText(userdto.getUsername())) {
        	// tìm kiếm theo username (like)
            conditionList.add(builder.like(root.get(User_.USERNAME), "%"+userdto.getUsername()+"%" ));
        }
        if (null != userdto && StringUtils.hasText(userdto.getFirstName())) {
        	// tìm kiếm theo first name (like)
            conditionList.add(builder.like(root.get(User_.FIRSTNAME),"%"+userdto.getFirstName()+"%" ));
        }
        if(null != userdto && StringUtils.hasText(userdto.getLastName())) {
        	// tìm kiếm theo last name (like)
        	conditionList.add(builder.like(root.get(User_.LASTNAME), "%"+userdto.getLastName()+"%"));
        }
        if(null != userdto && StringUtils.hasText(userdto.getEmail())) {
        	// tìm kiếm theo email (like)
            conditionList.add(builder.like(root.get(User_.EMAIL),"%"+userdto.getEmail()+"%"));
        }
        conditionList.add(builder.or(builder.equal(root.get(User_.ENABLE),true),builder.equal(root.get(User_.ENABLE),false)));
//        conditionList.add(builder.equal(root.get(User_.ENABLE),userdto.isEnable()));
        criteriaQuery.select(root).where(builder.and(conditionList.toArray(new Predicate[0])));

        TypedQuery<User> result = entityManager.createQuery(criteriaQuery);
        List<UserDto> userDtoList = new ArrayList<>();

        result.getResultList().forEach(u -> {
            userDtoList.add(new UserDto().convert(u));
        });
        return userDtoList;
    }
}
