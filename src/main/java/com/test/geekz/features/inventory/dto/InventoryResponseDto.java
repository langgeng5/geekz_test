package com.test.geekz.features.inventory.dto;

import com.test.geekz.constant.InventoryType;
import com.test.geekz.features.item.dto.ItemResponseDto;

import lombok.Data;

@Data
public class InventoryResponseDto {
    private Long id;
    private ItemResponseDto item;
    private Integer qty;
    private InventoryType type;
}
