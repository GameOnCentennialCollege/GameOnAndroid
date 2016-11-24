package com.comp313.gameonapp.java;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.comp313.gameonapp.json.JSONFunctions;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void register(View view){
        EditText editId = (EditText) findViewById(R.id.tbUserId);
        EditText editPassword = (EditText) findViewById(R.id.tbUserPassword);
        EditText editUserName = (EditText) findViewById(R.id.tbUserName);
        EditText editEmail = (EditText) findViewById(R.id.tbUserEmail);
        EditText editPhone = (EditText) findViewById(R.id.tbUserPhone);

        String userid= editId.getText().toString();
        String password = editPassword.getText().toString();
        String username = editPassword.getText().toString();
        String email = editPassword.getText().toString();
        String phone = editPassword.getText().toString();

        JSONObject jsonObject = new JSONObject();
        //String requestURL = getResources().getString(R.string.server_address)+ "login";

        new JSONRegister().execute(userid,password,username,email, phone);
    }

    public class JSONRegister extends AsyncTask<String ,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            String requestURL = getResources().getString(R.string.server_address)+ "userdetails/register";
            String json = "";
            JSONObject objJson = null;
            try {
                objJson = new JSONObject();
                objJson.put("userid",params[0]);
                objJson.put("userpassword",params[1]);
                objJson.put("username",params[2]);
                objJson.put("useremail",params[3]);
                objJson.put("userphone",params[4]);
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
            Intent intent = new Intent(RegisterActivity.this, CategoryActivity.class);
            intent.putExtra("username",s);

            startActivity(intent);
        }
    }
}
