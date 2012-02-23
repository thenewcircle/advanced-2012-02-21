package com.marakana.android.countdownbroken;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CountdownFragment extends Fragment implements OnClickListener {

	private static final String TAG = "CountdownFragment";
	private TextView countdownText;
	private Button startButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View top = inflater.inflate(R.layout.countdown_fragment, container, false);
		
        startButton = (Button) top.findViewById(R.id.button_start);
        startButton.setOnClickListener(this);
        countdownText = (TextView) top.findViewById(R.id.text_countdown);
        
		return top;
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
			CountdownFragment.this.countdownText.setText(values[0].toString());
		}
		
		@Override
		protected void onPostExecute(String result) {
			CountdownFragment.this.startButton.setEnabled(true);
			Toast.makeText(CountdownFragment.this.getActivity().getApplicationContext(),
					result, Toast.LENGTH_LONG).show();
		}
	
	}

}
