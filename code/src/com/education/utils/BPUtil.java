package com.souyidai.investment.android.utils;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.Random;

/**
 * Created by su on 2014/6/6.
 */
public class BPUtil {

    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    //default settings
    private static final int DEFAULT_CODE_LENGTH = 4;
    private static final int DEFAULT_FONT_SIZE = 20;
    private static final int DEFAULT_LINE_NUMBER = 8;
    private static final int BASE_PADDING_LEFT = 5, RANGE_PADDING_LEFT = 10, BASE_PADDING_TOP = 15, RANGE_PADDING_TOP = 10;
    private static final int DEFAULT_WIDTH = 60, DEFAULT_HEIGHT = 30;

    //settings decided by the layout xml
    //canvas mWidth and mHeight
    private int mWidth = DEFAULT_WIDTH, mHeight = DEFAULT_HEIGHT;

    //mRandom word space and padding_top
    private int mBasePaddingLeft = BASE_PADDING_LEFT, mRangePaddingLeft = RANGE_PADDING_LEFT,
            mBasePaddingTop = BASE_PADDING_TOP, mRangePaddingTop = RANGE_PADDING_TOP;

    //number of chars, lines; font size
    private int mCodeLength = DEFAULT_CODE_LENGTH, mLineNumber = DEFAULT_LINE_NUMBER, mFontSize = DEFAULT_FONT_SIZE;

    //variables
    private String mCode;
    private int mPaddingLeft, mPaddingTop;
    private Random mRandom = new Random();

    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public void setBasePaddingLeft(int mBasePaddingLeft) {
        this.mBasePaddingLeft = mBasePaddingLeft;
    }

    public void setRangePaddingLeft(int mRangePaddingLeft) {
        this.mRangePaddingLeft = mRangePaddingLeft;
    }

    public void setBasePaddingTop(int mBasePaddingTop) {
        this.mBasePaddingTop = mBasePaddingTop;
    }

    public void setRangePaddingTop(int mRangePaddingTop) {
        this.mRangePaddingTop = mRangePaddingTop;
    }

    public void setCodeLength(int mCodeLength) {
        this.mCodeLength = mCodeLength;
    }

    public void setLineNumber(int mLineNumber) {
        this.mLineNumber = mLineNumber;
    }

    public void setFontSize(int mFontSize) {
        this.mFontSize = mFontSize;
    }

    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    public void setPaddingLeft(int mPaddingLeft) {
        this.mPaddingLeft = mPaddingLeft;
    }

    public void setPaddingTop(int mPaddingTop) {
        this.mPaddingTop = mPaddingTop;
    }

    public void initSizeParameters(DisplayMetrics displayMetrics) {
        mWidth = dp2px(mWidth, displayMetrics);
        mHeight = dp2px(mHeight, displayMetrics);
        mBasePaddingLeft = dp2px(mBasePaddingLeft, displayMetrics);
        mRangePaddingLeft = dp2px(mRangePaddingLeft, displayMetrics);
        mBasePaddingTop = dp2px(mBasePaddingTop, displayMetrics);
        mRangePaddingTop = dp2px(mRangePaddingTop, displayMetrics);

        mFontSize = sp2px(mFontSize, displayMetrics);
    }



    public static int dp2px(int dpValue, DisplayMetrics displayMetrics) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, displayMetrics);
    }

    public static int sp2px(int spValue, DisplayMetrics displayMetrics) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, displayMetrics);
    }

    public Bitmap createBitmap() {
        mPaddingLeft = 0;

        Bitmap bp = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
        Canvas c = new Canvas(bp);

        mCode = createCode();

        c.drawColor(Color.WHITE);

        Rect targetRect = new Rect(0, 0, mWidth, mHeight);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(mFontSize);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int baseline = (int) (targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);

//        int textLen = (int) paint.measureText(mCode);
//        int textLeft = (mWidth - textLen) / 2;
        int center = targetRect.centerX();
        int half = (int) paint.measureText(mCode) / 2;
        int textLeft = targetRect.centerX() - (int) paint.measureText(mCode.substring(0, 4)) / 2 + (int) paint.measureText(mCode.substring(0, 4)) / 8;
        for (int i = 0; i < mCode.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            c.drawText(mCode.charAt(i) + "", textLeft, baseline, paint);
            textLeft += paint.measureText(String.valueOf(mCode.charAt(i)));
        }

        for (int i = 0; i < mLineNumber; i++) {
            drawLine(c, paint);
        }

        c.save(Canvas.ALL_SAVE_FLAG);//保存
        c.restore();//
        return bp;
    }

    public String getCode() {
        return mCode;
    }

    private String createCode() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < mCodeLength; i++) {
            buffer.append(CHARS[mRandom.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = mRandom.nextInt(mWidth);
        int startY = mRandom.nextInt(mHeight);
        int stopX = mRandom.nextInt(mWidth);
        int stopY = mRandom.nextInt(mHeight);
        paint.setStrokeWidth(2);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    private int randomColor() {
        return randomColor(1);
    }

    private int randomColor(int rate) {
        int red = mRandom.nextInt(256) / rate;
        int green = mRandom.nextInt(256) / rate;
        int blue = mRandom.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }

    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(mRandom.nextBoolean());  //true为粗体，false为非粗体
        float skewX = mRandom.nextInt(11) / 10;
        skewX = (skewX * 30 - 15) / 100;
        skewX = mRandom.nextBoolean() ? skewX : -skewX;
        paint.setTextSkewX(skewX); //float类型参数，负数表示右斜，整数左斜
//      paint.setUnderlineText(true); //true为下划线，false为非下划线
//      paint.setStrikeThruText(true); //true为删除线，false为非删除线
    }

    private void randomPadding() {
        mPaddingLeft += mBasePaddingLeft + mRandom.nextInt(mRangePaddingLeft);
        mPaddingTop = mBasePaddingTop + mRandom.nextInt(mRangePaddingTop);
    }
}
