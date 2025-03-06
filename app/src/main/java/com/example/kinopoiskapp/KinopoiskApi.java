package com.example.kinopoiskapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface KinopoiskApi {
    /**
     * Поиск фильмов по ключевому слову.
     *
     * @param apiKey  API-ключ для доступа к API КиноПоиска.
     * @param keyword Ключевое слово для поиска фильмов.
     * @param page    Номер страницы (пагинация).
     * @return Ответ от API в виде объекта MovieResponse.
     */
    @GET("api/v2.1/films/search-by-keyword")
    Call<MovieResponse> searchMovies(
            @Header("X-API-KEY") String apiKey,
            @Query("keyword") String keyword,
            @Query("page") int page
    );
}