package yzl.swu.yyreader.utils;

import org.litepal.LitePal;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import yzl.swu.yyreader.common.Constants;
import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.models.TxtChapterModel;

public class BookManager{
    private static final String TAG = "BookManager";
    private String chapterName;
    private String bookId;
    private long chapterLen;
    private long position;
    private Map<String, Cache> cacheMap = new HashMap<>();
    private static volatile BookManager sInstance;

    public static BookManager getInstance(){
        if (sInstance == null){
            synchronized (BookManager.class){
                if (sInstance == null){
                    sInstance = new BookManager();
                }
            }
        }
        return sInstance;
    }

    public boolean openChapter(String bookId,String chapterName){
        return openChapter(bookId,chapterName,0);
    }

    public boolean openChapter(String bookId,String chapterName,long position){
        //如果文件不存在，则打开失败
        File file = new File(Constants.BOOK_CACHE_PATH + bookId
                + File.separator + chapterName + FileUtils.SUFFIX_NB);
        if (!file.exists()){
            return false;
        }
        this.bookId = bookId;
        this.chapterName = chapterName;
        this.position = position;
        createCache();
        return true;
    }

    private void createCache(){
        //创建Cache
        if (!cacheMap.containsKey(chapterName)){
            Cache cache = new Cache();
            File file = getBookFile(bookId, chapterName);
            //TODO:数据加载默认utf-8(以后会增加判断),FileUtils采用Reader获取数据的，可能用byte会更好一点
            char[] array = FileUtils.getFileContent(file).toCharArray();
            WeakReference<char[]> charReference = new WeakReference<char[]>(array);
            cache.size = array.length;
            cache.data = charReference;
            cacheMap.put(chapterName, cache);

            chapterLen = cache.size;
        }
        else {
            chapterLen = cacheMap.get(chapterName).getSize();
        }
    }

    public void setPosition(long position){
        this.position = position;
    }

    public long getPosition(){
        return position;
    }

    //获取上一段
    public String getPrevPara(){
        //首先判断是否Position已经达到起始位置，已经越界
        if (position < 0){
            return null;
        }

        //初始化从后向前获取的起始点,终止点,文本
        int end = (int)position;
        int begin = end;
        char[] array = getContent();

        while (begin >= 0) { //判断指针是否达到章节的起始位置
            char character = array[begin]; //获取当前指针下的字符

            //判断当前字符是否为换行，如果为换行，就代表获取到了一个段落，并退出。
            //有可能发生初始指针指的就是换行符的情况。
            if ((character+"").equals("\n") && begin != end) {
                position = begin;
                //当当前指针指向换行符的时候向后退一步
                begin++;
                break;
            }
            //向前进一步
            begin--;
        }
        //最后end获取到段落的起始点，begin是段落的终止点。

        //当越界的时候，保证begin在章节内
        if (begin < 0){
            begin = 0;//在章节内
            position = -1; //越界
        }
        int size = end+1 - begin;
        return new String(array,begin,size);
    }

    //获取下一段
    public String getNextPara(){
        //首先判断是否Position已经达到终点位置
        if (position >= chapterLen){
            return null;
        }

        //初始化起始点，终止点。
        int begin = (int)position;
        int end = begin;
        char[] array = getContent();

        while (end < chapterLen) { //判断指针是否在章节的末尾位置
            char character = array[end]; //获取当前指针下的字符
            //判断当前字符是否为换行，如果为换行，就代表获取到了一个段落，并退出。
            //有可能发生初始指针指的就是换行符的情况。
            //这里当遇到\n的时候，不需要回退
            if ((character+"").equals("\n") && begin != end){
                ++end;//指向下一字段
                position = end;
                break;
            }
            //指向下一字段
            end++;
        }
        //所要获取的字段的长度
        int size = end - begin;
        return new String(array,begin,size);
    }

    //获取章节的内容
    public char[] getContent() {
        if (cacheMap.size() == 0){
            return new char[1];
        }
        char[] block = cacheMap.get(chapterName).getData().get();
        if (block == null) {
            File file = getBookFile(bookId, chapterName);
            block = FileUtils.getFileContent(file).toCharArray();
            Cache cache = cacheMap.get(chapterName);
            cache.data = new WeakReference<char[]>(block);
        }
        return block;
    }

    public long getChapterLen(){
        return chapterLen;
    }

    public void clear(){
        cacheMap.clear();
        position = 0;
        chapterLen = 0;
    }

    /**
     * 创建或获取存储文件
     * @param folderName
     * @param fileName
     * @return
     */
    public static File getBookFile(String folderName, String fileName){
        return FileUtils.getFile(Constants.BOOK_CACHE_PATH + folderName
                + File.separator + fileName + FileUtils.SUFFIX_NB);
    }

    public static long getBookSize(String folderName){
        return FileUtils.getDirSize(FileUtils
                .getFolder(Constants.BOOK_CACHE_PATH + folderName));
    }

    /**
     * 根据文件名判断是否被缓存过 (因为可能数据库显示被缓存过，但是文件中却没有的情况，所以需要根据文件判断是否被缓存
     * 过)
     * @param folderName : bookId
     * @param fileName: chapterName
     * @return
     */
    public static boolean isChapterCached(String folderName, String fileName){
        File file = new File(Constants.BOOK_CACHE_PATH + folderName
                + File.separator + fileName + FileUtils.SUFFIX_NB);
        return file.exists();
    }

    public class Cache {
        private long size;
        private WeakReference<char[]> data;

        public WeakReference<char[]> getData() {
            return data;
        }

        public void setData(WeakReference<char[]> data) {
            this.data = data;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }

    public Single<Void> deleteCollBookInRx(BookModel bean) {
        return Single.create(new SingleOnSubscribe<Void>() {
            @Override
            public void subscribe(SingleEmitter<Void> e) throws Exception {
                //查看文本中是否存在删除的数据
                LitePal.delete(BookModel.class,bean.getId());
                //删除任务
//                deleteDownloadTask(bean.get_id());
                //删除目录
                LitePal.delete(TxtChapterModel.class,bean.getId());
                //删除CollBook
            }
        });
    }

    public BookModel getCollectedBook(String bookId){
        return LitePal.where("id=?",bookId).findFirst(BookModel.class);
    }

    //存储已收藏书籍
    public void saveCollBookWithAsync(BookModel bean){
        //启动异步存储
        boolean resuly = bean.save();
//        mSession.startAsyncSession()
//                .runInTx(
//                        () -> {
//                            if (bean.getBookChapters() != null){
//                                // 存储BookChapterBean
//                                mSession.getBookChapterBeanDao()
//                                        .insertOrReplaceInTx(bean.getBookChapters());
//                            }
//                            //存储CollBook (确保先后顺序，否则出错)
//                            mCollBookDao.insertOrReplace(bean);
//                        }
//                );
    }
}
