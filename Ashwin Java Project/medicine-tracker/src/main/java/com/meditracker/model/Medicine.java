package com.meditracker.model;

public class Medicine {
    private int id;
    private int userId;
    private String name;
    private String dosage;
    private int quantity;
    private int threshold;

    public Medicine() { }

    public Medicine(int userId, String name, String dosage, int quantity, int threshold) {
        this.userId = userId;
        this.name = name;
        this.dosage = dosage;
        this.quantity = quantity;
        this.threshold = threshold;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getThreshold() { return threshold; }
    public void setThreshold(int threshold) { this.threshold = threshold; }
}
