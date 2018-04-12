package com.example.dell.my_rubric;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Created by Dell on 01-Jun-16.
 */
public class ListAdapterHelper {
    ImageView imageView;
    String mname,rating,url;

    public ListAdapterHelper(String name, String rating, String url){
        this.mname=name;
        this.rating=rating;
        this.url=url;
    }
}

