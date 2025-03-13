package com.ttl.coffeemanagement.order;

public class ItemOrder {
    private int id;
    private String name;
    private double price;
    private String description; // Thêm biến description
    private int quantity; // Số lượng được chọn
    private String imagePath; // Đường dẫn hình ảnh

    public ItemOrder(int id, String name, double price, String description, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description; // Sử dụng biến description
        this.quantity = 0; // Mặc định số lượng là 0
        this.imagePath = imagePath;
    }

    // Getters và Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; } // Sử dụng biến description
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotal() { return price * quantity; }
    public String getImagePath() { return imagePath;  }
}
