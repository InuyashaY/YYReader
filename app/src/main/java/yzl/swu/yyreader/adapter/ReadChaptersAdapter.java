package yzl.swu.yyreader.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.models.TxtChapterModel;

public class ReadChaptersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TxtChapterModel> chapterModels;
    private OnChapterClickListener listener;
    private Context mContext;
    //选中章节
    private int selectedChapterIndex = 0;

    public ReadChaptersAdapter(List<TxtChapterModel> values,Context context, OnChapterClickListener listener) {
        this.chapterModels = values;
        this.listener = listener;
        this.mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.read_chapters_item,parent,false);
        ChapterViewHolder viewHolder = new ChapterViewHolder(root);
        viewHolder.chapterTextView = root.findViewById(R.id.titleTextView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ChapterViewHolder viewHolder = (ChapterViewHolder) holder;
        //显示数据
        viewHolder.chapterTextView.setText("· "+chapterModels.get(position).getTitle());
        if (position == selectedChapterIndex){
            viewHolder.setSelectedChapter();
        }else {
            viewHolder.chapterTextView.setTextColor(Color.BLACK);
        }

        viewHolder.chapterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(viewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterModels.size();
    }


    //item类
    class ChapterViewHolder extends RecyclerView.ViewHolder{
        TextView chapterTextView;
        public ChapterViewHolder(View itemView){
            super(itemView);
        }

        public void setSelectedChapter(){
            chapterTextView.setTextColor(ContextCompat.getColor(mContext,R.color.light_red));
            chapterTextView.setSelected(true);
        }
    }

    public void setChapter(int pos){
        selectedChapterIndex = pos;
        notifyDataSetChanged();
    }



    /**********************interface***************************/
    public interface OnChapterClickListener{
        void onItemClick(int pos);
    }
}
