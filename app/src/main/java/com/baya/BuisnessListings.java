package com.baya;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.baya.WebServices.URL;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by prachi on 02-11-2015.
 * this call is used for the showing all the list of the business which are
 * come in the search criteria of the find business screen
 *
 */
public class BuisnessListings extends Fragment implements RequestCompleteListener<JSONObject> {

    // Declaretion of the variable
    ListView lidstview;
    BuisnessAdapter buisness;
    TextView noitem=null,add_listing=null;
    @Nullable
    Getbuisness getbuisness;
    ArrayList<Getbuisness> arrayget = null;
    Buisnesscategories bcategories;
    String placelatitude = null, categorids = "";
    String placelongitude = null, name = "";

    String basepaths = "";
    ImageLoader imageloader;
    DisplayImageOptions doptions;
    private DisplayImageOptions options;
    ArrayList<Speciality> arrayspecialities = new ArrayList<Speciality>();
    ArrayList<Slots> arslots;
    Availablities availablities;
    String latitude, longitude;

    // variable used for th pagination
    private int pagenumber = 1, totalpage = 0;
    private boolean state = false;
    private RelativeLayout progress_more;
    private int scorllstartposition = 0;boolean loadbool=false;
    String category = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pagenumber = 1;
        totalpage = 0;

        View v = inflater.inflate(R.layout.buisness_listings, null);
        noitem=(TextView)v.findViewById(R.id.noitem);
        add_listing=(TextView)v.findViewById(R.id.add_listing);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        System.out.println("version "+currentapiVersion);
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
        RelativeLayout.LayoutParams p=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mainheight = metrics.heightPixels;
        DisplayMetrics metrics1 = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics1);
        int usableHeight = metrics1.heightPixels;
        System.out.println("hegit  "+mainheight+"  "+usableHeight);
        p.setMargins(0, 0, 0,usableHeight-mainheight);
        p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        add_listing.setLayoutParams(p);
        add_listing.setPadding(10, 30, 10, 30);
    } else{
        // do something for phones running an SDK before lollipop
            add_listing.setPadding(10, 20, 10, 20);
    }
        lidstview = (ListView) v.findViewById(R.id.listview);
        pagenumber = 1; totalpage = 1;
        placelatitude = getArguments().getString("latitude");
        placelongitude = getArguments().getString("longitudes");
        System.out.println("lati  " + placelatitude);
        if (placelatitude.equals("0.0"))
        {
//            placelatitude = "" + Constants.latitude;
//          placelongitude = "" + Constants.logitude;
        }
        arrayget = new ArrayList<Getbuisness>();
        categorids = getArguments().getString("categorids");
        name = getArguments().getString("name");

        imageloader = ImageLoader.getInstance();
        imageloader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defult_icon).showImageForEmptyUri(R.drawable.defult_icon)
                .showImageOnFail(R.drawable.defult_icon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(900))
                .build();
        System.out.println("types" + placelatitude + placelongitude + categorids + name);
        Landing_Screen.content.setText("Business Listings");
        Landing_Screen.image.setVisibility(View.VISIBLE);
//        arrayget = new ArrayList<Getbuisness>();
        setlisteners();
        buisness = new BuisnessAdapter(getActivity());
        lidstview.setAdapter(buisness);
        getbuisness();

        // click listner through which we will redirect to the
        // add listng screen
        add_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.isInternetOn(getActivity())) {
                    Intent i = new Intent(getActivity(), AddListing.class);
                    startActivity(i);
                }
                    else {
                    Constants.showMessage("Please Connect to Internet", getActivity());
                }
            }
        });

        // set image of filer in header
        Landing_Screen.image.setImageResource(R.drawable.filter);

        /*
        * Scroll listner on which the load more api will call
        * */
        lidstview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                int threshold = 1;
                int count = lidstview.getCount();
                System.out.println("threshold" + count);

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (lidstview.getLastVisiblePosition() >= count - threshold) {
                        // Execute LoadMoreDataTask AsyncTask
                        System.out.println("on scrollll" + state);
                        if (state) {

                            if (Constants.isInternetOn(getActivity()) == true) {
                                state = false;
                                loadmoreData();
                            } else {

                                Constants.dialogs("", "No Internet", getActivity(), "true");
                            }
                        }
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }
        });
        progress_more=(RelativeLayout)v.findViewById(R.id.relativeprogress);
        return v;
    }

    // on scroll this method will call

    private void loadmoreData() {
        int listposition = arrayget.size();

        pagenumber = pagenumber + 1;

        if (pagenumber > totalpage) {

            Constants.showMessage("no more data", getActivity());
        } else {

            progress_more.setVisibility(View.VISIBLE);
            try {

                getbuisness();
            } catch (Exception e) {

            }
        }

    }

    // businss listing adapter
    class BuisnessAdapter extends BaseAdapter {
        Context mcontext;
        LayoutInflater inflater = null;

        public BuisnessAdapter(Context mcontext) {
            this.mcontext = mcontext;
            inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return arrayget.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listings, null);
            }
            TextView textview_buisnesslistings_bookings=(TextView)convertView.findViewById(R.id.textview_buisnesslistings_bookings);
            TextView textviewname = (TextView) convertView.findViewById(R.id.textview_buisnesslistings_nam);
            TextView textviewsp = (TextView) convertView.findViewById(R.id.textview_buisnesslistings_appointment);
            TextView textviewad = (TextView) convertView.findViewById(R.id.textview_buisnesslistings_address);

            textviewname.setText(arrayget.get(position).getBusiness_name());
            if(arrayget.get(position).getBuisnesscategories().size()==0)
            {
                textviewsp.setText("No Categories are available");
            }
            else {
                String category = "";
                for (int i = 0; i < arrayget.get(position).getBuisnesscategories().size(); i++) {
                    category = category + "," + arrayget.get(position).getBuisnesscategories().get(i).get_id();
                }

                category = category.substring(1, category.length());
                textviewsp.setText(category);


            }
            textview_buisnesslistings_bookings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(getActivity(), Bussiness_Available_slots.class);
                    in.putExtra("business_id",arrayget.get(position).get_id());
                    in.putExtra("reschedule","");
                    getActivity().startActivityForResult(in, 1234);
                }
            });
            textviewad.setText(arrayget.get(position).getAddress() + ", " + arrayget.get(position).getCity() + " " + arrayget.get(position).getCountry() + "," + arrayget.get(position).getZipcode());
            ImageView imagelogos = (ImageView) convertView.findViewById(R.id.imageview_buisnesslistings_images);
            imageloader.displayImage(arrayget.get(position).getBusiness_logo(), imagelogos, options);

            textviewname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/JUSTOLDFASHION_5.TTF"));
            textviewsp.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
            textviewad.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));

            TextView booknow = (TextView) convertView.findViewById(R.id.textview_buisnesslistings_bookings);
            booknow.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_0.OTF"));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BusinessProfile.class);
                    intent.putExtra("buisnesslist", arrayget.get(position));
                    getActivity().startActivityForResult(intent,1234);
                }
            });
            return convertView;
        }
    }

    void setlisteners() {
        Landing_Screen.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FindaBuisness findsbuisness = new FindaBuisness();
//                ((BaseContainers) getParentFragment()).replaceFragment(findsbuisness, true);
                Intent intent = new Intent(getActivity(), Landing_Screen.class);
                intent.putExtra("position", 0);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

// api request for the get business listing
    void getbuisness() {

        JSONObject js = new JSONObject();
        try {
            js.put("business_name", name);
            JSONArray ja = new JSONArray();
            if (!categorids.equals(""))
                ja.put(categorids);

            js.put("cat_id", ja);
            if (!placelatitude.equals("0.0")) {
            js.put("latitude", placelatitude);
           js.put("longitude", placelongitude);
            }
            System.out.println("catt  " + js.toString());
        } catch (Exception e) {

        }
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.BAYA, 0);
        if (Constants.isInternetOn(getActivity())) {
            if(pagenumber==1) {
                VolleyRequest volley = new VolleyRequest(getActivity(), BuisnessListings.this);
                volley.makeRequest(Request.Method.POST, URL.GET_BUISNESS.getUrl() + "page=" + pagenumber + "?access_token=" + preferences.getString(Constants.ACCESS_TOKEN, "") + "&is_verified=verified", js, null, "", "getbuisness", true);
                System.out.println(js.toString() );
            }
            else
            {
                VolleyRequest volley = new VolleyRequest(getActivity(), BuisnessListings.this);
                volley.makeRequest(Request.Method.POST, URL.GET_BUISNESS.getUrl() + "page=" + pagenumber  + "?access_token=" + preferences.getString(Constants.ACCESS_TOKEN, "") + "&is_verified=verified", js, null, "", "getbuisness", false);
                System.out.println(js.toString());
            }

        } else {
            Constants.showMessage("Please Connect to Internet", getActivity());
        }
        System.out.println("assssssssss"+pagenumber);

    }

    // api responce

    @Override
    public void onTaskComplete(String tag, JSONObject response) {
        String sucess;
        try {
            if (response.getString("success").equals("true")) {
                JSONObject data = response.getJSONObject("data");
                basepaths = response.getString("basePath");
                JSONArray items = data.getJSONArray("items");

                if (items.length() == 0) {
                    Constants.showMessage("No Business", getActivity());
                    progress_more.setVisibility(View.GONE);
                    noitem.setVisibility(View.VISIBLE);
                }
//                   else
//                    {
//

                else{
//                        JSONObject meta = data.getJSONObject("_meta");

                    if (loadbool == false) {
                        loadbool = true;
                        totalpage = Integer.parseInt(data.getString("totalItems"));
                        totalpage = Integer.parseInt(data.getString("page"));
                    }
                    noitem.setVisibility(View.GONE);
                    System.out.println("asssssssssssssss"+data.getString("page"));
                    state = true;
                    for (int u = 0; u < items.length(); u++) {
                        JSONObject js = items.getJSONObject(u);
                        System.out.println("iiiiii  1" + u);
                        JSONArray buisnesscat = js.getJSONArray("business_category");
                        ArrayList<Buisnesscategories> arraybuisness = new ArrayList<Buisnesscategories>();
                        System.out.println("iiiiii  2" + u);
                        for (int y = 0; y < buisnesscat.length(); y++) {
                            JSONObject buisnesscategories = buisnesscat.getJSONObject(y);
                            JSONObject idcategories = buisnesscategories.getJSONObject("cat_id");
                            Categories cat_id = new Categories(idcategories.getString("category_name"), idcategories.getString("category_icon"), idcategories.getString("_id"));

                            bcategories = new Buisnesscategories(idcategories.getString("category_name"), cat_id);
                            arraybuisness.add(bcategories);
                        }
                        System.out.println("iiiiii  3" + u);
                        JSONObject state = js.getJSONObject("state");
                        state st = new state(state.getString("_id"), state.getString("state_name"));
                        if (js.has("owner_id")) {
                            JSONObject ownerids = js.getJSONObject("owner_id");
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

                            System.out.println("iiiiii  4" + u);
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
                            System.out.println("iiiiii  5" + u);
                            slot_status = availability.getString("status");
                            slot_id = availability.getString("_id");
                            availability_slot = availability.getString("availability_slot");
                            System.out.println("availibity  " + availability_slot);

                        } else {
                            availablities = new Availablities("", "", null);
                        }
                        Doctor doctorob;
                        ArrayList<Doctor> arraydoctorr = new ArrayList<Doctor>();
                        System.out.println("iiiiii  6" + u);
                        if (js.has("doctors")) {
                            JSONArray doctors = js.getJSONArray("doctors");

                            for (int y = 0; y < doctors.length(); y++) {
                                JSONObject jsonob = doctors.getJSONObject(y);
                                JSONArray speciality = jsonob.getJSONArray("speciality");
                                arrayspecialities=new ArrayList<Speciality>();
                                category="";
                                for (int m = 0; m < speciality.length(); m++) {
                                    JSONObject specalities = speciality.getJSONObject(m);
                                    JSONObject c = specalities.getJSONObject("cat_id");
                                    Categories ca = new Categories(c.getString("category_name"), c.getString("_id"), c.getString("category_icon"));
                                    System.out.println("speciality  "+ca.getCategoryname());
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
                                doctorob = new Doctor(basepaths + "profile/" + jsonob.getString("image"), jsonob.getString("_id"), arrayspecialities, jsonob.getString("description"), jsonob.getString("name"),category);
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
                        String buisness_image = "",neibhour="";
                        if (js.has("business_logo")) {
                            buisness_image = basepaths + "listing/" + js.getString("business_logo");
                        } else {
                            buisness_image = basepaths+"default_logo.jpg";
                        }
if(js.has("neibhour"))

{
    neibhour=js.getString("neighbour");
}

                        else{
    neibhour="";
}
                        getbuisness = new Getbuisness(buisness_image, js.getString("who_added"), js.getString("phone_number"), js.getString("is_verified"), js.getString("status"), arraybuisness, js.getString("zipcode"), js.getString("__v"), st, js.getString("country"), js.getString("city"), js.getString("business_name"), js.getString("isClaimed"), js.getString("updated_at"), js.getString("_id"), js.getString("address"), js.getString("who_added_role"), longitude, arrayclaim, latitude, js.getString("created_at"), arraydoctorr, js.getString("is_approved"), neibhour, about_us, slot_status, slot_id, arslots, availability_slot);
                        arrayget.add(getbuisness);
                    }
                    progress_more.setVisibility(View.GONE);
                    buisness.notifyDataSetChanged();
                }
            }
            else
            {
if(arrayget.size()==0)
{
    Constants.showMessage("No Business", getActivity());
    progress_more.setVisibility(View.GONE);
    noitem.setVisibility(View.VISIBLE);
}
            }

        } catch (Exception e) {

            progress_more.setVisibility(View.GONE);
            System.out.println("aarrrrrrrrrrrrrrrrrrrr" + e.toString());
        }
    }


    // In this method we will show congratulation popup
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("data   " + data.getStringExtra("time"));
        if((resultCode==1234)&&(requestCode==1234))
        {

            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_booking_success);
            TextView business_name=(TextView)dialog.findViewById(R.id.buisness_name);
            TextView date=(TextView)dialog.findViewById(R.id.date);
            TextView time=(TextView)dialog.findViewById(R.id.time);
            Button okbutton=(Button)dialog.findViewById(R.id.okbutton);
            business_name.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/JUSTOLDFASHION_5.TTF"));
            date.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/JUSTOLDFASHION_5.TTF"));
            time.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/JUSTOLDFASHION_5.TTF"));
            okbutton.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/JUSTOLDFASHION_5.TTF"));
            business_name.setText(data.getStringExtra("business_name"));
            date.setText(data.getStringExtra("date"));
            time.setText(data.getStringExtra("time"));
            okbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Landing_Screen.class);
                    intent.putExtra("position", 1);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
            dialog.show();
        }
        else
        {

        }
    }

}
