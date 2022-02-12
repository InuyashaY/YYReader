package yzl.swu.yyreader.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import yzl.swu.yyreader.R;
import yzl.swu.yyreader.adapter.BaseListAdapter;
import yzl.swu.yyreader.adapter.HotCommentAdapter;
import yzl.swu.yyreader.adapter.RecommandBookListAdapter;
import yzl.swu.yyreader.common.Constants;
import yzl.swu.yyreader.databinding.ActivityBookDetailBinding;
import yzl.swu.yyreader.models.BookComment;
import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.models.StoreBookItemDao;
import yzl.swu.yyreader.models.TxtChapterModel;
import yzl.swu.yyreader.remote.RemoteRepository;
import yzl.swu.yyreader.utils.BookManager;
import yzl.swu.yyreader.utils.MD5Utils;
import yzl.swu.yyreader.utils.StringUtils;
import yzl.swu.yyreader.utils.Utils;

import static yzl.swu.yyreader.common.Constants.READBOOK_KEY;

public class BookDetailActivity extends BaseActivity<ActivityBookDetailBinding> {
    public static final String RESULT_IS_COLLECTED = "result_is_collected";

    private static final String TAG = "BookDetailActivity";
    private static final String EXTRA_BOOK_ID = "extra_book_id";

    private static final int REQUEST_READ = 1;

    /************************************/
    private HotCommentAdapter mHotCommentAdapter;
    private RecommandBookListAdapter mBookListAdapter;
    private BookModel mCollBookBean;
    private ProgressDialog mProgressDialog;
    /*******************************************/
    private String mBookId;
    private boolean isBriefOpen = false;
    private boolean isCollected = false;

    public static void startActivity(Context context, String bookId) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(EXTRA_BOOK_ID, bookId);
        context.startActivity(intent);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null) {
            mBookId = savedInstanceState.getString(EXTRA_BOOK_ID);
        } else {
            mBookId = getIntent().getStringExtra(EXTRA_BOOK_ID);
        }
    }

    @Override
    protected void setUpToolbar() {
        setSupportActionBar(viewBinding.selectorToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        viewBinding.selectorToolbar.setNavigationOnClickListener(
                (v) -> finish()
        );
        getSupportActionBar().setTitle("书籍详情");
    }

    protected void setUpToolbar(Toolbar toolbar) {

    }

    @Override
    protected void initClick() {
        super.initClick();

        //可伸缩的TextView
        viewBinding.bookDetailTvBrief.setOnClickListener(
                (view) -> {
                    if (isBriefOpen) {
                        viewBinding.bookDetailTvBrief.setMaxLines(4);
                        isBriefOpen = false;
                    } else {
                        viewBinding.bookDetailTvBrief.setMaxLines(8);
                        isBriefOpen = true;
                    }
                }
        );

        viewBinding.bookListTvChase.setOnClickListener(
                (V) -> {
                    //点击存储
                    if (isCollected) {
                        //放弃点击
                        BookManager.getInstance()
                                .deleteCollBookInRx(mCollBookBean);

                        viewBinding.bookListTvChase.setText(getResources().getString(R.string.book_detail_chase_update));

                        //修改背景
                        Drawable drawable = getResources().getDrawable(R.drawable.selector_btn_book_list);
                        viewBinding.bookListTvChase.setBackground(drawable);
                        //设置图片
                        viewBinding.bookListTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_add), null,
                                null, null);

                        isCollected = false;
                    } else {
                        addToBookShelf(mCollBookBean);
                        viewBinding.bookListTvChase.setText(getResources().getString(R.string.book_detail_give_up));

                        //修改背景
                        Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
                        viewBinding.bookListTvChase.setBackground(drawable);
                        //设置图片
                        viewBinding.bookListTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete), null,
                                null, null);

                        isCollected = true;
                    }
                }
        );

        viewBinding.bookDetailTvRead.setOnClickListener(
                (v) ->
                        startActivityForResult(new Intent(this, BookReaderActivity.class)
                        .putExtra("collected", isCollected)
                        .putExtra(READBOOK_KEY, mCollBookBean), REQUEST_READ)
        );


    }

    @Override
    protected void processLogic() {
        super.processLogic();
        viewBinding.refreshLayout.showLoading();
        refreshBookDetail(mBookId);
    }

    public void finishRefresh(StoreBookItemDao bean) {
        //封面
        Glide.with(this)
                .load(bean.getPicUrl())
                .placeholder(R.drawable.ic_book_loading)
                .error(R.drawable.ic_load_error)
                .centerCrop()
                .into(viewBinding.bookDetailIvCover);
        //书名
        viewBinding.bookDetailTvTitle.setText(bean.getBookName());
        //作者
        viewBinding.bookDetailTvAuthor.setText(bean.getAuthorName());
        //类型
        viewBinding.bookDetailTvType.setText(bean.getCatName());

        //总字数
        viewBinding.bookDetailTvWordCount.setText(getResources().getString(R.string.book_word, bean.getWordCount() / 10000));
        //更新时间
        viewBinding.bookDetailTvLatelyUpdate.setText(StringUtils.dateConvert(bean.getUpdateTime(), Constants.FORMAT_BOOK_DATE));
        //追书人数
        viewBinding.bookDetailTvFollowerCount.setText(bean.getVisitCount() + "");
        //存留率
        viewBinding.bookDetailTvRetention.setText(bean.getVisitCount() + "%");
        //日更字数
        viewBinding.bookDetailTvDayWordCount.setText(bean.getWordCount() / 10000 + "");
        //简介
        viewBinding.bookDetailTvBrief.setText(bean.getBookDesc());
        //社区
        viewBinding.bookDetailTvCommunity.setText(getResources().getString(R.string.book_detail_community, bean.getBookName()));
        //帖子数
        viewBinding.bookDetailTvPostsCount.setText(getResources().getString(R.string.book_detail_posts_count, bean.getCommentCount()));
        mCollBookBean = BookManager.getInstance().getCollectedBook(String.valueOf(bean.getId()));

        //判断是否收藏
        if (mCollBookBean != null) {
            isCollected = true;

            viewBinding.bookListTvChase.setText(getResources().getString(R.string.book_detail_give_up));
            //修改背景
            Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
            viewBinding.bookListTvChase.setBackground(drawable);
            //设置图片
            viewBinding.bookListTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete), null,
                    null, null);
            viewBinding.bookDetailTvRead.setText("继续阅读");
        } else {
            mCollBookBean = bean.getBookModel();
        }
    }

    public void finishHotComment(List<BookComment> beans) {
        if (beans.isEmpty()) {
            return;
        }
        mHotCommentAdapter = new HotCommentAdapter();
        viewBinding.bookDetailRvHotComment.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //与外部ScrollView滑动冲突
                return false;
            }
        });
        viewBinding.bookDetailRvHotComment.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
        viewBinding.bookDetailRvHotComment.setAdapter(mHotCommentAdapter);
        mHotCommentAdapter.addItems(beans);
    }


    public void finishRecommendBookList(List<StoreBookItemDao> beans) {
        if (beans.isEmpty()) {
            viewBinding.bookDetailRvRecommendBookList.setVisibility(View.GONE);
            return;
        }
        //推荐书单列表
        mBookListAdapter = new RecommandBookListAdapter();
        viewBinding.bookDetailRvRecommendBookList.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //与外部ScrollView滑动冲突
                return false;
            }
        });
        final Context context = this;
        mBookListAdapter.setOnItemClickListener(new BaseListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                //点击查看书籍详情
                String bookId = String.valueOf(mBookListAdapter.getItem(pos).getId());
                BookDetailActivity.startActivity(context,bookId);
            }
        });
        viewBinding.bookDetailRvRecommendBookList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
        viewBinding.bookDetailRvRecommendBookList.setAdapter(mBookListAdapter);
        mBookListAdapter.addItems(beans);
    }

    public void waitToBookShelf() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("正在添加到书架中");
        }
        mProgressDialog.show();
    }


    public void errorToBookShelf() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        Utils.showToast("加入书架失败，请检查网络");
    }


    public void succeedToBookShelf() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        Utils.showToast("加入书架成功");
    }


    public void showError() {
        viewBinding.refreshLayout.showError();
    }


    public void complete() {
        viewBinding.refreshLayout.showFinish();
    }

    /*******************************************************/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_BOOK_ID, mBookId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果进入阅读页面收藏了，页面结束的时候，就需要返回改变收藏按钮
        if (requestCode == REQUEST_READ) {
            if (data == null) {
                return;
            }

            isCollected = data.getBooleanExtra(RESULT_IS_COLLECTED, false);

            if (isCollected) {
                viewBinding.bookListTvChase.setText(getResources().getString(R.string.book_detail_give_up));
                //修改背景
                Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
                viewBinding.bookListTvChase.setBackground(drawable);
                //设置图片
                viewBinding.bookListTvChase.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete), null,
                        null, null);
                viewBinding.bookDetailTvRead.setText("继续阅读");
            }
        }
    }

    /**************************逻辑处理***********************/
    public void refreshBookDetail(String bookId) {
        refreshBook();
        refreshComment();
        refreshRecommend();
    }

    public void addToBookShelf(BookModel collBookBean)  {
        Disposable disposable = RemoteRepository.getInstance()
                .getBookChapters(String.valueOf(collBookBean.getBookId()))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(
                        (d) -> waitToBookShelf() //等待加载
                )
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        beans -> {

                            //设置 id
                            for(TxtChapterModel bean :beans){
                                bean.setChapterId(MD5Utils.strToMd5By16(bean.getLink()));
                            }

                            //设置目录
//                            collBookBean.setBookChapters(beans);
                            //存储收藏
                            BookManager.getInstance()
                                    .saveCollBookWithAsync(collBookBean);

                            succeedToBookShelf();
                        }
                        ,
                        e -> {
                            errorToBookShelf();
                            Log.e(this.getClass().getName(),e.toString());
                        }
                );
        addDisposable(disposable);
    }

    private void refreshBook(){
        RemoteRepository
                .getInstance()
                .getBookDetail(mBookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<StoreBookItemDao>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(StoreBookItemDao value){
                        finishRefresh(value);
                        complete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                    }
                });
    }

    private void refreshComment(){
        Disposable disposable = RemoteRepository
                .getInstance()
                .getBookCommentList(mBookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (value) -> finishHotComment(value)
                );
        addDisposable(disposable);
    }

    private void refreshRecommend(){
        Disposable disposable = RemoteRepository
                .getInstance()
                .getRecommandBookListByCatId(String.valueOf(2))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (value) -> finishRecommendBookList(value)
                );
        addDisposable(disposable);
    }
}
