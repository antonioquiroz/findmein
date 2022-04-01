package com.example.findmein.model;

public class Category {

    Integer id;
    Integer imageurl;
    String text;

    public Category(Integer id, Integer imageurl, String text) {
        this.id = id;
        this.imageurl = imageurl;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImageurl() {
        return imageurl;
    }

    public void setImageurl(Integer imageurl) {
        this.imageurl = imageurl;
    }

    public String getTV2(){return text;}
    public void setTV2(String text) {this.text = text;}

}
