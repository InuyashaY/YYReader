package yzl.swu.yyreader.fragment;

import android.widget.ImageView;


import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.databinding.FragmentStore1Binding;

public class StoreFragment_one extends BaseFragment<FragmentStore1Binding> {
    @Override
    protected void initWidget() {
        setupBanner();
    }

    //配置banner
    public void setupBanner(){
        List<ImageView> images = new ArrayList<>();
        ImageView imageView1 = new ImageView(getContext());
        imageView1.setImageResource(R.drawable.banner1);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageView imageView2 = new ImageView(getContext());
        imageView2.setImageDrawable(getActivity().getDrawable(R.drawable.banner2));
        ImageView imageView3 = new ImageView(getContext());
        imageView3.setImageDrawable(getActivity().getDrawable(R.drawable.banner3));
        images.add(imageView1);
        images.add(imageView2);
        images.add(imageView3);

        viewBinding.adBanner.setAdapter(new BannerImageAdapter<ImageView>(images) {
            @Override
            public void onBindView(BannerImageHolder holder, ImageView data, int position, int size) {
                holder.imageView = data;

            }
        })
                //添加生命周期观察者
                .addBannerLifecycleObserver(this)
                .setIndicator(new CircleIndicator(getContext()));
    }


}
