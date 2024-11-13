package com.test.geekz.features.order.dto;

import com.test.geekz.features.item.dto.ItemResponseDto;

import lombok.Data;

@Data
public class OrderResponseDto {
    private Long id;
    private String orderNo;
    private ItemResponseDto item;
    private Integer qty;
    private Double price;
}
