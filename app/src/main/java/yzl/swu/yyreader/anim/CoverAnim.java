package yzl.swu.yyreader.anim;

import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import yzl.swu.yyreader.common.Direction;

public class CoverAnim extends SlideAnim {
    private GradientDrawable mBackShadowDrawableLR;
    public CoverAnim(View view, OnPageChangeListener mListener) {
        super(view, mListener);
        int[] mBackShadowColors = new int[] { 0x66000000,0x00000000};
        mBackShadowDrawableLR = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
        mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }

    //添加阴影
    public void addShadow(int left,Canvas canvas) {
        mBackShadowDrawableLR.setBounds(left, 0, left + 30 , mViewHeight);
        mBackShadowDrawableLR.draw(canvas);
    }

    @Override
    public void drawMove(Canvas canvas) {
        if (!isAnim) return;
        switch (mDirection){
            case NEXT:
                int dis = (int) (mViewWidth - mStartX + mTouchX);
                if (dis > mViewWidth){
                    dis = mViewWidth;
                }
                //计算bitmap截取的区域
                mSrcRect.left = mViewWidth - dis;
                //计算bitmap在canvas显示的区域
                mDestRect.right = dis;
                canvas.drawBitmap(mNextBitmap,0,0,null);
                canvas.drawBitmap(mCurBitmap,mSrcRect,mDestRect,null);
                addShadow(dis,canvas);
                break;
            default:
                mSrcRect.left = (int) (mViewWidth - mTouchX);
                mDestRect.right = (int) mTouchX;
                canvas.drawBitmap(mCurBitmap,0,0,null);
                canvas.drawBitmap(mNextBitmap,mSrcRect,mDestRect,null);
                addShadow((int) mTouchX,canvas);
                break;
        }
    }
}
