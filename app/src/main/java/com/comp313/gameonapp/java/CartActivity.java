package com.comp313.gameonapp.java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comp313.gameonapp.model.ProductModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    JSONObject jsonUserObj = null;
    private ListView listofprod;
    ArrayList prodlist;
    private com.comp313.gameonapp.java.CartActivity.ProductAdapter prodadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        listofprod = (ListView) findViewById(R.id.listprod);
        SharedPreferences preferences = getSharedPreferences("UserPref",0);

        try {
            jsonUserObj = new JSONObject(((JSONArray)(new JSONObject(preferences.getString("user",null))).get("user")).getString(0));
            setUserObjectJSON(jsonUserObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<ProductModel> result = new ArrayList<>();
        try {
            new JsonThread().execute(getResources().getString(R.string.server_address)+"carts?user_id=" + jsonUserObj.getString("UserID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setUserObjectJSON(JSONObject jsonObject) {
        jsonUserObj = new JSONObject();
        try
        {
            jsonUserObj.put("UserID", jsonObject.get("UserID").toString());
            jsonUserObj.put("UserPassword", jsonObject.get("UserPassword").toString());
            jsonUserObj.put("UserName", jsonObject.get("UserName").toString());
            jsonUserObj.put("UserEmail", jsonObject.get("UserEmail").toString());
            jsonUserObj.put("UserPhone", jsonObject.get("UserPhone").toString());
        }catch (JSONException jEx){
            jEx.printStackTrace();
        }
    }


    public class ProductAdapter extends ArrayAdapter {

        public List<ProductModel> productModelsList;
        private int resource;
        private LayoutInflater inflater;

        public ProductAdapter(Context context, int resource, List<ProductModel> objects) {
            super(context, resource, objects);
            productModelsList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
            }
            ImageView pimg;
            TextView pname;
            TextView pprice;

            pimg = (ImageView) convertView.findViewById(R.id.pimg);
            pname = (TextView) convertView.findViewById(R.id.pname);
            pprice = (TextView) convertView.findViewById(R.id.pprice);
            ImageLoader.getInstance().displayImage(productModelsList.get(position).getImage(), pimg);
            pname.setText(productModelsList.get(position).getName());
            pprice.setText("Price: $" + productModelsList.get(position).getPrice());

            return convertView;
        }
    }

    public class  JsonThread extends AsyncTask<String, String, List<ProductModel>> {
        @Override
        protected List<ProductModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            URL url = null;
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String jobj = buffer.toString();

                JSONObject obj = new JSONObject(jobj);
                JSONArray categoriesArray = obj.getJSONArray("cart");


                prodlist = new ArrayList<>();
                for(int i=0; i<categoriesArray.length(); i++) {
                    JSONObject arrobj = categoriesArray.getJSONObject(i);
                    ProductModel model = new ProductModel();
                    model.setId(arrobj.getInt("ProductID"));
                    model.setName(arrobj.getString("ProductName"));
                    model.setDescription(arrobj.getString("ProductDescription"));
                    model.setPrice(arrobj.getInt("ProductPrice"));
                    model.setSub(arrobj.getInt("ProductSubCategoryID"));
                    model.setImage(arrobj.getString("ProductThumbnail"));
                    prodlist.add(model);
                }
                return prodlist;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }


        @Override
        protected void onPostExecute(final List<ProductModel> result) {
            super.onPostExecute(result);

            prodadapter = new com.comp313.gameonapp.java.CartActivity.ProductAdapter (getApplicationContext(), R.layout.productrow, result);
            listofprod.setAdapter(prodadapter);

            listofprod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), ProductDescription.class);
                    System.out.println(result.get(position).getId());
                    intent.putExtra("Product", result.get(position));
                    startActivity(intent);
                }
            });

        }
    }


}
