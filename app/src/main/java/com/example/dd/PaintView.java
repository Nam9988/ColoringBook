package com.example.dd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class PaintView extends View {

    public static int BRUSH_SIZE = 20;
    public static final int DEFAULT_COLOR = Color.RED;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<FingerPath> paths = new ArrayList<>();
    private ArrayList<FingerPath> undoPaths = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps=new ArrayList<>();
    private ArrayList<Bitmap> undobitmap=new ArrayList<>();
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private boolean emboss;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Bitmap mBitmap;
    private Bitmap mBitmap1;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private DisplayMetrics displayMetrics;
    private int isFlood;

    public PaintView(Context context) {
        super(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PaintView(Context context, AttributeSet attrs) {

        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

        mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);

    }

    public void init(DisplayMetrics metrics) {

        displayMetrics = metrics;
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
        isFlood=0;
    }

    public  void setbm(Bitmap bm){


        ///resize
        mBitmap1=bm;
        float bmwidth=bm.getWidth();
        float bmheigh=bm.getHeight();
        float newwidth=0;
        float newhight=0;
      //  mBitmap1= BitmapFactory.decodeResource(getResources(),R.drawable.im);
        if(bmwidth>displayMetrics.widthPixels){
            newwidth=(float) displayMetrics.widthPixels;
            newhight=bmheigh/bmwidth *newwidth;
        }
        if(bmheigh>displayMetrics.heightPixels){
            newhight=(float) displayMetrics.heightPixels;
            newwidth=bmwidth/bmheigh*newhight;
        }
        if(newhight!=0&&newwidth!=0)
        mBitmap1=Bitmap.createScaledBitmap(mBitmap1, (int)newwidth, (int)newhight, false);


        //ghep
        mBitmap = Bitmap.createBitmap(displayMetrics.widthPixels, displayMetrics.heightPixels, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.WHITE);
        mBitmap1=mergeToPin(mBitmap,mBitmap1);


        mBitmap=Bitmap.createBitmap(mBitmap1);
        mCanvas.setBitmap(mBitmap);
        invalidate();
    }

    public void normal() {
        emboss = false;
        blur = false;
    }

//    public void emboss() {
//        emboss = true;
//        blur = false;
//    }
//
//    public void blur() {
//        emboss = false;
//        blur = true;
//    }

    public void clear() {
       // backgroundColor = DEFAULT_BG_COLOR;
       // mBitmap.eraseColor(Color.TRANSPARENT);


        mBitmap = Bitmap.createBitmap(displayMetrics.widthPixels, displayMetrics.heightPixels, Bitmap.Config.ARGB_8888);

        if(mBitmap1!=null) {
            mBitmap = Bitmap.createBitmap(mBitmap1);
        }

        mCanvas.setBitmap(mBitmap);
        paths.clear();
        undoPaths.clear();
        bitmaps.clear();
        undobitmap.clear();
        normal();
        invalidate();
    }

    public void undo() {
        if (bitmaps.size() > 0) {
          //  clearDraw();

            undobitmap.add(bitmaps.remove(bitmaps.size() - 1));
            if(bitmaps.size()>0){
                Bitmap temp=Bitmap.createBitmap(bitmaps.get(bitmaps.size()-1));
            mBitmap=Bitmap.createBitmap(temp);
            paths.clear();}
            else
                mBitmap=Bitmap.createBitmap(mBitmap1);
            mCanvas.setBitmap(mBitmap);
            paths.clear();
            invalidate();
            Toast.makeText(getContext(),String.valueOf(bitmaps.size()),Toast.LENGTH_SHORT).show();
        }
    }
    public void redo(){
        if (undobitmap.size() > 0) {
            Bitmap temp =Bitmap.createBitmap(undobitmap.remove(undobitmap.size()-1));
            bitmaps.add(temp);
            mBitmap=Bitmap.createBitmap(temp);
            mCanvas.setBitmap(mBitmap);
            invalidate();
        }
    }
//    private void clearDraw() {
//        mBitmap = Bitmap.createBitmap(displayMetrics.widthPixels, displayMetrics.heightPixels, Bitmap.Config.ARGB_8888);
//        if(mBitmap1!=null)
//        mBitmap=Bitmap.createBitmap(mBitmap1);
//        mCanvas.setBitmap(mBitmap);
//      //  mCanvas.drawBitmap(mBitmap1, 0, 0, mBitmapPaint);;
//        invalidate();
//    }

    @Override
    protected void onDraw(Canvas canvas) {
       // canvas.save();
        // ps.drawColor(backgroundColor);
        for (FingerPath fp : paths) {
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);
//            if (fp.emboss)
//                mPaint.setMaskFilter(mEmboss);
//            else if (fp.blur)
//                mPaint.setMaskFilter(mBlur);
            mCanvas.drawPath(fp.path, mPaint);
        }

        canvas.drawBitmap(mBitmap, 0, 0, null);
       // canvas.restore();
    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, emboss, blur, strokeWidth, mPath);
        paths.add(fp);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }


    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
              if(isFlood==0)
                  touchStart(x, y);
              else {

                  int clr_int = mBitmap.getPixel((int) x, (int) y);

                  QueueLinearFloodFiller f = new QueueLinearFloodFiller(mBitmap, clr_int, currentColor);
               //   f.setFillColor(mPaint.getColor());
                  f.setTargetColor(clr_int);
                  f.setTolerance(10);
                  mBitmap = Bitmap.createBitmap(f.floodFill((int) x, (int) y)); //fill bitmap, at p1 with p1's color and fill with Color fr
                  mCanvas.setBitmap(mBitmap);
              }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                    if(isFlood==0){
                    touchMove(x, y);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isFlood==0) {
                    touchUp();
                }
                invalidate();
                Bitmap temp =Bitmap.createBitmap(mBitmap);
                bitmaps.add(temp);
                mCanvas.setBitmap(mBitmap);
                Toast.makeText(getContext(),String.valueOf(paths.size()),Toast.LENGTH_SHORT).show();
                paths.clear();
                break;
        }

        return true;
    }

    public void setColorPaint(int color) {
        currentColor=color;
    }
    public void resize(int size) {
        strokeWidth=BRUSH_SIZE*(size+1);
    }
    public void modeFlood() {
        isFlood=(isFlood+1)%2;
    }
    public int getis(){
        return  isFlood;
    }
    public static Bitmap mergeToPin(Bitmap back, Bitmap front) {
        Bitmap result = Bitmap.createBitmap(back.getWidth(), back.getHeight(), back.getConfig());
        Canvas mecanvas = new Canvas(result);
        int widthBack = back.getWidth();
        int widthFront = front.getWidth();
        float move = (widthBack - widthFront) / 2;
        mecanvas.drawBitmap(back, 0f, 0f, null);
        mecanvas.drawBitmap(front, move, 0, null);
        return result;
    }
}
