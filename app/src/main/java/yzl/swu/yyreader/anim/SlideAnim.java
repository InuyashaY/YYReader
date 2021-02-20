package yzl.swu.yyreader.anim;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import yzl.swu.yyreader.common.Direction;

public class SlideAnim extends PageAnim {

    public SlideAnim(View view, PageAnim.OnPageChangeListener mListener){
        super(view,mListener);
    }


    public void startAnim(){
        int dx = 0;
        switch (mDirection){
            case NEXT:
                if (isCancel){
                    int dis = (int) ((mViewWidth - mStartX) + mTouchX);
                    if (dis > mViewWidth){
                        dis = mViewWidth;
                    }
                    dx = mViewWidth - dis;
                }else{
                    dx = (int) -(mTouchX + (mViewWidth - mStartX));
                }
                break;
            default:
                if (isCancel){
                    dx = (int) -mTouchX;
                }else{
                    dx = (int) (mViewWidth - mTouchX);
                }
                break;
        }

        //滑动速度保持一致
        int duration = (800 * Math.abs(dx)) / mViewWidth;
        mScroller.startScroll((int) mTouchX, 0, dx, 0, duration);
    }



    public Bitmap getmCurBitmap() {
        return mCurBitmap;
    }

    public Bitmap getmNextBitmap() {
        return mNextBitmap;
    }


    public boolean onTouchEvent(MotionEvent event){
        //点击位置
        int x = (int) event.getX();
        int y = (int) event.getY();

        //更新滑动坐标
        mLastX = mTouchX;
        mLastY = mTouchY;
        mTouchX = x;
        mTouchY = y;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                mStartY = y;
                //移动的点击位置
                mLastX = 0;
                mLastY = 0;
                //未开始动画
                isAnim = false;
                //取消
                isCancel = false;

                break;
            case MotionEvent.ACTION_MOVE:
                //判断是否移动了
                if (!isMove) {
                    isMove = Math.abs(mStartX - x) > slop || Math.abs(mStartY - y) > slop;
                }

                //处理滑动
                if (isMove) {
                    //还未开始滑动
                    if (mLastX == mStartX) {
                        //判断方向
                        if (x - mStartX > 0) {
                            mDirection = Direction.PRE;
                            //如果一页不存在
                            if (!mListener.hasPrev()) {
                                noNext = true;
                                return true;
                            }
                        } else {
                            mDirection = Direction.NEXT;
                            //如果一页不存在
                            if (!mListener.hasNext()) {
                                noNext = true;
                                return true;
                            }
                        }


                    }
                    //已经开始滑动
                    else {
                        //判断是否需要取消翻页
                        //即滑动位置到了与初始滑动方向相反时
                        if (mDirection == Direction.NEXT) {
                            if (x - mLastX > 0) {
                                isCancel = true;
                            } else {
                                isCancel = false;
                            }
                        } else {
                            if (x - mLastX < 0) {
                                isCancel = true;
                            } else {
                                isCancel = false;
                            }
                        }

                    }

                    mLastX = x;
                    mLastY = y;
                    isAnim = true;
                    mView.invalidate();

                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isMove) {
                    if (x < mViewWidth / 2) {
                        mDirection = Direction.PRE;
                    } else {
                        mDirection = Direction.NEXT;
                    }

                    if (mDirection == Direction.NEXT) {
                        //判断是否下一页存在
                        boolean hasNext = mListener.hasNext();

                        if (!hasNext) {
                            return true;
                        }
                    } else {
                        boolean hasPrev = mListener.hasPrev();

                        if (!hasPrev) {
                            return true;
                        }
                    }
                }

                // 是否取消翻页
                if (isCancel){
                    mListener.pageCancel();
                }

                    // 开启翻页效果
                if (!noNext) {
                    startAnim();
                    mView.invalidate();
                }
                    break;

        }
        return true;
    }


    public void scrollAnim() {
        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            mLastX = mTouchX;
            mLastY = mTouchY;

            mTouchX = x;
            mTouchY = y;


            if (mScroller.getFinalX() == x && mScroller.getFinalY() == y){
                isAnim = false;
            }
            mView.postInvalidate();
        }
    }


    //绘制翻页动画
    public void drawMove(Canvas canvas) {

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

                break;
            default:
                mSrcRect.left = (int) (mViewWidth - mTouchX);
                mDestRect.right = (int) mTouchX;
                canvas.drawBitmap(mCurBitmap,0,0,null);
                canvas.drawBitmap(mNextBitmap,mSrcRect,mDestRect,null);

                break;
        }
    }






}
