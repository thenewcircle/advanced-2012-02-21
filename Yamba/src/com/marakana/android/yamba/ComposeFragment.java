package com.marakana.android.yamba;

import winterwell.jtwitter.TwitterException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeFragment extends Fragment
			implements OnClickListener {

	private static final String TAG = "ComposeFragment";
	
	private EditText editStatusMsg;
	private Toast toast;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View top = inflater.inflate(R.layout.compose_fragment, container, false);
		
		editStatusMsg = (EditText) top.findViewById(R.id.edit_status_msg);
		Button updateButton = (Button) top.findViewById(R.id.button_update);
		updateButton.setOnClickListener(this);
		
		toast = Toast.makeText(getActivity().getApplicationContext(), null, Toast.LENGTH_LONG);
		
		return top;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.button_update:
			Log.v(TAG, "Button clicked");
			
			String msg = editStatusMsg.getText().toString();
			Log.v(TAG, "User entered: " + msg);
			editStatusMsg.setText("");
			
			if (msg.length() != 0) {
				new PostToTwitter().execute(msg);
			}
			
			break;
		default:
			// We should never get here! Unrecognized view.
		}
	}
	
	private class PostToTwitter extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... args) {
			int result = R.string.post_status_success;
			
			try {
				YambaApplication app = YambaApplication.getInstance();
				app.getTwitter().setStatus(args[0]);
			} catch (TwitterException e) {
				Log.e(TAG, "Failed to post status message", e);
				result = R.string.post_status_fail;
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			toast.setText(result);
			toast.show();
		}
		
	}

}







