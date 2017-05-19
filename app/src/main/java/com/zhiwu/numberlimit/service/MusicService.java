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
        if(mp==null) {
            mp=MediaPlayer.create(this, R.raw.bgmusic);
            mp.setVolume(0.3f,0.3f);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    public class MusicBinder extends Binder{
        public void startMusic(){
            try{
                mp.start();
            }catch(IllegalStateException e){
                mp=null;
                mp=MediaPlayer.create(MusicService.this, R.raw.bgmusic);
                mp.setVolume(0.3f,0.3f);
                mp.start();
            }
        }
        public void pauseMusic(){if(mp!=null) mp.pause();}
        public void stopMusic(){if(mp!=null) mp.stop();mp.release();}
        public void setLoop(boolean ifLoop){if(mp!=null) mp.setLooping(ifLoop);}
        public boolean getIfPlayMusic(){return ifPlayMusic;}
        public void setIfPlayMusic(boolean ifPlayMusic2){ifPlayMusic=ifPlayMusic2;}
        public boolean getIfPause(){return ifPause;}
        public void setIfPause(boolean ifPause2){ifPause=ifPause2;}
    }

}
