package com.yennie.restapi;

/**
 * 传统 JavaBean：Jackson 可以通过无参构造器和 setter 写入 JSON 数据，
 * 并通过 getter 将对象序列化为 JSON。
 */
public class Order {

    private String customerName;
    private String productName;
    private int quantity;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customerName='" + customerName + '\'' +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
