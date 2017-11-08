package edu.csi5230.salamdawood.csi5230assignment3;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by salamdawood on 11/5/17.
 */

public class CSI5230Assignment3Application extends Application {
    private ArrayList<Product> products = new ArrayList<>();

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
