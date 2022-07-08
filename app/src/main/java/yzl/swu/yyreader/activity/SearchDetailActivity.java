package yzl.swu.yyreader.activity;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import yzl.swu.yyreader.adapter.SpaceItemDecoration;
import yzl.swu.yyreader.adapter.StoreBookListAdapter;
import yzl.swu.yyreader.adapter.StoreRankListAdapter;
import yzl.swu.yyreader.databinding.ActivitySearchDetailBinding;
import yzl.swu.yyreader.models.BookCategory;
import yzl.swu.yyreader.models.BookRankModel;
import yzl.swu.yyreader.models.StoreBookItemDao;
import yzl.swu.yyreader.remote.RemoteRepository;
import yzl.swu.yyreader.utils.RxUtils;

public class SearchDetailActivity extends BaseActivity<ActivitySearchDetailBinding>  {
    StoreRankListAdapter mStoreBookRecyclerAdapter;
    @Override
    protected void initWidget() {
        initRecyclerView();
    }

    @Override
    protected void processLogic() {
        String keyWord = getIntent().getStringExtra("keyWord");
        refreshBookBrief(keyWord);
    }

    //初始化RecyclerView
    private void initRecyclerView() {
        viewBinding.bookListRv.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.bookListRv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
        mStoreBookRecyclerAdapter = new StoreRankListAdapter();
        viewBinding.bookListRv.setAdapter(mStoreBookRecyclerAdapter);
        mStoreBookRecyclerAdapter.setOnItemClickListener(
                (view, pos) -> {
                    String bookId = String.valueOf(mStoreBookRecyclerAdapter.getItem(pos).getId());
                    BookDetailActivity.startActivity(SearchDetailActivity.this,bookId);
                }
        );

    }

    //网络请求
    public void refreshBookBrief(String keyWord) {
        Disposable remoteDisp = RemoteRepository.getInstance()
                .searchByPage(keyWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (beans)-> {
//                            mView.finishRefresh(beans);
//                            mView.complete();
                            List<BookRankModel> list = new ArrayList<>();
                            for (StoreBookItemDao dao : beans) {
                                BookRankModel model = new BookRankModel();
                                model.setId(dao.getId());
                                model.setBookName(dao.getBookName());
                                model.setPicUrl(dao.getPicUrl());
                                model.setBookDesc(dao.getBookDesc());
                                model.setAuthorName(dao.getAuthorName());
                                model.setCatName(dao.getCatName());
                                model.setVisitCount(dao.getVisitCount());
                                model.setScore(dao.getScore());
                                list.add(model);
                            }
                            mStoreBookRecyclerAdapter.refreshItems(list);

                        }
                        ,
                        (e) ->{

                            Log.e("StoreRank","net error");
                        }
                );
        addDisposable(remoteDisp);
    }


}
