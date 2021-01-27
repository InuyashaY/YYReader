package yzl.swu.yyreader.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yzl.swu.yyreader.adapter.LocalFileAdapter;
import yzl.swu.yyreader.adapter.SpaceItemDecoration;
import yzl.swu.yyreader.common.FileType;
import yzl.swu.yyreader.databinding.FragmentLocalFilesBinding;
import yzl.swu.yyreader.utils.FileManager;
import yzl.swu.yyreader.utils.StringUtils;
import yzl.swu.yyreader.utils.Utils;

import static yzl.swu.yyreader.common.Constants.FORMAT_DATE;

public class LocalFilesFragment extends BaseFragment<FragmentLocalFilesBinding> {
    List<File> files;
    //选中文件回调
    private OnTxtCheckedListener mCheckedListener;
    //Adapter
    LocalFileAdapter mAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    public void setCheckedListener(OnTxtCheckedListener listener){
        this.mCheckedListener = listener;
    }

    //初始化数据
    private void initData(){
        files = FileManager.getInstance().listTxtFiles();

    }

    protected void initWidget(){
        //设置线性布局
        viewBinding.localFilesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置适配器
        mAdapter = new LocalFileAdapter(files);
        viewBinding.localFilesRv.setAdapter(mAdapter);
        //设置decoration
        viewBinding.localFilesRv.addItemDecoration(new SpaceItemDecoration(Utils.dpToPx(getContext(),2)));

        initEvent();
    }

    public void initEvent(){
        mAdapter.setCheckedListener(new LocalFileAdapter.OnTxtCheckedListener() {
            @Override
            public void onTxtFileChecked(List<File> selectedFiles) {
                mCheckedListener.onTxtFileChecked(selectedFiles);
            }
        });
    }

    /*****************interface********************/
    public interface OnTxtCheckedListener{
        void onTxtFileChecked(List<File> selectedFiles);
    }
}
