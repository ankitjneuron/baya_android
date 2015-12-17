package com.baya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baya.R;


/**
 * Created by yatim on 10/30/2015.
 */
public class Previous_date_history extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private final int[] Imageid;

    public Previous_date_history(Context c, String[] web, int[] Imageid) {
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

        convertView = inflater.inflate(R.layout.previoud_date_history_layout, null);
        TextView textView = (TextView) convertView.findViewById(R.id.grid_text);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_image);
        textView.setText(web[position]);
        imageView.setImageResource(Imageid[position]);

        return convertView;
    }
}
