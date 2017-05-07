package com.numberlimit.activity;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.numberlimit.R;

import java.util.HashMap;

public class HelpActivity extends Activity implements ViewSwitcher.ViewFactory, View.OnTouchListener {

    private ImageSwitcher mImageSwitcher;
    private int[] imgIds;
    private int currentPosition;
    private float downX;
    private LinearLayout linearLayout;
    private ImageView[] tips;
    private float scale;

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

        setContentView(R.layout.layout_help);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        scale=dm.density;

        mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initSound();
        ifPlaySound=getIntent().getBooleanExtra("ifSound",true);

        imgIds = new int[]{R.drawable.help1,R.drawable.help2,R.drawable.help3,R.drawable.help4,R.drawable.help5,R.drawable.help6,R.drawable.help7,R.drawable.help8,R.drawable.help9};
        //实例化ImageSwitcher
        mImageSwitcher  = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
        //设置Factory
        mImageSwitcher.setFactory(this);
        //设置OnTouchListener，我们通过Touch事件来切换图片
        mImageSwitcher.setOnTouchListener(this);

        linearLayout = (LinearLayout) findViewById(R.id.viewGroup);

        tips = new ImageView[imgIds.length];
        for(int i=0; i<imgIds.length; i++){
            ImageView mImageView = new ImageView(this);
            tips[i] = mImageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.rightMargin = 8;
            layoutParams.leftMargin = 8;
            layoutParams.bottomMargin = dp2px(40)-9;

            mImageView.setBackgroundResource(R.drawable.others);
            linearLayout.addView(mImageView, layoutParams);
        }

        currentPosition = 0;
        mImageSwitcher.setImageResource(imgIds[currentPosition]);

        setImageBackground(currentPosition);

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

    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.current);
            }else{
                tips[i].setBackgroundResource(R.drawable.others);
            }
        }
    }

    @Override
    public View makeView() {
        final ImageView i = new ImageView(this);
        i.setBackgroundColor(0xff000000);
        i.setScaleType(ImageView.ScaleType.FIT_XY);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return i ;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                //手指按下的X坐标
                downX = event.getX();
                break;
            }
            case MotionEvent.ACTION_UP:{
                float lastX = event.getX();
                //抬起的时候的X坐标大于按下的时候就显示上一张图片
                if(lastX > downX){
                    if(currentPosition > 0){
                        //设置动画，这里的动画比较简单，不明白的去网上看看相关内容
                        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_in));
                        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_out));
                        currentPosition--;
                        mImageSwitcher.setImageResource(imgIds[currentPosition % imgIds.length]);
                        setImageBackground(currentPosition);
                    }else{
                        Toast.makeText(getApplication(), "已经是第一张", Toast.LENGTH_SHORT).show();
                    }
                }

                if(lastX < downX){
                    if(currentPosition < imgIds.length - 1){
                        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_in));
                        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_out));
                        currentPosition ++ ;
                        mImageSwitcher.setImageResource(imgIds[currentPosition]);
                        setImageBackground(currentPosition);
                    }else{
                        Toast.makeText(getApplication(), "到了最后一张", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            break;
        }

        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(ifPlaySound) playSound(2,0);
        }
        return super.onKeyDown(keyCode, event);
    }

    private int dp2px(float dpValue){
        return (int)(dpValue*scale+0.5f);
    }

}
