package com.example.useraccessdivide.product.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.useraccessdivide.product.entities.CartHistory;
import com.example.useraccessdivide.product.forms.CartHistoryForm;
import com.example.useraccessdivide.product.repositories.CartHistoryRepository;

@Service
public class CartHistoryService {
    @Autowired
    CartHistoryRepository cartHistoryRepository;

    public Page<CartHistory> findByUserId(long id, int currentPage,int pageSize, Sort sort){
        return cartHistoryRepository.findByUserId(id, PageRequest.of(currentPage, pageSize, sort));
    }

    public void saveAll(List<CartHistoryForm> forms){
        List<CartHistory> cartHistoryEntityList = new ArrayList<>();
        forms.forEach(e -> {
            CartHistory entity = new CartHistory();
            BeanUtils.copyProperties(e, entity);
            entity.setBoughtDatetime(LocalDateTime.now());
            cartHistoryEntityList.add(entity);
        });

        cartHistoryRepository.saveAll(cartHistoryEntityList);
    }
   
}
