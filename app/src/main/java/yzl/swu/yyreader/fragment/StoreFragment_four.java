package yzl.swu.yyreader.fragment;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import yzl.swu.yyreader.activity.BookDetailActivity;
import yzl.swu.yyreader.adapter.SpaceItemDecoration;
import yzl.swu.yyreader.adapter.StoreBookListAdapter;
import yzl.swu.yyreader.adapter.StoreRankListAdapter;
import yzl.swu.yyreader.databinding.FragmentStore4Binding;
import yzl.swu.yyreader.remote.RemoteRepository;

public class StoreFragment_four extends BaseFragment<FragmentStore4Binding> {
    private StoreRankListAdapter mStoreRankAdapter;


    @Override
    protected void initWidget() {
        initRecyclerView();
    }

    //初始化RecyclerView
    private void initRecyclerView() {
        viewBinding.recommendRv.setLayoutManager(new LinearLayoutManager(getContext()));
        viewBinding.recommendRv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));
        mStoreRankAdapter = new StoreRankListAdapter();
        viewBinding.recommendRv.setAdapter(mStoreRankAdapter);
        mStoreRankAdapter.setOnItemClickListener(
                (view, pos) -> {
                    String bookId = String.valueOf(mStoreRankAdapter.getItem(pos).getId());
                    BookDetailActivity.startActivity(getContext(),bookId);
                }
        );
    }

    @Override
    protected void processLogic() {

        loadData();
    }

    private void loadData() {
        Disposable remoteDisp = RemoteRepository.getInstance()
                .getRankBookList("1")
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
                            Log.e("定制","net error");
                        }
                );
        addDisposable(remoteDisp);
    }

}
