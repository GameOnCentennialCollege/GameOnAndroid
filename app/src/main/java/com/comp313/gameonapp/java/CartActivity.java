package com.comp313.gameonapp.java;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    SharedPreferences preferences;
    JSONObject jsonUserObj = null;
    private ListView listofprod;
    ArrayList prodlist;
    private com.comp313.gameonapp.java.CartActivity.ProductAdapter prodadapter;

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId("Ae7Ou_2OQY_ygK3JgY1jPCYHtPofBW_n49-O8iAqn2x6lz2rFPTK5KGeVOFboPPMqxkDlwulkHbf1TXA");

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

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

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
            ImageLoader imgLoader = ImageLoader.getInstance();
            imgLoader.init(ImageLoaderConfiguration.createDefault(CartActivity.this));
            imgLoader.displayImage(productModelsList.get(position).getImage(), pimg);
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
                intent = new Intent(CartActivity.this, ProfileActivity.class);
                preferences = getSharedPreferences("UserPref",0);
                preferences.edit().clear();
                startActivity(intent);
                break;
            case R.id.item_category:
                intent = new Intent(CartActivity.this, CategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.item_logout:
                intent = new Intent(CartActivity.this, LoginActivity.class);
                preferences = getSharedPreferences("UserPref",0);
                preferences.edit().clear();
                startActivity(intent);
                break;
            case R.id.item_cart:
                intent = new Intent(CartActivity.this, CartActivity.class);
                startActivity(intent);
        }
        return true;
    }

    public void onCheckOutClick(View view){
        int amt = getTotalAmountToPay();
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amt)), "USD", "Purchase from GameOnApp",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, 0);
    }

    private int getTotalAmountToPay() {
        int amount = 0;
        for (Object item:prodlist) {
            ProductModel oneProd = (ProductModel)item;
            amount += oneProd.getPrice();
            Log.e("price", String.valueOf(oneProd.getId()) +" = " + oneProd.getPrice());
        }
        return amount;
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    Log.e("tag-note","This is Sandboxed Account");
                    Log.e("tag-payment",confirm.toString());
                    Toast.makeText(getApplicationContext(), "Your Payment has been made successfully", Toast.LENGTH_SHORT).show();
                    // TODO: send 'confirm' to your server for verification.
                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

}
