package com.baya.BeanClass;


import android.os.Parcel;
import android.os.Parcelable;

public class AppointmentBean implements Parcelable {
    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getIs_visited() {
        return is_visited;
    }

    public void setIs_visited(String is_visited) {
        this.is_visited = is_visited;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAppointment_reason() {
        return appointment_reason;
    }

    public void setAppointment_reason(String appointment_reason) {
        this.appointment_reason = appointment_reason;
    }

    public String getIs_new_customer() {
        return is_new_customer;
    }

    public void setIs_new_customer(String is_new_customer) {
        this.is_new_customer = is_new_customer;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public String getLast_name() {
        return patient_last_name;
    }

    public void setLast_name(String last_name) {
        this.patient_last_name = last_name;
    }

    public String getFirst_name() {
        return patient_first_name;
    }

    public void setFirst_name(String first_name) {
        this.patient_first_name = first_name;
    }

    public String get_id() {
        return patient_id;
    }

    public void set_id(String _id) {
        this.patient_id = _id;
    }





    public String getEmail() {
        return patient_email;
    }

    public void setEmail(String email) {
        this.patient_email = email;
    }

    public String getpatientAddress() {
        return patient_address;
    }

    public void setpatientAddress(String address) {
        this.patient_address = address;
    }

    public String getDob() {
        return patient_dob;
    }

    public void setDob(String dob) {
        this.patient_dob = dob;
    }

    public String getBusiness_logo() {
        return business_logo;
    }

    public void setBusiness_logo(String business_logo) {
        this.business_logo = business_logo;
    }

    public AppointmentBean(String appointment_id, String doctor_name, String business_name, String is_visited, String user_id, String appointment_reason, String is_new_customer, String appointment_date, String appointment_time, String patient_first_name, String patient_last_name, String patient_email, String patient_id, String patient_address, String patient_dob, String business_logo, String business_address, String business_category,String business_id) {
        this.appointment_id = appointment_id;
        this.doctor_name = doctor_name;
        this.business_name = business_name;
        this.is_visited = is_visited;
        this.user_id = user_id;
        this.appointment_reason = appointment_reason;
        this.is_new_customer = is_new_customer;
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
        this.patient_last_name = patient_last_name;
        this.patient_first_name = patient_first_name;
        this.patient_id = patient_id;
        this.patient_email = patient_email;
        this.patient_address = patient_address;
        this.patient_dob = patient_dob;
        this.business_logo = business_logo;
        this.business_address = business_address;
        this.business_category = business_category;
        this.business_id=business_id;
    }

    public String getBusiness_category() {

        return business_category;
    }

    public void setBusiness_category(String business_category) {
        this.business_category = business_category;
    }

    public String getBusiness_address() {

        return business_address;
    }

    public void setBusiness_address(String business_address) {
        this.business_address = business_address;
    }

    String appointment_id;
    String doctor_name;
    String business_name;
    String is_visited;
    String user_id;
    String appointment_reason;
    String is_new_customer;
    String appointment_date;
    String appointment_time;
    String patient_last_name;
    String patient_first_name;
    String patient_id;
    String patient_email;
    String patient_address;
    String patient_dob;
    String business_logo;
    String business_address;
    String business_category;

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    String business_id;


    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(appointment_id);
        dest.writeString(doctor_name);
        dest.writeString(business_name);
        dest.writeString(is_visited);
        dest.writeString(user_id);
        dest.writeString(appointment_reason);
        dest.writeString(is_new_customer);
        dest.writeString(appointment_date);

        dest.writeString(appointment_time);
        dest.writeString(patient_last_name);
        dest.writeString(patient_first_name);
        dest.writeString(patient_id);

        dest.writeString(patient_email);
        dest.writeString(patient_address);
        dest.writeString(patient_dob);
        dest.writeString(business_logo);
        dest.writeString(business_address);
        dest.writeString(business_category);
         dest.writeString(business_id);
    }

    public static final Parcelable.Creator<AppointmentBean> CREATOR = new Parcelable.Creator<AppointmentBean>() {
        public AppointmentBean createFromParcel(Parcel in) {
            return new AppointmentBean(in);
        }

        public AppointmentBean[] newArray(int size) {
            return new AppointmentBean[size];
        }
    };

    @SuppressWarnings("unchecked")
    public AppointmentBean(Parcel in) {

        this.appointment_id = in.readString();
        this.doctor_name = in.readString();
        this.business_name = in.readString();
        this.is_visited = in.readString();
        this.user_id = in.readString();
        this.appointment_reason = in.readString();
        this.is_new_customer = in.readString();
        this.appointment_date = in.readString();

        this.appointment_time = in.readString();
        this.patient_last_name = in.readString();
        this.patient_first_name = in.readString();
        this.patient_id = in.readString();
        this.patient_email = in.readString();
        this.patient_address = in.readString();
        this.patient_dob = in.readString();
        this.business_logo = in.readString();
        this.business_address = in.readString();
        this.business_category = in.readString();
        this.business_id=in.readString();
    }


    public static Parcelable.Creator<AppointmentBean> getCreator() {
        return CREATOR;
    }
}
