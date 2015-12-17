package com.baya.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baya.R;


public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private final int[] Imageid;

    public CustomGrid(Context c, String[] web, int[] Imageid) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.scrollview, null);
            TextView textView = (TextView) convertView.findViewById(R.id.grid_text);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_image);
            textView.setText(web[position]);
            imageView.setImageResource(Imageid[position]);
            imageView.setBackgroundResource(R.drawable.grid_circle_image_view);
            textView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "museo/MUSEO_SLAB_0.OTF"));
            convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              imageView.setBackgroundResource(R.drawable.blue_oval);

            }
        });
            return convertView;
        }
    }