package com.numberlimit.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.numberlimit.gameview.ChallengeModeView;
import com.numberlimit.gameview.ClassicModeView;
import com.numberlimit.gameview.PropModeView;

public class Card {
	
	private int num;
	private RectF cardRectF;
	private float left;
	private float top;
	private int cardLength;
	private int m;
	private int n;
	private int deleteNum=0;

	int proptime=3;
	//private Context context=null;
	
	public Card(float left,float top,int cardLength,int num){
		//this.context=context;
		this.left=left;
		this.top=top;
		this.cardLength=cardLength;
		this.num=num;
		RectF rectF=new RectF(left,top,left+cardLength,top+cardLength);
		cardRectF=rectF;
	}
	
	public int getNum(){
		return num;
	}
	
	public void setNum(int num){ this.num=num;}

	public int getDeleteNum() { return deleteNum; }

	public void setDeleteNum(int deleteNum) { this.deleteNum=deleteNum; }

	public RectF getCardRectF(){ return cardRectF; }

	public void setCardRectF(RectF new_cardRectF){
		this.left=new_cardRectF.left;
		this.top=new_cardRectF.top;
		this.cardRectF=new_cardRectF;
	}

	public float getLeft(){ return left; }

	public float getTop(){ return top; }

	public void setM(int m){ this.m=m; }

	public void setN(int n){ this.n=n; }

	public int getM(){ return m; }

	public int getN(){ return n; }

	public int getProptime() {
		return proptime;
	}

	public void setProptime(int proptime) {
		this.proptime = proptime;
	}

	public boolean ifChoose(float x, float y){
		int incision=8;
		RectF centerRectF=new RectF(this.left+this.cardLength/incision,this.top+this.cardLength/incision,this.left+(incision-1)*this.cardLength/incision,this.top+(incision-1)*this.cardLength/incision);
		return centerRectF.contains(x,y);
	}

	public boolean ifIntersect(Card card){
		int incision=8;
		RectF centerRectF=new RectF(this.left+this.cardLength/incision,this.top+this.cardLength/incision,this.left+(incision-1)*this.cardLength/incision,this.top+(incision-1)*this.cardLength/incision);
		return centerRectF.contains(card.getCardRectF().centerX(),card.getCardRectF().centerY());
	}

	public void drawCard_init(View view,Canvas canvas,int sign){
		Paint paint=new Paint();
		paint.setAntiAlias(true);

		switch(sign){
			case 1: {
				ClassicModeView classicModeView = (ClassicModeView) view;
				Bitmap bitmap = classicModeView.getBitmap(num);
				canvas.drawBitmap(bitmap, left, top, paint);
				break;
			}
			case 2: {
				PropModeView propModeView = (PropModeView) view;
				Bitmap bitmap = propModeView.getBitmap(num);
				canvas.drawBitmap(bitmap, left, top, paint);
				if(num<0 && num>-8 && num!=-1) {
					Bitmap proptimeBitmap = propModeView.getProptimeBitmap(proptime);
					canvas.drawBitmap(proptimeBitmap,left+cardLength*1.0f/5,top+cardLength-cardLength*4.0f/23,paint);
				}
				break;
			}
			case 3: {
				ChallengeModeView challengeModeView = (ChallengeModeView) view;
				Bitmap bitmap = challengeModeView.getBitmap(num);
				canvas.drawBitmap(bitmap, left, top, paint);
				if(num<0 && num>-8 && num!=-1) {
					Bitmap proptimeBitmap = challengeModeView.getProptimeBitmap(proptime);
					canvas.drawBitmap(proptimeBitmap,left+cardLength*1.0f/5,top+cardLength-cardLength*4.0f/23,paint);
				}
				break;
			}
		}

	}

	public void drawCard_move(View view,Canvas canvas,float x,float y,float x_dif,float y_dif,int sign){

		left=x-x_dif;
		top=y-y_dif;
		Paint paint=new Paint();
		paint.setAntiAlias(true);

		switch(sign){
			case 1: {
				ClassicModeView classicModeView = (ClassicModeView) view;
				Bitmap bitmap = classicModeView.getBitmap(num);
				canvas.drawBitmap(bitmap, left, top, paint);
				break;
			}
			case 2: {
				PropModeView propModeView = (PropModeView) view;
				Bitmap bitmap = propModeView.getBitmap(num);
				canvas.drawBitmap(bitmap, left, top, paint);
				break;
			}
			case 3: {
				ChallengeModeView challengeModeView = (ChallengeModeView) view;
				Bitmap bitmap = challengeModeView.getBitmap(num);
				canvas.drawBitmap(bitmap, left, top, paint);
				break;
			}
		}

		cardRectF=new RectF(left,top,left+cardLength,top+cardLength);

	}

	public void drawCard_rotate(View view,Canvas canvas,int index,int sign){
		Paint paint=new Paint();
		paint.setAntiAlias(true);

		Bitmap bitmap=null;
		switch(sign){
			case 1: {
				ClassicModeView classicModeView = (ClassicModeView) view;
				bitmap = classicModeView.getBitmap(num);
				break;
			}
			case 2: {
				PropModeView propModeView = (PropModeView) view;
				bitmap = propModeView.getBitmap(num);
				break;
			}
			case 3: {
				ChallengeModeView challengeModeView = (ChallengeModeView) view;
				bitmap = challengeModeView.getBitmap(num);
				break;
			}
		}
		Matrix matrix=new Matrix();
		float scaleHeight=1;
		float scaleWidth;
		if(index>=0 && index<=4)
			scaleWidth=1-(1/4f*index)/4f*2;
		else if(index>=5 && index<=8)
			scaleWidth=1-(1/4f*(8-index))/4f*2;
		else if(index>=9 && index<=12)
			scaleWidth=1-(1/4f*(index-8))/4f*2;
		else
			scaleWidth=1-(1/4f*(16-index))/4f*2;
		System.out.println("index="+index+",scaleWidth="+scaleWidth);
		matrix.postScale(scaleWidth,scaleHeight);

		Bitmap newBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		float newLeft=left+(bitmap.getWidth()-newBitmap.getWidth())/2;
		canvas.drawBitmap(newBitmap,newLeft,top,paint);
	}

	public void drawCard_hint(View view, Canvas canvas, Card card ,int sign){
		Paint paint=new Paint();
		paint.setAntiAlias(true);
		paint.setAlpha(128);

		switch(sign){
			case 1: {
				ClassicModeView classicModeView = (ClassicModeView) view;
				Bitmap bitmap = classicModeView.getBitmap(card.getNum());
				canvas.drawBitmap(bitmap, left, top, paint);
				break;
			}
			case 2: {
				PropModeView propModeView = (PropModeView) view;
				Bitmap bitmap = propModeView.getBitmap(card.getNum());
				canvas.drawBitmap(bitmap, left, top, paint);
				break;
			}
			case 3: {
				ChallengeModeView challengeModeView = (ChallengeModeView) view;
				Bitmap bitmap = challengeModeView.getBitmap(card.getNum());
				canvas.drawBitmap(bitmap, left, top, paint);
				break;
			}
		}

	}

}
