package com.jetnix.my.schoolopaediaapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    EditText email_field, password_field;
    TextView forgot_password_text_view, login_error_text;
    Button sign_in_button, facebook_button, google_button;

    private final Integer SET_VISIBILITY_GONE = 1;
    private final Integer SET_VISIBILITY_INVISIBLE = 2;
    private final Integer SET_VISIBILITY_VISIBLE = 3;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_field = (EditText) findViewById(R.id.email_field);
        password_field = (EditText) findViewById(R.id.password_field);

        forgot_password_text_view = (TextView) findViewById(R.id.forgot_password_text_view);
        forgot_password_text_view.setOnClickListener(this);

        sign_in_button = (Button) findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(this);

        login_error_text = (TextView) findViewById(R.id.login_error_text);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgot_password_text_view:
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_in_button:

                String email = email_field.getText().toString();
                String password = password_field.getText().toString();

                boolean validateEmail = validateEmailAddress(email);

                if(validateEmail){

                    Users users = new Users(email, password);
                    authenticateUser(users);

                    break;
                }else{
                    setErrorMessage("Email is not Valid", SET_VISIBILITY_VISIBLE);
                }
        }
    }

    private void authenticateUser(Users users) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchDataInBackground(users, new GetUserDataFromRequest() {
            @Override
            public void getData(ArrayList<String> list) {
                String user_id = list.get(0);
                String email = list.get(1);
                String response = list.get(2);
                String id = new String("-1");

                if(user_id.equals(id)){
                    setErrorMessage(response, SET_VISIBILITY_VISIBLE);
                }else{
                    Log.d("email", email);
                    Log.d("user_id", user_id);
                    Log.d("response", response);
                    logUserIn(Integer.parseInt(user_id), email);
                }

            }
        });
    }

    private void logUserIn(Integer user_id, String email){
        userLocalStore.storeUserData(user_id, email);
        userLocalStore.setUserLoggedIn(true);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private boolean validateEmailAddress(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void setErrorMessage(String text, Integer Visibility){

        login_error_text.setText(text);

        if(Visibility.equals(SET_VISIBILITY_GONE)){
            login_error_text.setVisibility(View.GONE);
        }else if (Visibility.equals(SET_VISIBILITY_VISIBLE)){
            login_error_text.setVisibility(View.VISIBLE);
        }else if(Visibility.equals(SET_VISIBILITY_INVISIBLE)){
            login_error_text.setVisibility(View.INVISIBLE);
        }

    }

}
