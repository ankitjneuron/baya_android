package com.baya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.baya.Helper.Constants;
import com.baya.WebServices.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


//import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
//import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by prachi on 23-11-2015.
 */
public class Upcomingappointment extends Activity implements RequestCompleteListener<JSONObject>{
    private String[] countries;
    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
    String date="";
    ArrayList<Long> datearray=null;
    private SharedPreferences preferences;
    private ArrayList<AppointmentBean> mAppList;
    SwipeMenuListView stlist;

    TextView selectIndex;
    private ImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        preferences =getSharedPreferences(Constants.BAYA, 0);
      stlist = (SwipeMenuListView)findViewById(R.id.stickSwipeList);

        back=(ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        callwebService();

        // set creator

        stlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!mAppList.get(position).getAppointment_time().equals("")) {
                    Intent i = new Intent(Upcomingappointment.this, AppointmentDetail.class);
                    i.putExtra("from", "upcoming");
                    i.putExtra("appointment_id", mAppList.get(position).getAppointment_id());
                    startActivityForResult(i, 1234);
                }
            }
        });
        stlist.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                AppointmentBean item = mAppList.get(position);
                switch (index) {
                    case 0:
                        callCancelService(position);
                        break;
                    case 1:

                            Intent i = new Intent(Upcomingappointment.this, Bussiness_Available_slots.class);
                            i.putExtra("reschedule", mAppList.get(position).getAppointment_id());
                            i.putExtra("business_id", mAppList.get(position).getBusiness_id());
                            Upcomingappointment.this.startActivityForResult(i, 1234);

                        break;
                }
                return false;
            }
        });
    }

    private void callwebService() {
        Calendar cal=Calendar.getInstance();
        date=output.format(cal.getTime());
        System.out.println("date  "+date+"T00:00:00.000Z");
        try {

            JSONObject dateobj = new JSONObject();
            dateobj.put("date", date+"T00:00:00.000Z");
            if (Constants.isInternetOn(Upcomingappointment.this)) {
                VolleyRequest volley = new VolleyRequest(Upcomingappointment.this,this);
                volley.makeRequest(Request.Method.POST, URL.ALLUPCOMMINGAPPOINTMENT.getUrl() + "access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""), dateobj, null, "", "ALLAPPOINTMENT", true);
            } else {
                Constants.showMessage("Please Connect to Internet", Upcomingappointment.this);
            }

        }
        catch(JSONException e)
        {
        }

    }

    @Override
    public void onTaskComplete(String tag, JSONObject responses) {
        try {
            if(tag.equals("ALLAPPOINTMENT")) {
                mAppList = new ArrayList<AppointmentBean>();
                if (responses.getString("success").equals("true")) {

                    System.out.println("date3 " + responses.getJSONArray("data").length());

                    if (responses.getJSONArray("data").length() != 0) {
                        datearray = new ArrayList<Long>();
                        for (int j = 0; j < responses.getJSONArray("data").length(); j++) {
                            datearray.add(Long.valueOf(j));

//
                            JSONObject response = responses.getJSONArray("data").getJSONObject(j);
                            SimpleDateFormat formatterparent = new SimpleDateFormat("EEEE MMM dd yyyy");
                            String stringparent = "yyyy-MM-dd";
                            DateFormat formatparent = new SimpleDateFormat(stringparent);
                            Date dateparent = formatparent.parse(response.getString("date").split("T")[0]);
                            String str_selected_parent = formatterparent.format(dateparent);
                            mAppList.add( new AppointmentBean("","", "", "",
                                    "","","", str_selected_parent,"", "","", "","","",
                                   "","", "", "",""));
                            for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                datearray.add(Long.valueOf(j));
                                JSONObject jos = response.getJSONArray("data").getJSONObject(i);
                                JSONObject businessdetail = response.getJSONArray("data").getJSONObject(i).getJSONObject("listing_id");
                                JSONObject patient_info = response.getJSONArray("data").getJSONObject(i).getJSONObject("patient_info");
                                String doctor_name = "", email = "", address = "", patient_address = "";
                                if (jos.getString("doctor_id").equals("")) {
                                    doctor_name = "No Specific Doctor";
                                } else {
                                    JSONArray doctors=businessdetail.getJSONArray("doctors");
                                    for (int k= 0; k < doctors.length();k++) {
                                        JSONObject jobj=doctors.getJSONObject(k);
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
                                SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMM dd yyyy");
                                String string = "yyyy-MM-dd";
                                DateFormat format = new SimpleDateFormat(string);
                                Date date = format.parse(jos.getString("appointment_date").split("T")[0]);
                                String str_selected_date = formatter.format(date);

                                System.out.println("mapplist  " + businessdetail.getString("business_name") + "  " + jos.getString("appointment_date"));
                                AppointmentBean app = new AppointmentBean(jos.getString("_id"), doctor_name, businessdetail.getString("business_name"), jos.getString("appointment_status"),
                                        jos.getString("user_id"), jos.getString("appointment_reason"), jos.getString("is_new_customer"), str_selected_date, jos.getString("appointment_time"), patient_info.getString("first_name"), patient_info.getString("last_name"), email, patient_info.getString("_id"), patient_address,
                                        patient_info.getString("dob"), businessdetail.getString("business_logo"), address + "," + businessdetail.getString("city") + "," + businessdetail.getString("zipcode"), "", businessdetail.getString("_id"));
                                mAppList.add(app);

                            }
                        }

                        Swipe_menu_Adapter adapter = new Swipe_menu_Adapter();
                        stlist.setAdapter(adapter);
                        SwipeMenuCreator creator = new SwipeMenuCreator() {

                            @Override
                            public void create(SwipeMenu menu) {
                                // Create different menus depending on the view type
                                switch (menu.getViewType()) {
                                    case 0:
                                        System.out.println("intttt 2");
                                        createMenu1(menu);

                                        break;
                                    case 1:
                                        System.out.println("intttt 1");
                                        createMenu2(menu);
                                        break;
                                }
                            }

                            private void createMenu1(SwipeMenu menu) {

                                // create "delete" item


                                SwipeMenuItem item1 = new SwipeMenuItem(Upcomingappointment.this);
                                // set item background
                                item1.setBackground(new ColorDrawable(Color.parseColor("#373737")));
                                // set item width
                                item1.setWidth(dp2px(110));
                                item1.setTitle("Cancel");
                                item1.setTitleSize(12);
                                item1.setTitleColor(Color.parseColor("#FFFFFF"));
                                menu.addMenuItem(item1);



                                SwipeMenuItem item2 = new SwipeMenuItem(
                                        Upcomingappointment.this);
                                // set item background
                                item2.setBackground(new ColorDrawable(Color.parseColor("#0EA1D4")));
                                // set item width
                                item2.setWidth(dp2px(110));
                                // set item title
                                item2.setTitle("Reschedule");
                                item2.setTitleSize(12);
                                item2.setTitleColor(Color.parseColor("#FFFFFF"));
                                // set a icon
//                                item2.setIcon(R.drawable.reschedule01);
                                // add to menu
                                menu.addMenuItem(item2);


                            }
                            private void createMenu2(SwipeMenu menu) {

                                // create "delete" item


                                SwipeMenuItem item2 = new SwipeMenuItem(
                                        Upcomingappointment.this);
                                // set item background
                                item2.setBackground(new ColorDrawable(Color.parseColor("#0EA1D4")));
                                // set item width
                                item2.setWidth(dp2px(110));
                                // set item title
                                invalidateOptionsMenu();
                                // set a icon
                                item2.setIcon(null);
                                // add to menu
                                menu.addMenuItem(item2);







                            }
                        };
                        stlist.setMenuCreator(creator);
                        /** indexable listview */


                    } else {
                        Constants.showMessage("Appointments are not available", Upcomingappointment.this);
                    }


                } else {
                    Constants.showMessage("Appointments are not available", Upcomingappointment.this);
                }
            }
            else
            {

                if (responses.getString("success").equals("true"))
                {
                    Constants.showMessage("Cancelled Successfully", Upcomingappointment.this);
                    callwebService();
                }
                else
                {
                    Constants.showMessage("Something went wrong", Upcomingappointment.this);
                }
            }

        }
        catch (Exception e)
        {
            System.out.println("eee  "+e.toString());
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
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            // current menu type
            System.out.println("time  "+mAppList.get(position).getAppointment_time());
            if (mAppList.get(position).getAppointment_time().equals("")) {
                System.out.println("if");
                return 1;
            }
            else
            {
                System.out.println("else");
                return 0;
            }
        }
        @Override
        public boolean getSwipEnableByPosition(int position) {
//            if(mAppList.get(position).getIs_visited().equals("cancelled")){
//                return false;
//            }
//
//            if(mAppList.get(position).getAppointment_time().equals("")) {
//                return false;
//            }
            return false;
        }


        @Override
        public View getView(int position, View view, ViewGroup parent) {


            if (view == null) {


                if (mAppList.get(position).getAppointment_time().equals("")) {
                    // Inflate the layout with image
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = vi.inflate(R.layout.header, parent,false);

                }
                else {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = vi.inflate(R.layout.swipe_list, parent,false);

                }

//                new ViewHolder(convertView);
            }


            if (!mAppList.get(position).getAppointment_time().equals("")) {
                TextView bussiness_name_txt = null, doctor_name_txt = null, time_txt = null, status_txt = null;
                RelativeLayout status_layout;
                LinearLayout name_time_layout;
                bussiness_name_txt = (TextView) view.findViewById(R.id.bussiness_name_txt);
                doctor_name_txt = (TextView) view.findViewById(R.id.doctor_name);
                time_txt = (TextView) view.findViewById(R.id.time);
                status_txt = (TextView) view.findViewById(R.id.status_txt);
                status_layout = (RelativeLayout) view.findViewById(R.id.status_layout);
                name_time_layout = (LinearLayout) view.findViewById(R.id.name_time_layout);
                bussiness_name_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
                doctor_name_txt.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
                time_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
                status_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
                AppointmentBean item = (AppointmentBean) getItem(position);
                bussiness_name_txt.setText(item.getBusiness_name());
                doctor_name_txt.setText(item.getDoctor_name());
                time_txt.setText(item.getAppointment_time());

                status_txt.setText( item.getIs_visited().substring(0, 1).toUpperCase() + item.getIs_visited().substring(1).toLowerCase());
                if (mAppList.get(position).getAppointment_time().equals("")) {
                    bussiness_name_txt.setText(mAppList.get(position).getAppointment_date());
                    bussiness_name_txt.setBackgroundColor(Color.parseColor("#000000"));
                    bussiness_name_txt.setTextColor(Color.parseColor("#FFFFFF"));
                    doctor_name_txt.setVisibility(View.GONE);
                    time_txt.setVisibility(View.GONE);
                    status_txt.setVisibility(View.GONE);
                    status_layout.setVisibility(View.GONE);
                    name_time_layout.setVisibility(View.GONE);
                } else {
                    bussiness_name_txt.setBackgroundColor(Color.parseColor("#00000000"));
                    bussiness_name_txt.setTextColor(Color.parseColor("#0EA1D4"));
                    bussiness_name_txt.setText(item.getBusiness_name());
                    doctor_name_txt.setVisibility(View.VISIBLE);
                    time_txt.setVisibility(View.VISIBLE);
                    status_txt.setVisibility(View.VISIBLE);
                    status_layout.setVisibility(View.VISIBLE);
                    name_time_layout.setVisibility(View.VISIBLE);
                }
            }
            else
            {
               TextView text1 = (TextView) view.findViewById(R.id.text1);
                text1.setText(mAppList.get(position).getAppointment_date());
            }
            return view;
        }

        class ViewHolder {


            public ViewHolder(View view) {

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

           callwebService();
        }
        else
        {

        }
    }


    void callCancelService(int position)
    {
        if (Constants.isInternetOn(Upcomingappointment.this)) {
            VolleyRequest volley = new VolleyRequest(Upcomingappointment.this,this);
            volley.makeRequest(Request.Method.GET, URL.CANCELAPPOINTMENT.getUrl() + mAppList.get(position).getAppointment_id() + "/status/cancelled?" + "access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""), null, null, "", "Cancel", true);
        } else {
            Constants.showMessage("Please Connect to Internet", Upcomingappointment.this);
        }
    }
}
