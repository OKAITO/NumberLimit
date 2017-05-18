package com.zhiwu.numberlimit.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zhiwu.numberlimit.R;

public class MusicService extends Service {

    private MusicBinder musicBinder=new MusicBinder();
    private MediaPlayer mp;
    private boolean ifPlayMusic=false;
    private boolean ifPause=false;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("before mp");
        if(mp==null) {
            mp=MediaPlayer.create(this, R.raw.bgmusic);
            mp.setVolume(0.3f,0.3f);
        }
        System.out.println("after mp");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    public class MusicBinder extends Binder{
        public void startMusic(){mp.start();}
        public void pauseMusic(){mp.pause();}
        public void stopMusic(){mp.stop();mp.release();}
        public void setLoop(boolean ifLoop){mp.setLooping(ifLoop);}
        public boolean getIfPlayMusic(){return ifPlayMusic;}
        public void setIfPlayMusic(boolean ifPlayMusic2){ifPlayMusic=ifPlayMusic2;}
        public boolean getIfPause(){return ifPause;}
        public void setIfPause(boolean ifPause2){ifPause=ifPause2;}
    }

}
