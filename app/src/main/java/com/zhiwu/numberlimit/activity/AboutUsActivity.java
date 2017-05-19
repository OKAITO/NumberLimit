package com.zhiwu.numberlimit.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.zhiwu.numberlimit.R;
import com.zhiwu.numberlimit.service.MusicService;

import java.util.HashMap;

public class AboutUsActivity extends Activity {

    private Button back;
    private boolean ifPlaySound;
    private AudioManager mgr;

    private SoundPool soundPool;//声音池
    private HashMap<Integer, Integer> soundPoolMap; //声音池中声音ID与自定义声音ID的Map

    private MusicService.MusicBinder musicBinder;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder=(MusicService.MusicBinder)service;
            if (musicBinder!=null && musicBinder.getIfPlayMusic()) {
                musicBinder.startMusic();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public Handler hd=new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    Intent bindIntent=new Intent(AboutUsActivity.this,MusicService.class);
                    bindService(bindIntent,connection,BIND_AUTO_CREATE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hd.sendEmptyMessage(1);
        setContentView(R.layout.layout_about_us);

        mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initSound();
        ifPlaySound=getIntent().getBooleanExtra("ifSound",true);

        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ifPlaySound) playSound(2,0);
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(musicBinder!=null) {
            if (musicBinder.getIfPlayMusic()) {
                if (musicBinder.getIfPause()) {
                    //mp.start();
                    musicBinder.startMusic();
                    //ifPause=false;
                    musicBinder.setIfPause(false);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        if(musicBinder!=null && musicBinder.getIfPlayMusic()){
            //mp.pause();
            //ifPause=true;
            musicBinder.pauseMusic();
            musicBinder.setIfPause(true);
        }
        super.onPause();
    }

    private void initSound(){
        //声音池
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(2, soundPool.load(this, R.raw.button, 1));

    }

    //播放声音
    public void playSound(int sound, int loop)
    {
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;
        if(sound==2)
            soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
        else
            soundPool.play(soundPoolMap.get(sound), 1, 1, 1, loop, 1f);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(ifPlaySound) playSound(2,0);
        }
        return super.onKeyDown(keyCode, event);
    }

}
