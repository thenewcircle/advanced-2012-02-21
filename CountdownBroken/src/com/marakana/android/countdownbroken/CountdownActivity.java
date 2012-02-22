package com.marakana.android.countdownbroken;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This version of the application uses an AsyncTask incorrectly.
 * If the activity encounters a run-time configuration change
 * (e.g., device rotation), the system runs the activity through
 * its lifecycle methods, attempting to release the old activity
 * object and create a new one reflecting the new configuration.
 * 
 * However, the AsyncTask holds an implicit reference to the old
 * CountdownActivity object because it is an inner class. The old
 * CountdownActivity object is not released and the AsyncTask
 * continues to update the interface of that (now invisible) old
 * object.
 */
public class CountdownActivity extends Activity implements OnClickListener {
	
	private static final String TAG = "CountdownActivity";
	private TextView countdownText;
	private Button startButton;
	final long timestamp = System.currentTimeMillis();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(this);
        countdownText = (TextView) findViewById(R.id.text_countdown);
        
        Log.d(TAG, "onCreate() with timestamp: " + timestamp);
    }

	public void onClick(View v) {
		if (v.getId() == R.id.button_start) {
			v.setEnabled(false);
			new CountdownTask().execute();
		}
	}
	
	private class CountdownTask extends AsyncTask<Void, Integer, String> {

		@Override
		protected String doInBackground(Void... args) {
			for (int i = 10; i >= 0; i--) {
				publishProgress(i);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					return "Thread interrupted";
				}
			}
			return "Thread completed";
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			CountdownActivity.this.countdownText.setText(values[0].toString());
			Log.d(TAG, "Activity timestamp: " + CountdownActivity.this.timestamp);
		}
		
		@Override
		protected void onPostExecute(String result) {
			CountdownActivity.this.startButton.setEnabled(true);
			Toast.makeText(CountdownActivity.this, result, Toast.LENGTH_LONG).show();
		}

	}

	// Other lifecycle methods implemented only to log Activity state
	@Override
	protected void onStart() {
		super.onStart();
        Log.d(TAG, "onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();
        Log.d(TAG, "onResume()");
	}

	@Override
	protected void onPause() {
		super.onPause();
        Log.d(TAG, "onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
        Log.d(TAG, "onStop()");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
        Log.d(TAG, "onRestart()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
        Log.d(TAG, "onDestroy()");
	}
}