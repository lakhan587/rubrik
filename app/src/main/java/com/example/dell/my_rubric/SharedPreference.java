package com.example.dell.my_rubric;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by Dell on 02-Jun-16.
 */
public class SharedPreference {
    final static  String pref_name="MYLIST";
    final static String pref_settings="MyDataList";

    public  SharedPreference(){
        super();
    }

    public void saveFav(Context context, ArrayList<String> fav){
        SharedPreferences sharedPreference=context.getSharedPreferences(pref_name,0);
        SharedPreferences.Editor editor=sharedPreference.edit();
        Gson gson=new Gson();
        String jsonFav=gson.toJson(fav);
        editor.putString(pref_settings,jsonFav);
        editor.commit();
    }

    public void addtoFav(Context context,String s){
        ArrayList<String > stringArrayList=getFav(context);
        if(stringArrayList==null)
            stringArrayList=new ArrayList<String>();
        stringArrayList.add(s);
        saveFav(context,stringArrayList);
    }

    public void remove(Context context,String s){
        ArrayList<String > stringArrayList=getFav(context);
        if(stringArrayList!=null){
            stringArrayList.remove(s);
            saveFav(context,stringArrayList);
        }
    }

    public ArrayList<String > getFav(Context context){
        SharedPreferences sharedPreferences;
        List<String> fav;
        sharedPreferences=context.getSharedPreferences(pref_name,0);

        if(sharedPreferences.contains(pref_settings)){
            String jsonfav=sharedPreferences.getString(pref_settings,null);
            Log.v("red",jsonfav);
            Gson gson=new Gson();
            String string=gson.fromJson(jsonfav,String.class);
            fav= Arrays.asList(string);
            fav=new ArrayList<String >(fav);

        }else{
            return null;
        }
        return (ArrayList<String >) fav;
    }
}
