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
 * @ClassName ReviewProgress
 * @Author TGYH
 * @Date 2023/3/28 9:26
 * Version 1.0
 */
public class ReviewProgress extends View {
    public static final String PROGRESS = "progress";
    public static final String P_INSTANCE = "pInstance";
    private int color;
    private int lineWidth;
    private int radius;
    private int progress;
    private int textSize;
    private int width;
    private int height;
    private RectF rectF;
    private String progressStr;
    private static  Rect bound = new Rect();
    private static Paint paint = new Paint();

    public ReviewProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ReviewProgress);
            color = ta.getColor(R.styleable.ReviewProgress_color,0xffff0000);
            lineWidth = (int)ta.getDimension(R.styleable.ReviewProgress_lineWidth,dp2px(30));
            radius = (int)ta.getDimension(R.styleable.ReviewProgress_radius,dp2px(60));
            progress = ta.getInt(R.styleable.ReviewProgress_android_progress,0);
            textSize = (int)ta.getDimension(R.styleable.ReviewProgress_android_textSize,dp2px(10));
        ta.recycle();
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if(widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else{
            int t = onMesureWidth() + getPaddingLeft() + getPaddingRight();
            if(widthMode == MeasureSpec.AT_MOST){
                width = Math.min(widthSize,t);
            }else {
                width = t;
            }
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else{
            int t = onMesureHeight() + getPaddingTop() + getPaddingBottom();
            if(heightMode == MeasureSpec.AT_MOST){
                height = Math.min(heightSize,t);
            }else {
                height = t;
            }
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF = new RectF(0,0,radius * 2,radius * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float ox = getWidth() / 2;
        float oy = getHeight() / 2;

        paint.setStrokeWidth(lineWidth / 4);
            canvas.drawCircle(ox,oy,radius,paint);
        paint.setStrokeWidth(lineWidth);

        canvas.save();
            canvas.translate(getWidth() / 2 - radius,getHeight() / 2 - radius);
            float progressAngle = progress * 1.0f / 100 * 360;
            canvas.drawArc(rectF,270,progressAngle,false,paint);
        canvas.restore();

        paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.CENTER);
            progressStr = progress + "%";
            paint.getTextBounds(progressStr,0,progressStr.length(),bound);
            int textHeight = bound.height();
            canvas.drawText(progressStr,getWidth() / 2,getHeight() / 2 + textHeight / 2 ,paint);
        paint.setStyle(Paint.Style.STROKE);

        super.onDraw(canvas);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt(PROGRESS,progress);
        bundle.putParcelable(P_INSTANCE,super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle)state;
        super.onRestoreInstanceState(bundle.getParcelable(P_INSTANCE));
        progress = bundle.getInt(PROGRESS);
    }

    private int onMesureHeight() {
        return radius * 2;
    }

    private int onMesureWidth() {
        return radius * 2;
    }

    private void initPaint() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(lineWidth);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
    }

    private float dp2px(int dpVal) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,dpVal,getResources().getDisplayMetrics()
        );
    }
}
