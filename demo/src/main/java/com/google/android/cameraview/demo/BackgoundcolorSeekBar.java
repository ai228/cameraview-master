/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.cameraview.demo;

import static android.view.View.MeasureSpec.getSize;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextPaint;
import android.util.AttributeSet;

public class BackgoundcolorSeekBar extends AppCompatSeekBar {

    //  刻度说明文本，数组数量跟刻度数量一致，跟mTextSize的长度要一致
    private String[] mTickMarkTitles = new String[]{
            "无",
            "标准",
            "中","深"
    };
    // 刻度代表的字体大小
    public static int[] mTextSize = new int[]{
            18,
            24,
            26
    };
    /**
     * Paint.ANTI_ALIAS_FLAG ：抗锯齿标志
     * Paint.FILTER_BITMAP_FLAG : 使位图过滤的位掩码标志
     * Paint.DITHER_FLAG : 使位图进行有利的抖动的位掩码标志
     * Paint.UNDERLINE_TEXT_FLAG : 下划线
     * Paint.STRIKE_THRU_TEXT_FLAG : 中划线
     * Paint.FAKE_BOLD_TEXT_FLAG : 加粗
     * Paint.LINEAR_TEXT_FLAG : 使文本平滑线性扩展的油漆标志
     * Paint.SUBPIXEL_TEXT_FLAG : 使文本的亚像素定位的绘图标志
     * Paint.EMBEDDED_BITMAP_TEXT_FLAG : 绘制文本时允许使用位图字体的绘图标志
     * */
    // 刻度文本画笔
    public static final Paint mTickMarkTitlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    //  刻度文本字体大小
    private int mTickMarkTitleTextSize = 18;
    // 刻度文本跟刻度之间的间隔
    private int mOffsetY = 60;
    // 刻度线的高度
    private int mLineHeight = 10;
    // 保存位置大小信息
    private final Rect mRect = new Rect();

    // ...省略一些其他构造函数
    public BackgoundcolorSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        //  初始化刻度文本字体大小
        mTickMarkTitleTextSize = getSize(mTickMarkTitleTextSize);
        // 刻度文本跟刻度之间的间隔
        mOffsetY = getSize(mOffsetY);
        // 刻度线的高度
        mLineHeight = getSize(mLineHeight);
        // 刻度文字的对齐方式为居中对齐
        mTickMarkTitlePaint.setTextAlign(Paint.Align.CENTER);
        // 刻度文字的字体颜色
        mTickMarkTitlePaint.setColor(ContextCompat.getColor(getContext(), R.color.primaryDark));
        // 设置最大刻度值为字体大小数组的长度
        setMax(mTextSize.length);
        // 设置当前的刻度
        setProgress(1);
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 原来的布局大小
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        // 以最大的字体为基础，加上刻度字体大小
        h += getSize(mTextSize[mTextSize.length - 1]);
        // 加上与刻度之间的间距大小
        h += mOffsetY;
        // 保存测量结果
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(w, wm), MeasureSpec.makeMeasureSpec(h, hm));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 刻度长度
        int maxLength = getMax();
        int width = getWidth();
        int height = getHeight();
        int h2 = height / 2; // 居中

        // 画刻度背景
        mRect.left = getPaddingLeft();
        mRect.right = width - getPaddingRight();
        mRect.top = h2 - getSize(1); // 居中
        mRect.bottom = mRect.top + getSize(1); // 1.5f为直线的高度
        // 直线的长度
        int lineWidth = mRect.width();
        // 画直线
        canvas.drawRect(mRect, mTickMarkTitlePaint);

        //  遍历刻度，画分割线和刻度文本
        for (int i = 0; i <= maxLength; i++) {

            // 刻度的起始间隔 = 左间距 + (线条的宽度 * 当前刻度位置 / 刻度长度)
            int thumbPos = getPaddingLeft() + (lineWidth * i / maxLength);
            // 画分割线
            mRect.top = h2 - mLineHeight / 2;
            mRect.bottom = h2 + mLineHeight / 2;
            mRect.left = thumbPos;
            mRect.right = thumbPos + getSize(1); // 直线的宽度为1.5
            canvas.drawRect(mRect, mTickMarkTitlePaint);

            // 画刻度文本
            String title = mTickMarkTitles[i % mTickMarkTitles.length]; // 拿到刻度文本
//            mTickMarkTitlePaint.getTextBounds(title, 0, title.length(), mRect); // 计算刻度文本的大小以及位置
            mTickMarkTitlePaint.setTextSize(getSize(30)); // 设置刻度文字大小
            // 画文本
            canvas.drawText(title, thumbPos, getSize(mTextSize[mTextSize.length - 1]), mTickMarkTitlePaint);
        }
    }

    public static void setPaintText(int size){
        mTickMarkTitlePaint.setTextSize(getSize(size)); // 设置刻度文字大小
    }
}
