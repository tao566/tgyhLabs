package com.tgyh.widght.Views;

import static android.view.View.MeasureSpec.*;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.tgyh.widght.R;

/**
 * @ClassName SwitchButton
 * @Author TGYH
 * @Date 2023/3/28 14:07
 * Version 1.0
 */
public class SwitchButton extends View {
    //关闭时主颜色 白色
    public static final int BG_COLOR = 0xffffffff;
    //关闭时线条颜色 灰色1
    public static final int STOCK_COLOR = 0XFFcfcfcf;
    //开启时主颜色 绿色
    public static final int CHECKED_COLOR = 0xff4bd763;
    //禁用颜色
    public static final int DISABLED_COLOR = 0xffbbbbbb;
    //父控件状态key
    public static final String P_BUNDLE = "pBundle";
    //状态保存
    public static final String CHECKED = "checked";
    public static final String ENABLED = "enabled";

    //按钮属性
    private boolean checked;
    private boolean enabled;

    //ui属性
    //左右圆半径
    private int radius = 80;
    //平行线长度
    private int line = 120;
    //园沿着水平线移动的横坐标
    private int circleX;
    //圆开始时的x坐标
    private int startX;
    //园结束时的x坐标
    private int endX;
    //线条宽度
    private int lineWidth = 2;

    //画图时需要的一些对象
    private static Paint paint = new Paint();
    //左右两边圆弧需要的矩形对象
    private static RectF rectFL = new RectF();
    private static RectF rectFR = new RectF();

    //监听事件
    private OnListener onListener;

    public void setOnListener(SwitchButton.OnListener onListener) {
        this.onListener = onListener;
    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        checked = ta.getBoolean(R.styleable.SwitchButton_android_checked,false);
        enabled = ta.getBoolean(R.styleable.SwitchButton_android_enabled,true);
        ta.recycle();
        initPaint();
    }

    private void initPaint() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(BG_COLOR);
        paint.setStrokeWidth(lineWidth);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = line + radius * 2;
        int heightT =  MeasureSpec.getSize(heightMeasureSpec);
        int widthT =  MeasureSpec.getSize(widthMeasureSpec);
        //取最小值做宽度
        int size = Math.min(widthT,heightT);
        switch (MeasureSpec.getMode(widthMeasureSpec)){
            case AT_MOST:
            case UNSPECIFIED:
                break;
            case EXACTLY:
                width = size;
                radius = size / 7 * 2;
                line = radius + radius / 2;
                break;
            default:
        }
        int height = radius * 2;
        circleX = radius;
        startX = radius;
        endX = radius + line;
        setMeasuredDimension(width + (int)paint.getStrokeWidth() + 2, (int) (height + paint.getStrokeWidth()) + 2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectFL = new RectF(0,0,radius * 2,radius * 2);
        rectFR = new RectF(line,0,line + radius * 2,radius * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        int padding = ((int)paint.getStrokeWidth() + 2) / 2;
        canvas.translate(padding,padding);
        if(checked){
            paint.setColor(CHECKED_COLOR);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawArc(rectFL,90,180,true,paint);
            canvas.drawRect(radius,0,radius + line ,radius * 2,paint);
            canvas.drawArc(rectFR,270,180,true,paint);
            paint.setColor(BG_COLOR);
            //-3是为了让园与边界有空隙
            canvas.drawCircle(circleX,radius,radius-3,paint);
        }else{
            paint.setColor(BG_COLOR);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawArc(rectFL,90,180,true,paint);
            canvas.drawRect(radius,0,radius + line ,radius * 2,paint);
            canvas.drawArc(rectFR,270,180,true,paint);
            paint.setColor(STOCK_COLOR);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(circleX,radius,radius,paint);
            canvas.drawArc(rectFL,90,180,false,paint);
            canvas.drawLine(radius,0,radius + line,0,paint);
            canvas.drawLine(radius ,radius * 2 ,radius + line,radius * 2,paint);
            canvas.drawArc(rectFR,270,180,false,paint);
        }
        canvas.restore();
    }

    public void setCircleX(int circleX) {
        this.circleX = circleX;
        invalidate();
    }

    public int getStartX() {
        return startX;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getEndX() {
        return endX;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(P_BUNDLE,super.onSaveInstanceState());
        bundle.putBoolean(CHECKED,checked);
        bundle.putBoolean(ENABLED,enabled);
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle)state;
        super.onRestoreInstanceState(((Bundle) state).getParcelable(P_BUNDLE));
        checked = bundle.getBoolean(CHECKED);
        enabled = bundle.getBoolean(ENABLED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                if(checked){
                    checked = false;
                    ObjectAnimator.ofInt(this,"circleX",getEndX(),getStartX()).setDuration(500).start();
                }else {
                    checked = true;
                    ObjectAnimator.ofInt(this,"circleX",getStartX(),getEndX()).setDuration(500).start();
                }
                if(onListener != null){
                    onListener.switched(this,checked);
                }
                break;
            default:break;
        }
       return true;
    }

    public interface OnListener{
        public void switched(SwitchButton button,boolean cheaked);
    }
}
