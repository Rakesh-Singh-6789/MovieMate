package com.example.rakeshsingh.moviemate.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class TrailerResult {


    @SerializedName("key")
    private String youtubeKey;
    @SerializedName("name")
    private String name;

    public String getYoutubeKey() {
        return youtubeKey;
    }

    public String getName() {
        return name;
    }
}
