package com.example.dell.my_rubric;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Dell on 01-Jun-16.
 */
public class ListAdapter extends ArrayAdapter<ListAdapterHelper> {


    public ListAdapter(Context context, List<ListAdapterHelper> list){
        super(context,0, list);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListAdapterHelper listAdapterHelper=getItem(position);
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_adapter,parent,false);
        }
        ImageView i=(ImageView)convertView.findViewById(R.id.Mposter);
        String url=listAdapterHelper.url;
        Glide.with(getContext()).load(url).centerCrop().into(i);

        TextView tv1,tv2;
        tv1=(TextView)convertView.findViewById(R.id.mname);
        tv2=(TextView)convertView.findViewById(R.id.rate);
        tv1.setText(listAdapterHelper.mname);
        tv2.setText(listAdapterHelper.rating+"/10");

        Log.v("hello2",url);
        return convertView;

    }
}
