package com.baya.Helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baya.ApiRequest.RequestCompleteListener;
import com.baya.ApiRequest.VolleyRequest;
import com.baya.BeanClass.AppointmentDateCount;
import com.baya.R;
import com.baya.WebServices.AppController;
import com.baya.WebServices.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class CalendarAdapter extends BaseAdapter  {
	private static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY;
    private final Calendar calendar;
    private final CalendarItem today;

    public static CalendarItem selected = null;
    private final LayoutInflater inflater;
    public static CalendarItem[] days;
    Context context;
	private SharedPreferences preferences;
	Drawable img=null;
	String current_date="";
ArrayList<AppointmentDateCount> datearray=new ArrayList<AppointmentDateCount>();
	public CalendarAdapter(Context context, Calendar monthCalendar, String current_date) {

		this.context= context;
this.current_date=current_date;
    	calendar = monthCalendar;
    	today = new CalendarItem(monthCalendar,"0");
    	selected = new CalendarItem(monthCalendar,"0");
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		callWebServiceCount();
    }

    @Override
	public int getCount() {
        return days.length;
    }

    @Override
	public Object getItem(int position) {
        return days[position];
    }

    @Override
	public long getItemId(int position) {
    	final CalendarItem item = days[position];
    	if (item != null) {
    		return days[position].id;
    	}
    	return -1;
    }

    @Override
	public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.calendar_date, null);
        }
		TextView date2=(TextView)view.findViewById(R.id.date2);
		TextView datetwodot=(TextView)view.findViewById(R.id.datetwodot);
		TextView datethreedot=(TextView)view.findViewById(R.id.datethreedot);

        final TextView dayView = (TextView)view.findViewById(R.id.date);
        final CalendarItem currentItem = days[position];

        if (currentItem == null) {
        	dayView.setClickable(false);
        	dayView.setFocusable(false);
			dayView.setBackgroundDrawable(null);
        	dayView.setText(null);
			date2.setVisibility(View.GONE);
			datetwodot.setVisibility(View.GONE);
			datethreedot.setVisibility(View.GONE);
			dayView.setVisibility(View.VISIBLE);
        } else {
        	if (currentItem.equals(today)) {
				date2.setVisibility(View.GONE);
				datetwodot.setVisibility(View.GONE);
				datethreedot.setVisibility(View.GONE);
				dayView.setVisibility(View.VISIBLE);
				dayView.setBackgroundResource(R.drawable.today_background);
                dayView.setTextColor(Color.parseColor("#373737"));

            } else {
                if (currentItem.equals(selected)) {
					date2.setVisibility(View.GONE);
					datetwodot.setVisibility(View.GONE);
					datethreedot.setVisibility(View.GONE);
					dayView.setVisibility(View.VISIBLE);
					date2.setBackgroundResource(R.drawable.selected_background);
					date2.setTextColor(Color.parseColor("#FFFFFF"));
					datetwodot.setBackgroundResource(R.drawable.selected_background);
					datetwodot.setTextColor(Color.parseColor("#FFFFFF"));
					datethreedot.setBackgroundResource(R.drawable.selected_background);
					datethreedot.setTextColor(Color.parseColor("#FFFFFF"));
					dayView.setBackgroundResource(R.drawable.selected_background);
                    dayView.setTextColor(Color.parseColor("#FFFFFF"));

                 } else {
					date2.setVisibility(View.GONE);
					datetwodot.setVisibility(View.GONE);
					datethreedot.setVisibility(View.GONE);
					dayView.setVisibility(View.VISIBLE);
					date2.setBackgroundResource(R.drawable.normal_background);
					date2.setTextColor(Color.parseColor("#FFFFFF"));
					datetwodot.setBackgroundResource(R.drawable.normal_background);
					datetwodot.setTextColor(Color.parseColor("#FFFFFF"));
					datethreedot.setBackgroundResource(R.drawable.normal_background);
					datethreedot.setTextColor(Color.parseColor("#FFFFFF"));
					dayView.setBackgroundResource(R.drawable.normal_background);
					dayView.setTextColor(Color.parseColor("#FFFFFF"));
                }
				System.out.println("current items  " + currentItem.count);
				img = context.getResources().getDrawable(R.drawable.dot);



            }
			if(currentItem.count.equals("0"))
			{
				dayView.setCompoundDrawables(null, null, null, null);
			}
			else
			{
				if(Integer.parseInt(currentItem.count)==2) {
					System.out.println("date2  "+currentItem.day);
					date2.setVisibility(View.GONE);
					dayView.setVisibility(View.GONE);
					datetwodot.setVisibility(View.VISIBLE);
					datethreedot.setVisibility(View.GONE);
					if (currentItem.equals(today))
					{
						datetwodot.setBackgroundResource(R.drawable.today_background);
						datetwodot.setTextColor(Color.parseColor("#373737"));
					}
				}
				else if(Integer.parseInt(currentItem.count)==1)
				{
					System.out.println("date1  "+currentItem.day);
					date2.setVisibility(View.VISIBLE);
					dayView.setVisibility(View.GONE);
					datetwodot.setVisibility(View.GONE);
					datethreedot.setVisibility(View.GONE);
					if (currentItem.equals(today))
					{
						date2.setBackgroundResource(R.drawable.today_background);
						date2.setTextColor(Color.parseColor("#373737"));
					}
				}
				else
				{
					System.out.println("date3more  "+currentItem.day);
					date2.setVisibility(View.GONE);
					dayView.setVisibility(View.GONE);
					datetwodot.setVisibility(View.GONE);
					datethreedot.setVisibility(View.VISIBLE);
					if (currentItem.equals(today))
					{
						datethreedot.setBackgroundResource(R.drawable.today_background);
						datethreedot.setTextColor(Color.parseColor("#373737"));
					}
				}
			}
            date2.setText(currentItem.text);
			datethreedot.setText(currentItem.text);
			datetwodot.setText(currentItem.text);
        	dayView.setText(currentItem.text);
        }

        return view;
    }

    public final void setSelected(int year, int month, int day) {
    	selected.year = year;
    	selected.month = month;
    	selected.day = day;
    	notifyDataSetChanged();
    }

    public final void refreshDays() {
    	final int year = calendar.get(Calendar.YEAR);
    	final int month = calendar.get(Calendar.MONTH);
    	final int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
    	final int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    	final int blankies;
    	final CalendarItem[] days;

    	if (firstDayOfMonth == FIRST_DAY_OF_WEEK) {
    		blankies = 0;
        } else if (firstDayOfMonth < FIRST_DAY_OF_WEEK) {
        	blankies = Calendar.SATURDAY - (FIRST_DAY_OF_WEEK - 1);
        } else {
        	blankies = firstDayOfMonth - FIRST_DAY_OF_WEEK;
        }
    	days = new CalendarItem[lastDayOfMonth + blankies];

        for (int day = 1, position = blankies; position < days.length; position++) {

              int count=0;
			for(int i=0;i<datearray.size();i++){
               int datess=day;
				String dayes=datess+"";
				String months=(month+1)+"";
				if(dayes.length()==1)
					dayes="0"+dayes;
				if(months.length()==1)
					months="0"+months;

				String dates=(year+"-"+months+"-"+dayes);
				System.out.println("checks111  " + dates + "   " + datearray.get(i).getDate().split("T")[0]);
				if(dates.equals(datearray.get(i).getDate().split("T")[0]))
				{
                    count=count+1;
					System.out.println("checks  " + dates + "   " + datearray.get(i).getDate().split("T")[0]);
					days[position] = new CalendarItem(year, month, day++,datearray.get(i).getCount());
				}

			}
if(count==0)
{
	System.out.println("outside  ");
	days[position] = new CalendarItem(year, month, day++,"0");
}


        }

        this.days = days;
        notifyDataSetChanged();

    }
	private void callWebServiceCount()
	{
		try {

			JSONObject dateobject = new JSONObject();
			dateobject.put("date", current_date);
			preferences = context.getSharedPreferences(Constants.BAYA, 0);
			System.out.println("date3iii " + dateobject+"  "+ URL.UPCOMINGCOUNT.getUrl() + "access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""));

			JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL.UPCOMINGCOUNT.getUrl() + "access_token=" + preferences.getString(Constants.ACCESS_TOKEN, ""),dateobject,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {

							try
							{
								System.out.println("aaaaaaa  "+response.toString());
								datearray=new ArrayList<AppointmentDateCount>();
								if (response.getString("success").equals("true"))
								{

									if(response.getJSONArray("data").length()!=0) {
										for (int i = 0; i < response.getJSONArray("data").length(); i++)
										{
											datearray.add(new AppointmentDateCount(response.getJSONArray("data").getJSONObject(i).getString("date"),response.getJSONArray("data").getJSONObject(i).getString("count")));
											System.out.println("sizee  " + response.getJSONArray("data").getJSONObject(i).getString("date"));
										}
										refreshDays();
										notifyDataSetChanged();
									}
									else
									{

									}
								}
								else
								{

								}
							}
							catch (Exception e)
							{

							}

						}
					}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {

					Log.v("TAG", "Error:>>> " + error.getMessage());


					Constants.web_service_status = false;
				}
			}) {

				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					HashMap<String, String> headers = new HashMap<String, String>();
//					headers.put("Content-Type", "application/json");
					return headers;
				}

			};

			AppController.getInstance().addToRequestQueue(objectRequest, "Forgot_Passsword");
		}catch (Exception e){}



	}



	private static class CalendarItem {
    	public int year;
    	public int month;
    	public int day;
    	public String text;
		String count="";
    	public long id;

    	public CalendarItem(Calendar calendar,String count) {
    		this(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),count);
    	}

    	public CalendarItem(int year, int month, int day,String count) {
			this.year = year;
			this.month = month;
			this.day = day;
            this.count=count;
			this.text = String.valueOf(day);
			this.id = Long.valueOf(year + "" + month + "" + day);
		}

    	@Override
    	public boolean equals(Object o) {
    		if (o != null && o instanceof CalendarItem) {
    			final CalendarItem item = (CalendarItem)o;
    			return item.year == year && item.month == month && item.day == day;
    		}
    		return false;
    	}
    }
}