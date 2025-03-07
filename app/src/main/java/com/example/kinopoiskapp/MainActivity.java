package com.example.kinopoiskapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private EditText movieInput;
    private Button searchButton;
    private TextView movieInfo;
    private ImageView moviePoster;
    private Button prevButton;
    private Button nextButton;

    private List<MovieResponse.Movie> movies;
    private int currentMovieIndex = 0;

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
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        searchButton.setOnClickListener(v -> searchMovies(movieInput.getText().toString()));

        prevButton.setOnClickListener(v -> {
            if (movies != null && currentMovieIndex > 0) {
                currentMovieIndex--;
                displayMovieInfo(movies.get(currentMovieIndex));
            }
        });

        nextButton.setOnClickListener(v -> {
            if (movies != null && currentMovieIndex < movies.size() - 1) {
                currentMovieIndex++;
                displayMovieInfo(movies.get(currentMovieIndex));
            }
        });
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
                    movies = response.body().getFilms();
                    if (movies != null && !movies.isEmpty()) {
                        currentMovieIndex = 0;
                        displayMovieInfo(movies.get(currentMovieIndex));
                    } else {
                        movieInfo.setText("Фильмы не найдены.");
                    }
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
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

            Glide.with(this)
                    .load(movie.getPosterUrl())
                    .into(moviePoster);
        } else {
            movieInfo.setText("Фильм не найден.");
        }
    }
}