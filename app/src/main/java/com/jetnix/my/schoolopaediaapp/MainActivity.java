package com.jetnix.my.schoolopaediaapp;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    Toolbar toolbar;
    UserLocalStore userLocalStore;

    TextView main_activity_text_view;
    NavigationDrawerFragment navigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLocalStore = new UserLocalStore(this);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        main_activity_text_view = (TextView) findViewById(R.id.main_activity_text_view);

        navigationDrawerFragment = (NavigationDrawerFragment)  getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        navigationDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.main_activity_layout), toolbar);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Integer user_id = checkforLogin();
        Integer id = new Integer(-1);
        if(user_id.equals(id) == true){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }else{
            String email = userLocalStore.getLoggedInEmailAddress();
            ArrayList<String> list = new ArrayList<String>();
            list.add(email);
            changeData(email);
            navigationDrawerFragment.showUserDataOfDrawer(email);
        }
    }

    private void changeData(String email) {

        main_activity_text_view.setText(email);
    }

    private Integer checkforLogin() {

        Integer user_id = userLocalStore.getLoggedInUserId();
        return user_id;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = null;
        switch (id){
            case R.id.action_login:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.action_forgot_password:
                intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.action_register:
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.action_logout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.action_register_school:
                intent = new Intent(MainActivity.this, RegisterSchoolActivity.class);
                startActivity(intent);
                break;
            case R.id.action_brief_registration:
                intent = new Intent(MainActivity.this, BriefRegistrationActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
