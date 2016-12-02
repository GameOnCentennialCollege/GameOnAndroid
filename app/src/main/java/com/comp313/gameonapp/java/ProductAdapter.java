package com.comp313.gameonapp.java;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comp313.gameonapp.model.ProductModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by ganesh on 12/2/2016.
 */

public class ProductAdapter extends ArrayAdapter {

    public List<ProductModel> productModelsList;
    private int resource;
    private LayoutInflater inflater;
    public ProductAdapter(Context context, int resource, List<ProductModel> objects) {
        super(context, resource, objects);
        productModelsList = objects;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return productModelsList.size();
    }

    @Override
    public Object getItem(int position) {
        return productModelsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productModelsList.indexOf(getItem(position));
    }

    private class ViewHolder{
        TextView productname;
        TextView productprice;

        ImageView prodimg;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ProductAdapter.ViewHolder productholder = null;
        if(convertView == null) {
            convertView = inflater.inflate(resource, null);


            productholder = new ProductAdapter.ViewHolder();

            productholder.productname = (TextView) convertView.findViewById(R.id.pname);
            productholder.productprice = (TextView) convertView.findViewById(R.id.pprice);
            productholder.prodimg = (ImageView) convertView.findViewById(R.id.pimg);


            ProductModel pos = productModelsList.get(position);

            productholder.productname.setText(pos.getName());
            productholder.productprice.setText("Price: $"+pos.getPrice());
            ImageLoader.getInstance().displayImage(productModelsList.get(position).getImage(), productholder.prodimg);
            convertView.setTag(productholder);
        }
        else {
            productholder = (ProductAdapter.ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}