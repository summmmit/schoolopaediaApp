package com.jetnix.my.schoolopaediaapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ForgotPasswordActivity extends ActionBarActivity implements View.OnClickListener {

    EditText email_field;
    Button forgot_password_button;
    TextView login_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email_field = (EditText) findViewById(R.id.email_field);

        login_text_view = (TextView) findViewById(R.id.login_text_view);
        login_text_view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.login_text_view:
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
