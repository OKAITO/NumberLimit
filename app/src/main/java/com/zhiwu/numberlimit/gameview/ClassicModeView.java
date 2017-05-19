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
import com.zhiwu.numberlimit.R;
import com.zhiwu.numberlimit.activity.GameActivity;

import java.util.ArrayList;
import java.util.TreeMap;

public class ClassicModeView extends View {

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

    private int deleteMaxNum=0;

    private Card[][] cards=new Card[dimension][dimension]; //卡片保存参数
    private Card[] chooseCards=new Card[3];
    private ArrayList<Card> freeCards=new ArrayList<Card>();
    private ArrayList<Card> toBeChangedCards=new ArrayList<Card>();
    private Card nextCard=null;
    private Card chooseCard=null;
    private int chooseIndex=-1;

    private ArrayList<Card> toBeDeletedCards=new ArrayList<Card>();
    private int deleteStep=0;

    private int randomSeed=137;
    private float current_x=-1; //卡片移动参数
    private float current_y=-1;
    private float x_dif=-1;
    private float y_dif=-1;

    private boolean ifStart=true; //标志位
    private boolean ifMove=false;
    private int ifEnd=0;

    private int endIndex=0;

    private ArrayList<Bitmap> bitmaps=new ArrayList<Bitmap>();

    private Bitmap bgBitmap;

    private GameActivity gameActivity;

    private Bitmap mode_title;
    private Bitmap btn_pause;

    public ClassicModeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gameActivity=(GameActivity)context;
        init();
    }
    public ClassicModeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameActivity=(GameActivity)context;
        init();
    }

    public ClassicModeView(Context context) {
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

        //ArrayList<Bitmap> tempBitmaps=new ArrayList<Bitmap>();
        Bitmap bitmap0= BitmapFactory.decodeResource(getResources(), R.drawable.blank);
        Bitmap newBitmap0=resizeCard(bitmap0,cardLength);
        bitmaps.add(newBitmap0);
        bitmap0.recycle();

        Bitmap bitmap1= BitmapFactory.decodeResource(getResources(),R.drawable.game01);
        Bitmap newBitmap1=resizeCard(bitmap1,cardLength);
        bitmaps.add(newBitmap1);
        bitmap1.recycle();

        Bitmap bitmap2= BitmapFactory.decodeResource(getResources(),R.drawable.game02);
        Bitmap newBitmap2=resizeCard(bitmap2,cardLength);
        bitmaps.add(newBitmap2);
        bitmap2.recycle();

        Bitmap bitmap3= BitmapFactory.decodeResource(getResources(),R.drawable.game03);
        Bitmap newBitmap3=resizeCard(bitmap3,cardLength);
        bitmaps.add(newBitmap3);
        bitmap3.recycle();

        Bitmap bitmap4= BitmapFactory.decodeResource(getResources(),R.drawable.game04);
        Bitmap newBitmap4=resizeCard(bitmap4,cardLength);
        bitmaps.add(newBitmap4);
        bitmap4.recycle();

        Bitmap bitmap5= BitmapFactory.decodeResource(getResources(),R.drawable.game05);
        Bitmap newBitmap5=resizeCard(bitmap5,cardLength);
        bitmaps.add(newBitmap5);
        bitmap5.recycle();

        Bitmap bitmap6= BitmapFactory.decodeResource(getResources(),R.drawable.game06);
        Bitmap newBitmap6=resizeCard(bitmap6,cardLength);
        bitmaps.add(newBitmap6);
        bitmap6.recycle();

        Bitmap bitmap7= BitmapFactory.decodeResource(getResources(),R.drawable.game07);
        Bitmap newBitmap7=resizeCard(bitmap7,cardLength);
        bitmaps.add(newBitmap7);
        bitmap7.recycle();

        Bitmap bitmap8= BitmapFactory.decodeResource(getResources(),R.drawable.game08);
        Bitmap newBitmap8=resizeCard(bitmap8,cardLength);
        bitmaps.add(newBitmap8);
        bitmap8.recycle();

        Bitmap bitmap9= BitmapFactory.decodeResource(getResources(),R.drawable.game09);
        Bitmap newBitmap9=resizeCard(bitmap9,cardLength);
        bitmaps.add(newBitmap9);
        bitmap9.recycle();

        Bitmap bitmap10= BitmapFactory.decodeResource(getResources(),R.drawable.game10);
        Bitmap newBitmap10=resizeCard(bitmap10,cardLength);
        bitmaps.add(newBitmap10);
        bitmap10.recycle();

        Bitmap bitmap11= BitmapFactory.decodeResource(getResources(),R.drawable.game11);
        Bitmap newBitmap11=resizeCard(bitmap11,cardLength);
        bitmaps.add(newBitmap11);
        bitmap11.recycle();

        Bitmap bitmap12= BitmapFactory.decodeResource(getResources(),R.drawable.game12);
        Bitmap newBitmap12=resizeCard(bitmap12,cardLength);
        bitmaps.add(newBitmap12);
        bitmap12.recycle();

        Bitmap bitmap13= BitmapFactory.decodeResource(getResources(),R.drawable.game13);
        Bitmap newBitmap13=resizeCard(bitmap13,cardLength);
        bitmaps.add(newBitmap13);
        bitmap13.recycle();

        Bitmap bitmap14= BitmapFactory.decodeResource(getResources(),R.drawable.game14);
        Bitmap newBitmap14=resizeCard(bitmap14,cardLength);
        bitmaps.add(newBitmap14);
        bitmap14.recycle();

        Bitmap bitmap15= BitmapFactory.decodeResource(getResources(),R.drawable.game15);
        Bitmap newBitmap15=resizeCard(bitmap15,cardLength);
        bitmaps.add(newBitmap15);
        bitmap15.recycle();

        Bitmap bitmap16= BitmapFactory.decodeResource(getResources(),R.drawable.next);
        Bitmap newBitmap16=resizeCard(bitmap16,cardLength);
        bitmaps.add(newBitmap16);
        bitmap16.recycle();

        /*for(int i=0;i<tempBitmaps.size();i++){
            int height=tempBitmaps.get(i).getHeight();
            int width=tempBitmaps.get(i).getWidth();
            Matrix matrix=new Matrix();
            float scaleHeight=cardLength*1.0f/height;
            matrix.postScale(scaleHeight,scaleHeight);
            Bitmap newBitmap=Bitmap.createBitmap(tempBitmaps.get(i),0,0,width,height,matrix,true);
            bitmaps.add(newBitmap);
        }*/

        Bitmap tempBgBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.classic_bg);
        int bgHeight=tempBgBitmap.getHeight();
        int bgWidth=tempBgBitmap.getWidth();
        Matrix bgMatrix=new Matrix();
        float bgScaleHeight=height*1.0f/bgHeight;
        float bgScaleWidth=width*1.0f/bgWidth;
        bgMatrix.postScale(bgScaleWidth,bgScaleHeight);
        bgBitmap=Bitmap.createBitmap(tempBgBitmap,0,0,bgWidth,bgHeight,bgMatrix,true);

        Bitmap titleBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.classic_title);
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

    private Bitmap resizeCard(Bitmap srcBitmap,int cardLength){
        int height=srcBitmap.getHeight();
        int width=srcBitmap.getWidth();
        Matrix matrix=new Matrix();
        float scaleHeight=cardLength*1.0f/height;
        matrix.postScale(scaleHeight,scaleHeight);
        Bitmap dstBitmap=Bitmap.createBitmap(srcBitmap,0,0,width,height,matrix,true);
        return dstBitmap;
    }
    
    public Bitmap getBitmap(int num){
        if(num==-1)
            return bitmaps.get(bitmaps.size()-1);
        else if(num>=0 && num<=bitmaps.size()-3)
            return bitmaps.get(num);
        else return bitmaps.get(bitmaps.size()-2);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
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
        //float chooseCardTop=cardTop+dimension*20+dimension*cardLength;
        float chooseCardTop=cardTop+dimension*cardLength+40;
        chooseCardTop=(chooseCardTop+height)/2-cardLength/2;
        float margin=(width-4*cardLength)/6;

        if(ifStart){
            ifStart=false;
            for(int i=0;i<dimension;i++){
                for(int j=0;j<dimension;j++){
                    float left=40+j*10+j*cardLength;
                    float top=cardTop+10*i+i*cardLength;
                    cards[i][j]=new Card(left,top,cardLength,0);
                    cards[i][j].setM(i);
                    cards[i][j].setN(j);
                    freeCards.add(cards[i][j]);
                }
            }
            //cards[0][0].setNum(0);
            //freeCards.add(cards[0][0]);
            int rand1=randomNumber(0,freeCards.size());
            freeCards.get(rand1).setNum(1);
            freeCards.remove(rand1);
            int rand2=randomNumber(0,freeCards.size());
            freeCards.get(rand2).setNum(1);
            freeCards.remove(rand2);
            int rand3=randomNumber(0,freeCards.size());
            freeCards.get(rand3).setNum(2);
            freeCards.remove(rand3);

            for(int i=0;i<dimension;i++){
                for(int j=0;j<dimension;j++){
                    cards[i][j].drawCard_init(this,canvas,1);
                }
            }

            chooseCards[0]=new Card(margin,chooseCardTop,cardLength,1);
            chooseCards[0].drawCard_init(this,canvas,1);

            chooseCards[1]=new Card(2*margin+cardLength,chooseCardTop,cardLength,1);
            chooseCards[1].drawCard_init(this,canvas,1);

            chooseCards[2]=new Card(3*margin+2*cardLength,chooseCardTop,cardLength,1);
            chooseCards[2].drawCard_init(this,canvas,1);

            nextCard=new Card(5*margin+3*cardLength,chooseCardTop,cardLength,1);
            nextCard.drawCard_init(this,canvas,1);
            canvas.drawBitmap(getBitmap(-1),5*margin+3*cardLength,chooseCardTop,paint);
            invalidate();
        }

        else {

            if (ifEnd == 2) {

                for (int i = 0; i < dimension; i++) {
                    for (int j = 0; j < dimension; j++) {
                        cards[i][j].drawCard_rotate(this, canvas, endIndex, 1);
                    }
                }

                chooseCards[0].drawCard_init(this, canvas, 1);
                chooseCards[1].drawCard_init(this, canvas, 1);
                chooseCards[2].drawCard_init(this, canvas, 1);
                nextCard.drawCard_init(this, canvas, 1);

                //if(endIndex<=10) {
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
                            }
                        }
                    }).start();
                //}

            }
            else {
                if(ifEnd==1) {ifEnd++;if(gameActivity.ifPlaySound) gameActivity.playSound(8,0);invalidate();}

                for (int i = 0; i < dimension; i++) {
                    for (int j = 0; j < dimension; j++) {
                        cards[i][j].drawCard_init(this, canvas, 1);
                    }
                }

                chooseCards[0].drawCard_init(this, canvas, 1);
                chooseCards[1].drawCard_init(this, canvas, 1);
                chooseCards[2].drawCard_init(this, canvas, 1);
                nextCard.drawCard_init(this, canvas, 1);
                canvas.drawBitmap(getBitmap(-1), 5 * margin + 3 * cardLength, chooseCardTop, paint);

                if (ifMove) {

                    if (chooseCard != null) {
                        for (int i = 0; i < freeCards.size(); i++) {
                            if (freeCards.get(i).ifIntersect(chooseCard) && freeCards.get(i).getNum() == 0) {
                                freeCards.get(i).drawCard_hint(this, canvas, chooseCard, 1);
                                break;
                            }
                        }
                    }
                    canvas.drawBitmap(getBitmap(0), chooseCards[chooseIndex].getLeft(), chooseCards[chooseIndex].getTop(), paint);
                    chooseCard.drawCard_move(this, canvas, current_x, current_y, x_dif, y_dif, 1);
                }

                if (toBeDeletedCards.size() > 0) {
                    for (int i = 0; i < toBeDeletedCards.size(); i++) {
                        Card tempCard = toBeDeletedCards.get(i);
                        if (tempCard.getM() > 0 && cards[tempCard.getM() - 1][tempCard.getN()].getDeleteNum() == tempCard.getDeleteNum() - 1) {

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
                            if (toBeDeletedCards.size() > 0) {
                                deleteStep++;
                                if (deleteStep == 3) {
                                    for (int i = 0; i < toBeDeletedCards.size(); i++)
                                        toBeDeletedCards.get(i).setDeleteNum(0);
                                    deleteStep = 0;
                                    toBeDeletedCards.clear();
                                }
                                try {
                                    Thread.sleep(2);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                postInvalidate();
                            } else {
                                if (deleteMaxNum > 1) {
                                    for (int i = 0; i < toBeChangedCards.size(); i++) {
                                        if (toBeChangedCards.get(i).getDeleteNum() == deleteMaxNum) {
                                            //toBeChangedCards.get(i).setDeleteNum(0);
                                            toBeChangedCards.get(i).setNum(0);
                                            toBeDeletedCards.add(toBeChangedCards.get(i));
                                            freeCards.add(toBeChangedCards.get(i));
                                        }
                                    }
                                    deleteMaxNum--;
                                    //postInvalidateDelayed(5000);
                                    try {
                                        Thread.sleep(5);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    postInvalidate();
                                } else {
                                    Card tempCard = null;
                                    for (int i = 0; i < toBeChangedCards.size(); i++) {
                                        if (toBeChangedCards.get(i).getDeleteNum() == deleteMaxNum) {
                                            if(gameActivity.ifPlaySound) gameActivity.playSound(7,0);
                                            tempCard = toBeChangedCards.get(i);
                                            score += toBeChangedCards.size() * tempCard.getNum();
                                            int mergenum = tempCard.getNum() + (toBeChangedCards.size() - 1) / 2;
                                            tempCard.setNum(mergenum);
                                            if (mergenum > maxnum) maxnum = mergenum;
                                            changeChoosedMaxNum();
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
                setOnTouchListener(new TouchAction());

            }
        }
    }

    private int dp2px(float dpValue){
        return (int)(dpValue*scale+0.5f);
    }

    private int px2dp(float pxValue){
        return (int)(pxValue/scale+0.5f);
    }

    private int sp2px(float spValue){
        return (int)(spValue*fontScale+0.5f);
    }

    private int px2sp(float pxValue){ return (int)(pxValue/fontScale+0.5f); }

    private class TouchAction implements OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (chooseCard == null) {
                        for (int i = 0; i < 3; i++) {
                            if (chooseCards[i].ifChoose(event.getX(), event.getY())) {
                                if(gameActivity.ifPlaySound) gameActivity.playSound(2,0);
                                chooseCard = new Card(chooseCards[i].getLeft(), chooseCards[i].getTop(), cardLength, chooseCards[i].getNum());
                                chooseIndex = i;
                                x_dif = event.getX() - chooseCard.getLeft();
                                y_dif = event.getY() - chooseCard.getTop();
                                ifMove = true;
                                break;
                            }
                        }
                    }
                    if(event.getX()>=dp2px(30)-10 && event.getX()<=dp2px(30)+btn_pause.getWidth()+10
                            && event.getY()>=dp2px(50)+(mode_title.getHeight()*1f)/12-10
                            && event.getY()<=dp2px(50)+(mode_title.getHeight()*1f)/12+btn_pause.getHeight()+10){
                        if(gameActivity.ifPlaySound) gameActivity.playSound(2,0);
                        gameActivity.hd2.sendEmptyMessage(2);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (chooseCard != null) {
                        current_x = event.getX();
                        current_y = event.getY();
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (chooseCard != null) {
                        for (int i = 0; i < freeCards.size(); i++) {
                            if (freeCards.get(i).ifIntersect(chooseCard) && freeCards.get(i).getNum() == 0) {
                                if(gameActivity.ifPlaySound) gameActivity.playSound(3,0);
                                freeCards.get(i).setNum(chooseCard.getNum());
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
                                        freeCards.get(newCardIndex).setNum(nextRandom());
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
                                    } else {
                                        ifEnd = 1;
                                    }
                                }
                                stepNum++;
                                break;
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

    private int randomNumber(int start,int end){
        if(start==end) return -1;
        long time=System.currentTimeMillis();
        time%=10000;
        double rand=Math.random();
        randomSeed=(int)(time*rand*randomSeed)%10000;
        return start+randomSeed%(end-start);
    }

    private void changeNext(int index){
        for(int i=index+1;i<3;i++){
            chooseCards[i-1].setNum(chooseCards[i].getNum());
        }
        chooseCards[2].setNum(nextCard.getNum());
        int nextNum=nextRandom();
        if(choosedMaxNum>1){
            if(chooseCards[0].getNum()==chooseCards[1].getNum()
                    && chooseCards[1].getNum()==chooseCards[2].getNum()){
                if(chooseCards[0].getNum()==nextNum){
                    if(nextNum+1>choosedMaxNum) nextCard.setNum(nextNum-1);
                    else nextCard.setNum(nextNum+1);
                }
                else nextCard.setNum(nextNum);
            }
            else nextCard.setNum(nextNum);
        }
        else nextCard.setNum(1);
    }

    private void mergeCard(Card card){
        toBeChangedCards.add(card);
        if(card.getM()>0){
            if(!toBeChangedCards.contains(cards[card.getM()-1][card.getN()]) && card.getNum()==cards[card.getM()-1][card.getN()].getNum()) {
                if(card.getDeleteNum()+1>deleteMaxNum) deleteMaxNum=card.getDeleteNum()+1;
                cards[card.getM() - 1][card.getN()].setDeleteNum(card.getDeleteNum()+1);
                mergeCard(cards[card.getM() - 1][card.getN()]);
            }
        }
        if(card.getM()<dimension-1){
            if(!toBeChangedCards.contains(cards[card.getM()+1][card.getN()]) && card.getNum()==cards[card.getM()+1][card.getN()].getNum()) {
                if(card.getDeleteNum()+1>deleteMaxNum) deleteMaxNum=card.getDeleteNum()+1;
                cards[card.getM() + 1][card.getN()].setDeleteNum(card.getDeleteNum()+1);
                mergeCard(cards[card.getM() + 1][card.getN()]);
            }
        }
        if(card.getN()>0){
            if(!toBeChangedCards.contains(cards[card.getM()][card.getN()-1]) && card.getNum()==cards[card.getM()][card.getN()-1].getNum()) {
                if(card.getDeleteNum()+1>deleteMaxNum) deleteMaxNum=card.getDeleteNum()+1;
                cards[card.getM()][card.getN() - 1].setDeleteNum(card.getDeleteNum()+1);
                mergeCard(cards[card.getM()][card.getN() - 1]);
            }
        }
        if(card.getN()<dimension-1){
            if(!toBeChangedCards.contains(cards[card.getM()][card.getN()+1]) && card.getNum()==cards[card.getM()][card.getN()+1].getNum()) {
                if(card.getDeleteNum()+1>deleteMaxNum) deleteMaxNum=card.getDeleteNum()+1;
                cards[card.getM()][card.getN() + 1].setDeleteNum(card.getDeleteNum()+1);
                mergeCard(cards[card.getM()][card.getN() + 1]);
            }
        }
    }

    private void changeChoosedMaxNum(){
        int maxcount=0;
        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                if(cards[i][j].getNum()==maxnum)
                    maxcount++;
            }
        }
        if(maxcount>=2) choosedMaxNum=maxnum;
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

    private int nextRandom(){
        int result=1;
        TreeMap<Integer, Double> rate = randRate(choosedMaxNum);
        Double p = Math.random();
        for (int i = 0; i < choosedMaxNum; i++){
            if (p >= rate.get(i) && p <= rate.get(i + 1)){
                result=i+1;
                break;
            }
        }
        return result;
    }

}
