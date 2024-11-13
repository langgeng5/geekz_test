package com.test.geekz.features.order.dto;

import lombok.Data;

@Data
public class OrderRequestDto {
    private Long itemId;
    private String orderNo;
    private Integer qty;
    private Double price;
}
