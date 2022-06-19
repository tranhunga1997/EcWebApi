package com.example.useraccessdivide.product.services;

import com.example.useraccessdivide.product.dtos.CartHistoryDto;
import com.example.useraccessdivide.product.entities.CartHistory;
import com.example.useraccessdivide.product.forms.CartHistoryForm;
import com.example.useraccessdivide.product.repositories.CartHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            entity.setUserId(e.getUserId());
            entity.setProductName(e.getProductName());
            entity.setPrice(e.getPrice());
            entity.setQuantity(e.getQuantity());
            entity.setAddress(e.getAddress());
            entity.setBoughtDatetime(LocalDateTime.now());
            cartHistoryEntityList.add(entity);
        });

        cartHistoryRepository.saveAll(cartHistoryEntityList);
    }
   
}
