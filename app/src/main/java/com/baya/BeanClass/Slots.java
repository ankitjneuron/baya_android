package com.baya.BeanClass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prachi on 17-11-2015.
 */
public class Slots implements Parcelable {
    String availability_day;
    String availability_status;
    String availability_to;
    String availability_from;
    String availability_schedule_from;
    String availability_schedule_to;
    String _id;

    public Slots(Parcel in) {
        this.availability_day = in.readString();
        this.availability_status = in.readString();
        this.availability_to=in.readString();
        this.availability_from=in.readString();
        this.availability_schedule_from=in.readString();
        this.availability_schedule_to=in.readString();
        this._id=in.readString();
    }

    public static Parcelable.Creator<Slots> getCreator() {
        return CREATOR;
    }

    public static final Parcelable.Creator<Slots> CREATOR = new Parcelable.Creator<Slots>() {
        public Slots createFromParcel(Parcel in) {
            return new Slots(in);
        }

        public Slots[] newArray(int size) {
            return new Slots[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(availability_day);
        dest.writeString(availability_status);
        dest.writeString(availability_to);
        dest.writeString(availability_from);
        dest.writeString(availability_schedule_from);
        dest.writeString(availability_schedule_to);
        dest.writeString(_id);
    }

    public Slots(String availability_day,
                 String availability_status,
                 String availability_to,
                 String availability_from,
                 String availability_schedule_from,
                 String availability_schedule_to,
                 String _id) {
        this.availability_day = availability_day;
        this.availability_status = availability_status;
        this.availability_to = availability_to;
        this.availability_from = availability_from;
        this.availability_schedule_from = availability_schedule_from;
        this.availability_schedule_to = availability_schedule_to;
        this._id = _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setAvailability_day(String availability_day) {
        this.availability_day = availability_day;
    }

    public void setAvailability_from(String availability_from) {
        this.availability_from = availability_from;
    }

    public void setAvailability_schedule_from(String availability_schedule_from) {
        this.availability_schedule_from = availability_schedule_from;
    }

    public void setAvailability_schedule_to(String availability_schedule_to) {
        this.availability_schedule_to = availability_schedule_to;
    }

    public void setAvailability_status(String availability_status) {
        this.availability_status = availability_status;
    }

    public void setAvailability_to(String availability_to) {
        this.availability_to = availability_to;
    }

    public String get_id() {
        return _id;
    }

    public String getAvailability_day() {
        return availability_day;
    }

    public String getAvailability_from() {
        return availability_from;
    }

    public String getAvailability_schedule_from() {
        return availability_schedule_from;
    }

    public String getAvailability_schedule_to() {
        return availability_schedule_to;
    }

    public String getAvailability_status() {
        return availability_status;
    }

    public String getAvailability_to() {
        return availability_to;
    }
}

