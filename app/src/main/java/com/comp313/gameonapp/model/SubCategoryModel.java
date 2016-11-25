package com.comp313.gameonapp.model;

/**
 * Created by ganesh on 11/24/2016.
 */

public class SubCategoryModel {
    private int catid;
    private String catname;
    private int subcatid;
    private String subcatname;

    public SubCategoryModel(int catid, String catname, int subcatid, String subcatname){
        this.catid = catid;
        this.catname = catname;
        this.subcatid = subcatid;
        this.subcatname = subcatname;
    }

    public SubCategoryModel() {

    }



    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getSubcatname() {
        return subcatname;
    }

    public void setSubcatname(String subcatname) {
        this.subcatname = subcatname;
    }

    public int getSubcatid() {
        return subcatid;
    }

    public void setSubcatid(int subcatid) {
        this.subcatid = subcatid;
    }






}
