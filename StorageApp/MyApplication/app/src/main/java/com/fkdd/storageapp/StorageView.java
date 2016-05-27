package com.fkdd.storageapp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


/**
 * Created by Peter on 2016/5/25.
 */
public class StorageView extends View {

    private String namespace = "http://schemas.android.com/apk/res-auto";
    private Paint mPaint;
    private Rect totalBound;
    private Rect leftBound;
    private Rect rightBound;

    //attr
    private int usedRate;

    private String totalText;
    private int totalTextSize;
    private String leftText;
    private int leftTextSize;
    private String rightText;
    private int rightTextSize;

    //左边的padding
    private int st_paddingLeft;
    //右边的padding
    private int st_paddingRight;
    //总空间的方形条和总空间textview的间距
    private int paddingTotalRectWidth;

    //上边的padding
    private int st_paddingTop;
    //下边的padding
    private int st_paddingBottom;
    //总空间的方形条和已使用空间textview的间距
    private int paddingRectUsedHeight;

    //已用空间小矩形和已用空间文字textview的间距
    private int paddingReclUsedWidth;
    //已用空间文字textview和可用空间小矩形的间距
    private int paddingUsedRecrWidth;
    //可用空间小矩形和可用空间文字textview的间距
    private int paddingRecrAvailableWidth;

    public StorageView(Context context) {
        this(context, null);
    }

    public StorageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StorageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        //attrs
        //percent
        usedRate = attrs.getAttributeIntValue(namespace,"usedRate",0);
        //all strings
        int resourceId = attrs.getAttributeResourceValue(namespace, "totalText", -1);
        if (resourceId <= 0)
            totalText = attrs.getAttributeValue(namespace, "totalText")== null ?"":attrs.getAttributeValue(namespace, "totalText");
        else
            totalText = getResources().getString(resourceId);

        resourceId = attrs.getAttributeResourceValue(namespace, "leftText", -1);
        if (resourceId <= 0)
            leftText = attrs.getAttributeValue(namespace, "leftText") == null ?"":attrs.getAttributeValue(namespace, "leftText");
        else
            leftText = getResources().getString(resourceId);

        resourceId = attrs.getAttributeResourceValue(namespace, "rightText", -1);
        if (resourceId <= 0)
            rightText = attrs.getAttributeValue(namespace, "rightText")== null ?"":attrs.getAttributeValue(namespace, "rightText");
        else
            rightText = getResources().getString(resourceId);

        //all text size
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.storageView);
        totalTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, typedArray.getDimension(R.styleable.storageView_totalTextSize, 16), getResources().getDisplayMetrics());
        leftTextSize =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, typedArray.getDimension(R.styleable.storageView_leftTextSize, 16), getResources().getDisplayMetrics());
        rightTextSize =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, typedArray.getDimension(R.styleable.storageView_rightTextSize, 16), getResources().getDisplayMetrics());

        //all padding
        st_paddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getDimension(R.styleable.storageView_st_paddingLeft, 10), getResources().getDisplayMetrics());
        st_paddingRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getDimension(R.styleable.storageView_st_paddingRight, 10), getResources().getDisplayMetrics());
        paddingTotalRectWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getDimension(R.styleable.storageView_paddingTotalRectWidth, 10), getResources().getDisplayMetrics());
        st_paddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getDimension(R.styleable.storageView_st_paddingTop, 10), getResources().getDisplayMetrics());
        st_paddingBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getDimension(R.styleable.storageView_st_paddingBottom, 15), getResources().getDisplayMetrics());
        paddingRectUsedHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getDimension(R.styleable.storageView_paddingRectUsedHeight, 10), getResources().getDisplayMetrics());
        paddingReclUsedWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getDimension(R.styleable.storageView_paddingReclUsedWidth, 10), getResources().getDisplayMetrics());
        paddingUsedRecrWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getDimension(R.styleable.storageView_paddingUsedRecrWidth, 20), getResources().getDisplayMetrics());
        paddingRecrAvailableWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getDimension(R.styleable.storageView_paddingRecrAvailableWidth, 10), getResources().getDisplayMetrics());


        totalBound = new Rect();
        mPaint.setTextSize(totalTextSize);
        mPaint.getTextBounds(totalText, 0, totalText.length(), totalBound);

        leftBound = new Rect();
        mPaint.setTextSize(leftTextSize);
        mPaint.getTextBounds(leftText, 0, leftText.length(), leftBound);

        rightBound = new Rect();
        mPaint.setTextSize(rightTextSize);
        mPaint.getTextBounds(rightText, 0, rightText.length(), rightBound);

        typedArray.recycle();
    }

    public void setRefresh(){
        ValueAnimator animator = ValueAnimator.ofInt(0, usedRate);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                usedRate  = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(1500);
        animator.start();

    }

    public int getUsedRate() {
        return usedRate;
    }

    public void setUsedRate(int usedRate) {
        this.usedRate = usedRate;
    }


    public void setTotalText(String totalText) {
        this.totalText = totalText;
        totalBound = new Rect();
        mPaint.setTextSize(totalTextSize);
        mPaint.getTextBounds(totalText, 0, totalText.length(), totalBound);
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;

        leftBound = new Rect();
        mPaint.setTextSize(leftTextSize);
        mPaint.getTextBounds(leftText, 0, leftText.length(), leftBound);
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
        rightBound = new Rect();
        mPaint.setTextSize(rightTextSize);
        mPaint.getTextBounds(rightText, 0, rightText.length(), rightBound);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //整个背景
        mPaint.setColor(getResources().getColor(R.color.storage_background));
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        //已用空间的小矩形(高度和宽度都为leftBound.height())
        mPaint.setColor(getResources().getColor(R.color.storage_used));
        canvas.drawRect(st_paddingLeft, st_paddingTop, st_paddingLeft +leftBound.height(), st_paddingTop +leftBound.height(),mPaint);

        //左上角的已用空间的文字
        mPaint.setColor(getResources().getColor(R.color.black));
        mPaint.setTextSize(leftTextSize);
        canvas.drawText(leftText, st_paddingLeft + leftBound.height() + paddingReclUsedWidth, leftBound.height()+ st_paddingTop, mPaint);

        //可用空间的小矩形(高度和宽度都为leftBound.height())
        mPaint.setColor(getResources().getColor(R.color.storage_available));
        canvas.drawRect(st_paddingLeft +leftBound.height()+ paddingReclUsedWidth +leftBound.width()+ paddingUsedRecrWidth, st_paddingTop, st_paddingLeft +leftBound.height()+ paddingReclUsedWidth +leftBound.width()+ paddingUsedRecrWidth +leftBound.height(), st_paddingTop +leftBound.height(),mPaint);


        //可用空间的文字
        mPaint.setColor(getResources().getColor(R.color.black));
        mPaint.setTextSize(rightTextSize);
        canvas.drawText(rightText, st_paddingLeft +leftBound.height()+ paddingReclUsedWidth +leftBound.width()+ paddingUsedRecrWidth +leftBound.height()+ paddingRecrAvailableWidth, leftBound.height()+ st_paddingTop, mPaint);


        //已经使用的空间的矩形
        mPaint.setColor(getResources().getColor(R.color.storage_used));
        float divider = st_paddingLeft + ( getMeasuredWidth()  - totalBound.width() - st_paddingLeft - st_paddingRight - paddingTotalRectWidth) / 100f * usedRate;
        int recWid = getMeasuredHeight() - st_paddingTop - leftBound.height() - paddingRectUsedHeight - st_paddingBottom;
        canvas.drawRect(st_paddingLeft, leftBound.height() + st_paddingTop + paddingRectUsedHeight + recWid / 4, divider, getMeasuredHeight()- st_paddingBottom - recWid / 4, mPaint);

        //可用空间的矩形
        mPaint.setColor(getResources().getColor(R.color.storage_available));
        canvas.drawRect(divider, leftBound.height() + st_paddingTop + paddingRectUsedHeight + recWid / 4, getMeasuredWidth()  - totalBound.width() - st_paddingRight - paddingTotalRectWidth, getMeasuredHeight()- st_paddingBottom - recWid / 4, mPaint);

        //总空间的文字
        mPaint.setColor(getResources().getColor(R.color.black));
        mPaint.setTextSize(totalTextSize);
        canvas.drawText(totalText, getWidth()  - totalBound.width() - st_paddingRight, (getHeight() - leftBound.height() - st_paddingTop - paddingRectUsedHeight - st_paddingBottom) / 2 + leftBound.height() + st_paddingTop + paddingRectUsedHeight + totalBound.height() / 2, mPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;

        //the width always fill the screen
        width = widthSize;


        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            float textHeight = totalBound.height() + leftBound.height();
            int desired = (int) (getPaddingTop() + textHeight + st_paddingTop + paddingRectUsedHeight + st_paddingBottom + getPaddingBottom());
            height = desired;
        }


        setMeasuredDimension(width, height);
    }


    }
