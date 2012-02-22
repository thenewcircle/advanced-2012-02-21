package com.marakana.android.countdownfixed;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CountdownActivity extends Activity implements OnClickListener {
	
	private static final String TAG = "CountdownActivity";
	private static final String COUNTDOWN_VALUE = "COUNTDOWN_VALUE";
	private TextView countdownText;
	private Button startButton;
	private final long timestamp = System.currentTimeMillis();
	private CountdownTask task = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(this);
        
        countdownText = (TextView) findViewById(R.id.text_countdown);
        
        // Restore the last value displayed by the countdown view
        if (savedInstanceState != null) {
	        CharSequence lastValue = savedInstanceState.getCharSequence(COUNTDOWN_VALUE);
	        if (lastValue != null) {
	        	countdownText.setText(lastValue);
	        }
        }
        
        task = (CountdownTask) getLastNonConfigurationInstance();
        
        if (task != null) {
        	// There is a CountdownTask that was saved
        	task.attach(this);
        	if (task.getStatus() != AsyncTask.Status.FINISHED) {
        		// And it is still active
        		startButton.setEnabled(false);
        	}
        }
        
        Log.d(TAG, "onCreate() with timestamp: " + timestamp);
    }

    /*
     * Note that the system invokes this method only in response to a
     * configuration change, where the system will create a new instance
     * immediately. The system will *not* invoke this method if the
     * user presses the hardware back button or the activity invokes
     * finish() on itself.
     * 
     * @see android.app.Activity#onRetainNonConfigurationInstance()
     */
	@Override
	public Object onRetainNonConfigurationInstance() {
		task.detach();
		return task;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putCharSequence(COUNTDOWN_VALUE, countdownText.getText());
	}

	public void onClick(View v) {
		if (v.getId() == R.id.button_start) {
			v.setEnabled(false);
			task = new CountdownTask(this);
			task.execute();
		}
	}
	
	private void updateCounter(CharSequence msg) {
		countdownText.setText(msg.toString());
		Log.d(TAG, "Activity timestamp: " + timestamp + '\n'
				   + "Updated TextView to: " + msg);
	}
	
	private void countdownComplete(String result) {
		startButton.setEnabled(true);
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
	
	private static class CountdownTask extends AsyncTask<Void, Integer, String> {
		
		private CountdownActivity activity;

		public CountdownTask(CountdownActivity activity) {
			super();
			this.activity = activity;
		}
		
		public void attach(CountdownActivity activity) {
			this.activity = activity;
		}

		public void detach() {
			activity = null;
		}

		@Override
		protected String doInBackground(Void... args) {
			String status = "Thread completed";
			for (int i = 10; i >= 0; i--) {
				publishProgress(i);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					status = "Thread interrupted";
					break;
				}
			}
			return status;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if (activity != null) {
				activity.updateCounter(values[0].toString());
			}
			else {
				Log.w(TAG, "onProgressUpdate(): No associated Activity");
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (activity != null) {
				activity.countdownComplete(result);
			}
			else {
				Log.w(TAG, "onPostExecute(): No associated Activity");
			}
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