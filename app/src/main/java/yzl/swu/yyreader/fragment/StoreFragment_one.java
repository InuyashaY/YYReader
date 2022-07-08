package yzl.swu.yyreader.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;


import androidx.recyclerview.widget.GridLayoutManager;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIListener;
import com.hedgehog.ratingbar.RatingBar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import yzl.swu.yyreader.R;
import yzl.swu.yyreader.activity.BookDetailActivity;
import yzl.swu.yyreader.activity.RecommendActivity;
import yzl.swu.yyreader.activity.StoreRankActivity;
import yzl.swu.yyreader.adapter.StoreGroupBooksAdapter;
import yzl.swu.yyreader.databinding.FragmentStore1Binding;
import yzl.swu.yyreader.models.BookRankModel;
import yzl.swu.yyreader.models.StoreBookItemDao;
import yzl.swu.yyreader.models.StoreGroupBookModel;
import yzl.swu.yyreader.remote.RemoteRepository;

public class StoreFragment_one extends BaseFragment<FragmentStore1Binding> {
    //热门连载
    private List<BookRankModel> hotRefreshBookList;
    private StoreGroupBooksAdapter mHotAdapter;

    //高分神作
    private List<BookRankModel> highCommentBookList;
    private StoreGroupBooksAdapter mHighCommentAdapter;



    @Override
    protected void initWidget() {
        setupBanner();
        setupGroupBooks();
        initEvents();
    }

    private void initEvents() {
        viewBinding.l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //排行榜
                StoreRankActivity.show(getContext());
            }
        });



        viewBinding.l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分类

            }
        });

        viewBinding.l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //推荐
                startActivity(new Intent(getContext(), RecommendActivity.class));
            }
        });

        MaterialSearchView searchView = getActivity().findViewById(R.id.searchView);
        viewBinding.l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //搜索
                if (searchView != null) searchView.showSearch();
            }
        });



    }

    //配置banner
    public void setupBanner(){
        //banner图片资源
        List<Integer> images = Arrays.asList(R.drawable.banner1,R.drawable.banner2,R.drawable.banner3);

        //绑定资源
        viewBinding.adBanner.setAdapter(new BannerImageAdapter<Integer>(images) {
            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                holder.imageView.setImageResource(data);
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            }
        })
                //添加生命周期观察者
                .addBannerLifecycleObserver(this)
                .setIndicator(new CircleIndicator(getContext()));
    }


    //配置一类书
    public void setupGroupBooks(){
        //加载数据
        initBooksData();

        //网格布局
        viewBinding.booksRv.setLayoutManager(new GridLayoutManager(this.getContext(),3));
        //适配器
        mHotAdapter = new StoreGroupBooksAdapter(hotRefreshBookList, this.getContext(), new StoreGroupBooksAdapter.OnBookClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                //点击回调
                String bookId = String.valueOf(mHotAdapter.getItem(pos).getId());
                BookDetailActivity.startActivity(getContext(),bookId);
            }
        });
        viewBinding.booksRv.setAdapter(mHotAdapter);


        //网格布局
        viewBinding.booksRv2.setLayoutManager(new GridLayoutManager(this.getContext(),3));
        //适配器
        mHighCommentAdapter = new StoreGroupBooksAdapter(highCommentBookList, this.getContext(), new StoreGroupBooksAdapter.OnBookClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                //点击回调
                //点击回调
                String bookId = String.valueOf(mHotAdapter.getItem(pos).getId());
                BookDetailActivity.startActivity(getContext(),bookId);
            }
        });
        viewBinding.booksRv2.setAdapter(mHighCommentAdapter);

    }

    private void initBooksData(){
        hotRefreshBookList = new ArrayList<>();
        highCommentBookList = new ArrayList<>();
        //加载
        refreshBookBookList();

    }

    //网络请求
    public void refreshBookBookList() {
        Disposable remoteDisp = RemoteRepository.getInstance()
                .getListNewRank()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (beans)-> {
//                            mView.finishRefresh(beans);
//                            mView.complete();
                            int len = beans.size() >= 6 ? 6 : beans.size();
                            for(int i=0; i<len; i++) {
                                hotRefreshBookList.add(beans.get(i));
                            }
                            mHotAdapter.refreshModels(hotRefreshBookList);
                        }
                        ,
                        (e) ->{
                            Log.e("storeGroup","net error" + e.toString());
                        }
                );
        addDisposable(remoteDisp);

        Disposable remoteDisp2 = RemoteRepository.getInstance()
                .getListClickRank()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (beans)-> {
//                            mView.finishRefresh(beans);
//                            mView.complete();
                            int len = beans.size() >= 6 ? 6 : beans.size();
                            for(int i=0; i<len; i++) {
                                highCommentBookList.add(beans.get(i));
                            }
                            mHighCommentAdapter.refreshModels(highCommentBookList);
                        }
                        ,
                        (e) ->{
                            Log.e("storeGroup","net error");
                        }
                );
        addDisposable(remoteDisp2);
    }
}
