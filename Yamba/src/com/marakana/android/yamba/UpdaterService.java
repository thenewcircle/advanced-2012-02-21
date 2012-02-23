package com.marakana.android.yamba;

import java.util.Date;
import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

public class UpdaterService extends IntentService {
	
	private static final String TAG = "UpdaterService";

	public UpdaterService() {
		super(TAG);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		YambaApplication app = YambaApplication.getInstance();
		ContentResolver cr = getContentResolver();
		int count = 0;
		try {
			List<Twitter.Status> timeline = app.getTwitter().getHomeTimeline();
			
			ContentValues values = new ContentValues();
			for (Twitter.Status status: timeline) {
				Date createdAt = status.createdAt;
				long id = status.id;
				String user = status.user.name;
				String msg = status.text;
				
				values.clear();
				values.put(StatusProvider.KEY_ID, id);
				values.put(StatusProvider.KEY_USER, user);
				values.put(StatusProvider.KEY_MESSAGE, msg);
				values.put(StatusProvider.KEY_CREATED_AT, createdAt.getTime());
				
				try {
					cr.insert(StatusProvider.CONTENT_URI, values);
					count++;
				} catch (Exception e) {
					// For simplicity, we'll assume the exception is because of a duplicate status
				}
				
				Log.v(TAG, user + " posted at " + createdAt + ": " + msg);
			}
		} catch (TwitterException e) {
			Log.e(TAG, "Unable to fetch timeline", e);
		}
		if (count > 0) {
			notifyNewStatus(count);
		}
	}
	
	private void notifyNewStatus(int count) {
		Intent broadcast = new Intent(YambaApplication.ACTION_NEW_STATUS);
		broadcast.putExtra(YambaApplication.EXTRA_NEW_STATUS_COUNT, count);
		sendBroadcast(broadcast, YambaApplication.PERM_NEW_STATUS);
		
		Notification notification = new Notification(android.R.drawable.stat_notify_chat,
													 "", System.currentTimeMillis());
		CharSequence title = "New Yamba Message";
		CharSequence text = "You have new Yamba messages";
		Intent viewMessages = new Intent(this, MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 1, viewMessages,
													 PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(this, title, text, pi);
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		if (count > 1) {
			notification.number = count;
		}
		
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.notify(YambaApplication.NOTIFICATION_NEW_STATUS, notification);
	}

}



