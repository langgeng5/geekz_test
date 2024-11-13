package com.test.geekz.features.item.dto;

import lombok.Data;

@Data
public class ItemResponseDto {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;
}
