package com.example.dell.my_rubric;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    String id,JSONURL,commonURL="https://api.themoviedb.org/3/movie/";
    String api_key="2e3406cc89c1f748f4fa301db4816c47";
    String imgurl = "http://image.tmdb.org/t/p/w154/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        id=getSharedPreferences("1",0).getString("MovieID","2");
        Log.v("url",getSharedPreferences("1",0).getString("MovieID","2"));
        setContentView(R.layout.fav_section);
        ListAdapterHelper[] l=getData();
        ListView l2=(ListView)findViewById(R.id.listview);
        l2.setAdapter(new ListAdapter(this,Arrays.asList(l)));
        l2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FavouritesActivity.this,position+" ",Toast.LENGTH_SHORT);
                Log.v("url","hello");

            }
        });
    }


    public ListAdapterHelper[] getData(){
        ListAdapterHelper[] l=new ListAdapterHelper[]{
                new ListAdapterHelper("Interstellar","5.6","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
                new ListAdapterHelper("Interstellar","5.6","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
                new ListAdapterHelper("Interstellar","5.6","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
                new ListAdapterHelper("Interstellar","5.6","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
                new ListAdapterHelper("Interstellar","5.6","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
                new ListAdapterHelper("Interstellar","5.6","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
                new ListAdapterHelper("Interstellar","5.6","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg")
        };
        return  l;
    }

    public class LoadingTask extends AsyncTask<String, Void, Void> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        @Override
        protected Void doInBackground(String... params) {
            try {
                //Uri.Builder builder = new Uri.Builder();
                //builder.authority(topMoviesUrl).appendQueryParameter("api_key", api_key).build();

                // Create the request to OpenWeatherMap, and open the connection
                //URL url = new URL(commonUrl+"?api_key="+api_key);
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                JSONURL = buffer.toString();

            } catch (IOException e) {
                Log.e("Respones", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                        Log.v("response", JSONURL);
                      } catch (final IOException e) {
                        Log.e("response", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void p) {
            try {
                JSONObject jsonObject=new JSONObject(JSONURL);
                String poster_path=jsonObject.getString("poster_path");

                //ImageView i=(ImageView)findViewById(R.id.Poster);
                //Glide.with(getApplicationContext()).load(imgurl+poster_path).into(i);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    }

