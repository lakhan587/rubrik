package com.example.dell.my_rubric;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Dell on 29-May-16.
 */
public class ImageAdapter extends ArrayAdapter<String> {

    public ImageAdapter(Activity context, List<String> urls) {
        super(context,0, urls);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String url=getItem(position);
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.image_class,parent,false);
        }
        ImageView i=(ImageView)convertView.findViewById(R.id.myImageView);
        Glide.with(getContext()).load(url).centerCrop().into(i);
      //  Log.v("hello2",url);
        return convertView;
    }
}
