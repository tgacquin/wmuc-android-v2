package com.example.wmucv2;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.app.NotificationManager.IMPORTANCE_LOW;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.media3.common.AudioAttributes;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.Player;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.ui.PlayerNotificationManager;

public class PlayerService extends Service {

    private final IBinder serviceBinder = new ServiceBinder();
    SimpleExoPlayer player;
    PlayerNotificationManager notificationManager;

    public class ServiceBinder extends Binder {
        public PlayerService getPlayerService() {
            return PlayerService.this;
        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind is called!");
        return serviceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(this, new AdaptiveTrackSelection.Factory());
        DefaultTrackSelector.Parameters trackSelectorParameters = trackSelector.buildUponParameters().setMaxAudioChannelCount(0).build();
        trackSelector.setParameters(trackSelectorParameters);
        RenderersFactory renderersFactory = new DefaultRenderersFactory(this);
        player = new SimpleExoPlayer.Builder(this, renderersFactory).setTrackSelector(trackSelector).build();
        HttpDataSource.Factory factory = new DefaultHttpDataSource.Factory();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA).setContentType(C.CONTENT_TYPE_MUSIC)
                .build();
        final String channelId = getString(R.string.channel_name);
        final int notificationId = 1111111;

        player.setAudioAttributes(audioAttributes, true);
        notificationManager = new PlayerNotificationManager.Builder(this, notificationId,channelId)
                .setNotificationListener(notificationListener)
                .setSmallIconResourceId(R.drawable.smallicon)
                .setMediaDescriptionAdapter(descriptionAdapter)
                .setChannelImportance(IMPORTANCE_LOW)
                .setChannelNameResourceId(R.string.channel_name)
                .build();

        notificationManager.setPlayer(player);
        notificationManager.setPriority(NotificationCompat.PRIORITY_MIN);
        notificationManager.setUseRewindAction(false);
        notificationManager.setUseFastForwardAction(false);
    }



    @Override
    public void onDestroy() {
        if (player.isPlaying()) player.stop();
        notificationManager.setPlayer(null);
        player.release();
        player = null;
        stopForeground(true);
        stopSelf();

        super.onDestroy();
    }


    PlayerNotificationManager.NotificationListener notificationListener = new PlayerNotificationManager.NotificationListener() {
        @Override
        public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
            PlayerNotificationManager.NotificationListener.super.onNotificationCancelled(notificationId, dismissedByUser);
            stopForeground(true);
            if (player.isPlaying()) {
                player.pause();
            }
        }

        @Override
        public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
            PlayerNotificationManager.NotificationListener.super.onNotificationPosted(notificationId, notification, ongoing);
            startForeground(notificationId, notification);
        }
    };

    PlayerNotificationManager.MediaDescriptionAdapter descriptionAdapter = new PlayerNotificationManager.MediaDescriptionAdapter() {
        @Override
        public CharSequence getCurrentContentTitle(Player player) {
            return "WMUC";
        }

        @Nullable
        @Override
        public PendingIntent createCurrentContentIntent(Player player) {
            return null;
        }

        @Nullable
        @Override
        public CharSequence getCurrentContentText(Player player) {
            return null;
        }

        @Nullable
        @Override
        public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
            return null;
        }
    };
}
