package yzl.swu.yyreader.views;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.models.TxtChapterModel;
import yzl.swu.yyreader.utils.FileManager;

public class LocalPageLoader extends PageLoader {
    //默认从文件中获取数据的长度
    private final static int BUFFER_SIZE = 512 * 1024;
    //没有标题的时候，每个章节的最大长度
    private final static int MAX_LENGTH_WITH_NO_CHAPTER = 10 * 1024;
    //打开的书籍文件
    File bookFile;
    //书籍模式
    Pattern mChapterPattern;
    //正则表达式章节匹配模式
    // "(第)([0-9零一二两三四五六七八九十百千万壹贰叁肆伍陆柒捌玖拾佰仟]{1,10})([章节回集卷])(.*)"
    private static final String[] CHAPTER_PATTERNS = new String[]{"^(.{0,8})(\u7b2c)([0-9\u96f6\u4e00\u4e8c\u4e24\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341\u767e\u5343\u4e07\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe\u4f70\u4edf]{1,10})([\u7ae0\u8282\u56de\u96c6\u5377])(.{0,30})$",
            "^(\\s{0,4})([\\(\u3010\u300a]?(\u5377)?)([0-9\u96f6\u4e00\u4e8c\u4e24\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341\u767e\u5343\u4e07\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe\u4f70\u4edf]{1,10})([\\.:\uff1a\u0020\f\t])(.{0,30})$",
            "^(\\s{0,4})([\\(\uff08\u3010\u300a])(.{0,30})([\\)\uff09\u3011\u300b])(\\s{0,2})$",
            "^(\\s{0,4})(\u6b63\u6587)(.{0,20})$",
            "^(.{0,4})(Chapter|chapter)(\\s{0,4})([0-9]{1,4})(.{0,30})$"};


    public LocalPageLoader(YPageView pageView, BookModel bookModel) {
        super(pageView, bookModel);
    }

    @Override
    public BufferedReader getChapterReader(TxtChapterModel chapterModel) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FileManager.getInstance().getFileByFilePath("斗罗大陆.txt")),"utf-8"));
        byte[] chapterContent = getChapterContent(chapterModel);
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(chapterContent)));

        return br;
    }


    static final byte BLANK = 0x0a;
    //分章
    @Override
    public void loadChapters() throws IOException {

        //先获取本地文件
        bookFile = FileManager.getInstance().getFileByFilePath(bookModel.getFilePath());

        if (!mChapterList.isEmpty()){
            //回调加载目录完毕
            chapterChangeListener.finishedLoadChapters(mChapterList);
            return;
        }

        //如果没有 就根据文件进行分章
        List<TxtChapterModel> chapters = new ArrayList<>();
        //获取文件流
        RandomAccessFile bookStream = new RandomAccessFile(bookFile, "r");
        //查看文件内是否存在分章
        boolean hasChapters = checkChapterExist(bookStream);
        //根据块加载章节
        byte[] blockBuffer = new byte[BUFFER_SIZE];
        //获取到的块起始点，在文件中的位置
        long curOffset = 0;
        //block的个数
        int blockCount = 0;
        //读取长度
        int length;

        //从文件读取到buffer上，直到没有数据为止
        while ((length = bookStream.read(blockBuffer,0,blockBuffer.length)) > 0){
            blockCount++;
            //存在章节
            if (hasChapters){
                //将缓存块的数据转换为String
                String blockContent = new String(blockBuffer,0,blockBuffer.length);
                //搜索指针
                int seekIndex = 0;
                //使用正则匹配，对章节信息进行匹配
                Matcher chapterMatcher = mChapterPattern.matcher(blockContent);
                //块中匹配到章节信息
                while(chapterMatcher.find()){
                    //获取匹配到的标题在字符串中的起始位置
                    int chapterStart = chapterMatcher.start();

                    //当章节标题前还有内容时，两种情况
                    //第一种情况：前部分为序言
                    //第二种情况：前部分为上一章未加载完的内容
                    if (seekIndex == 0 && chapterStart != 0){
                        //获取章节信息前的内容
                        String lastContent = blockContent.substring(seekIndex,chapterStart);
                        //记录搜索指针
                        seekIndex += lastContent.length();

                        //如果当前对整个文件的偏移位置为0的话，那么就是序章(或者说前言等)
                        if (curOffset == 0) {
                            //创建序章
                            TxtChapterModel forwardChapter = new TxtChapterModel("序章");
                            forwardChapter.start = 0;
                            forwardChapter.end = lastContent.getBytes("utf-8").length; //获取String的byte值,作为最终值

                            //如果序章大小大于30才添加进去
                            if (forwardChapter.end - forwardChapter.start > 30) {
                                chapters.add(forwardChapter);
                            }

                            //创建新章节，加入数组
                            TxtChapterModel curChapter = new TxtChapterModel(bookModel.getId());
                            curChapter.title = chapterMatcher.group();
                            curChapter.start = forwardChapter.end;
                            chapters.add(curChapter);
                        }
                        //为前一章为加载完的内容
                        else {
                            //获取上一章，将为完成的添加上去，即将model的end填加上这部分的长度
                            TxtChapterModel lastChapter = chapters.get(chapters.size()-1);
                            lastChapter.end += lastContent.getBytes("utf-8").length;

                            //创建新章节，加入数组
                            TxtChapterModel curChapter = new TxtChapterModel(bookModel.getId());
                            curChapter.title = chapterMatcher.group();
                            curChapter.start = lastChapter.end;
                            chapters.add(curChapter);
                        }
                    }
                    //章节标题前无内容
                    else {
                        //是否已创建章节
                        if (chapters.size() != 0){
                            //获取章节内容
                            String chapterContent = blockContent.substring(seekIndex,chapterStart);
                            seekIndex += chapterContent.length();

                            //获取已创建的章节
                            TxtChapterModel lastChapter = chapters.get(chapters.size()-1);
                            lastChapter.end = lastChapter.start+chapterContent.getBytes("utf-8").length;

                            //创建新章节，加入数组
                            TxtChapterModel curChapter = new TxtChapterModel(bookModel.getId());
                            curChapter.title = chapterMatcher.group();
                            curChapter.start = lastChapter.end;
                            chapters.add(curChapter);
                        }else {     //没有已创建的章节
                            //创建新章节，加入数组
                            TxtChapterModel curChapter = new TxtChapterModel(bookModel.getId());
                            curChapter.title = chapterMatcher.group();
                            curChapter.start = 0;
                            chapters.add(curChapter);
                        }
                    }
                }

            }

            //若文件中不存在章节信息，则以固定长度为一章划分
            else {
                //章节在buffer的偏移量
                int chapterOffset = 0;
                //当前剩余可分配的长度
                int strLength = length;
                //分章的位置
                int chapterPos = 0;

                while (strLength > 0) {
                    ++chapterPos;
                    //是否长度超过一章
                    if (strLength > MAX_LENGTH_WITH_NO_CHAPTER) {
                        //在buffer中一章的终止点
                        int end = length;
                        //寻找换行符作为终止点
                        for (int i = chapterOffset + MAX_LENGTH_WITH_NO_CHAPTER; i < length; ++i) {
                            if (blockBuffer[i] == BLANK) {
                                end = i;
                                break;
                            }
                        }
                        TxtChapterModel chapter = new TxtChapterModel(bookModel.getId());
                        chapter.title = "第" + blockCount + "章" + "(" + chapterPos + ")";
                        chapter.start = curOffset + chapterOffset + 1;
                        chapter.end = curOffset + end;
                        chapters.add(chapter);
                        //减去已经被分配的长度
                        strLength = strLength - (end - chapterOffset);
                        //设置偏移的位置
                        chapterOffset = end;
                    } else {
                        TxtChapterModel chapter = new TxtChapterModel(bookModel.getId());
                        chapter.title = "第" + blockCount + "章" + "(" + chapterPos + ")";
                        chapter.start = curOffset + chapterOffset + 1;
                        chapter.end = curOffset + length;
                        chapters.add(chapter);
                        strLength = 0;
                    }
                }
            }

            //一块数据处理完，移动block的指针（即偏移）
            curOffset += length;

            //设置上一章结尾为此block的结尾
            if (hasChapters && chapters.size()!=0){
                TxtChapterModel lastChapter = chapters.get(chapters.size() - 1);
                lastChapter.end = curOffset;
            }

            //当block过多时，建议JVM执行gc处理垃圾，防止过多占用内存
            if (blockCount % 15 == 0){
                System.gc();
                System.runFinalization();
            }
        }

        if (chapters.size() == 0){
            TxtChapterModel chapterModel = new TxtChapterModel(bookModel.getId());
            chapterModel.title = "抱歉加载失败";
            chapterModel.start = 0 ;
            chapterModel.end = 0;
            chapters.add(chapterModel);
        }

        mChapterList = chapters;
        //关闭文件流
        bookStream.close();
        //建议GC
        System.gc();
        System.runFinalization();
        //回调加载目录完毕
        chapterChangeListener.finishedLoadChapters(mChapterList);
    }


    /**
     * 从文件中提取一章的内容
     *
     * @param chapter
     * @return
     */
    private byte[] getChapterContent(TxtChapterModel chapter) {
        RandomAccessFile bookStream = null;
        try {
            if (bookFile == null){
                bookFile = FileManager.getInstance().getFileByFilePath(bookModel.getFilePath());
            }
            bookStream = new RandomAccessFile(bookFile, "r");
            bookStream.seek(chapter.start);
            int extent = (int) (chapter.end - chapter.start);
            byte[] content = new byte[extent];
            bookStream.read(content, 0, extent);
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bookStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return new byte[0];
    }



    //检验文件是否存在章节划分
    private boolean checkChapterExist(RandomAccessFile bookStream) throws IOException {
        //首先获取128k的数据
        byte[] buffer = new byte[BUFFER_SIZE / 4];
        int length = bookStream.read(buffer, 0, buffer.length);
        //进行章节匹配
        for (String str : CHAPTER_PATTERNS) {
            Pattern pattern = Pattern.compile(str, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(new String(buffer, 0, length, "utf-8"));
            //如果匹配存在，那么就表示当前章节使用这种匹配方式
            if (matcher.find()) {
                mChapterPattern = pattern;
                //重置指针位置
                bookStream.seek(0);
                return true;
            }
        }

        //重置指针位置
        bookStream.seek(0);
        return false;
    }
}
