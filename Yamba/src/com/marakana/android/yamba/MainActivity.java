package com.marakana.android.yamba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
	
	private TimelineFragment timelineFragment;
	private ComposeFragment composeFragment;
	private FragmentManager fm;
	
	private int fragmentVisible = TIMELINE_VISIBLE;
	private static final int TIMELINE_VISIBLE = 1;
	private static final int COMPOSE_VISIBLE = 2;
	private static final String FRAGMENT_VISIBLE_KEY = "FRAGMENT_VISIBLE_KEY";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        fm = getSupportFragmentManager();
        
        if (savedInstanceState == null) {
        	// Create the activity's fragments and initialize
        	timelineFragment = new TimelineFragment();
        	composeFragment = new ComposeFragment();
        	
        	fm.beginTransaction()
        		.add(R.id.main_container, timelineFragment, "timeline")
        		.add(R.id.main_container, composeFragment, "compose")
        		.hide(composeFragment)
        		.commit();
        	fragmentVisible = TIMELINE_VISIBLE;
        }
        else {
        	// Get references to the re-created fragments
        	timelineFragment = (TimelineFragment) fm.findFragmentByTag("timeline");
        	composeFragment = (ComposeFragment) fm.findFragmentByTag("compose");
        	
        	fragmentVisible = savedInstanceState.getInt(FRAGMENT_VISIBLE_KEY, TIMELINE_VISIBLE);
        	Log.v(TAG, "FRAGMENT_VISIBLE_KEY = " + fragmentVisible);
        	switch (fragmentVisible) {
        	case COMPOSE_VISIBLE:
        		showComposeFragment();
        		break;
        	default:
        		showTimelineFragment();
        	}
        }
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.v(TAG, "Saving FRAGMENT_VISIBLE_KEY = " + fragmentVisible);
		outState.putInt(FRAGMENT_VISIBLE_KEY, fragmentVisible);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.options_status_update, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		int id = item.getItemId();
		switch (id) {
		case R.id.menu_preference:
			// Display the preference activity
			intent = new Intent(this, PrefsActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_show_compose:
			// Show the compose fragment
			showComposeFragment();
			return true;
		case R.id.menu_show_timeline:
			// Show the timeline fragment
			showTimelineFragment();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void showComposeFragment() {
		Log.v(TAG, "Showing compose fragment");
		fm.beginTransaction()
			.hide(timelineFragment)
			.show(composeFragment)
			.commit();
		fragmentVisible = COMPOSE_VISIBLE;
	}
	
	private void showTimelineFragment() {
		Log.v(TAG, "Showing timeline fragment");
		fm.beginTransaction()
			.hide(composeFragment)
			.show(timelineFragment)
			.commit();
		fragmentVisible = TIMELINE_VISIBLE;
	}

}


