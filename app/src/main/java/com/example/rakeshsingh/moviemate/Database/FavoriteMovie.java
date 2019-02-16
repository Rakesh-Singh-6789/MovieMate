package com.example.rakeshsingh.moviemate.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;


@Entity(tableName="tableFavoriteMovies")
public class FavoriteMovie {

        @PrimaryKey
        private int id;
        private String title;

        @TypeConverters({DateConverter.class})
        private Date releaseDate;

        private Integer vote;
        private Double popularity;
        private String synopsis;
        private String image;
        private String backdrop;

        public FavoriteMovie(int id, String title, Date releaseDate, Integer vote, Double popularity, String synopsis, String image, String backdrop) {
            this.id = id;
            this.title = title;
            this.releaseDate = releaseDate;
            this.vote = vote;
            this.popularity = popularity;
            this.synopsis = synopsis;
            this.image = image;
            this.backdrop = backdrop;
        }

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }

        public Date getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(Date releaseDate) {
            this.releaseDate = releaseDate;
        }

        public Integer getVote() {
            return vote;
        }
        public void setVote(String synopsis) {
            this.vote = vote;
        }

        public Double getPopularity() {
            return popularity;
        }
        public void setPopularity(String synopsis) {
            this.popularity = popularity;
        }

        public String getSynopsis() {
            return synopsis;
        }
        public void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }

        public String getImage() {
            return image;
        }
        public void setImage(String image) {
            this.image = image;
        }

        public String getBackdrop() {
            return backdrop;
        }
        public void setBackdrop(String backdrop) {
            this.backdrop = backdrop;
        }


    }

