package com.jetnix.my.schoolopaediaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by 1084760 on 2015/07/29.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://10.0.2.2/projects/android/AndroidToPhpConnect/";

    public ServerRequests(Context context){

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(Users user, GetUserCallback userCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchDataInBackground(Users user, GetUserDataFromRequest getUserDataFromRequest){
        progressDialog.show();
        new FetchDataAsyncTask(user, getUserDataFromRequest).execute();
    }

    public void validateSchoolByCodes(Schools schools, String group_id, GetUserCallback getUserCallback){
        new ValidatingSchool(schools, group_id, getUserCallback).execute();
    }

    public class ValidatingSchool extends AsyncTask<Void , Void, Void>{

        Schools schools;
        GetUserCallback getUserCallback;
        String group_id;

        public ValidatingSchool(Schools schools, String group_id, GetUserCallback getUserCallback) {
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
    }

    public class FetchDataAsyncTask extends AsyncTask<Void, Void, ArrayList<String>>{

        Users users;
        GetUserDataFromRequest getUserDataFromRequest;

        public FetchDataAsyncTask(Users user, GetUserDataFromRequest getUserDataFromRequest) {
            this.users = user;
            this.getUserDataFromRequest = getUserDataFromRequest;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params){

            String link= "http://10.0.2.2/projects/schoolopaedia/public/mobile/user/sign/in/post";
            String data  = null;
            try {
                data = URLEncoder.encode("identity", "UTF-8") + "=" + URLEncoder.encode(users.email, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(users.password, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.v("data_send", data);

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

            String JsonResult = sb.toString();
            JSONObject jsonObject = null;
            String response = null;

            ArrayList<String> list = new ArrayList<>();

            try {
                jsonObject = new JSONObject(JsonResult);

                if(jsonObject.length() !=0 ){

                    String status = jsonObject.getString("status");

                    String failed = new String("failed");
                    String success = new String("success");
                    String emailAddress = null;
                    Integer user_id = -1;
                    Integer login_flag = -1;

                    if(status.equals(failed)){
                        String error = jsonObject.getString("error");
                        JSONObject errorObject = jsonObject.getJSONObject("error");
                        String errorDescription = errorObject.getString("description");
                        response = errorDescription;
                    }else if(status.equals(success)){

                        String result = jsonObject.getString("result");
                        JSONObject resultObject = jsonObject.getJSONObject("result");
                        JSONObject userObject = resultObject.getJSONObject("user");

                        login_flag = resultObject.getInt("login_flag");
                        user_id = userObject.getInt("id");
                        JSONObject requestObject = jsonObject.getJSONObject("request");
                        emailAddress = requestObject.getString("email");

                        response = success;
                    }
                    list.add(user_id.toString());
                    list.add(emailAddress);
                    Log.v("resutl", list + "");
                    Log.v("login_flag", login_flag+"");
                    Log.v("user_id", user_id+"");
                    list.add(response);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<String> response) {
            super.onPostExecute(response);
            Log.v("restl", response+"");
            progressDialog.dismiss();
            getUserDataFromRequest.getData(response);
        }
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, String> {

        Users users;
        GetUserCallback getUserCallback;

        public StoreUserDataAsyncTask(Users users, GetUserCallback userCallBack) {
            this.users = users;
            this.getUserCallback = userCallBack;
        }

        @Override
        protected String doInBackground(Void... params) {

            String link= "http://10.0.2.2/projects/schoolopaedia/public/mobile/user/account/create/post";
            String data  = null;
            try {
                data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(users.email, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(users.password, "UTF-8");
                data += "&" + URLEncoder.encode("password_again", "UTF-8") + "=" + URLEncoder.encode(users.password_again, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


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

            String JsonResult = sb.toString();
            JSONObject jsonObject = null;
            String response = null;

            try {
                jsonObject = new JSONObject(JsonResult);

                if(jsonObject.length() !=0 ){

                    String status = jsonObject.getString("status");

                    String failed = new String("failed");
                    String success = new String("success");

                    if(status.equals(failed)){
                        String error = jsonObject.getString("error");
                        JSONObject errorObject = jsonObject.getJSONObject("error");
                        String errorDescription = errorObject.getString("description");
                        response = errorDescription;
                    }else if(status.equals(success)){
                        response = "registered";
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            progressDialog.dismiss();
            getUserCallback.done(response);
        }
    }
}
