package com.jetnix.my.schoolopaediaapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    private View containerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    TextView fragment_drawer_name_text_view, fragment_drawer_email_text_view;

    private DrawerAdapter drawerAdapter;

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
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);

        drawerAdapter = new DrawerAdapter(getActivity(), getData());

        recyclerView.setAdapter(drawerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Intent intent;
                switch (position){
                    case 0:
                        intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), ProfileActivity.class);
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                Intent intent;
                switch (position){
                    case 0:
                        intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), ProfileActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        }));

        fragment_drawer_name_text_view = (TextView) view.findViewById(R.id.fragment_drawer_name_text_view);
        fragment_drawer_email_text_view = (TextView) view.findViewById(R.id.fragment_drawer_email_text_view);

        return view;
    }

    public static List<DrawerDataLayout> getData() {
        //load only static data inside a drawer
        List<DrawerDataLayout> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_inbox, R.drawable.ic_font_download_black_24dp, R.drawable.ic_archive_black_24dp, R.drawable.ic_mail_black_24dp};
        String[] titles = {"Home", "Profile", "Slidenerd", "YouTube"};
        for (int i = 0; i < icons.length && i<titles.length; i++) {
            DrawerDataLayout current = new DrawerDataLayout();
            current.rowIconId = icons[i % icons.length];
            current.rowTitle = titles[i % titles.length];
            data.add(current);
        }
        return data;
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

    public void showUserDataOfDrawer(String email) {
        fragment_drawer_email_text_view.setText(email);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){

            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if(child!=null && clickListener!=null){
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());

            if(child!=null && clickListener!=null){
                clickListener.onLongClick(child, rv.getChildPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    }

    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }
}
