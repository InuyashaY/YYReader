package yzl.swu.yyreader.views;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import yzl.swu.yyreader.common.Constants;
import yzl.swu.yyreader.models.TxtChapterModel;
import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.remote.RemoteRepository;
import yzl.swu.yyreader.models.ChapterInfoBean;
import yzl.swu.yyreader.utils.BookManager;
import yzl.swu.yyreader.utils.FileUtils;
import yzl.swu.yyreader.utils.RxUtils;

public class NetworkPageLoader extends PageLoader {
    private Subscription mChapterSub;
    public NetworkPageLoader(YPageView pageView, BookModel bookModel) {
        super(pageView, bookModel);
    }

    @Override
    public void initData() {
        loadCurrentChapter();
//        super.initData();
    }

    @Override
    public BufferedReader getChapterReader(TxtChapterModel chapterModel) throws FileNotFoundException, IOException {
        File file = new File(Constants.BOOK_CACHE_PATH + String.valueOf(bookModel.getBookId())
                + File.separator + chapterModel.title + FileUtils.SUFFIX_NB);
        if (!file.exists()) return null;


        Reader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        return br;
    }

    //加载章节列表
    @Override
    public void loadChapters(){
        //已缓存
        if (mChapterList != null && !mChapterList.isEmpty()) return;
        //请求网络小说目录
        Disposable disposable = RemoteRepository.getInstance()
                .getBookChapters(String.valueOf(bookModel.getBookId()))
                .doOnSuccess(new Consumer<List<TxtChapterModel>>() {
                    @Override
                    public void accept(List<TxtChapterModel> bookChapters) throws Exception {
                        //进行设定BookChapter所属的书的id。
                        System.out.println();
//                        for (TxtChapterModel bookChapter : bookChapters) {
//                            bookChapter.setId(MD5Utils.strToMd5By16(bookChapter.getLink()));
//                            bookChapter.setBook_id(bookModel.getId());
//                        }
                    }
                })
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        beans -> {
                            //异步请求完成
                            //显示
                            mChapterList = beans;
//                            mView.showCategory(beans);
                            chapterChangeListener.finishedLoadChapters(mChapterList);
                        }
                        ,
                        e -> {
                            //TODO: Haven't grate conversation method.
                            Log.e("TAG",e.toString());
                        }
                );
//        addDisposable(disposable);

    }

    @Override
    public void preChapter() {
        loadPrevChapter();
        super.preChapter();
    }

    @Override
    public void nextChapter() {
        loadNextChapter();
        super.nextChapter();
    }

    @Override
    public void skipToChapter(int pos) {
        loadCurrentChapter();
        super.skipToChapter(pos);
    }

    /**
     * 加载当前页的前面两个章节
     */
    private void loadPrevChapter() {
        if (chapterChangeListener != null) {
            int end = curChapterIndex;
            int begin = end - 2;
            if (begin < 0) {
                begin = 0;
            }

            requestChapters(begin, end);
        }
    }

    /**
     * 加载前一页，当前页，后一页。
     */
    private void loadCurrentChapter() {
        if (chapterChangeListener != null) {
            int begin = curChapterIndex;
            int end = curChapterIndex;

            // 是否当前不是最后一章
            if (end < mChapterList.size()) {
                end = end + 1;
                if (end >= mChapterList.size()) {
                    end = mChapterList.size() - 1;
                }
            }

            // 如果当前不是第一章
            if (begin != 0) {
                begin = begin - 1;
                if (begin < 0) {
                    begin = 0;
                }
            }

            requestChapters(begin, end);
        }
    }

    /**
     * 加载当前页的后两个章节
     */
    private void loadNextChapter() {
        if (chapterChangeListener != null) {

            // 提示加载后两章
            int begin = curChapterIndex + 1;
            int end = begin + 1;

            // 判断是否大于最后一章
            if (begin >= mChapterList.size()) {
                // 如果下一章超出目录了，就没有必要加载了
                return;
            }

            if (end > mChapterList.size()) {
                end = mChapterList.size() - 1;
            }

            requestChapters(begin, end);
        }
    }

    private void requestChapters(int start, int end) {
        // 检验输入值
        if (start < 0) {
            start = 0;
        }

        if (end >= mChapterList.size()) {
            end = mChapterList.size() - 1;
        }


        List<TxtChapterModel> chapters = new ArrayList<>();

        // 过滤，哪些数据已经加载了
        for (int i = start; i <= end; ++i) {
            TxtChapterModel txtChapter = mChapterList.get(i);
            if (!hasChapterData(txtChapter)) {
                chapters.add(txtChapter);
            }
        }

        if (!chapters.isEmpty()) {
            chapterChangeListener.requestChapters(chapters);
            loadChapter(String.valueOf(bookModel.getBookId()),chapters);
        }else {
            pageView.showContent();
        }
    }

    private boolean hasChapterData(TxtChapterModel chapter) {
        return BookManager.isChapterCached(String.valueOf(bookModel.getBookId()), chapter.title);
    }

    //加载章节具体内容
    private void loadChapter(String bookId, List<TxtChapterModel> bookChapters) {
        int size = bookChapters.size();

        //取消上次的任务，防止多次加载
        if (mChapterSub != null) {
            mChapterSub.cancel();
        }

        List<Single<ChapterInfoBean>> chapterInfos = new ArrayList<>(bookChapters.size());
        ArrayDeque<String> titles = new ArrayDeque<>(bookChapters.size());

        // 将要下载章节，转换成网络请求。
        for (int i = 0; i < size; ++i) {
            TxtChapterModel bookChapter = bookChapters.get(i);
            // 网络中获取数据
            Single<ChapterInfoBean> chapterInfoSingle = RemoteRepository.getInstance()
                    .getChapterInfo(String.valueOf(bookId),bookChapter.getChapterId());

            chapterInfos.add(chapterInfoSingle);

            titles.add(bookChapter.getTitle());
        }

        Single.concat(chapterInfos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<ChapterInfoBean>() {
                            String title = titles.poll();

                            @Override
                            public void onSubscribe(Subscription s) {
                                s.request(Integer.MAX_VALUE);
                                mChapterSub = s;
                            }

                            @Override
                            public void onNext(ChapterInfoBean chapterInfoBean) {
                                //存储数据
                                saveChapterInfo(
                                        bookId, title, chapterInfoBean.getBody()
                                );
//                                mView.finishChapter();
                                pageView.showContent();
                                //将获取到的数据进行存储
                                title = titles.poll();
                            }

                            @Override
                            public void onError(Throwable e) {
                                //只有第一个加载失败才会调用errorChapter
                                if (bookChapters.get(0).getTitle().equals(title)) {
                                    /**
                                     * 加载失败
                                     * */
//                                    mView.errorChapter();
                                }
                                Log.e("NetWorkPageLoader",e.toString());
                            }

                            @Override
                            public void onComplete() {
                            }
                        }
                );
    }

    /**
     * 存储章节
     * @param folderName
     * @param fileName
     * @param content
     */
    public void saveChapterInfo(String folderName,String fileName,String content){
        File file = BookManager.getBookFile(folderName, fileName);
        //获取流并存储
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            close(writer);
        }
    }

    //关闭数据流
    private void close(Closeable closeable){
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
            //close error
        }
    }

}
