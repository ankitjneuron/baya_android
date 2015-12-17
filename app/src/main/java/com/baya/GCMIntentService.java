package com.baya;
/*
* for Notification
*
* */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.baya.Helper.Constants;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONObject;


public class GCMIntentService extends GCMBaseIntentService {
	private static final String TAG = "GcmTest";
	@SuppressWarnings("unused")
	private Context context;
	static int j = 0;
	String box_to_id = "";
	String type = "";
	AsyncTask<Void, Void, Void> mRegisterTask;
	private Notification notification;
	private SharedPreferences preferences;

	public GCMIntentService() {

		super(Constants.SENDER_ID);

	}

	@Override
	protected void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);

	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		super.onDeletedMessages(context, total);
		Log.i(TAG, "Received deleted messages notification");
	

		// notifies user
		// generateNotification(context, message,notification_type);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		this.context = context;
		Log.i(TAG, "Received message");
		try
		{
		String message = intent.getStringExtra("message");
		System.out.println("intent " + intent.getExtras().toString());
		JSONObject messageobj = new JSONObject(intent.getExtras().toString());
		generateNotification(context, messageobj.getString("message"));
	}
		catch (Exception e){}
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);

	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");

		if (GCMRegistrar.isRegisteredOnServer(context)) {

		} else {
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			Log.i(TAG, "Ignoring unregister callback");
		}
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);

		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */

	@SuppressWarnings("deprecation")
	private void generateNotification(final Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		preferences = context.getSharedPreferences(Constants.BAYA, 0);
		System.out.println("meeeeeeeeee   " + message);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		String title = context.getString(R.string.app_name);

		j = (int) (System.currentTimeMillis() & 0xfffffff);
		Notification notification = new Notification(icon, message, when);
		Intent notificationIntent=null;
		String access_token = preferences.getString(Constants.ACCESS_TOKEN, "");
		if (access_token.equals("")) {

			notificationIntent = new Intent(context, BaseScreen.class);

		} else {
			notificationIntent = new Intent(context, Landing_Screen.class);
			notificationIntent.putExtra("position", 1);
		}
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent intent = PendingIntent.getActivity(context, j, notificationIntent, 0);

		// set intent so it does not start a new activity

		notification = new Notification.Builder(context).setTicker(title).setSmallIcon(icon).setWhen(when).setContentTitle(title).setContentText(message)
				.setContentIntent(intent).build();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notificationManager.notify(j, notification);

	}

}
