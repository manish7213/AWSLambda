package com.manish.aws.lambda.apis.dto;

import java.util.Objects;

public class Order {

    private int id;
    private String itemName;
    private int quantity;

    public Order(int id, String itemName, int quantity) {
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && quantity == order.quantity && Objects.equals(itemName, order.itemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemName, quantity);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
