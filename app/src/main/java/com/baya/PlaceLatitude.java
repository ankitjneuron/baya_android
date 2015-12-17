package com.baya;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baya.WebServices.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;




public class PlaceLatitude {

	static String result = "0.0";
	static String result1 = "0.0";
	static double lat = 0.0;
	static double lon = 0.0;
	static double value = 0;

	public static void getAddressFromLocation(final String locationAddress, final Context context, final Handler handler) {
		Thread thread = new Thread() {
			@Override
			public void run() {

				String uri;

				try {
					uri = "http://maps.google.com/maps/api/geocode/json?address=" + URLEncoder.encode(locationAddress, "utf-8")
							+ "&sensor=false";

					/** Volley request to get lat long of particular address */

					JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, uri, null, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {

							try {

								lon = ((JSONArray) response.get("results")).getJSONObject(0).getJSONObject("geometry")
										.getJSONObject("location").getDouble("lng");

								lat = ((JSONArray) response.get("results")).getJSONObject(0).getJSONObject("geometry")
										.getJSONObject("location").getDouble("lat");

								Log.d("latitude", lat + "");

								Log.d("longitude", lon + "");

							} catch (Exception e) {

								e.printStackTrace();
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							VolleyLog.d("Error", error.getMessage());

						}
					}) {

					};
					AppController.getInstance().addToRequestQueue(jsonObjReq, "PLACE_LATLONG");

				} catch (Exception e) {

					e.printStackTrace();
				}

				finally {

					Message message = Message.obtain();
					message.setTarget(handler);
					if (result1 != null) {
						message.what = 1;
						Bundle bundle = new Bundle();

						bundle.putString("address", locationAddress);
						bundle.putString("latitude", "" + lat);
						bundle.putString("longitudes", "" + lon);
						message.setData(bundle);
					} else {
						message.what = 1;
						Bundle bundle = new Bundle();
						result = "Address";
						bundle.putString("address", result);
						bundle.putString("latitude", "");
						bundle.putString("longitudes", "");
						message.setData(bundle);
					}
					message.sendToTarget();
				}
			}
		};
		thread.start();

	}
}