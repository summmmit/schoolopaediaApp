package com.jetnix.my.schoolopaediaapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterSchoolActivity extends ActionBarActivity implements View.OnClickListener {

    EditText student_code_register_school, school_code_register_school;
    Button register_button_register_school;
    TextView error_text_register_school, student_group_id_register_school;

    private final Integer SET_VISIBILITY_GONE = 1;
    private final Integer SET_VISIBILITY_INVISIBLE = 2;
    private final Integer SET_VISIBILITY_VISIBLE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_school);

        student_code_register_school = (EditText) findViewById(R.id.student_code_register_school);
        school_code_register_school = (EditText) findViewById(R.id.school_code_register_school);

        register_button_register_school = (Button) findViewById(R.id.register_button_register_school);
        register_button_register_school.setOnClickListener(this);

        error_text_register_school = (TextView) findViewById(R.id.error_text_register_school);
        student_group_id_register_school = (TextView) findViewById(R.id.student_group_id_register_school);
    }

    private int validate_register_school(String school_code, String student_code){

        int response = 0;
        Log.v("school_code_aga", school_code);
        Log.v("student_codeasdg", student_code);

        if(student_code.isEmpty()){
            setErrorMessage("Please Insert Student Code", SET_VISIBILITY_VISIBLE);
            response = 1;
        }else if(school_code.isEmpty()){
            setErrorMessage("Please Insert School Code", SET_VISIBILITY_VISIBLE);
            response = 2;
        }
        return response;
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.register_button_register_school:
                String student_code = student_code_register_school.getText().toString();
                String school_code = school_code_register_school.getText().toString();
                String group_id = student_group_id_register_school.getText().toString();

                int validateRegistration = validate_register_school(school_code, student_code);

                if(validateRegistration == 0){
                    setErrorMessage(null, SET_VISIBILITY_GONE);

                    Schools schools = new Schools();
                    schools.setRegistration_code(school_code);
                    schools.setCode_for_students(student_code);

                    WelcomeSettingsRequests welcomeSettingsRequests = new WelcomeSettingsRequests(this);
                    welcomeSettingsRequests.validateSchoolBySchoolCodeAndStudentCode(schools, group_id, new GetUserCallback() {
                        @Override
                        public void done(String jsonString) {

                        }
                    });

                }
                break;
        }
    }
}
