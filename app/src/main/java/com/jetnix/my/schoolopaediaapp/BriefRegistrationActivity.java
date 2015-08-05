package com.jetnix.my.schoolopaediaapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jetnix.my.schoolopaediaapp.DatabaseClasses.UserDetails;


public class BriefRegistrationActivity extends ActionBarActivity implements View.OnClickListener {

    private RadioGroup sex_radio_group_brief_registration;
    private EditText first_name_brief_registration, last_name_brief_registration;
    private RadioButton radio_sex_button, male_radio_button, female_radio_button;
    private Button register_button_brief_registration;
    private TextView error_text_register_school;

    private final Integer SET_VISIBILITY_GONE = 1;
    private final Integer SET_VISIBILITY_INVISIBLE = 2;
    private final Integer SET_VISIBILITY_VISIBLE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_registration);

        sex_radio_group_brief_registration = (RadioGroup) findViewById(R.id.sex_radio_group_brief_registration);
        male_radio_button = (RadioButton) findViewById(R.id.male_radio_button_brief_registration);
        female_radio_button = (RadioButton) findViewById(R.id.female_radio_button_brief_registration);

        first_name_brief_registration = (EditText) findViewById(R.id.first_name_brief_registration);
        last_name_brief_registration = (EditText) findViewById(R.id.last_name_brief_registration);

        error_text_register_school = (TextView) findViewById(R.id.error_text_register_school);

        register_button_brief_registration = (Button) findViewById(R.id.register_button_brief_registration);
        register_button_brief_registration.setOnClickListener(this);
    }

    private void setErrorMessage(String text, Integer Visibility){

        error_text_register_school.setText(text);

        if(Visibility.equals(SET_VISIBILITY_GONE)){
            error_text_register_school.setVisibility(View.GONE);
        }else if (Visibility.equals(SET_VISIBILITY_VISIBLE)){
            error_text_register_school.setVisibility(View.VISIBLE);
        }else if(Visibility.equals(SET_VISIBILITY_INVISIBLE)){
            error_text_register_school.setVisibility(View.INVISIBLE);
        }

    }

    private int validateBriefRegistration(String first_name, String last_name, int sex){

        Log.v("sex here", sex+"");

        int response = 0;

        if(first_name.isEmpty()){
            setErrorMessage("Please Insert First Name", SET_VISIBILITY_VISIBLE);
            response = 1;
        }else if(last_name.isEmpty()){
            setErrorMessage("Please Insert Last Name", SET_VISIBILITY_VISIBLE);
            response = 2;
        }else if(sex == -1){
            setErrorMessage("Please Select Your Sex", SET_VISIBILITY_VISIBLE);
            response = 3;
        }
        return response;
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.register_button_brief_registration:

                String first_name = first_name_brief_registration.getText().toString();
                String last_name = last_name_brief_registration.getText().toString();

                int sex_radio_button_selected_id = sex_radio_group_brief_registration.getCheckedRadioButtonId();

                int sex = -1;
                if(sex_radio_button_selected_id == female_radio_button.getId()){
                    sex = 1;
                }else if(sex_radio_button_selected_id == male_radio_button.getId()){
                    sex = 0;
                }

                radio_sex_button = (RadioButton) findViewById(sex_radio_button_selected_id);

                int validateBriefRegistration = validateBriefRegistration(first_name, last_name, sex);

                if(validateBriefRegistration == 0){
                    setErrorMessage(null, SET_VISIBILITY_GONE);
                    Toast.makeText(this, radio_sex_button.getText(), Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
