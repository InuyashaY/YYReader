package yzl.swu.yyreader.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yzl.swu.yyreader.adapter.TabFragmentPageAdapter;
import yzl.swu.yyreader.databinding.ActivityBaseTabBinding;
import yzl.swu.yyreader.fragment.FilesCategoryFragment;
import yzl.swu.yyreader.fragment.LocalFilesFragment;
import yzl.swu.yyreader.models.BookModel;

import static yzl.swu.yyreader.common.Constants.FIELSELECTOR_RESULT_KEY;
import static yzl.swu.yyreader.common.Constants.FILESELECTOR_RESULT_CODE;

public class BaseTabActivity extends BaseActivity<ActivityBaseTabBinding> {
    //tab的标题
    List<String> tabTitles;
    //对应的fragment
    List<Fragment> tabFragments;

    public static void show(Context context){
        Intent intent = new Intent(context,FileSelectorActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initWidget() {
        setUpTabLayout();
    }

    protected void setUpTabLayout(){
        //设置适配器 绑定tab和viewPager
        viewBinding.fileSelectViewpager.setAdapter(new TabFragmentPageAdapter(getSupportFragmentManager(),0,tabTitles,tabFragments));
        viewBinding.fileSelectViewpager.setOffscreenPageLimit(3);
        viewBinding.tabTlIndicator.setupWithViewPager(viewBinding.fileSelectViewpager);

    }

}
