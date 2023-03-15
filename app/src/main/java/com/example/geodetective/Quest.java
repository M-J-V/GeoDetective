package com.example.geodetective;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Quest {
    private String name;
    private String creator;
    private String description;
    private String hint;
    private Bitmap image;

    private Location location;

    public Quest(String name, String creator, String description, String hint, Bitmap image) {
        this.name = name;
        this.creator = creator;
        this.hint = hint;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public String getCreator() {
        return this.creator;
    }

    public String getDescription() {
        return this.description;
    }

    public String getHint() {
        return this.hint;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public Location getLocation() { return this.location; }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setLocation(Location location) { this.location = location; }
}
