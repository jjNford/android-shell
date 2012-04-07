package com.jjnford.android;

import com.jjnford.android.shell.R;

import android.app.Activity;
import android.os.Bundle;

public class DriverActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}