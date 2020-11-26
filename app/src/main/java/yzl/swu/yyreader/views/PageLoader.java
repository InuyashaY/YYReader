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
    protected List<TxtChapterModel> mChapterList;
    // 当前显示的页
    private TxtPageModel mCurPage;
    // 上一章的页面列表缓存
    private List<TxtPageModel> mPrePageList;
    // 当前章节的页面列表
    private List<TxtPageModel> mCurPageList;
    // 下一章的页面列表缓存
    private List<TxtPageModel> mNextPageList;

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
    Paint titleTextPaint;
    //内容画笔
    Paint contentTextPaint;

    public PageLoader(YPageView pageView, BookModel bookModel){
        this.pageView = pageView;
        this.bookModel = bookModel;

        initData();
        initPaint();
        initDimens();

    }

    private void initData(){

    }

    private void initDimens(){
        mMarginWidth = 20;
        mMarginHeight = 50;
        mVisibleWidth = pageView.getWidth()-mMarginWidth*2;
        mVisibleHeight = pageView.getHeight()-mMarginHeight;
        mTitleInterval = (int) (titleTextPaint.getTextSize()/2);
        mTextInterval = (int) (contentTextPaint.getTextSize()/2);
    }

    private void initPaint(){
        // 绘制页面内容的画笔
        contentTextPaint = new TextPaint();
        contentTextPaint.setColor(Color.BLACK);
        contentTextPaint.setTextSize(dp2px(17));
        contentTextPaint.setAntiAlias(true);
        contentTextPaint.setSubpixelText(true);

        // 绘制标题的画笔
        titleTextPaint = new TextPaint();
        titleTextPaint.setColor(Color.BLACK);
        titleTextPaint.setTextSize(dp2px(22));
        titleTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        titleTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        titleTextPaint.setAntiAlias(true);

    }


    //加载某章内容
    public List<TxtPageModel> loadPageList(int chapterIndex) throws IOException {
        TxtChapterModel chapterModel = new TxtChapterModel("第一章");
        BufferedReader br = getChapterReader(chapterModel);
        List<TxtPageModel> pageList = loadPages(chapterModel,br);


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
                        readHeight -= titleTextPaint.getTextSize();
                    }else {
                        readHeight -= contentTextPaint.getTextSize();
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
                        wordCount = titleTextPaint.breakText(paragraph,
                                true, mVisibleWidth, null);
                    } else {
                        wordCount = contentTextPaint.breakText(paragraph,
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

                if (pageLists.size() > 30)
                    break;
            }
            //处理尾页
            if (lines.size() != 0) {
                //创建Page
                // 创建Page
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

            /**TTTTTTTTTTTTTTTTesst*/
            mCurPageList = pageLists;
            mCurPage = mCurPageList.get(0);
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
        int contentInterval = (int) contentTextPaint.getTextSize()+mTextInterval;
        int titleInterval = (int) titleTextPaint.getTextSize()+mTitleInterval;

        //对标题进行绘制
        for (int i = 0; i < mCurPage.titleLines; ++i) {
            str = mCurPage.lines.get(i);

            //设置顶部间距
            if (i == 0) {
                top += 10;
            }

            //计算文字显示的起始点
            int start = (int) (mVisibleWidth - titleTextPaint.measureText(str)) / 2;
            //进行绘制
            canvas.drawText(str, start, top, titleTextPaint);
//            canvas.drawText(str, 50, 100, titleTextPaint);

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

            canvas.drawText(str, mMarginWidth, top, contentTextPaint);
//            canvas.drawText(str, 10, 300, contentTextPaint);
            if (str.endsWith("\n")) {
                top += contentInterval+10;
            } else {
                //行间距
                top += contentInterval;
            }
        }
    }


    public abstract BufferedReader getChapterReader(TxtChapterModel chapterModel) throws FileNotFoundException, IOException;

    private float dp2px(float dp){
        return pageView.getContext().getResources().getDisplayMetrics().density*dp;
    }
}
