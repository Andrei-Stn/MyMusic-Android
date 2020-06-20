package com.example.mymusic.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.Adapter.AdapterHome;
import com.example.mymusic.R;
import com.example.mymusic.Models.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<Song> songList = new ArrayList<>();

    private AdapterHome mAdapter;

    private TextView search_bar;

    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new AdapterHome(getActivity(), songList);
        mRecyclerView.setAdapter(mAdapter);
        search_bar = view.findViewById(R.id.search_bar);

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        addItemsFromJSON();

        return view;
    }

    private void filter(String text) {
        ArrayList<Song> filteredList = new ArrayList<>();

        for (Song song : songList) {
            if (song.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    song.getArtist().toLowerCase().contains(text.toLowerCase()) ||
                    song.getSongGenre().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(song);
            }
        }

        mAdapter.filterList(filteredList);
    }

    private void addItemsFromJSON() {
        try {

            String jsonDataString = readJSONDataFromFile();
            JSONArray jsonArray = new JSONArray(jsonDataString);

            for (int i = 0; i < jsonArray.length(); ++i) {

                JSONObject itemObj = jsonArray.getJSONObject(i);

                String title = itemObj.getString("song");
                String artist = itemObj.getString("artists");
                String imageUrl = itemObj.getString("cover_image");
                String songUrl = itemObj.getString("url");
                String songGenre = itemObj.getString("genre");

                Song songs = new Song(title, artist, imageUrl, songUrl, songGenre, false);
                songList.add(songs);
            }

        } catch (JSONException | IOException e) {
            Log.d(TAG, "addItemsFromJSON: ", e);
        }
    }

    private String readJSONDataFromFile() throws IOException {

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {

            String jsonString = null;
            inputStream = getResources().openRawResource(R.raw.song_list);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));

            while ((jsonString = bufferedReader.readLine()) != null) {
                builder.append(jsonString);
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }

}