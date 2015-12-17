package com.baya;
/*
*
* Created by Neeraj Rathore
*
* In this class we will show the the buisness profile of the selected business from the listing
* */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baya.BeanClass.Doctor;
import com.baya.BeanClass.Getbuisness;
import com.baya.BeanClass.Slots;
import com.baya.BeanClass.Speciality;
import com.baya.Helper.Constants;
import com.baya.adapter.Bussiness_profile_adapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;


public class BusinessProfile extends Activity {
    //declaration of the view

    TextView textView = null, textview_buisnessp_titles = null;
    RelativeLayout getdirection;
    private ImageButton business_profile_business_images;
    private DisplayImageOptions options;
    private ImageLoaderConfiguration config;
    ImageLoader imageLoader = null;
    ScrollView scroll1,scroll2;
    private TextView profile_button = null, business_hour_button = null;
    RelativeLayout relativeLayout_business_profile_layout = null;
    RelativeLayout relativeLayout_business_hour_layout = null;
    ArrayList<Slots> aravailaablities = null;
    RelativeLayout relativeLayout_profile_button_layout = null;
    RelativeLayout relativeLayout_hour_button_layout = null;
    ListView business_profile_doctor_list_view, hours_list;
    Bussiness_profile_adapter adapter = null;
    Getbuisness bcategories;
    RelativeLayout business_profile_about_description_layout;
    String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    TextView textview_buisnessprofile_book = null, textview_buisness_docnm, textview_buisness_contactn, textview_buisness_specils, texview_buisness_addressn, textview_noslotlists, textview_about, textview_buisness, textview_nodocotorlist;
    ImageView imageview_buisnesslistings_images;
    ArrayList<Speciality> specialities;
    ArrayList<Doctor> ardoctors = null;
    Adapter arrayadapter=null;

    @Override
    public void onBackPressed() {
        Intent i=new Intent();
        setResult(1235,i);
        BusinessProfile.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);
        aravailaablities=new ArrayList<Slots>();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(BusinessProfile.this));
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defult_icon).showImageForEmptyUri(R.drawable.defult_icon)
                .showImageOnFail(R.drawable.defult_icon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(900))
                .build();

        initView();

        bcategories = getIntent().getParcelableExtra("buisnesslist");
        ardoctors = bcategories.getArrayDoctor();
        adapter = new Bussiness_profile_adapter(BusinessProfile.this, ardoctors);
        business_profile_doctor_list_view.setAdapter(adapter);

        for(int n = 0; n < ardoctors.size(); n++) {
            specialities = ardoctors.get(n).getArrspecialities();
            System.out.println("speciality " + ardoctors.get(n).getArrspecialities().get(0).getCa().getCategoryname());
        }

        if (bcategories.getArray_slots() != null) {
            aravailaablities = bcategories.getArray_slots();

        }



        int totalHeight1 = 0;
        int desiredWidth1 = View.MeasureSpec.makeMeasureSpec(hours_list.getWidth(), View.MeasureSpec.AT_MOST);
        System.out.println("count  "+adapter.getCount());
        for (int i = 0; i < adapter.getCount(); i++) {
            System.out.println("count  "+i);
            View listItem = adapter.getView(i, null, business_profile_doctor_list_view);
            listItem.measure(0, 0);
            totalHeight1 += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params1 = business_profile_doctor_list_view.getLayoutParams();
        params1.height = totalHeight1 + (business_profile_doctor_list_view.getDividerHeight() * (adapter.getCount() - 1));
        business_profile_doctor_list_view.setLayoutParams(params1);
        business_profile_doctor_list_view.requestLayout();
        if (bcategories.getArray_slots() == null) {
            textview_noslotlists.setVisibility(View.VISIBLE);
        } else {
            textview_noslotlists.setVisibility(View.GONE);
        }
        System.out.println("arrrrrrrrrrrrrrrrrrrr" + aravailaablities.size());
        setlisteners();

        settypeface();

    }
// click listner
    void setlisteners() {
        business_profile_business_images.setOnClickListener(onlisteners);
        profile_button.setOnClickListener(onlisteners);
        business_hour_button.setOnClickListener(onlisteners);
        textview_buisnessprofile_book.setOnClickListener(onlisteners);
        business_hour_button.setOnClickListener(onlisteners);
        textview_buisnessprofile_book.setOnClickListener(onlisteners);
        getdirection.setOnClickListener(onlisteners);
    }
// set fonts on the text
    void settypeface() {
        profile_button.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        business_hour_button.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));


        textview_buisnessp_titles.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_buisnessprofile_book.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));

        textview_buisness_contactn.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        textview_buisness_specils.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        texview_buisness_addressn.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        textview_buisness_docnm.setTypeface(Typeface.createFromAsset(getAssets(), "museo/JUSTOLDFASHION_5.TTF"));
        scroll1=(ScrollView)findViewById(R.id.scroll1);
        scroll2=(ScrollView)findViewById(R.id.scroll2);
        scroll1.fullScroll(View.FOCUS_UP);
        scroll2.fullScroll(View.FOCUS_UP);
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
        TextView textviewhourt = (TextView) findViewById(R.id.txt_business_hour_txt);
        TextView textviewmessaget = (TextView) findViewById(R.id.txt_business_hour_txt);
        textviewhourt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textviewmessaget.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));

    }
// click listner
    View.OnClickListener onlisteners = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.business_profile_button:

                    business_profile();
                    scroll1.fullScroll(View.FOCUS_UP);
                    scroll2.fullScroll(View.FOCUS_UP);
                    break;
                case R.id.business_hour_button:
                    business_hours();
                    scroll1.fullScroll(View.FOCUS_UP);
                    scroll2.fullScroll(View.FOCUS_UP);
                    break;
                case R.id.booking_buisnessprofiles_txt_booknow:
                    Intent in = new Intent(BusinessProfile.this, Bussiness_Available_slots.class);
                    in.putExtra("business_id",bcategories.get_id());
                    in.putExtra("reschedule","");
                    startActivityForResult(in, 1234);
                    break;
                case R.id.back_buisnessp:
                    Intent i=new Intent();

                    setResult(1235,i);
                    BusinessProfile.this.finish();
//                     getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    break;
                case R.id.getdirection:
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+ Constants.latitude+","+Constants.logitude+"&daddr="+bcategories.getLattitude()+","+bcategories.getLongtitude()));
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if((resultCode==1234)&&(requestCode==1234))
        {
            Intent i=new Intent();
            i.putExtra("business_name",data.getStringExtra("business_name"));
            i.putExtra("time", data.getStringExtra("time"));
            i.putExtra("date", data.getStringExtra("date"));
            setResult(1234,i);
            BusinessProfile.this.finish();
        }
    }
// initialization of the view
    public void initView() {
        profile_button = (TextView) findViewById(R.id.business_profile_button);
        business_hour_button = (TextView) findViewById(R.id.business_hour_button);
        relativeLayout_profile_button_layout = (RelativeLayout) findViewById(R.id.business_profile_button_layout);
        relativeLayout_hour_button_layout = (RelativeLayout) findViewById(R.id.business_hour_button_layout);
        relativeLayout_business_profile_layout = (RelativeLayout) findViewById(R.id.business_profile_layout);

        business_profile_doctor_list_view = (ListView) findViewById(R.id.business_profile_doctor_list);
      //  setListViewHeightBasedOnChildren(business_profile_doctor_list_view);
//      business_profile_doctor_list.setAdapter(adapter);

        relativeLayout_business_hour_layout = (RelativeLayout) findViewById(R.id.business_profile_doctor_business_hour_layout);
        relativeLayout_business_hour_layout.setVisibility(View.GONE);

        business_profile_business_images = (ImageButton) findViewById(R.id.back_buisnessp);
        textview_buisnessp_titles = (TextView) findViewById(R.id.textview_buisnessp_titles);
        textview_buisnessprofile_book = (TextView) findViewById(R.id.booking_buisnessprofiles_txt_booknow);
        textview_buisness_docnm = (TextView) findViewById(R.id.textview_buisness_doctornam);
        textview_buisness_contactn = (TextView) findViewById(R.id.textview_bussiness_phonen);
        textview_buisness_specils = (TextView) findViewById(R.id.textview_buisness_specials);
        texview_buisness_addressn = (TextView) findViewById(R.id.textview_buisness_addresstext);
        imageview_buisnesslistings_images = (ImageView) findViewById(R.id.imageview_buisnesslistings_images);
        TextView textviewabout = (TextView) findViewById(R.id.business_about_txt);
        TextView textviewour = (TextView) findViewById(R.id.business_our);
        getdirection = (RelativeLayout) findViewById(R.id.getdirection);
        textview_about = (TextView) findViewById(R.id.about_description_txt);
        textview_nodocotorlist = (TextView) findViewById(R.id.buisness_text);
        business_profile_about_description_layout = (RelativeLayout) findViewById(R.id.business_profile_about_description_layout);
        textview_noslotlists = (TextView) findViewById(R.id.buisness_text1);


        textviewabout.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        textviewour.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        hours_list = (ListView) findViewById(R.id.hours_list);


        bcategories = getIntent().getParcelableExtra("buisnesslist");
        textview_buisness_docnm.setText(bcategories.getBusiness_name());
        textview_buisness_contactn.setText(bcategories.getPhone_number());
//        textview_buisness_specils.setText(bcategories.describeContents());
//        textview_buisnessprofile_book.setText("Book Now");
        texview_buisness_addressn.setText(bcategories.getAddress() + " " + bcategories.getCity() + " " + bcategories.getCountry() + " " + bcategories.getZipcode());
        imageLoader.displayImage(bcategories.getBusiness_logo(), imageview_buisnesslistings_images, options);
        ardoctors = bcategories.getArrayDoctor();

        if (ardoctors.size() == 0) {
            textview_nodocotorlist.setVisibility(View.VISIBLE);
            textview_nodocotorlist.setText("No Doctors");
        } else {
            textview_nodocotorlist.setVisibility(View.GONE);
        }
        if (bcategories.getAbout_us().equals("")) {
            business_profile_about_description_layout.setVisibility(View.GONE);
//            textview_about.setText(bcategories.getAbout_us());
        } else {
            business_profile_about_description_layout.setVisibility(View.VISIBLE);
            textview_about.setText(bcategories.getAbout_us());
        }


    }

//    public void business_image_loader() {
//        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(BusinessProfile.this));
//        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher)
//                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(900)).build();
//        imageLoader.displayImage("http://www.keenthemes.com/preview/metronic/theme/assets/global/plugins/jcrop/demos/demo_files/image1.jpg", business_profile_business_images, options);
//    }


// set top busness profile in the screen
    public void business_profile() {
        scroll1.setVisibility(View.GONE);
        scroll2.setVisibility(View.VISIBLE);
        relativeLayout_business_profile_layout.setVisibility(View.VISIBLE);
        relativeLayout_business_hour_layout.setVisibility(View.GONE);
        adapter = new Bussiness_profile_adapter(BusinessProfile.this, ardoctors);
        business_profile_doctor_list_view.setAdapter(adapter);
        relativeLayout_profile_button_layout.setBackgroundColor(getResources().getColor(R.color.business_profile_background_color2));
        relativeLayout_hour_button_layout.setBackgroundColor(getResources().getColor(R.color.business_profile_background_color));
        profile_button.setTextColor(getResources().getColor(R.color.button_txt_color));
        business_hour_button.setTextColor(getResources().getColor(R.color.button_txt_color1));
        int totalHeight1 = 0;
        int desiredWidth1 = View.MeasureSpec.makeMeasureSpec(hours_list.getWidth(), View.MeasureSpec.AT_MOST);
        System.out.println("count  "+adapter.getCount());
        for (int i = 0; i < adapter.getCount(); i++) {
            System.out.println("count  "+i);
            View listItem = adapter.getView(i, null, business_profile_doctor_list_view);
            listItem.measure(0, 0);
            totalHeight1 += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params1 = business_profile_doctor_list_view.getLayoutParams();
        params1.height = totalHeight1 + (business_profile_doctor_list_view.getDividerHeight() * (adapter.getCount() - 1));
        business_profile_doctor_list_view.setLayoutParams(params1);
        business_profile_doctor_list_view.requestLayout();

    }
// set busness hours
    public void business_hours() {
        arrayadapter = new Adapter();
        hours_list.setAdapter(arrayadapter);
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(hours_list.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < arrayadapter.getCount(); i++) {
            View listItem = arrayadapter.getView(i, null, hours_list);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = hours_list.getLayoutParams();
        params.height = totalHeight + (hours_list.getDividerHeight() * (arrayadapter.getCount() - 1));
        hours_list.setLayoutParams(params);
        hours_list.requestLayout();
        scroll1.setVisibility(View.VISIBLE);
       scroll2.setVisibility(View.GONE);
        relativeLayout_business_hour_layout.setVisibility(View.VISIBLE);
        relativeLayout_business_profile_layout.setVisibility(View.GONE);
        relativeLayout_hour_button_layout.setBackgroundColor(getResources().getColor(R.color.business_profile_background_color2));
        relativeLayout_profile_button_layout.setBackgroundColor(getResources().getColor(R.color.business_profile_background_color));
        business_hour_button.setTextColor(getResources().getColor(R.color.button_txt_color));
        profile_button.setTextColor(getResources().getColor(R.color.button_txt_color1));
    }

    // set the height of the listing of the doctor list and hour list
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

//Adapter for the list
     public class Adapter extends BaseAdapter {
        // View lookup cache
        LayoutInflater inflater;
        TextView name, times;

        public Adapter() {


        }

        @Override
        public int getCount() {
            return aravailaablities.size();
        }

        @Override
        public Object getItem(int position) {
            return aravailaablities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return aravailaablities.indexOf(aravailaablities.get(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position


                LayoutInflater inflater = LayoutInflater.from(BusinessProfile.this);
                convertView = inflater.inflate(R.layout.hours_lists, parent, false);
                name = (TextView) convertView.findViewById(R.id.textview_buisnesshours_days);
                times = (TextView) convertView.findViewById(R.id.textview_buisnesshours_times);
//                name.setTypeface(Typeface.createFromAsset(BusinessProfile.this.getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
//                times.setTypeface(Typeface.createFromAsset(BusinessProfile.this.getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
//                convertView.setTag(viewHolder);

            // Populate the data into the template view using the data object
            name.setText(aravailaablities.get(position).getAvailability_day());
            if(!aravailaablities.get(position).getAvailability_status().equals("no")) {
                times.setText(aravailaablities.get(position).getAvailability_from() + "" + aravailaablities.get(position).getAvailability_schedule_from() + "-" + aravailaablities.get(position).getAvailability_to() + "" + aravailaablities.get(position).getAvailability_schedule_to());

            }
            else
            {
                times.setText("Closed");
            }
            // Return the completed view to render on screen
            return convertView;
        }
    }
}






