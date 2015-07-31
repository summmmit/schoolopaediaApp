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

            ArrayList<String> list = new ArrayList<>();

            try {
                jsonObject = new JSONObject(JsonResult);

                if(jsonObject.length() !=0 ){

                    String status = jsonObject.getString("status");

                    String failed = new String("failed");
                    String success = new String("success");
                    String emailAddress = null;
                    Integer user_id = -1;

                    if(status.equals(failed)){
                        String error = jsonObject.getString("error");
                        JSONObject errorObject = jsonObject.getJSONObject("error");
                        String errorDescription = errorObject.getString("description");
                        response = errorDescription;
                    }else if(status.equals(success)){

                        String result = jsonObject.getString("result");
                        JSONObject resultObject = jsonObject.getJSONObject("result");
                        user_id = resultObject.getInt("user_id");
                        JSONObject requestObject = jsonObject.getJSONObject("request");
                        emailAddress = requestObject.getString("email");

                        response = success;
                    }
                    list.add(user_id.toString());
                    list.add(emailAddress);
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
