package com.marakana.android.countdownfragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CountdownFragment extends Fragment implements OnClickListener {
	private static final String TAG = "CountdownFragment";

	private static final String COUNTER_VALUE = "COUNTER_VALUE";

	private CountdownTask task = null;
	private TextView countdownText;
	private Button startButton;
	private Toast toast;
	private ProgressBar progressBar;
	private final long timestamp = System.currentTimeMillis();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView() invoked");
		View top = inflater.inflate(R.layout.countdown_fragment, container, false);
		
        // Restore content of the countdown display.
		countdownText = (TextView) top.findViewById(R.id.text_countdown);
        if (savedInstanceState != null) {
        	Log.d(TAG, "onCreate() Bundle is not null");
        	CharSequence previousValue = savedInstanceState.getCharSequence(COUNTER_VALUE);
        	if (previousValue != null) {
        		Log.d(TAG, "Previous countdown text value: " + previousValue);
        		countdownText.setText(previousValue);
        	}
        }
        
        // Set state of ProgressBar and Button, depending on whether a countdown is in progress.
        progressBar = (ProgressBar) top.findViewById(R.id.progress_bar);
        startButton = (Button) top.findViewById(R.id.button_start);
        startButton.setOnClickListener(this);
        if (task != null) {
        	startButton.setEnabled(false);
        	progressBar.setVisibility(View.VISIBLE);
        }
        
        // Initialize and cache a Toast
		toast = Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_LONG);
        
		return top;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState() invoked");
		outState.putCharSequence(COUNTER_VALUE, countdownText.getText());
	}

	@Override
	public void onDestroy() {
		if (task != null) {
			task.cancel(true);
		}
		super.onDestroy();
	}

	public void onClick(View v) {
		if (v.getId() == R.id.button_start) {
			v.setEnabled(false);
        	progressBar.setVisibility(View.VISIBLE);
			task = new CountdownTask();
			task.execute();
		}
	}
	
	private void updateCounter(CharSequence msg) {
		countdownText.setText(msg.toString());
		Log.d(TAG, "CountdownFragment timestamp: " + timestamp + '\n'
				   + "Updated TextView to: " + msg);
	}
	
	private void countdownComplete(String result) {
		task = null;
		startButton.setEnabled(true);
    	progressBar.setVisibility(View.INVISIBLE);
		toast.setText(result);
		toast.show();
	}
	
	private class CountdownTask extends AsyncTask<Void, Integer, String> {
		
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
			updateCounter(values[0].toString());
		}
		
		@Override
		protected void onPostExecute(String result) {
			countdownComplete(result);
		}

		@Override
		protected void onCancelled(String result) {
			countdownComplete(result);
		}

	}
}
