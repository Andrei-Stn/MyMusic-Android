package com.example.mymusic.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymusic.Adapter.AdapterLibrary;
import com.example.mymusic.R;

import static com.example.mymusic.Adapter.AdapterHome.songs;

public class UserFragment extends Fragment {

    TextView favorites;
    TextView numberOfSongs;
    TextView fullName, userName, password;
    TextView email;
    TextView full_name, user_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        fullName = view.findViewById(R.id.fullName_edit);
        userName = view.findViewById(R.id.user_edit);
        email = view.findViewById(R.id.email_edit);
        password = view.findViewById(R.id.password_edit);
        full_name = view.findViewById(R.id.full_name);
        user_name = view.findViewById(R.id.user_name);

        favorites = view.findViewById(R.id.favorites_label);
        numberOfSongs = view.findViewById(R.id.songs_label);

        numberOfSongs.setText(String.valueOf(getNumberOfSongs()));
        favorites.setText(String.valueOf(getFavouriteNumber()));

        SharedPreferences sp = this.getActivity().getSharedPreferences("DisplayName", Context.MODE_PRIVATE);
        String email_key = sp.getString("emailKey", "");
        String username_key = sp.getString("UserNameKey", "");

        userName.setText(username_key);
        email.setText(email_key);

        return view;
    }

/*    private void showAllUserData() {

        Intent intent = getActivity().getIntent();
        String user_fullname = intent.getStringExtra("fullName");
        String user_username = intent.getStringExtra("username");
        String user_email = intent.getStringExtra("email");
        String user_password = intent.getStringExtra("password");

        full_name.setText(user_fullname);
        user_name.setText(user_username);

        email.setText(user_email);

    }*/

    private int getFavouriteNumber() {

        int size = 0;

        try {
            size = AdapterLibrary.favoriteSongs.size();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return size;
    }

    public int getNumberOfSongs() {
        try {
            return songs.size();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
