package com.baya;
/*
* created by Neeraj
* Loging class
*
* */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.baya.ApiRequest.RequestCompleteListener;
import com.baya.ApiRequest.VolleyRequest;
import com.baya.Helper.Constants;
import com.baya.WebServices.URL;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;




public class Login extends Activity implements RequestCompleteListener<JSONObject> {
//>
Dialog dialog;
    Button Login;
    TextView signUp,facebook,or,textview_dont;
    Button forgot_button;
    EditText username,password;
    String str_emails,str_passwords;	private PendingAction pendingAction = PendingAction.NONE;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private final String PENDING_ACTION_BUNDLE_KEY = "com.example.hellofacebook:PendingAction";
    private static final int RC_SIGN_IN = 0;
    private String fb_id="";
    String fb_email="";
    String fb_first_name="";
    String fb_last_name="";
    private String regisId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        GCMRegistrar.register(Login.this, Constants.SENDER_ID);
        initview();
        initdata();
        Logininitializes();
        
        
        forgot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcustomdialog();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();

            }
        });
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo("com.baya", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    facebook.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Constants.isInternetOn(Login.this)) {
                try {
                    LoginManager.getInstance().logOut();
                } catch (Exception e) {

                }
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "email"));
            } else {
                Constants.showMessage("Please Connect to Internet", Login.this);
            }
        }
    });

//        facebook.setTypeface(Typeface.createFromAsset(getAssets(),"/proximanova/JUSTOLDFASHION_5.TTF"));
    }
    String fn;
    private void Logininitializes() {FacebookSdk.sdkInitialize(getApplicationContext());callbackManager=CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {

            @Override
            public void onSuccess(LoginResult loginResult)
            {
                handlePendingAction();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {

                        Log.i("LoginActivity", response.toString());
                        try
                        {

                            fb_email = object.getString("email");
                            fb_id = object.getString("id");
                            fb_first_name = object.getString("name");
                            fn = fb_first_name.split(" ")[0];
                            fb_last_name = object.getString("email");
                            fb_last_name = fb_first_name.split(" ")[1];
                            fb_first_name = fn;
//                          profilepic= ""+profile.getProfilePictureUri(530,530);
                            System.out.println("==========" + object.toString());

                            // gender =
                            // object.getString("gender");
                            // birthday =
                            // object.getString("birthday");
                            updateUI();
                        }
                        catch (JSONException e)
                        {System.out.println("ty        " + e.toString());
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel()
            {
                if (pendingAction != PendingAction.NONE)
                {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                } System.out.println("ty" + "response             ");
                // updateUI();
            }

            @Override
            public void onError(FacebookException exception)
            {
                if (pendingAction != PendingAction.NONE && exception instanceof FacebookAuthorizationException)
                {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                } System.out.println("ty" + "response");
                // updateUI();
            }

            private void showAlert()
            {
                new AlertDialog.Builder(Login.this).setTitle(R.string.cancelled).setMessage(R.string.permission_not_granted)
                        .setPositiveButton(R.string.ok, null).show();
            }
        });

        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, shareCallback);
    }

    String profilepic;


    // view initilization
    private void initview() {

        username =(EditText)findViewById(R.id.edittext_login_user);
        password =(EditText)findViewById(R.id.edittext_login_passwords);
        facebook=(TextView)findViewById(R.id.textview_facebook);
        or=(TextView)findViewById(R.id.textview);
        textview_dont=(TextView)findViewById(R.id.textview_login_text);

        username.setBackgroundColor(Color.TRANSPARENT);
        password.setBackgroundColor(Color.TRANSPARENT);
        forgot_button =(Button)findViewById(R.id.ForgotButton);
        signUp =(TextView)findViewById(R.id.SingupButton);
        Login = (Button)findViewById(R.id.loginbutton);
    }

  //  set fonts
    private void initdata() {
        username.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        password.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        signUp.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        Login.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        facebook.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        or.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        textview_dont.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
    }
// show custom dialog
    protected  void showcustomdialog()
    {

        dialog = new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_forgot_dailog);
        dialog.show();

        TextView textview_header=(TextView)dialog.findViewById(R.id.forget_text);
        TextView textview_message=(TextView)dialog.findViewById(R.id.message);
        final EditText edittext=(EditText)dialog.findViewById(R.id.edittext_log_forgotpassword);
        Button cancel=(Button)dialog.findViewById(R.id.Cancel);
        Button request=(Button)dialog.findViewById(R.id.Request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edittext.getText().toString().equals("")) {
                    Constants.showMessage("Enter Email", Login.this);
                } else if (!Constants.validateEmail(edittext.getText().toString())) {
                    Constants.showMessage("Enter valid Email", Login.this);
                } else {
                    try {
                        JSONObject req = new JSONObject();

                        req.put("email", edittext.getText().toString());
                        req.put("user_type", "patient");
                        VolleyRequest volley = new VolleyRequest(Login.this, Login.this);

                        volley.makeRequest(Request.Method.POST, URL.FORGOTPASSWORD.getUrl(), req, null, "", "FORGOT", true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        textview_header.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        textview_message.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        edittext.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        cancel.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));
        request.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_0.OTF"));

    }

    // simple login request
    void Login()
    {
        try
       {
        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
       }
       catch (Exception e)
       {

       }
        str_emails=username.getText().toString();
        str_passwords=password.getText().toString();
        JSONObject js=new JSONObject();
        try
        {
            GCMRegistrar.checkDevice(Login.this);
            GCMRegistrar.checkManifest(Login.this);
            regisId = GCMRegistrar.getRegistrationId(Login.this);
            js.put("email",str_emails);
            js.put("password",str_passwords);
            js.put("device_type","android");
            js.put("device_id",regisId);
            js.put("certification_t","");
        }
        catch(Exception exception)
        {
            System.out.println("exception  "+regisId);
        }
        System.out.println("js  "+js  );
        if (Constants.isInternetOn(Login.this)) {
            VolleyRequest volley=new VolleyRequest(Login.this, Login.this);
            volley.makeRequest(Request.Method.POST, URL.LOGIN.getUrl(), js, null, "", "LOGIN", true);
        } else {
            Constants.showMessage("Please Connect to Internet", Login.this);
        }

    }
// responce of the login request
    @Override
    public void onTaskComplete(String tag, JSONObject response) {
      String success;
        try {
            Gson gs = new Gson();
            if (tag.equals("LOGIN")) {
                if (response.getString("success").equals("true")) {
                    SharedPreferences sharedpreferences = getSharedPreferences(Constants.BAYA, 0);
                    SharedPreferences.Editor e = sharedpreferences.edit();
                    e.putString(Constants.LOGIN, gs.toJson(response.getJSONObject("data")));
                    e.putString(Constants.ACCESS_TOKEN, response.getJSONObject("data").getString("access_token"));
                    if (response.getJSONObject("data").has("image")) {
                        e.putString("profileimage", response.getJSONObject("data").getString("image"));
                    }
                    e.putString("basePath", response.getString("basePath"));
                    e.putString("first_name", response.getJSONObject("data").getString("first_name"));
                    e.putString("last_name", response.getJSONObject("data").getString("last_name"));
                    e.putString(Constants.LOGIN, gs.toJson(response.getJSONObject("data")));
                    e.commit();

                    Intent intent = new Intent(Login.this, Landing_Screen.class);
                    intent.putExtra("position",0);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Constants.showMessage(response.getString("message"), Login.this);
                }
            } else if (tag.equals("FACEBOOKLOGIN")) {
                if (response.getString("success").equals("true")) {
                    SharedPreferences sharedpreferences = getSharedPreferences(Constants.BAYA, 0);

                    SharedPreferences.Editor e = sharedpreferences.edit();
                    e.putString(Constants.LOGIN, gs.toJson(response.getJSONObject("data")));
                    e.putString(Constants.ACCESS_TOKEN, response.getJSONObject("data").getString("access_token"));
                    if (response.getJSONObject("data").has("image")) {
                        e.putString("profileimage", response.getJSONObject("data").getString("image"));
                    }
                    e.putString("basePath", response.getString("basePath"));
                    e.putString("first_name", response.getJSONObject("data").getString("first_name"));
                    e.putString("last_name", response.getJSONObject("data").getString("last_name"));
                    e.putString(Constants.LOGIN, gs.toJson(response.getJSONObject("data")));
                    e.commit();
                    Intent intent = new Intent(Login.this, Landing_Screen.class);
                    intent.putExtra("position",0);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Constants.showMessage(response.getString("message"), Login.this);
                }
            } else {
                if (response.getString("success").equals("true")) {
dialog.dismiss();
                    Constants.showMessage("Please Check Your Email", Login.this);
                } else {
                    Constants.showMessage(response.getString("message"), Login.this);
                }
            }
        }
        catch(Exception exception)
        {
            System.out.println("yaa  "+exception.toString());
        }
    }

    private enum PendingAction
    {
        NONE
    }
    private void updateUI()
    {

        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
        Profile profile = Profile.getCurrentProfile();

        if (enableButtons && profile != null)
        {

            fb_id = profile.getId();
            fb_first_name = profile.getFirstName();
            fb_last_name = profile.getLastName();
            profilepic= ""+profile.getProfilePictureUri(530,530);

            System.out.println("ur          "+profilepic);
//if()// updateUI(false)
            loginfb();

        }
        else
        {

//            fb_id = "";
        }
    }
    private void handlePendingAction()
    {

        PendingAction previouslyPendingAction = pendingAction;
        pendingAction = PendingAction.NONE;
        switch (previouslyPendingAction)
        {
            case NONE:
                break;

        }
    }
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
//            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {

            Log.d("HelloFacebook", "Success!");

            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
//                showResult(title, alertMessage);
            }
        }

    };
        public void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        void validations()
   {
       str_emails=username.getText().toString();
       str_passwords=password.getText().toString();
       if(str_emails.equals(""))
       {
           Constants.showMessage("Enter Email", Login.this);
       }
       else if(!Constants.validateEmail(str_emails))
       {
           Constants.showMessage("Enter valid Email", Login.this);
       }
       else if(str_passwords.equals(""))
       {
           Constants.showMessage("Enter Password", Login.this);
       }
       else
       {
           Login();
       }
   }

    // facebook login request
    void loginfb()
    {
        JSONObject js=new JSONObject();
        try
        {
            GCMRegistrar.checkDevice(Login.this);
            GCMRegistrar.checkManifest(Login.this);
            regisId = GCMRegistrar.getRegistrationId(Login.this);

            js.put("facebook_id",fb_id);
            js.put("email",fb_email);
            js.put("first_name",fb_first_name);
            js.put("last_name",fb_last_name);
            js.put("username",fb_first_name+"" + fb_last_name);
            js.put("image",""+profilepic);
            js.put("phone_number", "");
            js.put("address", "lig");
            js.put("state", "MP");
            js.put("city", "Indore");
            js.put("device_type","android");
            js.put("device_id",regisId);
            js.put("certification_type","");
        }
        catch(Exception exception)
        {
            System.out.println("exception  "+regisId);
        }
        System.out.println("type" + js.toString());

        VolleyRequest tt=new VolleyRequest(Login.this,Login.this);
        tt.makeRequest(Request.Method.POST,URL.Facebooklogin.getUrl(),js,null,"","FACEBOOKLOGIN",true);
    }
    
}
