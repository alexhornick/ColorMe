package com.bauandhornick.colorme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Make status bar dark.
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setStatusBarColor(0xff11051B);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_about);
    }
}
