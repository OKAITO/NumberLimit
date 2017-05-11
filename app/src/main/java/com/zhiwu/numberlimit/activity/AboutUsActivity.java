package com.zhiwu.numberlimit.activity;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.numberlimit.R;

import java.util.HashMap;

public class AboutUsActivity extends Activity {

    private Button back;
    private boolean ifPlaySound;
    private AudioManager mgr;

    private SoundPool soundPool;//声音池
    private HashMap<Integer, Integer> soundPoolMap; //声音池中声音ID与自定义声音ID的Map

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

    private void initSound(){
        //声音池
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(2, soundPool.load(this, R.raw.button, 1));

        System.out.println("init");
    }

    //播放声音
    public void playSound(int sound, int loop)
    {
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;
        System.out.println("play:"+soundPoolMap.containsKey(5)+","+soundPoolMap.toString());
        System.out.println("sound:"+sound);
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
