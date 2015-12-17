package com.baya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baya.R;


public class Current_Date_History extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private final int[] Imageid;

    public Current_Date_History(Context c, String[] web, int[] Imageid) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.current_date_history_layout, null);

        TextView business_name =(TextView)convertView.findViewById(R.id.business_name_txt);
        TextView visited_missed =(TextView)convertView.findViewById(R.id.visited_or_missed);
        TextView businessname =(TextView)convertView.findViewById(R.id.business2_name_txt);
        TextView time =(TextView)convertView.findViewById(R.id.time_txt);

        business_name.setText(web[position]);
        visited_missed.setText(web[position]);
        businessname.setText(web[position]);
        time.setText(web[position]);
        return convertView;
    }

}
