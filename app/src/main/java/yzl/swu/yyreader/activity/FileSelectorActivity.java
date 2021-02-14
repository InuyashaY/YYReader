package yzl.swu.yyreader.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.adapter.TabFragmentPageAdapter;
import yzl.swu.yyreader.databinding.ActivityFileSelectorBinding;
import yzl.swu.yyreader.fragment.FilesCategoryFragment;
import yzl.swu.yyreader.fragment.LocalFilesFragment;
import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.utils.FileManager;

import static yzl.swu.yyreader.common.Constants.FIELSELECTOR_RESULT_KEY;
import static yzl.swu.yyreader.common.Constants.FILESELECTOR_RESULT_CODE;

public class FileSelectorActivity extends BaseActivity<ActivityFileSelectorBinding> implements FilesCategoryFragment.OnTxtCheckedListener, LocalFilesFragment.OnTxtCheckedListener {
    //tab的标题
    List<String> tabTitles;
    //对应的fragment
    List<Fragment> tabFragments;
    //选中的文件
    List<File> seletedFiles;

    public static void show(Context context){
        Intent intent = new Intent(context,FileSelectorActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initWidget() {
//        super.initWidget();
        viewBinding.selectorToolbar.setTitle("本机导入");
        setUpTabLayout();

        //添加选中的书籍
        viewBinding.addToShelfBtn.setOnClickListener((v)->{
            ArrayList<BookModel> models = new ArrayList<>();
            for (File file:seletedFiles){
                BookModel model = new BookModel(file.getName(), "moren","未读",file.getPath());
                models.add(model);
            }

            Intent result = new Intent();
            result.putParcelableArrayListExtra(FIELSELECTOR_RESULT_KEY,models);
            setResult(FILESELECTOR_RESULT_CODE,result);
            finish();
        });
    }

    private void setUpTabLayout(){
        //设置标题和fragment
        tabTitles = Arrays.asList("智能导入","手机目录");
        tabFragments = new ArrayList<>();
        LocalFilesFragment localFilesFragment = new LocalFilesFragment();
        FilesCategoryFragment filesCategoryFragment = new FilesCategoryFragment();
        localFilesFragment.setCheckedListener(this);
        filesCategoryFragment.setCheckedListener(this);
        tabFragments.add(localFilesFragment);
        tabFragments.add(filesCategoryFragment);

        //设置适配器 绑定tab和viewPager
        viewBinding.fileSelectViewpager.setAdapter(new TabFragmentPageAdapter(getSupportFragmentManager(),0,tabTitles,tabFragments));
        viewBinding.fileSelectViewpager.setOffscreenPageLimit(3);
        viewBinding.tabTlIndicator.setupWithViewPager(viewBinding.fileSelectViewpager);

    }

    @Override
    public void onTxtFileChecked(List<File> selectedFiles) {
        this.seletedFiles = selectedFiles;
        if (selectedFiles.size() > 0){
            viewBinding.addToShelfBtn.setEnabled(true);
            viewBinding.addToShelfBtn.setText("导入书架("+selectedFiles.size()+")");
        }else {
            viewBinding.addToShelfBtn.setText("导入书架");
            viewBinding.addToShelfBtn.setEnabled(false);
        }
    }

}