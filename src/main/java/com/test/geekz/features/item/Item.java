package com.test.geekz.features.item;

import java.util.Set;

import com.test.geekz.constant.InventoryType;
import com.test.geekz.features.inventory.Inventory;
import com.test.geekz.features.order.Order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String name;
    
    @NotNull
    private Double price;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Inventory> inventory;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> order;

    public Integer getStock(){
        Integer stock = 0;

        // count stock from inventory
        Set<Inventory> inverntories = this.getInventory();
        if (inverntories != null) {
            for (Inventory data : inverntories) {
                if (data.getType() == InventoryType.T) {
                    stock += data.getQty();
                } else {
                    stock -= data.getQty();
                }
            }
        }

        // count from order
        Set<Order> orders = this.getOrder();
        if (orders != null) {
            for (Order data : orders) {
                stock -= data.getQty();
            }
        }

        return stock;
    }
}
