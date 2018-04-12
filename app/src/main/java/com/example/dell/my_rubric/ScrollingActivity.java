package com.example.dell.my_rubric;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    private SharedPreferences.Editor sh;
    private static final int PREFRENCE_MODE_PRIVATE=1;
    String videoUrl="https://api.themoviedb.org/3/movie/";
    String api_key = "2e3406cc89c1f748f4fa301db4816c47";
    String JSONresult, MovieName, year, rating, overview,url,id,YoutubeResponse;
    String YouUrl="https://www.youtube.com/watch?",MyYouUrl;
   // SharedPreference shpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // shpref=new SharedPreference();
        
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              SharedPreferences mh=getSharedPreferences("1",0);
                SharedPreferences.Editor editor=mh.edit();
                //sh= getPreferences(PREFRENCE_MODE_PRIVATE).edit();
                //List<String> list=
                editor.putString("MovieID",id).commit();

                    /*if(checkSet(id)){
                        //yes
                        Snackbar.make(view, "Already added", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else{
                        //no
                        shpref.addtoFav(ScrollingActivity.this,id);
                      */  Snackbar.make(view, "Added as a favourite", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    //}

              }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        JSONresult = i.getStringExtra("json");
        url=i.getStringExtra("url");
        Log.v("details",JSONresult+" "+JSONresult.toString());
        getDetails(i.getIntExtra("pos", 0));
    }
    /*public boolean checkSet(String s){
        boolean check=false;
        List<String > st=shpref.getFav(this);

        if(st!=null)
        {
            for(String fs:st)
            {
                if(fs.equals(s)){
                    check=true;
                    break;
                }
            }
        }
        return  check;
    }*/

    public class LoadingTask extends AsyncTask<Void, Void,Void> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //Uri.Builder builder = new Uri.Builder();
                //builder.authority(topMoviesUrl).appendQueryParameter("api_key", api_key).build();

                // Create the request to OpenWeatherMap, and open the connection
                URL url = new URL(videoUrl+id+"/videos?api_key=" + api_key);
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
                YoutubeResponse = buffer.toString();

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
                          Log.v("you",YoutubeResponse);
                        getUrls();
                    } catch (final IOException e) {
                        Log.e("response", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void strings) {
            ImageView tr=(ImageView)findViewById(R.id.trailer_img);
            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.v("yes",MyYouUrl);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MyYouUrl)));
                }
            });
            TextView tv=(TextView)findViewById(R.id.trailer);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MyYouUrl)));
                }
            });
        }
    }
    public Void getUrls(){
        try {
            JSONObject jsonObject=new JSONObject(YoutubeResponse);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            JSONObject r1=jsonArray.getJSONObject(0);
            MyYouUrl=YouUrl+"v="+r1.getString("key");
            Log.v("you",MyYouUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
        public void getDetails(int pos) {
        try {
            Log.v("pos", String.valueOf(pos));
            JSONObject jsonObject = new JSONObject(JSONresult);
            JSONArray result = jsonObject.getJSONArray("results");
            JSONObject obj = result.getJSONObject(pos);

            id=obj.getString("id");
            new LoadingTask().execute();
            MovieName = obj.getString("title");
            TextView mn=(TextView)findViewById(R.id.movieName);
            mn.setText(MovieName);
            year = obj.getString("release_date");
            TextView yeartv=(TextView)findViewById(R.id.year);
            yeartv.setText(year);
            ImageView i=(ImageView)findViewById(R.id.poster);
            Glide.with(this).load(url).into(i);
            rating = obj.getString("vote_average");
            TextView ratetv=(TextView)findViewById(R.id.rating);
            ratetv.setText("Rating : "+rating+"/10");
            overview = obj.getString("overview");
            TextView o=(TextView)findViewById(R.id.overview);
            o.setText(overview);
            //o.setMovementMethod(new ScrollingMovementMethod());
            Log.v("id"+id+"details",MovieName+" "+year+" "+rating+" "+overview);
            ImageView tr=(ImageView)findViewById(R.id.trailer_img);
            Glide.with(this).load("https://www.youtube.com/yt/brand/media/image/YouTube-icon-full_color.png").into(tr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
