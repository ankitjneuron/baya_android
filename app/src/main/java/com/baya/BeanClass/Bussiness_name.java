package com.baya.BeanClass;


public class Bussiness_name
{
    String bussiness_name;
    String doctor_name;
    String time ;

    public Bussiness_name(String bussiness_name, String doctor_name, String time) {
        this.bussiness_name = bussiness_name;
        this.doctor_name = doctor_name;
        this.time = time;
    }

    public String getBussiness_name() {
        return bussiness_name;
    }

    public void setBussiness_name(String bussiness_name) {
        this.bussiness_name = bussiness_name;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
