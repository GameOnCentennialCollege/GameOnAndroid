package com.comp313.gameonapp.java;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comp313.gameonapp.model.ProductModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

public class ProductActivity extends AppCompatActivity {

    private ListView listofprod;
    int subcategoryId;
    List<ProductModel> prodlist;
    ProductAdapter prodadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        subcategoryId = intent.getIntExtra("SubCategoryid",0);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .defaultDisplayImageOptions(defaultOptions)
        .build();
        ImageLoader.getInstance().init(config);

        listofprod = (ListView) findViewById(R.id.listprod);

        new JsonThread().execute(getResources().getString(R.string.server_address)+"products/getproducts");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_profile:
                break;
            case R.id.item_category:
                Intent intent = new Intent(ProductActivity.this, CategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.item_logout:
                break;
        }
        return true;
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
                JSONArray categoriesArray = obj.getJSONArray("product");


                prodlist = new ArrayList<>();
                for(int i=0; i<categoriesArray.length(); i++) {

                    JSONObject arrobj = categoriesArray.getJSONObject(i);
                    if(arrobj.getInt("ProductSubCategoryID") == subcategoryId) {
                        ProductModel model = new ProductModel();
                        model.setId(arrobj.getInt("ProductID"));
                        model.setName(arrobj.getString("ProductName"));
                        model.setDescription(arrobj.getString("ProductDescription"));
                        model.setPrice(arrobj.getInt("ProductPrice"));
                        model.setSub(arrobj.getInt("ProductSubCategoryID"));
                        model.setImage(arrobj.getString("ProductThumbnail"));


                        prodlist.add(model);
                    }
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

            prodadapter = new ProductAdapter(getApplicationContext(), R.layout.productrow, result);
            listofprod.setAdapter(prodadapter);

            listofprod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = prodlist.get(position).getName();
                    int prodid = prodlist.get(position).getId();
                    Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ProductActivity.this, SubCategories.class);
                    intent.putExtra("Productid",prodid);
                    startActivity(intent);
                }
            });


        }
    }




}


