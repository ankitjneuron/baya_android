package com.baya.ApiRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baya.Helper.Constants;
import com.baya.WebServices.AppController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class VolleyRequest {
    private ProgressDialog pDialog;
    private RequestCompleteListener<JSONObject> callback;
    HashMap<String, String> requestparams = null;
    boolean progress = true;
String responseString="";
    Context mcontext;
    public VolleyRequest(Context context, RequestCompleteListener<JSONObject> callback) {

        // this.context = context;
        mcontext=context;
        this.callback = callback;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    // TODO Auto-generated method stub

    public void makeRequest(int method, String URL, JSONObject json, final HashMap<String, String> params, final String access_token, final String tag, final boolean progress) {
        this.progress = progress;
        requestparams = params;
        if (progress == true) {
            showProgressDialog();
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(method, URL, json, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                Log.v("Response ", response.toString());

                callback.onTaskComplete(tag, response);
                if (progress == true) {
                    hideProgressDialog();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                VolleyLog.d("Error ", "Error: " + error.toString());
                error.printStackTrace();


                NetworkResponse localNetworkResponse = error.networkResponse;
                        if (localNetworkResponse != null) {

                } else {
                    Log.e("", error.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(error.getMessage());

                    }
                    catch (Exception e) { e.printStackTrace(); }
                }
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    try {
                        Log.v("" , ".getResponseError code :"+networkResponse.statusCode);
                        responseString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
                        Log.v("", ".getResponseError body :" + responseString);
                        JSONObject js=new JSONObject(responseString);
                        String success=js.getString("success");
                        if(success.equals("false"))
                        {

                            String message=js.getString("message");
                            Constants.dialogs("Alert",message,mcontext,"true");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (progress == true) {

                    hideProgressDialog();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO Auto-generated method stub

                return requestparams;
            }

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //	headers.put("Content-Type", "application/json");


                return headers;
            }

        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 1, 1f));
        AppController.getInstance().addToRequestQueue(objectRequest, "REQUEST");

    }
}
