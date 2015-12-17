package com.baya;

/*
* created by neeraj
* this class is used for
* shiowing side menu
*
* */
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.baya.ApiRequest.RequestCompleteListener;
import com.baya.ApiRequest.VolleyRequest;
import com.baya.BeanClass.Appointment;
import com.baya.BeanClass.AppointmentBean;
import com.baya.Helper.Constants;

import java.util.ArrayList;
import java.util.List;

import com.baya.WebServices.URL;
import com.baya.adapter.CustomListAdapter;
import com.baya.adapter.List_Adapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONObject;


public class SampleListFragment extends Fragment implements RequestCompleteListener<JSONObject> {
    Dialog dialogs;View rootview;

    Appointment item ;
    TextView findaDoctor,user_name_txt;
    public static ImageView user_image;
    public static List<AppointmentBean> rowItems;
    ListView menulist;
    public static ListView upcominglist;
    Context mcontext=getActivity();
    String[] web = {
            "Home","Upcoming Appointments","Appointment History","My Favorites","Sign Out"} ;
    public static final Integer[] images = { R.drawable.search,
            R.drawable.search  };

    public static final String[] descriptions = new String[] {
            "It is an aggregate accessory fruit",
            "It is the largest herbaceous flowering plant", "Citrus Fruit",
            "Mixed Fruits" };
    public static final String[] titles = new String[] { "Elite Dental Care"};
    Integer[] imageId = {R.drawable.home ,R.drawable.upcoming_appointments, R.drawable.appointment_history, R.drawable.my_favorite, R.drawable.sign_out,};
    TextView textview_menuupcoming_appointm,textview_menutextlist;
    Landing_Screen mactvty;
    private SharedPreferences preferences;
    String profile_image,basepath,user_first_name,user_last_name;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
 static List_Adapter listadapter=null;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview= inflater.inflate(R.layout.sliding_menu_layout, null);
        preferences= getActivity().getSharedPreferences(Constants.BAYA, 0);
        profile_image = preferences.getString("profileimage", "");
        basepath =preferences.getString("basePath", "");
        user_first_name =preferences.getString("first_name","");
        user_last_name = preferences.getString("last_name", "");
        user_image =(ImageView)rootview.findViewById(R.id.user_image);

        menulist =(ListView)rootview.findViewById(R.id.llist);

        user_name_txt=(TextView) rootview.findViewById(R.id.user_name_txt);
        user_name_txt.setText(user_first_name+" "+user_last_name);
        textview_menuupcoming_appointm=(TextView)rootview.findViewById(R.id.textview_menuupcoming_appointm);
        textview_menutextlist=(TextView)rootview.findViewById(R.id.textview_menu);
        textview_menuupcoming_appointm.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"museo/JUSTOLDFASHION_5.TTF"));
        textview_menutextlist.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/JUSTOLDFASHION_5.TTF"));
        profile_imageLoader();
        rowItems = new ArrayList<AppointmentBean>();
//        for (int i = 0; i < titles.length; i++) {
//            Appointment item = new Appointment(images[i], titles[i], descriptions[i]);
//            rowItems.add(item);
//        }

        CustomListAdapter adapter = new CustomListAdapter(getActivity(),web,imageId);
        menulist.setAdapter(adapter);
        menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment replace = null;
                switch (position) {
                    case 0:


                        replace = new Mediator();
                        break;
                    case 1:

                        replace = new Mediator();
                        break;
                    case 2:

                        replace = new Mediator();

                        break;
                    case 3:
                        Landing_Screen.image.setVisibility(View.GONE);
                        replace = new Mediator();
                        break;
                    case 4:
                        dialogs = new Dialog(getActivity());
                        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogs.setContentView(R.layout.signout);
                        dialogs.show();

                        TextView textview_header = (TextView) dialogs.findViewById(R.id.textview__header);
                        TextView textview_message = (TextView) dialogs.findViewById(R.id.textview__messages);
                        Button request = (Button) dialogs.findViewById(R.id.button_ok);
                        Button cancel = (Button) dialogs.findViewById(R.id.button_cancel);


                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogs.dismiss();
                            }
                        });
                        request.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if (Constants.isInternetOn(getActivity())) {

                                    LOG_OUT();
                                } else {
                                    Constants.showMessage("No Internet Connection", getActivity());
                                }
                            }
                        });
                        textview_header.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_0.OTF"));
                        textview_message.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_0.OTF"));
                        request.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_0.OTF"));
                        cancel.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_0.OTF"));

                        break;
                    default:
                        break;

                }
                if (replace != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    replace.setArguments(bundle);
                    FragmentTransaction ft = mactvty.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, replace);
                    ft.commit();
                    mactvty.toggle();
                }

            }
        });
        upcominglist =(ListView)rootview.findViewById(R.id.listview);
        listadapter= new List_Adapter(getActivity(),rowItems);
//        setListViewHeightBasedOnChildren(upcominglist, adapter);
        setListViewHeightBasedOnChildren(menulist);
        setlisteners();;
        /*set imageview 20% below from top the screen*/
        RelativeLayout relativeLayout =(RelativeLayout)rootview.findViewById(R.id.logoLayout);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                (int) (displaymetrics.heightPixels * 20) / 100);
        RelativeLayout.LayoutParams paramimage = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                (int) (displaymetrics.heightPixels * 15) / 100);
        paramimage.addRule(RelativeLayout.CENTER_IN_PARENT);


      //  relativeLayout.setLayoutParams(param);

        return rootview;
    }

    // logout request
    void LOG_OUT() {

        VolleyRequest volley=new VolleyRequest(getActivity(),SampleListFragment.this);
        SharedPreferences p=getActivity().getSharedPreferences(Constants.BAYA,0);
        volley.makeRequest(Request.Method.GET, URL.LOGOUT.getUrl() + "?access_token=" + p.getString(Constants.ACCESS_TOKEN, ""), null, null, "", "logout", true);
    }

    //profile imge load work
    public void profile_imageLoader() {

        System.out.println("path  "+basepath+profile_image);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_profile).showImageForEmptyUri(R.drawable.default_profile)
                .showImageOnFail(R.drawable.default_profile).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(900)).build();
        imageLoader.displayImage(basepath + profile_image, user_image, options);
        user_name_txt.setText(user_first_name + "  " + user_last_name);

    }

    // set the height of the menu iteme
    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);


        Log.e("Height of listView", "" + String.valueOf(totalHeight));
    }

    // set the height of the upcoming appointment
    public static void setListViewHeightBasedOnChildren1(ListView listView, List_Adapter adapter,ArrayList<AppointmentBean> appointmentarray) {
        rowItems=appointmentarray;
        if(rowItems.size()==0)
        {
            AppointmentBean app = new AppointmentBean("", "", "No Appointment available", "",
                    "", "", "","Please Create Appointment First","","","", "", "", "",
                   "", "","", "", "");
            rowItems.add(app);
        }
        upcominglist.setAdapter(adapter);
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        upcominglist.setLayoutParams(params);
        listView.requestLayout();
        upcominglist.requestLayout();
        Log.e("Height of listView", "" + String.valueOf(totalHeight));
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("image  "+profile_image);
        mactvty=(Landing_Screen)getActivity();
        profile_image = preferences.getString("profileimage", "");
        basepath =preferences.getString("basePath", "");
        user_first_name =preferences.getString("first_name","");
        user_last_name = preferences.getString("last_name", "");

    }
    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.user_image:
                    Intent intent=new Intent(getActivity(),Profile.class);
                    getActivity().startActivity(intent);
                    break;
            }
        }
    };


    // responce of the request api
    @Override
    public void onTaskComplete(String tag, JSONObject response) {

        try
        {   String sucess=response.getString("success");
            if(response.getString("success").equals("true"))
            {
                dialogs.dismiss();
                SharedPreferences.Editor e=preferences.edit();
                e.putString(Constants.ACCESS_TOKEN,"");
                e.putString("profileimage","");
                e.commit();
                Constants.showMessage(response.getString("message"), getActivity());
                Intent intent=new Intent(getActivity(),Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
            else
            {
                dialogs.dismiss();
                Constants.showMessage(response.getString("message"), getActivity());
            }
        }
        catch(Exception exception)
        {
            dialogs.dismiss();
        }
    }
    void setlisteners()
    {
        user_image.setOnClickListener(listener);
    }
}

