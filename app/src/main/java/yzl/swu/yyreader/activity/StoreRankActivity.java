package yzl.swu.yyreader.activity;

import android.content.Context;
import android.content.Intent;

import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.Arrays;

import yzl.swu.yyreader.adapter.StoreRankListAdapter;
import yzl.swu.yyreader.adapter.TabFragmentPageAdapter;
import yzl.swu.yyreader.databinding.ActivityBaseTabBinding;
import yzl.swu.yyreader.fragment.FilesCategoryFragment;
import yzl.swu.yyreader.fragment.LocalFilesFragment;
import yzl.swu.yyreader.fragment.StoreRankFragment;

public class StoreRankActivity extends BaseTabActivity<ActivityBaseTabBinding> {
    public static void show(Context context){
        Intent intent = new Intent(context,StoreRankActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void initWidget() {
        viewBinding.selectorToolbar.setTitle("排行榜");
        super.initWidget();
    }

    @Override
    protected void setUpTabLayout() {
        //设置标题和fragment
        tabTitles = Arrays.asList("点击榜","新书榜","更新榜","评论榜");
        tabFragments = new ArrayList<>();
        for (byte i=0; i<4; i++){
            tabFragments.add(StoreRankFragment.newInstance(String.valueOf(i)));
        }

        //设置适配器 绑定tab和viewPager
        viewBinding.fileSelectViewpager.setAdapter(new TabFragmentPageAdapter(getSupportFragmentManager(),0,tabTitles,tabFragments));
        viewBinding.fileSelectViewpager.setOffscreenPageLimit(4);
        viewBinding.tabTlIndicator.setupWithViewPager(viewBinding.fileSelectViewpager);

    }
}
