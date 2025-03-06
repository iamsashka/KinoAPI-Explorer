package com.example.kinopoiskapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "KinopoiskApp";
    private EditText movieInput;
    private Button searchButton;
    private TextView movieInfo;
    private ImageView moviePoster;

    private static final String API_KEY = "bc6cc148-6816-4ea1-8b3f-18271de84ff5";
    private static final String BASE_URL = "https://kinopoiskapiunofficial.tech/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieInput = findViewById(R.id.movieInput);
        movieInfo = findViewById(R.id.movieInfo);
        moviePoster = findViewById(R.id.moviePoster);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> searchMovies(movieInput.getText().toString()));
    }

    private void searchMovies(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KinopoiskApi api = retrofit.create(KinopoiskApi.class);
        Call<MovieResponse> call = api.searchMovies(API_KEY, query, 1);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MovieResponse.Movie> movies = response.body().getFilms();
                    if (movies != null && !movies.isEmpty()) {
                        displayMovieInfo(movies.get(0)); // Отображаем первый фильм из списка
                    } else {
                        movieInfo.setText("Фильмы не найдены.");
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "Ошибка: " + errorBody);
                    } catch (IOException e) {
                        Log.e(TAG, "Ошибка при чтении тела ответа", e);
                    }
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, "Ошибка сети: " + t.getMessage());
                movieInfo.setText("Ошибка сети: " + t.getMessage());
            }
        });
    }

    private void handleErrorResponse(Response<MovieResponse> response) {
        String message = "Неизвестная ошибка";
        if (response.code() == 404) {
            message = "Фильм не найден (Ошибка 404)";
        } else if (response.code() == 500) {
            message = "Ошибка сервера (Ошибка 500)";
        } else {
            message = "Ошибка: " + response.code();
        }

        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void displayMovieInfo(MovieResponse.Movie movie) {
        if (movie != null) {
            String movieDetails = "Название: " + movie.getNameRu() + "\n" +
                    "Год: " + movie.getYear() + "\n" +
                    "Рейтинг: " + movie.getRating() + "\n" +
                    "Описание: " + movie.getDescription() + "\n" +
                    "Жанры: " + movie.getGenres().stream()
                    .map(genre -> genre.getGenre())
                    .reduce((g1, g2) -> g1 + ", " + g2)
                    .orElse("") + "\n";
            movieInfo.setText(movieDetails);

            // Загружаем изображение постера с помощью библиотеки Glide
            Glide.with(this)
                    .load(movie.getPosterUrl())
                    .into(moviePoster);
        } else {
            movieInfo.setText("Фильм не найден.");
        }
    }
}