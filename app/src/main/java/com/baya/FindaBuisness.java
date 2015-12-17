package com.baya;
/*
* this screen is a search filter for the
* Business , we can put the location, category and enter keyword for the search
*
* */
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;




public class FindaBuisness extends Fragment implements RequestCompleteListener<JSONObject>, AdapterView.OnItemClickListener {

    // declaration  of the variable and object
    GridView grid;
    Categories categories;
    ArrayList<Categories> arrcategories = new ArrayList<Categories>();
    TextView findtexts;
    LinearLayout mainlayout=null;
    protected static String baseurls = "";
    CustomGrid adapter;
    int mSelectedPosition;
    String categorids = "";
    private static final String LOG_TAG = "BAYA";
String str="";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    DisplayImageOptions imageOptions;
    ArrayList<String> resultList1;
    AutoCompleteTextView edittext_searchedbylocation;
    // ------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyCvdC6k39Cy2BNJk9LHrxZD7FIomcXg8pU";
    private String placelatitude="0.0", placelongitude="0.0";
    private Dialog dialog;
    private ListView language_list;
    EditText edittextsearch=null;
    private ArrayAdapter<String> languageadapter;
    ArrayList<String> language=null;
    ArrayList<String> languagecode=null;
    private String str_language="",str_languagecode="";
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_finda_doctor, null);
        categorids = "";
        language=new ArrayList<String>();
        languagecode=new ArrayList<String>();
      //  Landing_Screen.language_icon.setVisibility(View.VISIBLE);
        initview();
        preferences = getActivity().getSharedPreferences(Constants.BAYA, 0);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        } catch (Exception e) {

        }

        resultList1 = new ArrayList<String>();

        // calling category api
        category();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            int bottomPadding =0;
            System.out.println("height  " + getNavBarHeight());
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
            findtexts.setLayoutParams(p);
            findtexts.setPadding(10, 30, 10, 30);
        } else{
            // do something for phones running an SDK before lollipop
            findtexts.setPadding(10, 20, 10, 20);
        }

        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
// find button click for searching the business
        findtexts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                try {
                    InputMethodManager im = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                } catch (Exception e) {

                }
                if(!edittext_searchedbylocation.getText().toString().equals("")) {
                    getLatlng(edittext_searchedbylocation.getText().toString());
                }
                else
                {
                    Bundle b = new Bundle();
                    if(edittext_searchedbylocation.getText().toString().equals("")) {
                        placelatitude = "0.0";
                        placelongitude = "0.0";
                    }
                    b.putString("latitude", placelatitude);
                    b.putString("longitudes", placelongitude);

                    b.putString("categorids", categorids);
                    b.putString("name", edittext_searchedbynam.getText().toString());
                    BuisnessListings buisnesslistings = new BuisnessListings();
                    buisnesslistings.setArguments(b);
                    ((BaseContainers) getParentFragment()).replaceFragment(buisnesslistings, true);
                }

            }
        });


        return views;
    }

    // geting the nevigation bar height
    private int getNavBarHeight() {
        Resources r = getResources();
        int id = r.getIdentifier("navigation_bar_height", "dimen", "android");
        return r.getDimensionPixelSize(id);
    }

    // dialog for the language
    public void dialogLanguageList() {

        if (arrcategories.size() != 0) {

            dialog = new Dialog(getActivity());

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_list);

            language_list = (ListView) dialog.findViewById(R.id.lst);
            ImageButton close = (ImageButton) dialog.findViewById(R.id.close);
            edittextsearch = (EditText) dialog.findViewById(R.id.search_box_city);
            edittextsearch.setVisibility(View.GONE);
            TextView txtTitle=(TextView) dialog.findViewById(R.id.txtTitle);
            txtTitle.setText("Select Category");

            close.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    dialog.dismiss();
                }
            });

            SetLanguagedata();

            dialog.show();
        } else {
            Constants.showMessage("No Language", getActivity());
        }
    }


    // set data in language dialog
    public void SetLanguagedata() {

        languageadapter = new ArrayAdapter<String>(getActivity(), R.layout.dialog_list_items, language);

        language_list.setAdapter(languageadapter);
        language_list.setChoiceMode(1);
        if (str_language.equals("")) {

        } else {
            language_list.setItemChecked(language.indexOf(str_language), true);
        }

        language_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

                dialog.dismiss();
                str_language = arg0.getItemAtPosition(pos).toString();
str_languagecode=languagecode.get(pos);


            }
        });

    }
    View views;
    EditText edittext_searchedbynam;


    // response of the converting address to latitude and longitude
    @Override
    public void onTaskComplete(String tag, JSONObject response) {
        String success;
        try {
            System.out.println("ress  "+tag);
            if(tag.equals("LATLNG"))
            {



                    try
                    {
                        placelongitude = String.valueOf(((JSONArray) response.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                        placelatitude = String.valueOf(((JSONArray) response.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                        System.out.println("lat lng"+placelongitude+" "+placelatitude);
                        Bundle b = new Bundle();
                        if(placelatitude.equals("")) {
                            placelatitude = "0.0";
                            placelongitude = "0.0";
                        }
                        b.putString("latitude", placelatitude);
                        b.putString("longitudes", placelongitude);

                        b.putString("categorids", categorids);
                        b.putString("name", edittext_searchedbynam.getText().toString());
                        BuisnessListings buisnesslistings = new BuisnessListings();
                        buisnesslistings.setArguments(b);
                        ((BaseContainers) getParentFragment()).replaceFragment(buisnesslistings, true);


                    }
                    catch (JSONException e)
                    {
                        Bundle b = new Bundle();

                            placelatitude = "0.0";
                            placelongitude = "0.0";

                        b.putString("latitude", placelatitude);
                        b.putString("longitudes", placelongitude);

                        b.putString("categorids", categorids);
                        b.putString("name", edittext_searchedbynam.getText().toString());
                        BuisnessListings buisnesslistings = new BuisnessListings();
                        buisnesslistings.setArguments(b);
                        ((BaseContainers) getParentFragment()).replaceFragment(buisnesslistings, true);
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }






            }
            else if (response.getString("success").equals("true")) {
                baseurls = response.getString("basePath");
                JSONArray ar = response.getJSONArray("data");

                arrcategories = new ArrayList<Categories>();
                for (int n = 0; n < ar.length(); n++) {
                    JSONObject js = ar.getJSONObject(n);
                    String categoryname = js.getString("category_name");
                    String _id = js.getString("_id");
                    String category_icon = js.getString("category_icon");
                    categories = new Categories(categoryname, _id, baseurls+category_icon, "", "0");
                    arrcategories.add(categories);

                }
                Gson gs = new Gson();
                SharedPreferences.Editor e = preferences.edit();
                e.putString("Category", gs.toJson(arrcategories));
                e.commit();
                adapter = new CustomGrid(getActivity(), arrcategories);
                grid.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                System.out.println("name " + arrcategories.get(0).getCategoryname());
                System.out.println("type " + arrcategories.get(0).getCategoryicon());

//                Constants.showMessage(, getActivity());
            } else {
                Constants.showMessage(response.getString("message"), getActivity());
            }
        } catch (Exception exception) {

        }
    }


    // requesting of the converting the address to latitude and longitude
    private void getLatlng(String address)
    {

        if (Constants.isInternetOn(getActivity()))
        {

            VolleyRequest request = new VolleyRequest(getActivity(), FindaBuisness.this);
            request.makeRequest(Request.Method.POST, "https://maps.google.com/maps/api/geocode/json?address=" + address.replace(" ", "%20")
                    + "&AIzaSyDpA0kPWBa7xeQfEvS-OSV9eu31-dKGicI" + "&sensor=true", null, null, "","LATLNG", true);

        }
        else
        {
            Constants.showMessage("No internet Connection",getActivity());
        }

    }

   // initialization of the view
    void initview() {
        edittext_searchedbylocation = (AutoCompleteTextView) views.findViewById(R.id.find_doctor_search_location_edt);
        grid = (GridView) views.findViewById(R.id.find_doctor_gridview);
        findtexts = (TextView) views.findViewById(R.id.submit_area);
        edittext_searchedbynam = (EditText) views.findViewById(R.id.find_doctor_search_name_edt);

        ImageView back = (ImageView) views.findViewById(R.id.find_doctor_back_image);
        edittext_searchedbynam.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/JUSTOLDFASHION_5.TTF"));
        edittext_searchedbylocation.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/JUSTOLDFASHION_5.TTF"));
        Landing_Screen.content.setText("Find a Business");
        Landing_Screen.image.setVisibility(View.GONE);
//        edittext_searchedbynam.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
//        edittext_searchedbylocation.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        Landing_Screen.content.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        TextView textview_findba_location = (TextView) views.findViewById(R.id.find_doctor_select_loction_text);
        TextView textview_finda_speciality = (TextView) views.findViewById(R.id.find_doctor_select_speciality_text);
        textview_findba_location.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_finda_speciality.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        resultList1 = new ArrayList<String>();
        edittext_searchedbylocation.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.single_text_item, resultList1));

        edittext_searchedbylocation.setOnItemClickListener(this);
        imageloader = ImageLoader.getInstance();
        imageloader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.defult_icon).showImageOnFail(R.drawable.defult_icon).showImageOnLoading(R.drawable.defult_icon).cacheInMemory(true).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(900)).build();


    }

// calling speciality api
    void category() {
        if (Constants.isInternetOn(getActivity())) {
            VolleyRequest volley = new VolleyRequest(getActivity(), FindaBuisness.this);
            volley.makeRequest(Request.Method.GET, URL.GETCATEGORY.getUrl(), null, null, "", "GET_CATEGORY", true);
        } else {
            Constants.showMessage("Please Connect to Internet", getActivity());
        }

    }

    ImageLoader imageloader;
    DisplayImageOptions doptions;


    // Grid adapter for the category
    class CustomGrid extends BaseAdapter {
        private Context mContext;
        ArrayList<Categories> ar;

        public CustomGrid(Context c, ArrayList<Categories> ar) {
            mContext = c;
            this.ar = ar;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrcategories.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.scrollview, null);
            TextView textView = (TextView) convertView.findViewById(R.id.grid_text);
            textView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "museo/MUSEO_SLAB_0.OTF"));
            textView.setText(arrcategories.get(position).getCategoryname());
            RelativeLayout rlayout = (RelativeLayout) convertView.findViewById(R.id.rlayout);
            final RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.rlayout);
            final ImageView imageView_black = (ImageView) convertView.findViewById(R.id.grid_image);


            imageloader.displayImage(arrcategories.get(position).getCategoryicon(), imageView_black,imageOptions);
            textView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "museo/JUSTOLDFASHION_5.TTF"));

            if (arrcategories.get(position).getState().equals("1")) {
                rlayout.setBackgroundColor(getResources().getColor(R.color.lightgrey));
//                imageView_black.setBackgroundResource(R.drawable.blue_oval);

            } else {
                rlayout.setBackgroundColor(Color.WHITE);
//                imageView_black.setBackgroundResource(R.drawable.grid_circle_image_view);
            }
            rlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("arrrrrrrrrrrrrrr"+v);
                    categorids = arrcategories.get(position).getIds();
                    for (int m = 0; m < arrcategories.size(); m++) {
                        if (m == position) {
                            if (arrcategories.get(m).getState().equals("0")) {
                                arrcategories.get(m).setState("1");
//                                imageView_black.setBackgroundResource(R.drawable.blue_oval);
//                                imageloader.displayImage("http://192.168.0.157:2171/uploads/category/defult_icon.png", imageView_black);
                            } else {
                                arrcategories.get(m).setState("0");
//                                imageView_black.setBackgroundResource(R.drawable.grid_circle_image_view);
//                                imageloader.displayImage("http://192.168.0.157:2171/uploads/category/defult_icon.png", imageView_black);
                            }
                        } else {
                            arrcategories.get(m).setState("0");

                        }
                    }
                    notifyDataSetChanged();
                // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                    try {
//                        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
//                        im.hideSoftInputFromWindow(rlayout.getView().getWindowToken(), 0);
//                    } catch (Exception e) {
//
//                    }
                    if(!edittext_searchedbylocation.getText().toString().equals("")) {
                        getLatlng(edittext_searchedbylocation.getText().toString());
                    }
                    else
                    {
                        Bundle b = new Bundle();
                        if(edittext_searchedbylocation.getText().toString().equals("")) {
                            placelatitude = "0.0";
                            placelongitude = "0.0";
                        }
                        b.putString("latitude", placelatitude);
                        b.putString("longitudes", placelongitude);

                        b.putString("categorids", categorids);
                        b.putString("name", edittext_searchedbynam.getText().toString());
                        BuisnessListings buisnesslistings = new BuisnessListings();
                        buisnesslistings.setArguments(b);
                        ((BaseContainers) getParentFragment()).replaceFragment(buisnesslistings, true);
                    }
                }
            });
            return convertView;
        }

        public void setSelectedItemPosition(int position) {
            mSelectedPosition = position;
        }
    }


    @SuppressWarnings("static-access")
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

         str = (String) adapterView.getItemAtPosition(position);


    }


    // Handler

    private class GeocoderHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:

                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");

                    placelatitude = bundle.getString("latitude");
                    placelongitude = bundle.getString("longitudes");
                    System.out.println("placelongitude " + placelatitude);


                    break;
                default:
                    locationAddress = null;
            }

//            Intent intentmessage = new Intent();
//            intentmessage.putExtra("latitude", placelatitude);
//            intentmessage.putExtra("longitudes", placelongitude);
//            intentmessage.putExtra("placename", locationAddress);
//            setResult(Postjobs.RESULTCODES, intentmessage);
//            finish();
        }
    }



    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            // sb.append("&components=");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            java.net.URL url = new java.net.URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            System.out.println("types        " + jsonResults.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {

                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId, ArrayList<String> resultList1) {
            super(context, 0, resultList1);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_text_item, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.textview);
//            TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
            // Populate the data into the template view using the data object
            tvName.setText(resultList.get(position));
//            tvHome.setText(user.hometown);
            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        resultList1 = resultList;
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();

                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (results != null && results.count > 0) {

                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
}

