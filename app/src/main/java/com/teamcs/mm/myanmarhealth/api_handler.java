package com.teamcs.mm.myanmarhealth;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface api_handler {
    /*
    Get request to fetch city weather.Takes in two parameter-city name and API key.
    */
    @GET("/blogger/blog_post_retrieve.php")
    Call< Edu_post > getStoryByLatestId(@Query("ops_type") String ops_type, @Query("last_post_id") int last_post_id);
}

