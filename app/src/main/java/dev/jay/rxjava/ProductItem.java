package dev.jay.rxjava;

import androidx.annotation.NonNull;

public class ProductItem {
    private String category;
    private String description;
    private int id;
    private String image;
    private Double price;
    private String title;

    public ProductItem(String category, String description, int id, String image, Double price, String title) {
        this.category = category;
        this.description = description;
        this.id = id;
        this.image = image;
        this.price = price;
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public Double getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "name='" + category + '\'' +
                ", number=" + image +
                '}';
    }
}
