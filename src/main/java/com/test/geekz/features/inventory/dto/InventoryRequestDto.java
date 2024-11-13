package com.test.geekz.features.inventory.dto;

import com.test.geekz.constant.InventoryType;

import lombok.Data;

@Data
public class InventoryRequestDto {
    private Long itemId;
    private Integer qty;
    private InventoryType type;
}
