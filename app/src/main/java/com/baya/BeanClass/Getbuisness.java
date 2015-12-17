package com.baya.BeanClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by prachi on 16-11-2015.
 */
public class Getbuisness implements Parcelable {
    private  String availability_slot;
    String business_logo;
    String who_added;
    String phone_number;
    String is_verified;
    String status;
    ArrayList<Buisnesscategories> buisnesscategories;
    String zipcode;
    String __v;
    state state;
    String country;
    String city;
    String business_name;
    String isClaimed;
    String updated_at;
    String _id;
    String address;
    String who_added_role;
    String lattitude;
    ArrayList<String> arrayclaim;
    String longtitude;
    String created_at;
    ArrayList<Doctor> arrayDoctor;
    String is_approved;
    String neighbour;
    String about_us;

    public String getSlots_id() {
        return slots_id;
    }

    public void setSlots_id(String slots_id) {
        this.slots_id = slots_id;
    }

    public String getSlots_Status() {
        return slots_Status;
    }

    public void setSlots_Status(String slots_Status) {
        this.slots_Status = slots_Status;
    }

    public ArrayList<Slots> getArray_slots() {
        return array_slots;
    }

    public void setArray_slots(ArrayList<Slots> array_slots) {
        this.array_slots = array_slots;
    }

    String slots_Status;
    String slots_id;
    ArrayList<Slots> array_slots;
    Availablities availabilites;

    public Getbuisness(String business_logo,
                       String who_added,
                       String phone_number,
                       String is_verified,
                       String status,
                       ArrayList<Buisnesscategories> buisnesscategories,
                       String zipcode,
                       String __v,
                       state state,
                       String country,
                       String city,
                       String business_name,
                       String isClaimed,
                       String updated_at,
                       String _id,
                       String address,
                       String who_added_role,
                       String lattitude, ArrayList<String> arrayclaim,
                       String longtitude,
                       String created_at, ArrayList<Doctor> arrayDoctor,
                       String is_approved,
                       String neighbour, String about_us,
                      String slots_Status,String slots_id,ArrayList<Slots> array_slots,String availability_slot ) {
        super();
        this.business_logo = business_logo;
        this.who_added = who_added;
        this.phone_number = phone_number;
        this.is_verified = is_verified;
        this.status = status;
        this.buisnesscategories = buisnesscategories;
        this.zipcode = zipcode;
        this.__v = __v;
        this.state = state;
        this.country = country;
        this.city = city;
        this.business_name = business_name;
        this.isClaimed = isClaimed;
        this.updated_at = updated_at;
        this._id = _id;
        this.address = address;
        this.who_added_role = who_added_role;
        this.lattitude = lattitude;
        this.arrayclaim = arrayclaim;
        this.longtitude = longtitude;
        this.created_at = created_at;
        this.arrayDoctor = arrayDoctor;
        this.is_approved = is_approved;
        this.neighbour = neighbour;
        this.about_us = about_us;
        this.slots_Status = slots_Status;
        this.slots_id=slots_id;
        this.array_slots=array_slots;
        this.availability_slot=availability_slot;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvailability_slot() {
        return availability_slot;
    }

    public void setAvailability_slot(String availability_slot) {
        this.availability_slot = availability_slot;
    }

    public Availablities getAvailabilites() {
        return availabilites;
    }

    public void setAvailabilites(Availablities availabilites) {
        this.availabilites = availabilites;
    }

    public void setBuisnesscategories(ArrayList<Buisnesscategories> buisnesscategories) {
        this.buisnesscategories = buisnesscategories;
    }

    public void setBusiness_logo(String business_logo) {
        this.business_logo = business_logo;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setIs_approved(String is_approved) {
        this.is_approved = is_approved;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public void setIsClaimed(String isClaimed) {
        this.isClaimed = isClaimed;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public void setNeighbour(String neighbour) {
        this.neighbour = neighbour;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setState(com.baya.BeanClass.state state) {
        this.state = state;
    }

    public void setArrayclaim(ArrayList<String> arrayclaim) {
        this.arrayclaim = arrayclaim;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setWho_added(String who_added) {
        this.who_added = who_added;
    }

    public void setArrayDoctor(ArrayList<Doctor> arrayDoctor) {
        this.arrayDoctor = arrayDoctor;
    }

    public void setWho_added_role(String who_added_role) {
        this.who_added_role = who_added_role;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setAbout_us(String about_us) {
        this.about_us = about_us;
    }


    public String get__v() {
        return __v;
    }

    public com.baya.BeanClass.state getState() {
        return state;
    }

    public String get_id() {
        return _id;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<Buisnesscategories> getBuisnesscategories() {
        return buisnesscategories;
    }

    public String getBusiness_logo() {
        return business_logo;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getIs_approved() {
        return is_approved;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public String getIsClaimed() {
        return isClaimed;
    }

    public String getLattitude() {
        return lattitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public String getNeighbour() {
        return neighbour;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<String> getArrayclaim() {
        return arrayclaim;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getWho_added() {
        return who_added;
    }

    public ArrayList<Doctor> getArrayDoctor() {
        return arrayDoctor;
    }

    public String getWho_added_role() {
        return who_added_role;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getAbout_us() {
        return about_us;
    }



    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(business_logo);
        dest.writeString(who_added);
        dest.writeString(phone_number);
        dest.writeString(is_verified);
        dest.writeString(status);
        dest.writeList(buisnesscategories);
        dest.writeString(zipcode);
        dest.writeString(__v);
        state = new state();
        dest.writeString(state.get_id());
        dest.writeString(state.getState_name());
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(business_name);
        dest.writeString(isClaimed);
        dest.writeString(updated_at);
        dest.writeString(_id);
        dest.writeString(address);
        dest.writeString(who_added_role);
        dest.writeString(lattitude);
        dest.writeList(arrayclaim);
        dest.writeString(longtitude);
        dest.writeString(created_at);
        dest.writeList(arrayDoctor);
        dest.writeString(is_approved);
        dest.writeString(neighbour);
        dest.writeString(about_us);
        dest.writeString(slots_Status);
        dest.writeString(slots_id);
        dest.writeList(array_slots);
        dest.writeString(availability_slot);
    }

    public static final Parcelable.Creator<Getbuisness> CREATOR = new Parcelable.Creator<Getbuisness>() {
        public Getbuisness createFromParcel(Parcel in) {
            return new Getbuisness(in);
        }

        public Getbuisness[] newArray(int size) {
            return new Getbuisness[size];
        }
    };

    @SuppressWarnings("unchecked")
    public Getbuisness(Parcel in) {

        this.business_logo = in.readString();
        this.who_added = in.readString();
        this.phone_number = in.readString();
        this.is_verified = in.readString();
        this.status = in.readString();
        this.buisnesscategories = in.readArrayList(Buisnesscategories.class.getClassLoader());
        this.zipcode = in.readString();
        this.__v = in.readString();
        state = new state();
        this.state.set_id(in.readString());
        this.state.setState_name(in.readString());
        this.country = in.readString();
        this.city = in.readString();
        this.business_name = in.readString();
        this.isClaimed = in.readString();
        this.updated_at = in.readString();
        this._id = in.readString();
        this.address = in.readString();
        this.who_added_role = in.readString();
        this.lattitude = in.readString();
        this.arrayclaim = in.readArrayList(String.class.getClassLoader());
        this.longtitude = in.readString();
        this.created_at = in.readString();
        this.arrayDoctor = in.readArrayList(Doctor.class.getClassLoader());
        this.is_approved = in.readString();
        this.neighbour = in.readString();
        this.about_us = in.readString();
        this.slots_Status=in.readString();
        this.slots_id=in.readString();
        this.array_slots=in.readArrayList(Slots.class.getClassLoader());
         this.availability_slot=in.readString();
    }


    public static Parcelable.Creator<Getbuisness> getCreator() {
        return CREATOR;
    }
}
