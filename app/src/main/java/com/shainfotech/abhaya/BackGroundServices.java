package com.shainfotech.abhaya;

import android.app.Service;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import android.os.Handler;

/**
 * Created by paxterra on 4/17/2015.
 */
public class BackGroundServices extends Service {

    AudioManager mAudioManager;
    Handler mHandler;


    private ContentObserver mVolumeObserver = new ContentObserver(mHandler) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if(mAudioManager != null){
                final int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
                Log.e("TEST","VOLUME STREAM IS : " + volume);

            }
        }
    };

    @Override
    public IBinder onBind(Intent arg0){
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        Uri uri = Settings.System.getUriFor(Settings.System.VOLUME_SETTINGS[AudioManager.STREAM_RING]);
        getContentResolver().registerContentObserver(uri, true, mVolumeObserver);
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        getContentResolver().unregisterContentObserver(mVolumeObserver);
    }
}
