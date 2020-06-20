package com.example.mymusic.Fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymusic.R;
import com.example.mymusic.Models.Song;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.mymusic.Adapter.AdapterHome.songs;


public class MediaPlayerFragment extends Fragment {

    Integer songsListIndex;

    static MediaPlayer mediaPlayer;

    List<Song> songsList;

    ImageView songImage;
    SeekBar songPositionBar;

    TextView songTitle;
    TextView songElapsedTime;
    TextView songRemainingTime;

    Button play_or_pause;
    Button next;
    Button previous;

    Button favorite;
    Button shuffle;

    Integer totalTime;

    Realm mRealm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_media_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        play_or_pause = view.findViewById(R.id.btn_play_or_pause);
        next = view.findViewById(R.id.btn_next);
        previous = view.findViewById(R.id.btn_previous);
        favorite = view.findViewById(R.id.btn_favorite);
        shuffle = view.findViewById(R.id.btn_shuffle);
        songImage = view.findViewById(R.id.iv_song_image);
        songTitle = view.findViewById(R.id.tv_song_title);
        songElapsedTime = view.findViewById(R.id.tv_elapsed_time);
        songRemainingTime = view.findViewById(R.id.tv_remaining_time);
        songPositionBar = view.findViewById(R.id.sb_song_status);

        initMediaPlayer();

        listeners();

        totalTime = mediaPlayer.getDuration();
        songPositionBar.setMax(totalTime);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            songPositionBar.setProgress(currentPosition);


            if (songPositionBar.getProgress() == mediaPlayer.getDuration()) {
                nextSong();
            }

            String elapsedTime = createTimeLabel(currentPosition);
            songElapsedTime.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - currentPosition);
            songRemainingTime.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time) {
        String timeLabel = "";

        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    private void initMediaPlayer() {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        Bundle bundle = getArguments();

        String title = bundle.getString("title");
        String image_url = bundle.getString("image_url");
        String song_url = bundle.getString("song_url");

        Picasso.get().load(image_url).fit().into(songImage);
        songTitle.setText(title);

        songsListIndex = songs.indexOf(new Song(title, "", "", "", "", false));

        setSong(song_url);

    }

    private void listeners() {

        songPositionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mediaPlayer.seekTo(progress);
                            songPositionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        play_or_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    play_or_pause.setBackgroundResource(R.drawable.start_song);
                    mediaPlayer.pause();
                } else {
                    play_or_pause.setBackgroundResource(R.drawable.pause_song);
                    mediaPlayer.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSong();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousSong();
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFavorite();
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffle();
            }
        });

    }

    private void addFavorite() {
        if (!songs.get(songsListIndex).getFavorite()) {
            favorite.setBackgroundResource(R.drawable.full_heart);
            songs.get(songsListIndex).setFavorite(true);
            addSong(songs.get(songsListIndex));
        } else {
            favorite.setBackgroundResource(R.drawable.empty_heart);
            songs.get(songsListIndex).setFavorite(false);
            deleteSong();
        }
    }

    private void addSong(final Song song) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
                                           @Override
                                           public void execute(Realm realm) {
                                               Number currentSongId = realm.where(Song.class).max("id");
                                               int nextId;

                                               if (currentSongId == null) {
                                                   nextId = 1;
                                               } else {
                                                   nextId = currentSongId.intValue() + 1;
                                               }
                                               song.setId(nextId);

                                               realm.insertOrUpdate(song);
                                           }
                                       }, new Realm.Transaction.OnSuccess() {
                                           @Override
                                           public void onSuccess() {
                                               Toast.makeText(getContext(), "Succes!", Toast.LENGTH_SHORT).show();
                                           }
                                       }, new Realm.Transaction.OnError() {
                                           @Override
                                           public void onError(Throwable error) {
                                               Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                                           }
                                       }
        );
    }

    private void deleteSong() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Song> song = realm.where(Song.class).equalTo("title", songs.get(songsListIndex).getTitle()).findAll();
                if (song != null) {
                    song.deleteAllFromRealm();
                }
            }
        });
    }

    private void nextSong() {

        play_or_pause.setBackgroundResource(R.drawable.pause_song);
        mediaPlayer.reset();

        if (++songsListIndex >= songs.size()) {
            songsListIndex = 0;
        }

        setSong(songs.get(songsListIndex).getSongUrl());

        try {

            Picasso.get().load(songs.get(songsListIndex).getImageUrl()).fit().into(songImage);

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(getContext(), "Download image error.", Toast.LENGTH_SHORT).show();

        }

        songTitle.setText(songs.get(songsListIndex).getTitle());


        if (songs.get(songsListIndex).getFavorite()) {
            favorite.setBackgroundResource(R.drawable.full_heart);
        } else {
            favorite.setBackgroundResource(R.drawable.empty_heart);
        }

        totalTime = mediaPlayer.getDuration();
        songPositionBar.setMax(totalTime);

        mediaPlayer.start();
    }

    private void previousSong() {

        play_or_pause.setBackgroundResource(R.drawable.pause_song);
        mediaPlayer.reset();

        if (--songsListIndex < 0) {
            songsListIndex = songs.size() - 1;
        }

        setSong(songs.get(songsListIndex).getSongUrl());
        Picasso.get().load(songs.get(songsListIndex).getImageUrl()).fit().into(songImage);
        songTitle.setText(songs.get(songsListIndex).getTitle());

        if (songs.get(songsListIndex).getFavorite()) {
            favorite.setBackgroundResource(R.drawable.full_heart);
        } else {
            favorite.setBackgroundResource(R.drawable.empty_heart);
        }

        totalTime = mediaPlayer.getDuration();
        songPositionBar.setMax(totalTime);

        mediaPlayer.start();
    }

    private void shuffle() {
        if (shuffle.getBackground().getConstantState() == getResources().getDrawable(R.drawable.shuffle_off).getConstantState()) {
            songsList = songs;
            Collections.shuffle(songs);
            shuffle.setBackgroundResource(R.drawable.shuffle_on);
        } else {
            songs = songsList;
            shuffle.setBackgroundResource(R.drawable.shuffle_off);
        }
    }

    private void setSong(String url) {
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            Toast.makeText(getContext(), "setData problem", Toast.LENGTH_SHORT).show();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            Toast.makeText(getContext(), "prepare problem", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
    }
}