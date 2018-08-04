package com.example.ngenge.popularmovies.utils;


import android.content.Context;
import android.net.ConnectivityManager;

import com.example.ngenge.popularmovies.models.Movie;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
    public static String movieKey = "movie";
    public static String basePopularUrl = "https://api.themoviedb.org/3/movie/popular?api_key=YOUR_API_KEY";
    public static String baseTopRatedUrl = "https://api.themoviedb.org/3/movie/top_rated?api_key=YOUR_API_KEY";
    public static String baseImageUrl = "https://image.tmdb.org/t/p/w185";
    public static String vote_count_key = "vote_count";
    public static String id_key = "id";
    public static String vote_avg_key = "vote_average";
    public static String title_key = "title";

    public static String overview_key ="overview";
    public static String poster_path_key = "poster_path";

    public static String release_date_key ="release_date";



    public static Movie getMovieFromJsonObject(JSONObject jsonObject)
    {
        try {


            int id = jsonObject.getInt(id_key);
            String poster_path = jsonObject.getString(poster_path_key);
            String release_date = jsonObject.getString(release_date_key);
            double vote_average = jsonObject.getDouble(vote_avg_key);
            int vote_count = jsonObject.getInt(vote_count_key);
            String title = jsonObject.getString(title_key);

            String overview = jsonObject.getString(overview_key);
            return new Movie(id,title,release_date,vote_average,vote_count,poster_path,overview);



        }
        catch (JSONException ex)
        {
            System.out.println(ex.getMessage());

        }


        return null;
        }




}
