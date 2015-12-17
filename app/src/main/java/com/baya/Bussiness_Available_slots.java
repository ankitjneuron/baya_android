package com.baya;
/*
* created by Neeraj
* this class is used for the showing the slots of the business
* */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.baya.ApiRequest.RequestCompleteListener;
import com.baya.ApiRequest.VolleyRequest;
import com.baya.BeanClass.Availablities;
import com.baya.BeanClass.Buisnesscategories;
import com.baya.BeanClass.Categories;
import com.baya.BeanClass.Doctor;
import com.baya.BeanClass.Getbuisness;
import com.baya.BeanClass.Slots;
import com.baya.BeanClass.Speciality;
import com.baya.BeanClass.state;
import com.baya.Helper.Constants;
import com.baya.Helper.HorizontalListView;
import com.baya.WebServices.URL;
import com.baya.adapter.MyExpandableAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Bussiness_Available_slots extends Activity implements RequestCompleteListener<JSONObject>{

/*
* declaration of the variable
* */

    boolean select_prefrence = false;
    TextView textview_year_select;
    private ArrayList<Date> arraylist_select = null;
    ArrayList<String> array_status = null;
    ArrayList<String> timelimits = null;
    private String select_monthfromlists, select_yearfromlist;
    private Calendar _calendar;
    String matching_month = null;
    String match_year = null;
    private int list_select = 0;
    private int selectedIndex = -1;
    List_Select_Date_hl_Adapter adapter;
    public HorizontalListView h_list_view = null;
    RelativeLayout relativeLayout = null;
    TextView textView_week, textview_buisnessprofile_book = null, textview_buisness_docnm, textview_buisness_contactn, textview_buisness_specils, texview_buisness_addressn;
    TextView textview_datefromlist;
    Calendar opentime, closetime;
    ArrayList<String> timeslot = null, morning = null, afternoon = null, evening = null, night = null;
    String availability_slot = "";
    private DisplayImageOptions options;
    private ImageLoaderConfiguration config;
    ImageLoader imageLoader = null;
    Integer[] imagesid = {R.drawable.morning, R.drawable.afternoon, R.drawable.evening, R.drawable.night};
    MyExpandableAdapter expandableAdapter;
    ExpandableListView expandableList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ArrayList<Integer> image;
    public List<Integer> header_images;
    Getbuisness bcategories;
    private ImageView imageview_buisnesslistings_images;
    private TextView textview_buisnessp_titles;
    Availablities   availablities=null;
    String str_opentime = "", str_closetime = "";
    SharedPreferences preferences=null;
    private Buisnesscategories bcategorie;
    private String latitude="",longitude="";
ArrayList<Slots> arslots=null;
    ArrayList<Speciality> arrayspecialities = new ArrayList<Speciality>();
    private String basepaths;
    private String category="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_available_slots);
        preferences = getSharedPreferences(Constants.BAYA, 0);

        callWebServices();
    }

// for the setting the slots in the business profile
    public  void setData()
    {
        imageLoader = ImageLoader.getInstance();

        imageLoader.init(ImageLoaderConfiguration.createDefault(Bussiness_Available_slots.this));
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defult_icon).showImageForEmptyUri(R.drawable.defult_icon)
                .showImageOnFail(R.drawable.defult_icon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(900))
                .build();
        setResult(RESULT_OK);
        // callWebServices();

        timelimits = new ArrayList<String>();
        morning = new ArrayList<String>();
        afternoon = new ArrayList<String>();
        evening = new ArrayList<String>();
        night = new ArrayList<String>();
        timelimits.add("12:00 AM");
        timelimits.add("12:00 PM");
        timelimits.add("04:00 PM");
        timelimits.add("08:00 PM");
        timelimits.add("11:59 PM");
        initView();
        textview_year_select = (TextView) findViewById(R.id.book_appointment_month_n_year);
        h_list_view = (HorizontalListView) findViewById(R.id.hlvCustomListWithDividerAndFadingEdge);

        adapter = new List_Select_Date_hl_Adapter(Bussiness_Available_slots.this);

        h_list_view.setAdapter(adapter);

        availability_slot = bcategories.getAvailability_slot();
        System.out.println("slotss  " + availability_slot);
        if (availability_slot.equals("")) {
            availability_slot = "30";
        }
        // click listner of the horizotal listview date click in the horizontal calender
        h_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("values " + arraylist_select.get(position));
                list_select = position;

                adapter.setSelectedIndex(position);
                adapter.notifyDataSetChanged();
                String s = (String) android.text.format.DateFormat.format("EE", arraylist_select.get(position));
                str_opentime = "";
                str_closetime = "";
                if (bcategories.getArray_slots() != null) {
                    for (int k = 0; k < bcategories.getArray_slots().size(); k++) {
                        if (bcategories.getArray_slots().get(k).getAvailability_day().substring(0, 3).equals(s)) {
                            if (!bcategories.getArray_slots().get(k).getAvailability_status().equals("no")) {
                                str_opentime = bcategories.getArray_slots().get(k).getAvailability_from() + " " + bcategories.getArray_slots().get(k).getAvailability_schedule_from();
                                str_closetime = bcategories.getArray_slots().get(k).getAvailability_to() + " " + bcategories.getArray_slots().get(k).getAvailability_schedule_to();
                            }
                        }
                    }

                    morning = new ArrayList<String>();
                    afternoon = new ArrayList<String>();
                    evening = new ArrayList<String>();
                    night = new ArrayList<String>();
                    new GetSlots().execute("");
                }


            }
        });
        arraylist_select = new ArrayList<Date>();
        array_status = new ArrayList<String>();
        _calendar = Calendar.getInstance();

        // showing the horizontal list of the 2 year
        for (int i = 0; i < 720; i++) {
            arraylist_select.add(_calendar.getTime());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String date2 = formatter.format(_calendar.getTime());
            String date1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            if (date1.compareTo(date2) == 0) {
                selectedIndex = i;
                list_select = i;
                String s = (String) android.text.format.DateFormat.format("EE", arraylist_select.get(0));
                if (bcategories.getArray_slots() != null) {
                    for (int k = 0; k < bcategories.getArray_slots().size(); k++) {
                        System.out.println("sssssss  " + s + " " + bcategories.getArray_slots().get(k).getAvailability_day().substring(0, 3));
                        if (bcategories.getArray_slots().get(k).getAvailability_day().substring(0, 3).equals(s)) {
                            if (!bcategories.getArray_slots().get(k).getAvailability_status().equals("no")) {
                                str_opentime = bcategories.getArray_slots().get(k).getAvailability_from() + " " + bcategories.getArray_slots().get(k).getAvailability_schedule_from();
                                str_closetime = bcategories.getArray_slots().get(k).getAvailability_to() + " " + bcategories.getArray_slots().get(k).getAvailability_schedule_to();
                            }
                        }
                    }
                }
            }
            _calendar.add(Calendar.DATE, 1);
            array_status.add("false");
            match_year = (String) android.text.format.DateFormat.format("yyyy", arraylist_select.get(0));
            matching_month = (String) android.text.format.DateFormat.format("MMMM", arraylist_select.get(0));
            textview_year_select.setText(matching_month + " " + match_year);
            android.text.format.DateFormat.format("yyyy", arraylist_select.get(0));
            h_list_view.setSelection(list_select);
            expandableList = (ExpandableListView) findViewById(R.id.lvExp);
            expandableList.setGroupIndicator(null);


            expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    // Toast.makeText(getApplicationContext(),
                    // "Group Clicked " + listDataHeader.get(groupPosition),
                    // Toast.LENGTH_SHORT).show();
                    return false;
                }
            });


            // Listview Group expanded listener
            expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {


                }
            });

            // Listview Group collasped listener
            expandableList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {


                }
            });


        }
        h_list_view.setSelection(list_select);
        if (bcategories.getArray_slots() != null) {
            new GetSlots().execute("");
        }
    }


    // calling the deatail of the business profile with its slots
    private void callWebServices()
    {


        try {
            http://baya.whatall.com/api/users/get-business-detail-by-id/
            System.out.println("data" );
            if (Constants.isInternetOn(Bussiness_Available_slots.this)) {
                VolleyRequest volley = new VolleyRequest(Bussiness_Available_slots.this,this);
                System.out.println("url  "+URL.BUSINESSDETAIL.getUrl()+getIntent().getExtras().getString("business_id")+"?access_token=" + preferences.getString(Constants.ACCESS_TOKEN,""));
                volley.makeRequest(Request.Method.GET, URL.BUSINESSDETAIL.getUrl()+getIntent().getExtras().getString("business_id")+"?access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""), null, null, "", "", true);
            } else {
                Constants.showMessage("Please Connect to Internet", Bussiness_Available_slots.this);
            }

        }
        catch(Exception e)
        {
        }


    }
// initalization of the view
    private void initView() {
        textview_buisnessp_titles = (TextView) findViewById(R.id.textview_buisnessp_titles);

        textview_buisness_docnm = (TextView) findViewById(R.id.textview_buisness_doctornam);
        textview_buisness_contactn = (TextView) findViewById(R.id.textview_bussiness_phonen);
        textview_buisness_specils = (TextView) findViewById(R.id.textview_buisness_specials);
        texview_buisness_addressn = (TextView) findViewById(R.id.textview_buisness_addresstext);
        imageview_buisnesslistings_images = (ImageView) findViewById(R.id.imageview_buisnesslistings_images);
        TextView textview_month = (TextView) findViewById(R.id.book_appointment_month_n_year);
        textview_month.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_buisnessp_titles.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_buisness_contactn.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-SEMIBOLD.OTF"));
        textview_buisness_specils.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-SEMIBOLD.OTF"));
        texview_buisness_addressn.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-SEMIBOLD.OTF"));
        textview_buisness_docnm.setTypeface(Typeface.createFromAsset(getAssets(), "museo/JUSTOLDFASHION_5.TTF"));
        ImageButton bac = (ImageButton) findViewById(R.id.name);
        bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                ;
            }
        });


        if(bcategories.getBuisnesscategories().size()==0)
        {
            textview_buisness_specils.setText("No Categories are available");
        }
        else {
            String category = "";
            for (int i = 0; i < bcategories.getBuisnesscategories().size(); i++) {
                category = category + "," + bcategories.getBuisnesscategories().get(i).get_id();
            }

            category = category.substring(1, category.length());
            textview_buisness_specils.setText(category);


        }
        textview_buisness_docnm.setText(bcategories.getBusiness_name());
        textview_buisness_contactn.setText(bcategories.getPhone_number());
//        textview_buisness_specils.setText(bcategories.describeContents());
//        textview_buisnessprofile_book.setText("Book Now");
        texview_buisness_addressn.setText(bcategories.getAddress() + " " + bcategories.getCity() + " " + bcategories.getCountry() + " " + bcategories.getZipcode());
        imageLoader.displayImage(bcategories.getBusiness_logo(), imageview_buisnesslistings_images, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("stateeee  "+requestCode+"  "+resultCode );
        if((resultCode==1234)&&(requestCode==1234))
        {
            Intent i=new Intent();
            i.putExtra("business_name",data.getStringExtra("business_name"));
            i.putExtra("time", data.getStringExtra("time"));
            i.putExtra("date", data.getStringExtra("date"));
            setResult(1234,i);
            this.finish();
        }
    }

    // for dividing the slots in morning, noon, evening and night
    // and setting the data in the expandable listview
    protected void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        header_images = new ArrayList<Integer>();
        header_images.add(R.drawable.morning);
        header_images.add(R.drawable.afternoon);
        header_images.add(R.drawable.evening);
        header_images.add(R.drawable.night);



        if (str_closetime.equals("12:00 PM")) {
            str_closetime = "11:59 PM";
        }
        opentime = Calendar.getInstance();
        closetime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        try {
            opentime.setTime(sdf.parse(str_opentime));
            System.out.println("times " + str_opentime + "  " + str_closetime);
            closetime.setTime(sdf.parse(str_closetime));
            System.out.println("timy  " + sdf.format(closetime.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("time open  " + str_opentime.length() + " " + str_closetime.length());
if(!str_opentime.equals("")) {
    if (opentime != null && closetime != null) {
        System.out.println("time 12 ");
        Calendar mTime = (Calendar) opentime.clone();


        timeslot = new ArrayList<String>();
        while (mTime.before(closetime)) {

            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, mTime.get(Calendar.HOUR_OF_DAY));
            startTime.set(Calendar.MINUTE, mTime.get(Calendar.MINUTE));
            mTime.add(Calendar.MINUTE, Integer.parseInt(availability_slot));

            timeslot.add(sdf.format(startTime.getTime()));
            System.out.println("time  " + sdf.format(startTime.getTime()));
        }
        if (str_closetime.equals("11:59 PM")) {
            timeslot.add("12:00 AM");
        }

        Calendar time1 = Calendar.getInstance();
        Calendar time2 = Calendar.getInstance();

        try {
            time1.setTime(sdf.parse(timelimits.get(0)));
            time2.setTime(sdf.parse(timelimits.get(1)));
            for (int j = 0; j < timeslot.size(); j++) {
                Calendar measure = Calendar.getInstance();
                measure.setTime(sdf.parse(timeslot.get(j)));
                if (measure.after(time1) && measure.before(time2)) {
                    //checkes whether the current time is between 14:49:00 and 20:11:13.
                    morning.add(timeslot.get(j));
                } else if (measure.equals(time1)) {
                    morning.add(timeslot.get(j));
                }

            }
            try {
                if (morning.size() != 0) {
                    if (morning.get(morning.size() - 1).equals("12:00 AM")) {
                        morning.remove(morning.size() - 1);
                    }
                }
            } catch (Exception e) {

            }
            Calendar time3 = Calendar.getInstance();
            Calendar time4 = Calendar.getInstance();
            time3.setTime(sdf.parse(timelimits.get(1)));
            time4.setTime(sdf.parse(timelimits.get(2)));
            for (int j = 0; j < timeslot.size(); j++) {
                Calendar measure = Calendar.getInstance();
                measure.setTime(sdf.parse(timeslot.get(j)));
                if (measure.after(time3) && measure.before(time4)) {
                    //checkes whether the current time is between 14:49:00 and 20:11:13.
                    afternoon.add(timeslot.get(j));
                } else if (measure.equals(time3)) {
                    afternoon.add(timeslot.get(j));
                }
            }
            Calendar time5 = Calendar.getInstance();
            Calendar time6 = Calendar.getInstance();
            time5.setTime(sdf.parse(timelimits.get(2)));
            time6.setTime(sdf.parse(timelimits.get(3)));
            for (int j = 0; j < timeslot.size(); j++) {
                Calendar measure = Calendar.getInstance();
                measure.setTime(sdf.parse(timeslot.get(j)));
                if (measure.after(time5) && measure.before(time6)) {
                    //checkes whether the current time is between 14:49:00 and 20:11:13.
                    evening.add(timeslot.get(j));
                } else if (measure.equals(time5)) {
                    if (!availability_slot.equals("45")) {
                        evening.add(timeslot.get(j));
                    }
                }
            }
            Calendar time7 = Calendar.getInstance();
            Calendar time8 = Calendar.getInstance();
            time7.setTime(sdf.parse(timelimits.get(3)));
            time8.setTime(sdf.parse(timelimits.get(4)));
            for (int j = 0; j < timeslot.size(); j++) {
                Calendar measure = Calendar.getInstance();
                measure.setTime(sdf.parse(timeslot.get(j)));
                if (measure.after(time7) && measure.before(time8)) {
                    //checkes whether the current time is between 14:49:00 and 20:11:13.

                    night.add(timeslot.get(j));

                } else if (measure.equals(time7)) {
                    if (!availability_slot.equals("45")) {
                        night.add(timeslot.get(j));
                    }
                }
            }


            System.out.println("sizeeee  " + morning.size() + " " + afternoon.size() + "  " + evening.size() + "  " + night.size());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Adding child data
        String m = "Morning ";
        String b = "before 12 PM";
        String afternon = "Afternoon ";
        String t = "12 - 4PM";
        String ev = "Evening ";
        String e = "4 - 8 PM";
        String ni = "Night ";
        String n = "after 8 PM";

        SpannableString mb = new SpannableString(m);
        mb.setSpan(new TypefaceSpan("museo/MUSEO_SLAB_5.OTF"), 0, mb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString bef = new SpannableString(b);
        bef.setSpan(new TypefaceSpan("proximanova/PROXIMANOVACOND-SEMIBOLD.OTF"), 0, bef.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString an = new SpannableString(afternon);
        an.setSpan(new TypefaceSpan("museo/MUSEO_SLAB_5.OTF"), 0, an.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString te = new SpannableString(ev);
        te.setSpan(new TypefaceSpan("museo/MUSEO_SLAB_5.OTF"), 0, te.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString be = new SpannableString(e);
        be.setSpan(new TypefaceSpan("proximanova/PROXIMANOVACOND-SEMIBOLD.OTF"), 0, be.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString na = new SpannableString(ni);
        na.setSpan(new TypefaceSpan("museo/MUSEO_SLAB_5.OTF"), 0, na.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString sp = new SpannableString(n);
        sp.setSpan(new TypefaceSpan("proximanova/PROXIMANOVACOND-SEMIBOLD.OTF"), 0, sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString et = new SpannableString(t);
        et.setSpan(new TypefaceSpan("museo/MUSEO_SLAB_5.OTF"), 0, et.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        listDataHeader.add("" + mb + bef);
        listDataHeader.add("" + an + et);
        listDataHeader.add("" + te + be);
        listDataHeader.add("" + na + sp);

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add(" ");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");

        List<String> night = new ArrayList<String>();
        night.add("4 gun");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
        listDataChild.put(listDataHeader.get(3), night);
        System.out.println("list header " + listDataHeader.size());

    }
}
        else
{

}

    }


    // api responce of the request
    @Override
    public void onTaskComplete(String tag, JSONObject response) {
        try {
            if (response.getString("success").equals("true")) {
                JSONObject data = response.getJSONObject("data");
                JSONObject js = data.getJSONObject("listing_detail");
                basepaths = response.getString("basePath");
              //  basepaths="http://baya.whatall.com/uploads/profile/";
                JSONArray buisnesscat = js.getJSONArray("business_category");
                ArrayList<Buisnesscategories> arraybuisness = new ArrayList<Buisnesscategories>();

                for (int y = 0; y < buisnesscat.length(); y++) {
                    JSONObject buisnesscategories = buisnesscat.getJSONObject(y);
                    JSONObject idcategories = buisnesscategories.getJSONObject("cat_id");
                    Categories cat_id = new Categories(idcategories.getString("category_name"), idcategories.getString("category_icon"), idcategories.getString("_id"));

                    bcategorie = new Buisnesscategories(idcategories.getString("category_name"), cat_id);
                    arraybuisness.add(bcategorie);
                }

                JSONObject state = js.getJSONObject("state");
                com.baya.BeanClass.state st = new state(state.getString("_id"), state.getString("state_name"));
                if (js.has("owner_id")) {
                    String ownerids = js.getString("owner_id");
                }
                JSONArray business_claim = js.getJSONArray("business_claim");
                ArrayList<String> arrayclaim = new ArrayList<String>();
                for (int y = 0; y < business_claim.length(); y++) {

                }
                if (js.has("loc")) {
                    JSONArray loc = js.getJSONArray("loc");
                    for (int m = 0; m < loc.length(); m++) {
                        latitude = loc.getString(0);
                        longitude = loc.getString(1);
                    }
                } else {
                    latitude = "0.0";
                    longitude = "0.0";
                }
                String slot_status = "";
                String slot_id = "";
                String availability_slot = "";
                arslots = new ArrayList<Slots>();
                if (js.has("availability")) {
                    JSONObject availability = js.getJSONObject("availability");
                    arslots = new ArrayList<Slots>();

                    JSONArray slotsjson = availability.getJSONArray("availability");


                    for (int m = 0; m < slotsjson.length(); m++) {

                        JSONObject slotsjsonobjects = slotsjson.getJSONObject(m);
                        Slots slots = null;
                        if (slotsjsonobjects.has("_id")) {
                            slots = new Slots(slotsjsonobjects.getString("availability_day"), slotsjsonobjects.getString("availability_status"), slotsjsonobjects.getString("availability_to"), slotsjsonobjects.getString("availability_from"), slotsjsonobjects.getString("availability_schedule_from"), slotsjsonobjects.getString("availability_schedule_to"), slotsjsonobjects.getString("_id"));

                        } else {
                            slots = new Slots(slotsjsonobjects.getString("availability_day"), slotsjsonobjects.getString("availability_status"), slotsjsonobjects.getString("availability_to"), slotsjsonobjects.getString("availability_from"), slotsjsonobjects.getString("availability_schedule_from"), slotsjsonobjects.getString("availability_schedule_to"), "");
                        }
                        arslots.add(slots);
                        System.out.println("slots  " + slots.getAvailability_schedule_to());
                    }

                    slot_status = availability.getString("status");
                    slot_id = availability.getString("_id");
                    availability_slot = availability.getString("availability_slot");
                    System.out.println("availibity  " + availability_slot);

                } else {
                    availablities = new Availablities("", "", null);
                }
                Doctor doctorob;
                ArrayList<Doctor> arraydoctorr = new ArrayList<Doctor>();

                if (js.has("doctors")) {
                    JSONArray doctors = js.getJSONArray("doctors");

                    for (int y = 0; y < doctors.length(); y++) {
                        JSONObject jsonob = doctors.getJSONObject(y);
                        JSONArray speciality = jsonob.getJSONArray("speciality");
                        category="";
                        for (int m = 0; m < speciality.length(); m++) {

                            JSONObject specalities = speciality.getJSONObject(m);
                            JSONObject c = specalities.getJSONObject("cat_id");
                            String category_icon="";
                            if (c.has("category_icon")) {
                                category_icon = js.getString("category_icon");
                            } else {
                                category_icon = "";
                            }
                            Categories ca = new Categories(c.getString("category_name"), c.getString("_id"), category_icon);
                            Speciality s = new Speciality(specalities.getString("_id"), ca);
                            arrayspecialities.add(s);
                        }
                        if(speciality.length()==0)
                        {
                            category="" ;
                        }
                        else {

                            for (int l = 0; l < speciality.length(); l++) {
                                category = category + "," + speciality.getJSONObject(l).getJSONObject("cat_id").getString("category_name");
                            }

                            category = category.substring(1, category.length());



                        }
                        doctorob = new Doctor(basepaths + "profile/" + jsonob.getString("image"), jsonob.getString("_id"), arrayspecialities, jsonob.getString("description"), jsonob.getString("name"), category);
                        arraydoctorr.add(doctorob);
                    }
                } else {
                    doctorob = new Doctor("", "", arrayspecialities, "", "", category);
                    arraydoctorr.add(doctorob);
                }
                String about_us;
                if (js.has("about_us")) {
                    about_us = js.getString("about_us");
                } else {
                    about_us = "";
                }
                String buisness_image = "";
                if (js.has("business_logo")) {
                    buisness_image = basepaths + "listing/" + js.getString("business_logo");
                } else {
                    buisness_image = basepaths+"default_logo.jpg";
                }
                String isClaimed="";
                if (js.has("isClaimed")) {
                    isClaimed = js.getString("isClaimed");
                } else {
                    isClaimed = "";
                }
                bcategories = new Getbuisness(buisness_image, js.getString("who_added"), js.getString("phone_number"), js.getString("is_verified"), js.getString("status"), arraybuisness, js.getString("zipcode"), js.getString("__v"), st, js.getString("country"), js.getString("city"), js.getString("business_name"),isClaimed, js.getString("updated_at"), js.getString("_id"), js.getString("address"), js.getString("who_added_role"), longitude, arrayclaim, latitude, js.getString("created_at"), arraydoctorr, js.getString("is_approved"), js.getString("neighbour"), about_us, slot_status, slot_id, arslots, availability_slot);
                setData();
            }
            else
            {
                Constants.showMessage("Something went wrong",Bussiness_Available_slots.this);
            }
        }catch (JSONException e)
        {
            System.out.println("exception  "+e.toString());
        }
    }

// horizontal calender adapter
    class List_Select_Date_hl_Adapter extends BaseAdapter {
        Context mcontext;
        String string_week = "";
        String dd = "";
        List<Date> arraylist_selected = null;
        private RelativeLayout rlayout;


        public List_Select_Date_hl_Adapter(Context rcontext) {
            mcontext = rcontext;
        }

        @Override
        public int getCount() {
            return arraylist_select.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = null;
            if (convertView == null) {
                System.out.println("arraylist " + arraylist_select.get(position));
                LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.book_app_date_scroll, null);

                rlayout = (RelativeLayout) convertView.findViewById(R.id.layout);

                textView_week = (TextView) convertView.findViewById(R.id.week);

                textview_datefromlist = (TextView) convertView.findViewById(R.id.date);
                textView_week.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
                textview_datefromlist.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));


            } else {

            }
            String week = (String) android.text.format.DateFormat.format("dd", arraylist_select.get(position));

            String selectm = (String) android.text.format.DateFormat.format("dd", arraylist_select.get(position));


            select_yearfromlist = (String) android.text.format.DateFormat.format("yyyy", arraylist_select.get(position));
            relativeLayout = (RelativeLayout) convertView.findViewById(R.id.l);
            string_week = (String) android.text.format.DateFormat.format("EE", arraylist_select.get(position));
            select_monthfromlists = (String) android.text.format.DateFormat.format("MMMM", arraylist_select.get(position));

            dd = (String) android.text.format.DateFormat.format("dd", arraylist_select.get(position));
            textView_week.setText("" + string_week);

            textview_datefromlist.setText("" + selectm);
            if (!dd.equals("01")) {
                textview_year_select.setText(select_monthfromlists + " " + select_yearfromlist);
            }

            if (selectedIndex == position) {

                convertView.setBackgroundResource(R.drawable.roundedtextview);
                textView_week.setTextColor(Color.parseColor("#159AB9"));
                textview_datefromlist.setTextColor(Color.parseColor("#199AB9"));
            } else {

            }

            return convertView;
        }

        public void setSelectedIndex(int index) {
            selectedIndex = index;
        }
    }

    class GetSlots extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            prepareListData();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            expandableAdapter = new MyExpandableAdapter(Bussiness_Available_slots.this, listDataHeader, listDataChild, header_images, timeslot, morning, afternoon, evening, night, bcategories, arraylist_select.get(selectedIndex),getIntent().getExtras().getString("reschedule"));
            expandableList.setAdapter(expandableAdapter);
            if(expandableAdapter.getGroupCount()==0)
            {
                Constants.showMessage("No slots Available",Bussiness_Available_slots.this);
            }
        }

        @Override
        protected void onPreExecute() {


        }

    }


}



/*

    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }*/
