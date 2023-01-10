package com.example.carofinal;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicGame extends Service{
    public static boolean boolIsServiceCreated = false; public static MediaPlayer player;
    @Override public IBinder onBind(Intent intent) { return null; }
    @Override
    public void onCreate() {

        boolIsServiceCreated = true;
        player = MediaPlayer.create(getApplicationContext(), R.raw.night_run);
    }
    @Override
    public void onDestroy() {

        player.stop(); player.release(); player = null;
    }

    @Override
    public void onStart(Intent intent, int startid) {
        player.setLooping(true);
        player.start();
    }

}
