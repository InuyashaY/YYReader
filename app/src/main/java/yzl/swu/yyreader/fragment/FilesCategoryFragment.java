package yzl.swu.yyreader.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import yzl.swu.yyreader.adapter.LocalFileAdapter;
import yzl.swu.yyreader.adapter.SpaceItemDecoration;
import yzl.swu.yyreader.common.FileStack;
import yzl.swu.yyreader.common.FileType;
import yzl.swu.yyreader.databinding.FragmentFileCategoryBinding;
import yzl.swu.yyreader.utils.FileManager;
import yzl.swu.yyreader.utils.StringUtils;
import yzl.swu.yyreader.utils.Utils;

import static yzl.swu.yyreader.common.Constants.FORMAT_DATE;

public class FilesCategoryFragment extends BaseFragment<FragmentFileCategoryBinding> {
    List<File> files;
    //文件回退栈
    FileStack stack;
    //Adapter
    LocalFileAdapter mAdapter;
    //选中文件回调
    private OnTxtCheckedListener mCheckedListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData(){
        files = FileManager.getInstance().listRootFiles();
        stack = new FileStack();
//        FileStack.FileSnapShot snapShot = new FileStack.FileSnapShot();
//        snapShot.filePath = FileManager.getInstance().getRootPath();
//        snapShot.fileList = files;
//        stack.push(snapShot);
//        for (File file : files){
//            LocalFileModel model = new LocalFileModel(file.getName());
//            if (file.isDirectory()){
//                model.setFileType(FileType.DIRECTORY);
//
//            }else {
//                long file_date = file.lastModified();
//                model.setFileDate(StringUtils.dateConvert(file_date,FORMAT_DATE));
//                model.setFileSize(FileManager.getInstance().getFileSize(file));
//                model.setFileType(FileType.TXTFILE);
//            }
//            models.add(model);
//        }
        files.sort(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && !o2.isDirectory()) {
                    return -1;
                }
                if (o2.isDirectory() && !o1.isDirectory()) {
                    return 1;
                }
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

    }


    public void setCheckedListener(OnTxtCheckedListener listener){
        this.mCheckedListener = listener;
    }


    //重写initWidget  onCreatedView调用
    @Override
    protected void initWidget() {
        super.initWidget();
        //线性布局
        viewBinding.fileCategoryRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //适配器
        mAdapter = new LocalFileAdapter(files);
        viewBinding.fileCategoryRv.setAdapter(mAdapter);
        //decoration 设置item间距
        viewBinding.fileCategoryRv.addItemDecoration(new SpaceItemDecoration(Utils.dpToPx(getContext(),2)));
        //文件路径
        viewBinding.filePathText.setText(FileManager.getInstance().getRootPath());
        //事件处理
        initEvent();
    }


    //事件处理
    private void initEvent(){
        //选中文件item
        mAdapter.setClickListener(new LocalFileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                File file = mAdapter.getItem(pos);
                if (file.isDirectory()){
                    //若为目录
                    FileStack.FileSnapShot fileSnapShot = new FileStack.FileSnapShot();
                    fileSnapShot.filePath = viewBinding.filePathText.getText().toString();
                    fileSnapShot.fileList = new ArrayList<>(mAdapter.getItems());
                    stack.push(fileSnapShot);
                    List<File> newFiles = FileManager.getInstance().listFilesByFilePath(file.getPath());
                    newFiles.sort(new Comparator<File>() {
                        @Override
                        public int compare(File o1, File o2) {
                            if (o1.isDirectory() && !o2.isDirectory()) {
                                return -1;
                            }
                            if (o2.isDirectory() && !o1.isDirectory()) {
                                return 1;
                            }
                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }
                    });

                    viewBinding.filePathText.setText(file.getPath());
                    mAdapter.refreshItems(newFiles);
                }else {
                    //若为文件
                }
            }
        });

        //文件目录回退事件
        viewBinding.backCategoryBtn.setOnClickListener((v)->{
            FileStack.FileSnapShot snapShot = stack.pop();
            if (snapShot == null) return;
            mAdapter.refreshItems(snapShot.fileList);
            viewBinding.filePathText.setText(snapShot.filePath);
        });

        //选中回调
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
