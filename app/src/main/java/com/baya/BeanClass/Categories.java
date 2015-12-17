package com.baya.BeanClass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prachi on 06-11-2015.
 */
public class Categories implements Parcelable {
    String categoryname;
    String ids;
    String categoryicon;
    String isselected;
    String state;
   public Categories(String categoryname,String id,String categoryicon)
   {
       this.categoryname=categoryname;
       this.categoryicon=categoryicon;
       this.ids=id;
   }
    public Categories(String categoryname,String id,String categoryicon, String isselected,
            String state)
    {
        this.categoryname=categoryname;
        this.categoryicon=categoryicon;
        this.ids=id;
        this.isselected=isselected;
        this.state=state;
    }
    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public void setCategoryicon(String categoryicon) {
        this.categoryicon = categoryicon;
    }

    public void setIsselected(String isselected) {
        this.isselected = isselected;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIds() {
        return ids;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public String getCategoryicon() {
        return categoryicon;
    }

    public String getIsselected() {
        return isselected;
    }

    public String getState() {
        return state;
    }

    public static Parcelable.Creator<Categories> getCreator()
    {
        return CREATOR;
    }
    public Categories(Parcel in)
    {
        this.categoryname=in.readString();;
        this.ids=in.readString();
        this.categoryicon=in.readString();
this.isselected=in.readString();
        this.state=in.readString();
    }
    public static final Parcelable.Creator<Categories> CREATOR = new Parcelable.Creator<Categories>() {
        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryname);
        dest.writeString(ids);
        dest.writeString(categoryicon);
        dest.writeString(isselected);
        dest.writeString(state);
    }
    public Categories()
    {

    }
}
