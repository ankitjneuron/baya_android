package com.baya;
/*
* Profile Screen
* with edit profile work
*
* */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baya.ApiRequest.MultipartRequest;
import com.baya.ApiRequest.RequestCompleteListener;
import com.baya.ApiRequest.VolleyRequest;
import com.baya.Helper.Constants;
import com.baya.WebServices.AppController;
import com.baya.WebServices.URL;
import com.baya.switchbutton.SwitchButton;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class Profile extends Activity implements RequestCompleteListener<JSONObject>, View.OnClickListener
{
    private static final int DATE_PICKER_ID = 1111;
    ImageView edit_profile_image_button = null;
    SharedPreferences sharedpreferences = null;
    VolleyRequest vt;
    String imagepath_String="";
    JSONObject js = null;
    String user_image="",user_name = "", last_name = "", email = "", phone_no = "", birthday = "",gender="";
    String hasimage="false";
    Calendar calendar = null;
    int day;
    int month;
    int year;

    private SwitchButton switchButton;

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener()
    {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay)
        {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            // Show selected date
            String daystr=""+day,monthstr=""+(month+1);
            if(daystr.length()==1)
            {
                daystr="0"+day;
            }
            if(monthstr.length()==1)
            {
                monthstr="0"+month;
            }
            profile_birthday_txt.setText(new StringBuilder().append(daystr).append("-").append(monthstr)
                    .append("-").append(year));


        }
    };
    RelativeLayout birthday_layout = null;
    RelativeLayout updateprofile_layout = null;
    Bitmap bitmap = null;
    String url = "";
    File f = new File(Environment.getExternalStorageDirectory().toString());
    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "BAYA" + File.separator + "default";
    String[] filePath = {MediaStore.Images.Media.DATA};
    ImageLoader imageLoader = null;
    private String str_user_name = "", str_last_name = "", str_email = "", str_phone_number = "", str_birthday = "", access_token;
    private TextView change_password_txt = null, birthday_txt = null, gender_txt = null, user_name_txt = null, last_name_txt = null, email_id_txt = null, phone_number_txt = null, _txt = null;
    private TextView profile_gender_txt = null, profile_birthday_txt = null;
    private EditText profile_user_name_txt = null, profile_last_name_txt = null, profile_email_id_txt = null, profile_phone_number_txt = null;
    private ImageView user_profile_image = null;
    private MultipartRequest request;
    private HashMap<String, String> params;
    private DisplayImageOptions options;
    private ImageLoaderConfiguration config;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);
        initView();
        initdata();
        back=(ImageButton)findViewById(R.id.back_buisnessp);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sharedpreferences = getSharedPreferences(Constants.BAYA, 0);
        access_token = sharedpreferences.getString(Constants.ACCESS_TOKEN, "");
        params = new HashMap<String, String>();
        profile();


        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_profile).showImageForEmptyUri(R.drawable.default_profile)
                .showImageOnFail(R.drawable.default_profile).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(900)).build();

        change_password_txt.setOnClickListener(this);
        birthday_layout.setOnClickListener(this);
        edit_profile_image_button.setOnClickListener(this);
        user_profile_image.setOnClickListener(this);
        updateprofile_layout.setOnClickListener(this);
    }
// initialization of the view
    protected void initView()
    {
        change_password_txt = (TextView) findViewById(R.id.change_password_txt);
        user_name_txt = (TextView) findViewById(R.id.profile_user_name);
        last_name_txt = (TextView) findViewById(R.id.profile_last_name);
        email_id_txt = (TextView) findViewById(R.id.profile_email_address);
        phone_number_txt = (TextView) findViewById(R.id.profile_phone_number);
        gender_txt = (TextView) findViewById(R.id.profile_gender);
        birthday_txt = (TextView) findViewById(R.id.profile_birthday);
        birthday_layout = (RelativeLayout) findViewById(R.id.r_layoutbirths);
        user_profile_image = (ImageView) findViewById(R.id.profile_user_image);
        updateprofile_layout = (RelativeLayout) findViewById(R.id.update_profile);
        updateprofile_layout.setVisibility(View.GONE);

        edit_profile_image_button = (ImageView) findViewById(R.id.edit_profile);
        profile_user_name_txt = (EditText) findViewById(R.id.profile_user_name_txt);
        profile_last_name_txt = (EditText) findViewById(R.id.profile_last_name_txt);
        profile_email_id_txt = (EditText) findViewById(R.id.profile_email_address_txt);
        profile_phone_number_txt = (EditText) findViewById(R.id.profile_phone_number_txt);
        profile_birthday_txt = (TextView) findViewById(R.id.profile_birth_day_txt);
       // profile_phone_number_txt.addTextChangedListener(new FourDigitCardFormatWatcher());
        switchButton=(SwitchButton)findViewById(R.id.gender_switch);

        SpannableString content = new SpannableString("Change Password");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        change_password_txt.setText(content);


        profile_user_name_txt.setEnabled(false);
        profile_last_name_txt.setEnabled(false);
        profile_email_id_txt.setEnabled(false);
        profile_phone_number_txt.setEnabled(false);
        birthday_layout.setEnabled(false);
        user_profile_image.setEnabled(false);
    }
// initialization of the data
    private void initdata()
    {
        change_password_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        user_name_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        last_name_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        gender_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        phone_number_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        email_id_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        profile_phone_number_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        gender_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        birthday_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        phone_number_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        profile_user_name_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        profile_birthday_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        profile_email_id_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        profile_last_name_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));


        //set the switch to ON
        switchButton.setEnabled(false);
        //switchButton.setChecked(false);
        gender="male";
        //attach a listener to check for changes in state
        switchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked)
                {
                    gender="female";
                }
                else
                {
                    gender="male";
                }

            }
        });


    }
// intenet check
    private void request_profile()
    {
        try
        {
            if (Constants.isInternetOn(getApplicationContext()) == true)
            {

            }
            else
            {
                Constants.showMessage("No Connection available", Profile.this);
            }
        }
        catch (Exception exception)
        {
            Constants.showMessage("An errors in change password" + exception.toString(), Profile.this);
        }
        finally
        {
        }
    }


    // get profile request
    protected void profile()
    {
        vt = new VolleyRequest(Profile.this, Profile.this);
        vt.makeRequest(Request.Method.GET, URL.PROFILE.getUrl() + "access_token=" + sharedpreferences.getString(Constants.ACCESS_TOKEN, ""), null, null, "", "Profile", true);
    }


    // Request responce
    public void onTaskComplete(String tag, JSONObject response)
    {

//        "phone_number": "9464646464",
//            "status": "active",
//            "user_type": "patient",
//            "state": "MP",
//            "__v": 0,
//            "password": "$2a$10$2.PQE68adTKUTBbihl/Y5.a57grUziNyZpRmqR1QF6tLLSqj8QY6G",
//            "country": "US",
//            "city": "Indore",
//            "first_name": "jordan",
//            "updated_at": "2015-11-24T06:09:50.110Z",
//            "is_receive_email": false,
//            "_id": "564eb58038cb51b812086133",
//            "email": "jordan@test.com",
//            "address": "lig",
//            "last_name": "test",
//            "created_at": "2015-11-20T05:54:08.098Z",
//            "is_approved": false,
//            "verify_token": "h!d2fLIDOTfcfJ5f",
//            "access_token": "1448345390107_gehLnXKxrCgJzs5W"
        try
        {
            Gson gs = new Gson();
            if (response.getString("success").equals("true"))
            {

                Log.v("ShowView","Response of Profile :"+response.toString());

                JSONObject js = response.getJSONObject("data");

                if(js.has("facebook_id"))
                {
                    change_password_txt.setVisibility(View.GONE);
                }
                user_name = js.getString("first_name");
                last_name = js.getString("last_name");
                email = js.getString("email");

                if (js.has("dob"))
                {
                    phone_no = js.getString("phone_number");

                }
                else
                {
                    phone_no="";

                }
                if (js.has("dob"))
                {
                    birthday = js.getString("dob");
                    profile_birthday_txt.setText(birthday);
                }
                else
                {
                    profile_birthday_txt.setText("Select Birthdate");
                }
                if(js.has("gender"))
                {
                    gender = js.getString("gender");
//                    gender_txt.setText(gender);
                    if(gender.equals("female"))
                    {
                      switchButton.setChecked(true);
                    }
                    else
                    {
                        switchButton.setChecked(false);
                    }
                }
                if(js.has("image"))
                {
                    imagepath_String = response.getString("basePath")+js.getString("image");

                   imageLoader.displayImage(imagepath_String,user_profile_image,options);
                }

                profile_user_name_txt.setText(user_name);
                profile_last_name_txt.setText(last_name);
                profile_email_id_txt.setText(email);
                profile_phone_number_txt.setText(phone_no);
            } else {
                Constants.showMessage(response.getString("message"), Profile.this);
            }
        }
        catch (JSONException x)
        {
            Constants.showMessage("erro r"+x.getMessage(),Profile.this );
        }
    }

    private void profile_edit()
    {

        user_profile_image.setEnabled(true);
        profile_user_name_txt.setEnabled(true);
        profile_last_name_txt.setEnabled(true);
        profile_phone_number_txt.setEnabled(true);
      // profile_phone_number_txt.addTextChangedListener(new FourDigitCardFormatWatcher());
        birthday_layout.setEnabled(true);
        user_profile_image.setEnabled(true);
        updateprofile_layout.setVisibility(View.VISIBLE);
        change_password_txt.setVisibility(View.GONE);
        switchButton.setEnabled(true);


//        str_user_name = profile_user_name_txt.getText().toString();
//
//
//        if (str_user_name.equals(""))
//        {
//            Constants.showMessage("Please enter first name", Profile.this);
//            return;
//        }
    }


    // update request profile
    private void request_update_profile()
    {
        str_user_name = profile_user_name_txt.getText().toString();
        str_last_name = profile_last_name_txt.getText().toString();
        str_phone_number = profile_phone_number_txt.getText().toString();
        str_birthday = profile_birthday_txt.getText().toString();
        SharedPreferences preferences = getSharedPreferences(Constants.BAYA, 0);

        //js.put("image", user_profile_image);
        //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_launcher);


        Log.v("ShowView", "Gender : " + str_phone_number.length());


        System.out.println("Response" + params.toString());


        if (str_user_name.equals(""))
        {
            Constants.showMessage("Please enter first name", Profile.this);
            return;
        }
        if (str_last_name.equals(""))
        {
            Constants.showMessage("Please enter last name", Profile.this);
            return;
        }

        if (str_phone_number.equals(""))
        {
            Constants.showMessage("Please enter phone number", Profile.this);
            return;
        }
        if (str_phone_number.length()< 10)
        {
            Constants.showMessage("Enter valid phone number", Profile.this);
            return;
        }

        if (str_birthday.equals("") || str_birthday.equals("dd-mm-yyyy"))
        {
            Constants.showMessage("Please Select your Birth Date", Profile.this);
            return;
        }

        if (imagepath_String==null&&imagepath_String.equals(""))
        {
            Constants.showMessage("Please Select the Image", Profile.this);
        }
        try
        {
            System.out.println("bitmap  "+bitmap);
           if(bitmap==null) {
               bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
               hasimage="false";
           }

            if (Constants.isInternetOn(getApplicationContext()) == true)
            {
                profile_edit();
                url = URL.PROFILEUPDATE.getUrl();
                params.put("first_name", str_user_name);
                params.put("last_name", str_last_name);
                params.put("gender", gender);
                params.put("phone_number", str_phone_number);

                params.put("lattitude", "22.739389");
                params.put("longtitude", "75.8849835");
                params.put("dob", str_birthday);
                Log.d("strurer  ", str_user_name);
                request = new MultipartRequest(url + "access_token=" + access_token, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {

                    }
                }, new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String s)
                    {

                        System.out.println("check on Response" + s);
                        JSONObject response = null;
                        try
                        {
                            response = new JSONObject(s);
                            System.out.println("Success      " + response.toString());
                            Log.v("ShowView","Profile Success : "+response.toString());

                            if (response.getString("success").equals("true"))
                            {
                                if(hasimage.equals("true")) {
                              //      imagepath_String = response.getString("basePath") + response.getJSONObject("data").getString("image");

                              //      imageLoader.displayImage(imagepath_String, user_profile_image, options);

                                }
                                    Constants.showMessage("Profile Updated  Successfully...", Profile.this);
                                SharedPreferences.Editor e = sharedpreferences.edit();

                                if(response.getJSONObject("data").has("image"))
                                e.putString("profileimage",response.getJSONObject("data").getString("image"));
                                e.putString("basePath",response.getString("basePath"));
                                e.putString("first_name",response.getJSONObject("data").getString("first_name"));
                                e.putString("last_name", response.getJSONObject("data").getString("last_name"));
                                if(response.getJSONObject("data").has("image"))
                                imageLoader.displayImage(response.getString("basePath") + response.getJSONObject("data").getString("image"), SampleListFragment.user_image, options);
                                e.commit();
                            }
                            else
                            {
                                 JSONArray array = response.getJSONArray("errors");
                                 Constants.showMessage(array.getJSONObject(0).getString("message"), Profile.this);
                             }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, bitmap, params, "",hasimage);
                request.setRetryPolicy(new DefaultRetryPolicy(60000, 1, 1f));
                AppController.getInstance().addToRequestQueue(request, "request tag");

            }
            else
            {
                Constants.showMessage("No Connection available", Profile.this);
            }
        }
        catch (Exception exception)
        {
            Constants.showMessage("An errors " + exception.toString(), Profile.this);
        }


    }

    public void date_picker()
    {

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        showDialog(DATE_PICKER_ID);


    }

    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case DATE_PICKER_ID:
               // return new DatePickerDialog(this, pickerListener, year, month, day);
                try {
                    DatePickerDialog dpDialog = new DatePickerDialog(this, pickerListener, year, month, day);
                    dpDialog.getDatePicker().setMaxDate(new Date().getTime());
                    DatePicker datePicker = dpDialog.getDatePicker();

                    Calendar calendar = Calendar.getInstance();//get the current day
                //set the current day as the max date
                    return dpDialog;
                }
                catch (Exception e)
                {

                }
        }
        return null;
    }

    private void showDate(int year, int month, int day)
    {
        String daystr=""+day,monthstr=""+month;
        if(daystr.length()==1)
        {
            daystr="0"+day;
        }
        if(monthstr.length()==1)
        {
monthstr="0"+month;
        }
        profile_birthday_txt.setText(new StringBuilder().append(daystr).append("/")
                .append(monthstr).append("/").append(year));
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.change_password_txt:
                Intent intent = new Intent(Profile.this, ChangePassword.class);
                startActivity(intent);
                break;
            case R.id.edit_profile:
                profile_edit();
                break;
            case R.id.r_layoutbirths:
                date_picker();
                break;
            case R.id.profile_user_image:
                profile_image();
                break;
            case R.id.update_profile:
                request_update_profile();
        }
    }


    // alert for the profile image
    private void profile_image()
    {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int item)
            {

                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                for (File temp : f.listFiles())
                {
                    if (temp.getName().equals("temp.jpg"))
                    {
                        f = temp;
                        File photo = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                        break;
                    }
                }
                try
                {

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    //user_profile_image.setImageBitmap(bitmap);
                    user_profile_image.setImageBitmap(getRoundedShape(bitmap, 80, 80));

                    f.delete();
                    OutputStream outFile = null;

                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    imagepath_String=file.getPath();

                    try
                    {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outFile);
                        outFile.flush();
                        outFile.close();
                        if(bitmap!=null) {

                            hasimage="true";
                        }
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == 2)
            {
                Uri selectedImage = data.getData();
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                imagepath_String=picturePath;

                c.close();
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                bitmap = (BitmapFactory.decodeFile(picturePath));
                if(bitmap!=null) {

                    hasimage="true";
                }
                //user_profile_image.setImageBitmap(bitmap);

                user_profile_image.setImageBitmap(getRoundedShape(bitmap, 80, 80));
            }
        }
    }


    public Bitmap getRoundedShape(Bitmap scaleBitmapImage, int width, int height)
    {
        int targetWidth = 110;
        int targetHeight = 110;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2, ((float) targetHeight - 1) / 2, (Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()), new Rect(0, 0, targetWidth, targetHeight),
                new Paint(Paint.FILTER_BITMAP_FLAG));
        return targetBitmap;
    }


}
