package com.baya.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baya.BeanClass.Doctor;
import com.baya.BeanClass.Speciality;
import com.baya.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;


public class Bussiness_profile_adapter extends BaseAdapter {
    private Context mContext;

    private ImageView doctor_image;
    private TextView doctor_name_txt,business_profile_doctor_speciality_txt;
    private TextView doctor_description_txt;
    TextView business_profile_doctor_description;
    StringBuilder sp;
    private DisplayImageOptions options;
    private ImageLoaderConfiguration config;
    ImageLoader imageLoader = null;
    ArrayList<Doctor> ardoctor;
    ArrayList<Speciality> specialitie=new ArrayList<Speciality>();
    String sq;
    public Bussiness_profile_adapter(Context c,ArrayList<Doctor> ardoctor) {

        this.mContext = c;
        this.ardoctor=ardoctor;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(900)).build();
    }

    @Override
    public int getCount() {
        return ardoctor.size();
    }

    @Override
    public Object getItem(int position) {
        return ardoctor.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ardoctor.indexOf(ardoctor.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.business_profile_laytout, parent,false);
        doctor_image = (ImageView) convertView.findViewById(R.id.business_profile_doctor_image);
        doctor_name_txt = (TextView) convertView.findViewById(R.id.business_profile_doctor_name);
        business_profile_doctor_speciality_txt = (TextView) convertView.findViewById(R.id.business_profile_doctor_speciality_txt);
        business_profile_doctor_description=(TextView)convertView.findViewById(R.id.business_profile_doctor_description);

        doctor_name_txt.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "museo/JUSTOLDFASHION_5.TTF"));

        business_profile_doctor_speciality_txt.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        business_profile_doctor_description.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        imageLoader.displayImage(ardoctor.get(position).getImage(), doctor_image, options);
        doctor_name_txt.setText(ardoctor.get(position).getName());

        if(ardoctor.get(position).getCategory().equals(""))
        {
            business_profile_doctor_speciality_txt.setText("No Categories are available");
        }
        else {
            business_profile_doctor_speciality_txt.setText(ardoctor.get(position).getCategory());
        }
        business_profile_doctor_description.setText(ardoctor.get(position).getDescription());
        return convertView;
    }

}