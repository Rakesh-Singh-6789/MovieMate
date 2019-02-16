package com.example.rakeshsingh.moviemate.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatter {


    public static String getFormattedDurationTime(int duration) {
        String hour, min, formatterDuration;
        hour = String.valueOf(duration / 60);
        min = String.valueOf(duration % 60);

        formatterDuration = hour + "hr " + min + "min";

        return formatterDuration;
    }

    public static String getFormattedReleaseDate(Date date) {
        String formattedDate;

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM, yyyy", Locale.getDefault());
        formattedDate = dateFormat.format(date);

        return formattedDate;
    }



}
