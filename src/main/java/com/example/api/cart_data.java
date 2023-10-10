package com.example.api;

public class cart_data {


private String name;
private int price;

private int currentprice;

    public int getCurrentprice() {
        return currentprice;
    }

    public void setCurrentprice(int currentprice) {
        this.currentprice = currentprice;
    }

    private String link;



    public cart_data(String name, int price, int currentprice, String link) {
        this.name = name;
        this.price = price;
        this.currentprice=currentprice;
        this.link = link;
    }

    public cart_data(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
