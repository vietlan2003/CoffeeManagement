package com.ttl.coffeemanagement.staff;

public class Employee {
    private int id;
    private String username;
    private String phone;
    private String name;

    public Employee(int id, String username, String phone, String name) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.name = name;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPhone() { return phone; }
    public String getName() { return name; }
}
