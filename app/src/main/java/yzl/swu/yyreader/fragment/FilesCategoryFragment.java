package yzl.swu.yyreader.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import yzl.swu.yyreader.adapter.LocalFileAdapter;
import yzl.swu.yyreader.adapter.SpaceItemDecoration;
import yzl.swu.yyreader.common.FileType;
import yzl.swu.yyreader.databinding.FragmentFileCategoryBinding;
import yzl.swu.yyreader.models.LocalFileModel;
import yzl.swu.yyreader.utils.FileManager;
import yzl.swu.yyreader.utils.StringUtils;
import yzl.swu.yyreader.utils.Utils;

import static yzl.swu.yyreader.common.Constants.FORMAT_DATE;

public class FilesCategoryFragment extends BaseFragment<FragmentFileCategoryBinding> {
    List<LocalFileModel> models;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData(){
        List<File> files = FileManager.getInstance().listRootFiles();
        models = new ArrayList<>();
        for (File file : files){
            LocalFileModel model = new LocalFileModel(file.getName());
            if (file.isDirectory()){
                model.setFileType(FileType.DIRECTORY);

            }else {
                long file_date = file.lastModified();
                model.setFileDate(StringUtils.dateConvert(file_date,FORMAT_DATE));
                model.setFileSize(FileManager.getInstance().getFileSize(file));
                model.setFileType(FileType.TXTFILE);
            }
            models.add(model);
        }
        models.sort(new Comparator<LocalFileModel>() {
            @Override
            public int compare(LocalFileModel o1, LocalFileModel o2) {
                if (o1.getFileType()==FileType.DIRECTORY && o2.getFileType()==FileType.TXTFILE) {
                    return -1;
                }
                if (o2.getFileType()==FileType.DIRECTORY && o1.getFileType()==FileType.TXTFILE) {
                    return 1;
                }
                return o1.getFileTitle().compareToIgnoreCase(o2.getFileTitle());
            }
        });
    }


    //重写initWidget  onCreatedView调用
    @Override
    protected void initWidget() {
        super.initWidget();
        //线性布局
        viewBinding.fileCategoryRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //适配器
        viewBinding.fileCategoryRv.setAdapter(new LocalFileAdapter(models));
        //decoration 设置item间距
        viewBinding.fileCategoryRv.addItemDecoration(new SpaceItemDecoration(Utils.dpToPx(getContext(),2)));

        //事件处理
        initEvent();
    }


    //事件处理
    private void initEvent(){

    }
}
