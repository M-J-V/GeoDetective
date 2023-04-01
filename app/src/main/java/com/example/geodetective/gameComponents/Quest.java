package com.example.geodetective.gameComponents;

import android.graphics.Bitmap;

public class Quest {
    private String name;
    private String creator;
    private String description;
    private String hint;
    private Bitmap image;
    private Location location;
    private boolean isStarted = false;

    public Quest(String name, String creator, String description, String hint, Bitmap image, Location location) {
        this.name = name;
        this.creator = creator;
        this.hint = hint;
        this.description = description;
        this.image = image;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHint() {
        return this.hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Location getLocation() { return this.location; }

    public void setLocation(Location location) { this.location = location; }

    public void start() {
        this.isStarted = true;
    }

    public void stop() {
        this.isStarted = false;
    }

    public boolean isStarted() {
        return this.isStarted;
    }

}
