package com.baya;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by prachi on 02-11-2015.
 */
public class Mediator extends BaseContainers{private boolean mIsViewInited;
    Bundle b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.e("test", "tab 1 oncreateview");
        return inflater.inflate(R.layout.contentframe, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.e("test", "tab 1 container on activity created");
        if (!mIsViewInited)
        {
            mIsViewInited = true;
            initView();
        }
    }

    private void initView()
    {

        SharedPreferences preferences;
        switch (this.getArguments().getInt("position"))
        {
            case 0:
               // Landing_Screen.language_icon.setVisibility(View.VISIBLE);
                replaceFragment(new FindaBuisness(), false);
                break;
            case 1:

              //  Landing_Screen.language_icon.setVisibility(View.GONE);
                replaceFragment(new UpcomingAppointments(), false);
                break;
            case 2:

                // preferences=getActivity().getSharedPreferences("taxidriver", 0);
                // String page_countt=preferences.getString("pagecount", "");
                // DriverMyRideList.pagecount=Integer.parseInt(page_countt);
               // Landing_Screen.language_icon.setVisibility(View.VISIBLE);
                replaceFragment(new Appointment_history(), false);
                break;
            case 3:


                replaceFragment(new FavoriteBusiness(), false);
                break;
        }

    }
}
