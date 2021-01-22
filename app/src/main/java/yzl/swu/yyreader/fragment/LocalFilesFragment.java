package yzl.swu.yyreader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.List;

import yzl.swu.yyreader.activity.FileSelectorActivity;
import yzl.swu.yyreader.adapter.LocalFileAdapter;
import yzl.swu.yyreader.databinding.FragmentLocalFilesBinding;
import yzl.swu.yyreader.models.LocalFileModel;

public class LocalFilesFragment extends BaseFragment<FragmentLocalFilesBinding> {

    protected void initWidget(){
        //设置线性布局
        viewBinding.localFilesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置适配器
        List<LocalFileModel> models = Arrays.asList(new LocalFileModel("sds","fsaf","dsd"));
        viewBinding.localFilesRv.setAdapter(new LocalFileAdapter(models));
    }
}
