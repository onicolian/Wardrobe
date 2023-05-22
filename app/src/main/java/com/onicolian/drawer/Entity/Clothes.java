package com.onicolian.drawer.Entity;

public class Clothes {

    private long id;
    private byte[] image;
    private String category;
    private String date;
    private double price;
    private String desk;
    private String isFavorite;

    public Clothes(long id, byte[] image, String category, String date, double price,
                   String desk, String isFavorite) {
        this.id = id;
        this.image = image;
        this.category = category;
        this.date = date;
        this.price = price;
        this.desk = desk;
        this.isFavorite = isFavorite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[]  getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public String isFavorite() {
        return isFavorite;
    }

    public void setFavorite(String favorite) {
        isFavorite = favorite;
    }
}
