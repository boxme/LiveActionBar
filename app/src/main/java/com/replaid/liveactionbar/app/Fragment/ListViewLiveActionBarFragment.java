package com.replaid.liveactionbar.app.Fragment;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.replaid.liveactionbar.app.AlphaForegroundColorSpan;
import com.replaid.liveactionbar.app.R;

import java.util.ArrayList;

public class ListViewLiveActionBarFragment extends Fragment {
    private static final String TAG = "ListViewLiveActionBarFragment";

    private ListView mListView;
    private View mHeaderView;                                       //The view on top of listview
    private View mPlaceHolderView;                                  //ListView header view

    private int mHeaderHeight;                                      //Height of the headerView
    private int mActionBarHeight;
    private int mActionBarTitleColor;
    private int mMinHeaderTranslation;
    private AccelerateDecelerateInterpolator mSmoothInterpolator;

    private SpannableString mSpannableString;                       //The actionBar title
    private AlphaForegroundColorSpan mAlphaForegroundColorSpan;     //To change the color of the actionBar title

    private TypedValue mTypedValue = new TypedValue();              //Container for a dynamically typed data value. Use with resource value

    //Not in use
    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();

    public static ListViewLiveActionBarFragment newInstance() {
        return new ListViewLiveActionBarFragment();
    }

    public ListViewLiveActionBarFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSmoothInterpolator = new AccelerateDecelerateInterpolator();
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);

        //Max Y to be translated by the Header, stop when the height of the view
        //on top of listView, mHeaderView, is the same with the height of an action bar
        mMinHeaderTranslation = getActionBarHeight() - mHeaderHeight;

        mActionBarTitleColor = getResources().getColor(R.color.actionbar_title_color);
        mSpannableString = new SpannableString("ListView");
        mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(mActionBarTitleColor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_listview_live_actionbar, container, false);
        mListView = (ListView) view.findViewById(R.id.listview);
        mHeaderView = view.findViewById(R.id.header);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Fake data for listView
        ArrayList<String> listViewData = new ArrayList<String>();
        for (int i = 0; i < 1000; ++i) {
            listViewData.add("Entry " + i);
        }

        mPlaceHolderView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_header, null);
        mListView.addHeaderView(mPlaceHolderView);
        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                listViewData));

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //Translating the mHeaderView which is on top of the listView
                int scrollY = getScrollY();

                //Sticky actionBar
                mHeaderView.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
                Log.i(TAG, "scrollY " + (scrollY) + " minTranslation " + mMinHeaderTranslation);

                //header_logo --> actionbar icon
                float ratio = clamp(mHeaderView.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
//                interpolate(mHeaderLogo, getActionBarIconView(), mSmoothInterpolator.getInterpolation(ratio));

                //ActionBar Title Alpha. refresh the title
                setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
            }
        });
    }

    private int getActionBarHeight() {
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }

        //Retrieve the value of an attribute in the Theme
        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data,
                                                                  getResources().getDisplayMetrics());
        return mActionBarHeight;
    }

    /**
     * Change the color of the actionbar title
     */
    private void setTitleAlpha(float alpha) {
        mAlphaForegroundColorSpan.setAlpha(alpha);
        mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(),
                                 Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        getActivity().getActionBar().setTitle(mSpannableString);
    }

    /**
     * Get the scroll position of the listView
     */
    private int getScrollY() {
        //Returns the headerView of listView
        View view = mListView.getChildAt(0);
        if (view == null) {
            return 0;
        }

        //First visible item of the listView currently
        int firstVisiblePosition = mListView.getFirstVisiblePosition();

        // Y coordinate of the first child view with respect to its parent
        // Height offset from the top of the listView when the first visible
        // view is partially scroll out of view has to add back
        int top = view.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            //Height of the headerView when it's out of view
            headerHeight = mPlaceHolderView.getHeight();
        }

        return firstVisiblePosition * view.getHeight() + headerHeight - top;
    }

    /**
     * Clamping is the process of limiting a position to an area
     */
    private float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    /**
     * The two functions below are for the actionBar logo
     */
    private void interpolate(View view1, View view2, float interpolation) {
        getOnScreenRect(mRect1, view1);
        getOnScreenRect(mRect2, view2);

        float scaleX = 1.0f + interpolation * (mRect2.width() / mRect1.width() - 1.0f);
        float scaleY = 1.0f + interpolation * (mRect2.height() / mRect1.width() - 1.0f);
        float translationX = 0.5f * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
        float translationY = 0.5f * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

        view1.setTranslationX(translationX);
        view1.setTranslationY(translationY - mHeaderView.getTranslationY());
        view1.setScaleX(scaleX);
        view1.setScaleY(scaleY);
    }

    private RectF getOnScreenRect(RectF rect, View view) {
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }
}
