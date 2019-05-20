package com.androidgroup5.onlinecontact.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.androidgroup5.onlinecontact.R;


public class CharIndexView extends View {
    private static final int INDEX_NONE = -1;

    private static char[] CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '#'};
    /**
     * text大小
     */
    private float textSize = 24.f;
    /**
     * 字符颜色与索引颜色
     */
    private int textColor = Color.BLACK, indexTextColor = Color.RED;
    /**
     * 画笔
     */
    private TextPaint textPaint;
    /**
     * 每个item
     */
    private float itemHeight;
    /**
     * 文本居中时的位置
     */
    private float textY;
    /**
     * 当前位置
     */
    private int currentIndex = INDEX_NONE;

    private Drawable indexDrawable;

    public CharIndexView(Context context) {
        super(context);
        init(context, null);
    }

    public CharIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CharIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CharIndexView);
            textSize = a.getDimension(R.styleable.CharIndexView_indexTextSize, textSize);
            textColor = a.getColor(R.styleable.CharIndexView_charTextColor, textColor);
            indexTextColor = a.getColor(R.styleable.CharIndexView_indexTextColor, indexTextColor);
            a.recycle();
        }
        indexDrawable = context.getResources().getDrawable(R.drawable.charIndexColor);
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textHeight = fm.bottom - fm.top;
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        itemHeight = height / (float) CHARS.length;
        textY = itemHeight - (itemHeight - textHeight) / 2 - fm.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float centerX = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2.0f;
        float centerY = getPaddingTop() + textY;
        if (centerX <= 0 || centerY <= 0) return;
        for (int i = 0; i < CHARS.length; i++) {
            char c = CHARS[i];
            textPaint.setColor(i == currentIndex ? indexTextColor : textColor);
            canvas.drawText(String.valueOf(c), centerX, centerY, textPaint);
            centerY += itemHeight;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currentIndex = INDEX_NONE;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundDrawable(indexDrawable);
                currentIndex = computeCurrentIndex(event);
                if (listener != null) {
                    listener.onCharIndexSelected(String.valueOf(CHARS[currentIndex]));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                currentIndex = computeCurrentIndex(event);
                if (listener != null) {
                    listener.onCharIndexSelected(String.valueOf(CHARS[currentIndex]));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setBackgroundDrawable(null);
                if (listener != null) {
                    listener.onCharIndexSelected(null);
                }
                break;
        }
        if (currentIndex != this.currentIndex) {
            this.currentIndex = currentIndex;
            invalidate();
            if (this.currentIndex != INDEX_NONE && listener != null) {
                listener.onCharIndexChanged(CHARS[this.currentIndex]);
            }
        }
        return true;
    }

    private int computeCurrentIndex(MotionEvent event) {
        if (itemHeight <= 0) return INDEX_NONE;
        float y = event.getY() - getPaddingTop();
        int index = (int) (y / itemHeight);
        if (index < 0) {
            index = 0;
        } else if (index >= CHARS.length) {
            index = CHARS.length - 1;
        }
        return index;
    }

    private OnCharIndexChangedListener listener;

    public void setOnCharIndexChangedListener(OnCharIndexChangedListener listener) {
        this.listener = listener;
    }

    public interface OnCharIndexChangedListener {

        void onCharIndexChanged(char currentIndex);

        void onCharIndexSelected(String currentIndex);
    }

}