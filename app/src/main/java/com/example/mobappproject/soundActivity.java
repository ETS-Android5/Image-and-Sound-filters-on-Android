package com.example.mobappproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;

public class soundActivity extends AppCompatActivity implements Runnable, RecyclerViewAdapter.OnPickListener {

    private RecyclerView listOfSongs;
    private recyclerViewSongList listOfSongsAdapter;
    private RecyclerView.LayoutManager listofSongsLayout;

    private static final String TAG = "RecyclerViewer";
    private static final int SOUND_PICK_CODE = 1002;

    //variables
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<songItem> songCards = new ArrayList<>();
    int songNumber;

    //buttons and stuff
    Button mPlay;
    Button mPause;
    Button mStop;

    SeekBar soundSeekbar;

    MediaPlayer soundPlayer;

    Thread soundThread;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound2);

        mPlay = findViewById(R.id.PlayButton);
        mPause = findViewById(R.id.PauseButton);
        mStop = findViewById(R.id.StopButton);


        soundSeekbar = findViewById(R.id.soundSeekBar);
        soundPlayer = MediaPlayer.create(this, R.raw.kaleida);

        songNumber = 0;


        setupListeners();

        getImages();

        getSongs();
        listOfSongs = findViewById(R.id.songList);
        listOfSongs.setHasFixedSize(true);
        listofSongsLayout = new LinearLayoutManager(this);
        listOfSongsAdapter = new recyclerViewSongList(songCards);
        listOfSongs.setLayoutManager(listofSongsLayout);
        listOfSongs.setAdapter(listOfSongsAdapter);

        listOfSongsAdapter.setOnItemClickListener(new recyclerViewSongList.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                soundPlayer.pause();
                soundPlayer.seekTo(0);
                soundPlayer.stop();
                songNumber = position;
                switch (position){
                    case 0:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.baguette);    Log.d(TAG,"Baguette loaded"); break;
                    case 1:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.birthday);   Log.d(TAG,"Birthday loaded"); break;
                    case 2:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.cha_cha);   Log.d(TAG,"Cha cha loaded"); break;
                    case 3:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.fanfare);   Log.d(TAG,"Fanfare loaded"); break;
                    case 4:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.frank);   Log.d(TAG,"Frank loaded"); break;
                    case 5:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.samara);   Log.d(TAG,"Samara loaded"); break;
                    case 6:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.study);   Log.d(TAG,"Study loaded"); break;
                    case 7:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.toms_diner);  Log.d(TAG,"Toms loaded"); break;
                    case 8:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.ussr);   Log.d(TAG,"Ussr loaded"); break;
                    case 9:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.village);   Log.d(TAG,"Village loaded"); break;
                    default:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.kaleida);    Log.d(TAG,"Kaleida loaded"); break;
                }
                soundSeekbar.setMax(soundPlayer.getDuration());
            }
        });




        soundThread = new Thread(this);
        soundThread.start();

    }

    private void getSongs(){
        int resourceId = R.drawable.ic_audiotrack_black_24dp;

        songCards.add(new songItem(resourceId, "Le Baguette"));
        songCards.add(new songItem(resourceId, "Sing A Birthday Song"));
        songCards.add(new songItem(resourceId, "Cha-Cha"));
        songCards.add(new songItem(resourceId, "Fanfare X"));
        songCards.add(new songItem(resourceId, "When The Mockingbirds Are Singing"));
        songCards.add(new songItem(resourceId, "Samara Jade"));
        songCards.add(new songItem(resourceId, "Study And Relax"));
        songCards.add(new songItem(resourceId, "Toms dinner"));
        songCards.add(new songItem(resourceId, "USSR"));
        songCards.add(new songItem(resourceId, "Village Tarantella"));
    }

    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        int resourceId = 0;
        String res = "";

        resourceId = R.drawable.time_warp;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Time-warping x2");

        resourceId = R.drawable.time_scale;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Time-scalling x2");

        resourceId = R.drawable.p_scalling;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Pitch-scalling x2");

        resourceId = R.drawable.lp;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Lowpass filter 1kHz");

        resourceId = R.drawable.hp;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Highpass filter 1kHz");

        resourceId = R.drawable.echo;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Echo");

        initRecyclerView();

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSound);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mImageUrls, this, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners(){
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPlayer.start();
            }
        });

        mPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPlayer.pause();
            }
        });

        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPlayer.pause();
                soundPlayer.seekTo(0);

            }
        });

        soundSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    soundPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SOUND_PICK_CODE: {
                if (resultCode == RESULT_OK) {
                    soundPlayer.stop();
                    Uri myUri = data.getData();
                    String PathHolder = myUri.getPath();
                    Toast.makeText(this, "Loaded " + PathHolder, Toast.LENGTH_LONG).show();
                    try {
                        soundPlayer = MediaPlayer.create(this, myUri);
                    } catch (Exception e) {
                        soundPlayer = MediaPlayer.create(this, R.raw.kaleida);
                        Toast.makeText(this, "Error 1! Loading default song", Toast.LENGTH_LONG).show();
                    }

                }
            }
            break;
        }
    }

    //Seekbar update
    @Override
    public void run(){
        int currentPos = 0;
        int soundLength = soundPlayer.getDuration();
        soundSeekbar.setMax(soundLength);

        while(soundPlayer != null && currentPos < soundLength){
            try
            {
                Thread.sleep(300);
                currentPos = soundPlayer.getCurrentPosition();

            }
            catch (InterruptedException e) {
                return;
            }
            catch (Exception other){
                return;
            }
            soundSeekbar.setProgress(currentPos);

        }
    }

    @Override
    public void onPickClick(int position) {
        //Toast.makeText(this,mNames.get(position),Toast.LENGTH_SHORT);
        Log.d(TAG, "OnPickListener " + position + " clicked" );
        soundPlayer.pause();
        soundPlayer.seekTo(0);
        soundPlayer.stop();
        switch (position){
            case 0: switchSongWarping(songNumber);
                break;
            case 1: switchSongTime(songNumber);
                break;
            case 2: switchSongPitch(songNumber);
                break;
            case 3: switchSongLowPass(songNumber);
                break;
            case 4: switchSongHighPass(songNumber);
                break;
            case 5: switchSongEcho(songNumber);
                break;
        }
        soundSeekbar.setMax(soundPlayer.getDuration());


        soundPlayer.start();

    }

    private void switchSongWarping(int numberOfSong){
        switch (numberOfSong){
            case 0:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.baguette_warp);   break;
            case 1:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.birthday_warp);   break;
            case 2:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.cha_cha_warp);   break;
            case 3:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.fanfare_warp);   break;
            case 4:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.frank_warp);   break;
            case 5:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.samara_warp);   break;
            case 6:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.study_warp);   break;
            case 7:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.toms_diner_warp);   break;
            case 8:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.ussr_warp);   break;
            case 9:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.village_warp);   break;
            default:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.kaleida);    break;
        }

    }

    private void switchSongTime(int numberOfSong){
        switch (numberOfSong){
            case 0:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.baguette_time);   break;
            case 1:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.birthday_time);   break;
            case 2:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.cha_cha_time);   break;
            case 3:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.fanfare_time);   break;
            case 4:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.frank_time);   break;
            case 5:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.samara_time);   break;
            case 6:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.study_time);   break;
            case 7:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.toms_diner_time);   break;
            case 8:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.ussr_time);   break;
            case 9:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.village_time);   break;
            default:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.kaleida);    break;
        }

    }

    private void switchSongPitch(int numberOfSong){
        switch (numberOfSong){
            case 0:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.baguette_pitch);   break;
            case 1:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.birthday_pitch);   break;
            case 2:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.cha_cha_pitch);   break;
            case 3:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.fanfare_pitch);   break;
            case 4:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.frank_pitch);   break;
            case 5:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.samara_pitch);   break;
            case 6:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.study_pitch);   break;
            case 7:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.toms_diner_pitch);   break;
            case 8:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.ussr_pitch);   break;
            case 9:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.village_pitch);   break;
            default:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.kaleida);    break;
        }

    }

    private void switchSongEcho(int numberOfSong){
        switch (numberOfSong){
            case 0:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.baguette_echo);   break;
            case 1:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.birthday_echo);   break;
            case 2:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.cha_cha_echo);   break;
            case 3:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.fanfare_echo);   break;
            case 4:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.frank_echo);   break;
            case 5:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.samara_echo);   break;
            case 6:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.study_echo);   break;
            case 7:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.toms_diner_echo);   break;
            case 8:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.ussr_echo);   break;
            case 9:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.village_echo);   break;
            default:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.kaleida);    break;
        }

    }

    private void switchSongLowPass(int numberOfSong){
        switch (numberOfSong){
            case 0:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.baguette_lp);   break;
            case 1:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.birthday_lp);   break;
            case 2:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.cha_cha_lp);   break;
            case 3:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.fanfare_lp);   break;
            case 4:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.frank_lp);   break;
            case 5:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.samara_lp);   break;
            case 6:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.study_lp);   break;
            case 7:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.toms_diner_lp);   break;
            case 8:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.ussr_lp);   break;
            case 9:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.village_lp);   break;
            default:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.kaleida);    break;
        }

    }

    private void switchSongHighPass(int numberOfSong){
        switch (numberOfSong){
            case 0:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.baguette_hp);   break;
            case 1:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.birthday_hp);   break;
            case 2:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.cha_cha_hp);   break;
            case 3:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.fanfare_hp);   break;
            case 4:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.frank_hp);   break;
            case 5:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.samara_hp);   break;
            case 6:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.study_hp);   break;
            case 7:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.toms_diner_hp);   break;
            case 8:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.ussr_hp);   break;
            case 9:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.village_hp);   break;
            default:soundPlayer = MediaPlayer.create(soundActivity.this, R.raw.kaleida);    break;
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        soundPlayer.stop();
    }
}


