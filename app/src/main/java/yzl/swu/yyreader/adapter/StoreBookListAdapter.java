package yzl.swu.yyreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.models.StoreBookItemDao;
import yzl.swu.yyreader.models.StoreGroupBookModel;

public class StoreBookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StoreBookItemDao> bookModels;
    private OnBookClickListener listener;
    private Context context;

    public StoreBookListAdapter(List<StoreBookItemDao> values, Context context, OnBookClickListener listener) {
        this.bookModels = values;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_store_book_list,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(root);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        //显示数据
        Glide.with(context)
                .load(bookModels.get(position).getPicUrl())
                .placeholder(R.drawable.ic_book_loading)
                .error(R.drawable.ic_load_error)
                .centerCrop()
                .into(viewHolder.cover);

        viewHolder.mTitle.setText(bookModels.get(position).getBookName());
        viewHolder.mDescribe.setText(bookModels.get(position).getBookDesc().replace("&nbsp"," ").replace("<br/>"," "));
        viewHolder.mTags.setText(String.format("%s · %s · %s万人气",bookModels.get(position).getAuthorName(),bookModels.get(position).getCatName(),bookModels.get(position).getVisitCount()));
        viewHolder.mScores.setText(bookModels.get(position).getScore()+"分");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(viewHolder.getAdapterPosition(),viewHolder.cover);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (bookModels == null) return 0;
        return bookModels.size();
    }


    /******************inner class*********************/
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView mTitle;
        TextView mDescribe;
        TextView mTags;
        TextView mScores;
        TextView mRank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.iconImageView);
            mTitle = itemView.findViewById(R.id.titleTextView);
            mDescribe = itemView.findViewById(R.id.bookDescribeTextView);
            mTags = itemView.findViewById(R.id.tagTextView);
            mScores = itemView.findViewById(R.id.scoreTextView);
            mRank = itemView.findViewById(R.id.rankTextView);
        }
    }

    public interface OnBookClickListener{
        void onItemClick(int pos,View view);
    }

    public void refreshModels(List<StoreBookItemDao> models){
        this.bookModels = models;
        notifyDataSetChanged();
    }

    public StoreBookItemDao getItem(int pos){
        if (bookModels != null && !bookModels.isEmpty()){
            return bookModels.get(pos);
        }
        return null;
    }
}
