package com.example.geodetective.singletons;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * The QuestImages class is a singleton that stores the array of quest images to be displayed in the
 * ListOfQuests activity.
 */
public class QuestImages {

    private static QuestImages instance = null;

    private static ArrayList<Bitmap> images = null;

    private QuestImages(){ }

    /**
     * This method returns the already existing instance of the class or a
     * new one (if it doesn't exist already).
     * @return instance of the class
     */
    public static QuestImages getInstance() {
        if (instance == null) {
            instance = new QuestImages();
        }
        return instance;
    }

    /**
     * This method returns the array of images to be displayed in the ListOfQuests activity.
     * @return array of images
     */
    public ArrayList<Bitmap> getImages() {
        return images;
    }

    /**
     * This method sets the new array of images to be displayed in the ListOfQuests activity.
     * @param questImages new array of images
     */
    public void setImages(ArrayList<Bitmap> questImages) {
        images = questImages;
    }

}

