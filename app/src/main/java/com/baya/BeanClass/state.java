package com.baya.BeanClass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prachi on 16-11-2015.
 */
public class state implements Parcelable{
    String _id;
    String state_name;

    public state(Parcel in)
    {
        this._id=in.readString();
        this.state_name=in.readString();
    }
    public static Parcelable.Creator<state> getCreator(){return CREATOR;}
    public static final Parcelable.Creator<state> CREATOR = new Parcelable.Creator<state>() {
        public state createFromParcel(Parcel in) {
            return new state(in);
        }

        public state[] newArray(int size) {
            return new state[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(state_name);
    }
    public state( String _id,
            String state_name)
    {
       this._id=_id;
       this.state_name=state_name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String get_id() {
        return _id;
    }

    public String getState_name() {
        return state_name;
    }
    public state()
    {
    }

}
