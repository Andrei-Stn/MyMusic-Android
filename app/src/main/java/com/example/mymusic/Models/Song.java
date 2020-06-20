package com.example.mymusic.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Song extends RealmObject {
    @PrimaryKey
    private Integer id = 0;
    @SerializedName("title")
    private String title;
    @SerializedName("artist")
    private String artist;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("songUrl")
    private String songUrl;
    @SerializedName("songGenre")
    private String songGenre;
    @SerializedName("favorite")
    private Boolean favorite;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getSongGenre() {
        return songGenre;
    }

    public void setSongGenre(String songGenre) {
        this.songGenre = songGenre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public Song(String title, String artist, String imageUrl, String songUrl, String songGenre, Boolean favorite) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.songUrl = songUrl;
        this.songGenre = songGenre;
        this.favorite = favorite;
    }

    public Song() {
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        super.equals(obj);

        if (this.title == ((Song) obj).title) {
            return true;
        } else {
            return false;
        }
    }
}

