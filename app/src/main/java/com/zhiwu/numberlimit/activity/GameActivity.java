package com.zhiwu.numberlimit.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiwu.numberlimit.gameview.ChallengeModeView;
import com.zhiwu.numberlimit.gameview.ClassicModeView;
import com.zhiwu.numberlimit.gameview.PropModeView;
import com.numberlimit.R;
import com.zhiwu.numberlimit.util.SecuritySharedPreference;

import java.util.HashMap;

public class GameActivity extends Activity implements View.OnClickListener{

    private int height;
    private int width;
    private PopupWindow p=null;
    private View view=null;

    private int num=1;
    private int choosedMaxNum=1;

    private int maxnum=1;
    private int score=0;
    private int stepNum=0;

    private int mode;

    private AudioManager mgr;

    private SoundPool soundPool;//声音池
    private HashMap<Integer, Integer> soundPoolMap; //声音池中声音ID与自定义声音ID的Map
    public boolean ifPlaySound;

    private Button gameover_again;
    private Button gameover_back;
    private ImageView gameover_maxnum;
    private TextView gameover_score;
    private TextView gameover_steps;
    private TextView gameover_score_text;
    private TextView gameover_steps_text;
    private LinearLayout gameover_bg;

    private Button writenum_sub;
    private Button writenum_add;
    private Button writenum_ok;
    private Button writenum_cancel;
    private TextView writenum_num;
    private TextView writenum_scope;

    private ImageButton pause_quit;
    private ImageButton pause_retry;
    private ImageButton pause_continue;


    public Handler hd=new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 0:
                    int[] msg_res= (int[]) msg.obj;
                    maxnum=msg_res[0];
                    score=msg_res[1];
                    stepNum=msg_res[2];
                    //playSound(8,0);
                    showPopWindow(GameActivity.this,view,1);
                    Bitmap maxnumBitmap;
                    if(mode==1) {
                        maxnumBitmap=((ClassicModeView)view).getBitmap(maxnum);
                        gameover_bg.setBackgroundResource(R.drawable.classic_finish);
                    }
                    else if(mode==2) {
                        maxnumBitmap=((PropModeView)view).getBitmap(maxnum);
                        gameover_bg.setBackgroundResource(R.drawable.prop_finish);
                    }
                    else {
                        maxnumBitmap=((ChallengeModeView)view).getBitmap(maxnum);
                        gameover_bg.setBackgroundResource(R.drawable.challenge_finish);
                    }
                    gameover_maxnum.setImageBitmap(maxnumBitmap);
                    Typeface typeFace1 = Typeface.createFromAsset(getAssets(),
                                "font/SIMHEI.ttf");
                    //Typeface typeFace1 = Typeface.createFromAsset(getAssets(),
                    //    "font/BPreplay.ttf");
                    gameover_steps.setTypeface(typeFace1);
                    gameover_score.setTypeface(typeFace1);
                    //Typeface typeFace2 = Typeface.createFromAsset(getAssets(),
                    //        "font/STXINWEI.ttf");
                    gameover_steps_text.setTypeface(typeFace1);
                    gameover_score_text.setTypeface(typeFace1);
                    gameover_steps.setText(stepNum+"");
                    gameover_score.setText(score+"");
                    updateRecord();
                    break;
            }
        }
    };

    public Handler hd2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    choosedMaxNum=Integer.parseInt(String.valueOf(msg.obj));
                    showPopWindow(GameActivity.this,view,2);
                    writenum_scope.setText("可指定数字范围为1到"+choosedMaxNum);
                    num=num/2>1?num/2:1;
                    writenum_num.setText(num+"");
                    break;
                case 1:
                    writenum_num.setText(num+"");
                    break;
                case 2:
                    showPopWindow(GameActivity.this,view,3);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initSound();

        mode=getIntent().getIntExtra("mode",1);
        ifPlaySound=getIntent().getBooleanExtra("ifSound",true);

        System.out.println("act;"+this);
        if(mode==1) {
            view = new ClassicModeView(this);
            setContentView(view);
        }
        else if(mode==2){
            view=new PropModeView(this);
            setContentView(view);
        }
        else{
            view=new ChallengeModeView(this);
            setContentView(view);
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        height = dm.heightPixels;
        width = dm.widthPixels;


    }

    private void initSound(){
        //声音池
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        //吃东西音乐
        soundPoolMap.put(2, soundPool.load(this, R.raw.button, 1));
        soundPoolMap.put(3, soundPool.load(this, R.raw.put, 1));
        soundPoolMap.put(4, soundPool.load(this, R.raw.upanddown, 1));
        soundPoolMap.put(5, soundPool.load(this, R.raw.bomb, 1));
        soundPoolMap.put(6, soundPool.load(this, R.raw.absorb, 1));
        soundPoolMap.put(7, soundPool.load(this, R.raw.merge, 1));
        soundPoolMap.put(8, soundPool.load(this, R.raw.gameover, 1));

        System.out.println("init");
        //soundPoolMap.put(2, soundPool.load(this, R.raw.dong, 1)); //玩家走棋
        //soundPoolMap.put(4, soundPool.load(this, R.raw.win, 1)); //赢了
        //soundPoolMap.put(5, soundPool.load(this, R.raw.loss, 1)); //输了
    }

    //播放声音
    public void playSound(int sound, int loop){
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:{
                if(ifPlaySound) playSound(2,0);
                hd2.sendEmptyMessage(2);
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void showPopWindow(Context context, View parent, int sign)
    {
        System.out.println("popup");
        LayoutInflater inflater=LayoutInflater.from(context);
        View vPopWindow=null;
        PopupWindow popWindow=null;
        if(sign==1) {
            vPopWindow = inflater.inflate(R.layout.popup_gameover, null, false);
            popWindow = new PopupWindow(vPopWindow,width,height,true);
        }
        else if(sign==2) {
            vPopWindow = inflater.inflate(R.layout.popup_writenum, null, false);
            int vw=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
            int vh=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
            vPopWindow.measure(vw,vh);
            popWindow = new PopupWindow(vPopWindow,vPopWindow.getMeasuredWidth(),vPopWindow.getMeasuredHeight(),true);
        }
        else {
            vPopWindow = inflater.inflate(R.layout.popup_pause, null, false);
            int vw=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
            int vh=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
            vPopWindow.measure(vw,vh);
            popWindow = new PopupWindow(vPopWindow,vPopWindow.getMeasuredWidth(),vPopWindow.getMeasuredHeight(),true);
        }

        p=popWindow;
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        if(sign==1) {
            backgroundAlpha(0.3f);
            gameover_again=(Button)vPopWindow.findViewById(R.id.gameover_again);
            gameover_again.setOnClickListener(GameActivity.this);
            gameover_back=(Button)vPopWindow.findViewById(R.id.gameover_back);
            gameover_back.setOnClickListener(GameActivity.this);
            gameover_maxnum=(ImageView) vPopWindow.findViewById(R.id.gameover_maxnum);
            gameover_score=(TextView) vPopWindow.findViewById(R.id.gameover_score);
            gameover_steps=(TextView) vPopWindow.findViewById(R.id.gameover_steps);
            gameover_score_text=(TextView) vPopWindow.findViewById(R.id.gameover_score_text);
            gameover_steps_text=(TextView) vPopWindow.findViewById(R.id.gameover_steps_text);
            gameover_bg=(LinearLayout)vPopWindow.findViewById(R.id.gameover_bg);
        }
        else if(sign==2){
            backgroundAlpha(0.3f);
            writenum_add=(Button)vPopWindow.findViewById(R.id.writenum_add);
            writenum_add.setOnClickListener(GameActivity.this);
            writenum_sub=(Button)vPopWindow.findViewById(R.id.writenum_sub);
            writenum_sub.setOnClickListener(GameActivity.this);
            writenum_ok=(Button)vPopWindow.findViewById(R.id.writenum_ok);
            writenum_ok.setOnClickListener(GameActivity.this);
            writenum_cancel=(Button)vPopWindow.findViewById(R.id.writenum_cancel);
            writenum_cancel.setOnClickListener(GameActivity.this);
            writenum_num=(TextView) vPopWindow.findViewById(R.id.writenum_num);
            writenum_scope=(TextView) vPopWindow.findViewById(R.id.writenum_scope);
        }
        else{
            backgroundAlpha(0.3f);
            pause_continue=(ImageButton)vPopWindow.findViewById(R.id.pause_continue);
            pause_continue.setOnClickListener(GameActivity.this);
            pause_retry=(ImageButton)vPopWindow.findViewById(R.id.pause_retry);
            pause_retry.setOnClickListener(GameActivity.this);
            pause_quit=(ImageButton)vPopWindow.findViewById(R.id.pause_quit);
            pause_quit.setOnClickListener(GameActivity.this);
        }
        popWindow.setOnDismissListener(new GameActivity.poponDismissListener());


    }

    public void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0:不透明到全透明
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gameover_again:
            case R.id.pause_retry:
                if(ifPlaySound) playSound(2,0);
                p.dismiss();
                if(mode==1) {
                    view = new ClassicModeView(this);
                    setContentView(view);
                }
                else if(mode==2){
                    view=new PropModeView(this);
                    setContentView(view);
                }
                else{
                    view=new ChallengeModeView(this);
                    setContentView(view);
                }
                break;
            case R.id.gameover_back:
            case R.id.pause_quit:
                if(ifPlaySound) playSound(2,0);
                System.out.println("back");
                p.dismiss();
                finish();
                break;
            case R.id.writenum_add:
                if(ifPlaySound) playSound(2,0);
                if(num==1) writenum_sub.setClickable(true);
                num++;
                if(num==choosedMaxNum) writenum_add.setClickable(false);
                hd2.sendEmptyMessage(1);
                break;
            case R.id.writenum_sub:
                if(ifPlaySound) playSound(2,0);
                if(num==choosedMaxNum) writenum_add.setClickable(true);
                num--;
                if(num==1) writenum_sub.setClickable(false);
                hd2.sendEmptyMessage(1);
                break;
            case R.id.writenum_ok:
                if(ifPlaySound) playSound(2,0);
                if(mode==2) {
                    ((PropModeView) view).setWritenum(num);
                    p.dismiss();
                    ((PropModeView) view).forPoint();
                }
                else{
                    ((ChallengeModeView) view).setWritenum(num);
                    p.dismiss();
                    ((ChallengeModeView) view).forPoint();
                }
                break;
            case R.id.writenum_cancel:
                if(ifPlaySound) playSound(2,0);
                p.dismiss();
                break;
            case R.id.pause_continue:
                if(ifPlaySound) playSound(2,0);
                p.dismiss();
                break;
        }
    }

    class poponDismissListener implements PopupWindow.OnDismissListener{
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    private void updateRecord(){
        SecuritySharedPreference ssp;
        if(mode==1)
            ssp = new SecuritySharedPreference(this, "classic", Context.MODE_PRIVATE);
        else if(mode==2)
            ssp = new SecuritySharedPreference(this, "prop", Context.MODE_PRIVATE);
        else
            ssp = new SecuritySharedPreference(this, "challenge", Context.MODE_PRIVATE);
        
        SecuritySharedPreference.SecurityEditor se = ssp.edit();
        
        int _wholenum=ssp.getInt("wholenum",0);
        int _maxnum=ssp.getInt("maxnum",0);
        float _avg_maxnum=ssp.getFloat("avg_maxnum",0);
        int _score=ssp.getInt("score",0);
        float _avg_score=ssp.getFloat("avg_score",0);
        int _wholesteps=ssp.getInt("wholesteps",0);
        float _avg_steps=ssp.getFloat("avg_steps",0);

        _wholenum++;
        _maxnum=maxnum>_maxnum?maxnum:_maxnum;
        _avg_maxnum=((_avg_maxnum*(_wholenum-1))+maxnum)/_wholenum;
        _score=score>_score?score:_score;
        _avg_score=((_avg_score*(_wholenum-1))+score)/_wholenum;
        _wholesteps+=stepNum;
        _avg_steps=_wholesteps*1.0f/_wholenum;

        se.putInt("wholenum",_wholenum);
        se.putInt("maxnum", _maxnum);
        se.putFloat("avg_maxnum",remainTwoNumber(_avg_maxnum));
        se.putInt("score",_score);
        se.putFloat("avg_score",remainTwoNumber(_avg_score));
        se.putInt("wholesteps",_wholesteps);
        se.putFloat("avg_steps",remainTwoNumber(_avg_steps));
        se.apply();

    }

    private float remainTwoNumber(float a){
        float b=(float)(Math.round(a*100))/100;
        return b;
    }

}
