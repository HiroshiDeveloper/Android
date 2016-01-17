package com.example.htokutomi.battlegame;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class MyService extends Service {

    // mediaPlayer : music
    MediaPlayer mediaPlayer;
    // save data
    SharedPreferences sharedPreference;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // save text
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String stage = sharedPreference.getString("stage", "");

        int rawId;
        rawId = getResources().getIdentifier(stage, "raw", getPackageName());

        // create music
        mediaPlayer = MediaPlayer.create(this, rawId);

        mediaPlayer.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();

    }
}
