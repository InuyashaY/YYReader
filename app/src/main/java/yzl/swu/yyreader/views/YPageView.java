package yzl.swu.yyreader.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import yzl.swu.yyreader.common.AlikeAnim;
import yzl.swu.yyreader.common.SlideAnim;
import yzl.swu.yyreader.models.BookModel;

public class YPageView extends View implements SlideAnim.OnPageChangeListener {
    /** 属性*/
    //背景颜色
//    private int bgColor = 0xFFCEC29C;
    //内容加载器
    PageLoader mPageLoader;

    //触摸事件位移记录
    int mStartX = 0;
    int mStartY = 0;
    //是否是在移动
    private boolean isMove = false;
    // 唤醒菜单的区域
    private RectF mCenterRect = null;
    //中心点击事件回调
    private OnCenterClickListener listener;
    //是否能触摸
    boolean canTouch = true;
    //翻页方向
    boolean isNext = true;

    AlikeAnim slideAnim;

    public YPageView(Context context) {
        this(context,null);
    }

    public YPageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public YPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    /*******************************初始化方法***************************************/
    private void initData() {
        slideAnim = new AlikeAnim(this, this);
        mPageLoader.initDimens();
        mPageLoader.initData();
//        mPageLoader.drawPage(coverAnimation.getmCurBitmap());
        mPageLoader.drawPage(slideAnim.getmNextBitmap());
    }

    /*******************************自定义绘制方法***************************************/
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initData();
    }

    //自定义绘制
    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(bgColor);


//        mPageLoader.drawPage(coverAnimation.getmNextBitmap());

        slideAnim.drawViewPages(canvas);



    }


    /*******************************Setter and Getter***************************************/
    public PageLoader getPageLoader(BookModel bookModel) {
        if (mPageLoader == null){
//            BookModel bookModel = new BookModel("斗罗大陆",R,"","/storage/emulated/0/斗罗大陆.txt");
            mPageLoader = new LocalPageLoader(this,bookModel);
        }
        return mPageLoader;
    }

    public void setOnCenterClickListener(OnCenterClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (!canTouch && event.getAction() != MotionEvent.ACTION_DOWN) return true;

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                mStartY = y;
                isMove = false;
                //canTouch = mTouchListener.onTouch();
                slideAnim.onTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                // 判断是否大于最小滑动值。
                int slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                if (!isMove) {
                    isMove = Math.abs(mStartX - event.getX()) > 100 || Math.abs(mStartY - event.getY()) > slop;
                    isNext = event.getX()-mStartX<0;
                }
                if (isMove){
                    slideAnim.onTouchEvent(event);
                }

                break;
            case MotionEvent.ACTION_UP:
                if (!isMove) {
                    //设置中间区域范围
                    if (mCenterRect == null) {
                        mCenterRect = new RectF(getWidth() / 5, getHeight() / 3,
                                getWidth() * 4 / 5, getHeight() * 2 / 3);
                    }

                    //是否点击了中间
                    if (mCenterRect.contains(x, y)) {
//                        if (mTouchListener != null) {
//                            mTouchListener.center();
//                        }
                        if (listener != null) listener.centerClicked();
                        return true;
                    }
                }
                //如果滑动了，则进行翻页
                if (isMove) {
//                    if (isNext) mPageLoader.nextPage();
//                    else mPageLoader.prePage();

                    isNext = true;
                    isMove = !isMove;
                }
                slideAnim.onTouchEvent(event);


                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        // 判断Scroller是否执行完毕
        //进行滑动
        Log.v("yzll","computScroll");
        slideAnim.scrollAnim();
        super.computeScroll();
    }

    //判断是否存在上一页
    private boolean hasPrevPage() {
        return mPageLoader.prePage();
    }

    //判断是否下一页存在
    private boolean hasNextPage() {

        return mPageLoader.nextPage();
    }

    //绘制下一页
    public void drawNextPage() {
        slideAnim.changePage();
        mPageLoader.drawPage(slideAnim.getmNextBitmap());
    }

    //绘制当前页
    public void drawCurPage() {
        mPageLoader.drawPage(slideAnim.getmNextBitmap());
    }

    //取消翻页
    public void cancelChangePage(){
        mPageLoader.cancelChangePage();
    }




    /*********************interface implements*************************/
    public interface OnCenterClickListener{
        void centerClicked();
    }


    @Override
    public boolean hasPrev() {
        return this.hasPrevPage();
    }

    @Override
    public boolean hasNext() {
        return this.hasNextPage();
    }

    @Override
    public void pageCancel() {
        cancelChangePage();
    }
}
