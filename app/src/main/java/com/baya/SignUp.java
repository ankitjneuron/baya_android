package com.baya;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.baya.ApiRequest.RequestCompleteListener;
import com.baya.ApiRequest.VolleyRequest;
import com.baya.Helper.Constants;
import com.baya.WebServices.URL;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;

import org.json.JSONObject;




public class SignUp extends Activity implements RequestCompleteListener<JSONObject> {

    TextView textview_signup_text;
    EditText edittext_name,edittext_lname,edittext_emailids,edittext_phone,edittext_password,edittext_confirm;

    private JSONObject request_json;
    private String first_name_str = "", last_name_str = "", email_str = "", user_name_str = "", password_str = "", confirm_password = "", mobile_no_str = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign__up);
        GCMRegistrar.register(SignUp.this, Constants.SENDER_ID);
        initView();
        setlisteners();
        initdata();

    }



    View.OnClickListener onlisteners=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        switch(v.getId())
       {
           case R.id.textview_signup_text:
               requestsignUp();
//               Intent intent=new Intent(SignUp.this,Landing_Screen.class);
//               startActivity(intent);
               break;
       }
        }
    };

    private void initdata() {
        edittext_name.setTypeface(Typeface.createFromAsset(getAssets(),"proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        edittext_lname.setTypeface(Typeface.createFromAsset(getAssets(),"proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        edittext_phone.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        edittext_password.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        edittext_confirm.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        edittext_emailids.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        textview_signup_text.setTypeface(Typeface.createFromAsset(getAssets(), "museo/JUSTOLDFASHION_5.TTF"));
    }

    private void initView() {
        edittext_name=(EditText)findViewById(R.id.edittext_sign_firstname);
        edittext_lname=(EditText)findViewById(R.id.edittext_lastname_su);
        edittext_phone=(EditText)findViewById(R.id.edittext_su_phone);
        edittext_password=(EditText)findViewById(R.id.edittext_su_password);
        edittext_confirm=(EditText)findViewById(R.id.edittext_su_confirm);
        edittext_emailids=(EditText)findViewById(R.id.edittext_su_email);
        textview_signup_text=(TextView)findViewById(R.id.textview_signup_text);
      //  edittext_phone.addTextChangedListener(new FourDigitCardFormatWatcher());
    }

    void setlisteners()
    {
        textview_signup_text.setOnClickListener(onlisteners);
    }

//    View.OnClickListener listner = new View.OnClickListener() {
//        @Override
//        public void onClick(View v)
//        {
//            switch (v.getId()) {
//                case R.id.signup_button:
//                    Intent intent= new Intent(SignUp.this,FindaDoctor.class);
//                    startActivity(intent);
//                    break;
//            }
//        }
//    };
    private void requestsignUp() {

        first_name_str = edittext_name.getText().toString().trim();
        last_name_str = edittext_lname.getText().toString().trim();
        email_str = edittext_emailids.getText().toString();
        password_str = edittext_password.getText().toString().trim();
        confirm_password = edittext_confirm.getText().toString().trim();
        mobile_no_str = edittext_phone.getText().toString();
        if (first_name_str.equals("")) {
            Constants.showMessage("Please enter first name", SignUp.this);
            return;
        }
        if (last_name_str.equals("")) {
            Constants.showMessage("Please enter last name", SignUp.this);
            return;
        }
        if (email_str.equals("")) {
            Constants.showMessage("Please enter email address", SignUp.this);
            return;
        }
        if (!Constants.validateEmail(email_str)) {
            Constants.showMessage("Please enter valid email address", SignUp.this);
            return;
        }
        if (mobile_no_str.equals("")) {
            Constants.showMessage("Please enter phone number", SignUp.this);
            return;
        }
//        if(!mobile_no_str.equals(""))
//        {
            if(mobile_no_str.length()<10)
            {
                Constants.showMessage("Enter valid phone number", SignUp.this);
                return;
            }

//        }
        if (password_str.equals("")) {
            Constants.showMessage("Please enter password", SignUp.this);
            return;

        }

        if (password_str.length() < 6) {

            Constants.showMessage("Password should contain at least 6 characters", SignUp.this);
            return;
        }
        if (confirm_password.equals("")) {
            Constants.showMessage("Please enter confirm password", SignUp.this);
            return;
        }
        if (!password_str.equals(confirm_password)) {
            Constants.showMessage("password you entered did not match", SignUp.this);
            return;
        }

        try {

            if (Constants.isInternetOn(getApplicationContext()) == true) {
//                Constants.showMessage("Workings...", SignUp.this);
                Signuprequest();
            } else {
                Constants.showMessage("No Connection available", SignUp.this);

            }
        } catch (Exception exception) {
            Constants.showMessage("An errors "+exception.toString(), SignUp.this);
        }

        finally {
           // Constants.showMessage("An errors", SignUp.this);
        }

}

    @Override
    public void onTaskComplete(String tag, JSONObject response) {
        try{
            Gson gs=new Gson();
            if(response.getString("success").equals("true"))
            {
                SharedPreferences sharedpreferences=getSharedPreferences(Constants.BAYA, 0);
                SharedPreferences.Editor e=sharedpreferences.edit();
                e.putString(Constants.SIGNUP, gs.toJson(response.getJSONObject("data")));
//                e.putString(Constants.ACCESS_TOKEN, response.getJSONObject("data").getString("access_token"));
                e.commit();
//
//               Intent intent=new Intent(SignUp.this,Login.class);
//               startActivity(intent);
//                finish();
                Constants.dialogsAlerts(SignUp.this);
            }
            else
            {
                Constants.showMessage(response.getString("message"), SignUp.this);
            }
        }
        catch(Exception x)
        {

        }

    }

    void Signuprequest()
    { JSONObject js=null;VolleyRequest vt=null;
        try {
            first_name_str = edittext_name.getText().toString();
            last_name_str = edittext_lname.getText().toString();
            email_str = edittext_emailids.getText().toString();
            password_str = edittext_password.getText().toString();
            confirm_password = edittext_confirm.getText().toString();
            mobile_no_str = edittext_phone.getText().toString();
            js = new JSONObject();

            js.put("user_type", "patient");
            js.put("first_name", first_name_str);
            js.put("last_name", last_name_str);
            js.put("email", email_str);
            js.put("password", password_str);
            js.put("phone_number", mobile_no_str);
            js.put("address", "lig");
            js.put("state", "MP");
            js.put("city", "Indore");
            js.put("device_type", "android");
            js.put("device_id", "tt");
            //js.put("dob","15/10/1993");
        }
        catch(Exception m)
        {
            Constants.showMessage(""+m.toString(), SignUp.this);
        }System.out.println("" + js);
            vt = new VolleyRequest(SignUp.this, SignUp.this);

           vt.makeRequest(Request.Method.POST, URL.SIGN_UP.getUrl(), js, null, "", "SIGNUP", true);

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
