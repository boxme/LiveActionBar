package com.replaid.liveactionbar.app;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.replaid.liveactionbar.app.Fragment.LiveActionBarWithScrollView;

import java.util.ArrayList;


public class NavigationDrawerActivity extends FragmentActivity {
    private static final String TAG = "NavigationDrawerActivity";
    private Drawable mActionBarDrawble;
    private int mActionBarHeight;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private TypedValue mTypedValue = new TypedValue();

    private LiveActionBarWithScrollView mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        //Create ActionBar drawable
        ActionBar actionBar = getActionBar();
        mActionBarDrawble = getResources().getDrawable(R.drawable.actionbar_background);
        actionBar.setBackgroundDrawable(mActionBarDrawble);
        mActionBarDrawble.setAlpha(0);

        //Set up navigation drawer
        setupDrawer();

        setupFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getActionBar().setTitle("Navigation Drawer");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.isDrawerIndicatorEnabled() &&
                mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawer() {
        ArrayList<String> navigationDrawerItem = new ArrayList<String>();
        for (int i = 0; i < 20; ++i) {
            navigationDrawerItem.add("Item " + i);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setPadding(0, getActionBarHeight(), 0, 0);
        setupDrawerToggle();
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.nav_drawer_item,
                                                        navigationDrawerItem));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
                                                  R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mFragment.changeActionBarAlpha(slideOffset);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private int getActionBarHeight() {
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }

        getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data,
                getResources().getDisplayMetrics());
        return mActionBarHeight;
    }

    private void setupFragment() {
        mFragment = LiveActionBarWithScrollView.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, mFragment, "scroll_view");
        ft.commit();
    }

}
