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
import com.example.mymusic.R;
import com.example.mymusic.Models.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder> {
    Context context;
    static public List<Song> songs;

    public AdapterHome(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.costum_list_layout, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song currentSong = songs.get(position);

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
        return songs.size();
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final String title = String.valueOf(songTitle.getText());
                    final String songUrl = songs.get(songs.indexOf(new Song(title, "", "", "", "", false))).getSongUrl();
                    final String imageUrl = songs.get(songs.indexOf(new Song(title, "", "", "", "", false))).getImageUrl();

                    Bundle arguments = new Bundle();
                    arguments.putString("title", title);
                    arguments.putString("image_url", imageUrl);
                    arguments.putString("song_url", songUrl);

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    MediaPlayerFragment myFragment = new MediaPlayerFragment();

                    myFragment.setArguments(arguments);

                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
                }
            });
        }
    }

    public void filterList(ArrayList<Song> filteredList) {
        songs = filteredList;
        notifyDataSetChanged();
    }
}


