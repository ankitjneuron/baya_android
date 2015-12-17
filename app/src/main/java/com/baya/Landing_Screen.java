package com.baya;
/*
* Main class which contain all the fragment
*
*
* */
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.baya.ApiRequest.RequestCompleteListener;
import com.baya.ApiRequest.VolleyRequest;
import com.baya.BeanClass.AppointmentBean;
import com.baya.Helper.Constants;
import com.baya.WebServices.URL;
import com.baya.adapter.List_Adapter;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Landing_Screen extends BaseActivity implements RequestCompleteListener<JSONObject> {
    public static TextView content;
    static ImageButton imageside;
    public static ImageButton image;

    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
    String date="";

    private SharedPreferences preferences;
    private ArrayList<AppointmentBean> mAppList;
    private String basepaths;

    public Landing_Screen() {
        super(R.string.left_and_right);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSlidingMenu().setMode(SlidingMenu.LEFT);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        setContentView(R.layout.activity_landing__screen);
      /*  getSupportFragmentManager()
                .beginTransaction() //Center
                .replace(R.id.content_frame, new SampleListFragment())
                .commit();*/
        //Right
        int position=0;
        preferences =getSharedPreferences(Constants.BAYA, 0);
        Fragment fragment = null;

        fragment = new Mediator();

        if (fragment != null)
        {
            Bundle bundle = new Bundle();
            bundle.putInt("position", getIntent().getExtras().getInt("position"));
            fragment.setArguments(bundle);

            FragmentTransaction ft = Landing_Screen.this.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment);
            ft.commit();

        }
        getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
        getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame_two, new SampleListFragment())
                .commit();
        content=(TextView)findViewById(R.id.content);
        imageside=(ImageButton)findViewById(R.id.imagessidesdrawer);
        image=(ImageButton)findViewById(R.id.imageview);
        Landing_Screen.content.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
       callwebService();
    }

    private void callwebService() {
        Calendar cal=Calendar.getInstance();
        date=output.format(cal.getTime());
        System.out.println("date  " + date + "T00:00:00.000Z");
        try {

            JSONObject dateobj = new JSONObject();
            dateobj.put("date", date+"T00:00:00.000Z");
            System.out.println("date  "+dateobj);
            if (Constants.isInternetOn(Landing_Screen.this)) {
                VolleyRequest volley = new VolleyRequest(Landing_Screen.this,this);
                volley.makeRequest(Request.Method.POST, URL.TWOAPPOINTMENT.getUrl() + "access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""), dateobj, null, "", "UPCOMING", true);
            } else {
                Constants.showMessage("Please Connect to Internet", Landing_Screen.this);
            }

        }
        catch(JSONException e)
        {
        }

    }

    public void leftshow(View view){
        getSlidingMenu().showMenu();
    }
    public void rightshow(View view){
        getSlidingMenu().showSecondaryMenu();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        boolean isPopFragment = false;
//        String currentTabTag = mTabHost.getCurrentTabTag();
//        if (currentTabTag.equals("My Bazr")) {

        isPopFragment = ((BaseContainers) getSupportFragmentManager().findFragmentById(R.id.content_frame)).popFragment();
//        }
        if (!isPopFragment) {
            finish();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("yeess");

 try {

     List<Fragment> fragments = ((BaseContainers) getSupportFragmentManager().findFragmentById(R.id.content_frame)).getChildFragmentManager()
             .getFragments();
     if (fragments != null) {
         for (Fragment fragment : fragments) {
             fragment.onActivityResult(requestCode, resultCode, data);
         }
     }
 }
 catch (Exception e)
 {

 }

    }

    @Override
    public void onTaskComplete(String tag, JSONObject response) {
//

        try {
            if(tag.equals("UPCOMING")) {
                mAppList = new ArrayList<AppointmentBean>();
                if (response.getString("success").equals("true")) {
                    basepaths = response.getString("basePath");
                    System.out.println("date3 " + response.getJSONArray("data").length());
                    if (response.getJSONArray("data").length() != 0) {


                        for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                            JSONObject jos = response.getJSONArray("data").getJSONObject(i);
                            JSONObject businessdetail = response.getJSONArray("data").getJSONObject(i).getJSONObject("listing_id");
                            JSONObject patient_info = response.getJSONArray("data").getJSONObject(i).getJSONObject("patient_info");
                            String doctor_name = "", email = "", address = "", patient_address = "";
                            if(jos.has("doctor_name")) {
                                if (jos.getString("doctor_name").equals("")) {
                                    doctor_name = "No Specific Doctor";
                                } else {
                                    doctor_name = jos.getString("doctor_name");
                                }
                            }
                            else
                            {
                                doctor_name = "";
                            }
                            if (patient_info.has("email")) {
                                email = patient_info.getString("email");
                            } else {
                                email = "";
                            }
                            if (businessdetail.has("address")) {
                                address = businessdetail.getString("address");
                            } else {
                                address = "";
                            }
                            if (patient_info.has("address")) {
                                patient_address = patient_info.getString("address");
                            } else {
                                patient_address = "";
                            }
                            AppointmentBean app = new AppointmentBean(jos.getString("_id"), doctor_name, businessdetail.getString("business_name"), jos.getString("appointment_status"),
                                    jos.getString("user_id"), jos.getString("appointment_reason"), jos.getString("is_new_customer"), jos.getString("appointment_date"), jos.getString("appointment_time"), patient_info.getString("first_name"), patient_info.getString("last_name"), email, patient_info.getString("_id"), patient_address,
                                    patient_info.getString("dob"), basepaths+businessdetail.getString("business_logo"), address + "," + businessdetail.getString("city") + "," + businessdetail.getString("zipcode"), "", businessdetail.getString("_id"));
                            mAppList.add(app);

                        }
                    } else {
//                        Constants.showMessage("Appointments are not available",Landing_Screen.this);
                    }


                } else {
                   // Constants.showMessage("Appointments are not available", Landing_Screen.this);
                }

                List_Adapter adapter = new List_Adapter(Landing_Screen.this,mAppList);
                SampleListFragment.upcominglist.setAdapter(adapter);
               SampleListFragment.setListViewHeightBasedOnChildren1(SampleListFragment.upcominglist, adapter,mAppList);
            }

        }
        catch (Exception e)
        {
            System.out.println("eee  "+e.toString());
        }
    }

}

