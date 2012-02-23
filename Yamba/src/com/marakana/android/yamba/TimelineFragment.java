package com.marakana.android.yamba;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TimelineFragment extends ListFragment
		implements SimpleCursorAdapter.ViewBinder, LoaderCallbacks<Cursor> {
	
	private static final String[] FROM = {
		StatusProvider.KEY_USER,
		StatusProvider.KEY_MESSAGE,
		StatusProvider.KEY_CREATED_AT
	};
	
	private static final int[] TO = {
		R.id.status_user,
		R.id.status_msg,
		R.id.status_date
	};
	
	private SimpleCursorAdapter adapter;
	private TimelineReceiver receiver;
	private IntentFilter filter;
	
	private NotificationManager nm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View top = super.onCreateView(inflater, container, savedInstanceState);
		
		adapter = new SimpleCursorAdapter(getActivity(),
								R.layout.timeline_row, null, FROM, TO, 0);
		adapter.setViewBinder(this);
		setListAdapter(adapter);
		
		receiver = new TimelineReceiver();
		filter = new IntentFilter(YambaApplication.ACTION_NEW_STATUS);
		
		return top;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.options_timeline_fragment, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		Intent intent;
		switch (id) {
		case R.id.menu_start_service:
			// Start the UpdaterService
			intent = new Intent(getActivity(), UpdaterService.class);
			getActivity().startService(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		getLoaderManager().restartLoader(0, null, this);
		getActivity().registerReceiver(receiver, filter, YambaApplication.PERM_NEW_STATUS, null);
		nm.cancel(YambaApplication.NOTIFICATION_NEW_STATUS);
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(receiver);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity().getApplicationContext(),
				StatusProvider.CONTENT_URI, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// The cursor is ready!
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// The old cursor is going to be released, so stop using it!
		adapter.swapCursor(null);
	}

	@Override
	public boolean setViewValue(View v, Cursor cursor, int columnIndex) {
		int id = v.getId();
		switch (id) {
		case R.id.status_date:
			// Bind the date value to the target view
			long timestamp = cursor.getLong(columnIndex);
			CharSequence relTime = DateUtils.getRelativeTimeSpanString(timestamp);
			TextView tv = (TextView) v;
			tv.setText(relTime);
			return true;
		default:
			// Let the SimpleCursorAdapter do its default data binding
			return false;
		}
	}
	
	private class TimelineReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			getLoaderManager().restartLoader(0, null, TimelineFragment.this);
			nm.cancel(YambaApplication.NOTIFICATION_NEW_STATUS);
		}
	}

}



