package com.comp313.gameonapp.json;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONFunctions {
    public static JSONObject getJSONfromURL(String url){
        InputStream is = null;
        String result = "";
        JSONObject objJson = null;

        //http post
        try{
            URL objURL = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) objURL.openConnection();
            try{
//                urlConnection.setDoInput(true);
//                urlConnection.setDoOutput(true);
//                urlConnection.setRequestMethod("POST");
                is=urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                //convert response to string
                result=stringBuilder.toString();
                urlConnection.disconnect();
            }
            catch(IOException e){
                Log.e("log_tag", "Error converting result "+e.toString());
            }
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }

        try{
            //objJson = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
            if(result!=""){
                objJson = new JSONObject(result);
            }
        }catch(JSONException exJson){
            Log.e("log_tag", "Error parsing data "+exJson.toString());
        }

        return objJson;
    }

    public static JSONArray getJSONArrayfromURL(String url) throws JSONException{
        InputStream is = null;
        String result = "";
        JSONArray arrJson = null;

        //http post
        try{
            URL objURL = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) objURL.openConnection();
            try{
                is=urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                //convert response to string
                result=stringBuilder.toString();
                urlConnection.disconnect();
            }
            catch(IOException e){
                Log.e("log_tag", "Error converting result "+e.toString());
            }
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }

        try{
            //objJson = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
            if(result!=""){
                arrJson = new JSONArray(result);
            }
        }catch(JSONException exJson){
            Log.e("log_tag", "Error parsing data "+exJson.toString());
        }

        return arrJson;
    }

    public static JSONObject sendJSONToURL(String requestURL, String json) {
        InputStream is = null;
        String result = "";
        JSONObject objJson = null;

        HttpURLConnection urlConnection = null;

        //http post
        try{
            URL objURL = new URL(requestURL);
            urlConnection = (HttpURLConnection) objURL.openConnection();
            urlConnection.setRequestProperty("Content-Type","application/json");

            //urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.connect();
            OutputStreamWriter os = new OutputStreamWriter(urlConnection.getOutputStream());
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            os.write(json);

            os.flush();
            os.close();

            boolean res = urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK;

            try{
                if(res){
                    is = urlConnection.getInputStream();// is is inputstream
                } else {
                    is = urlConnection.getErrorStream();
                }
                //is=urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                    Log.e("xx", line);
                }
                bufferedReader.close();
                //convert response to string
                result=stringBuilder.toString();
            }
            catch(Exception e){
                Log.e("log_tag", urlConnection.getErrorStream().toString());
                //Log.e("log_tag", "Error converting result "+e.toString());
            }
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }
        finally {
            urlConnection.disconnect();
        }

        try{
            //objJson = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
            if(result!=""){
                objJson = new JSONObject(result);
            }
        }catch(JSONException exJson){
            Log.e("log_tag", "Error parsing data "+exJson.toString());
        }

        return objJson;
    }

}
