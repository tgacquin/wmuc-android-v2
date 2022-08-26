package com.example.wmucv2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;

import java.util.Calendar;


public class RadioService extends AppCompatActivity {
    Show show;
    TextView showName;
    TextView showHost;
    ImageView digStation;
    ImageView fmStation;
    ImageView playBtn;
    ImageView scheduleBtn;
    boolean isBound = false;
    SimpleExoPlayer audioPlayer;

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




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Context context = getApplicationContext();
        Intent intent = new Intent(this, PlayerService.class); // Build the intent for the service
        context.startForegroundService(intent);
        doBindService();


        setContentView(R.layout.activity_radio_selection);
        HttpDataSource.Factory factory = new DefaultHttpDataSource.Factory();

        fmStation=findViewById(R.id.FM);
        digStation=findViewById(R.id.DIG);
        playBtn=findViewById(R.id.Play);
        scheduleBtn=findViewById(R.id.schedule);
        showName=findViewById(R.id.showname);
        showHost=findViewById(R.id.showhost);

        fmStation.setImageAlpha(150);
        digStation.setImageAlpha(150);

        fmStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fmHit) {
                    fmStation.setImageAlpha(255);
                    digStation.setImageAlpha(150);
                    currChannel=FM;
                    getCurrShow();
                    fmHit=true;
                    digitalHit=false;
                    if (audioPlayer.isPlaying()) {
                        playBtn.setImageResource(R.drawable.pause);
                    }
                    audioPlayer.setMediaSource(new ProgressiveMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(fmURI)));
                    audioPlayer.prepare();
                }
            }
        });

        digStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!digitalHit) {
                    if (isBound==true) {
                    fmStation.setImageAlpha(150);
                    digStation.setImageAlpha(255);
                    currChannel=DIGITAL;
                    getCurrShow();
                    fmHit=false;
                    digitalHit=true;
                    if (audioPlayer.isPlaying()) {
                        playBtn.setImageResource(R.drawable.pause);
                    }
                    audioPlayer.setMediaSource(new ProgressiveMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(digURI)));
                    audioPlayer.prepare();
                    }
                }
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fmHit && !digitalHit) {
                    return;
                } else if (audioPlayer.isPlaying()){
                    audioPlayer.pause();
                } else {
                    audioPlayer.setPlayWhenReady(true);
                }
            }
        });

        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Schedule.class));
            }
        });
    }

    private void doBindService() {
        Intent playerServiceIntent = new Intent(this, PlayerService.class);
        bindService(playerServiceIntent, playerServiceConnection, Context.BIND_AUTO_CREATE);
        System.out.println("reached!");
    }

    ServiceConnection playerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            PlayerService.ServiceBinder binder = (PlayerService.ServiceBinder) iBinder;
            audioPlayer = binder.getPlayerService().player;
            isBound=true;
            System.out.println("reached!2");
            audioPlayer.addListener(new Player.Listener() {
                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    Player.Listener.super.onIsPlayingChanged(isPlaying);
                    if (isPlaying==true || audioPlayer.getPlaybackState()==Player.STATE_BUFFERING
                || audioPlayer.getPlaybackState()==Player.STATE_IDLE) {
                        playBtn.setImageResource(R.drawable.pause);
                    } else {
                        playBtn.setImageResource(R.drawable.play);
                    }
                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static int getShowIndex(Show[][] sched, int day, int currHour) {
        for (int i=0; i<24; i++) {
            Show currShow = sched[i][day];
            if (currShow==null) {
                break;
            } else {
                if (currHour>=currShow.startTimeInt && currHour<currShow.endTimeInt) {
                    return i;
                }
            }
        }
        return 23; //Will return off the air
    }

    //Reuse of old WMUC Android code
    public void getCurrShow() {
        Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int indexOfShow;


        //Should use a switch statement here instead
        if(currChannel == DIGITAL) {
            if (dayOfWeek == Calendar.SUNDAY) {
                show = MainActivity.digSched
                        [getShowIndex(MainActivity.digSched,0,hourOfDay)]
                        [0];
            } else if (dayOfWeek == Calendar.MONDAY) {
                show = MainActivity.digSched
                        [getShowIndex(MainActivity.digSched,1,hourOfDay)]
                        [1];
            } else if (dayOfWeek == Calendar.TUESDAY) {
                show = MainActivity.digSched
                        [getShowIndex(MainActivity.digSched,2,hourOfDay)]
                        [2];
            } else if (dayOfWeek == Calendar.WEDNESDAY) {
                show = MainActivity.digSched
                        [getShowIndex(MainActivity.digSched,3,hourOfDay)]
                        [3];
            } else if (dayOfWeek == Calendar.THURSDAY) {
                show = MainActivity.digSched
                        [getShowIndex(MainActivity.digSched,4,hourOfDay)]
                        [4];
            } else if (dayOfWeek == Calendar.FRIDAY) {
                show = MainActivity.digSched
                        [getShowIndex(MainActivity.digSched,5,hourOfDay)]
                        [5];
            } else if (dayOfWeek == Calendar.SATURDAY) {
                show = MainActivity.digSched
                        [getShowIndex(MainActivity.digSched,6,hourOfDay)]
                        [6];
            }
        } else if(currChannel == FM) {
            if (dayOfWeek == Calendar.SUNDAY) {
                show = MainActivity.fmSched
                        [getShowIndex(MainActivity.fmSched,0,hourOfDay)]
                        [0];
            } else if (dayOfWeek == Calendar.MONDAY) {
                show = MainActivity.fmSched
                        [getShowIndex(MainActivity.fmSched,1,hourOfDay)]
                        [1];
            } else if (dayOfWeek == Calendar.TUESDAY) {
                show = MainActivity.fmSched
                        [getShowIndex(MainActivity.fmSched,2,hourOfDay)]
                        [2];
            } else if (dayOfWeek == Calendar.WEDNESDAY) {
                show = MainActivity.fmSched
                        [getShowIndex(MainActivity.fmSched,3,hourOfDay)]
                        [3];
            } else if (dayOfWeek == Calendar.THURSDAY) {
                show = MainActivity.fmSched
                        [getShowIndex(MainActivity.fmSched,4,hourOfDay)]
                        [4];
            } else if (dayOfWeek == Calendar.FRIDAY) {
                show = MainActivity.fmSched
                        [getShowIndex(MainActivity.fmSched,5,hourOfDay)]
                        [5];
            } else if (dayOfWeek == Calendar.SATURDAY) {
                show = MainActivity.fmSched
                        [getShowIndex(MainActivity.fmSched,6,hourOfDay)]
                        [6];
            }
        }
        if (show==null) {
            showName.setText("Off The Air");
            showHost.setText("None");
            return;
        }
        showName.setText(show.showName);
        showHost.setText(show.showHost);
    }

}