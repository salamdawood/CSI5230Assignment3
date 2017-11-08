package edu.csi5230.salamdawood.csi5230assignment3;

import android.graphics.Bitmap;

/**
 * Created by salamdawood on 10/26/17.
 */

public class Product {
    private String name = null;
    private String price = null;
    private String image = null;

    public Product(String name, String price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

