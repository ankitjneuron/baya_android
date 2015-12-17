package com.baya.BeanClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by prachi on 17-11-2015.
 */
public class Availablities implements Parcelable{
    String _id;
    String status;
    ArrayList<Slots> slots;
    public Availablities(String _id,
            String status,
            ArrayList<Slots> slots)
    {
        this._id=_id;
        this.status=status;
        this.slots=slots;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setSlots(ArrayList<Slots> slots) {
        this.slots = slots;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<Slots> getSlots() {
        return slots;
    }

    public String get_id() {
        return _id;
    }

    public Availablities(Parcel in)
    {
        this._id=in.readString();
        this.status=in.readString();
        this.slots=in.readArrayList(Slots.class.getClassLoader());
    }
    public static Parcelable.Creator<Availablities> getCreator(){return CREATOR;}
    public static final Parcelable.Creator<Availablities> CREATOR = new Parcelable.Creator<Availablities>() {
        public Availablities createFromParcel(Parcel in) {
            return new Availablities(in);
        }

        public Availablities[] newArray(int size) {
            return new Availablities[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(status);
        dest.writeList(slots);
    }
    public Availablities()
    {

    }

}
