package com.example.api;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public class data {
    public  data(){};
    public data(String name, String price,String link,String image) {
        this.name = name;
        this.price = price;
        this.link=link;
        this.image=image;

    }

    private String name;
    private String price;
    private  String link;
    private  String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
