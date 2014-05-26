package com.replaid.liveactionbar.app;

import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.replaid.liveactionbar.app.Fragment.ChoicesFragment;


public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        Drawable actionbarBackground = getResources().getDrawable(R.drawable.actionbar_background);
        actionBar.setBackgroundDrawable(actionbarBackground);
        actionbarBackground.setAlpha(0);
        setupFragment();
    }

    private void setupFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, ChoicesFragment.newInstance(), "choices_fragment").commit();
    }
}
