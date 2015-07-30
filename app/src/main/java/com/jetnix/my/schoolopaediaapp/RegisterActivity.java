package com.jetnix.my.schoolopaediaapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class RegisterActivity extends ActionBarActivity implements View.OnClickListener {

    EditText email_field, password_field, confirm_password_field;
    Button register_button;
    TextView login_text_view, register_error_text;

    private final Integer SET_VISIBILITY_GONE = 1;
    private final Integer SET_VISIBILITY_INVISIBLE = 2;
    private final Integer SET_VISIBILITY_VISIBLE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_field = (EditText) findViewById(R.id.email_field);
        password_field = (EditText) findViewById(R.id.password_field);
        confirm_password_field = (EditText) findViewById(R.id.confirm_password_field);

        register_button = (Button) findViewById(R.id.register_button);
        login_text_view = (TextView) findViewById(R.id.login_text_view);

        register_error_text = (TextView) findViewById(R.id.register_error_text);

        login_text_view.setOnClickListener(this);
        register_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.login_text_view:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.register_button:

                String email = email_field.getText().toString();
                String password = password_field.getText().toString();
                String password_again = confirm_password_field.getText().toString();

                Boolean validateEmail = validateEmailAddress(email);

                if(validateEmail == false){
                    setErrorMessage("Email Address is not Valid", SET_VISIBILITY_VISIBLE);
                    break;
                }else{

                    Boolean checkPassword = checkPassword(password, password_again);

                    if(checkPassword){

                        Users users = new Users(email, password, password_again);
                        registerUser(users);
                    }
                    break;
                }
        }
    }

    private boolean checkPassword(String password, String password_again){

        if(password.equals(password_again)){

            if(password.length() <= 6){
                setErrorMessage("Password Should Be more than 6 letters", SET_VISIBILITY_VISIBLE);
            }else{
                return true;
            }

        }else{
            setErrorMessage("Password and Password Again Should Match", SET_VISIBILITY_VISIBLE);
        }

        return false;

    }

    private void setErrorMessage(String text, Integer Visibility){

        register_error_text.setText(text);

        if(Visibility.equals(SET_VISIBILITY_GONE)){
            register_error_text.setVisibility(View.GONE);
        }else if (Visibility.equals(SET_VISIBILITY_VISIBLE)){
            register_error_text.setVisibility(View.VISIBLE);
        }else if(Visibility.equals(SET_VISIBILITY_INVISIBLE)){
            register_error_text.setVisibility(View.INVISIBLE);
        }

    }

    private boolean validateEmailAddress(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void registerUser(Users users) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(users, new GetUserCallback() {
            @Override
            public void done(String response) {
                String registered = new String("registered");
                if (response.equals(registered)){
                    setErrorMessage(null, SET_VISIBILITY_GONE);

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);

                }else{
                    setErrorMessage(response, SET_VISIBILITY_VISIBLE);
                }

            }
        });
    }
}
