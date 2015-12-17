package com.baya;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.baya.ApiRequest.RequestCompleteListener;
import com.baya.ApiRequest.VolleyRequest;
import com.baya.BeanClass.Categories;
import com.baya.BeanClass.state;
import com.baya.Helper.Constants;
import com.baya.WebServices.URL;
import com.baya.adapter.CustomGrid;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by hp on 04-12-2015.
 * Add business listing class , In this class we are Adding business
 */
public class AddListing extends Activity implements RequestCompleteListener<JSONObject>{

    // the declaration of variable and object
    TextView heading=null,business_category=null,business_state=null,submit=null;
    EditText buisness_name=null,business_neighour=null,business_phone=null,business_address=null,business_city=null,business_zip_code=null,edittextsearch=null;
    ArrayList<state> arraymainstate=null;
    ArrayList<String> statearray=null;
    ArrayList<Categories> arrcategories=null;
    ArrayList<String> categoryarray=null;
    String str_category="";
    String str_category_id="";
    Dialog dialog=null;
    ListView alldialogliststate=null,category_list;
    ArrayAdapter<String> stateadapter=null,categoryadapter=null;
    String state_str="",state_id="",str_business_name="",str_neighbur="",str_address="",str_city="",str_phone="",str_zipcode="";
    SharedPreferences preferences=null;
    ArrayList<String> category_idarray =null;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_listing);
        back=(ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        arraymainstate=new ArrayList<state>();
        statearray=new ArrayList<String>();
        categoryarray=new ArrayList<String>();
         preferences = AddListing.this.getSharedPreferences(Constants.BAYA, 0);
        category_idarray=new ArrayList<String>();
        intiView();
        setListner();
        // calling category and state api
        callWebService();
    }

    private void callWebService() {
        if (Constants.isInternetOn(AddListing.this)) {
            VolleyRequest volley = new VolleyRequest(AddListing.this, this);
            volley.makeRequest(Request.Method.GET, URL.STATE.getUrl(), null, null, "", "GET_STATE", true);
        } else {
            Constants.showMessage("Please Connect to Internet",AddListing.this);
        }
    }
    void category() {
        if (Constants.isInternetOn(AddListing.this)) {
            VolleyRequest volley = new VolleyRequest(AddListing.this, this);
            volley.makeRequest(Request.Method.GET, URL.GETCATEGORY.getUrl(), null, null, "", "GET_CATEGORY", true);
        } else {
            Constants.showMessage("Please Connect to Internet", AddListing.this);
        }

    }
    // set click listner
    private void setListner() {
        business_state.setOnClickListener(listner);
        business_category.setOnClickListener(listner);
        submit.setOnClickListener(listner);
    }
// initializing view
    private void intiView() {
        heading=(TextView)findViewById(R.id.heading);
        business_category=(TextView)findViewById(R.id.business_category);
        business_state=(TextView)findViewById(R.id.business_state);
        buisness_name=(EditText)findViewById(R.id.business_name);
        business_neighour=(EditText)findViewById(R.id.business_neighour);
        business_phone=(EditText)findViewById(R.id.business_phone);
        business_address=(EditText)findViewById(R.id.business_address);
        business_city=(EditText)findViewById(R.id.business_city);
        business_zip_code=(EditText)findViewById(R.id.business_zip_code);
        submit=(TextView)findViewById(R.id.submit);

        //business_phone.addTextChangedListener(new FourDigitCardFormatWatcher());
        business_neighour.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        business_category.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        business_state.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        buisness_name.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        business_phone.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        business_address.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        business_city.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        business_zip_code.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        heading.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        Spannable WordtoSpan = new SpannableString("Fill and submit the information in order to add business listing");
        WordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#199AD9")), 48, 64,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        heading.setText(WordtoSpan);
    }
    View.OnClickListener listner=new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       switch (v.getId())
       {
           case R.id.business_state:
               dialogStateList();
               break;
           case R.id.business_category:
               dialogCategoryList();
               break;
           case R.id.submit:
               callAddListingService();
               break;
       }
     }
    };
// Add listing api
    private void callAddListingService() {
        try {
            str_business_name = buisness_name.getText().toString().trim();
            str_address = business_address.getText().toString().trim();
            str_phone = business_phone.getText().toString();
            str_city = business_city.getText().toString().trim();
            str_neighbur = business_neighour.getText().toString().trim();
            str_zipcode = business_zip_code.getText().toString();
            if (str_business_name.equals("")) {
                Constants.showMessage("Please enter business name", AddListing.this);
                return;
            } else if (category_idarray.size()==0) {
                Constants.showMessage("Please select business category", AddListing.this);
                return;
            }
//            else if (str_neighbur.equals("")) {
//                Constants.showMessage("Please enter Neighbour", AddListing.this);
//                return;
//            }

            else if (str_phone.equals("")) {
                Constants.showMessage("Please enter phone number", AddListing.this);
                return;
            } else if (str_phone.length() < 10) {
                Constants.showMessage("Please enter valid phone number", AddListing.this);
                return;
            } else if (str_address.equals("")) {
                Constants.showMessage("Please enter address", AddListing.this);
                return;
            } else if (str_city.equals("")) {
                Constants.showMessage("Please enter city", AddListing.this);
                return;
            } else if (state_id.equals("")) {
                Constants.showMessage("Please select state", AddListing.this);
                return;
            } else if (str_zipcode.equals("")) {
                Constants.showMessage("Please enter zipcode", AddListing.this);
                return;
            }

            JSONObject req = new JSONObject();
            JSONArray categoryselect=new JSONArray();
            for (int i=0;i<category_idarray.size();i++) {
                categoryselect.put(category_idarray.get(i));
            }
            req.put("business_category",categoryselect);
            req.put("business_name", str_business_name);
            req.put("neighbour", str_neighbur);
            req.put("phone_number", str_phone);
            req.put("address", str_address);
            req.put("state", state_id);
            req.put("city", str_city);
            req.put("listing_id", "");
            req.put("zipcode", str_zipcode);
            System.out.println("json  "+req);

            if (Constants.isInternetOn(AddListing.this)) {
                VolleyRequest volley = new VolleyRequest(AddListing.this, this);
                volley.makeRequest(Request.Method.POST, URL.ADDLISTING.getUrl() + "access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""), req, null, "", "ADDLISTING", true);
            } else {
                Constants.showMessage("Please Connect to Internet", AddListing.this);
            }
        }catch (Exception e)
        {

        }

    }

    public void dialogStateList() {

        if (arraymainstate.size() != 0) {

            dialog = new Dialog(AddListing.this);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_list);

            alldialogliststate = (ListView) dialog.findViewById(R.id.lst);
            ImageButton close = (ImageButton) dialog.findViewById(R.id.close);
            edittextsearch = (EditText) dialog.findViewById(R.id.search_box_city);
            TextView txtTitle=(TextView) dialog.findViewById(R.id.txtTitle);
            txtTitle.setText("Select State");
            edittextsearch.addTextChangedListener(filterTextWatcher1);
            close.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    dialog.dismiss();
                }
            });

            Setstatedata();

            dialog.show();
        } else {
            Constants.showMessage("No Cities Are Available", AddListing.this);
        }
    }

    public void Setstatedata() {

        stateadapter = new ArrayAdapter<String>(AddListing.this, R.layout.dialog_list_items, statearray);

        alldialogliststate.setAdapter(stateadapter);
        alldialogliststate.setChoiceMode(1);
        if (state_str.equals("")) {

        } else {
            alldialogliststate.setItemChecked(statearray.indexOf(state_str), true);
        }

        alldialogliststate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

                dialog.dismiss();
                state_str = alldialogliststate.getItemAtPosition(pos).toString();

                for (int i = 0; i < arraymainstate.size(); i++) {

                    if (arraymainstate.get(i).getState_name().equals(state_str)) {

                        state_id = arraymainstate.get(i).get_id();

                        business_state.setText(state_str);

                    }
                }

            }
        });

    }
    // text watcher for add "-" in phone number
    public static class FourDigitCardFormatWatcher implements TextWatcher {

        // Change this to what you want... ' ', '-' etc..
        private static final char space = '-';

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove spacing char
            if (s.length() > 0 && (s.length() % 4) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (space == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 4) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a
                // space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 2) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }
        }
    }
    // dialog for category
    public void dialogCategoryList() {

        if (arrcategories.size() != 0) {

            dialog = new Dialog(AddListing.this);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.multiple_selection);
            category_list = (ListView) dialog.findViewById(R.id.lst);


            ImageButton close = (ImageButton) dialog.findViewById(R.id.close);
            edittextsearch = (EditText) dialog.findViewById(R.id.search_box_city);
           TextView txtTitle=(TextView) dialog.findViewById(R.id.txtTitle);
            txtTitle.setText("Select Category");
            edittextsearch.addTextChangedListener(filterTextWatcher);
            close.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        str_category = "";
                        category_idarray = new ArrayList<String>();
                        SparseBooleanArray checked = category_list.getCheckedItemPositions();

                        for (int i = 0; i < checked.size() + 1; i++) {
                            System.out.println("iddddd  " + checked.valueAt(i));
                            int position = checked.keyAt(i);
                            if (checked.valueAt(i)) {
                                category_idarray.add(arrcategories.get(position).getIds());
                                str_category = str_category + "," + arrcategories.get(position).getCategoryname();
                            }
                        }
                        str_category = str_category.substring(1, str_category.length());
                        business_category.setText(str_category);
                        dialog.dismiss();

                    }
                    catch (Exception e)
                    {
                        str_category = "";
                        business_category.setText(str_category);
                        dialog.dismiss();
                    }

                }
            });

            Setcategorydata();

            dialog.show();
        } else {
            Constants.showMessage("No Cities Are Available", AddListing.this);
        }
    }
// set data of category
    public void Setcategorydata() {
        System.out.println("size   "+categoryarray.size());
        categoryadapter = new ArrayAdapter<String>(AddListing.this, R.layout.dialog_list_items, categoryarray);

        category_list.setAdapter(categoryadapter);
        category_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        if(str_category.length()!=0) {
            for (int i = 0; i < str_category.split(",").length; i++) {

                category_list.setItemChecked(categoryarray.indexOf(str_category.split(",")[i]), true);
            }
        }

        category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {


            }
        });

    }
    private TextWatcher filterTextWatcher1 = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                stateadapter.getFilter().filter(s);
                // 3 char data show
            } else if (s.length() == 0) {
                Setstatedata();
            }
        }
    };
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                categoryadapter.getFilter().filter(s);
                // 3 char data show
            } else if (s.length() == 0) {
               Setcategorydata();
            }
        }
    };

    // api response
    @Override
    public void onTaskComplete(String tag, JSONObject response) {
        try
        {
if(tag.equals("GET_STATE"))
{
    if (response.getString("success").equals("true"))
    {
        JSONArray jsonarray=response.getJSONArray("data");
        for (int i=0;i<jsonarray.length();i++)
        {
            arraymainstate.add(new state(jsonarray.getJSONObject(i).getString("_id"),jsonarray.getJSONObject(i).getString("state_name")));
            statearray.add(jsonarray.getJSONObject(i).getString("state_name"));
        }
        category();
    }
    else
    {
category();
    }
}
            else if(tag.equals("GET_CATEGORY"))
{
    if (response.getString("success").equals("true")) {
        JSONArray ar = response.getJSONArray("data");

        arrcategories = new ArrayList<Categories>();
        for (int n = 0; n < ar.length(); n++) {
            JSONObject js = ar.getJSONObject(n);
            String categoryname = js.getString("category_name");
            String _id = js.getString("_id");
            String category_icon = js.getString("category_icon");
         Categories  categories = new Categories(categoryname, _id, "", "", "0");
            arrcategories.add(categories);
             categoryarray.add(categoryname);
        }


    }
    else
    {
        Constants.showMessage(response.getString("message"), AddListing.this);
    }

//                Constants.showMessage(, getActivity());
} else {
    if (response.getString("success").equals("true"))
    {
        Constants.showMessage("Successfully added listing", AddListing.this);
        AddListing.this.finish();;
    }
    else
    {
        Constants.showMessage("Something went wrong", AddListing.this);
    }
}


        }
        catch(Exception e)
        {

        }
    }
}
