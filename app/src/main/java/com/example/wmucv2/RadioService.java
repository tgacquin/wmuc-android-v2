package com.example.wmucv2;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.exoplayer.hls.HlsMediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;

import java.util.Calendar;


public class RadioService extends AppCompatActivity {
    Show show;
    TextView showName;
    TextView showHost;
    ImageView digStation;
    ImageView fmStation;
    ImageView playBtn;
    ImageView scheduleBtn;

    public static int currDay=0;
    public static int currChannel=7;
    public static int FM = 7;
    public static int DIGITAL = 8;
    public static int SUN = 0;
    public static int MON = 1;
    public static int TUES = 2;
    public static int WED = 3;
    public static int THURS = 4;
    public static int FRI = 5;
    public static int SAT = 6;



    boolean fmHit = false;
    boolean digitalHit = false;
    boolean playing = false;
    private final Uri fmURI = Uri.parse("http://wmuc.umd.edu:8000/wmuc-hq");
    private final Uri digURI = Uri.parse("http://wmuc.umd.edu:8000/wmuc2-high");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_selection);

        fmStation=findViewById(R.id.FM);
        digStation=findViewById(R.id.DIG);
        playBtn=findViewById(R.id.Play);
        scheduleBtn=findViewById(R.id.schedule);
        showName=findViewById(R.id.showname);
        showHost=findViewById(R.id.showhost);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(this, new AdaptiveTrackSelection.Factory());
        DefaultTrackSelector.Parameters trackSelectorParameters = trackSelector.buildUponParameters().setMaxAudioChannelCount(0).build();
        trackSelector.setParameters(trackSelectorParameters);
        RenderersFactory renderersFactory = new DefaultRenderersFactory(this);
        SimpleExoPlayer audioPlayer = new SimpleExoPlayer.Builder(this, renderersFactory).setTrackSelector(trackSelector).build();
        HttpDataSource.Factory factory = new DefaultHttpDataSource.Factory();

        fmStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fmHit) {
                    currChannel=FM;
                    getCurrShow();
                    fmHit=true;
                    digitalHit=false;
                    playing=true;
                    playBtn.setImageResource(R.drawable.pause);
                    audioPlayer.setMediaSource(new ProgressiveMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(digURI)));
                    audioPlayer.prepare();
                    audioPlayer.setPlayWhenReady(true);
                }
            }
        });

        digStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!digitalHit) {
                    currChannel=DIGITAL;
                    getCurrShow();
                    fmHit=false;
                    digitalHit=true;
                    playing=true;
                    playBtn.setImageResource(R.drawable.pause);
                    audioPlayer.setMediaSource(new ProgressiveMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(fmURI)));
                    audioPlayer.prepare();
                    audioPlayer.setPlayWhenReady(true);
                }
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fmHit && !digitalHit) {
                    return;
                } else if (playing==true){
                    playBtn.setImageResource(R.drawable.play);
                    audioPlayer.pause();
                    playing = false;
                } else {
                    playBtn.setImageResource(R.drawable.pause);
                    audioPlayer.play();

                    playing = true;
                }
            }
        });

        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Schedule.class));
            }
        });






/*
        ExoPlayer player = new ExoPlayer.Builder(RadioService.this).build();
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true);
        HlsMediaSource hlsMediaSource =
                new HlsMediaSource.Factory(dataSourceFactory)
                        .setAllowChunklessPreparation(false) // use it only if needed
                        .createMediaSource(MediaItem.fromUri(fmURI)); // pass your url
        player.addMediaSource(hlsMediaSource);
        player.prepare();
        player.setPlayWhenReady(true);
        player.play();
*/




    }


    //Reuse of old WMUC Android code
    public void getCurrShow() {
        Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if(currChannel == DIGITAL) {
            if (dayOfWeek == Calendar.SUNDAY) {
                show = MainActivity.digSched[hourOfDay][0];
            } else if (dayOfWeek == Calendar.MONDAY) {
                show = MainActivity.digSched[hourOfDay][1];
            } else if (dayOfWeek == Calendar.TUESDAY) {
                show = MainActivity.digSched[hourOfDay][2];
            } else if (dayOfWeek == Calendar.WEDNESDAY) {
                show = MainActivity.digSched[hourOfDay][3];
            } else if (dayOfWeek == Calendar.THURSDAY) {
                show = MainActivity.digSched[hourOfDay][4];
            } else if (dayOfWeek == Calendar.FRIDAY) {
                show = MainActivity.digSched[hourOfDay][5];
            } else if (dayOfWeek == Calendar.SATURDAY) {
                show = MainActivity.digSched[hourOfDay][6];
            }
        } else if(currChannel == FM) {
            if (dayOfWeek == Calendar.SUNDAY) {
                show = MainActivity.fmSched[hourOfDay][0];
            } else if (dayOfWeek == Calendar.MONDAY) {
                show = MainActivity.fmSched[hourOfDay][1];
            } else if (dayOfWeek == Calendar.TUESDAY) {
                show = MainActivity.fmSched[hourOfDay][2];
            } else if (dayOfWeek == Calendar.WEDNESDAY) {
                show = MainActivity.fmSched[hourOfDay][3];
            } else if (dayOfWeek == Calendar.THURSDAY) {
                show = MainActivity.fmSched[hourOfDay][4];
            } else if (dayOfWeek == Calendar.FRIDAY) {
                show = MainActivity.fmSched[hourOfDay][5];
            } else if (dayOfWeek == Calendar.SATURDAY) {
                show = MainActivity.fmSched[hourOfDay][6];
            }
        }
        showName.setText(show.showName);
        showHost.setText(show.showHost);
    }

}