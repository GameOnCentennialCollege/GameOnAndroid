package com.comp313.gameonapp.java;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import com.comp313.gameonapp.model.CategoryModel;

import org.w3c.dom.Text;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by ganesh on 11/22/2016.
 */

public class CategoryAdapter extends ArrayAdapter {


    public List<CategoryModel> categoryModelList;
    private int resource;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context, int resource, List<CategoryModel> objects) {
        super(context, resource, objects);
        this.categoryModelList = objects;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return categoryModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categoryModelList.indexOf(getItem(position));
    }

    private class ViewHolder{
        TextView categoryname;
        TextView departmentname;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);

            holder = new ViewHolder();

            holder.categoryname = (TextView) convertView.findViewById(R.id.cname);
            holder.departmentname = (TextView) convertView.findViewById(R.id.dname);
            CategoryModel row_pos = categoryModelList.get(position);
            holder.categoryname.setText(row_pos.getCatname());
            holder.departmentname.setText(row_pos.getDeptname());
            convertView.setTag(holder);


        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

//        TextView cname;
//        TextView dname;
//
//        cname = (TextView) convertView.findViewById(R.id.cname);
//        //String catname = categoryModelList.get(position).getCatname();
//        cname.setText(categoryModelList.get(position).getCatname());

         return convertView;
    }


}