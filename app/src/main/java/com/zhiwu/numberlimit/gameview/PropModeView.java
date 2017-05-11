package com.zhiwu.numberlimit.gameview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.zhiwu.numberlimit.entity.Card;
import com.numberlimit.R;
import com.zhiwu.numberlimit.activity.GameActivity;

import java.util.ArrayList;
import java.util.TreeMap;

public class PropModeView extends View {

    private int height; //设备参数
    private int width;
    private float scale;
    private float fontScale;

    private int cardLength; //游戏参数
    private int dimension=5;
    private int maxnum=2;
    private int choosedMaxNum=1;
    private int score=0;
    private int stepNum=0;

    private int writenum=-1;

    private GameActivity gameActivity;

    private Card[][] cards=new Card[dimension][dimension]; //卡片保存参数
    private Card[][] oldCards=new Card[dimension][dimension];
    private Card[] chooseCards=new Card[3];
    private ArrayList<Card> freeCards=new ArrayList<Card>();
    private ArrayList<Card> toBeChangedCards=new ArrayList<Card>();
    private Card nextCard=null;
    private Card chooseCard=null;
    private int chooseIndex=-1;

    private Card pointCard;
    private int pointIndex;

    private ArrayList<Card> toBeDeletedCards=new ArrayList<Card>();
    private int deleteStep=0;
    private int deleteMaxNum=0;

    private int randomSeed=137;
    private float current_x=-1; //卡片移动参数
    private float current_y=-1;
    private float x_dif=-1;
    private float y_dif=-1;

    private boolean ifStart=true; //标志位
    private boolean ifMove=false;
    private int ifEnd=0;

    private boolean ifRotate=false;
    private int rotateIndex=0;
    private int endIndex=0;

    private ArrayList<Bitmap> bitmaps=new ArrayList<Bitmap>();
    private ArrayList<Bitmap> proptimeBitmaps=new ArrayList<Bitmap>();

    private Bitmap bgBitmap;
    private Bitmap mode_title;
    private Bitmap btn_pause;

    public PropModeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gameActivity=(GameActivity)context;
        init();
    }
    public PropModeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameActivity=(GameActivity)context;
        init();
    }

    public PropModeView(Context context) {
        super(context);
        gameActivity=(GameActivity)context;
        init();
    }

    private void init(){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;
        scale=metrics.density;
        fontScale=metrics.scaledDensity;

        cardLength=(width-(dimension+1)*20)/dimension;

        ArrayList<Bitmap> tempBitmaps=new ArrayList<Bitmap>();
        Bitmap bitmap0= BitmapFactory.decodeResource(getResources(), R.drawable.blank);
        tempBitmaps.add(bitmap0);
        Bitmap bitmap1= BitmapFactory.decodeResource(getResources(),R.drawable.game01);
        tempBitmaps.add(bitmap1);
        Bitmap bitmap2= BitmapFactory.decodeResource(getResources(),R.drawable.game02);
        tempBitmaps.add(bitmap2);
        Bitmap bitmap3= BitmapFactory.decodeResource(getResources(),R.drawable.game03);
        tempBitmaps.add(bitmap3);
        Bitmap bitmap4= BitmapFactory.decodeResource(getResources(),R.drawable.game04);
        tempBitmaps.add(bitmap4);
        Bitmap bitmap5= BitmapFactory.decodeResource(getResources(),R.drawable.game05);
        tempBitmaps.add(bitmap5);
        Bitmap bitmap6= BitmapFactory.decodeResource(getResources(),R.drawable.game06);
        tempBitmaps.add(bitmap6);
        Bitmap bitmap7= BitmapFactory.decodeResource(getResources(),R.drawable.game07);
        tempBitmaps.add(bitmap7);
        Bitmap bitmap8= BitmapFactory.decodeResource(getResources(),R.drawable.game08);
        tempBitmaps.add(bitmap8);
        Bitmap bitmap9= BitmapFactory.decodeResource(getResources(),R.drawable.game09);
        tempBitmaps.add(bitmap9);
        Bitmap bitmap10= BitmapFactory.decodeResource(getResources(),R.drawable.game10);
        tempBitmaps.add(bitmap10);
        Bitmap bitmap26= BitmapFactory.decodeResource(getResources(),R.drawable.game11);
        tempBitmaps.add(bitmap26);
        Bitmap bitmap27= BitmapFactory.decodeResource(getResources(),R.drawable.game12);
        tempBitmaps.add(bitmap27);
        Bitmap bitmap28= BitmapFactory.decodeResource(getResources(),R.drawable.game13);
        tempBitmaps.add(bitmap28);
        Bitmap bitmap24= BitmapFactory.decodeResource(getResources(),R.drawable.game14);
        tempBitmaps.add(bitmap24);
        Bitmap bitmap25= BitmapFactory.decodeResource(getResources(),R.drawable.game15);
        tempBitmaps.add(bitmap25);
        Bitmap bitmap11= BitmapFactory.decodeResource(getResources(),R.drawable.obstacle3);
        tempBitmaps.add(bitmap11);
        Bitmap bitmap12= BitmapFactory.decodeResource(getResources(),R.drawable.obstacle2);
        tempBitmaps.add(bitmap12);
        Bitmap bitmap13= BitmapFactory.decodeResource(getResources(),R.drawable.obstacle1);
        tempBitmaps.add(bitmap13);
        Bitmap bitmap14= BitmapFactory.decodeResource(getResources(),R.drawable.point);
        tempBitmaps.add(bitmap14);
        Bitmap bitmap15= BitmapFactory.decodeResource(getResources(),R.drawable.vortex);
        tempBitmaps.add(bitmap15);
        Bitmap bitmap16= BitmapFactory.decodeResource(getResources(),R.drawable.random);
        tempBitmaps.add(bitmap16);
        Bitmap bitmap17= BitmapFactory.decodeResource(getResources(),R.drawable.bomb);
        tempBitmaps.add(bitmap17);
        Bitmap bitmap18= BitmapFactory.decodeResource(getResources(),R.drawable.down);
        tempBitmaps.add(bitmap18);
        Bitmap bitmap19= BitmapFactory.decodeResource(getResources(),R.drawable.up);
        tempBitmaps.add(bitmap19);
        Bitmap bitmap20= BitmapFactory.decodeResource(getResources(),R.drawable.next);
        tempBitmaps.add(bitmap20);

        for(int i=0;i<tempBitmaps.size();i++){
            int height=tempBitmaps.get(i).getHeight();
            int width=tempBitmaps.get(i).getWidth();
            Matrix matrix=new Matrix();
            float scaleHeight=cardLength*1.0f/height;
            matrix.postScale(scaleHeight,scaleHeight);
            Bitmap newBitmap=Bitmap.createBitmap(tempBitmaps.get(i),0,0,width,height,matrix,true);
            bitmaps.add(newBitmap);
        }

        ArrayList<Bitmap> tempBitmaps2=new ArrayList<Bitmap>();
        Bitmap bitmap21= BitmapFactory.decodeResource(getResources(),R.drawable.proptime1);
        tempBitmaps2.add(bitmap21);
        Bitmap bitmap22= BitmapFactory.decodeResource(getResources(),R.drawable.proptime2);
        tempBitmaps2.add(bitmap22);
        Bitmap bitmap23= BitmapFactory.decodeResource(getResources(),R.drawable.proptime3);
        tempBitmaps2.add(bitmap23);

        for(int i=0;i<tempBitmaps2.size();i++){
            int height=tempBitmaps2.get(i).getHeight();
            int width=tempBitmaps2.get(i).getWidth();
            Matrix matrix=new Matrix();
            float scaleHeight=cardLength*2.0f/(height*23);
            float scaleWidth=cardLength*3.0f/(width*5);
            matrix.postScale(scaleWidth,scaleHeight);
            Bitmap newBitmap=Bitmap.createBitmap(tempBitmaps2.get(i),0,0,width,height,matrix,true);
            proptimeBitmaps.add(newBitmap);
        }

        Bitmap tempBgBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.prop_bg);
        int bgHeight=tempBgBitmap.getHeight();
        int bgWidth=tempBgBitmap.getWidth();
        Matrix bgMatrix=new Matrix();
        float bgScaleHeight=height*1.0f/bgHeight;
        float bgScaleWidth=width*1.0f/bgWidth;
        bgMatrix.postScale(bgScaleWidth,bgScaleHeight);
        bgBitmap=Bitmap.createBitmap(tempBgBitmap,0,0,bgWidth,bgHeight,bgMatrix,true);

        Bitmap titleBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.prop_title);
        int titleHeight=titleBitmap.getHeight();
        int titleWidth=titleBitmap.getWidth();
        Matrix titleMatrix=new Matrix();
        float titleScale=width*1.0f/(3*titleWidth);
        titleMatrix.postScale(titleScale,titleScale);
        mode_title=Bitmap.createBitmap(titleBitmap,0,0,titleWidth,titleHeight,titleMatrix,true);

        Bitmap pauseBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.btn_pause);
        int pauseHeight=pauseBitmap.getHeight();
        int pauseWidth=pauseBitmap.getWidth();
        Matrix pauseMatrix=new Matrix();
        float pauseScale=(mode_title.getHeight()*5f)/(pauseHeight*6);
        pauseMatrix.postScale(pauseScale,pauseScale);
        btn_pause=Bitmap.createBitmap(pauseBitmap,0,0,pauseWidth,pauseHeight,pauseMatrix,true);

    }

    public Bitmap getBitmap(int num){
        if(num<0)
            return bitmaps.get(bitmaps.size()+num);
        else if(num>=0 && num<=bitmaps.size()-12)
            return bitmaps.get(num);
        else return bitmaps.get(bitmaps.size()-11);
    }

    public Bitmap getProptimeBitmap(int proptime){
        return proptimeBitmaps.get(proptime-1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint=new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bgBitmap,0,0,paint);

        canvas.drawBitmap(mode_title,(width-mode_title.getWidth())/2,dp2px(50),paint);
        canvas.drawBitmap(btn_pause,dp2px(30),dp2px(50)+(mode_title.getHeight()*1f)/12,paint);

        float scoreTop=dp2px(50)+mode_title.getHeight()+dp2px(20);
        //scoreToBitmap(canvas,scoreTop);

        paint.setTextSize(sp2px(40));
        paint.setColor(0xff6646e6);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.createFromAsset(gameActivity.getAssets(),"font/BPreplay.ttf"));
        Paint.FontMetrics fm=paint.getFontMetrics();
        float textHeight=fm.descent-fm.ascent;
        RectF textRectF=new RectF(0,0,width,scoreTop+textHeight);
        canvas.drawText(maxnum+"/"+score,textRectF.left+textRectF.width()/2,textRectF.bottom-fm.descent,paint);

        float cardTop=textRectF.bottom+dp2px(20);
        float chooseCardTop=cardTop+dimension*cardLength;
        chooseCardTop=(chooseCardTop+height)/2-cardLength/2;
        float margin=(width-4*cardLength)/6;

        if(ifStart) {
            ifStart = false;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    float left=40+j*10+j*cardLength;
                    float top=cardTop+10*i+i*cardLength;
                    cards[i][j] = new Card(left, top, cardLength, 0);
                    cards[i][j].setM(i);
                    cards[i][j].setN(j);
                    freeCards.add(cards[i][j]);
                }
            }
            int rand1 = randomNumber(0, freeCards.size());
            freeCards.get(rand1).setNum(1);
            freeCards.remove(rand1);
            int rand2 = randomNumber(0, freeCards.size());
            freeCards.get(rand2).setNum(1);
            freeCards.remove(rand2);
            int rand3 = randomNumber(0, freeCards.size());
            freeCards.get(rand3).setNum(2);
            freeCards.remove(rand3);
            int rand4 = randomNumber(0, freeCards.size());
            freeCards.get(rand4).setNum(-8);
            freeCards.remove(rand4);
            //int rand5 = randomNumber(0, freeCards.size());
            //freeCards.get(rand5).setNum(-8);
            //freeCards.remove(rand5);

            for(int i=0;i<dimension;i++){
                for(int j=0;j<dimension;j++){
                    cards[i][j].drawCard_init(this,canvas,2);
                }
            }

            chooseCards[0]=new Card(margin,chooseCardTop,cardLength,1);
            chooseCards[0].drawCard_init(this,canvas,2);

            chooseCards[1]=new Card(2*margin+cardLength,chooseCardTop,cardLength,1);
            chooseCards[1].drawCard_init(this,canvas,2);

            chooseCards[2]=new Card(3*margin+2*cardLength,chooseCardTop,cardLength,1);
            chooseCards[2].drawCard_init(this,canvas,2);

            nextCard=new Card(5*margin+3*cardLength,chooseCardTop,cardLength,1);
            nextCard.drawCard_init(this,canvas,2);
            canvas.drawBitmap(getBitmap(-1), 5 * margin + 3 * cardLength, chooseCardTop, paint);
            invalidate();
        }

        else {

            if(ifEnd==2){

                for (int i = 0; i < dimension; i++) {
                    for (int j = 0; j < dimension; j++) {
                        //System.out.println("i="+i+",j="+j);
                        cards[i][j].drawCard_rotate(this, canvas, endIndex, 2);
                    }
                }

                chooseCards[0].drawCard_init(this, canvas, 2);
                chooseCards[1].drawCard_init(this, canvas, 2);
                chooseCards[2].drawCard_init(this, canvas, 2);
                nextCard.drawCard_init(this, canvas, 2);

                //if(angleIndex<=10) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (endIndex <= 15) {
                            endIndex++;
                            try {
                                Thread.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            postInvalidate();
                        }
                        else{
                            Message msg=gameActivity.hd.obtainMessage();
                            int[] msg_res=new int[3];
                            msg_res[0]=maxnum;
                            msg_res[1]=score;
                            msg_res[2]=stepNum;
                            msg.what=0;
                            msg.obj=msg_res;
                            gameActivity.hd.sendMessage(msg);
                            System.out.println("view:"+gameActivity);
                        }
                    }
                }).start();
            }
            else {

                if(ifEnd==1) {ifEnd++;if(gameActivity.ifPlaySound) gameActivity.playSound(8,0);invalidate();}

                if (ifRotate) {

                    if(gameActivity.ifPlaySound) gameActivity.playSound(7,0);
                    for (int i = 0; i < dimension; i++) {
                        for (int j = 0; j < dimension; j++) {
                            if (cards[i][j].getNum() > 0)
                                cards[i][j].drawCard_rotate(this, canvas, rotateIndex, 2);
                            else
                                cards[i][j].drawCard_init(this, canvas, 2);
                        }
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (rotateIndex <= 7) {
                                rotateIndex++;
                                try {
                                    Thread.sleep(5);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                postInvalidate();
                            } else {
                                ifRotate = false;
                                rotateIndex = 0;
                            }
                        }
                    }).start();
                } else {

                    for (int i = 0; i < dimension; i++) {
                        for (int j = 0; j < dimension; j++) {
                            cards[i][j].drawCard_init(this, canvas, 2);
                        }
                    }
                }

                chooseCards[0].drawCard_init(this, canvas, 2);
                chooseCards[1].drawCard_init(this, canvas, 2);
                chooseCards[2].drawCard_init(this, canvas, 2);
                nextCard.drawCard_init(this, canvas, 2);
                canvas.drawBitmap(getBitmap(-1), 5 * margin + 3 * cardLength, chooseCardTop, paint);

                if (ifMove) {

                    if (chooseCard != null) {
                        for (int i = 0; i < freeCards.size(); i++) {
                            if (freeCards.get(i).ifIntersect(chooseCard) && freeCards.get(i).getNum() == 0) {
                                freeCards.get(i).drawCard_hint(this, canvas, chooseCard, 2);
                                break;
                            }
                        }
                    }
                    canvas.drawBitmap(getBitmap(0), chooseCards[chooseIndex].getLeft(), chooseCards[chooseIndex].getTop(), paint);
                    chooseCard.drawCard_move(this, canvas, current_x, current_y, x_dif, y_dif, 2);
                }

                if (toBeDeletedCards.size() > 0) {
                    System.out.println("start");
                    for (int i = 0; i < toBeDeletedCards.size(); i++) {
                        Card tempCard = toBeDeletedCards.get(i);
                        if (tempCard.getM() > 0 && cards[tempCard.getM() - 1][tempCard.getN()].getDeleteNum() == tempCard.getDeleteNum() - 1) {
                            System.out.println("i=" + i + ",M>0");
                            System.out.println("step=" + deleteStep);
                            Paint tempPaint = new Paint();
                            tempPaint.setAntiAlias(true);
                            tempPaint.setAlpha(128);
                            Bitmap tempBitmap = getBitmap(cards[tempCard.getM() - 1][tempCard.getN()].getNum());
                            int tempHeight = tempBitmap.getHeight();
                            int tempWidth = tempBitmap.getWidth();
                            Bitmap tempBitmap2 = Bitmap.createBitmap(tempBitmap, 0, tempHeight * (deleteStep + 1) / 4, tempWidth, tempHeight * (3 - deleteStep) / 4);
                            canvas.drawBitmap(tempBitmap2, tempCard.getLeft(), tempCard.getTop(), tempPaint);
                        }
                        if (tempCard.getM() < dimension - 1 && cards[tempCard.getM() + 1][tempCard.getN()].getDeleteNum() == tempCard.getDeleteNum() - 1) {
                            System.out.println("i=" + i + ",M<dim");
                            System.out.println("step=" + deleteStep);
                            Paint tempPaint = new Paint();
                            tempPaint.setAntiAlias(true);
                            tempPaint.setAlpha(128);
                            Bitmap tempBitmap = getBitmap(cards[tempCard.getM() + 1][tempCard.getN()].getNum());
                            int tempHeight = tempBitmap.getHeight();
                            int tempWidth = tempBitmap.getWidth();
                            Bitmap tempBitmap2 = Bitmap.createBitmap(tempBitmap, 0, 0, tempWidth, tempHeight * (3 - deleteStep) / 4);
                            canvas.drawBitmap(tempBitmap2, tempCard.getLeft(), tempCard.getTop() + tempHeight * (deleteStep + 1) / 4, tempPaint);
                        }
                        if (tempCard.getN() > 0 && cards[tempCard.getM()][tempCard.getN() - 1].getDeleteNum() == tempCard.getDeleteNum() - 1) {
                            System.out.println("i=" + i + ",N>0");
                            System.out.println("step=" + deleteStep);
                            Paint tempPaint = new Paint();
                            tempPaint.setAntiAlias(true);
                            tempPaint.setAlpha(128);
                            Bitmap tempBitmap = getBitmap(cards[tempCard.getM()][tempCard.getN() - 1].getNum());
                            int tempHeight = tempBitmap.getHeight();
                            int tempWidth = tempBitmap.getWidth();
                            Bitmap tempBitmap2 = Bitmap.createBitmap(tempBitmap, tempWidth * (deleteStep + 1) / 4, 0, tempWidth * (3 - deleteStep) / 4, tempHeight);
                            canvas.drawBitmap(tempBitmap2, tempCard.getLeft(), tempCard.getTop(), tempPaint);
                        }
                        if (tempCard.getN() < dimension - 1 && cards[tempCard.getM()][tempCard.getN() + 1].getDeleteNum() == tempCard.getDeleteNum() - 1) {
                            System.out.println("i=" + i + ",N<dim");
                            System.out.println("step=" + deleteStep);
                            Paint tempPaint = new Paint();
                            tempPaint.setAntiAlias(true);
                            tempPaint.setAlpha(128);
                            Bitmap tempBitmap = getBitmap(cards[tempCard.getM()][tempCard.getN() + 1].getNum());
                            int tempHeight = tempBitmap.getHeight();
                            int tempWidth = tempBitmap.getWidth();
                            Bitmap tempBitmap2 = Bitmap.createBitmap(tempBitmap, 0, 0, tempWidth * (3 - deleteStep) / 4, tempHeight);
                            canvas.drawBitmap(tempBitmap2, tempCard.getLeft() + tempWidth * (deleteStep + 1) / 4, tempCard.getTop(), tempPaint);
                        }
                    }

                }

                if (toBeChangedCards.size() >= 3) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("thread1");
                            if (toBeDeletedCards.size() > 0) {
                                deleteStep++;
                                if (deleteStep == 3) {
                                    for (int i = 0; i < toBeDeletedCards.size(); i++)
                                        toBeDeletedCards.get(i).setDeleteNum(0);
                                    deleteStep = 0;
                                    toBeDeletedCards.clear();
                                }
                                try {
                                    Thread.sleep(5);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                postInvalidate();
                            } else {
                                if (deleteMaxNum > 1) {
                                    System.out.println("thread2");
                                    for (int i = 0; i < toBeChangedCards.size(); i++) {
                                        if (toBeChangedCards.get(i).getDeleteNum() == deleteMaxNum) {
                                            //toBeChangedCards.get(i).setDeleteNum(0);
                                            toBeChangedCards.get(i).setNum(0);
                                            toBeDeletedCards.add(toBeChangedCards.get(i));
                                            freeCards.add(toBeChangedCards.get(i));
                                        }
                                    }
                                    deleteMaxNum--;
                                    try {
                                        Thread.sleep(5);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    postInvalidate();
                                } else {
                                    System.out.println("thread3");
                                    Card tempCard = null;
                                    for (int i = 0; i < toBeChangedCards.size(); i++) {
                                        if (toBeChangedCards.get(i).getDeleteNum() == deleteMaxNum) {
                                            if(gameActivity.ifPlaySound) gameActivity.playSound(7,0);
                                            tempCard = toBeChangedCards.get(i);
                                            score += toBeChangedCards.size() * tempCard.getNum();
                                            int mergenum = tempCard.getNum() + (toBeChangedCards.size() - 1) / 2;
                                            tempCard.setNum(mergenum);
                                            if (mergenum > maxnum) maxnum = mergenum;
                                            changeChoosedMaxNum(mergenum);
                                            break;
                                        }
                                    }

                                    toBeChangedCards.clear();
                                    deleteMaxNum = 0;
                                    tempCard.setDeleteNum(1);
                                    mergeCard(tempCard);
                                    if (toBeChangedCards.size() < 3) {
                                        for (int i = 0; i < toBeChangedCards.size(); i++)
                                            toBeChangedCards.get(i).setDeleteNum(0);
                                        toBeChangedCards.clear();
                                    }
                                    //invalidate();
                                    postInvalidate();
                                }
                            }
                        }
                    }).start();
                }
                setOnTouchListener(new PropModeView.touchAction());
                System.out.println("choosedMaxNum:" + choosedMaxNum);
            }
        }

    }

    private int dp2px(float dpValue){
        return (int)(dpValue*scale+0.5f);
    }

    private int sp2px(float spValue){
        return (int)(spValue*fontScale+0.5f);
    }

    class touchAction implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(chooseCard==null){
                        for(int i=0;i<3;i++){
                            if(chooseCards[i].ifChoose(event.getX(),event.getY())){
                                if(gameActivity.ifPlaySound) gameActivity.playSound(2,0);
                                chooseCard=new Card(chooseCards[i].getLeft(),chooseCards[i].getTop(),cardLength,chooseCards[i].getNum());
                                chooseIndex=i;
                                x_dif=event.getX()-chooseCard.getLeft();
                                y_dif=event.getY()-chooseCard.getTop();
                                ifMove=true;
                                break;
                            }
                        }
                    }
                    if(event.getX()>=dp2px(20)-5 && event.getX()<=dp2px(20)+btn_pause.getWidth()+5
                            && event.getY()>=dp2px(50)+(mode_title.getHeight()*1f)/12-5
                            && event.getY()<=dp2px(50)+(mode_title.getHeight()*1f)/12+btn_pause.getHeight()+5){
                        if(gameActivity.ifPlaySound) gameActivity.playSound(2,0);
                        gameActivity.hd2.sendEmptyMessage(2);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(chooseCard!=null){
                        current_x=event.getX();
                        current_y=event.getY();
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (chooseCard != null) {
                        if (chooseCard.getNum() > 0 || (chooseCard.getNum()>=-7 && chooseCard.getNum()<=-5)) {
                            System.out.println("freeCards.size:" + freeCards.size());
                            for (int i = 0; i < freeCards.size(); i++) {
                                if (freeCards.get(i).ifIntersect(chooseCard) && freeCards.get(i).getNum() == 0) {

                                    int nextNum = chooseCard.getNum();
                                    if (nextNum == -5) {
                                        if(gameActivity.ifPlaySound) gameActivity.playSound(3,0);
                                        nextNum = nextRandom(choosedMaxNum);
                                        nextNum = choosedMaxNum + 1 - nextNum;
                                    } else if (nextNum == -6) {
                                        int tempRes = checkVortexCard(freeCards.get(i));
                                        if (tempRes != -1) {
                                            if(gameActivity.ifPlaySound) gameActivity.playSound(6,0);
                                            nextNum = tempRes;
                                        } else break;
                                    } else if (nextNum == -7) {
                                        System.out.println("before send");
                                        pointCard=freeCards.get(i);
                                        pointIndex=chooseIndex;
                                        //gameActivity.hd.sendEmptyMessage(1);
                                        Message msg=gameActivity.hd2.obtainMessage();
                                        msg.what=0;
                                        msg.obj=choosedMaxNum;
                                        gameActivity.hd2.sendMessage(msg);
                                        break;
                                        //System.out.println("after send");
                                        //nextNum = writenum;
                                        //System.out.println("nextNum="+nextNum);
                                    }
                                    else{
                                        if(gameActivity.ifPlaySound) gameActivity.playSound(3,0);
                                    }
                                    freeCards.get(i).setNum(nextNum);
                                    changeNext(chooseIndex);
                                    toBeChangedCards.clear();
                                    freeCards.get(i).setDeleteNum(1);
                                    deleteMaxNum = 0;
                                    mergeCard(freeCards.get(i));
                                    freeCards.remove(i);
                                    if (toBeChangedCards.size() < 3) {
                                        for (int j = 0; j < toBeChangedCards.size(); j++)
                                            toBeChangedCards.get(j).setDeleteNum(0);
                                        int newCardIndex = randomNumber(0, freeCards.size());
                                        if (newCardIndex != -1) {
                                            int nextNum2 = nextRandom(choosedMaxNum + 1);
                                            if(choosedMaxNum>=4) {
                                                if (nextNum2 == choosedMaxNum / 2 + 1) {
                                                    int obsNum=0;
                                                    for(int ti=0;ti<dimension;ti++)
                                                        for(int tj=0;tj<dimension;tj++)
                                                            if(cards[ti][tj].getNum()<0)
                                                                obsNum++;
                                                    if(obsNum+1<=((dimension*dimension)-freeCards.size())/4)
                                                        freeCards.get(newCardIndex).setNum(-8);
                                                    else{
                                                        int secondRand=randomNumber(0,2);
                                                        if(secondRand==0)
                                                            freeCards.get(newCardIndex).setNum(nextNum2);
                                                        else
                                                            freeCards.get(newCardIndex).setNum(nextNum2-1);
                                                    }
                                                }
                                                else if (nextNum2 > choosedMaxNum / 2 + 1)
                                                    freeCards.get(newCardIndex).setNum(nextNum2 - 1);
                                                else
                                                    freeCards.get(newCardIndex).setNum(nextNum2);
                                            }
                                            else{
                                                if (nextNum2 == choosedMaxNum / 2 + 1) {
                                                    int secondRand=randomNumber(0,5);
                                                    if(secondRand==0) {
                                                        int obsNum=0;
                                                        for(int ti=0;ti<dimension;ti++)
                                                            for(int tj=0;tj<dimension;tj++)
                                                                if(cards[ti][tj].getNum()<0)
                                                                    obsNum++;
                                                        if(obsNum+1<=((dimension*dimension)-freeCards.size())/4)
                                                            freeCards.get(newCardIndex).setNum(-8);
                                                        else{
                                                            freeCards.get(newCardIndex).setNum(nextNum2);
                                                        }
                                                    }
                                                    else{
                                                        if(choosedMaxNum==1)
                                                            freeCards.get(newCardIndex).setNum(nextNum2);
                                                        else {
                                                            if(secondRand<=2)
                                                                freeCards.get(newCardIndex).setNum(nextNum2);
                                                            else
                                                                freeCards.get(newCardIndex).setNum(nextNum2-1);
                                                        }
                                                    }
                                                }
                                                else if (nextNum2 > choosedMaxNum / 2 + 1)
                                                    freeCards.get(newCardIndex).setNum(nextNum2 - 1);
                                                else
                                                    freeCards.get(newCardIndex).setNum(nextNum2);
                                            }
                                            Card newCard = freeCards.get(newCardIndex);
                                            toBeChangedCards.clear();
                                            newCard.setDeleteNum(1);
                                            deleteMaxNum = 0;
                                            mergeCard(newCard);
                                            freeCards.remove(newCardIndex);
                                            if (toBeChangedCards.size() < 3) {
                                                for (int j = 0; j < toBeChangedCards.size(); j++)
                                                    toBeChangedCards.get(j).setDeleteNum(0);
                                                toBeChangedCards.clear();
                                                if (freeCards.size() == 0) ifEnd = 1;
                                            }
                                        }
                                        else {
                                            ifEnd = 1;
                                        }

                                    }

                                    stepNum++;
                                    break;
                                }
                            }
                        }
                        else{
                            boolean ifBreak=false;
                            for(int i=0;i<dimension;i++) {
                                for (int j = 0; j < dimension; j++) {
                                    if (cards[i][j].ifIntersect(chooseCard) && cards[i][j].getNum() != 0)
                                    {
                                        if(chooseCard.getNum()==-4){
                                            if(gameActivity.ifPlaySound) gameActivity.playSound(5,0);
                                            if(cards[i][j].getNum()<0) score+=maxnum;
                                            else score+=cards[i][j].getNum();
                                            cards[i][j].setNum(0);
                                            freeCards.add(cards[i][j]);
                                        }
                                        else if(chooseCard.getNum()==-3){
                                            if(gameActivity.ifPlaySound) gameActivity.playSound(4,0);
                                            if(cards[i][j].getNum()<0){
                                                score+=maxnum;
                                                cards[i][j].setNum(cards[i][j].getNum()+1);
                                                if(cards[i][j].getNum()==-7) {
                                                    cards[i][j].setNum(0);
                                                    freeCards.add(cards[i][j]);
                                                }
                                            }
                                            else{
                                                score+=cards[i][j].getNum();
                                                cards[i][j].setNum(cards[i][j].getNum()-1);
                                                if(cards[i][j].getNum()==0)
                                                    freeCards.add(cards[i][j]);
                                                else {
                                                    toBeChangedCards.clear();
                                                    cards[i][j].setDeleteNum(1);
                                                    deleteMaxNum = 0;
                                                    mergeCard(cards[i][j]);
                                                    if (toBeChangedCards.size() < 3) {
                                                        for (int k = 0; k < toBeChangedCards.size(); k++)
                                                            toBeChangedCards.get(k).setDeleteNum(0);
                                                        toBeChangedCards.clear();
                                                        if (freeCards.size() == 0) ifEnd = 1;
                                                    }
                                                }
                                            }
                                        }
                                        else{
                                            if(gameActivity.ifPlaySound) gameActivity.playSound(4,0);
                                            if(cards[i][j].getNum()<0){
                                                score+=maxnum;
                                                if(cards[i][j].getNum()!=-10)
                                                    cards[i][j].setNum(cards[i][j].getNum()-1);
                                                else{
                                                    cards[i][j].setNum(0);
                                                    freeCards.add(cards[i][j]);
                                                    for(int i2=0;i2<dimension;i2++)
                                                        for(int j2=0;j2<dimension;j2++){
                                                            if(cards[i2][j2].getNum()>0){
                                                                score+=cards[i2][j2].getNum();
                                                                cards[i2][j2].setNum(cards[i2][j2].getNum
                                                                        ()+1);
                                                            }
                                                        }
                                                    maxnum++;
                                                    choosedMaxNum++;
                                                    ifRotate=true;
                                                }
                                            }
                                            else{
                                                score+=cards[i][j].getNum();
                                                cards[i][j].setNum(cards[i][j].getNum()+1);
                                                if(cards[i][j].getNum()>maxnum)
                                                    maxnum=cards[i][j].getNum();
                                                changeChoosedMaxNum(cards[i][j].getNum());
                                                toBeChangedCards.clear();
                                                cards[i][j].setDeleteNum(1);
                                                deleteMaxNum = 0;
                                                mergeCard(cards[i][j]);
                                                if (toBeChangedCards.size() < 3) {
                                                    for (int k = 0; k < toBeChangedCards.size(); k++)
                                                        toBeChangedCards.get(k).setDeleteNum(0);
                                                    toBeChangedCards.clear();
                                                    if (freeCards.size() == 0) ifEnd = 1;
                                                }
                                            }
                                        }
                                        changeNext(chooseIndex);
                                        ifBreak=true;
                                        stepNum++;
                                        break;
                                    }
                                }
                                if(ifBreak) break;
                            }
                        }
                        chooseCard = null;
                        ifMove = false;
                        chooseIndex = -1;
                        invalidate();
                    }
                    break;
            }
            return true;
        }
    }

    public void forPoint(){
            changeNext(pointIndex);
            if (gameActivity.ifPlaySound) gameActivity.playSound(3, 0);
            pointCard.setNum(writenum);
            toBeChangedCards.clear();
            pointCard.setDeleteNum(1);
            deleteMaxNum = 0;
            mergeCard(pointCard);
            freeCards.remove(pointCard);
            if (toBeChangedCards.size() < 3) {
                for (int j = 0; j < toBeChangedCards.size(); j++)
                    toBeChangedCards.get(j).setDeleteNum(0);
                int newCardIndex = randomNumber(0, freeCards.size());
                if (newCardIndex != -1) {
                    int nextNum2 = nextRandom(choosedMaxNum + 1);
                    if (choosedMaxNum >= 3) {
                        if (nextNum2 == choosedMaxNum / 2 + 1)
                            freeCards.get(newCardIndex).setNum(-8);
                        else if (nextNum2 > choosedMaxNum / 2 + 1)
                            freeCards.get(newCardIndex).setNum(nextNum2 - 1);
                        else
                            freeCards.get(newCardIndex).setNum(nextNum2);
                    } else {
                        if (nextNum2 == choosedMaxNum / 2 + 1) {
                            int secondRand = randomNumber(0, 5);
                            if (secondRand == 0)
                                freeCards.get(newCardIndex).setNum(-8);
                            else if (secondRand <= 2)
                                freeCards.get(newCardIndex).setNum(nextNum2);
                            else
                                freeCards.get(newCardIndex).setNum(nextNum2 - 1);
                        } else if (nextNum2 > choosedMaxNum / 2 + 1)
                            freeCards.get(newCardIndex).setNum(nextNum2 - 1);
                        else
                            freeCards.get(newCardIndex).setNum(nextNum2);
                    }
                    Card newCard = freeCards.get(newCardIndex);
                    toBeChangedCards.clear();
                    newCard.setDeleteNum(1);
                    deleteMaxNum = 0;
                    mergeCard(newCard);
                    freeCards.remove(newCardIndex);
                    if (toBeChangedCards.size() < 3) {
                        for (int j = 0; j < toBeChangedCards.size(); j++)
                            toBeChangedCards.get(j).setDeleteNum(0);
                        toBeChangedCards.clear();
                        //if (freeCards.size() == 0) ifEnd = 1;
                    }
                }
            }
            invalidate();

    }

    private int checkVortexCard(Card card){
        ArrayList<Integer> tempList=new ArrayList<Integer>();
        int num1=0,num2=0,num3=0,num4=0;
        int size1=1,size2=1,size3=1,size4=1;
        ArrayList<Card> tempCards=new ArrayList<Card>();
        tempCards.addAll(toBeChangedCards);
        if(card.getM()>0){
            num1=cards[card.getM()-1][card.getN()].getNum();
            if(num1>0) {
                card.setNum(num1);
                toBeChangedCards.clear();
                mergeCard(card);
                size1 = toBeChangedCards.size();
            }
        }
        if(card.getM()<dimension-1){
            num2=cards[card.getM()+1][card.getN()].getNum();
            if(num2>0) {
                card.setNum(num2);
                toBeChangedCards.clear();
                mergeCard(card);
                size2 = toBeChangedCards.size();
            }
        }
        if(card.getN()>0){
            num3=cards[card.getM()][card.getN()-1].getNum();
            if(num3>0) {
                card.setNum(num3);
                toBeChangedCards.clear();
                mergeCard(card);
                size3 = toBeChangedCards.size();
            }
        }
        if(card.getN()<dimension-1){
            num4=cards[card.getM()][card.getN()+1].getNum();
            if(num4>0) {
                card.setNum(num4);
                toBeChangedCards.clear();
                mergeCard(card);
                size4 = toBeChangedCards.size();
            }
        }
        toBeChangedCards.clear();
        toBeChangedCards.addAll(tempCards);

        if(size1>=3 && size1>size2 && size1>size3 && size1>size4) return num1;
        if(size2>=3 && size2>size1 && size2>size3 && size2>size4) return num2;
        if(size3>=3 && size3>size1 && size3>size1 && size3>size4) return num3;
        if(size4>=3 && size4>size1 && size4>size2 && size4>size3) return num4;

        if(size1>=3 && size1>=size2 && size1>=size3 && size1>=size4){
            int res=num1;
            if(size1==size2) if(num2>res) res=num2;
            if(size1==size3) if(num3>res) res=num3;
            if(size1==size4) if(num4>res) res=num4;
            return res;
        }
        if(size2>=3 && size2>=size1 && size2>=size3 && size2>=size4){
            int res=num2;
            if(size2==size1) if(num1>res) res=num1;
            if(size2==size3) if(num3>res) res=num3;
            if(size2==size4) if(num4>res) res=num4;
            return res;
        }
        if(size3>=3 && size3>=size1 && size3>=size1 && size3>=size4){
            int res=num3;
            if(size3==size1) if(num1>res) res=num1;
            if(size3==size2) if(num2>res) res=num2;
            if(size3==size4) if(num4>res) res=num4;
            return res;
        }
        if(size4>=3 && size4>=size1 && size4>=size2 && size4>=size3){
            int res=num4;
            if(size4==size1) if(num1>res) res=num1;
            if(size4==size2) if(num2>res) res=num2;
            if(size4==size3) if(num3>res) res=num3;
            return res;
        }

        card.setNum(0);
        return -1;
    }

    public void setWritenum(int num){
        writenum=num;
    }

    private int randomNumber(int start,int end){
        if(start==end) return -1;
        long time=System.currentTimeMillis();
        time%=10000;
        //System.out.println("currentTime:"+time);
        double rand=Math.random();
        //System.out.println("rand:"+rand);
        //System.out.println("randomSeed1:"+randomSeed);
        randomSeed=(int)(time*rand*randomSeed)%10000;
        //System.out.println("randomSeed2:"+randomSeed);
        //System.out.println("result:"+(start+randomSeed%(end-start)));
        return start+randomSeed%(end-start);
    }

    private void changeNext(int index){

        int[] propsign=new int[3];

        for(int i=0;i<3;i++){
            if(i!=index) propsign[i]=1;
            else propsign[i]=0;
        }

        for(int i=0;i<3;i++){
            if(propsign[i]==1 && chooseCards[i].getNum()<0){
                chooseCards[i].setProptime(chooseCards[i].getProptime()-1);
                if(chooseCards[i].getProptime()==0)
                    propsign[i]=0;
            }
        }

        for(int i=0;i<2;i++){
            if(propsign[i]==0){
                for(int j=i+1;j<3;j++){
                    if(propsign[j]==1){
                        chooseCards[i].setNum(chooseCards[j].getNum());
                        chooseCards[i].setProptime(chooseCards[j].getProptime());
                        propsign[i]=1;
                        propsign[j]=0;
                        break;
                    }
                }
            }
        }

        if(propsign[0]==0){
            chooseCards[0].setNum(nextCard.getNum());
            chooseCards[0].setProptime(3);
            int nextNum=nextRandom(choosedMaxNum+1);
            if(nextNum>choosedMaxNum/2+1){
                chooseCards[1].setNum(nextNum-1);
            }
            else chooseCards[1].setNum(nextNum);
            if(choosedMaxNum>=4){
                if(nextNum==choosedMaxNum/2+1){
                    int secondRand=randomNumber(0,16);
                    if(secondRand==0) chooseCards[1].setNum(-7);
                    else if(secondRand==1 || secondRand==2) chooseCards[1].setNum(-6);
                    else if(secondRand==3 || secondRand==4) chooseCards[1].setNum(-2);
                    else if(secondRand>=5 && secondRand<=7) chooseCards[1].setNum(-4);
                    else if(secondRand>=8 && secondRand<=11) chooseCards[1].setNum(-5);
                    else chooseCards[1].setNum(-3);
                }
            }
            chooseCards[1].setProptime(3);

            int nextNum2=nextRandom(choosedMaxNum+1);
            if(nextNum2>choosedMaxNum/2+1){
                chooseCards[2].setNum(nextNum2-1);
            }
            else chooseCards[2].setNum(nextNum2);
            if(choosedMaxNum>=4){
                if(nextNum2==choosedMaxNum/2+1){
                    int secondRand=randomNumber(0,16);
                    if(secondRand==0) chooseCards[2].setNum(-7);
                    else if(secondRand==1 || secondRand==2) chooseCards[2].setNum(-6);
                    else if(secondRand==3 || secondRand==4) chooseCards[2].setNum(-2);
                    else if(secondRand>=5 && secondRand<=7) chooseCards[2].setNum(-4);
                    else if(secondRand>=8 && secondRand<=11) chooseCards[2].setNum(-5);
                    else chooseCards[2].setNum(-3);
                }
            }
            chooseCards[2].setProptime(3);
        }
        else if(propsign[1]==0){
            chooseCards[1].setNum(nextCard.getNum());
            chooseCards[1].setProptime(3);
            int nextNum=nextRandom(choosedMaxNum+1);
            if(nextNum>choosedMaxNum/2+1){
                chooseCards[2].setNum(nextNum-1);
            }
            else chooseCards[2].setNum(nextNum);
            if(choosedMaxNum>=4){
                if(nextNum==choosedMaxNum/2+1){
                    int secondRand=randomNumber(0,16);
                    if(secondRand==0) chooseCards[1].setNum(-7);
                    else if(secondRand==1 || secondRand==2) chooseCards[2].setNum(-6);
                    else if(secondRand==3 || secondRand==4) chooseCards[2].setNum(-2);
                    else if(secondRand>=5 && secondRand<=7) chooseCards[2].setNum(-4);
                    else if(secondRand>=8 && secondRand<=11) chooseCards[2].setNum(-5);
                    else chooseCards[2].setNum(-3);
                }
            }
            chooseCards[2].setProptime(3);
        }
        else {
            chooseCards[2].setNum(nextCard.getNum());
            chooseCards[2].setProptime(3);
        }

        int nextNum=nextRandom(choosedMaxNum+1);
        if(choosedMaxNum>1){
            if(chooseCards[0].getNum()>0 && chooseCards[0].getNum()==chooseCards[1].getNum()
                    && chooseCards[1].getNum()==chooseCards[2].getNum()){
                if(chooseCards[0].getNum()==nextNum){
                    if(nextNum+1>choosedMaxNum) nextCard.setNum(nextNum-1);
                    else nextCard.setNum(nextNum+1);
                }
                else {
                    if(nextNum>choosedMaxNum/2+1){
                        if(nextNum-1==chooseCards[0].getNum()) nextCard.setNum(nextNum-2);
                        else nextCard.setNum(nextNum-1);
                    }
                    else nextCard.setNum(nextNum);
                }
            }
            else {
                if(nextNum>choosedMaxNum/2+1){
                    nextCard.setNum(nextNum-1);
                }
                else nextCard.setNum(nextNum);
            }

            if(choosedMaxNum>=4){
                if(nextNum==choosedMaxNum/2+1){
                    int secondRand=randomNumber(0,16);
                    if(secondRand==0) nextCard.setNum(-7);
                    else if(secondRand==1 || secondRand==2) nextCard.setNum(-6);
                    else if(secondRand==3 || secondRand==4) nextCard.setNum(-2);
                    else if(secondRand>=5 && secondRand<=7) nextCard.setNum(-4);
                    else if(secondRand>=8 && secondRand<=11) nextCard.setNum(-5);
                    else nextCard.setNum(-3);
                }
            }
        }
        else nextCard.setNum(1);

    }

    private void mergeCard(Card card){
        toBeChangedCards.add(card);
        if(card.getM()>0){
            if(!toBeChangedCards.contains(cards[card.getM()-1][card.getN()]) && card.getNum()>0 && card.getNum()==cards[card.getM()-1][card.getN()].getNum()) {
                if(card.getDeleteNum()+1>deleteMaxNum) deleteMaxNum=card.getDeleteNum()+1;
                cards[card.getM() - 1][card.getN()].setDeleteNum(card.getDeleteNum()+1);
                mergeCard(cards[card.getM() - 1][card.getN()]);
            }
        }
        if(card.getM()<dimension-1){
            if(!toBeChangedCards.contains(cards[card.getM()+1][card.getN()]) && card.getNum()>0 && card.getNum()==cards[card.getM()+1][card.getN()].getNum()) {
                if(card.getDeleteNum()+1>deleteMaxNum) deleteMaxNum=card.getDeleteNum()+1;
                cards[card.getM() + 1][card.getN()].setDeleteNum(card.getDeleteNum()+1);
                mergeCard(cards[card.getM() + 1][card.getN()]);
            }
        }
        if(card.getN()>0){
            if(!toBeChangedCards.contains(cards[card.getM()][card.getN()-1]) && card.getNum()>0 && card.getNum()==cards[card.getM()][card.getN()-1].getNum()) {
                if(card.getDeleteNum()+1>deleteMaxNum) deleteMaxNum=card.getDeleteNum()+1;
                cards[card.getM()][card.getN() - 1].setDeleteNum(card.getDeleteNum()+1);
                mergeCard(cards[card.getM()][card.getN() - 1]);
            }
        }
        if(card.getN()<dimension-1){
            if(!toBeChangedCards.contains(cards[card.getM()][card.getN()+1]) && card.getNum()>0 && card.getNum()==cards[card.getM()][card.getN()+1].getNum()) {
                if(card.getDeleteNum()+1>deleteMaxNum) deleteMaxNum=card.getDeleteNum()+1;
                cards[card.getM()][card.getN() + 1].setDeleteNum(card.getDeleteNum()+1);
                mergeCard(cards[card.getM()][card.getN() + 1]);
            }
        }
    }

    private void changeChoosedMaxNum(int mergeNum){
        int maxcount=0;
        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                if(cards[i][j].getNum()==mergeNum)
                    maxcount++;
            }
        }
        if(maxcount>=2 && mergeNum>choosedMaxNum) choosedMaxNum=mergeNum;
    }

    private TreeMap<Integer, Double> randRate(int maxnum){
        TreeMap<Integer, Double> rate = new TreeMap<Integer, Double>();
        int n = maxnum/2;
        double all = 0;
        if (maxnum%2 == 0){
            all = Math.pow(2, n+2) - 5;
            double tmp = 0;
            rate.put(0, 0.0);
            rate.put(maxnum, 1.0);
            for (int i = 1; i < maxnum; i++){
                if (i%2 == 1 && i != maxnum - 1){//奇数
                    tmp = tmp + Math.pow(2, n - i/2)/all;
                }
                else if (i%2 == 0){//偶数
                    tmp = tmp + Math.pow(2, n - i/2 + 1)/all;
                }
                else if(i == maxnum - 1)
                    tmp = tmp + 2/all;
                rate.put(i, tmp);
            }
        }
        else{
            all = Math.pow(2, n+2) - 3;
            double tmp = 0;
            rate.put(0, 0.0);
            rate.put(maxnum, 1.0);
            for (int i = 1; i < maxnum; i++){
                if (i%2 == 1){//奇数
                    tmp = tmp + Math.pow(2, n - i/2)/all;
                }
                else if (i%2 == 0){//偶数
                    tmp = tmp + Math.pow(2, n - i/2 + 1)/all;
                }
                rate.put(i, tmp);
            }
        }
        return rate;
    }

    private int nextRandom(int newChoosedMaxNum){
        int result=1;
        TreeMap<Integer, Double> rate = randRate(newChoosedMaxNum);
        Double p = Math.random();
        for (int i = 0; i < newChoosedMaxNum; i++){
            if (p >= rate.get(i) && p <= rate.get(i + 1)){
                result=i+1;
                break;
            }
        }
        return result;
    }

}
