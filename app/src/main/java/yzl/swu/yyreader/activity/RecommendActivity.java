package yzl.swu.yyreader.activity;

import android.util.Log;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import yzl.swu.yyreader.adapter.StoreRankListAdapter;
import yzl.swu.yyreader.databinding.ActivityRecommendBinding;
import yzl.swu.yyreader.remote.RemoteRepository;

public class RecommendActivity extends BaseActivity<ActivityRecommendBinding> {

    private StoreRankListAdapter mStoreRankAdapter;

    @Override
    protected void initWidget() {
        initRecyclerView();
    }

    @Override
    protected void processLogic() {
        loadData();
    }

    //初始化RecyclerView
    private void initRecyclerView() {
        viewBinding.recommendRv.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.recommendRv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
        mStoreRankAdapter = new StoreRankListAdapter();
        viewBinding.recommendRv.setAdapter(mStoreRankAdapter);
        mStoreRankAdapter.setOnItemClickListener(
                (view, pos) -> {
                    String bookId = String.valueOf(mStoreRankAdapter.getItem(pos).getId());
                    BookDetailActivity.startActivity(this,bookId);
                }
        );
    }

    private void loadData() {
        Disposable remoteDisp = RemoteRepository.getInstance()
                .getRecommendBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (beans)-> {
//                            mView.finishRefresh(beans);
//                            mView.complete();
                            mStoreRankAdapter.refreshItems(beans);
//                            complete();
                        }
                        ,
                        (e) ->{
//                            showError();
                            Log.e("推荐","net error");
                        }
                );
        addDisposable(remoteDisp);
    }
}
