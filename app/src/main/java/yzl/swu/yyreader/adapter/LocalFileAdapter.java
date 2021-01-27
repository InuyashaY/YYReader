package yzl.swu.yyreader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.common.FileType;
import yzl.swu.yyreader.utils.FileManager;
import yzl.swu.yyreader.utils.StringUtils;

import static yzl.swu.yyreader.common.Constants.FORMAT_DATE;

public class LocalFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //显示文件的模型
    List<File> localFiles;
    //已选文件
    List<File> selectedFiles = new ArrayList<>();
    //点击事件监听者
    private OnItemClickListener mClickListener;
    //选中回调
    private OnTxtCheckedListener mCheckedListener;

    public LocalFileAdapter(List<File> localFileModels){
        this.localFiles = localFileModels;
    }

    public void setClickListener(OnItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setCheckedListener(OnTxtCheckedListener checkedListener){
        this.mCheckedListener = checkedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //判断文件类型
        int layout_id = viewType == FileType.DIRECTORY.ordinal()?R.layout.item_file_category:R.layout.item_file_seletor;
        View root = LayoutInflater.from(parent.getContext()).inflate(layout_id,parent,false);
        return new ViewHolder(root,viewType);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        File file = localFiles.get(position);
        if (file.isDirectory()){
            viewHolder.containsTextView.setText(String.valueOf(FileManager.getInstance().getSubFileCount(file)));
        }else {
            viewHolder.sizeTextView.setText(FileManager.getInstance().getFileSize(file));
            viewHolder.dateTextView.setText(StringUtils.dateConvert(file.lastModified(),FORMAT_DATE));
            //txt文件选中回调
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        selectedFiles.add(file);
                    }else {
                        selectedFiles.remove(file);
                    }
                    mCheckedListener.onTxtFileChecked(selectedFiles);
                }
            });
        }
        viewHolder.titleTextView.setText(file.getName());
        //item点击事件回调
        if (mClickListener != null) viewHolder.itemView.setOnClickListener((v)->{
            mClickListener.onItemClick(viewHolder.itemView,position);
        });
    }

    //获取文件数量
    @Override
    public int getItemCount() {
        return localFiles.size();
    }

    // 获取选中文件数量
    public int getSelectedCount(){
        return selectedFiles.size();
    }

    //获取某个item的model
    public File getItem(int pos){
        return localFiles.get(pos);
    }

    //获取文件类型
    @Override
    public int getItemViewType(int position) {
        return localFiles.get(position).isDirectory()?FileType.DIRECTORY.ordinal():FileType.TXTFILE.ordinal();
    }

    //更新目录
    public void refreshItems(List<File> items){
        localFiles.clear();
        localFiles.addAll(items);
        notifyDataSetChanged();
    }

    //获取当前目录的所有文件
    public List<File> getItems(){
        return localFiles;
    }

    //获取已选文件
    public List<File> getSelectedItems(){
        return selectedFiles;
    }

    /******************inner class*********************/
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        TextView sizeTextView;
        TextView dateTextView;
        CheckBox checkBox;
        TextView containsTextView;
        public ViewHolder(View itemView,int fileType){
            super(itemView);
            titleTextView = itemView.findViewById(R.id.fileTitleTextView);
            if (fileType == FileType.TXTFILE.ordinal()){
                sizeTextView = itemView.findViewById(R.id.fileSizeTextView);
                dateTextView = itemView.findViewById(R.id.fileDateTextView);
                checkBox = itemView.findViewById(R.id.fileSelectBox);
            }else {
                containsTextView = itemView.findViewById(R.id.containsTextView);
            }
        }
    }


    /**********************interface***************************/
    public interface OnItemClickListener{
        void onItemClick(View view, int pos);
    }

    public interface OnTxtCheckedListener{
        void onTxtFileChecked(List<File> selectedFiles);
    }
}
