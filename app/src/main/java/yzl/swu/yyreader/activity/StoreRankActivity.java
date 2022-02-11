package yzl.swu.yyreader.activity;

import java.util.ArrayList;
import java.util.Arrays;

import yzl.swu.yyreader.adapter.StoreRankListAdapter;
import yzl.swu.yyreader.fragment.FilesCategoryFragment;
import yzl.swu.yyreader.fragment.LocalFilesFragment;
import yzl.swu.yyreader.fragment.StoreRankFragment;

public class StoreRankActivity extends BaseTabActivity {
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

        super.setUpTabLayout();
    }
}
