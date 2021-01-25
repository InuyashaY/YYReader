package yzl.swu.yyreader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.common.FileType;
import yzl.swu.yyreader.models.LocalFileModel;

public class LocalFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<LocalFileModel> localFileModels;
    //已选文件
    List<LocalFileModel> selectedFiles = new ArrayList<>();

    public LocalFileAdapter(List<LocalFileModel> localFileModels){
        this.localFileModels = localFileModels;
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
        LocalFileModel model = localFileModels.get(position);
        if (model.getFileType() == FileType.DIRECTORY){

        }else {
            viewHolder.sizeTextView.setText(model.getFileSize());
            viewHolder.dateTextView.setText(model.getFileDate());
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        selectedFiles.add(model);
                    }else {
                        selectedFiles.remove(model);
                    }
                }
            });
        }
        viewHolder.titleTextView.setText(model.getFileTitle());

    }

    @Override
    public int getItemCount() {
        return localFileModels.size();
    }

    //获取文件类型
    @Override
    public int getItemViewType(int position) {
        return localFileModels.get(position).getFileType().ordinal();
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
}
