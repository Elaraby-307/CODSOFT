package com.example.quotes;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView favoritesRecyclerView;
    private FavoritesAdapter adapter;
    private List<String> favoriteQuotes;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        sharedPreferences = getSharedPreferences("QuoteApp", Context.MODE_PRIVATE);
        favoriteQuotes = new ArrayList<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteQuotes(); // Reload quotes when returning to the activity
    }

    private void removeFavoriteQuote(int position) {
        if (position >= 0 && position < favoriteQuotes.size()) {
            // Remove the quote from the list
            favoriteQuotes.remove(position);
            // Notify the adapter of the removal
            adapter.notifyItemRemoved(position);
            // Save updated favorites to SharedPreferences
            saveFavorites();
        } else {
            Log.e("FavoritesActivity", "Invalid position: " + position);
        }
    }

    private void loadFavoriteQuotes() {
        String savedQuotes = sharedPreferences.getString("favoriteQuotes", "");
        favoriteQuotes.clear(); // Clear the list to avoid duplicates

        if (!savedQuotes.isEmpty()) {
            favoriteQuotes.addAll(Arrays.asList(savedQuotes.split("\\|\\|")));
            favoriteQuotes.removeIf(String::isEmpty); // Remove empty strings
        }

        if (adapter == null) {
            adapter = new FavoritesAdapter(favoriteQuotes, this::removeFavoriteQuote);
            favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            favoritesRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged(); // Refresh the adapter
        }
    }


    private void saveFavorites() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("favoriteQuotes", String.join("||", favoriteQuotes));
        editor.apply(); // Apply changes to SharedPreferences
        Log.d("FavoritesActivity", "Saved favorites: " + String.join("||", favoriteQuotes));
    }

}
