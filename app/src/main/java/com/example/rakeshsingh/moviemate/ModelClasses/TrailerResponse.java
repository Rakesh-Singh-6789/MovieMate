package com.example.rakeshsingh.moviemate.ModelClasses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TrailerResponse {


        @SerializedName("results")
        private ArrayList<TrailerResult> trailers;

        public ArrayList<TrailerResult> getResults() {
            return trailers;
        }
    }


