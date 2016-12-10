package com.comp313.gameonapp.java;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.comp313.gameonapp.json.JSONFunctions;
import com.comp313.gameonapp.model.ProductModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductDescription extends Activity {

    JSONObject jsonUserObj= null;
    private List<ProductModel> productList;
    Spinner spinnersize,spinnercolor;
    ArrayList<String> sizelist;
    ArrayList<String> colorlist;
    ArrayList<String> stocklist;
    int pid;
    Button btnAddCart, btncart;
    String stockid;
    int sizeisvalid, colorisvalid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences preferences = getSharedPreferences("UserPref",0);

        try {
            jsonUserObj = new JSONObject(((JSONArray)(new JSONObject(preferences.getString("user",null))).get("user")).getString(0));
            setUserObjectJSON(jsonUserObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button btnAddCart = (Button) findViewById(R.id.btnAddCart);
        btncart = (Button) findViewById(R.id.btnCart);
        btncart.setVisibility(View.GONE);
        ImageView iv = (ImageView) findViewById(R.id.imgProduct);
        TextView lblPrice = (TextView) findViewById(R.id.lblPrice);
        TextView lblDescription = (TextView) findViewById(R.id.lblDescription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ProductModel product = (ProductModel) getIntent().getSerializableExtra("Product");

        toolbar.setTitle(product.getName());
        productList = new ArrayList<ProductModel>();
        pid = product.getId();
        try {
            JSONObject sizeObj = JSONFunctions.getJSONfromURL("http://www.gameon.enigmacondos.ca/API/Sizes/product/"+pid);
            JSONArray getsizeArray = sizeObj.getJSONArray("size");
            try {
                sizelist = new ArrayList<String>();
                for (int i = 0; i < getsizeArray.length(); i++) {
                    JSONObject row = getsizeArray.getJSONObject(i);
                    String sizename = row.getString("SizeName");
                    int sizeid = row.getInt("SizeID");
                    sizename = sizeid + "-" + sizename;
                    sizelist.add(sizename);

                }
            }
            catch (JSONException e){

                e.printStackTrace();
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        try {
            JSONObject colorObj = JSONFunctions.getJSONfromURL("http://www.gameon.enigmacondos.ca/API/Colors/product/"+pid);
            JSONArray getcolorArray = colorObj.getJSONArray("color");
            try {
                colorlist = new ArrayList<String>();
                for (int i = 0; i < getcolorArray.length(); i++) {
                    JSONObject colorrow = getcolorArray.getJSONObject(i);
                    String colorname = colorrow.getString("ColorName");
                    int colorid = colorrow.getInt("ColorID");
                    colorname = colorid + "-" + colorname;
                    colorlist.add(colorname);
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        spinnersize = (Spinner) findViewById(R.id.sizespinner);
        if(sizelist != null && sizelist.size()>0) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sizelist);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinnersize.setAdapter(spinnerArrayAdapter);
            sizeisvalid = 1;
        }
        else{
            sizeisvalid = 0;
            spinnersize.setVisibility(View.GONE);
        }

        spinnercolor = (Spinner) findViewById(R.id.colorspinner);
        if(colorlist != null && colorlist.size()>0) {
        ArrayAdapter<String> spinnerArrayColorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colorlist);
        spinnerArrayColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnercolor.setAdapter(spinnerArrayColorAdapter);
            colorisvalid = 1;
        }
        else{
            colorisvalid = 0;
            spinnercolor.setVisibility(View.GONE);
        }

        lblPrice.setText("Price : $" + product.getPrice());
        lblDescription.setText("Description : \n" + product.getDescription());

        ImageLoader.getInstance().displayImage(product.getImage(), iv);

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sizeisvalid ==1 && colorisvalid==1) {
                    String sizeid = spinnersize.getSelectedItem().toString();
                    String[] sizeiddetails = sizeid.split("-");
                    String sizeforprod = sizeiddetails[0];

                    String colorid = spinnercolor.getSelectedItem().toString();
                    String[] coloriddetails = colorid.split("-");
                    String colorforprod = coloriddetails[0];

                    try {
                        JSONObject stockobj = JSONFunctions.getJSONfromURL("http://www.gameon.enigmacondos.ca/API/Stocks/GetStock?product_id=" + pid + "&color_id=" + colorforprod + "&size_id=" + sizeforprod);
                        JSONArray getStockArray = stockobj.getJSONArray("stock");
                        if (getStockArray != null && getStockArray.length() > 0) {
                            stocklist = new ArrayList<String>();
                            for (int i = 0; i < getStockArray.length(); i++) {
                                JSONObject stockrow = getStockArray.getJSONObject(i);
                                int availqty = stockrow.getInt("StockQuantityAvailable");
                                stockid = stockrow.getString("StockID");
                                stocklist.add(stockid);
                            }
                            btncart.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getApplicationContext(), "Out of Stock", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sold Out", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String requestURL ="http://gameon.enigmacondos.ca/Api/Carts/PostCart";
                String json = "";
                JSONObject objJson = null;
                try {
                    objJson = new JSONObject();
                    objJson.put("CartStockID",stockid);
                    objJson.put("CartQuantity",1);
                    objJson.put("CartQuantity",1);
                    objJson.put("CartUnitCost",product.getPrice());
                    objJson.put("CartClientID",jsonUserObj.getString("UserID"));
                    json= objJson.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject jsoncart = JSONFunctions.sendJSONToURL(requestURL, json);
                Toast.makeText(getApplicationContext(), "Item added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

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
}
