package yzl.swu.yyreader.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

public class CoverAnimation {
    //绘制的页面
    Bitmap mCurBitmap;
    Bitmap mNextBitmap;
    //滑动器
    public Scroller mScroller;
    //需要动画的视图
    View mView;

    //视图的尺寸
    protected int mViewWidth;
    protected int mViewHeight;
    //起始点
    protected float mStartX;
    protected float mStartY;
    //触碰点
    protected float mTouchX;
    protected float mTouchY;
    //上一个触碰点
    protected float mLastX;
    protected float mLastY;

    //是否在移动
    private boolean isMove;
    //最短滑动距离
    int slop;
    //滑动方向
    Direction mDirection;
    //是否取消翻页
    boolean isCancel;
    //页面位移矩阵
    private Rect mSrcRect, mDestRect;
    //是否开始动画
    boolean isAnim = false;
    //监听者
    private OnPageChangeListener mListener;
    //是否没下一页或者上一页
    private boolean noNext = false;

    public CoverAnimation(View view,OnPageChangeListener mListener){
        mView = view;
        mScroller = new Scroller(mView.getContext());
        this.mListener = mListener;
        initData();
    }

    private void initData(){
        mViewWidth = mView.getWidth();
        mViewHeight = mView.getHeight();
        mCurBitmap = Bitmap.createBitmap(mView.getWidth(),mView.getHeight(),Bitmap.Config.RGB_565);
        mNextBitmap = Bitmap.createBitmap(mView.getWidth(),mView.getHeight(),Bitmap.Config.RGB_565);
        mSrcRect = new Rect(0, 0, mViewWidth, mViewHeight);
        mDestRect = new Rect(0, 0, mViewWidth, mViewHeight);
        slop = ViewConfiguration.get(mView.getContext()).getScaledTouchSlop();
    }

    public void startAnim(){
//        TranslateAnimation translateAnimation = new TranslateAnimation(0,200,0,200);
//        translateAnimation.setDuration(1000);
//        mView.startAnimation(translateAnimation);
        //mView.scrollBy(100,100);
        if (isAnim){
            return;
        }
        isAnim = true;
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
        int duration = (400 * Math.abs(dx)) / mViewWidth;
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
                //方向默认
                mDirection = Direction.NEXT;
                break;
            case MotionEvent.ACTION_MOVE:
                //判断是否移动了
                if (!isMove) {
                    isMove = Math.abs(mStartX - x) > slop || Math.abs(mStartY - y) > slop;
                }

                //处理滑动
                if (isMove) {
                    //还未开始滑动
                    if (mLastX == mStartX && mLastY == mStartY) {
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
                //addShadow(dis,canvas);
                break;
            default:
                mSrcRect.left = (int) (mViewWidth - mTouchX);
                mDestRect.right = (int) mTouchX;
                canvas.drawBitmap(mCurBitmap,0,0,null);
                canvas.drawBitmap(mNextBitmap,mSrcRect,mDestRect,null);
                //addShadow((int) mTouchX,canvas);
                break;
        }
    }


    //画页面
    public void drawViewPages(Canvas canvas){
        if (isCancel){
            mNextBitmap = mCurBitmap.copy(Bitmap.Config.RGB_565, true);
            canvas.drawBitmap(mCurBitmap, 0, 0, null);
        }else {
            canvas.drawBitmap(mNextBitmap, 0, 0, null);
        }
    }


    public void changePage(){
        Bitmap bitmap = mCurBitmap;
        mCurBitmap = mNextBitmap;
        mNextBitmap = bitmap;
    }


    //监听界面变化    PageView实现
    public interface OnPageChangeListener {
        boolean hasPrev();
        boolean hasNext();
        void pageCancel();
    }

}
