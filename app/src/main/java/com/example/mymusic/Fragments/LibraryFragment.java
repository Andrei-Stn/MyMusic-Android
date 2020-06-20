package com.example.mymusic.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.Adapter.AdapterLibrary;
import com.example.mymusic.Models.Song;
import com.example.mymusic.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class LibraryFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private List<Song> songList = new ArrayList<>();

    private AdapterLibrary mAdapter;

    private RecyclerView.LayoutManager layoutManager;

    private Realm mRealm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        mRealm = Realm.getDefaultInstance();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleview_library);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        songList = readSong();

        mAdapter = new AdapterLibrary(getActivity(), songList);

        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    private List<Song> readSong() {
        List<Song> songList;
        RealmQuery<Song> query = mRealm.where(Song.class);

        RealmResults<Song> result = query.findAll();

        songList = result;

        return songList;
    }

}
