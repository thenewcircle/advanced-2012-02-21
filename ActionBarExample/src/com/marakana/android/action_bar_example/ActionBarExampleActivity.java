package com.marakana.android.action_bar_example;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ActionBarExampleActivity extends Activity {
	private Toast toast;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        toast = Toast.makeText(this, null, Toast.LENGTH_LONG);
        
        // Enable the Home action item for ICS and later
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        	getActionBar().setHomeButtonEnabled(true);
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.options_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.menu_add:
		case R.id.menu_edit:
		case R.id.menu_settings:
			toast.setText(item.getTitle());
			toast.show();
			return true;
		case android.R.id.home:
			toast.setText("Home");
			toast.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
    
}