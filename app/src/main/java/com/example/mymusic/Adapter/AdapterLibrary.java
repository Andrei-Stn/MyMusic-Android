package com.example.mymusic.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.Fragments.MediaPlayerFragment;
import com.example.mymusic.Models.Song;
import com.example.mymusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterLibrary extends RecyclerView.Adapter<AdapterLibrary.ViewHolder> {
    Context context;
    static public List<Song> favoriteSongs;

    public AdapterLibrary(Context context, List<Song> favoriteSongs) {
        this.context = context;
        this.favoriteSongs = favoriteSongs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.costum_list_layout, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song currentSong = favoriteSongs.get(position);

        String imageUrl = currentSong.getImageUrl();
        String title = currentSong.getTitle();
        String artist = currentSong.getArtist();
        String genre = currentSong.getSongGenre();

        holder.songArtists.setText(artist);
        holder.songTitle.setText(title);
        holder.songGenre.setText(genre);
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.songCoverImage);

    }

    @Override
    public int getItemCount() {
        return favoriteSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle, songArtists, songGenre;
        ImageView songCoverImage;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            songTitle = itemView.findViewById(R.id.songTitle);
            songArtists = itemView.findViewById(R.id.songArtist);
            songCoverImage = itemView.findViewById(R.id.coverImage);
            songGenre = itemView.findViewById(R.id.songGenre);

        }
    }

}


