package com.teamcs.mm.myanmarhealth;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class networking {
    // http://teamcs.000webhostapp.com/blogger/blog_post_retrieve.php?ops_type=select&last_post_id=0
    // http://teamcs.000webhostapp.com/intakebook_std/api/v1
    //public static final String BASE_URL = "http://api.openweathermap.org";
    public static final String BASE_URL = "https://teamcs.000webhostapp.com"; // android 9 pie allow on only HTTPS
    public static Retrofit retrofit;
    /*
    This public static method will return Retrofit client
    anywhere in the appplication
    */
    public static Retrofit getRetrofitClient() {
        //If condition to ensure we don't create multiple retrofit instances in a single application
        if (retrofit == null) {
            //Defining the Retrofit using Builder
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) //This is the only mandatory call on Builder object.
                    .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                    .build();
        }
        return retrofit;
    }

}

