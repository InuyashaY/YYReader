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
import yzl.swu.yyreader.databinding.ItemFileSeletorBinding;
import yzl.swu.yyreader.models.LocalFileModel;

public class LocalFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<LocalFileModel> localFileModels;
    List<LocalFileModel> selectedFiles = new ArrayList<>();

    public LocalFileAdapter(List<LocalFileModel> localFileModels){
        this.localFileModels = localFileModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_seletor,parent,false);
        return new ViewHolder(root);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.titleTextView.setText(localFileModels.get(position).getFileTitle());
        viewHolder.sizeTextView.setText(localFileModels.get(position).getFileSize());
        viewHolder.dateTextView.setText(localFileModels.get(position).getFileDate());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectedFiles.add(localFileModels.get(position));
                }else {
                    selectedFiles.remove(localFileModels.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return localFileModels.size();
    }

    /******************inner class*********************/
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        TextView sizeTextView;
        TextView dateTextView;
        CheckBox checkBox;
        public ViewHolder(View itemView){
            super(itemView);
            titleTextView = itemView.findViewById(R.id.fileTitleTextView);
            sizeTextView = itemView.findViewById(R.id.fileSizeTextView);
            dateTextView = itemView.findViewById(R.id.fileDateTextView);
            checkBox = itemView.findViewById(R.id.fileSelectBox);
        }
    }
}
