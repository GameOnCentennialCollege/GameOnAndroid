package com.comp313.gameonapp.java;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.comp313.gameonapp.json.JSONFunctions;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    LinearLayout layout;
    Button loginbtn;
    EditText editPassword, editEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layout = (LinearLayout) findViewById(R.id.progressbar_login);
        layout.setVisibility(View.GONE);
        loginbtn = (Button) findViewById(R.id.btnLogin);
    }

    public void sendContent(View view)
    {
        editEmail = (EditText) findViewById(R.id.etEmail);
        editPassword = (EditText) findViewById(R.id.etPassword);
        loginbtn.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        editEmail = (EditText) findViewById(R.id.etEmail);
        editPassword = (EditText) findViewById(R.id.etPassword);

        String username= editEmail.getText().toString();
        String password = editPassword.getText().toString();

        new JSONLogin().execute(username,password);
    }

    public class JSONLogin extends AsyncTask<String ,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            String requestURL = getResources().getString(R.string.server_address)+ "userdetails/login";
            String json = "";
            JSONObject objJson = null;
            try {
                objJson = new JSONObject();
                objJson.put("userid",params[0]);
                objJson.put("userpassword",params[1]);
                json= objJson.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonUser = JSONFunctions.sendJSONToURL(requestURL, json);
            try {
                if(jsonUser.getJSONArray("user").length() >0) {
                    SharedPreferences preferences = getSharedPreferences("UserPref",0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user",jsonUser.toString());
                    editor.commit();
                    return jsonUser.toString();
                }
                else
                    return "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //return jsonUser.toString();
            return "";
        }

        @Override
        protected void onPostExecute(String userJsonString) {
            super.onPostExecute(userJsonString);
            layout.setVisibility(View.GONE);
            if(userJsonString != ""){
                Intent intent = new Intent(LoginActivity.this, CategoryActivity.class);
                intent.putExtra("username",userJsonString);
                startActivity(intent);
            }
            else{
                loginbtn.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                editPassword.setError("Invalid Credentials Provided");
            }

        }
    }

}
