package com.baya.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baya.R;


/**
 * Created by user on 23-Oct-15.
 */
public class CustomListAdapter extends ArrayAdapter<String> {
    LayoutInflater layoutInflater = null;
    TextView txtTitle = null;
    private final Context context;
    private final String[] web;
    private final Integer[] imageId;
    ImageView imageView = null;

    public CustomListAdapter(Context context, String[] web, Integer[] imageId) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;

        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.list_single, parent, false);
        txtTitle = (TextView) view.findViewById(R.id.txt);
        imageView = (ImageView) view.findViewById(R.id.img);
        txtTitle.setText(web[position]);
        txtTitle.setTypeface(Typeface.createFromAsset(context.getAssets(),"museo/MUSEO_SLAB_5.OTF"));

        imageView.setImageResource(imageId[position]);
        return view;
    }

}