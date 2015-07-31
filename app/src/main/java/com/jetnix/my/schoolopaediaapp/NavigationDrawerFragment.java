package com.jetnix.my.schoolopaediaapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements MainActivityToDrawerFragmentCallback {

    private View containerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    TextView fragment_drawer_name_text_view, fragment_drawer_email_text_view;

    private String email = null;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        fragment_drawer_name_text_view = (TextView) view.findViewById(R.id.fragment_drawer_name_text_view);
        fragment_drawer_email_text_view = (TextView) view.findViewById(R.id.fragment_drawer_email_text_view);

        return view;
    }


    public void setUp(int fragment_navigation_drawer_id, DrawerLayout drawerLayout, Toolbar toolbar) {

        containerView = getActivity().findViewById(fragment_navigation_drawer_id);
        mDrawerLayout = drawerLayout;

        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

    }

    @Override
    public void sendData(ArrayList<String> list) {

        Log.v("result_email", list.get(0));
        fragment_drawer_email_text_view.setText(list.get(0));
    }
}
