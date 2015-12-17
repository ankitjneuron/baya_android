package com.baya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.baya.ApiRequest.RequestCompleteListener;
import com.baya.ApiRequest.VolleyRequest;
import com.baya.BeanClass.AppointmentBean;
import com.baya.Helper.Constants;
import com.baya.Helper.HeaderListView;
import com.baya.Helper.SectionAdapter;
import com.baya.WebServices.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



/**
 * Created by prachi on 24-11-2015.
 * Appointment history class in this class we
 * show the history of the appointment
 */
public class Appointment_history extends Fragment implements RequestCompleteListener<JSONObject> {

// declaretion of view
    String tag="";
    private TextView textheading=null;
    HeaderListView stickyList=null;
    private View views=null;
    ArrayList<ArrayList<AppointmentBean>> appointmentarray=null;
    ArrayList<String> datearray=null;
    String date="";
    private SharedPreferences preferences=null;
    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");

    //initialization of the view
    void initview()

    {
        preferences =getActivity().getSharedPreferences(Constants.BAYA, 0);
        appointmentarray=new ArrayList<ArrayList<AppointmentBean>>();
        datearray=new ArrayList<String>();
        tag = "Appointmenthistory";
        callwebService();

        stickyList = (HeaderListView) views.findViewById(R.id.appointmentlist);


    }
// calling appointment histor web service
    private void callwebService() {
        Calendar cal=Calendar.getInstance();
        date=output.format(cal.getTime());
        System.out.println("date  " + date + "T00:00:00.000Z");
        try {

            JSONObject dateobj = new JSONObject();
            dateobj.put("date", date+"T00:00:00.000Z");
            if (Constants.isInternetOn(getActivity())) {
                VolleyRequest volley = new VolleyRequest(getActivity(),this);
                volley.makeRequest(Request.Method.POST, URL.APPOINTMENTHISTORY.getUrl() + "access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""), dateobj, null, "", "", true);
            } else {
                Constants.showMessage("Please Connect to Internet", getActivity());
            }

        }
        catch(JSONException e)
        {
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.appointmenthistory, null);
        Landing_Screen.image.setVisibility(View.GONE);
        Landing_Screen.content.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_5.OTF"));

        Landing_Screen.content.setText("Appointment History");
        initview();

        return views;
    }
// responce of the api request
    @Override
    public void onTaskComplete(String tag, JSONObject responses) {
        try {
            appointmentarray = new ArrayList<ArrayList<AppointmentBean>>();
            if (responses.getString("success").equals("true")) {



                if(responses.getJSONArray("data").length()!=0) {
                    datearray=new ArrayList<String>();
                    ArrayList<AppointmentBean> apparray=new ArrayList<AppointmentBean>();
                    for(int j=0;j<responses.getJSONArray("data").length();j++) {
                        apparray=new ArrayList<AppointmentBean>();
                        datearray.add(responses.getJSONArray("data").getJSONObject(j).getString("date"));
                        JSONObject response = responses.getJSONArray("data").getJSONObject(j);
                        for (int i = 0; i < response.getJSONArray("data").length(); i++) {

                            JSONObject jos = response.getJSONArray("data").getJSONObject(i);
                            JSONObject businessdetail = response.getJSONArray("data").getJSONObject(i).getJSONObject("listing_id");
                            JSONObject patient_info = response.getJSONArray("data").getJSONObject(i).getJSONObject("patient_info");
                            String doctor_name="",email="",address="",patient_address="";
                            if(jos.has("doctor_name")) {
                                if (jos.getString("doctor_name").equals("")) {
                                    doctor_name = "No Specific Doctor";
                                } else {
                                    doctor_name = jos.getString("doctor_name");
                                }
                            }
                            else
                            {
                                doctor_name = "No Specific Doctor";
                            }
                            if(patient_info.has("email"))
                            {
                                email=patient_info.getString("email");
                            }
                            else
                            {
                                email="";
                            }
                            if(businessdetail.has("address"))
                            {
                                address=businessdetail.getString("address");
                            }
                            else
                            {
                                address="";
                            }
                            if(patient_info.has("address"))
                            {
                                patient_address=patient_info.getString("address");
                            }
                            else
                            {
                                patient_address="";
                            }
                            SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMM dd yyyy");
                            String string = "yyyy-MM-dd";
                            DateFormat format = new SimpleDateFormat(string);
                            java.util.Date date = format.parse(jos.getString("appointment_date").split("T")[0]);
                            String  str_selected_date=formatter.format(date);


                            AppointmentBean app = new AppointmentBean(jos.getString("_id"),doctor_name, businessdetail.getString("business_name"), jos.getString("appointment_status"),
                                    jos.getString("user_id"), jos.getString("appointment_reason"), jos.getString("is_new_customer"), str_selected_date, jos.getString("appointment_time"), patient_info.getString("first_name"), patient_info.getString("last_name"), email, patient_info.getString("_id"), patient_address,
                                    patient_info.getString("dob"), businessdetail.getString("business_logo"), address+ "," + businessdetail.getString("city") + "," + businessdetail.getString("zipcode"), "",businessdetail.getString("_id"));
                            apparray.add(app);

                        }
                        appointmentarray.add(apparray);
                    }

                    Appointment_History mAdapter = new Appointment_History(getActivity());
                    stickyList.setAdapter(mAdapter);

                }
                else
                {
                    Constants.showMessage("Appointments are not available",getActivity());
                }


            }
            else {
                Constants.showMessage("Appointments are not available", getActivity());
            }


        }
        catch (Exception e)
        {
            System.out.println("eee  "+e.toString());
        }
    }

// Adapter of the Appointment history
    class Appointment_History extends SectionAdapter {

        private TextView section_text;

        public Appointment_History(Context context) {

        }

        @Override
        public int numberOfSections() {
            return datearray.size();
        }

        @Override
        public int numberOfRows(int section) {

            for (int i = 0; i < datearray.size(); i++) {

                if (i == section)

                    return appointmentarray.get(i).size();
            }
            return 0;
        }

        @Override
        public Object getRowItem(int section, int row) {
            return appointmentarray.get(section).get(row);
        }

        @Override
        public boolean hasSectionHeaderView(int section) {
            return true;
        }

        @Override
        public View getRowView(final int section, final int row, View convertView, ViewGroup parent) {

            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = vi.inflate(R.layout.upcomingdoctorlist, null);
            }


            TextView textname = (TextView) convertView.findViewById(R.id.nameOfClinic);
            TextView textnamedoc = (TextView) convertView.findViewById(R.id.nameOfDoctor);
            TextView timeofappoint = (TextView) convertView.findViewById(R.id.timeOfAppointment);
            TextView timeOfAppointment  = (TextView) convertView.findViewById(R.id.timeOfAppointment);
            TextView  statusOfAppointment= (TextView) convertView.findViewById(R.id.statusOfAppointment);
            textname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_5.OTF"));
            textnamedoc.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
            timeofappoint.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
            timeOfAppointment.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_5.OTF"));
            statusOfAppointment.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_5.OTF"));

            textname.setText(appointmentarray.get(section).get(row).getBusiness_name());
            textnamedoc.setText(appointmentarray.get(section).get(row).getDoctor_name());
            timeofappoint.setText(appointmentarray.get(section).get(row).getAppointment_time());

            statusOfAppointment.setText(appointmentarray.get(section).get(row).getIs_visited().substring(0, 1).toUpperCase() +appointmentarray.get(section).get(row).getIs_visited().substring(1).toLowerCase());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), AppointmentDetail.class);
                    i.putExtra("from","history");
                    i.putExtra("appointment_id",appointmentarray.get(section).get(row).getAppointment_id());
                    startActivity(i);
                }
            });

            return convertView;
        }

        @Override
        public int getSectionHeaderViewTypeCount() {
            return 2;
        }

        @Override
        public int getSectionHeaderItemViewType(int section) {
            return section % 2;
        }

        @Override
        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = vi.inflate(R.layout.upcoming_header, null);

            }

            TextView text = (TextView) convertView.findViewById(R.id.text1);
            text.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_5.OTF"));
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMM dd yyyy");
            String string = "yyyy-MM-dd";
            DateFormat format = new SimpleDateFormat(string);
            java.util.Date date = null;
            try {
                date = format.parse(datearray.get(section).split("T")[0]);

            String  str_selected_date=formatter.format(date);
             text.setText(str_selected_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return convertView;
        }

        @Override
        public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
            super.onRowItemClick(parent, view, section, row, id);

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            try {

                if (observer != null) {

                    super.unregisterDataSetObserver(observer);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
