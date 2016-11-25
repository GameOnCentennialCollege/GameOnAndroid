package com.comp313.gameonapp.java;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.comp313.gameonapp.json.JSONFunctions;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    LinearLayout layout;
    Button loginbtn;
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
        loginbtn.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        EditText editEmail = (EditText) findViewById(R.id.etEmail);
        EditText editPassword = (EditText) findViewById(R.id.etPassword);

        String username= editEmail.getText().toString();
        String password = editPassword.getText().toString();

        JSONObject jsonObject = new JSONObject();
        //String requestURL = getResources().getString(R.string.server_address)+ "login";

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

            //return userObj.toString();
            return jsonUser.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            layout.setVisibility(View.GONE);
            Intent intent = new Intent(LoginActivity.this, CategoryActivity.class);
            intent.putExtra("username",s);

            startActivity(intent);
        }
    }

}
