package yzl.swu.yyreader.fragment;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yzl.swu.yyreader.adapter.TabFragmentPageAdapter;
import yzl.swu.yyreader.databinding.FragmentStore3Binding;

public class StoreFragment_three extends BaseFragment<FragmentStore3Binding> {
    //tab的标题
    List<String> tabTitles;
    //对应的fragment
    List<Fragment> tabFragments;

    @Override
    protected void initWidget() {
        super.initWidget();
        setupTabs();
    }

    private void setupTabs() {
        //设置标题和fragment
        tabTitles = Arrays.asList("点击榜","新书榜","更新榜","评论榜");
        tabFragments = new ArrayList<>();
        for (byte i=0; i<4; i++){
            tabFragments.add(StoreRankFragment.newInstance(String.valueOf(i)));
        }

        //设置适配器 绑定tab和viewPager
        viewBinding.fragmentSelectViewpager.setAdapter(new TabFragmentPageAdapter(getChildFragmentManager(),0,tabTitles,tabFragments));
        viewBinding.fragmentSelectViewpager.setOffscreenPageLimit(4);
        viewBinding.tabTlIndicator.setupWithViewPager(viewBinding.fragmentSelectViewpager);
    }
}
