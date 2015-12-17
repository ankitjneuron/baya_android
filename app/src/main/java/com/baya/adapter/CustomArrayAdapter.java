package com.baya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baya.R;


/** An array adapter that knows how to render views when given CustomData classes */
public class CustomArrayAdapter extends ArrayAdapter<String>


{
    LayoutInflater layoutInflater = null;
    TextView txtTitle= null;
    private final Context context;
    private final String[] web;
    private final Integer[] imageId;
    ImageView imageView=null;


    public CustomArrayAdapter(Context context,String[] web, Integer[] imageId)
    {
        super(context, R.layout.scrollview, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        layoutInflater = LayoutInflater.from(context);
        view= layoutInflater.inflate(R.layout.scrollview, parent, false);
        txtTitle = (TextView)view.findViewById(R.id.txt);
        imageView =(ImageView)view.findViewById(R.id.img);
        txtTitle.setText(web[position]);
        imageView.setImageResource(imageId[position]);
        return view;


    }

}