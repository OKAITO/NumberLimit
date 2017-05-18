package com.zhiwu.numberlimit.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhiwu.numberlimit.R;
import com.zhiwu.numberlimit.service.MusicService;
import com.zhiwu.numberlimit.util.SecuritySharedPreference;
import com.zhiwu.numberlimit.welcomeview.WelcomeView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends Activity implements View.OnClickListener,Serializable {

	private WelcomeView welcomeView=null;
	private ImageButton start=null;
	private Button classicMode=null;
	private Button propMode=null;
	private Button challengeMode=null;
	private ImageButton back=null;
	private ImageButton setting_back=null;
	private ImageView gamename = null;
	private ImageButton setting= null;
	private ImageButton rank = null;

	private Button setting_music;
	private Button setting_sound;
	private Button setting_teaching;
	private Button setting_us;

	private int height;
	private int width;

	private long exitTime = 0;

	private PopupWindow p;

	private AudioManager mgr;

	private SoundPool soundPool;//声音池
	private HashMap<Integer, Integer> soundPoolMap; //声音池中声音ID与自定义声音ID的Map

	private boolean ifPlaySound=true;
	//private boolean ifPlayMusic=true;
	//private boolean ifPause=false;

	//private MediaPlayer mp;
	//int position=0;
	private MusicService.MusicBinder musicBinder;
	private ServiceConnection connection=new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			System.out.print("init musicBinder");
			musicBinder=(MusicService.MusicBinder)service;
			if (musicBinder.getIfPlayMusic()) {
				musicBinder.startMusic();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};

	public RelativeLayout relayout = null;
	DisplayMetrics dm = new DisplayMetrics();
	float scale=0;

	RelativeLayout.LayoutParams lp =null;
	ImageView img0=null;
	ImageView img1=null;
	ImageView img2=null;
	ImageView img3=null;
	ImageView img4=null;
	ImageView img5=null;
	ImageView img6=null;
	ImageView img7=null;
	ImageView img8=null;
	ImageView img9=null;
	ImageView img10=null;
	ImageView img11=null;
	ImageView img12=null;
	ImageView img13=null;
	ImageView img14=null;
	ImageView img15=null;
	ImageView img16=null;
	ImageView img17=null;
	ImageView img18=null;
	ImageView img19=null;


	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			Random rand = new Random();
			int x = (int)((Math.random() * 0.6 + 0.2)* dm.widthPixels);
			int y = (int)((Math.random() * 0.6 + 0.3)* dm.heightPixels);
			lp = new RelativeLayout.LayoutParams(-2,-2);
			lp.width = dp2px(25.5f);
			lp.height = dp2px(25.5f);
			lp.setMargins(x, y, 0, 0);
			int index = msg.getData().getInt("index");
			int way = msg.getData().getInt("way");
			int time = msg.getData().getInt("time");
			switch (index){
				case 0:
					ani(way, img0, time, index);
					break;
				case 1:
					ani(way, img1, time, index);
					break;
				case 2:
					ani(way, img2, time, index);
					break;
				case 3:
					ani(way, img3, time, index);
					break;
				case 4:
					ani(way, img4, time, index);
					break;
				case 5:
					ani(way, img5, time, index);
					break;
				case 6:
					ani(way, img6, time, index);
					break;
				case 7:
					ani(way, img7, time, index);
					break;
				case 8:
					ani(way, img8, time, index);
					break;
				case 9:
					ani(way, img9, time, index);
					break;
				case 10:
					ani(way, img10, time, index);
					break;
				case 11:
					ani(way, img11, time, index);
					break;
				case 12:
					ani(way, img12, time, index);
					break;
				case 13:
					ani(way, img13, time, index);
					break;
				case 14:
					ani(way, img14, time, index);
					break;
				case 15:
					ani(way, img15, time, index);
					break;
				case 16:
					ani(way, img16, time, index);
					break;
				case 17:
					ani(way, img17, time, index);
					break;
				case 18:
					ani(way, img18, time, index);
					break;
				case 19:
					ani(way, img19, time, index);
					break;
			}
			super.handleMessage(msg);
		}
	};

	public Handler hd=new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 0:
					goToGameView();
					break;
				case 1:
					Intent bindIntent=new Intent(MainActivity.this,MusicService.class);
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
		System.out.println("create");
		mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
		/*if(mp==null) {
			mp=MediaPlayer.create(this, R.raw.bgmusic);
			mp.setVolume(0.3f,0.3f);
		}*/

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		initSound();
		hd.sendEmptyMessage(1);
		System.out.println("bind");

		welcomeView=new WelcomeView(MainActivity.this);
		goToWelcomeView();
	}

	@Override
	protected void onDestroy() {
		saveSound();
		/*if(mp!=null) {
			mp.stop();
			mp.release();
		}*/
		musicBinder.stopMusic();
		unbindService(connection);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onResume");
		if(musicBinder==null) System.out.println("musicBinder==null");
		else {
			System.out.println("musicBinder!=null");
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
		if(musicBinder.getIfPlayMusic()){
			//mp.pause();
			//ifPause=true;
			musicBinder.pauseMusic();
			musicBinder.setIfPause(true);
		}
		super.onPause();
	}

	private void goToWelcomeView(){
		setContentView(welcomeView);

		height=welcomeView.getScreenHeight();
		width=welcomeView.getScreenWidth();
	}
	private void goToGameView(){

		setContentView(R.layout.activity_game);
		loadSound();
		System.out.println("gameview");
		System.out.println("ifPlayMusic:"+musicBinder.getIfPlayMusic());
		if(musicBinder.getIfPlayMusic()){
			//mp.start();
			//mp.setLooping(true);
			musicBinder.startMusic();
			musicBinder.setLoop(true);
		}

		relayout =(RelativeLayout)findViewById(R.id.abslayout);
		gamename=(ImageView)findViewById(R.id.gamename);
		start=(ImageButton)findViewById(R.id.startGame);
		rank=(ImageButton)findViewById(R.id.rank);
		setting=(ImageButton)findViewById(R.id.setting);
		start.setOnClickListener(this);
		rank.setOnClickListener(this);
		setting.setOnClickListener(this);
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		scale=dm.density;
		Random random = new Random();
		for (int i = 0; i<20; i++){
			new Thread(new photo(relayout, i, random.nextInt(10) - 5)).start();
		}

		TranslateAnimation down = new TranslateAnimation(0, 0, -300, 0);
		down.setFillAfter(true);
		down.setInterpolator(new BounceInterpolator());
		down.setDuration(1000);
		gamename.startAnimation(down);

		AnimationSet set =new AnimationSet(true);
		RotateAnimation rotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setFillAfter(true);
		rotate.setDuration(1000);

		AlphaAnimation alpha = new AlphaAnimation(0,1.0f);
		alpha.setDuration(1000);
		set.addAnimation(rotate);
		set.addAnimation(alpha);

		ScaleAnimation scale = new ScaleAnimation(0,1.0f,0,1.0f);
		scale.setDuration(1000);
		set.addAnimation(scale);
		start.startAnimation(set);

		TranslateAnimation right = new TranslateAnimation(-150,0, 0,0);
		right.setFillAfter(true);
		right.setInterpolator(new BounceInterpolator());
		right.setDuration(1000);
		rank.startAnimation(right);

		TranslateAnimation left = new TranslateAnimation(150, 0, 0, 0);
		left.setFillAfter(true);
		left.setInterpolator(new BounceInterpolator());
		left.setDuration(1000);
		setting.startAnimation(left);

	}

	private void initSound(){
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		//soundPoolMap.put(1, soundPool.load(this, R.raw.bgmusic, 1));
		soundPoolMap.put(2, soundPool.load(this, R.raw.button, 1));
		System.out.println("init");
	}

	//播放声音
	public void playSound(int sound, int loop){
		float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCurrent / streamVolumeMax;
		System.out.println("play:"+soundPoolMap.containsKey(5)+","+soundPoolMap.toString());
		System.out.println("sound:"+sound);
		soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1);
	}

	private void beginTransAtranslationYnimation() {
		ObjectAnimator animator = ObjectAnimator.ofFloat(gamename, "translationY", -300, 0);
		animator.setInterpolator(new JumpInterpolator());
		animator.setDuration(1000);
		animator.start();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.startGame:
				if(ifPlaySound) playSound(2,0);
				showPopWindow(MainActivity.this, v, 1);
				break;
			case R.id.rank: {
				if(ifPlaySound) playSound(2,0);
				Intent intent = new Intent(MainActivity.this, RecordActivity.class);
				intent.putExtra("ifSound", ifPlaySound);
				//intent.putExtra("ifMusic", ifPlayMusic);
				startActivity(intent);
			}
				break;
			case R.id.setting:
				if(ifPlaySound) playSound(2,0);
				showPopWindow(MainActivity.this, v, 2);
				break;
			case R.id.ClassicMode: {
				if(ifPlaySound) playSound(2,0);
				p.dismiss();
				Intent intent = new Intent(MainActivity.this, GameActivity.class);
				intent.putExtra("mode", 1);
				intent.putExtra("ifSound", ifPlaySound);
				startActivity(intent);
			}
				break;
			case R.id.PropMode:
				if(ifPlaySound) playSound(2,0);
				p.dismiss();
				Intent intent2=new Intent(MainActivity.this,GameActivity.class);
				intent2.putExtra("mode",2);
				intent2.putExtra("ifSound", ifPlaySound);
				startActivity(intent2);
				break;
			case R.id.ChallengeMode:
				if(ifPlaySound) playSound(2,0);
				p.dismiss();
				Intent intent3=new Intent(MainActivity.this,GameActivity.class);
				intent3.putExtra("mode",3);
				intent3.putExtra("ifSound", ifPlaySound);
				startActivity(intent3);
				break;
			case R.id.back:
				if(ifPlaySound) playSound(2,0);
				p.dismiss();
				break;
			case R.id.setting_back:
				if(ifPlaySound) playSound(2,0);
				p.dismiss();
				break;
			case R.id.setting_music:
				if(ifPlaySound) playSound(2,0);
				if(musicBinder.getIfPlayMusic()){
					musicBinder.setIfPlayMusic(false);
					//ifPlayMusic=false;
					setting_music.setBackgroundResource(R.drawable.btn_music_off);
					//mp.pause();
					musicBinder.pauseMusic();
				}
				else{
					musicBinder.setIfPlayMusic(true);
					//ifPlayMusic=true;
					setting_music.setBackgroundResource(R.drawable.btn_music_on);
					//mp.start();
					musicBinder.startMusic();
				}
				break;
			case R.id.setting_sound:
				if(ifPlaySound) playSound(2,0);
				if(ifPlaySound){
					ifPlaySound=false;
					setting_sound.setBackgroundResource(R.drawable.btn_sound_off);
				}
				else{
					ifPlaySound=true;
					setting_sound.setBackgroundResource(R.drawable.btn_sound_on);
				}
				break;
			case R.id.setting_teaching:
				if(ifPlaySound) playSound(2,0);
				p.dismiss();
				Intent intent4=new Intent(MainActivity.this,HelpActivity.class);
				intent4.putExtra("ifSound",ifPlaySound);
				startActivity(intent4);
				break;
			case R.id.setting_us:
				if(ifPlaySound) playSound(2,0);
				p.dismiss();
				Intent intent5=new Intent(MainActivity.this,AboutUsActivity.class);
				intent5.putExtra("ifSound",ifPlaySound);
				startActivity(intent5);
				break;
		}
	}

	private void saveSound(){
		System.out.println("save");
		SecuritySharedPreference ssp = new SecuritySharedPreference(this, "conf", Context.MODE_PRIVATE);
		SecuritySharedPreference.SecurityEditor se = ssp.edit();
		se.putBoolean("sound",ifPlaySound);
		se.putBoolean("music",musicBinder.getIfPlayMusic());
		se.apply();
	}

	private void loadSound(){
		SecuritySharedPreference ssp = new SecuritySharedPreference(this, "conf", Context.MODE_PRIVATE);
		boolean ifPlayMusic=ssp.getBoolean("music",true);
		musicBinder.setIfPlayMusic(ifPlayMusic);
		ifPlaySound=ssp.getBoolean("sound",true);
	}

	private void showPopWindow(Context context, View parent, int sign){

		LayoutInflater inflater=LayoutInflater.from(context);
		if(sign==1) {
			final View vPopWindow = inflater.inflate(R.layout.popup_choosemodel, null, false);
			final PopupWindow popWindow = new PopupWindow(vPopWindow, width * 2 / 3, height * 5 / 7, true);
			p = popWindow;
			popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
			popWindow.setBackgroundDrawable(new BitmapDrawable());
			backgroundAlpha(0.3f);
			popWindow.setOnDismissListener(new poponDismissListener());

			classicMode = (Button) vPopWindow.findViewById(R.id.ClassicMode);
			classicMode.setOnClickListener(MainActivity.this);

			propMode = (Button) vPopWindow.findViewById(R.id.PropMode);
			propMode.setOnClickListener(MainActivity.this);

			challengeMode = (Button) vPopWindow.findViewById(R.id.ChallengeMode);
			challengeMode.setOnClickListener(MainActivity.this);

			back = (ImageButton) vPopWindow.findViewById(R.id.back);
			back.setOnClickListener(MainActivity.this);
		}
		else{
			final View vPopWindow = inflater.inflate(R.layout.popup_setting, null, false);
			//final PopupWindow popWindow = new PopupWindow(vPopWindow, width * 3 / 4, height * 2/ 7, true);
			int vw=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			int vh=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			vPopWindow.measure(vw,vh);
			final PopupWindow popWindow = new PopupWindow(vPopWindow,vPopWindow.getMeasuredWidth(), vPopWindow.getMeasuredHeight(), true);
			p = popWindow;
			popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
			popWindow.setBackgroundDrawable(new BitmapDrawable());
			backgroundAlpha(0.3f);
			popWindow.setOnDismissListener(new poponDismissListener());

			setting_music = (Button) vPopWindow.findViewById(R.id.setting_music);
			if(musicBinder.getIfPlayMusic()) setting_music.setBackgroundResource(R.drawable.btn_music_on);
			else setting_music.setBackgroundResource(R.drawable.btn_music_off);
			setting_music.setOnClickListener(MainActivity.this);

			setting_sound = (Button) vPopWindow.findViewById(R.id.setting_sound);
			if(ifPlaySound) setting_sound.setBackgroundResource(R.drawable.btn_sound_on);
			else setting_sound.setBackgroundResource(R.drawable.btn_sound_off);
			setting_sound.setOnClickListener(MainActivity.this);

			setting_teaching = (Button) vPopWindow.findViewById(R.id.setting_teaching);
			setting_teaching.setOnClickListener(MainActivity.this);

			setting_us = (Button) vPopWindow.findViewById(R.id.setting_us);
			setting_us.setOnClickListener(MainActivity.this);

			setting_back = (ImageButton) vPopWindow.findViewById(R.id.setting_back);
			setting_back.setOnClickListener(MainActivity.this);

		}

	}

	public void backgroundAlpha(float bgAlpha)  //背景透明度
	{
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		getWindow().setAttributes(lp);
	}

	class poponDismissListener implements PopupWindow.OnDismissListener{
		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}

	}

	public void ani(int way, View v, long time, int index){
		switch (way) {
			case 1:
				alphaHigh(v, time, index);
				relayout.addView(v,0,lp);
				break;
			case 2:
				relayout.removeView(v);
				break;
		}
	}


	public void alphaHigh(View v, long time, int index){
		Random random = new Random();
		int angle = random.nextInt(90) - 45;

		double x = 400 * Math.sin(angle*Math.PI/180);
		double y = -400 * Math.cos(angle*Math.PI/180);

		ObjectAnimator ani = ObjectAnimator.ofFloat(v, "alpha", 1.0f, 0f);
		ani.setDuration(time);

		AnimatorSet set1 = new AnimatorSet();
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 0, 1f);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 0, 1f);
		set1.setDuration(time);
		set1.play(scaleX).with(scaleY);

		AnimatorSet set2 = new AnimatorSet();
		ObjectAnimator tranX = ObjectAnimator.ofFloat(v, "translationX", 0, (int)x);
		ObjectAnimator tranY = ObjectAnimator.ofFloat(v, "translationY", 0, (int)y);
		set2.setDuration(time);
		set2.play(tranX).with(tranY);

		AnimatorSet set3 = new AnimatorSet();
		ObjectAnimator tranX1 = ObjectAnimator.ofFloat(v, "translationX", 0, 2*(int)x);
		ObjectAnimator tranY1 = ObjectAnimator.ofFloat(v, "translationY", 0, 2*(int)y);
		set3.setDuration(2*time);
		set3.play(tranX1).with(tranY1);

		AnimatorSet set4 = new AnimatorSet();
		set4.play(set1).before(ani);

		AnimatorSet set = new AnimatorSet();
		set.play(set3).with(set4);
		set.start();

	}
	class photo implements Runnable {

		RelativeLayout abslayout = null;
		int index;
		int flag;

		public photo(RelativeLayout abslayout, int index, int flag) {
			this.abslayout = abslayout;
			this.index = index;
			this.flag = flag;
		}

		@Override
		public void run() {
			while (true) {
				Random random = new Random();
				int time = random.nextInt(3000) + 2000;
				if (flag < 0){
					try {
						Thread.sleep(time);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					flag = 0;
				}
				if (index == 0){
					img0 = new ImageView(MainActivity.this);
					img0.setImageDrawable(getResources().getDrawable(R.drawable.image0));
				}
				else if (index == 1){
					img1 = new ImageView(MainActivity.this);
					img1.setImageDrawable(getResources().getDrawable(R.drawable.image1));
				}
				else if (index == 2){
					img2 = new ImageView(MainActivity.this);
					img2.setImageDrawable(getResources().getDrawable(R.drawable.image2));
				}
				else if (index == 3){
					img3 = new ImageView(MainActivity.this);
					img3.setImageDrawable(getResources().getDrawable(R.drawable.image3));
				}
				else if (index == 4){
					img4 = new ImageView(MainActivity.this);
					img4.setImageDrawable(getResources().getDrawable(R.drawable.image4));
				}
				else if (index == 5){
					img5 = new ImageView(MainActivity.this);
					img5.setImageDrawable(getResources().getDrawable(R.drawable.image5));
				}
				else if (index == 6){
					img6 = new ImageView(MainActivity.this);
					img6.setImageDrawable(getResources().getDrawable(R.drawable.image6));
				}
				else if (index == 7){
					img7 = new ImageView(MainActivity.this);
					img7.setImageDrawable(getResources().getDrawable(R.drawable.image7));
				}
				else if (index == 8){
					img8 = new ImageView(MainActivity.this);
					img8.setImageDrawable(getResources().getDrawable(R.drawable.image8));
				}
				else if (index == 9){
					img9 = new ImageView(MainActivity.this);
					img9.setImageDrawable(getResources().getDrawable(R.drawable.image9));
				}else if (index == 10){
					img10 = new ImageView(MainActivity.this);
					img10.setImageDrawable(getResources().getDrawable(R.drawable.image0));
				}
				else if (index == 11){
					img11 = new ImageView(MainActivity.this);
					img11.setImageDrawable(getResources().getDrawable(R.drawable.image1));
				}
				else if (index == 12){
					img12 = new ImageView(MainActivity.this);
					img12.setImageDrawable(getResources().getDrawable(R.drawable.image2));
				}
				else if (index == 13){
					img13 = new ImageView(MainActivity.this);
					img13.setImageDrawable(getResources().getDrawable(R.drawable.image3));
				}
				else if (index == 14){
					img14 = new ImageView(MainActivity.this);
					img14.setImageDrawable(getResources().getDrawable(R.drawable.image4));
				}
				else if (index == 15){
					img15 = new ImageView(MainActivity.this);
					img15.setImageDrawable(getResources().getDrawable(R.drawable.image5));
				}
				else if (index == 16){
					img16 = new ImageView(MainActivity.this);
					img16.setImageDrawable(getResources().getDrawable(R.drawable.image6));
				}
				else if (index == 17){
					img17 = new ImageView(MainActivity.this);
					img17.setImageDrawable(getResources().getDrawable(R.drawable.image7));
				}
				else if (index == 18){
					img18 = new ImageView(MainActivity.this);
					img18.setImageDrawable(getResources().getDrawable(R.drawable.image8));
				}
				else if (index == 19){
					img19 = new ImageView(MainActivity.this);
					img19.setImageDrawable(getResources().getDrawable(R.drawable.image9));
				}

				Message msg1 = Message.obtain();
				Bundle bundle1 = new Bundle();
				bundle1.putInt("way", 1);
				bundle1.putInt("index", index);
				bundle1.putInt("time", time);
				msg1.setData(bundle1);
				handler.sendMessage(msg1);

				try {
					Thread.sleep(2*time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg2 = Message.obtain();
				Bundle bundle2 = new Bundle();
				bundle2.putInt("way", 2);
				bundle2.putInt("index", index);
				bundle2.putInt("time", time);
				msg2.setData(bundle2);
				handler.sendMessage(msg2);
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public class JumpInterpolator implements TimeInterpolator {

		public float getInterpolation(float input) {
			if (input <= 4/11f) {
				return 121 / 16f * input * input;
			} else if (input <= 8/11f) {
				return 1 / 2f + 121 / 8f * (input - 6 / 11f) * (input - 6 / 11f);
			} else if (input <= 10/11f){
				return 3 / 4f + 121 / 4f * (input - 9 / 11f) * (input - 9 / 11f);
			}else{
				return 7 / 8f + 121 / 2f * (input - 21 / 22f) * (input - 21 / 22f);
			}

		}
	}

	private void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出游戏",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			//System.exit(-1);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(ifPlaySound) playSound(2,0);
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private int dp2px(float dpValue){
		return (int)(dpValue*scale+0.5f);
	}

}
