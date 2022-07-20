package com.example.useraccessdivide.product.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.useraccessdivide.product.dtos.PageDto;
import com.example.useraccessdivide.product.entities.CartHistory;
import com.example.useraccessdivide.product.services.CartHistoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/buy-item-history")
@Api(value = "Purchase history", tags = {"api lịch sử mua hàng"})
public class BuyItemHistoryApi {
    @Autowired
    CartHistoryService cartHistoryService;

    @ApiOperation(value = "Xem lịch sử mua hàng")
    @GetMapping
    ResponseEntity<PageDto> viewHistory(long userId, int currentPage){
        Page<CartHistory> cartHistoryEntityPage = cartHistoryService.findByUserId(userId, currentPage-1, 2, Sort.by(Sort.Order.asc("boughtDatetime")));
        PageDto<CartHistory> pageDto = PageDto.<CartHistory>builder()
                .datas(cartHistoryEntityPage.toList())
                .totalPages(cartHistoryEntityPage.getTotalPages())
                .totalElements(cartHistoryEntityPage.getTotalElements())
                .build();
       log.info("ABCSCS");
        return ResponseEntity.ok(pageDto);
    }

}
