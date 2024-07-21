package com.example.quotes;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private List<String> favoriteQuotes;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public FavoritesAdapter(List<String> favoriteQuotes, OnDeleteClickListener onDeleteClickListener) {
        this.favoriteQuotes = favoriteQuotes;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_quote_item, parent, false);
        return new ViewHolder(view, onDeleteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.quoteTextView.setText(favoriteQuotes.get(position));

    }

    @Override
    public int getItemCount() {
        return favoriteQuotes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView quoteTextView;
        public Button deleteButton;

        public ViewHolder(View view, OnDeleteClickListener onDeleteClickListener) {
            super(view);
            quoteTextView = view.findViewById(R.id.quoteTextView);
            deleteButton = view.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(getAdapterPosition()));
 }
}
}