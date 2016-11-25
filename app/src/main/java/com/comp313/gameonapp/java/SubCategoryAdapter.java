package com.comp313.gameonapp.java;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import com.comp313.gameonapp.model.SubCategoryModel;

import org.w3c.dom.Text;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
/**
 * Created by ganesh on 11/24/2016.
 */

public class SubCategoryAdapter extends ArrayAdapter {

    public List<SubCategoryModel> subcategoryModelList;
    private int resource;
    private LayoutInflater inflater;

    public SubCategoryAdapter(Context context, int resource, List<SubCategoryModel> objects) {
        super(context, resource, objects);
        this.subcategoryModelList = objects;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return subcategoryModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return subcategoryModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return subcategoryModelList.indexOf(getItem(position));
    }

    private class ViewHolder{
       // TextView categoryname;
        TextView subcategoryname;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SubCategoryAdapter.ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);

            holder = new SubCategoryAdapter.ViewHolder();

           // holder.categoryname = (TextView) convertView.findViewById(R.id.cname);
            holder.subcategoryname = (TextView) convertView.findViewById(R.id.subcname);
            SubCategoryModel row_pos = subcategoryModelList.get(position);
           // holder.categoryname.setText(row_pos.getCatname());
            holder.subcategoryname.setText(row_pos.getSubcatname());
            convertView.setTag(holder);


        }
        else {
            holder = (SubCategoryAdapter.ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}
