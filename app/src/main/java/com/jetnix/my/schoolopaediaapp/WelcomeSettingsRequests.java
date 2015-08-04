package com.jetnix.my.schoolopaediaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by 1084760 on 2015/08/04.
 */
public class WelcomeSettingsRequests {

    ProgressDialog progressDialog;

    public WelcomeSettingsRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing..........");
        progressDialog.setMessage("Validating Your School............");
        progressDialog.setCancelable(false);
    }

    public void validateSchoolBySchoolCodeAndStudentCode(Schools schools, String group_id, GetUserCallback getUserCallback){
        Log.v("school", schools+"");
        Log.v("groupod", group_id);

        new ValidationSchool(schools, group_id, getUserCallback).execute();
    }

    public class ValidationSchool extends AsyncTask<Void ,Void, Void>{

        Schools schools;
        GetUserCallback getUserCallback;
        String group_id;

        public ValidationSchool(Schools schools, String group_id, GetUserCallback getUserCallback) {
            this.schools = schools;
            this.group_id = group_id;
            this.getUserCallback = getUserCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            String link= "https://10.0.2.2/projects/schoolopaedia/public/mobile/user/school/validation";
            String data = null;

            Log.v("schoolor_students()", schools.getCode_for_students()+"");

            Log.v("schools.getCode", schools.getCode_for_students()+"");

            try {
                data = URLEncoder.encode("school_code", "UTF-8") + "=" + URLEncoder.encode(schools.getRegistration_code(), "UTF-8");
                data += "&" + URLEncoder.encode("student_code", "UTF-8") + "=" + URLEncoder.encode(schools.getCode_for_students(), "UTF-8");
                data += "&" + URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.v("data", data+"");

            StringBuilder sb = new StringBuilder();
            String line = null;

            try {

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.v("sb", sb+"");


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }


}
