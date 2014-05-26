package com.replaid.liveactionbar.app.Fragment;


import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.replaid.liveactionbar.app.CustomView.CustomScrollView;
import com.replaid.liveactionbar.app.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class LiveActionBarWithScrollView extends Fragment {
    private static final String TAG = "LiveActionBarWithScrollView";
    private Drawable mActionBarDrawble;
    private View mHeaderView;
    private int mHeaderHeight, mCurrentActionBarAlpha;
    private CustomScrollView mScrollView;

    //Navigation Drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;


    private TypedValue mTypedValue = new TypedValue();

    public static LiveActionBarWithScrollView newInstance() {
        return new LiveActionBarWithScrollView();
    }

    public LiveActionBarWithScrollView() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);

        mActionBarDrawble = getResources().getDrawable(R.drawable.actionbar_background);
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setBackgroundDrawable(mActionBarDrawble);
        mActionBarDrawble.setAlpha(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_action_bar_with_scroll_view, container, false);
        mScrollView = (CustomScrollView) view.findViewById(R.id.scrollView);
        mHeaderView = view.findViewById(R.id.topView);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mScrollView.setScrollViewListener(new CustomScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(CustomScrollView scrollView, int x, int y, int oldx, int oldy) {
                int scrollY = y;
                mHeaderView.setTranslationY(Math.min(scrollY/2, mHeaderHeight/2));
                int alphaValue = getAlphaValue(getRatio(mHeaderView.getTranslationY()));
                mActionBarDrawble.setAlpha(alphaValue);

                mCurrentActionBarAlpha = alphaValue;
            }
        });
    }

    private float getRatio(float translatedY) {
        return  (translatedY / 250);
    }

    private int getAlphaValue(float ratio) {
        int alpha = (int) Math.max(0.0, (ratio * 255));
        return alpha;
    }

    public void changeActionBarAlpha(float offset) {
        Log.i(TAG, "offset is " + offset);
        setAlphaValueWithNavigationDrawer(offset);
    }

    private void setAlphaValueWithNavigationDrawer(float ratio) {
        int alpha = (int) Math.max(0.0, mCurrentActionBarAlpha + (ratio * (255 - mCurrentActionBarAlpha)));
        mActionBarDrawble.setAlpha(alpha);
    }
}
