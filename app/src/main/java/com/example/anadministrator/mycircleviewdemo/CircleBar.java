package com.example.anadministrator.mycircleviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by 张祺钒
 * on2017/8/31.
 */

public class CircleBar extends View {

    private static final String TAG = "cn.netmoon.netmoondevicemanager.widget.CircleBar";

    private RectF mColorWheelRectangle = new RectF();
    private Paint mDefaultWheelPaint;
    private Paint mColorWheelPaint;
    private Paint textPaint;
    private float mColorWheelRadius;
    private float circleStrokeWidth;
    private float pressExtraStrokeWidth;
    private String mText;
    private int mCount;
    private float mSweepAnglePer;
    private float mSweepAngle;
    private int mTextSize;
    BarAnimation anim;
    public CircleBar(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    private void init(AttributeSet attrs, int defStyle) {

        circleStrokeWidth = dip2px(getContext(), 10);
        pressExtraStrokeWidth = dip2px(getContext(), 2);
        mTextSize = dip2px(getContext(), 40);

        mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorWheelPaint.setColor(0xFF29a6f6);
        mColorWheelPaint.setStyle(Paint.Style.STROKE);
        mColorWheelPaint.setStrokeWidth(circleStrokeWidth);

        mDefaultWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultWheelPaint.setColor(0xFFeeefef);
        mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
        mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setColor(0xFF333333);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(mTextSize);

        mText = "0";
        mSweepAngle = 0;

        anim = new BarAnimation();
        anim.setDuration(2000);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        int colors[] = new int[3];
        float positions[] = new float[3];

        // 第1个点
        colors[0] = 0xFF111111;
        positions[0] = 0;

        // 第2个点
        colors[1] = 0xFF999999;
        positions[1] = 0.5f;

        // 第3个点
        colors[2] = 0xFF111111;
        positions[2] = 1;

        LinearGradient shader = new LinearGradient(
                0, 0,
                0, 400,
                colors,
                positions,
                Shader.TileMode.MIRROR);

        //虚线圆
        PathEffect effects = new DashPathEffect(new float[]{50,20,50,20},1);
        mDefaultWheelPaint.setPathEffect(effects);

        mColorWheelPaint.setPathEffect(effects);
        mColorWheelPaint.setShader(shader);
        canvas.drawArc(mColorWheelRectangle, -90, 360, false, mDefaultWheelPaint);
        canvas.drawArc(mColorWheelRectangle, -90, mSweepAnglePer, false, mColorWheelPaint);
        Rect bounds = new Rect();
        String textstr=mCount+"%";

        textPaint.getTextBounds(textstr, 0, textstr.length(), bounds);
        canvas.drawText(
                textstr+"",
                (mColorWheelRectangle.centerX())
                        - (textPaint.measureText(textstr) / 2),
                mColorWheelRectangle.centerY() + bounds.height() / 2,
                textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        mColorWheelRadius = min - circleStrokeWidth -pressExtraStrokeWidth ;

        mColorWheelRectangle.set(circleStrokeWidth+pressExtraStrokeWidth, circleStrokeWidth+pressExtraStrokeWidth,
                mColorWheelRadius, mColorWheelRadius);
    }


    @Override
    public void setPressed(boolean pressed) {
        if (pressed) {
            mColorWheelPaint.setColor(0xFF165da6);
            textPaint.setColor(0xFF070707);
            mColorWheelPaint.setStrokeWidth(circleStrokeWidth+pressExtraStrokeWidth);
            mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth+pressExtraStrokeWidth);
            textPaint.setTextSize(mTextSize-pressExtraStrokeWidth);
        } else {
            mColorWheelPaint.setColor(0xFF29a6f6);
            textPaint.setColor(0xFF333333);
            mColorWheelPaint.setStrokeWidth(circleStrokeWidth);
            mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);
            textPaint.setTextSize(mTextSize);
        }
        super.setPressed(pressed);
        this.invalidate();
    }

    public void startCustomAnimation(){
        this.startAnimation(anim);
    }

    public void setText(String text){
        mText = text;
        this.startAnimation(anim);
    }

    public void setSweepAngle(float sweepAngle){
        mSweepAngle = sweepAngle;

    }


    public class BarAnimation extends Animation {
        /**
         * Initializes expand collapse animation, has two types, collapse (1) and expand (0).
         * 1 will collapse view and set to gone
         */
        public BarAnimation() {

        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                mSweepAnglePer =  interpolatedTime * mSweepAngle;
                mCount = (int)(interpolatedTime * Float.parseFloat(mText));
            } else {
                mSweepAnglePer = mSweepAngle;
                mCount = Integer.parseInt(mText);
            }
            postInvalidate();
        }
    }

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }


}

