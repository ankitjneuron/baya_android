package com.baya;
/*
* Created by Neeraj Rathore
* For showing the appointment detail
*
* */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.baya.ApiRequest.RequestCompleteListener;
import com.baya.ApiRequest.VolleyRequest;
import com.baya.BeanClass.Categories;
import com.baya.Helper.Constants;
import com.baya.WebServices.URL;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AppointmentDetail extends Activity implements RequestCompleteListener<JSONObject>{

    // declation of the variabels
    ImageButton back;
    TextView heading;
    ImageView imagedoc;
    TextView textview_doc;
    TextView textview_number;
    TextView textview_speciality;
    TextView textview_address, textview_name, textview_reason;
    TextView textview_date, textview_time, textview_specific;
    RelativeLayout relativelayout_reschedule, relativelayout_cancel, visited,accept_layout;
    ImageLoader imageLoader;
    DisplayImageOptions ooptions;
    String tag;
    TextView viewreasontext;
    TextView texview_paitent;
    TextView view_patientname;
    TextView view_patientemail;
    TextView view_patientphone;
    TextView view_pateientgender;
    TextView view_patientbirthday;
    TextView view_patientinsurance, cancel, reschedule,textview_get;
    LinearLayout reschedulecancel=null;
    private SharedPreferences preferences;
    String category = "";
String appointment_id="",listin_id="";
    ArrayList<Categories> datajson = null;


    // initialization of the views
    void initview() {
        textview_get=(TextView) findViewById(R.id.textview_get);
        back = (ImageButton) findViewById(R.id.back);
        heading = (TextView) findViewById(R.id.content);
        imagedoc = (ImageView) findViewById(R.id.imageview_buisnesslistings_images);
        textview_doc = (TextView) findViewById(R.id.textview_buisnesslistings_nam);
        textview_number = (TextView) findViewById(R.id.textview_bussiness_listing_contatctno);
        textview_speciality = (TextView) findViewById(R.id.bussinesslisting_profession_textview);
        textview_address = (TextView) findViewById(R.id.bussinesslisting_address_textview);
        textview_date = (TextView) findViewById(R.id.datetext);
        textview_time = (TextView) findViewById(R.id.timetext);
        textview_specific = (TextView) findViewById(R.id.specificdoctortxt);
        textview_name = (TextView) findViewById(R.id.specificdoctorname);
        textview_reason = (TextView) findViewById(R.id.reasontext);
        viewreasontext = (TextView) findViewById(R.id.status);
        texview_paitent = (TextView) findViewById(R.id.patientinformationtext);
        view_patientname = (TextView) findViewById(R.id.patient_name);
        view_patientemail = (TextView) findViewById(R.id.patient_email);
        view_patientphone = (TextView) findViewById(R.id.patient_phone);
        view_pateientgender = (TextView) findViewById(R.id.patient_gender);
        view_patientbirthday = (TextView) findViewById(R.id.patient_birthday);
        view_patientinsurance = (TextView) findViewById(R.id.patient_insurance);
        cancel = (TextView) findViewById(R.id.textcancel);
        reschedule = (TextView) findViewById(R.id.textreschedule);
        visited = (RelativeLayout) findViewById(R.id.getvisited);
        reschedulecancel=(LinearLayout)findViewById(R.id.reschedulecancel);
        relativelayout_cancel = (RelativeLayout) findViewById(R.id.cancel_layout);
        relativelayout_reschedule = (RelativeLayout) findViewById(R.id.rescheduleLayout2);
        accept_layout=(RelativeLayout)findViewById(R.id.accept_layout);
        relativelayout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCancelService();
            }
        });
        accept_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callacceptService();
            }
        });
        relativelayout_reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AppointmentDetail.this,Bussiness_Available_slots.class);
                i.putExtra("reschedule",getIntent().getExtras().getString("appointment_id"));
                i.putExtra("business_id",listin_id);
               AppointmentDetail.this.startActivityForResult(i, 1234);
            }
        });
        if(getIntent().getExtras().getString("from").equals("history"))
        {
            reschedulecancel.setVisibility(View.GONE);
        }
        else
        {

            reschedulecancel.setVisibility(View.VISIBLE);
        }
        appointment_id=getIntent().getExtras().getString("appointment_id");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();

                setResult(1234, i);
                AppointmentDetail.this.finish();
            }
        });
    }



    @Override
    public void onBackPressed() {
        Intent i=new Intent();

        setResult(1234, i);
        AppointmentDetail.this.finish();
    }
// calling cancel api
    void callCancelService()
    {
        if (Constants.isInternetOn(AppointmentDetail.this)) {
            VolleyRequest volley = new VolleyRequest(AppointmentDetail.this,this);
            volley.makeRequest(Request.Method.GET, URL.CANCELAPPOINTMENT.getUrl()+getIntent().getExtras().getString("appointment_id")+"/status/cancelled?"+ "access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""), null, null, "", "Cancel", true);
        } else {
            Constants.showMessage("Please Connect to Internet", AppointmentDetail.this);
        }
    }

    // calling acceptance api
    void callacceptService()
    {
        if (Constants.isInternetOn(AppointmentDetail.this)) {
            VolleyRequest volley = new VolleyRequest(AppointmentDetail.this,this);
            volley.makeRequest(Request.Method.GET, URL.CANCELAPPOINTMENT.getUrl()+getIntent().getExtras().getString("appointment_id")+"/status/accepted?"+ "access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""), null, null, "", "Accept", true);
        } else {
            Constants.showMessage("Please Connect to Internet", AppointmentDetail.this);
        }
    }

    // calling detail api
    private void callWebService()
    {
        try {

            JSONObject data = new JSONObject();
            data.put("id",getIntent().getExtras().getString("appointment_id"));
            System.out.println("url  "+URL.GETAPPOINTMENTDETAIL.getUrl() +"/"+getIntent().getExtras().getString("appointment_id")+ "?access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""));
            if (Constants.isInternetOn(AppointmentDetail.this)) {
                VolleyRequest volley = new VolleyRequest(AppointmentDetail.this, AppointmentDetail.this);
                volley.makeRequest(Request.Method.GET, URL.GETAPPOINTMENTDETAIL.getUrl() +"/"+getIntent().getExtras().getString("appointment_id")+ "?access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""), null, null, "", "DETAIL", true);
            } else {
                Constants.showMessage("Please Connect to Internet", AppointmentDetail.this);
            }

        }
        catch(JSONException e)
        {
        }

    }

// initialize fonts
    void inittypeS() {
        textview_get.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        heading.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_doc.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_number.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_speciality.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_address.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_date.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_time.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_specific.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_name.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_reason.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        viewreasontext.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        texview_paitent.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        view_patientname.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        view_patientemail.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        view_patientphone.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        view_pateientgender.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        view_patientbirthday.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        view_patientinsurance.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        reschedule.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        cancel.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        preferences = AppointmentDetail.this.getSharedPreferences(Constants.BAYA, 0);
        initview();
        Gson gson = new Gson();
        Type testType = new com.google.gson.reflect.TypeToken<ArrayList<Categories>>() {
        }.getType();

        String jsondata = preferences.getString("Category", "");

        Gson gson1 = new Gson();

        if (jsondata != null && jsondata.length() > 0) {
            datajson=new ArrayList<Categories>();
            datajson = gson1.fromJson(jsondata, testType);
            System.out.println("cate  "+datajson.toString());
        }
        else
        {

        }
        inittypeS();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(AppointmentDetail.this));
        ooptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.xdefault).showImageForEmptyUri(R.drawable.xdefault)
                .showImageOnFail(R.drawable.xdefault).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(900))
                .build();
        callWebService();

    }

// responce of the api request
    @Override
    public void onTaskComplete(String tag, JSONObject response) {
        try {
            if (tag.equals("DETAIL")) {
                if (response.getString("success").equals("true"))
                {
                    JSONObject data=response.getJSONObject("data");

                    JSONObject listing=data.getJSONObject("listing_id");
                    JSONObject patient_info=data.getJSONObject("patient_info");
                   listin_id=listing.getString("_id");
                    JSONArray business_categoryobj=listing.getJSONArray("business_category");
                    ArrayList<String> arrayid=new ArrayList<String>();

                    if(listing.has("business_category"))
                    {

                        for(int k=0;k<business_categoryobj.length();k++)
                        {
                            JSONObject cat=business_categoryobj.getJSONObject(k);
                            arrayid.add(cat.getString("cat_id"));
                        }
                    }
                    if(getIntent().getExtras().getString("from").equals("history")) {
                        reschedulecancel.setVisibility(View.GONE);
                    }
                    else {
                        if (data.has("rescheduled_by")) {
                            if (!data.getString("rescheduled_by").equals("patient")) {
                                if(data.getString("appointment_status").equals("accepted"))
                                {
                                    accept_layout.setVisibility(View.GONE);
                                }
                                else {
                                    accept_layout.setVisibility(View.VISIBLE);
                                }
                            } else {
                                accept_layout.setVisibility(View.GONE);
                            }
                        } else {
                            accept_layout.setVisibility(View.GONE);
                        }
                    }
                    System.out.println("category "+arrayid.toString());
                    String category = "";
                    for(int l=0;l<datajson.size();l++)
                    {
                        System.out.println(  "id  "+datajson.get(l).getIds());
                        if(arrayid.contains(datajson.get(l).getIds())) {
                            category = category + ", " + datajson.get(l).getCategoryname();
                        }
                    }

                    if(category.length()!=0) {
                        category = category.substring(1, category.length());
                        textview_speciality.setText(category);
                    }

                    view_pateientgender.setText(patient_info.getString("gender").substring(0, 1).toUpperCase() + patient_info.getString("gender").substring(1).toLowerCase());
                    view_patientbirthday.setText(patient_info.getString("dob"));
                    view_patientemail.setText(patient_info.getString("email"));
                    view_patientphone.setText(patient_info.getString("Contact_no"));

                    textview_get.setText( data.getString("appointment_status").substring(0, 1).toUpperCase() + data.getString("appointment_status").substring(1).toLowerCase());
                    if(data.getString("appointment_status").equals("cancelled"))
                    {
                        reschedulecancel.setVisibility(View.GONE);
                    }
//                    else
//                    {
//                        reschedulecancel.setVisibility(View.VISIBLE);
//                    }
                    if(data.has("insurance")) {
                        view_patientinsurance.setText(data.getString("insurance"));
                    }
                    textview_doc.setText(listing.getString("business_name"));
                    textview_number.setText(listing.getString("phone_number"));
                    textview_address.setText(listing.getString("address"));
                    SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMM dd yyyy");
                    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
                    textview_date.setText(data.getString("appointment_date").split("T")[0]);
                    textview_time.setText(data.getString("appointment_time"));
                    viewreasontext.setText(data.getString("appointment_reason"));
                     imageLoader.displayImage(response.getString("basePath")+listing.getString("business_logo"), imagedoc, ooptions);
                    view_patientname.setText(patient_info.getString("first_name") + " " + patient_info.getString("last_name"));


                    if(data.has("doctor_name"))
                    textview_name.setText(data.getString("doctor_name"));
                    else
                        textview_name.setText("");

                }
                else
                {
Constants.showMessage("No Appointment detail found",AppointmentDetail.this);
                }
            }
            else if(tag.equals("Accept"))
            {
                if (response.getString("success").equals("true"))
                {
                    Constants.showMessage("Accepted Successfully", AppointmentDetail.this);
//                    callWebService();
                    Intent i=new Intent();

                    setResult(1234, i);
                    AppointmentDetail.this.finish();
                }
                else
                {
                    Constants.showMessage("Something went wrong", AppointmentDetail.this);
                }
            }
            else
            {
                if (response.getString("success").equals("true"))
                {
                    Constants.showMessage("Cancelled Successfully", AppointmentDetail.this);
//                    callWebService();
                    Intent i=new Intent();

                    setResult(1234, i);
                    AppointmentDetail.this.finish();
                }
                else
                {
                    Constants.showMessage("Something went wrong", AppointmentDetail.this);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("exception  "+e.toString());
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((resultCode==1234)&&(requestCode==1234))
        {
            Intent i=new Intent();

            setResult(1234, i);
            AppointmentDetail.this.finish();
        }
        else
        {

        }
    }
}
