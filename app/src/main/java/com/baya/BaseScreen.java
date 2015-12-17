package com.baya;
/*
* this scree is used to show about application
* in view pager
* */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baya.Helper.Constants;
import com.baya.Helper.GPSTracker;

import com.viewpagerindicator.CirclePageIndicator;


public class BaseScreen extends Activity
{

    TextView textview_findadoctor, textview_findneardoctor, textview_bookanappointment, textview_bookneardoctor, textview_appreminder, textviewappnearreminder, t, textview, textviews;
    Button sign_in, Sign_up;
    ImageView imagelogo;
    private ViewPager view_pager;
    private boolean pagerMoved = false;
    private LocationManager locationManager;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences preferences = getSharedPreferences(Constants.BAYA, 0);
        if (preferences.getString(Constants.ACCESS_TOKEN, "").equals("")) {

        } else {



                Intent intent = new Intent(BaseScreen.this, Landing_Screen.class);
                intent.putExtra("position",0);
                startActivity(intent);
                finish();



        }
        setContentView(R.layout.activity_main);
        CirclePageIndicator titleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {

            Log.v("LogView", "no network GPS provider is enabled");
            showSettingsAlert();

        } else {

        }

        if (isGPSEnabled) {

            Intent in = new Intent(BaseScreen.this, GPSTracker.class);
            BaseScreen.this.startService(in);
        }



        view_pager = (ViewPager) findViewById(R.id.view_pager);
        TnCAdapter adapter = new TnCAdapter(this);
        view_pager.setAdapter(adapter);
        titleIndicator.setViewPager(view_pager);
        view_pager.setOnTouchListener(new OnTouchListener()
        {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                pagerMoved = true;
                return false;
            }
        });
    }
    void showSettingsAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseScreen.this);
        alertDialog.setTitle("Gps Settings");
        alertDialog.setMessage("Do you want to enable your GPS settings");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                BaseScreen.this.startActivity(callGPSSettingIntent);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event

                dialog.cancel();
            }
        });
        AlertDialog alerts = alertDialog.create();

        alerts.show();
    }

    class TnCAdapter extends PagerAdapter
    {
        Context mContext;

        public TnCAdapter(Context context)
        {
            mContext = context;
        }

        @Override
        public int getCount()
        {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            // TODO Auto-generated method stub
            return view == ((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            // TODO Auto-generated method stub
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            View rootview = inflater.inflate(R.layout.base_screen_first, container, false);
            rootview.setId(position);

            switch (position)
            {
                case 0:
                    rootview = inflater.inflate(R.layout.base_screen_first, container, false);
                    Log.e("base screen one", "base Screen one");
                    textview_findadoctor = (TextView) rootview.findViewById(R.id.find_doctor_txt);
                    textview_findneardoctor = (TextView) rootview.findViewById(R.id.find_doctor_txt_a);
                    textview_findadoctor.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
                    textview_findneardoctor.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
                    break;
                case 1:
                    rootview = inflater.inflate(R.layout.base_screen_second, container, false);
                    Log.e("base screen two", "base Screen two");
                    textview_bookanappointment = (TextView) rootview.findViewById(R.id.find_doctor_txt);
                    textview_bookneardoctor = (TextView) rootview.findViewById(R.id.find_doctor_txt_a);
                    textview_bookanappointment.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
                    textview_bookneardoctor.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));

                    break;
                case 2:
                    rootview = inflater.inflate(R.layout.base_screen_third, container, false);
                    Log.e("base screen three", "base Screen three");
                    textview_appreminder = (TextView) rootview.findViewById(R.id.find_doctor_txt);
                    textviewappnearreminder = (TextView) rootview.findViewById(R.id.find_doctor_txt_a);
                    textview_appreminder.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
                    textviewappnearreminder.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));

                    sign_in = (Button) rootview.findViewById(R.id.login_button);
                    Sign_up = (Button) rootview.findViewById(R.id.singup_button);
                    Sign_up.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(BaseScreen.this, SignUp.class);
                            startActivity(intent);
                        }
                    });
                    sign_in.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(BaseScreen.this, Login.class);
                            startActivity(intent);
                        }
                    });
                    sign_in.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
                    Sign_up.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));

                default:
                    break;
            }
            ((ViewPager) container).addView(rootview, 0);
            return rootview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            // TODO Auto-generated method stub
            ((ViewPager) container).removeView((View) object);
        }
    }

}
