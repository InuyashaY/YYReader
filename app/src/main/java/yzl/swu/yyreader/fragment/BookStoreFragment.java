package yzl.swu.yyreader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.adapter.TabFragmentPageAdapter;
import yzl.swu.yyreader.databinding.BookStoreFragmentBinding;

public class BookStoreFragment extends BaseFragment<BookStoreFragmentBinding> {
    //tab的标题
    List<String> tabTitles;
    //对应的fragment
    List<Fragment> tabFragments;

    @Override
    protected void initWidget() {
        //配置tab
        setUpTabLayout();
    }


    private void setUpTabLayout(){
        //设置标题和fragment
        tabTitles = Arrays.asList("精选","分类","榜单","书单");
        tabFragments = new ArrayList<>();

        tabFragments.add(new StoreFragment_one());
        tabFragments.add(new StoreFragment_two());
        tabFragments.add(new Fragment());
        tabFragments.add(new Fragment());



        //设置适配器 绑定tab和viewPager
        viewBinding.fileSelectViewpager.setAdapter(new TabFragmentPageAdapter(getActivity().getSupportFragmentManager(),0,tabTitles,tabFragments));
        viewBinding.fileSelectViewpager.setOffscreenPageLimit(3);
        viewBinding.tabTlIndicator.setupWithViewPager(viewBinding.fileSelectViewpager);

    }
}
