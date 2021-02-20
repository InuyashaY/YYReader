package yzl.swu.yyreader.anim;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import yzl.swu.yyreader.common.Direction;

public class NoneAnim extends PageAnim {
    public NoneAnim(View view, OnPageChangeListener mListener) {
        super(view, mListener);
    }

    @Override
    public void startAnim() {

    }

    @Override
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

    @Override
    public void scrollAnim() {

    }

    @Override
    public void drawMove(Canvas canvas) {
        if (isCancel){
            canvas.drawBitmap(mCurBitmap, 0, 0, null);
        }else {
            canvas.drawBitmap(mNextBitmap, 0, 0, null);
        }
    }
}
