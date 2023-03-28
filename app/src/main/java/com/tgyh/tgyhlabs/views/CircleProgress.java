package com.tgyh.tgyhlabs.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.tgyh.tgyhlabs.R;

/**
 * ClassName CircleProgress
 * Author TGYH
 * Date 2023/3/25 10:21
 * Version 1.0
 */
public class CircleProgress extends View {
    public static final String PROGESS = "progess";
    public static final String P_BUNDLE = "pBundle";
    private int radius;
    private int color;
    private int lineWidth;
    private int progress;
    private int textSize;
    private static Paint paint = new Paint();
    private static RectF recfInCircle;
    private static  Rect bound = new Rect();
    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.CircleProgress);
        color = ta.getColor(R.styleable.CircleProgress_ccolor,0xffff0000);
        radius = (int)ta.getDimension( R.styleable.CircleProgress_cradius,dp2px(30));
        lineWidth = (int)ta.getDimension( R.styleable.CircleProgress_clineWidth,dp2px(3));
        progress = ta.getInt( R.styleable.CircleProgress_android_progress,30);
        textSize = (int)ta.getDimension( R.styleable.CircleProgress_android_textSize,dp2px(16));

        /*int attrNum = ta.getIndexCount();
        for (int i = 0; i < attrNum; i++) {
            int index = ta.getIndex(i);
            if(index == R.styleable.CircleProgress_radius){
                radius = (int)ta.getDimension( index,dp2px(30));
            }
            if(index == R.styleable.CircleProgress_color){
                color = ta.getColor(R.styleable.CircleProgress_color,0xffff0000);
            }
            if(index == R.styleable.CircleProgress_lineWidth){
                lineWidth = (int)ta.getDimension( index,dp2px(3));
            }
            if(index == R.styleable.CircleProgress_android_progress){
                progress = ta.getInt( index,0);
            }
            if(index == R.styleable.CircleProgress_android_textSize){
                textSize = (int)ta.getDimension( index,dp2px(16));
            }
        }*/
        ta.recycle();
        initPaint();
    }

    private void initPaint() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(lineWidth * 1.0f / 4);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else {
            int mesureWidth = onMesaureWidth() + getPaddingRight() + getPaddingLeft();
            if (widthMode == MeasureSpec.AT_MOST){
                width = Math.min(widthSize,mesureWidth);
            }
            else {
                width = mesureWidth;
            }
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            int measureHeight = onMesaureHeight() + getPaddingTop() +getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST){
                height = Math.min(heightSize,measureHeight);
            }else {
                height = measureHeight;
            }
        }
        setMeasuredDimension(width,height);
    }

    private int onMesaureHeight() {
        return radius * 2;
    }

    private int onMesaureWidth() {
        return radius * 2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        recfInCircle = new RectF(0,0,
                w- getPaddingRight() - getPaddingLeft(),h - getPaddingBottom()- getPaddingTop());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPaint();
        int width = getWidth();
        int height = getHeight();
        canvas.drawCircle(width / 2,height / 2, width / 2 - getPaddingLeft() - paint.getStrokeWidth() / 2,paint);
        paint.setStrokeWidth(lineWidth);
        canvas.save();
        canvas.translate(getPaddingLeft(),getPaddingTop());
        float progressAngle = progress * 1.0f / 100 * 360;
        canvas.drawArc(recfInCircle,270,progressAngle,false,paint);
        canvas.restore();
        String text = progress + "%";
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.getTextBounds(text,0,text.length(),bound);
        int textHeight = bound.height();
        canvas.drawText(text,0,text.length(),width / 2,height / 2 + textHeight / 2,paint);
        super.onDraw(canvas);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt(PROGESS,progress);
        bundle.putParcelable(P_BUNDLE,super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle)state;
        Parcelable parcelable = bundle.getParcelable(P_BUNDLE);
        super.onRestoreInstanceState(parcelable);
        progress = bundle.getInt(PROGESS);
    }

    private float dp2px(int dpVal) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,dpVal,getResources().getDisplayMetrics()
        );
    }
}
