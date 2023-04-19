package com.example.geodetective.gameComponents;

import android.graphics.Bitmap;

/**
 * The Quest class offers a good structure for keeping information of a quests and operations related
 * to it.
 */
public class Quest {
    private String name;
    private String creator;
    private String description;
    private String hint;
    private Bitmap image;
    private Location location;
    private boolean isStarted = false;

    /**
     * This is the constructor of the class. It creates a quest with inputted information.
     * @param name name of the quest
     * @param creator name of the creator of the quest
     * @param description description of quest
     * @param hint optional hint of the quest
     * @param image image of the quest
     * @param location location of the quest
     */
    public Quest(String name, String creator, String description, String hint, Bitmap image, Location location) {
        this.name = name;
        this.creator = creator;
        this.hint = hint;
        this.description = description;
        this.image = image;
        this.location = location;
    }

    /**
     * This method returns the current name of the quest.
     * @return the current quest name
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method sets the new name of the quest.
     * @param name the new name of the quest
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns the current creator name of the quest.
     * @return current creator name
     */
    public String getCreator() {
        return this.creator;
    }

    /**
     * This method sets the new creator name of the quest.
     * @param creator the new creator name
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * This method returns the current description of the quest.
     * @return the current description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * This method sets the new description of the quest.
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This method returns the current hint of the quest.
     * @return the current hint
     */
    public String getHint() {
        return this.hint;
    }

    /**
     * This method sets the new hint of the quest.
     * @param hint the new hint
     */
    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     * This method returns the current image of the quest.
     * @return the current image
     */
    public Bitmap getImage() {
        return this.image;
    }

    /**
     * This method sets the image of the quest
     * @param image the new image of the quest
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * This method returns the location of the quest.
     * @return location of quest
     */
    public Location getLocation() { return this.location; }

    /**
     * This method sets the new location of the quest.
     * @param location new location
     */
    public void setLocation(Location location) { this.location = location; }

    /**
     * This method starts the quest.
     */
    public void start() {
        this.isStarted = true;
    }

    /**
     * This method stops the quest.
     */
    public void stop() {
        this.isStarted = false;
    }

    /**
     * This method returns whether the quest is started.
     * @return whether quest is started
     */
    public boolean isStarted() {
        return this.isStarted;
    }

}
