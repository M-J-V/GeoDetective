package com.example.geodetective;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class questImages {

    private static questImages instance = null;

    private static ArrayList<Bitmap> images = null;

    private questImages(){ }

    public static questImages getInstance() {
        if (instance == null) {
            instance = new questImages();
        }
        return instance;
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public void setImages(ArrayList<Bitmap> questImages) {
        images = questImages;
    }

}

