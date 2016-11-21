package com.comp313.gameonapp.java;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.BufferUnderflowException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.comp313.gameonapp.model.ProductModel;
import com.comp313.gameonapp.json.JSONFunctions;
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

   /* before changes
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_product);

        try{
            String requestURL = getResources().getString(R.string.server_address)+ "getproducts.aspx";
            JSONArray jsonArray = JSONFunctions.getJSONArrayfromURL(requestURL);
            for(int i=0; i< jsonArray.length();i++) {
                JSONObject one = jsonArray.getJSONObject(i);
            }
        }
        catch (Exception ex){
            Log.d("product_tag",ex.toString());
        }
    }
}*/
    private ListView listofprod;

    //private TextView tvdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .defaultDisplayImageOptions(defaultOptions)
        .build();
        ImageLoader.getInstance().init(config);

        listofprod = (ListView) findViewById(R.id.listprod);

        Button getinfo = (Button) findViewById(R.id.get);
        //tvdata = (TextView) findViewById(R.id.jsonitem);

        getinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonThread().execute("http://www.gameon.enigmacondos.ca/API/products/getproducts");
            }
        });
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
                //StringBuffer resultdata = new StringBuffer();


                List<ProductModel> prodlist = new ArrayList<>();
                for(int i=0; i<categoriesArray.length(); i++) {
                    JSONObject arrobj = categoriesArray.getJSONObject(i);
                    ProductModel model = new ProductModel();
                    model.setId(arrobj.getInt("ProductID"));
                    model.setName(arrobj.getString("ProductName"));
                    model.setDescription(arrobj.getString("ProductDescription"));
                    model.setPrice(arrobj.getInt("ProductPrice"));
                    model.setSub(arrobj.getInt("ProductSubCategoryID"));
                    //model.setImage(arrobj.getString("ProductThumbnail"));
                    model.setImage(arrobj.getString("ProductThumbnail"));


                    /*int productID = arrobj.getInt("ProductID");
                    String productName = arrobj.getString("ProductName");
                    String prodesc = arrobj.getString("ProductDescription");
                    String price = arrobj.getString("ProductPrice");
                    String subcat = arrobj.getString("ProductSubCategoryID");
                    String img = arrobj.getString("ProductThumbnail");*/
                    //resultdata.append(productID + " \n " + productName + " \n " + prodesc + " \n " + price + " \n " + subcat + " \n " + img+"\n\n");
                    //resultdata.append(productID + " \n " + productName + " \n " );
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
        protected void onPostExecute(List<ProductModel> result) {
            super.onPostExecute(result);
            //tvdata.setText(result);
            ProductAdapter padater = new ProductAdapter(getApplicationContext(), R.layout.productrow, result);

            listofprod.setAdapter(padater);


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
            if(convertView == null){
                convertView =inflater.inflate(resource,null);
            }
            ImageView pimg;
            TextView pname;
            TextView pprice;
            TextView pdesc;

            pimg = (ImageView) convertView.findViewById(R.id.pimg);
            pname = (TextView) convertView.findViewById(R.id.pname);
            pprice = (TextView) convertView.findViewById(R.id.pprice);
           // pdesc = (TextView) convertView.findViewById(R.id.pdesc);

            //byte[] b = productModelsList.get(position).getImage().getBytes();
            //InputStream stream = new ByteArrayInputStream(productModelsList.get(position).getImage().getBytes());
            ImageLoader.getInstance().displayImage(productModelsList.get(position).getImage(), pimg);
            //Bitmap bmp = BitmapFactory.decodeFile(productModelsList.get(position).getImage());
            //Bitmap bmp = BitmapFactory.decodeStream(stream);
            //pimg.setImageBitmap(bmp);
            pname.setText(productModelsList.get(position).getName());
            pprice.setText("Price: "+ productModelsList.get(position).getPrice());
           // pdesc.setText(productModelsList.get(position).getDescription());

            return convertView;
        }
    }


}


