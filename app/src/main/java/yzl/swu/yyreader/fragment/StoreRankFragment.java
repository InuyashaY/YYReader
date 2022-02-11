package yzl.swu.yyreader.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import yzl.swu.yyreader.activity.BookDetailActivity;
import yzl.swu.yyreader.adapter.StoreRankListAdapter;
import yzl.swu.yyreader.databinding.FragmentRefreshListBinding;
import yzl.swu.yyreader.remote.RemoteRepository;

public class StoreRankFragment extends BaseFragment<FragmentRefreshListBinding> {
    private static final String EXTRA_BILL_ID = "extra_bill_id";
    private StoreRankListAdapter mStoreRankAdapter;
    private String mRankType;
    protected CompositeDisposable mDisposable;

    public static Fragment newInstance(String billId){
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_BILL_ID,billId);
        Fragment fragment = new StoreRankFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initWidget() {
        mRankType = getArguments().getString(EXTRA_BILL_ID);
        super.initWidget();
        setUpAdapter();
    }

    protected void processLogic() {
        super.processLogic();
        viewBinding.refreshLayout.showLoading();
        refreshBookBrief(mRankType);
    }

    private void setUpAdapter(){
        viewBinding.refreshRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        viewBinding.refreshRvContent.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));
        mStoreRankAdapter = new StoreRankListAdapter();
        viewBinding.refreshRvContent.setAdapter(mStoreRankAdapter);
        mStoreRankAdapter.setOnItemClickListener(
                (view, pos) -> {
                    String bookId = String.valueOf(mStoreRankAdapter.getItem(pos).getId());
                    BookDetailActivity.startActivity(getContext(),bookId);
                }
        );
    }


    public void showError() {
        viewBinding.refreshLayout.showError();
    }


    public void complete() {
        viewBinding.refreshLayout.showFinish();
    }

    //网络请求
    public void refreshBookBrief(String type) {
        Disposable remoteDisp = RemoteRepository.getInstance()
                .getRankBookList(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (beans)-> {
//                            mView.finishRefresh(beans);
//                            mView.complete();
                            mStoreRankAdapter.refreshItems(beans);
                            complete();
                        }
                        ,
                        (e) ->{
                            showError();
                            Log.e("StoreRank","net error");
                        }
                );
        addDisposable(remoteDisp);
    }

    void addDisposable(Disposable subscription) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(subscription);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (mDisposable != null){
            mDisposable.clear();
        }
    }


}
