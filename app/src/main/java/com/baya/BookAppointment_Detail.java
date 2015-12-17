package com.baya;
/*
* Final booking appointment detail class on this class we will submit the
* appointment
*
* */
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.baya.ApiRequest.RequestCompleteListener;
import com.baya.ApiRequest.VolleyRequest;
import com.baya.BeanClass.Getbuisness;
import com.baya.Helper.Constants;
import com.baya.WebServices.URL;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


public class BookAppointment_Detail extends Activity implements RequestCompleteListener<JSONObject> {


    // declaration of the variable
    private static final int DATE_PICKER_ID = 1111;
    TextView bookappointment_submit_txt=null;
    private EditText bookappointment_firstname_edt = null;
    private EditText bookappointment_lastname_edt =null;
    private EditText bookapppointment_mailid_edt =null;
    private EditText bookapppointment_contactno_edt =null,insurance_edt=null;
    private ImageView bookappointment_male_image =null;
    private ImageView bookappointment_female_image =null;
    private TextView bookappointment_birthday_txt = null;
    private TextView bookappointment_date_txt =null;
    private TextView bookappointment_time_txt =null;
    private  TextView bussinessname=null;
    private  TextView book_appointment_submit=null;
    private TextView book_appointment_gender_txt =null,specificdoctorname=null,reasontxt=null;
    private RelativeLayout relativeLayout_book_appointment_maleimage_layout =null;
    private RelativeLayout relativeLayout_book_appointment_femaleimage_layout =null;
    DatePicker booking_appointment_date_picker = null;
    private JSONObject request_json;
    private String first_name_str = "", last_name_str = "", email_str = "",contact_no_str="",gendet_str="",birthdate_str ="",insurance_str="";
    Calendar calendar = null;
    int day;
    int month;
    int year;
    Getbuisness bcategory=null;
    String str_selected_date="";
    String str_selected_time="";
    String str_reason="";
    String doctor_name="";
    String doctor_id="";
    String customer_type="";
    String business_id="";
    String access_token="";
    private Date selectdate;
    private SharedPreferences sharedpreferences;
    VolleyRequest vt;
    private String user_image;
    private VolleyRequest request;
    private  String appointment_date="";
    ImageButton back=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_book_appointment__detail);
        sharedpreferences = getSharedPreferences(Constants.BAYA, 0);
        access_token = sharedpreferences.getString(Constants.ACCESS_TOKEN, "");
        bcategory=getIntent().getParcelableExtra("buisnesslist");
        selectdate= (Date) getIntent().getExtras().get("calender");
        back=(ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        str_selected_date=(String) android.text.format.DateFormat.format("dd-MM-yyyy", selectdate);
        appointment_date=(String) android.text.format.DateFormat.format("yyyy-MM-dd", selectdate);
        str_selected_time=getIntent().getExtras().getString("time");
        str_reason=getIntent().getExtras().getString("reason");
        doctor_name=getIntent().getExtras().getString("doctor_name");
        doctor_id=getIntent().getExtras().getString("doctor_id");
        customer_type=getIntent().getExtras().getString("customer_type");
        business_id=bcategory.get_id();
        initView();
        bookappointment_submit_txt.setOnClickListener(listener);
        bookappointment_male_image.setOnClickListener(listener);
        bookappointment_female_image.setOnClickListener(listener);
        bookappointment_birthday_txt.setOnClickListener(listener);
        book_appointment_submit.setOnClickListener(listener);
        if(!customer_type.equals("new")) {
            request_profile();
        }
    }
    // API for the requesting of the patient profile
    private void request_profile()
    {
        try
        {
            if (Constants.isInternetOn(getApplicationContext()) == true)
            {
                profile();
            }
            else
            {
                Constants.showMessage("No Connection available", BookAppointment_Detail.this);
            }
        }
        catch (Exception exception)
        {
            Constants.showMessage("An errors in change password" + exception.toString(), BookAppointment_Detail.this);
        }

    }

    protected void profile()
    {
        vt = new VolleyRequest(BookAppointment_Detail.this, BookAppointment_Detail.this);
        vt.makeRequest(Request.Method.GET, URL.PROFILE.getUrl() + "access_token=" + sharedpreferences.getString(Constants.ACCESS_TOKEN, ""), null, null, "", "Profile", true);
        System.out.println("url  "+URL.PROFILE.getUrl() + "access_token=" + sharedpreferences.getString(Constants.ACCESS_TOKEN, ""));
    }


// view initialization
    public void initView()
    {
        bookappointment_submit_txt  = (TextView) findViewById(R.id.book_appointment_submit);
        bookappointment_firstname_edt =(EditText)findViewById(R.id.book_appointment_username_edt);
        bookappointment_lastname_edt =(EditText)findViewById(R.id.book_appointment_last_name_edt);
        bookapppointment_mailid_edt =(EditText)findViewById(R.id.book_appointment_email_id_edt);
        bookappointment_male_image =(ImageView)findViewById(R.id.book_appointment_male_imageview);
        bookappointment_female_image =(ImageView)findViewById(R.id.book_appointment_female_imageview);
        bookappointment_birthday_txt =(TextView)findViewById(R.id.book_appointment_birthday_txt);
        bookappointment_date_txt =(TextView)findViewById(R.id.book_appintment_date_text);
        bookappointment_time_txt =(TextView)findViewById(R.id.book_appointment_time_text);
        book_appointment_submit=(TextView)findViewById(R.id.book_appointment_submit);
        insurance_edt=  (EditText) findViewById(R.id.insurance_edt);
        bookapppointment_contactno_edt =(EditText)findViewById(R.id.book_appointment_contactno_edt);
        specificdoctorname =(TextView)findViewById(R.id.specificdoctorname);
        reasontxt=(TextView)findViewById(R.id.txt_reason);
        bussinessname=(TextView)findViewById(R.id.bussinessname);
        relativeLayout_book_appointment_maleimage_layout =(RelativeLayout)findViewById(R.id.book_appointment_maleimage_layout);
        relativeLayout_book_appointment_femaleimage_layout =(RelativeLayout)findViewById(R.id.book_appointment_femaleimage_layout);
        book_appointment_gender_txt =(TextView)findViewById(R.id.book_appointment_gender_txt);
        bookappointment_submit_txt.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        bookappointment_firstname_edt.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        bookappointment_lastname_edt.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        bookapppointment_mailid_edt.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        bookappointment_birthday_txt.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        bookappointment_date_txt.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        bookappointment_time_txt.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        bookapppointment_contactno_edt.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        reasontxt.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        bussinessname.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        book_appointment_gender_txt.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        book_appointment_submit.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        insurance_edt.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        //bookapppointment_contactno_edt.addTextChangedListener(new FourDigitCardFormatWatcher());
        bookappointment_date_txt.setText(str_selected_date);

        specificdoctorname.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        bookappointment_time_txt.setText(str_selected_time);
        bussinessname.setText(bcategory.getBusiness_name());
        specificdoctorname.setText(doctor_name);
        reasontxt.setText(str_reason);

    }
// click listner
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {

                case R.id.book_appointment_submit:
                    try
                    {
                        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    catch (Exception e)
                    {

                    }
                    book_appointment_request();
                    break;
                case R.id.book_appointment_male_imageview:
                    maleimage();
                    break;
                case R.id.book_appointment_female_imageview:
                    femaleImage();
                    break;
                case R.id.book_appointment_birthday_txt:
                    date_picker();
                    break;

                default:
                    break;
            }
        }
    };

// changing image color of the male
    public void maleimage()
    {
        gendet_str="male";
        book_appointment_gender_txt.setText("Male");
        String uri = "@drawable/male_active";
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        bookappointment_male_image.setImageDrawable(res);
        relativeLayout_book_appointment_maleimage_layout.setBackgroundColor(getResources().getColor(R.color.image_layout_one));

        String urione = "@drawable/female_inactive";
        int imageResourceone = getResources().getIdentifier(urione, null, getPackageName());
        Drawable resone = getResources().getDrawable(imageResourceone);
        bookappointment_female_image.setImageDrawable(resone);
        relativeLayout_book_appointment_femaleimage_layout.setBackgroundColor(getResources().getColor(R.color.image_layout_two));
    }
    // changing female image
    public void femaleImage()
    {
        gendet_str="female";
        book_appointment_gender_txt.setText("Female");
        String uri_female_active ="@drawable/female_active";
        int female_active_image_res =getResources().getIdentifier(uri_female_active,null,getPackageName());
        Drawable f_active_res = getResources().getDrawable(female_active_image_res);
        bookappointment_female_image.setImageDrawable(f_active_res);
        relativeLayout_book_appointment_femaleimage_layout.setBackgroundColor(getResources().getColor(R.color.image_layout_one));

        String uri_male_active ="@drawable/male_inactive";
        int male_inactive_image_res =getResources().getIdentifier(uri_male_active,null,getPackageName());
        Drawable m_active_res = getResources().getDrawable(male_inactive_image_res);
        bookappointment_male_image.setImageDrawable(m_active_res);
        relativeLayout_book_appointment_maleimage_layout.setBackgroundColor(getResources().getColor(R.color.image_layout_two));

    }

// date picker for date of birth
    public void date_picker()
    {

        calendar = Calendar.getInstance();
        year  = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day   = calendar.get(Calendar.DAY_OF_MONTH);

        showDialog(DATE_PICKER_ID);




    }
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
             try
             {
                DatePickerDialog dpDialog = new DatePickerDialog(this, pickerListener, year, month, day);
                dpDialog.getDatePicker().setMaxDate( new Date().getTime());
                DatePicker datePicker = dpDialog.getDatePicker();

                Calendar calendar = Calendar.getInstance();//get the current day


                return dpDialog;
        }
        catch (Exception e)
        {

        }
        }
        return null;
    }
// listner for the date selection
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            String daystr=""+day,monthstr=""+(month+1);
            if(daystr.length()==1)
            {
                daystr="0"+day;
            }
            if(monthstr.length()==1)
            {
                monthstr="0"+month;
            }
            // Show selected date
            bookappointment_birthday_txt.setText(new StringBuilder().append(daystr).append("-").append(monthstr).append("-").append(year)
            );

        }
    };

// date show method
    private void showDate(int year, int month, int day) {
        bookappointment_birthday_txt.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }






// api responce
    @Override
    public void onTaskComplete(String tag, JSONObject response) {
        try
        {
            if(tag.equals("Profile")) {

                Gson gs = new Gson();
                if (response.getString("success").equals("true")) {

                    Log.v("ShowView", "Response of Profile :" + response.toString());

                    JSONObject js = response.getJSONObject("data");


                    first_name_str = js.getString("first_name");
                    last_name_str = js.getString("last_name");
                    email_str = js.getString("email");
                    contact_no_str = js.getString("phone_number");
                    if (js.has("phone_number")) {
                        contact_no_str = js.getString("phone_number");
                        bookapppointment_contactno_edt.setText(contact_no_str);
                    } else {
                        bookapppointment_contactno_edt.setText("");
                    }
                    if (js.has("dob")) {
                        birthdate_str = js.getString("dob");
                        bookappointment_birthday_txt.setText(birthdate_str);
                    } else {
                        bookappointment_birthday_txt.setText("Select Birthdate");
                    }
                    if (js.has("gender")) {
                        gendet_str = js.getString("gender");
                        book_appointment_gender_txt.setText(gendet_str);
                        if (gendet_str.equals("female")) {

                        } else {

                        }
                    }
                    if (js.has("image")) {

                    }

                    bookappointment_firstname_edt.setText(first_name_str);
                    bookappointment_lastname_edt.setText(last_name_str);
                    bookapppointment_mailid_edt.setText(email_str);

                } else {
                    Constants.showMessage(response.getString("message"), BookAppointment_Detail.this);
                }
            }
            else if(tag.equals("BOOK"))
            {
                if (response.getString("success").equals("true"))
                {


                    Intent i=new Intent();
                    i.putExtra("business_name", bcategory.getBusiness_name());
                    i.putExtra("time", str_selected_time);
                    i.putExtra("date", str_selected_date);
                    setResult(1234, i);
                    BookAppointment_Detail.this.finish();
                }
                else
                {
                    Constants.showMessage(response.getString("message"), BookAppointment_Detail.this);
                }
            }
        }
        catch (JSONException x)
        {
            Constants.showMessage("erro r"+ x.toString(),BookAppointment_Detail.this );
        }
    }



// book appointment validation
    private void book_appointment_request() {


        first_name_str = bookappointment_firstname_edt.getText().toString().trim();
        last_name_str = bookappointment_lastname_edt.getText().toString().trim();
        email_str = bookapppointment_mailid_edt.getText().toString();
        contact_no_str = bookapppointment_contactno_edt.getText().toString().trim();
//        gendet_str = book_appointment_gender_txt.getText().toString();
        birthdate_str = bookappointment_birthday_txt.getText().toString();
        insurance_str=insurance_edt.getText().toString();

        if (first_name_str.equals("")) {
            Constants.showMessage("Please enter first name", BookAppointment_Detail.this);
            return;
        }
        if (last_name_str.equals("")) {
            Constants.showMessage("Please enter last name", BookAppointment_Detail.this);
            return;
        }
        if (email_str.equals("")) {
            Constants.showMessage("Please enter email address", BookAppointment_Detail.this);
            return;
        }
        if (!Constants.validateEmail(email_str)) {
            Constants.showMessage("Please enter valid email address", BookAppointment_Detail.this);
            return;
        }
        if (!contact_no_str.equals("")) {
            if (contact_no_str.length() < 10) {
                Constants.showMessage("Invalid mobile number", BookAppointment_Detail.this);
                return;
            }
        }
        if (gendet_str.equals("")) {
            Constants.showMessage("Please Select the Gender", BookAppointment_Detail.this);
            return;
        }
        if (birthdate_str.equals("")) {
            Constants.showMessage("Please Select the Birth date", BookAppointment_Detail.this);
            return;
        }

        try {
            if (Constants.isInternetOn(getApplicationContext()) == true) {
                callappointmentService();
            } else {
                Constants.showMessage("No Connection available", BookAppointment_Detail.this);
            }
        } catch (Exception exception) {
            Constants.showMessage("An errors in change password" + exception.toString(), BookAppointment_Detail.this);
        }

    }

    //    http://192.168.0.157:2171/api/users/appointment?access_token=68778dfsfsdf4353
//    request POST
//
//    {
//        "listing_id": "dsfs444dfs4d4d",
//            "appointment_date": "date object",
//            "appointment_time": "12:00 PM",
//            "appointment_reason": "Cough && Dengu",
//            "doctor_id": "d4f7d8f8sdf7dsf8",
//            "is_new_customer": "true/false",
//            "patient_info": [
//
//        first_name: {type: String},
//        last_name: {type: String},
//        email: {type: String},
//        dob: {type: String},
//        gender: {type: String, enum: ['male', 'female', 'other']},
//        address: {type: String},
//        phone_number: {type: String},
//        ]
//    }
    //

    // call api for the book appoinment
    void callappointmentService()
    {
        try {
            request_json = new JSONObject();

            request_json.put("appointment_date", appointment_date+"T00:00:00.000Z");
            request_json.put("appointment_time", str_selected_time);
            request_json.put("appointment_reason", str_reason);
            request_json.put("doctor_id", doctor_id);
            request_json.put("is_new_customer ", "true");
            request_json.put("listing_id", bcategory.get_id());
            request_json.put("insurance", insurance_str);
            if(!getIntent().getExtras().getString("reschedule").equals("")) {
                request_json.put("reffrence_reschedule", getIntent().getExtras().getString("reschedule"));
                request_json.put("rescheduled_by", "patient");
            }
            System.out.println("refrence  "+getIntent().getExtras().getString("reschedule"));
            JSONObject patiant_info=new JSONObject();
            patiant_info.put("first_name", first_name_str);
            patiant_info.put("last_name", last_name_str);
            patiant_info.put("email", email_str);
            patiant_info.put("Contact_no", contact_no_str);
            patiant_info.put("gender",gendet_str);
            patiant_info.put("dob", birthdate_str);
            patiant_info.put("address", "");
            request_json.put("patient_info",patiant_info);

            Log.v("TAG", "request parameter >>>" + request_json.toString());

            if (Constants.isInternetOn(getApplicationContext()) == true) {
                request = new VolleyRequest(this, this);
                request.makeRequest(Request.Method.POST, URL.BOOKAPPOINTMENT.getUrl() + "access_token=" + sharedpreferences.getString(Constants.ACCESS_TOKEN, ""), request_json, null, "", "BOOK", true);

            } else {
                Constants.showMessage("No Connection available", BookAppointment_Detail.this);
            }
        } catch (Exception exception) {

        }
    }

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

}


