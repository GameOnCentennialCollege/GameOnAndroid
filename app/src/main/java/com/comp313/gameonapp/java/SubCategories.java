package com.comp313.gameonapp.java;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.comp313.gameonapp.json.JSONFunctions;
import com.comp313.gameonapp.model.SubCategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubCategories extends AppCompatActivity {

    private ListView listofSubCategory;
    private SubCategoryAdapter sadapter;
    List<SubCategoryModel> subcatlist;
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);

        Intent intent = getIntent();
        String message = intent.getStringExtra("username");
        categoryId = intent.getIntExtra("Categoryid",0);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_sub_categories);

        listofSubCategory = (ListView) findViewById(R.id.listsubcategory);
        new JsonSubCategory().execute();
    }

    public class JsonSubCategory extends AsyncTask<String, String, List<SubCategoryModel>> {
        protected List<SubCategoryModel> doInBackground(String... params) {
            try {
                JSONObject categoriesObj = JSONFunctions.getJSONfromURL("http://gameon.enigmacondos.ca/Api/SubCategories/category/" + categoryId);
                JSONArray getcategoriesArray = categoriesObj.getJSONArray("SubCategory");
                try {
                    subcatlist = new ArrayList<>();
                    for (int i = 0; i < getcategoriesArray.length(); i++) {
                        JSONObject arrobj = getcategoriesArray.getJSONObject(i);
                        SubCategoryModel model = new SubCategoryModel();
                        model.setCatid(arrobj.getInt("CategoryID"));
                        model.setCatname(arrobj.getString("categoryname"));
                        model.setSubcatid(arrobj.getInt("SubCategoryID"));
                        model.setSubcatname(arrobj.getString("SubCategoryName"));
                        subcatlist.add(model);
                    }
                    return subcatlist;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<SubCategoryModel> result) {
            super.onPostExecute(result);
            sadapter = new SubCategoryAdapter(getApplicationContext(), R.layout.subcategoryrow, result);
            listofSubCategory.setAdapter(sadapter);


            listofSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //String item = ((String) listofCategory.getItemAtPosition(position));
                    String item = subcatlist.get(position).getSubcatname();
                   int subcategoryid = subcatlist.get(position).getSubcatid();
                    Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SubCategories.this, ProductActivity.class);
                    intent.putExtra("SubCategoryid",subcategoryid);
                    startActivity(intent);
                }
            });

        }
    }
}
