package yzl.swu.yyreader.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.adapter.StoreGroupBooksAdapter;
import yzl.swu.yyreader.databinding.FragmentStore1Binding;
import yzl.swu.yyreader.models.StoreGroupBookModel;

public class StoreFragment_one extends BaseFragment<FragmentStore1Binding> {
    //显示的书籍列表
    private List<StoreGroupBookModel> models;


    @Override
    protected void initWidget() {
        setupBanner();
        setupGroupBooks();
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
        initBooksData();
        //网格布局
        viewBinding.booksRv.setLayoutManager(new GridLayoutManager(this.getContext(),3));
        //适配器
        StoreGroupBooksAdapter adapter = new StoreGroupBooksAdapter(models, this.getContext(), new StoreGroupBooksAdapter.OnBookClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                //点击回调
            }
        });
        viewBinding.booksRv.setAdapter(adapter);


        //网格布局
        viewBinding.booksRv2.setLayoutManager(new GridLayoutManager(this.getContext(),3));
        //适配器
        StoreGroupBooksAdapter adapter2 = new StoreGroupBooksAdapter(models, this.getContext(), new StoreGroupBooksAdapter.OnBookClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                //点击回调
            }
        });
        viewBinding.booksRv2.setAdapter(adapter);

    }

    private void initBooksData(){
        models = new ArrayList<>();

        StoreGroupBookModel model1 = new StoreGroupBookModel(R.drawable.tgsw,"太古神王","净无痕");
        StoreGroupBookModel model2 = new StoreGroupBookModel(R.drawable.dldl,"斗罗大陆","唐家三少");
        StoreGroupBookModel model3 = new StoreGroupBookModel(R.drawable.dzz,"大主宰","天蚕土豆");
        StoreGroupBookModel model4 = new StoreGroupBookModel(R.drawable.jl,"捡漏","净无痕");
        StoreGroupBookModel model5 = new StoreGroupBookModel(R.drawable.jswh,"绝世武魂","天逆");
        StoreGroupBookModel model6 = new StoreGroupBookModel(R.drawable.dpcq,"斗破苍穹","天蚕土豆");

        models.add(model1);
        models.add(model2);
        models.add(model3);
        models.add(model4);
        models.add(model5);
        models.add(model6);

    }

}
