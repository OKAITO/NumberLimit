package com.numberlimit.welcomeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.numberlimit.R;
import com.numberlimit.activity.MainActivity;

public class WelcomeView extends SurfaceView
        implements SurfaceHolder.Callback   //实现生命周期回调接口
{
    private MainActivity activity;//activity的引用
    private Paint paint;      //画笔
    private int currentAlpha=0;  //当前的不透明值
    private int height;
    private int width;
    private int sleepSpan=50;      //动画的时延ms
    //Bitmap[] logos=new Bitmap[1];//logo图片数组
    private Bitmap showLogo;  //当前logo图片引用
    private int currentX;      //图片位置
    private int currentY;
    public WelcomeView(MainActivity activity)
    {
        super(activity);
        this.activity = activity;
        this.getHolder().addCallback(this);  //设置生命周期回调接口的实现者
        paint = new Paint();  //创建画笔
        paint.setAntiAlias(true);  //打开抗锯齿
        //加载图片
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        Bitmap logo= BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        int logoHeight=logo.getHeight();
        int logoWidth=logo.getWidth();
        Matrix logoMatrix=new Matrix();
        float scaleWidth=(width*0.5f)/logoWidth;
        float scaleHeight=scaleWidth;
        logoMatrix.postScale(scaleWidth,scaleHeight);
        Bitmap newLogo=Bitmap.createBitmap(logo,0,0,logoWidth,logoHeight,logoMatrix,true);

        //logos[0]=newLogo;
        showLogo=newLogo;
    }
    public void draw(Canvas canvas)
    {
        //绘制黑填充矩形清背景
        paint.setColor(Color.BLACK);//设置画笔颜色
        paint.setAlpha(255);//设置不透明度为255
        canvas.drawRect(0, 0, width, height, paint);
        //进行平面贴图
        if(showLogo==null)return;
        paint.setAlpha(currentAlpha);
        canvas.drawBitmap(showLogo, currentX, currentY, paint);
    }
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {
    }
    public void surfaceCreated(SurfaceHolder holder) //创建时被调用
    {
        new Thread()
        {
            public void run()
            {
                    currentX=(int) (width*1.0/2-showLogo.getWidth()*1.0/2);//图片位置
                    currentY=(int) (height*1.0/2-showLogo.getHeight()*1.0/2);
                    for(int i=255;i>-10;i=i-10)
                    {//动态更改图片的透明度值并不断重绘
                        currentAlpha=i;
                        if(currentAlpha<0){
                            currentAlpha=0;//将不透明度置为零
                        }
                        SurfaceHolder myholder=WelcomeView.this.getHolder();//获取回调接口
                        Canvas canvas = myholder.lockCanvas();//获取画布
                        try{
                            synchronized(myholder){
                                draw(canvas);//进行绘制绘制
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                        finally{
                            if(canvas!= null){
                                myholder.unlockCanvasAndPost(canvas);//解锁画布
                            }
                        }
                        try{
                            if(i==255){
                                Thread.sleep(1000);
                            }
                            Thread.sleep(sleepSpan);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                System.out.println("welcome over");
                activity.hd.sendEmptyMessage(0);
                }

        }.start();
    }
    public void surfaceDestroyed(SurfaceHolder arg0)
    {//销毁时被调用
    }

    public int getScreenHeight(){ return height; }
    public int getScreenWidth(){ return width; }
}