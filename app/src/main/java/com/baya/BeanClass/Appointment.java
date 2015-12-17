package com.baya.BeanClass;

/**
 * Created by user on 19-Oct-15.
 * Appointment getter setter method
 */
public class Appointment {



    private int imageId;
    private String title;
    private String date;

    public Appointment(int imageId, String title, String date) {
        this.imageId = imageId;
        this.title = title;
        this.date = date;
    }
     public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
