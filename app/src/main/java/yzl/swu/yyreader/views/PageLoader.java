package yzl.swu.yyreader.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.models.TxtChapterModel;
import yzl.swu.yyreader.models.TxtPageModel;

public abstract class PageLoader {
    //显示容器
    YPageView pageView;
    //加载的书籍
    BookModel bookModel;
    // 当前书籍章节列表
    public List<TxtChapterModel> mChapterList;
    // 当前显示的页
    private TxtPageModel mCurPage;
    // 上一章的页面列表缓存
    private List<TxtPageModel> mPrePageList;
    // 当前章节的页面列表
    private List<TxtPageModel> mCurPageList;
    // 下一章的页面列表缓存
    private List<TxtPageModel> mNextPageList;
    //当前章节位置
    int curChapterIndex = 0;
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
    //间距
    private int mMarginWidth;
    private int mMarginHeight;

    //标题画笔
    Paint mTitleTextPaint;
    //内容画笔
    Paint mContentTextPaint;
    //字体大小 单位dp
    int mTextSize = 18;



    public PageLoader(YPageView pageView, BookModel bookModel){
        this.pageView = pageView;
        this.bookModel = bookModel;

        initPaint();
        initDimens();
        initData();
    }

    private void initData(){
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

    private void initDimens(){
        mMarginWidth = 40;
        mMarginHeight = 50;
        mVisibleWidth = pageView.getWidth()-mMarginWidth*2;
        mVisibleHeight = pageView.getHeight()-mMarginHeight;
        mTitleInterval = (int) (mTitleTextPaint.getTextSize()/2);
        mTextInterval = (int) (mContentTextPaint.getTextSize()/2);
    }

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
    public void drawPage(Canvas canvas){
        drawBG();
        drawContent(canvas);
    }

    //绘制背景
    private void drawBG(){

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
    public void prePage(){

        if (curPageIndex > 0){
            curPageIndex--;
        }else{
            curChapterIndex--;
            mNextPageList = mCurPageList;
            mCurPageList = mNextPageList;
            if (curChapterIndex > 0) mPrePageList = loadPageList(--curChapterIndex);;
            curPageIndex = 0;
        }
        mCurPage = mCurPageList.get(curPageIndex);
        pageView.invalidate();
    }

    //下一页
    public void nextPage(){

        if (curPageIndex >= 0 && curPageIndex < mCurPageList.size()-1){
            curPageIndex++;
        }else{
            curChapterIndex++;
            mPrePageList = mCurPageList;
            mCurPageList = mNextPageList;
            if (curChapterIndex < mChapterList.size()-1) mNextPageList = loadPageList(curChapterIndex+1);
            curPageIndex = 0;
        }
        mCurPage = mCurPageList.get(curPageIndex);
        pageView.invalidate();
    }


    //下一章
    public void nextChapter(){
        curPageIndex = 0;
        if (curChapterIndex < mChapterList.size()-1) skipToChapter(++curChapterIndex);
    }

    //上一章
    public void preChapter(){
        curPageIndex = 0;
        if (curChapterIndex < mChapterList.size()-1) skipToChapter(--curChapterIndex);
    }

    //跳转到某一页
    public void skipToChapter(int pos){
        curChapterIndex = pos;
        if (pos > 0) mPrePageList = loadPageList(pos-1);
        mCurPageList = loadPageList(pos);
        if (pos < mChapterList.size()-1) mNextPageList = loadPageList(pos+1);
        curPageIndex = 0;
        updateCurPage();
        pageView.invalidate();
    }

    //设置翻页模式
    public void setPageMode(){

    }

    //设置背景颜色
    public void setPageBgColor(int color){
        pageView.setBgColor(color);
    }

    //设置字体大小
    public void setTextSize(int textSize){
        mTextSize = textSize;
        initPaint();
        initDimens();
        reloadPageList();
        pageView.invalidate();
    }

    private void updateCurPage(){
        mCurPage = mCurPageList.get(curPageIndex);
    }


    /********************************abstract***********************************/
    public abstract BufferedReader getChapterReader(TxtChapterModel chapterModel) throws FileNotFoundException, IOException;

    public abstract void loadChapters() throws IOException;

    private float dp2px(float dp){
        return pageView.getContext().getResources().getDisplayMetrics().density*dp;
    }
}
