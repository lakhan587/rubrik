package com.example.dell.my_rubric;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SharedPreferences.OnSharedPreferenceChangeListener {

    String api_key = "2e3406cc89c1f748f4fa301db4816c47";
    String imgurl = "http://image.tmdb.org/t/p/w154/";
    String topMoviesUrl = "https://api.themoviedb.org/3/movie/top_rated";
    String PopMoviesUrl = "https://api.themoviedb.org/3/movie/popular";
    String commonUrl;
    private SwipeRefreshLayout swipeRefreshLayout;
    String[] url;
    public static String JSONresult;
    GridView gridView;
    int preferencesChanged=0;
    private ImageAdapter mImageAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences.OnSharedPreferenceChangeListener sp= new SharedPreferences.OnSharedPreferenceChangeListener()
        {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.v("pref","changed");
            }
        };
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        LoadUrls(commonUrl+"?api_key="+api_key);
                                    }
                                }
        );
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        String type=sharedPreferences.getString("sortBy","top_rated");
        if(type.equals("top_rated")){
            commonUrl=topMoviesUrl;
        }else{
            commonUrl=PopMoviesUrl;
        }
        LoadUrls(commonUrl+"?api_key="+api_key);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                    startActivity(new Intent(MainActivity.this,FavouritesActivity.class));
                }
            });
        }
        url= new String[]{"http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"};
        gridView = (GridView) findViewById(R.id.gridview);;
        if (gridView != null) {
            mImageAdapter=new ImageAdapter(this,Arrays.asList(url));
            gridView.setAdapter(mImageAdapter);

        }
    //    LoadUrls();

    }
    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        LoadUrls(commonUrl+"?api_key="+api_key);
    }

    public void LoadUrls(String s) {

        swipeRefreshLayout.setRefreshing(true);
        new LoadingTask().execute(s);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String type=sharedPreferences.getString(key,"top_rated");
        if(type.equals("top_rated")){
            commonUrl=topMoviesUrl;
        }else{
            commonUrl=PopMoviesUrl;
        }
        LoadUrls(commonUrl+"?api_key="+api_key);

    }


    public class LoadingTask extends AsyncTask<String, Void, String[]> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        @Override
        protected String[] doInBackground(String... params) {
            try {
                //Uri.Builder builder = new Uri.Builder();
                //builder.authority(topMoviesUrl).appendQueryParameter("api_key", api_key).build();

                // Create the request to OpenWeatherMap, and open the connection
                //URL url = new URL(commonUrl+"?api_key="+api_key);
                URL url=new URL(params[0]);
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
                JSONresult = buffer.toString();

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
                        Log.v("response",JSONresult);
                        try {
                            return getURLS();

                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    } catch (final IOException e) {
                        Log.e("response", "Error closing stream", e);

                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String[] urls) {
           // mImageAdapter.clear();
           // mImageAdapter.notifyDataSetChanged();
           if(urls!=null)
           {
               // stopping swipe refresh
               swipeRefreshLayout.setRefreshing(false);
               ImageView i=(ImageView)findViewById(R.id.myImageView);
//               Display display=((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//               int screenWidth = display.getWidth();
//               int screenHeight = display.getHeight();
//               i.getLayoutParams().height=screenHeight/2;
//               i.getLayoutParams().width=screenWidth/2;
               refreshAdapter();
               gridView.setAdapter(new ImageAdapter(MainActivity.this,Arrays.asList(urls)));
               gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(MainActivity.this,ScrollingActivity.class).putExtra("pos",position).putExtra("json",JSONresult).putExtra("url",urls[position]));
                       //Toast.makeText(MainActivity.this,position+"",Toast.LENGTH_SHORT).show();
                   }
               });
           }
        }
    }

public void refreshAdapter()
{
    gridView.setAdapter(null);
}
    public String[] getURLS() throws JSONException{

        JSONObject jsonObject=new JSONObject(JSONresult);
        JSONArray jsonArray=jsonObject.getJSONArray("results");
        String[] url=new String[jsonArray.length()];
        for (int i=0;i<jsonArray.length();i++)
        {
            JSONObject obj=jsonArray.getJSONObject(i);
            url[i]=imgurl+obj.getString("poster_path");
        }
        return url;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i=new Intent(this,SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
