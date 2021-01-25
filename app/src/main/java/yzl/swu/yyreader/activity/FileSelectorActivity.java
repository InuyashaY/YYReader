package yzl.swu.yyreader.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yzl.swu.yyreader.adapter.TabFragmentPageAdapter;
import yzl.swu.yyreader.databinding.ActivityFileSelectorBinding;
import yzl.swu.yyreader.fragment.FilesCategoryFragment;
import yzl.swu.yyreader.fragment.LocalFilesFragment;
import yzl.swu.yyreader.utils.FileManager;

public class FileSelectorActivity extends BaseActivity<ActivityFileSelectorBinding> {
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
//        super.initWidget();
        viewBinding.selectorToolbar.setTitle("本机导入");
        setUpTabLayout();
        FileManager.getInstance().listTxtFiles();
    }

    private void setUpTabLayout(){
        //设置标题和fragment
        tabTitles = Arrays.asList("智能导入","手机目录");
        tabFragments = new ArrayList<>();
        tabFragments.add(new LocalFilesFragment());
        tabFragments.add(new FilesCategoryFragment());

        //设置适配器
        viewBinding.fileSelectViewpager.setAdapter(new TabFragmentPageAdapter(getSupportFragmentManager(),0,tabTitles,tabFragments));
        viewBinding.fileSelectViewpager.setOffscreenPageLimit(3);
        viewBinding.tabTlIndicator.setupWithViewPager(viewBinding.fileSelectViewpager);
    }
}