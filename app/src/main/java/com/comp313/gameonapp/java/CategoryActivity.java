package com.comp313.gameonapp.java;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.comp313.gameonapp.json.JSONFunctions;
import com.comp313.gameonapp.model.CategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends AppCompatActivity {
    private ListView listofCategory;
    private CategoryAdapter padater;
    List<CategoryModel> catlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent = getIntent();
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_category);
//        layout.addView(textView);

        listofCategory = (ListView) findViewById(R.id.listcategory);
        // get the listview
        new JsonCategory().execute();
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
                break;
            case R.id.item_logout:
                break;
        }
        return true;
    }

    public class JsonCategory extends AsyncTask<String, String, List<CategoryModel>>  {
        protected List<CategoryModel> doInBackground(String... params) {
            try {
                JSONObject categoriesObj = JSONFunctions.getJSONfromURL("http://www.gameon.enigmacondos.ca/API/categories");
                JSONArray getcategoriesArray = categoriesObj.getJSONArray("category");
                try {
                    catlist = new ArrayList<>();
                    for (int i = 0; i < getcategoriesArray.length(); i++) {
                        JSONObject arrobj = getcategoriesArray.getJSONObject(i);
                        CategoryModel model = new CategoryModel();
                        model.setCatid(arrobj.getInt("CategoryID"));
                        model.setCatname(arrobj.getString("CategoryName"));
                        model.setDeptid(arrobj.getInt("DepartmentID"));
                        model.setDeptname(arrobj.getString("DepartmentName"));
                        catlist.add(model);
                    }
                    return catlist;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<CategoryModel> result) {
            super.onPostExecute(result);
            padater = new CategoryAdapter(getApplicationContext(), R.layout.categoryrow, result);
            listofCategory.setAdapter(padater);

            listofCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = catlist.get(position).getCatname();
                int categoryid = catlist.get(position).getCatid();
                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CategoryActivity.this, SubCategories.class);
                intent.putExtra("Categoryid",categoryid);
                startActivity(intent);
                }
            });

        }
    }
}
