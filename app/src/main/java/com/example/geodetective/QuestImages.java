package com.example.geodetective;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class QuestImages {

    private static QuestImages instance = null;

    private static ArrayList<Bitmap> images = null;

    private QuestImages(){ }

    public static QuestImages getInstance() {
        if (instance == null) {
            instance = new QuestImages();
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

