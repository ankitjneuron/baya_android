package com.baya;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.baya.ApiRequest.RequestCompleteListener;
import com.baya.ApiRequest.VolleyRequest;
import com.baya.Helper.Constants;
import com.baya.WebServices.URL;
import com.google.gson.Gson;

import org.json.JSONObject;



public class ChangePassword extends Activity implements RequestCompleteListener<JSONObject> {
    private JSONObject request_json;
    TextView change_password_txt = null;
    TextView change_password_content_txt = null;
    EditText edt_new_password = null, edt_confirm_password = null, edt_old_password = null;

    String str_old_password, str_new_password = "", str_confirm_password = "";
    TextView change_password_content = null;
    SharedPreferences sharedpreferences = null;
    ImageButton back_buisnessp=null;

     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        initView();
        sharedpreferences = getSharedPreferences(Constants.BAYA, 0);
         back_buisnessp=(ImageButton)findViewById(R.id.back_buisnessp);
         back_buisnessp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });
        change_password_txt.setOnClickListener(nlisteners);
    }

    protected void initView() {
        change_password_txt = (TextView) findViewById(R.id.change_password_save_txt);
        edt_old_password = (EditText) findViewById(R.id.old_password_edt);
        edt_new_password = (EditText) findViewById(R.id.new_password);
        edt_confirm_password = (EditText) findViewById(R.id.confirm_password);
        change_password_content_txt = (TextView) findViewById(R.id.textview_change_password_content);
        change_password_content = (TextView) findViewById(R.id.content);

    }

    private void initdata() {

        edt_old_password.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        edt_new_password.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        edt_confirm_password.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        change_password_content_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/JUSTOLDFASHION_5.TTF"));
        TextView textviewtitles=(TextView)findViewById(R.id.textview_buisnessp_titles);
        textviewtitles.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        change_password_content.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));

    }

    private void request_change_password() {
        str_old_password = edt_old_password.getText().toString().trim();
        str_new_password = edt_new_password.getText().toString().trim();
        str_confirm_password = edt_confirm_password.getText().toString().trim();
        if (str_old_password.equals(""))
        {
            Constants.showMessage("Please enter old password", ChangePassword.this);
            return;
        }
        if (str_new_password.equals("")) {
            Constants.showMessage("Please enter new password", ChangePassword.this);
            return;
        }
        if (str_confirm_password.equals("")) {
            Constants.showMessage("Please enter confirm password", ChangePassword.this);
            return;
        }


        if (str_new_password.length() < 6) {

            Constants.showMessage("Password should contain at least 6 characters", ChangePassword.this);
            return;
        }
        if (!str_new_password.equals(str_confirm_password)) {
            Constants.showMessage("password you entered did not match", ChangePassword.this);
            return;
        }
        try {

            if (Constants.isInternetOn(getApplicationContext()) == true) {
//                Constants.showMessage("Workings...", SignUp.this);
                change_password_request();
            } else {
                Constants.showMessage("No Connection available", ChangePassword.this);

            }
        } catch (Exception exception) {
            Constants.showMessage("An errors in change password" + exception.toString(), ChangePassword.this);
        } finally {
            // Constants.showMessage("An errors", SignUp.this);
        }

    }
    @Override
    public void onTaskComplete(String tag, JSONObject response) {
        try {
            Gson gs = new Gson();
            if (response.getString("success").equals("true")) {
                Constants.showMessage("Password Updated Successfully...", ChangePassword.this);
                finish();
            } else {
                Constants.showMessage(response.getString("message"), ChangePassword.this);
            }
        } catch (Exception x) {

        }

    }

    protected void change_password_request() {
        JSONObject js = null;
        VolleyRequest vn = null;
        try {
            str_old_password = edt_old_password.getText().toString().trim();
            str_confirm_password = edt_confirm_password.getText().toString().trim();
            js = new JSONObject();
            js.put("old_password", str_old_password);
            js.put("new_password", str_new_password);

        } catch (Exception m) {
            Constants.showMessage("" + m.toString(), ChangePassword.this);
        }
        System.out.println("response" + js.toString());
        Log.e("Response ","Change password "+js.toString());
        vn = new VolleyRequest(ChangePassword.this, ChangePassword.this);
        vn.makeRequest(Request.Method.POST, URL.Change_Passwords.getUrl() + "?access_token=" + sharedpreferences.getString(Constants.ACCESS_TOKEN, ""), js, null, "", "ChangePassword", true);
    }

   View.OnClickListener nlisteners=new View.OnClickListener() {
       @Override
       public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_password_save_txt:
                request_change_password();
//                change_password_request();
        }
    }};
}
