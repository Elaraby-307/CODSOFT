package com.example.quotes;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quotes.FavoritesActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private boolean isFirstLaunch = true ;
    private static final String LAST_QUOTE_UPDATE_KEY = "lastQuoteUpdate";
    private static final String QUOTE_KEY = "currentQuote";
    private static final String FAVORITE_QUOTES_KEY = "favoriteQuotes";

    private TextView quoteTextView;
    private Button shareButton, favoriteButton, viewFavoritesButton;
    private List<String> favoriteQuotes = new ArrayList<>();
    private String currentQuote;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quoteTextView = findViewById(R.id.quoteTextView);
        shareButton = findViewById(R.id.shareButton);
        favoriteButton = findViewById(R.id.favoriteButton);
        viewFavoritesButton = findViewById(R.id.viewFavoritesButton);

        sharedPreferences = getSharedPreferences("QuoteApp", Context.MODE_PRIVATE);

        final String[] quotes = {
                "The best way to predict the future is to invent it.",
                "Life is 10% what happens to us and 90% how we react to it.",
                "The only way to do great work is to love what you do.",
                "The only limit to our realization of tomorrow is our doubts of today.",
                "The purpose of our lives is to be happy."
        };



        //this part fetches a new random quote on app launch
        currentQuote = getRandomQuote(quotes);
        quoteTextView.setText(currentQuote);

        // Load favorite quotes from shared preferences
        String savedQuotes = sharedPreferences.getString(FAVORITE_QUOTES_KEY, "");
        favoriteQuotes.addAll(Arrays.asList(savedQuotes.split("\\|\\|")));


        shareButton.setOnClickListener(v -> shareQuote(currentQuote));

        favoriteButton.setOnClickListener(v -> {
            if (!favoriteQuotes.contains(currentQuote)) {
                favoriteQuotes.add(currentQuote);
                Toast.makeText(this, "Quote saved!", Toast.LENGTH_SHORT).show();

                // Save updated favorites to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("favoriteQuotes", String.join("||", favoriteQuotes));
                editor.apply(); // Apply changes to SharedPreferences
                Log.d("MainActivity", "Saved favorites: " + String.join("||", favoriteQuotes));

            } else {
                Toast.makeText(this, "This quote is already saved.", Toast.LENGTH_SHORT).show();
            }
        });







        viewFavoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        checkAndUpdateQuote(quotes);
    }





    private void checkAndUpdateQuote(String[] quotes) {
        if (isFirstLaunch || !sharedPreferences.getString(LAST_QUOTE_UPDATE_KEY, "")
                .equals(new SimpleDateFormat("yyyy-MM-dd")
                        .format(Calendar.getInstance().getTime()))) {

            currentQuote = getRandomQuote(quotes);
            quoteTextView.setText(currentQuote);

            isFirstLaunch = false; // Set to false after first launch
        } else {
            currentQuote = sharedPreferences.getString(QUOTE_KEY, getRandomQuote(quotes));
            quoteTextView.setText(currentQuote);
        }
    }


    private String getRandomQuote(String[] quotes) {
        Random random = new Random();
        int index = random.nextInt(quotes.length);
        return quotes[index];
    }

    private void shareQuote(String quote) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, quote);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Sharevia"));
}
}