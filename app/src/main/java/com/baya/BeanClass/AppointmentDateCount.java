package com.baya.BeanClass;

/**
 * Created by hp on 02-12-2015.
 */
public class AppointmentDateCount {
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public AppointmentDateCount(String date, String count) {
        this.date = date;
        this.count = count;
    }

    String date;
    String count;

}
