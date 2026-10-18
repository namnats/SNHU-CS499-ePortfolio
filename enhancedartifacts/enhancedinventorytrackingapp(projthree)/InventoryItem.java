package com.example.projthree;

public class InventoryItem {
    private final int id;
    private final String name;
    private final int quantity;
    private int displayId;

    public InventoryItem(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.displayId = 1;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public int getDisplayId() { return displayId; }
    public void setDisplayId(int displayId) { this.displayId = displayId; }
}