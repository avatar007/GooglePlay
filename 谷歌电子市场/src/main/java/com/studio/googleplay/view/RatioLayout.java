package com.studio.googleplay.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.studio.googleplay.R;

/**
 * Created by Administrator on 2017/4/4.
 */
public class RatioLayout extends FrameLayout {

    private float mRatioSize;

    public RatioLayout(Context context) {
        super(context);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //float ratioSize = attrs.getAttributeFloatValue("", "ratioSize", -1);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        mRatioSize = typedArray.getFloat(R.styleable.RatioLayout_ratioSize, -1);
        typedArray.recycle();
    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && mRatioSize > 0 && heightMode != MeasureSpec.EXACTLY) {
            int imageWidth = widthSize + getPaddingLeft() + getPaddingRight();
            int imageHeight = (int) ((imageWidth / mRatioSize) - 0.5f);
            heightSize = imageHeight + getPaddingTop() + getPaddingBottom();
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
