package yzl.swu.yyreader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.models.BookModel;

public class BookShelfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BookModel> bookModels;
    private OnBookClickListener listener;

    public BookShelfAdapter(List<BookModel> values,OnBookClickListener listener) {
        this.bookModels = values;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_recycler_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(root);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        //显示数据
        viewHolder.cover.setImageResource(bookModels.get(position).getCoverResource());
        viewHolder.mTitle.setText(bookModels.get(position).getBookTitle());
        viewHolder.mRecord.setText(bookModels.get(position).getRecord());

        viewHolder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(viewHolder.getAdapterPosition(),viewHolder.cover);
            }
        });
    }


    @Override
    public int getItemCount() {
        return bookModels.size();
    }


    /******************inner class*********************/
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView mTitle;
        TextView mRecord;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.iconImageView);
            mTitle = itemView.findViewById(R.id.titleTextView);
            mRecord = itemView.findViewById(R.id.recordTextView);
        }
    }

    public interface OnBookClickListener{
        void onItemClick(int pos,View view);
    }
}
