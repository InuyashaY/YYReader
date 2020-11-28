package yzl.swu.yyreader.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.IOException;

import yzl.swu.yyreader.models.BookModel;

public class YPageView extends View {
    /** 属性*/
    //背景颜色
    int bgColor = 0xFFCEC29C;
    //内容加载器
    PageLoader pageLoader;


    public YPageView(Context context) {
        this(context,null);
    }

    public YPageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public YPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    /*******************************初始化方法***************************************/
    private void initData() {

    }

    /*******************************自定义绘制方法***************************************/
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    //自定义绘制
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(bgColor);
        getPageLoader().drawPage(canvas);
    }


    /*******************************Setter and Getter***************************************/
    public PageLoader getPageLoader() {
        if (pageLoader == null){
            BookModel bookModel = new BookModel("斗罗大陆",0,"");
            pageLoader = new LocalPageLoader(this,bookModel);
        }
        return pageLoader;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            try {
                getPageLoader().nextPage();
                invalidate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
