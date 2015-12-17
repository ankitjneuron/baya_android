package com.baya.BeanClass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prachi on 17-11-2015.
 */
public class Speciality implements Parcelable{
    String _id;
    Categories ca;

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(_id);
       ca=new Categories();
        dest.writeString(ca.getCategoryname());
        dest.writeString(ca.getIds());
        dest.writeString(ca.getCategoryicon());


    }

    public static final Parcelable.Creator<Speciality> CREATOR = new Parcelable.Creator<Speciality>() {
        public Speciality createFromParcel(Parcel in) {
            return new Speciality(in);
        }

        public Speciality[] newArray(int size) {
            return new Speciality[size];
        }
    };

    @SuppressWarnings("unchecked")
    public Speciality(Parcel in) {

        this._id = in.readString();
        ca = new Categories();
        this.ca.setCategoryname(in.readString());
        this.ca.setIds(in.readString());
        this.ca.setCategoryicon(in.readString());


    }

    public static Parcelable.Creator<Speciality> getCreator(){return  CREATOR;}

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setCa(Categories ca) {
        this.ca = ca;
    }

    public String get_id() {
        return _id;
    }

    public Categories getCa() {
        return ca;
    }
    public Speciality(String _id,Categories ca)
    {

    }
}
