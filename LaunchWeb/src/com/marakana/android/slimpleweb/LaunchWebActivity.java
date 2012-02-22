package com.marakana.android.slimpleweb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LaunchWebActivity extends Activity implements OnClickListener {
	private EditText mEditUri;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mEditUri = (EditText) findViewById(R.id.edit_uri);
        Button buttonDisplay = (Button) findViewById(R.id.button_display);
        buttonDisplay.setOnClickListener(this);
    }

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.button_display:
			// It's the display button
			String uriString = mEditUri.getText().toString();
			Uri uri = Uri.parse(uriString);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		default:
			// We should never get here!
		}
	}
}