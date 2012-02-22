package com.marakana.android.programmatic_layout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProgrammaticLayoutActivity extends Activity implements OnClickListener {
	private static final String TAG = ProgrammaticLayoutActivity.class.getName();
	
	static final float PADDING_DP = 5;
	float mDensity;
	int mPaddingPixels;
	
	static final int EDIT_CONTENT_ID = 1;
	
	LinearLayout mLayoutMain;
	TextView mTextTitle;
	EditText mEditContent;
	Button mButtonSubmit;
	
	Toast mToast;
	String mToastMsgPrefix;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get the dp scaling factor for use in calculating pixel values
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDensity = metrics.density;
        
        // Calculate padding based on a 5dp value
        mPaddingPixels = (int) (mDensity * PADDING_DP);
        Log.v(TAG, "mPaddingPixels = " + mPaddingPixels);
        
        // Main LinearLayout configuration
        mLayoutMain = new LinearLayout(this);
        mLayoutMain.setOrientation(LinearLayout.VERTICAL);
        mLayoutMain.setPadding(mPaddingPixels, mPaddingPixels, mPaddingPixels, mPaddingPixels);
        
        setContentView(mLayoutMain);
        
        // Title TextView configuration
        mTextTitle = new TextView(this);
        mTextTitle.setTextSize(25.0f);	// 25sp
        mTextTitle.setText(R.string.activity_title);
        mTextTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        
        // Title TextView layout
        mLayoutMain.addView(mTextTitle, MATCH_PARENT, WRAP_CONTENT);
        
        // Content EditText configuration
        mEditContent = new EditText(this);
        mEditContent.setId(EDIT_CONTENT_ID);
        mEditContent.setHint(R.string.edit_content_hint);
        
        // Content EditText layout
        LinearLayout.LayoutParams editContentLayout
        	= new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1.0f);
        editContentLayout.topMargin = mPaddingPixels;
        editContentLayout.bottomMargin = mPaddingPixels;
        mLayoutMain.addView(mEditContent, editContentLayout);
        
        // Submit Button configuration
        mButtonSubmit = new Button(this);
        mButtonSubmit.setText(R.string.button_submit_text);
        
        // Submit Button layout
        LinearLayout.LayoutParams buttonSubmitLayout
        	= new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        buttonSubmitLayout.gravity = Gravity.CENTER_HORIZONTAL;
        mLayoutMain.addView(mButtonSubmit, buttonSubmitLayout);
        
        mButtonSubmit.setOnClickListener(this);
        
        // Initialize a reusable Toast
        mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
        mToastMsgPrefix = getString(R.string.toast_message_prefix);
    }

	@Override
	public void onClick(View arg0) {
		String content = mEditContent.getText().toString();
		mEditContent.setText("");
		mToast.setText(mToastMsgPrefix + content);
		mToast.show();
	}
}