package com.numberlimit.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.numberlimit.R;
import com.numberlimit.util.SecuritySharedPreference;

import java.util.HashMap;


public class RecordActivity extends Activity implements View.OnClickListener{

    private TabHost tabHost;
    private TabHost.TabSpec page1;
    private TabHost.TabSpec page2;
    private TabHost.TabSpec page3;

    private TextView classic_wholenum;
    private TextView classic_maxnum;
    private TextView classic_avg_maxnum;
    private TextView classic_score;
    private TextView classic_avg_score;
    private TextView classic_wholesteps;
    private TextView classic_avg_steps;

    private TextView prop_wholenum;
    private TextView prop_maxnum;
    private TextView prop_avg_maxnum;
    private TextView prop_score;
    private TextView prop_avg_score;
    private TextView prop_wholesteps;
    private TextView prop_avg_steps;

    private TextView challenge_wholenum;
    private TextView challenge_maxnum;
    private TextView challenge_avg_maxnum;
    private TextView challenge_score;
    private TextView challenge_avg_score;
    private TextView challenge_wholesteps;
    private TextView challenge_avg_steps;


    private TextView classic1;
    private TextView classic2;
    private TextView classic3;
    private TextView classic4;
    private TextView classic5;
    private TextView classic6;
    private TextView classic7;

    private TextView prop1;
    private TextView prop2;
    private TextView prop3;
    private TextView prop4;
    private TextView prop5;
    private TextView prop6;
    private TextView prop7;

    private TextView challenge1;
    private TextView challenge2;
    private TextView challenge3;
    private TextView challenge4;
    private TextView challenge5;
    private TextView challenge6;
    private TextView challenge7;

    private Button back;
    private Button globalRecord;

    private AudioManager mgr;
    private boolean ifPlaySound;
    private SoundPool soundPool;//声音池
    private HashMap<Integer, Integer> soundPoolMap; //声音池中声音ID与自定义声音ID的Map

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_record);

        mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initSound();
        ifPlaySound=getIntent().getBooleanExtra("ifSound",true);

        tabHost = (TabHost)findViewById(R.id.tabhost);
        tabHost.setup();

        page1 = tabHost.newTabSpec("tab1")
                .setIndicator(" ")
                .setContent(R.id.tab1);

        tabHost.addTab(page1);

        page2 = tabHost.newTabSpec("tab2")
                .setIndicator(" ")
                .setContent(R.id.tab2);

        tabHost.addTab(page2);

        page3 = tabHost.newTabSpec("tab3")
                .setIndicator(" ")
                .setContent(R.id.tab3);

        tabHost.addTab(page3);
        Typeface typeFace1 = Typeface.createFromAsset(getAssets(),
                "font/BPreplay.ttf");
        classic_wholenum=(TextView)findViewById(R.id.classic_wholenum);
        classic_wholenum.setTypeface(typeFace1);
        classic_maxnum=(TextView)findViewById(R.id.classic_maxnum);
        classic_maxnum.setTypeface(typeFace1);
        classic_avg_maxnum=(TextView)findViewById(R.id.classic_avg_maxnum);
        classic_avg_maxnum.setTypeface(typeFace1);
        classic_score=(TextView)findViewById(R.id.classic_score);
        classic_score.setTypeface(typeFace1);
        classic_avg_score=(TextView)findViewById(R.id.classic_avg_score);
        classic_avg_score.setTypeface(typeFace1);
        classic_wholesteps=(TextView)findViewById(R.id.classic_wholesteps);
        classic_wholesteps.setTypeface(typeFace1);
        classic_avg_steps=(TextView)findViewById(R.id.classic_avg_steps);
        classic_avg_steps.setTypeface(typeFace1);

        prop_wholenum=(TextView)findViewById(R.id.prop_wholenum);
        prop_wholenum.setTypeface(typeFace1);
        prop_maxnum=(TextView)findViewById(R.id.prop_maxnum);
        prop_maxnum.setTypeface(typeFace1);
        prop_avg_maxnum=(TextView)findViewById(R.id.prop_avg_maxnum);
        prop_avg_maxnum.setTypeface(typeFace1);
        prop_score=(TextView)findViewById(R.id.prop_score);
        prop_score.setTypeface(typeFace1);
        prop_avg_score=(TextView)findViewById(R.id.prop_avg_score);
        prop_avg_score.setTypeface(typeFace1);
        prop_wholesteps=(TextView)findViewById(R.id.prop_wholesteps);
        prop_wholesteps.setTypeface(typeFace1);
        prop_avg_steps=(TextView)findViewById(R.id.prop_avg_steps);
        prop_avg_steps.setTypeface(typeFace1);

        challenge_wholenum=(TextView)findViewById(R.id.challenge_wholenum);
        challenge_wholenum.setTypeface(typeFace1);
        challenge_maxnum=(TextView)findViewById(R.id.challenge_maxnum);
        challenge_maxnum.setTypeface(typeFace1);
        challenge_avg_maxnum=(TextView)findViewById(R.id.challenge_avg_maxnum);
        challenge_avg_maxnum.setTypeface(typeFace1);
        challenge_score=(TextView)findViewById(R.id.challenge_score);
        challenge_score.setTypeface(typeFace1);
        challenge_avg_score=(TextView)findViewById(R.id.challenge_avg_score);
        challenge_avg_score.setTypeface(typeFace1);
        challenge_wholesteps=(TextView)findViewById(R.id.challenge_wholesteps);
        challenge_wholesteps.setTypeface(typeFace1);
        challenge_avg_steps=(TextView)findViewById(R.id.challenge_avg_steps);
        challenge_avg_steps.setTypeface(typeFace1);

        Typeface typeFace = Typeface.createFromAsset(getAssets(),
                "font/SIMHEI.ttf");

        classic1=(TextView)findViewById(R.id.classic1);
        classic1.setTypeface(typeFace);
        classic2=(TextView)findViewById(R.id.classic2);
        classic2.setTypeface(typeFace);
        classic3=(TextView)findViewById(R.id.classic3);
        classic3.setTypeface(typeFace);
        classic4=(TextView)findViewById(R.id.classic4);
        classic4.setTypeface(typeFace);
        classic5=(TextView)findViewById(R.id.classic5);
        classic5.setTypeface(typeFace);
        classic6=(TextView)findViewById(R.id.classic6);
        classic6.setTypeface(typeFace);
        classic7=(TextView)findViewById(R.id.classic7);
        classic7.setTypeface(typeFace);

        prop1=(TextView)findViewById(R.id.prop1);
        prop1.setTypeface(typeFace);
        prop2=(TextView)findViewById(R.id.prop2);
        prop2.setTypeface(typeFace);
        prop3=(TextView)findViewById(R.id.prop3);
        prop3.setTypeface(typeFace);
        prop4=(TextView)findViewById(R.id.prop4);
        prop4.setTypeface(typeFace);
        prop5=(TextView)findViewById(R.id.prop5);
        prop5.setTypeface(typeFace);
        prop6=(TextView)findViewById(R.id.prop6);
        prop6.setTypeface(typeFace);
        prop7=(TextView)findViewById(R.id.prop7);
        prop7.setTypeface(typeFace);

        challenge1=(TextView)findViewById(R.id.challenge1);
        challenge1.setTypeface(typeFace);
        challenge2=(TextView)findViewById(R.id.challenge2);
        challenge2.setTypeface(typeFace);
        challenge3=(TextView)findViewById(R.id.challenge3);
        challenge3.setTypeface(typeFace);
        challenge4=(TextView)findViewById(R.id.challenge4);
        challenge4.setTypeface(typeFace);
        challenge5=(TextView)findViewById(R.id.challenge5);
        challenge5.setTypeface(typeFace);
        challenge6=(TextView)findViewById(R.id.challenge6);
        challenge6.setTypeface(typeFace);
        challenge7=(TextView)findViewById(R.id.challenge7);
        challenge7.setTypeface(typeFace);

        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(this);
        globalRecord=(Button)findViewById(R.id.globalRecord);
        globalRecord.setOnClickListener(this);

        //saveData();
        Object[] classic_res=loadData("classic");
        classic_wholenum.setText(String.valueOf(classic_res[0]));
        classic_maxnum.setText(String.valueOf(classic_res[1]));
        classic_avg_maxnum.setText(subZeroAndDot(String.valueOf(classic_res[2])));
        classic_score.setText(String.valueOf(classic_res[3]));
        classic_avg_score.setText(subZeroAndDot(String.valueOf(classic_res[4])));
        classic_wholesteps.setText(String.valueOf(classic_res[5]));
        classic_avg_steps.setText(subZeroAndDot(String.valueOf(classic_res[6])));

        Object[] prop_res=loadData("prop");
        prop_wholenum.setText(String.valueOf(prop_res[0]));
        prop_maxnum.setText(String.valueOf(prop_res[1]));
        prop_avg_maxnum.setText(subZeroAndDot(String.valueOf(prop_res[2])));
        prop_score.setText(String.valueOf(prop_res[3]));
        prop_avg_score.setText(subZeroAndDot(String.valueOf(prop_res[4])));
        prop_wholesteps.setText(String.valueOf(prop_res[5]));
        prop_avg_steps.setText(subZeroAndDot(String.valueOf(prop_res[6])));

        Object[] challenge_res=loadData("challenge");
        challenge_wholenum.setText(String.valueOf(challenge_res[0]));
        challenge_maxnum.setText(String.valueOf(challenge_res[1]));
        challenge_avg_maxnum.setText(subZeroAndDot(String.valueOf(challenge_res[2])));
        challenge_score.setText(String.valueOf(challenge_res[3]));
        challenge_avg_score.setText(subZeroAndDot(String.valueOf(challenge_res[4])));
        challenge_wholesteps.setText(String.valueOf(challenge_res[5]));
        challenge_avg_steps.setText(subZeroAndDot(String.valueOf(challenge_res[6])));

        tabHost.getTabWidget().getChildAt(0)
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_classic));;
        tabHost.getTabWidget().getChildAt(1)
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_prop));;
        tabHost.getTabWidget().getChildAt(2)
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_challenge));;
//        updateTab(tabHost);
        for(int i=0;i<3;i++){
            tabHost.getTabWidget().getChildAt(i).setOnClickListener(new MyClickListener(i));
        }

    }

    class MyClickListener implements View.OnClickListener{

        int index;
        public MyClickListener(int index){this.index=index;}

        @Override
        public void onClick(View v) {
            if(ifPlaySound) playSound(2,0);
            tabHost.setCurrentTab(index);
        }
    }

    public void playSound(int sound, int loop){
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;
        System.out.println("play:"+soundPoolMap.containsKey(5)+","+soundPoolMap.toString());
        System.out.println("sound:"+sound);
        soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1);
    }

    private void saveData(){
        SecuritySharedPreference classic_ssp = new SecuritySharedPreference(this, "classic", Context.MODE_PRIVATE);
        SecuritySharedPreference.SecurityEditor classic_se = classic_ssp.edit();

        classic_se.putInt("wholenum", 0);
        classic_se.putInt("maxnum", 0);
        classic_se.putFloat("avg_maxnum",0);
        classic_se.putInt("score",0);
        classic_se.putFloat("avg_score",0);
        classic_se.putInt("wholesteps",0);
        classic_se.putFloat("avg_steps",0);
        classic_se.apply();
    }

    private void initSound(){
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        //soundPoolMap.put(1, soundPool.load(this, R.raw.bgmusic, 1));
        soundPoolMap.put(2, soundPool.load(this, R.raw.button, 1));
        soundPoolMap.put(3, soundPool.load(this, R.raw.put, 1));
        System.out.println("init");
    }

    private Object[] loadData(String fileName){
        SecuritySharedPreference classic_ssp = new SecuritySharedPreference(this, fileName, Context.MODE_PRIVATE);
        //SecuritySharedPreference.SecurityEditor classic_se = classic_ssp.edit();

        Object[] res=new Object[7];
        res[0]=classic_ssp.getInt("wholenum",0);
        res[1]=classic_ssp.getInt("maxnum",0);
        res[2]=classic_ssp.getFloat("avg_maxnum",0);
        res[3]=classic_ssp.getInt("score",0);
        res[4]=classic_ssp.getFloat("avg_score",0);
        res[5]=classic_ssp.getInt("wholesteps",0);
        res[6]=classic_ssp.getFloat("avg_steps",0);

        return res;

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back:
                if(ifPlaySound) playSound(2,0);
                finish();
                break;
            case R.id.globalRecord:
                if(ifPlaySound) playSound(2,0);
                Toast toast = Toast.makeText(this,"正在开发中，敬请期待",Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }

    private String subZeroAndDot(String str){
        if(str.indexOf(".")>0){
            str=str.replaceAll("0+?$","");
            str=str.replaceAll("[.]$","");
        }
        return str;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(ifPlaySound) playSound(2,0);
        }
        return super.onKeyDown(keyCode, event);
    }

}
