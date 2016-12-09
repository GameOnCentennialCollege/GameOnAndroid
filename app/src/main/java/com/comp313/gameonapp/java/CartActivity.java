package com.comp313.gameonapp.java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comp313.gameonapp.model.ProductModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ListView listofprod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        listofprod = (ListView) findViewById(R.id.listprod);
        List<ProductModel> result = new ArrayList<>();
        SharedPreferences appSharedPrefs = getSharedPreferences("ProductsPref", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        String json = appSharedPrefs.getString("ProductsList", null);
        if (json != null) {
            Gson gson1 = new Gson();
            Type type = new TypeToken<List<ProductModel>>() {
            }.getType();
            result.addAll((ArrayList<ProductModel>) gson1.fromJson(json, type));
            CartActivity.ProductAdapter padater = new CartActivity.ProductAdapter(getApplicationContext(), R.layout.productrow, result);

            listofprod.setAdapter(padater);

            listofprod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                }
            });
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
            TextView pdesc;

            pimg = (ImageView) convertView.findViewById(R.id.pimg);
            pname = (TextView) convertView.findViewById(R.id.pname);
            pprice = (TextView) convertView.findViewById(R.id.pprice);
            ImageLoader.getInstance().displayImage(productModelsList.get(position).getImage(), pimg);
            pname.setText(productModelsList.get(position).getName());
            pprice.setText("Price: $" + productModelsList.get(position).getPrice());


            return convertView;
        }
    }


}
