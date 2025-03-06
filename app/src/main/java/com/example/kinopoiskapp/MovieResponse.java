package com.example.kinopoiskapp;

import java.util.List;

public class MovieResponse {
    private String keyword;
    private int pagesCount;
    private List<Movie> films;

    public String getKeyword() {
        return keyword;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public List<Movie> getFilms() {
        return films;
    }

    public static class Movie {
        private int filmId;
        private String nameRu;
        private String nameEn;
        private String year;
        private String filmLength;
        private List<Country> countries;
        private List<Genre> genres;
        private String rating;
        private int ratingVoteCount;
        private String posterUrl;
        private String posterUrlPreview;
        private String description;

        public int getFilmId() {
            return filmId;
        }

        public String getNameRu() {
            return nameRu;
        }

        public String getNameEn() {
            return nameEn;
        }

        public String getYear() {
            return year;
        }

        public String getFilmLength() {
            return filmLength;
        }

        public List<Country> getCountries() {
            return countries;
        }

        public List<Genre> getGenres() {
            return genres;
        }

        public String getRating() {
            return rating;
        }

        public int getRatingVoteCount() {
            return ratingVoteCount;
        }

        public String getPosterUrl() {
            return posterUrl;
        }

        public String getPosterUrlPreview() {
            return posterUrlPreview;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class Country {
        private String country;

        public String getCountry() {
            return country;
        }
    }

    public static class Genre {
        private String genre;

        public String getGenre() {
            return genre;
        }
    }
}