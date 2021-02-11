package yzl.swu.yyreader.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yzl.swu.yyreader.common.Constants;
import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.models.BookRecordModel;
import yzl.swu.yyreader.models.TxtChapterModel;
import yzl.swu.yyreader.models.TxtPageModel;
import yzl.swu.yyreader.utils.StringUtils;
import yzl.swu.yyreader.utils.Utils;

public abstract class PageLoader {
    //显示容器
    YPageView pageView;
    //加载的书籍
    BookModel bookModel;
    // 当前书籍章节列表
    public List<TxtChapterModel> mChapterList;
    // 当前显示的页
    private TxtPageModel mCurPage;
    // 被遮盖的页，或者认为被取消显示的页
    private TxtPageModel mCancelPage;
    // 上一章的页面列表缓存
    private List<TxtPageModel> mPrePageList;
    // 当前章节的页面列表
    private List<TxtPageModel> mCurPageList;
    // 下一章的页面列表缓存
    private List<TxtPageModel> mNextPageList;
    //当前章节位置
    int curChapterIndex = 0;
    //上一章节位置
    int lastChapterIndex = 0;
    //当前页面位置
    int curPageIndex = 0;


    //行间距
    private int mTextInterval;
    //标题的行间距
    private int mTitleInterval;
    //书籍绘制区域的宽高
    private int mVisibleWidth;
    private int mVisibleHeight;
    //应用的宽高
    private int mDisplayWidth;
    private int mDisplayHeight;
    //电池的百分比
    private int mBatteryLevel;
    //间距
    private int mMarginWidth;
    private int mMarginHeight;

    //标题画笔
    Paint mTitleTextPaint;
    //内容画笔
    Paint mContentTextPaint;
    // 绘制电池的画笔
    private Paint mBatteryPaint;
    // 绘制提示的画笔
    private Paint mTipPaint;
    //字体大小 单位dp
    int mTextSize = 18;
    //背景颜色
    int bgColor = 0xFFCEC29C;
    //字体颜色
    int textColor = Color.BLACK;
    //记录上一次颜色 用于恢复
    int lastBgColor;
    int lastTextColor;



    public PageLoader(YPageView pageView, BookModel bookModel){
        this.pageView = pageView;
        this.bookModel = bookModel;

        initPaint();
//        initDimens();
//        initData();
    }

    //初始化数据
    public void initData(){
        try {
            loadChapters();
            if (curChapterIndex > 0) mPrePageList = loadPageList(curChapterIndex-1);
            mCurPageList = loadPageList(curChapterIndex);
            if (curChapterIndex < mChapterList.size()-1) mNextPageList = loadPageList(curChapterIndex+1);
            mCurPage = mCurPageList.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //初始化尺寸
    public void initDimens(){
        mMarginWidth = 40;
        mMarginHeight = 50;
        mVisibleWidth = pageView.getWidth()-mMarginWidth*2;
        mVisibleHeight = pageView.getHeight()-mMarginHeight;
        mTitleInterval = (int) (mTitleTextPaint.getTextSize()/2);
        mTextInterval = (int) (mContentTextPaint.getTextSize()/2);
    }

    //初始化画笔
    private void initPaint(){
        // 绘制页面内容的画笔
        mContentTextPaint = new TextPaint();
        mContentTextPaint.setColor(Color.BLACK);
        mContentTextPaint.setTextSize(dp2px(mTextSize));
        mContentTextPaint.setAntiAlias(true);
        mContentTextPaint.setSubpixelText(true);

        // 绘制标题的画笔
        mTitleTextPaint = new TextPaint();
        mTitleTextPaint.setColor(Color.BLACK);
        mTitleTextPaint.setTextSize(dp2px(mTextSize+5));
        mTitleTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTitleTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTitleTextPaint.setAntiAlias(true);

        mTipPaint = new TextPaint();
        mBatteryPaint = new Paint();
    }

    public void reloadPageList(){
        if (curChapterIndex > 0) mPrePageList = loadPageList(curChapterIndex-1);
        mCurPageList = loadPageList(curChapterIndex);
        if (curChapterIndex < mChapterList.size()-1) mNextPageList = loadPageList(curChapterIndex+1);
        mCurPage = mCurPageList.get(0);

    }


    //加载某章内容
    public List<TxtPageModel> loadPageList(int chapterIndex) {
        TxtChapterModel chapterModel = mChapterList.get(chapterIndex);
        List<TxtPageModel> pageList = null;
        try {
            BufferedReader br = getChapterReader(chapterModel);
            pageList = loadPages(chapterModel,br);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pageList;
    }

    //根据章节模型将数据封装成页
    private List<TxtPageModel> loadPages(TxtChapterModel chapter, BufferedReader br){
        //章节的所有页面
        List<TxtPageModel> pageLists = new ArrayList<>();
        //每页的行
        List<String> lines = new ArrayList<>();
        int readHeight = mVisibleHeight;
        boolean showTitle = true;
        String paragraph = chapter.getTitle();//默认展示标题
        int titleLinesCount = 0;

        //流式读取书籍数据
        try {
            while(showTitle || (paragraph = br.readLine()) != null){
                //一行的字数
                int wordCount = 0;
                //一行的内容
                String lineContent = null;
                while (paragraph.length() > 0){
                    //检查是否能一行的高度
                    if (showTitle){
                        readHeight -= mTitleTextPaint.getTextSize();
                    }else {
                        readHeight -= mContentTextPaint.getTextSize();
                    }

                    // 一页已经填充满了，创建 TextPage
                    if (readHeight <= 0) {
                        // 创建Page
                        TxtPageModel page = new TxtPageModel();
                        page.position = pageLists.size();
                        page.title = chapter.getTitle();
                        page.lines = new ArrayList<>(lines);
                        page.titleLines = titleLinesCount;
                        pageLists.add(page);
                        // 重置Lines
                        lines.clear();
                        readHeight = mVisibleHeight;
                        titleLinesCount = 0;

                        continue;
                    }

                    //一页未填满     测量每行的字数
                    if (showTitle) {
                        wordCount = mTitleTextPaint.breakText(paragraph,
                                true, mVisibleWidth, null);
                    } else {
                        wordCount = mContentTextPaint.breakText(paragraph,
                                true, mVisibleWidth, null);
                    }
                    //截取一行的内容
                    lineContent = paragraph.substring(0,wordCount);
                    if (!lineContent.equals("\n")){
                        //一行不为空则加入数组
                        lines.add(lineContent);
                        //设置段落间距
                        if (showTitle) {
                            titleLinesCount += 1;
                            readHeight -= mTitleInterval;
                            showTitle = false;
                        } else {
                            readHeight -= mTextInterval;
                        }
                    }
                    //裁剪
                    paragraph = paragraph.substring(wordCount);
                }

                //增加段落的间距
//                if (!showTitle && lines.size() != 0) {
//                    readHeight = readHeight - mTextInterval;
//                }

                if (showTitle) {
                    readHeight = readHeight - mTitleInterval;
                    showTitle = false;
                }
            }
            //处理尾页
            if (lines.size() != 0) {
                //创建Page
                TxtPageModel page = new TxtPageModel();
                page.position = pageLists.size();
                page.title = chapter.getTitle();
                page.lines = new ArrayList<>(lines);
                page.titleLines = titleLinesCount;
                pageLists.add(page);
                //重置Lines
                lines.clear();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                br.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return pageLists;
    }


    //绘制页面
    public void drawPage(Bitmap bitmap){
        Canvas canvas = new Canvas(bitmap);
        drawBG(canvas);
        drawContent(canvas);
    }

    //绘制背景
    private void drawBG(Canvas canvas){
        int tipMarginHeight = Utils.dpToPx(pageView.getContext(),3);
        canvas.drawColor(bgColor);
        /******绘制电池********/
        int visibleRight = mDisplayWidth - mMarginWidth;

        int visibleBottom = mDisplayHeight - tipMarginHeight;

        int outFrameWidth = (int) mTipPaint.measureText("xxx");
        int outFrameHeight = (int) mTipPaint.getTextSize();

        int polarHeight = Utils.dpToPx(pageView.getContext(),6);
        int polarWidth = Utils.dpToPx(pageView.getContext(),2);
        int border = 1;
        int innerMargin = 1;

        //电极的制作
        int polarLeft = visibleRight - polarWidth;
        int polarTop = visibleBottom - (outFrameHeight + polarHeight) / 2;
        Rect polar = new Rect(polarLeft, polarTop, visibleRight,
                polarTop + polarHeight - Utils.dpToPx(pageView.getContext(),2));

        mBatteryPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(polar, mBatteryPaint);

        //外框的制作
        int outFrameLeft = polarLeft - outFrameWidth;
        int outFrameTop = visibleBottom - outFrameHeight;
        int outFrameBottom = visibleBottom - Utils.dpToPx(pageView.getContext(),2);
        Rect outFrame = new Rect(outFrameLeft, outFrameTop, polarLeft, outFrameBottom);

        mBatteryPaint.setStyle(Paint.Style.STROKE);
        mBatteryPaint.setStrokeWidth(border);
        canvas.drawRect(outFrame, mBatteryPaint);

        //内框的制作
        float innerWidth = (outFrame.width() - innerMargin * 2 - border) * (mBatteryLevel / 100.0f);
        RectF innerFrame = new RectF(outFrameLeft + border + innerMargin, outFrameTop + border + innerMargin,
                outFrameLeft + border + innerMargin + innerWidth, outFrameBottom - border - innerMargin);

        mBatteryPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(innerFrame, mBatteryPaint);

        /******绘制当前时间********/
        //底部的字显示的位置Y
        float y = mDisplayHeight - mTipPaint.getFontMetrics().bottom - tipMarginHeight;
        String time = StringUtils.dateConvert(System.currentTimeMillis(), Constants.FORMAT_TIME);
        float x = outFrameLeft - mTipPaint.measureText(time) - Utils.dpToPx(pageView.getContext(),4);
        canvas.drawText(time, x, y, mTipPaint);
    }

    //绘制内容
    private void drawContent(Canvas canvas){
        float top=mMarginHeight;
        String str = null;

        //确定距离
        int contentInterval = (int) mContentTextPaint.getTextSize()+mTextInterval;
        int titleInterval = (int) mTitleTextPaint.getTextSize()+mTitleInterval;

        //对标题进行绘制
        for (int i = 0; i < mCurPage.titleLines; ++i) {
            str = mCurPage.lines.get(i);

            //设置顶部间距
            if (i == 0) {
                top += 10;
            }

            //计算文字显示的起始点
            int start = (int) (mVisibleWidth - mTitleTextPaint.measureText(str)) / 2;
            //进行绘制
            canvas.drawText(str, start, top, mTitleTextPaint);
//            canvas.drawText(str, 50, 100, mTitleTextPaint);

            //设置尾部间距
            if (i == mCurPage.titleLines - 1) {
                top += titleInterval;
            } else {
                top += titleInterval;
            }
        }


        //对内容进行绘制
        for (int i = mCurPage.titleLines; i < mCurPage.lines.size(); ++i) {
            str = mCurPage.lines.get(i);

            canvas.drawText(str, mMarginWidth, top, mContentTextPaint);
//            canvas.drawText(str, 10, 300, mContentTextPaint);
            if (str.endsWith("\n")) {
                top += contentInterval+10;
            } else {
                //行间距
                top += contentInterval;
            }
        }
    }

    //上一页
    public boolean prePage(){

        if (curPageIndex > 0){
            curPageIndex--;
        }else{
            if (curChapterIndex == 0) return false;
            lastChapterIndex = curChapterIndex--;
            mNextPageList = mCurPageList;
            mCurPageList = mPrePageList;
            if (curChapterIndex > 0) mPrePageList = loadPageList(curChapterIndex-1);
            curPageIndex = mCurPageList.size()-1;
        }
        mCancelPage = mCurPage;
        mCurPage = mCurPageList.get(curPageIndex);
        pageView.drawNextPage();
        return true;
    }

    //下一页
    public boolean nextPage(){
        if (curPageIndex >= 0 && curPageIndex < mCurPageList.size()-1){
            curPageIndex++;
        }else{
            if (curChapterIndex >= mChapterList.size()-1) return false;
            lastChapterIndex = curChapterIndex++;
            mPrePageList = mCurPageList;
            mCurPageList = mNextPageList;
            if (curChapterIndex < mChapterList.size()-1) mNextPageList = loadPageList(curChapterIndex+1);
            curPageIndex = 0;
        }
        mCancelPage = mCurPage;
        mCurPage = mCurPageList.get(curPageIndex);
        pageView.drawNextPage();
        return true;
    }

    //取消翻页
    public void cancelChangePage(){
        // 加载到下一章取消了
        if (mCurPage.position == 0 && curChapterIndex > lastChapterIndex) {
            if (mPrePageList != null) {
                cancelNextChapter();
            } else {
                mCurPage = new TxtPageModel();
            }
        }
        // 加载上一章取消了
        else if (mCurPageList == null
                || (mCurPage.position == mCurPageList.size() - 1
                && curChapterIndex < lastChapterIndex)) {

            if (mNextPageList != null) {
                cancelPreChapter();
            } else {
                mCurPage = new TxtPageModel();
            }
        } else {
            // 假设加载到下一页，又取消了。那么需要重新装载。
            mCurPage = mCancelPage;
            curPageIndex = mCurPage.position;
        }
    }

    //翻到下一章 取消加载
    private void cancelNextChapter() {
        int temp = lastChapterIndex;
        lastChapterIndex = curChapterIndex;
        curChapterIndex = temp;

        mNextPageList = mCurPageList;
        mCurPageList = mPrePageList;
        mPrePageList = null;
        if (curChapterIndex > 0) mPrePageList = loadPageList(curChapterIndex-1);

        curPageIndex = mCurPageList.size()-1;

        mCurPage = mCurPageList.get(curPageIndex);
        mCancelPage = null;
    }

    //翻到上一章 取消加载
    private void cancelPreChapter() {
        // 重置位置点
        int temp = lastChapterIndex;
        lastChapterIndex = curChapterIndex;
        curChapterIndex = temp;
        // 重置页面列表
        mPrePageList = mCurPageList;
        mCurPageList = mNextPageList;
        mNextPageList = null;

        if (curChapterIndex < mChapterList.size()-1) mNextPageList = loadPageList(curChapterIndex+1);
        curPageIndex = 0;

        mCurPage = mCurPageList.get(curPageIndex);
        mCancelPage = null;
    }


    //下一章
    public void nextChapter(){

        if (curChapterIndex < mChapterList.size()-1) skipToChapter(++curChapterIndex);
    }

    //上一章
    public void preChapter(){

        if (curChapterIndex > 0) skipToChapter(--curChapterIndex);
    }

    //跳转到某一章
    public void skipToChapter(int pos){
        curChapterIndex = pos;
        if (pos > 0) mPrePageList = loadPageList(pos-1);
        mCurPageList = loadPageList(pos);
        if (pos < mChapterList.size()-1) mNextPageList = loadPageList(pos+1);
        curPageIndex = 0;
        updateCurPage();
    }

    //设置翻页模式
    public void setPageMode(){

    }

    //设置背景颜色
    public void setPageBgColor(int color){
        lastBgColor = this.bgColor;
        this.bgColor = color;
        updateCurPage();
    }

    //设置阅读样式
    public void setPageStyle(int bgColor,int textColor){
        lastBgColor = this.bgColor;
        lastTextColor = this.textColor;
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.mTitleTextPaint.setColor(textColor);
        this.mContentTextPaint.setColor(textColor);
        updateCurPage();
    }

    //恢复上次颜色
    public void cancelColorChange(){
        this.bgColor = lastBgColor;
        this.textColor = lastTextColor;
        this.mTitleTextPaint.setColor(textColor);
        this.mContentTextPaint.setColor(textColor);
        updateCurPage();
    }

    //设置字体大小
    public void setTextSize(int textSize){
        mTextSize = textSize;
        initPaint();
        initDimens();
        reloadPageList();
        updateCurPage();
    }

    private void updateCurPage(){
        mCurPage = mCurPageList.get(curPageIndex);
        pageView.drawNextPage();
        pageView.postInvalidate();
    }

    public int getCurChapterIndex() {
        return curChapterIndex;
    }

    public int getCurPageIndex() {
        return curPageIndex;
    }

    public void setCurChapterIndex(int curChapterIndex) {
        this.curChapterIndex = curChapterIndex;
    }

    public void setCurPageIndex(int curPageIndex) {
        this.curPageIndex = curPageIndex;
    }

    public int getBgColor(){return bgColor;}


    /********************************abstract***********************************/
    public abstract BufferedReader getChapterReader(TxtChapterModel chapterModel) throws FileNotFoundException, IOException;

    public abstract void loadChapters() throws IOException;

    private float dp2px(float dp){
        return pageView.getContext().getResources().getDisplayMetrics().density*dp;
    }

    /**************************interface*******************************/
    interface OnChapterChangeListener{
        void onChapterChange(int pos);
    }
}
