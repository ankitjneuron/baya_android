package com.baya.BeanClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by prachi on 16-11-2015.
 */
public class Doctor implements Parcelable {
    String image;
    String _id;
    ArrayList<Speciality> arrspecialities;
    String description;
    String name;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    String category;
    public Doctor(String image,
                  String _id,
                  ArrayList<Speciality> arrspecialities,
                  String description,
                  String name, String category) {

        this.image = image;
        this._id = _id;
        this.arrspecialities = arrspecialities;
        this.description = description;
        this.name = name;
        this.category=category;

    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setArrspecialities(ArrayList<Speciality> arrspecialities) {
        this.arrspecialities = arrspecialities;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public ArrayList<Speciality> getArrspecialities() {
        return arrspecialities;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }


    public static Parcelable.Creator<Doctor> getCreator() {
        return CREATOR;
    }

    public Doctor(Parcel in) {
        this.image = in.readString();
        this._id = in.readString();
        this.arrspecialities = in.readArrayList(Speciality.class.getClassLoader());
        this.description = in.readString();
        this.name = in.readString();
        this.category=in.readString();

    }

    public static final Parcelable.Creator<Doctor> CREATOR = new Parcelable.Creator<Doctor>() {
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(_id);
        dest.writeList(arrspecialities);
        dest.writeString(description);
        dest.writeString(name);
        dest.writeString(category);
    }
}
