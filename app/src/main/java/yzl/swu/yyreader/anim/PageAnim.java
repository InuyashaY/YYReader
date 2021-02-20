package yzl.swu.yyreader.anim;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import yzl.swu.yyreader.common.Direction;

public abstract class PageAnim {
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
    protected boolean isMove;
    //最短滑动距离
    int slop;
    //滑动方向
    Direction mDirection = Direction.NONE;
    //是否取消翻页
    boolean isCancel;
    //页面位移矩阵
    protected Rect mSrcRect, mDestRect;
    //是否开始动画
    boolean isAnim = false;
    //监听者
    protected OnPageChangeListener mListener;
    //是否没下一页或者上一页
    protected boolean noNext = false;

    public PageAnim(View view, OnPageChangeListener mListener){
        mView = view;
        mScroller = new Scroller(mView.getContext());
        this.mListener = mListener;
        initData();
    }

    /***********************************public***********************************/
    //初始化数据
    protected void initData(){
        mViewWidth = mView.getWidth();
        mViewHeight = mView.getHeight();
        mCurBitmap = Bitmap.createBitmap(mView.getWidth(),mView.getHeight(),Bitmap.Config.RGB_565);
        mNextBitmap = Bitmap.createBitmap(mView.getWidth(),mView.getHeight(),Bitmap.Config.RGB_565);
        mSrcRect = new Rect(0, 0, mViewWidth, mViewHeight);
        mDestRect = new Rect(0, 0, mViewWidth, mViewHeight);
        slop = ViewConfiguration.get(mView.getContext()).getScaledTouchSlop();
    }


    //画页面
    public void drawViewPages(Canvas canvas){
        if (isAnim){
            drawMove(canvas);
        }else {
            if (isCancel){
                mNextBitmap = mCurBitmap.copy(Bitmap.Config.RGB_565, true);
                canvas.drawBitmap(mCurBitmap, 0, 0, null);
            }else {
                canvas.drawBitmap(mNextBitmap, 0, 0, null);
            }
        }

    }


    public void changePage(){
        Bitmap bitmap = mCurBitmap;
        mCurBitmap = mNextBitmap;
        mNextBitmap = bitmap;
    }




    /************************************abstract****************************************/

    //开启翻页动画
    public abstract void startAnim();

    public Bitmap getmCurBitmap() {
        return mCurBitmap;
    }

    //获取下页的bitmap
    public Bitmap getmNextBitmap() {
        return mNextBitmap;
    }

    public void setmCurBitmap(Bitmap mCurBitmap) {
        this.mCurBitmap = mCurBitmap;
    }

    public void setmNextBitmap(Bitmap mNextBitmap) {
        this.mNextBitmap = mNextBitmap;
    }

    //处理触摸事件
    public abstract boolean onTouchEvent(MotionEvent event);

    //计算滚动
    public abstract void scrollAnim();

    //绘制翻页动画
    public abstract void drawMove(Canvas canvas);



    /************************************interface****************************************/


    //监听界面变化    PageView实现
    public interface OnPageChangeListener {
        boolean hasPrev();
        boolean hasNext();
        void pageCancel();
    }
}
