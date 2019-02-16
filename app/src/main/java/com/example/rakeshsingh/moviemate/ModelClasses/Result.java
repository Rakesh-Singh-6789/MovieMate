
package com.example.rakeshsingh.moviemate.ModelClasses;

import java.util.Date;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private Date releaseDate;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private float voteAverage;
    @SerializedName("runtime")
    @Expose
    private int duration;

      public Result(String posterPath, Boolean adult, String overview, Date releaseDate, List<Integer> genreIds,
                    Integer id, String originalTitle, String originalLanguage, String title, String backdropPath,
                    Double popularity, Integer voteCount, Boolean video, float voteAverage,int duration) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
        this.duration=duration;
    }

    public Result(Integer id, String originalTitle, Date releaseDate, Integer voteCount, Double popularity, String overview , String posterPath, String backdropPath ) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.originalTitle = originalTitle;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
    }


    public String getPosterPath() {
        String BASE_MOVIE_POSTER_URL = "https://image.tmdb.org/t/p/w342/";
        return BASE_MOVIE_POSTER_URL+posterPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public Integer getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        String BASE_MOVIE_BACKDROP_URL = "https://image.tmdb.org/t/p/w780/";
        return BASE_MOVIE_BACKDROP_URL + backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public int getDuration(){ return duration; }
}
