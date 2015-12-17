package com.baya.adapter;

/**
 * Created by yatim on 11/18/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baya.BeanClass.Getbuisness;
import com.baya.Helper.Constants;
import com.baya.Helper.FlowLayout;
import com.baya.R;
import com.baya.SelectDoctor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MyExpandableAdapter extends BaseExpandableListAdapter {
    private List<Integer> _group;
    private Context mcontext;
    private List<String> _listDataHeader ; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private static final int TEST_ELEMENTS_COUNT = 16;
    String str ="text";
    String select="";
    String rescedule_id="";
    ArrayList<String> timeslot,morning,afternoon, evening,night;
    Getbuisness business=null;
    Date date=null;
    public MyExpandableAdapter(Context context, List<String> _listDataHeader,
                               HashMap<String, List<String>> listChildData, List<Integer> group, ArrayList<String> timeslot, ArrayList<String> morning, ArrayList<String> afternoon, ArrayList<String> evening, ArrayList<String> night,Getbuisness business,Date date,String rescedule) {
        this.mcontext = context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = listChildData;
        this._group = group;
        this.timeslot=timeslot;
        this.morning=morning;
        this.afternoon=afternoon;
        this.evening=evening;
        this.night=night;
        this.business=business;
        this.date=date;
        this.rescedule_id=rescedule;
     }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
     @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


         final String childText = (String) getChild(groupPosition,
                 childPosition);
         System.out.println("vslue of String"+childText);

         if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mcontext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

          FlowLayout  flow = (FlowLayout)convertView.findViewById(R.id.test_flow_layout);
         ArrayList<String> addtime_array=new ArrayList<String>();
         addtime_array.addAll(timeslot);
         try
         {
            flow.removeAllViews();
         }
         catch (Exception e)
         {

         }
         if(groupPosition==0) {

             for (int i = 0; i < morning.size(); i++) {
                 System.out.println("add view" + i);
                 LinearLayout lt=new LinearLayout(mcontext);
                 LinearLayout.LayoutParams pa=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                 lt.setLayoutParams(pa);
                 pa.setMargins(5,5,5,5);lt.setPadding(5, 5, 5, 5);
                 TextView btn = new TextView(mcontext);
                 btn.setBackgroundResource(R.drawable.ractangleedittext);
                 if(select.equals(morning.get(i)))
                 {
                     btn.setBackgroundResource(R.drawable.reactangleblue);
                     btn.setTextColor(Color.parseColor("#ffffff"));
                 }
                 else
                 {
                     btn.setBackgroundResource(R.drawable.ractangleedittext);
                     btn.setTextColor(Color.parseColor("#000000"));
                 }
                 btn.setPadding(5, 5, 5, 5);
                 btn.setText(morning.get(i));
                 btn.setTag(morning.get(i));
                 btn.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         select=v.getTag().toString();
                         notifyDataSetChanged();

                         Intent i = new Intent(mcontext, SelectDoctor.class);
                         i.putExtra("buisnesslist", business);
                         i.putExtra("calender", date);
                         i.putExtra("time", v.getTag() + "");
                         i.putExtra("reschedule",rescedule_id);
                         ((Activity) mcontext).startActivityForResult(i, 1234);
                     }
                 });
                 lt.addView(btn);
                 flow.addView(lt);
             }

         }
         else if(groupPosition==1)
         {
             for (int i = 0; i < afternoon.size(); i++) {
                 System.out.println("add view" + i);
                 LinearLayout lt=new LinearLayout(mcontext);
                 LinearLayout.LayoutParams pa=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);lt.setLayoutParams(pa);
                pa.setMargins(5, 5, 5, 5);  lt.setPadding(5,5,5,5);
                 TextView btn = new TextView(mcontext);
                 btn.setBackgroundResource(R.drawable.ractangleedittext);
                 btn.setPadding(5, 5, 5, 5);
                 if(select.equals(afternoon.get(i)))
                 {
                     btn.setBackgroundResource(R.drawable.reactangleblue);
                     btn.setTextColor(Color.parseColor("#ffffff"));
                 }
                 else
                 {
                     btn.setBackgroundResource(R.drawable.ractangleedittext);
                     btn.setTextColor(Color.parseColor("#000000"));
                 }
                 btn.setText(afternoon.get(i));
                 btn.setTag(afternoon.get(i));
                 btn.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         select=v.getTag().toString();
                         notifyDataSetChanged();

                         Intent i=new Intent(mcontext, SelectDoctor.class);
                         i.putExtra("buisnesslist",business);
                         i.putExtra("calender", date);
                         i.putExtra("time", v.getTag() + "");
                         i.putExtra("reschedule",rescedule_id);
                         ((Activity) mcontext).startActivityForResult(i, 1234);
                     }
                 });

                 lt.addView(btn);
                 flow.addView(lt);
             }
         }
         else if(groupPosition==2)
         {
             for (int i = 0; i < evening.size(); i++) {
                 LinearLayout lt=new LinearLayout(mcontext);
                 LinearLayout.LayoutParams pa=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                 lt.setLayoutParams(pa);
                 pa.setMargins(5,5,5,5);lt.setPadding(5,5,5,5);
                 System.out.println("add view" + i);

                 TextView btn = new TextView(mcontext);
                 if(evening.equals(afternoon.get(i)))
                 {
                     btn.setBackgroundResource(R.drawable.reactangleblue);
                     btn.setTextColor(Color.parseColor("#ffffff"));
                 }
                 else
                 {
                     btn.setBackgroundResource(R.drawable.ractangleedittext);
                     btn.setTextColor(Color.parseColor("#000000"));
                 }
                 btn.setBackgroundResource(R.drawable.ractangleedittext);
                 btn.setPadding(5, 5, 5, 5);
                 btn.setText(evening.get(i));
                 btn.setTag(evening.get(i));
                 btn.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         select=v.getTag().toString();
                         notifyDataSetChanged();

                         Intent i=new Intent(mcontext, SelectDoctor.class);
                         i.putExtra("buisnesslist",business);
                         i.putExtra("calender", date);
                         i.putExtra("time", v.getTag() + "");
                         i.putExtra("reschedule",rescedule_id);
                         ((Activity) mcontext).startActivityForResult(i, 1234);
                     }
                 });
                 lt.addView(btn);
                 flow.addView(lt);
             }
         }
         else if(groupPosition==3)
         {
             for (int i = 0; i < night.size(); i++) {
                 LinearLayout lt=new LinearLayout(mcontext);
                 LinearLayout.LayoutParams pa=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                 lt.setLayoutParams(pa);
                 pa.setMargins(5,5,5,5);lt.setPadding(5,5,5,5);
                 System.out.println("add view" + i);
                 TextView btn = new TextView(mcontext);
                 if(night.equals(afternoon.get(i)))
                 {
                     btn.setBackgroundResource(R.drawable.reactangleblue);
                     btn.setTextColor(Color.parseColor("#ffffff"));
                 }
                 else
                 {
                     btn.setTextColor(Color.parseColor("#000000"));
                     btn.setBackgroundResource(R.drawable.ractangleedittext);
                 }
                 btn.setBackgroundResource(R.drawable.ractangleedittext);
                 btn.setPadding(5, 5, 5, 5);
                 btn.setText(night.get(i));
                 btn.setTag(night.get(i));
                 btn.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         select=v.getTag().toString();
                         notifyDataSetChanged();

                         Intent i=new Intent(mcontext, SelectDoctor.class);
                         i.putExtra("buisnesslist",business);
                         i.putExtra("calender", date);
                         i.putExtra("time", v.getTag() + "");
                         i.putExtra("reschedule",rescedule_id);
                         ((Activity) mcontext).startActivityForResult(i, 1234);
                     }
                 });
                 lt.addView(btn);
                 flow.addView(lt);
             }
         }
         return convertView;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {



        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mcontext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.night);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.images);
        int imageId = this._group.get(groupPosition);
        imageView.setImageResource(imageId);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

