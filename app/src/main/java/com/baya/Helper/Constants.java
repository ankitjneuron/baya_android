package com.baya.Helper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baya.BaseScreen;
import com.baya.BeanClass.Categories;
import com.baya.Login;
import com.baya.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Constants
{
	public static ProgressDialog pDialog = null;
	public static boolean web_service_status = false;

	public static double latitude = 0.0;

	public static double logitude = 0.0;
	public static final String SENDER_ID = "621615026258";
	public static final String SIGNUP = "signup";
	public static final String LOGIN = "LOGIN";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String BAYA = "baya";
    public static ArrayList<Categories> Categoriesarray=new ArrayList<Categories>();


	public static void showMessage(String message, Context context)
	{
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.getView().setBackgroundResource(R.drawable.customtoast);
		toast.show();
	}
	public static boolean validateEmail(String email)
{

	Pattern pattern;
	Matcher matcher;
	final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	pattern = Pattern.compile(EMAIL_PATTERN);
	matcher = pattern.matcher(email);
	return matcher.matches();
}



	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int round)
	{
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = round;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;

	}

	public static boolean isInternetOn(Context context)
	{
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();

			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}

		}

		return false;
	}
	public static Dialog dialogs(final String title, final String message,  final Context activity, String yes)
	{

		// TextView title1 = new TextView(activity);

		alert1 = new Dialog(activity);
		alert1.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alert1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
		alert1.setContentView(R.layout.invalidtoken);
		alert1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		final TextView token = (TextView) alert1.findViewById(R.id.textview_conten);
		Button login_btn = (Button) alert1.findViewById(R.id.buttonlogin);
		TextView login = (TextView) alert1.findViewById(R.id.textview);

		login_btn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				alert1.dismiss();
				preferences = activity.getSharedPreferences(Constants.BAYA, 0);
				preferences.edit().putString(Constants.ACCESS_TOKEN, "").commit();
				preferences.edit().putString(Constants.LOGIN, "").commit();
				Intent intent = new Intent(activity, BaseScreen.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				activity.startActivity(intent);
			}
		});

		alert1.setCancelable(false);
		alert1.setCanceledOnTouchOutside(false);
		alert1.show();
		return alert1;

	}

	public static Dialog dialogsAlerts( final Context activity)
	{

		// TextView title1 = new TextView(activity);

		alert1 = new Dialog(activity);
		alert1.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alert1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
		alert1.setCancelable(false);
		alert1.setContentView(R.layout.invalidtoken);
		alert1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		final TextView token = (TextView) alert1.findViewById(R.id.textview_conten);
		Button login_btn = (Button) alert1.findViewById(R.id.buttonlogin);
		TextView login = (TextView) alert1.findViewById(R.id.textview);
		login.setText("Message");
		token.setText("Your account has been successfully created. Activate account please click on verification link sent on your email address.");
		login_btn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				alert1.dismiss();
				Intent intent = new Intent(activity, Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				activity.startActivity(intent);
			}
		});
//		token.setTypeface(Typeface.createFromAsset(activity.getAssets(), "museo/MUSEO-SLAB_5.OTF"));
//		login_btn.setTypeface(Typeface.createFromAsset(activity.getAssets(), "museo/MUSEO-SLAB_5.OTF"));
//		login.setTypeface(Typeface.createFromAsset(activity.getAssets(), "museo/MUSEO-SLAB_5.OTF"));
		alert1.setCancelable(false);
		alert1.setCanceledOnTouchOutside(false);
		alert1.show();
		return alert1;

	}
	static Dialog alert1 = null;
	private static SharedPreferences preferences;

	public static void showProgressDialog() {

		if (!pDialog.isShowing())
			pDialog.show();
	}

	public static void hideProgressDialog() {
		if (pDialog.isShowing())
			pDialog.hide();
	}
}
