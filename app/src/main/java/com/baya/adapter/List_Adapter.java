package com.baya.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baya.AppointmentDetail;
import com.baya.BeanClass.AppointmentBean;
import com.baya.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class List_Adapter extends BaseAdapter
{

    private final DisplayImageOptions options1;
    Context context;
    LayoutInflater layoutInflater;
    ImageView image;
    TextView tv,tv2;
    List<AppointmentBean> rowItems;


    private final DisplayImageOptions options;
    private ImageLoaderConfiguration config;
    ImageLoader imageLoader = null;
    ImageView imageView=null;
     public List_Adapter(Context context,List<AppointmentBean> rowItems)
    {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options =  new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defult_icon).showImageForEmptyUri(R.drawable.defult_icon)
                .showImageOnFail(R.drawable.defult_icon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(900)).build();
        options1 =  new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.smiley).showImageForEmptyUri(R.drawable.smiley)
                .showImageOnFail(R.drawable.smiley).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(900)).build();

 this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.rowItems = rowItems;
     }
    @Override
    public int getCount() {
       return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
 return rowItems.get(position);
     }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

         convertView = layoutInflater.inflate(R.layout.customlistview,parent,false);
        image =(ImageView)convertView.findViewById(R.id.imgesid);
        tv =(TextView)convertView.findViewById(R.id.name);
        tv2 =(TextView)convertView.findViewById(R.id.date);
         final AppointmentBean rowItem = (AppointmentBean) getItem(position);
if(!rowItem.getAppointment_id().equals("")) {
    imageLoader.displayImage(rowItem.getBusiness_logo(), image, options);
}
        else
{
    imageLoader.displayImage(rowItem.getBusiness_logo(), image, options1);
}
        tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        tv2.setTypeface(Typeface.createFromAsset(context.getAssets(), "museo/MUSEO_SLAB_0.OTF"));
         tv.setText(rowItems.get(position).getBusiness_name());
        try {

            if(!rowItem.getAppointment_id().equals("")) {
                SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMM dd yyyy");
                String string = "yyyy-MM-dd";
                DateFormat format = new SimpleDateFormat(string);
                Date date = format.parse(rowItems.get(position).getAppointment_date().split("T")[0]);
                String str_selected_date = formatter.format(date);
                tv2.setText(str_selected_date);
            }
            else{
                tv2.setText("Please book appointment first");
            }
        }

        catch (Exception e){}
convertView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(!rowItems.get(position).getAppointment_id().equals(""))
        {
            Intent i = new Intent(context, AppointmentDetail.class);
            i.putExtra("from", "upcoming");
            i.putExtra("appointment_id", rowItems.get(position).getAppointment_id());
            context.startActivity(i);
        }
    }
});
         return convertView;




    }

}
