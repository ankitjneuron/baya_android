package com.baya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;


import com.android.volley.Request;
import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baya.ApiRequest.RequestCompleteListener;
import com.baya.ApiRequest.VolleyRequest;
import com.baya.BeanClass.AppointmentBean;
import com.baya.Helper.CalendarView;
import com.baya.Helper.Constants;
import com.baya.WebServices.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpcomingAppointments extends Fragment implements RequestCompleteListener<JSONObject> ,CalendarView.OnDateSelect{
String selecteddate="";
    private ArrayList<AppointmentBean> mAppList;
    private Swipe_menu_Adapter adapter;
    SharedPreferences preferences;
    SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMM dd yyyy");
    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
    JSONObject appointment_data=null;
    Date date=null;
    View views;
    private SwipeMenuListView listView;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.up_comming_appointment, null);
        Landing_Screen.content.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        Landing_Screen.image.setVisibility(View.VISIBLE);
        Landing_Screen.image.setImageResource(R.drawable.eye_view);
        Landing_Screen.content.setText("Upcoming Appointment");
        Landing_Screen.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Upcomingappointment.class);
                intent.putExtra("tag","UpcomingAppointment");
                getActivity().startActivity(intent);
            }
        });
        preferences = getActivity().getSharedPreferences(Constants.BAYA, 0);
        listView = (SwipeMenuListView)views.findViewById(R.id.listView);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            int bottomPadding =0;
            System.out.println("height  " + getNavBarHeight());
//            RelativeLayout.LayoutParams p=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int mainheight = metrics.heightPixels;
            DisplayMetrics metrics1 = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics1);
            int usableHeight = metrics1.heightPixels;
            System.out.println("hegit  "+mainheight+"  "+usableHeight);

//            p.setMargins(0, 0, 0,usableHeight-mainheight);
//            p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            findtexts.setLayoutParams(p);
            listView.setPadding(0, 0, 0, usableHeight-mainheight);
        } else{
            // do something for phones running an SDK before lollipop
           // findtexts.setPadding(10, 20, 10, 20);
        }
        adapter = new Swipe_menu_Adapter();
        mAppList = new ArrayList<AppointmentBean>();
        CalendarView.setListener(this);

        listView.setAdapter(adapter);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                switch (menu.getViewType()) {
                    case 0:
                        createMenu1(menu);
                        break;

                }
            }

            private void createMenu1(SwipeMenu menu) {

                // create "delete" item


                SwipeMenuItem item1 = new SwipeMenuItem(getActivity());
                // set item background
                item1.setBackground(new ColorDrawable(Color.parseColor("#373737")));
                // set item width
                item1.setWidth(dp2px(110));
                // set a icon
                item1.setIcon(R.drawable.cancel01);
                // add to menu
                menu.addMenuItem(item1);



                SwipeMenuItem item2 = new SwipeMenuItem(
                        getContext());
                // set item background
                item2.setBackground(new ColorDrawable(Color.parseColor("#0EA1D4")));
                // set item width
                item2.setWidth(dp2px(110));
                // set item title
                // set a icon
                item2.setIcon(R.drawable.reschedule01);
                // add to menu
                menu.addMenuItem(item2);


            }

                  };
        // set creator
        listView.setMenuCreator(creator);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent i = new Intent(getActivity(), AppointmentDetail.class);
               i.putExtra("from","upcoming");
                       i.putExtra("appointment_id", mAppList.get(position).getAppointment_id());
               getActivity().startActivityForResult(i, 1234);
           }
       });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                AppointmentBean item = mAppList.get(position);
                switch (index) {
                    case 0:
                        callCancelService(position);
                        break;
                    case 1:

                        Intent i=new Intent(getActivity(),Bussiness_Available_slots.class);
                        i.putExtra("reschedule",mAppList.get(position).getAppointment_id());
                        i.putExtra("business_id",mAppList.get(position).getBusiness_id());
                        getActivity().startActivityForResult(i, 1234);
                        break;
                }
                return false;
            }
        });


        try {
            selecteddate=CalendarView.selected_date_time.getText().toString();
            System.out.println("date "+selecteddate.toString());
            date = formatter.parse(selecteddate);
            System.out.println("date1 "+date.toString());
            selecteddate=output.format(date);
            System.out.println("date2 " + selecteddate);
            callWebService();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }



return views;
    }
    private int getNavBarHeight() {
        Resources r = getResources();
        int id = r.getIdentifier("navigation_bar_height", "dimen", "android");
        return r.getDimensionPixelSize(id);
    }
    void callCancelService(int position)
    {
        if (Constants.isInternetOn(getActivity())) {
            VolleyRequest volley = new VolleyRequest(getActivity(),this);
            volley.makeRequest(Request.Method.GET, URL.CANCELAPPOINTMENT.getUrl()+mAppList.get(position).getAppointment_id()+"/status/cancelled?"+ "access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""), null, null, "", "Cancel", true);
        } else {
            Constants.showMessage("Please Connect to Internet", getActivity());
        }
    }

    private void callWebService()
    {
        try {
            System.out.println("date3 " + selecteddate+"T00:00:00.000Z");
            JSONObject date = new JSONObject();
            date.put("appointment_date", selecteddate);
            if (Constants.isInternetOn(getActivity())) {
                VolleyRequest volley = new VolleyRequest(getActivity(), UpcomingAppointments.this);
                volley.makeRequest(Request.Method.POST, URL.UPCOMINGAPPOINTMENT.getUrl() + "access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""), date, null, "", "UPCOMING", true);
            } else {
                Constants.showMessage("Please Connect to Internet", getActivity());
            }

        }
            catch(JSONException e)
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
                    System.out.println("date3 " + response.getJSONArray("data").length());
                    if (response.getJSONArray("data").length() != 0) {


                        for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                            JSONObject jos = response.getJSONArray("data").getJSONObject(i);
                            JSONObject businessdetail = response.getJSONArray("data").getJSONObject(i).getJSONObject("listing_id");
                            JSONObject patient_info = response.getJSONArray("data").getJSONObject(i).getJSONObject("patient_info");

                            String doctor_name = "", email = "", address = "", patient_address = "";
                            if (jos.getString("doctor_id").equals("")) {
                                doctor_name = "No Specific Doctor";
                            } else {
                                JSONArray doctors=businessdetail.getJSONArray("doctors");
                                for (int j = 0; j < doctors.length();j++) {
                                    JSONObject jobj=doctors.getJSONObject(j);
                                    if(jobj.getString("_id").equals(jos.getString("doctor_id"))) {
                                        doctor_name = jobj.getString("name");
                                    }
                                }
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
                                    patient_info.getString("dob"), businessdetail.getString("business_logo"), address + "," + businessdetail.getString("city") + "," + businessdetail.getString("zipcode"), "", businessdetail.getString("_id"));
                            mAppList.add(app);

                        }
                    } else {
                        Constants.showMessage("Appointments are not available", getActivity());
                    }


                } else {
                    Constants.showMessage("Appointments are not available", getActivity());
                }

                adapter = new Swipe_menu_Adapter();
                listView.setAdapter(adapter);
            }
            else
            {
                if (response.getString("success").equals("true"))
                {
                    Constants.showMessage("Cancelled Successfully",getActivity());
                  callWebService();
                }
                else
                {
                    Constants.showMessage("Something went wrong", getActivity());
                }
            }
        }
        catch (Exception e)
        {
System.out.println("eee  "+e.toString());
        }
    }

    @Override
    public void onDateSelected(Calendar date1) {
        try {
            selecteddate=CalendarView.selected_date_time.getText().toString();
            System.out.println("date "+selecteddate.toString());
            date = formatter.parse(selecteddate);
            System.out.println("date1 "+date.toString());
            selecteddate=output.format(date);
            System.out.println("date2 " + selecteddate);
            callWebService();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }


    class Swipe_menu_Adapter extends BaseSwipListAdapter {




        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public Object getItem(int i) {
            return mAppList.get(i);

        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            // menu type count
            return 3;
        }


        @Override
        public boolean getSwipEnableByPosition(int position) {
            if(mAppList.get(position).getIs_visited().equals("cancelled")){
                return false;
            }
            else {

                return false;
            }
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(),
                        R.layout.swipe_list, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            AppointmentBean item = (AppointmentBean)getItem(position);
           holder.bussiness_name_txt.setText(item.getBusiness_name());
            holder.doctor_name_txt.setText(item.getDoctor_name());
            holder.time_txt.setText(item.getAppointment_time());

            holder.status_txt.setText(item.getIs_visited().substring(0, 1).toUpperCase() + item.getIs_visited().substring(1).toLowerCase());
            return convertView;
        }

        class ViewHolder {
            TextView bussiness_name_txt= null,doctor_name_txt= null,time_txt= null,status_txt = null;

            public ViewHolder(View view) {
                bussiness_name_txt = (TextView) view.findViewById(R.id.bussiness_name_txt);
                doctor_name_txt = (TextView) view.findViewById(R.id.doctor_name);
                time_txt =(TextView)view.findViewById(R.id.time);
                status_txt =(TextView)view.findViewById(R.id.status_txt);
                bussiness_name_txt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_5.OTF"));
                doctor_name_txt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
                time_txt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_5.OTF"));
                status_txt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_5.OTF"));
                view.setTag(this);
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((resultCode==1234)&&(requestCode==1234))
        {
            System.out.println("ssss");
            Intent intent = new Intent(getActivity(), Landing_Screen.class);
            intent.putExtra("position", 1);
            startActivity(intent);
            getActivity().finish();
       //   callWebService();
        }
        else
        {

        }
    }
}














