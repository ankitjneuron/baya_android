package com.baya;
/*
* IN this screen we are selcting the doctor and reson for the appointment
*
* */
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baya.BeanClass.Doctor;
import com.baya.BeanClass.Getbuisness;
import com.baya.BeanClass.Speciality;
import com.baya.Helper.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.Date;


public class SelectDoctor extends Activity {

// declation of the variable
    TextView next_txt = null,textview_buisnessprofile_book = null, textview_buisness_docnm, textview_buisness_contactn, textview_buisness_specils, texview_buisness_addressn,customertype;
    CheckBox checkBox_existing,checkBox_newcustomer;
    private ImageLoader imageLoader;
    TextView textView_date,textView_time,heading;
    private DisplayImageOptions options;
    private ImageView imageview_buisnesslistings_images;
    private TextView textview_buisnessp_titles,select_doctor;
    private Getbuisness bcategories;
    Date selectdate=null;
    String str_date="";
    String time="",str_reason="";
    ArrayList<String> doctor_array=null;
    ArrayList<Doctor> doctormain_array=null;
    private Dialog dialog;
    private ListView alldialoglistdoctor;
    EditText edittextsearch=null,edittext_reason=null;
    private ArrayAdapter<String> doctoradapter;
    String selected_doctor="",selected_doctor_id="";
    String customer_type="";
    boolean type=false,type1=false;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_with_confirm_appointment);
        doctor_array=new ArrayList<String>();
        bcategories = getIntent().getParcelableExtra("buisnesslist");
        doctormain_array = bcategories.getArrayDoctor();
        back=(ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        doctor_array.add("No Specific Doctor");
        selected_doctor="No Specific Doctor";
        for(int i=0;i<doctormain_array.size();i++)
        {
            doctor_array.add(doctormain_array.get(i).getName());
        }
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(SelectDoctor.this));
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defult_icon).showImageForEmptyUri(R.drawable.defult_icon)
                .showImageOnFail(R.drawable.defult_icon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(900))
                .build();
        initview();
        next_txt = (TextView) findViewById(R.id.book_appointment_next_txt);
        next_txt.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));

        // click listner of the next button and and validation and
        // pass the data to next screen
        next_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(next_txt.getWindowToken(), 0);
                }
                catch (Exception e)
                {

                }
                if(checkBox_existing.isChecked()==true)
                {
                    customer_type="existing";
                }
                else
                {
                    customer_type="";
                    if(checkBox_newcustomer.isChecked()==true)
                    {
                        customer_type="new";
                    }
                    else
                    {
                        customer_type="";
                    }
                }

                if (selected_doctor.equals("")) {
                    Constants.showMessage("Please Select Doctor ", SelectDoctor.this);
                } else if (customer_type.equals("")) {
                    Constants.showMessage("Please Select Customer Type ", SelectDoctor.this);
                }
//                else if (edittext_reason.getText().toString().equals("")) {
//                    Constants.showMessage("Please Enter Reason ", SelectDoctor.this);
//                }
                else {
                    Intent i = new Intent(SelectDoctor.this, BookAppointment_Detail.class);
                    i.putExtra("buisnesslist", bcategories);
                    i.putExtra("calender", selectdate);
                    i.putExtra("time", time);
                    i.putExtra("reschedule",getIntent().getExtras().getString("reschedule"));
                    i.putExtra("customer_type", customer_type);
                    i.putExtra("doctor_name", selected_doctor);
                    i.putExtra("doctor_id", selected_doctor_id);
                    i.putExtra("reason", edittext_reason.getText().toString());
                    startActivityForResult(i, 1234);
                }

            }
        });
        heading=(TextView)findViewById(R.id.textview_buisnessp_titles);
        heading.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        checkBox_existing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == false) {
                    type = false;

                } else {
                    type = true;
                    customer_type="existing";
                    checkBox_newcustomer.setChecked(false);
                }
            }
        });
        checkBox_newcustomer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==false)
                {
                    type=false;

                }
                else
                {
                    customer_type="new";
                    type=true;
                    checkBox_existing.setChecked(false);
                }
            }
        });

    }


    // initialixation of the view
    void initview() {
       
        textview_buisnessp_titles= (TextView) findViewById(R.id.textview_buisnessp_titles);
        edittext_reason=(EditText)findViewById(R.id.edittext_reason);
        textview_buisness_docnm = (TextView) findViewById(R.id.textview_buisness_doctornam);
        textview_buisness_contactn = (TextView) findViewById(R.id.textview_bussiness_phonen);
        textview_buisness_specils = (TextView) findViewById(R.id.textview_buisness_specials);
        texview_buisness_addressn = (TextView) findViewById(R.id.textview_buisness_addresstext);
        checkBox_existing=(CheckBox)findViewById(R.id.checkBox_existing);
        checkBox_newcustomer=(CheckBox)findViewById(R.id.checkBox_newcustomer);
        select_doctor= (TextView) findViewById(R.id.select_doctor);
        select_doctor.setText(selected_doctor);
        imageview_buisnesslistings_images = (ImageView) findViewById(R.id.imageview_buisnesslistings_images);
        textView_date= (TextView) findViewById(R.id.datetext);
        textView_time= (TextView) findViewById(R.id.timetext);customertype=(TextView)findViewById(R.id.customer_type_txt);
        select_doctor.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_buisnessp_titles.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_buisness_contactn.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        textview_buisness_specils.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        texview_buisness_addressn.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        textview_buisness_docnm.setTypeface(Typeface.createFromAsset(getAssets(), "museo/JUSTOLDFASHION_5.TTF"));
        edittext_reason.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textview_buisnessp_titles.setTypeface(Typeface.createFromAsset(getAssets(), "proximanova/PROXIMANOVACOND-REGULAR.OTF"));
        textView_time.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        textView_date.setTypeface(Typeface.createFromAsset(getAssets(), "museo/MUSEO_SLAB_5.OTF"));
        bcategories = getIntent().getParcelableExtra("buisnesslist");
        textView_date.setText(bcategories.getBusiness_name());
        textview_buisness_contactn.setText(bcategories.getPhone_number());
        textview_buisness_docnm.setText(bcategories.getBusiness_name());
//        textview_buisness_specils.setText(bcategories.describeContents());
//        textview_buisnessprofile_book.setText("Book Now");
        selectdate= (Date) getIntent().getExtras().get("calender");
        str_date=(String) android.text.format.DateFormat.format("dd-MM-yyyy", selectdate);
        time=getIntent().getExtras().getString("time");
        textView_date.setText(str_date);
        textView_time.setText(time);
        if(bcategories.getBuisnesscategories().size()==0)
        {
            textview_buisness_specils.setText("No Categories are available");
        }
        else {
            String category = "";
            for (int i = 0; i < bcategories.getBuisnesscategories().size(); i++) {
                category = category + "," + bcategories.getBuisnesscategories().get(i).get_id();
            }

            category = category.substring(1, category.length());
            textview_buisness_specils.setText(category);


        }
        texview_buisness_addressn.setText(bcategories.getAddress() + " " + bcategories.getCity() + " " + bcategories.getCountry() + " " + bcategories.getZipcode());
        imageLoader.displayImage(bcategories.getBusiness_logo(), imageview_buisnesslistings_images, options);
        select_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogdoctorList();
            }
        });
    }


    // dialog for the doctor
    public void dialogdoctorList() {

     

            dialog = new Dialog(SelectDoctor.this);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_list);

            alldialoglistdoctor = (ListView) dialog.findViewById(R.id.lst);
            ImageButton close = (ImageButton) dialog.findViewById(R.id.close);
            edittextsearch = (EditText) dialog.findViewById(R.id.search_box_city);
            edittextsearch.setVisibility(View.GONE);

            close.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    dialog.dismiss();
                }
            });

            Setdoctordata();

            dialog.show();
       
    }

    public void Setdoctordata() {

        doctoradapter = new ArrayAdapter<String>(SelectDoctor.this, R.layout.dialog_list_items, doctor_array);

        alldialoglistdoctor.setAdapter(doctoradapter);
        alldialoglistdoctor.setChoiceMode(1);
        if (selected_doctor.equals("")) {

        } else {
            alldialoglistdoctor.setItemChecked(doctor_array.indexOf(selected_doctor), true);
        }

        alldialoglistdoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

                dialog.dismiss();
                if(doctor_array.size()==1)
                {
                    selected_doctor = alldialoglistdoctor.getItemAtPosition(pos).toString();

                    selected_doctor_id ="";
                    select_doctor.setText(selected_doctor);
                }
                else {
                    selected_doctor = alldialoglistdoctor.getItemAtPosition(pos).toString();
if(pos==0)
{
    selected_doctor_id = "";
    select_doctor.setText(selected_doctor);
}
                    else {
    selected_doctor_id = doctormain_array.get(pos - 1).get_id();
    select_doctor.setText(selected_doctor);
}
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((resultCode==1234)&&(requestCode==1234))
        {
            Intent i=new Intent();
            i.putExtra("business_name",data.getStringExtra("business_name"));
            i.putExtra("time", data.getStringExtra("time"));
            i.putExtra("date", data.getStringExtra("date"));
            setResult(1234,i);
            this.finish();
        }
    }
}
