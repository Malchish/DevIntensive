package com.softdesign.devintensive.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.softdesign.devintensive.R;

/**
 * Created by alena on 15.07.16.
 */
public class AspectRatioImageView extends ImageView{
    private static final float DEFAULT_ASPECT_RATIO = 1.73f;
    private final float mAspectRatio;

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView);
        mAspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspect_ratio, DEFAULT_ASPECT_RATIO);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidth;
        int newHight;

        newWidth = getMeasuredWidth();
        newHight = (int)(newWidth/mAspectRatio);

        setMeasuredDimension(newWidth, newHight);
    }
}
