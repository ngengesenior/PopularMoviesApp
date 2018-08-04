package com.example.ngenge.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.ngenge.popularmovies.adapters.MoviesAdapter;
import com.example.ngenge.popularmovies.models.Movie;
import com.example.ngenge.popularmovies.utils.GridDividerItemDecoration;
import com.example.ngenge.popularmovies.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private GridDividerItemDecoration portratDecoration;
    private GridDividerItemDecoration landscapeDecoration;


    private static MoviesAdapter adapter = null;
    private GridLayoutManager manager;
    private CoordinatorLayout coordinatorLayout;


private static RecyclerView movieList;
private static List<Movie> movies;
private static Context context;
private static MoviesAdapter.OnItemClickListener listener;

private String url;
private String URL_KEY = "urlKey";
private String recPositionKey = "position";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = findViewById(R.id.constraintLayout);
        movies = new ArrayList<>();
        if(savedInstanceState != null)
        {
            url = savedInstanceState.getString(URL_KEY);

        }

        else {
            url = Utils.basePopularUrl;
        }
        context = MainActivity.this;
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(!isNetworkConnected())
        {
            Snackbar.make(coordinatorLayout,getResources().getString(R.string.no_internet),Snackbar.LENGTH_LONG)
                    .show();
            return;
        }

        landscapeDecoration = new GridDividerItemDecoration(getResources().getDrawable(R.drawable.divider_horizontal),getResources().getDrawable(R.drawable.divider),3);
        movieList = findViewById(R.id.movieList);
        portratDecoration = new GridDividerItemDecoration(getResources().getDrawable(R.drawable.divider_horizontal),getResources().getDrawable(R.drawable.divider),2);

        movieList.setHasFixedSize(true);

        callOnClickListener();
        new MyTask().execute(url);






    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {


            url = Utils.basePopularUrl;
            if(isNetworkConnected())
            {
                new MyTask().execute(Utils.basePopularUrl);
                adapter.notifyDataSetChanged();

            }

            else {


            }

            return true;
        }

       else if(id == R.id.action_top_rated)
        {
            url = Utils.baseTopRatedUrl;

            if(isNetworkConnected())
            {
                new MyTask().execute(Utils.baseTopRatedUrl);
                adapter.notifyDataSetChanged();

            }


            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString(URL_KEY,url);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(isNetworkConnected()){
            new MyTask().execute(url);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            movieList.addItemDecoration(landscapeDecoration);
        }

        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            movieList.addItemDecoration(portratDecoration);
        }
    }


    private void callOnClickListener()
    {
        listener = new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Movie movie) {

                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);

             intent.putExtra(Utils.movieKey,movie);
              startActivity(intent);

            }
        };
    }


    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!isNetworkConnected())
        {

            Snackbar.make(coordinatorLayout,getResources().getString(R.string.no_internet),Snackbar.LENGTH_LONG)
                    .show();
            return;
        }
    }


   static class MyTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder results = new StringBuilder();
            HttpURLConnection con = null;
            

            try {
                URL url = new URL(params[0]);
                 con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                //Get output with buffered reader
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;

                while ((line=reader.readLine()) != null)
                {
                    results.append(line);

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            finally {
                if(con!=null)
                {
                    con.disconnect();
                }
            }

            return results.toString();
        }


        @Override
        protected void onPostExecute(String s) {

            ArrayList<Movie> movies = new ArrayList<>();
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray array = jsonObject.getJSONArray("results");

                for(int i=0;i<array.length(); i++)
                {
                    JSONObject movieItem = array.getJSONObject(i);

                    Movie movie = Utils.getMovieFromJsonObject(movieItem);
                    //Log.d("MOVIE",movie.toString());
                    movies.add(movie);

                    adapter = new MoviesAdapter(movies,context,listener);
                    movieList.setAdapter(adapter);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}