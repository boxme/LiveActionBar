package com.replaid.liveactionbar.app.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.replaid.liveactionbar.app.NavigationDrawerActivity;
import com.replaid.liveactionbar.app.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ChoicesFragment extends Fragment {
    private static final String TAG = "ChoicesFragment";

    public static ChoicesFragment newInstance() {
        return new ChoicesFragment();
    }

    public ChoicesFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choices, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button listViewButton = (Button) view.findViewById(R.id.listViewActionBarButton);
        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fragmentContainer, ListViewLiveActionBarFragment.newInstance()).commit();
            }
        });

        Button scrollViewButton = (Button) view.findViewById(R.id.scrollViewActionBarButton);
        scrollViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fragmentContainer, LiveActionBarWithScrollView.newInstance()).commit();
            }
        });

        Button withNavigationDrawer = (Button) view.findViewById(R.id.scrollViewActionBarWithNavigationDrawerButton);
        withNavigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NavigationDrawerActivity.class);
                startActivity(intent);
            }
        });
    }
}
