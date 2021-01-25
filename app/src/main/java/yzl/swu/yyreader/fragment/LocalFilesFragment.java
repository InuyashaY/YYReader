package yzl.swu.yyreader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yzl.swu.yyreader.activity.FileSelectorActivity;
import yzl.swu.yyreader.adapter.LocalFileAdapter;
import yzl.swu.yyreader.adapter.SpaceItemDecoration;
import yzl.swu.yyreader.common.FileType;
import yzl.swu.yyreader.databinding.FragmentLocalFilesBinding;
import yzl.swu.yyreader.models.LocalFileModel;
import yzl.swu.yyreader.utils.FileManager;
import yzl.swu.yyreader.utils.StringUtils;
import yzl.swu.yyreader.utils.Utils;

import static yzl.swu.yyreader.common.Constants.FORMAT_DATE;

public class LocalFilesFragment extends BaseFragment<FragmentLocalFilesBinding> {
    List<LocalFileModel> models;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    //初始化数据
    private void initData(){
        List<File> files = FileManager.getInstance().listTxtFiles();
        models = new ArrayList<>();
        for (File file : files){
            long file_date = file.lastModified();
            LocalFileModel model = new LocalFileModel(file.getName());
            model.setFileDate(StringUtils.dateConvert(file_date,FORMAT_DATE));
            model.setFileSize(FileManager.getInstance().getFileSize(file));
            model.setFileType(FileType.TXTFILE);
            models.add(model);
        }
    }

    protected void initWidget(){
        //设置线性布局
        viewBinding.localFilesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置适配器
        viewBinding.localFilesRv.setAdapter(new LocalFileAdapter(models));
        //设置decoration
        viewBinding.localFilesRv.addItemDecoration(new SpaceItemDecoration(Utils.dpToPx(getContext(),2)));
    }
}
