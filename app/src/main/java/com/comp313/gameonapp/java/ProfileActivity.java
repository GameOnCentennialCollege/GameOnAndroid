package com.comp313.gameonapp.java;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    JSONObject jsonObject, userObject = null;
    SharedPreferences preferences;
    EditText etId, etName, etPassword, etPhone, etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etId = (EditText)findViewById(R.id.tbPUserId);
        etName = (EditText)findViewById(R.id.tbPUserName);
        etPassword = (EditText)findViewById(R.id.tbPUserPassword);
        etPhone = (EditText)findViewById(R.id.tbPUserPhone);
        etEmail = (EditText)findViewById(R.id.tbPUserEmail);

        SharedPreferences preferences = getSharedPreferences("UserPref",0);

        try {
            jsonObject = new JSONObject(((JSONArray)(new JSONObject(preferences.getString("user",null))).get("user")).getString(0));
            setUserObjectJSON(jsonObject);
            setTextBoxValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUserObjectJSON(JSONObject jsonObject) {
        userObject = new JSONObject();
        try
        {
            userObject.put("UserID", jsonObject.get("UserID").toString());
            userObject.put("UserPassword", jsonObject.get("UserPassword").toString());
            userObject.put("UserName", jsonObject.get("UserName").toString());
            userObject.put("UserEmail", jsonObject.get("UserEmail").toString());
            userObject.put("UserPhone", jsonObject.get("UserPhone").toString());
        }catch (JSONException jEx){
            jEx.printStackTrace();
        }
    }

    private void setTextBoxValues() throws JSONException{
        etId.setText(userObject.getString("UserID"));
        etName.setText(userObject.getString("UserName"));
        etPassword.setText(userObject.getString("UserPassword"));
        etEmail.setText(userObject.getString("UserEmail"));
        etPhone.setText(userObject.getString("UserPhone"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.item_profile:
                intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                preferences = getSharedPreferences("UserPref",0);
                preferences.edit().clear();
                startActivity(intent);
                break;
            case R.id.item_category:
                intent = new Intent(ProfileActivity.this, CategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.item_logout:
                intent = new Intent(ProfileActivity.this, LoginActivity.class);
                preferences = getSharedPreferences("UserPref",0);
                preferences.edit().clear();
                startActivity(intent);
                break;
            case R.id.item_cart:
                intent = new Intent(ProfileActivity.this, CartActivity.class);
                startActivity(intent);
        }
        return true;
    }
}
