package com.baya.BeanClass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prachi on 16-11-2015.
 */
public class Buisnesscategories implements Parcelable {
    String _id;
    Categories categories;

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public static Parcelable.Creator<Buisnesscategories> getCreator()
    {
        return CREATOR;
    }
    public Buisnesscategories(Parcel in)
    {
        this._id=in.readString();;
        categories=new Categories();
        categories.setCategoryicon(in.readString());
        categories.setCategoryname(in.readString());
        categories.setIds(in.readString());
    }
    public static final Parcelable.Creator<Buisnesscategories> CREATOR = new Parcelable.Creator<Buisnesscategories>() {
        public Buisnesscategories createFromParcel(Parcel in) {
            return new Buisnesscategories(in);
        }

        public Buisnesscategories[] newArray(int size) {
            return new Buisnesscategories[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(categories.getCategoryicon());
        dest.writeString(categories.getCategoryname());
        dest.writeString(categories.getIds());
    }
     public Buisnesscategories(String _id,
        Categories categories)
     {
         this._id=_id;
         this.categories=categories;
     }

}
