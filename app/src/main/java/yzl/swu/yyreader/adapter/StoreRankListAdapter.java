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
import com.bumptech.glide.request.RequestOptions;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.models.BookRankModel;

public class StoreRankListAdapter extends BaseListAdapter<BookRankModel> {

    @Override
    protected IViewHolder<BookRankModel> createViewHolder(int viewType) {
        return new ViewHolder();
    }

    @Override
    public void setOnItemClickListener() {

    }

    //ViewHolder
    public class ViewHolder implements IViewHolder<BookRankModel> {

        View itemView;
        Context context;

        ImageView cover;
        TextView mTitle;
        TextView mDescribe;
        TextView mTags;
        TextView mScores;
        TextView mRank;

        @Override
        public View createItemView(ViewGroup parent) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_store_book_list, parent, false);
            context = parent.getContext();
            return itemView;
        }

        @Override
        public void initView() {
            cover = itemView.findViewById(R.id.iconImageView);
            mTitle = itemView.findViewById(R.id.titleTextView);
            mDescribe = itemView.findViewById(R.id.bookDescribeTextView);
            mTags = itemView.findViewById(R.id.tagTextView);
            mScores = itemView.findViewById(R.id.scoreTextView);
            mRank = itemView.findViewById(R.id.rankTextView);
            mRank.setVisibility(View.VISIBLE);
        }

        @Override
        public void onBind(BookRankModel value, int pos) {

            //显示数据
            Glide.with(context)
                    .load(value.getPicUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_book_loading)
                            .error(R.drawable.ic_load_error)
                            .centerCrop())
                    .into(cover);

            mTitle.setText(value.getBookName());
            mDescribe.setText(value.getBookDesc());
            mTags.setText(String.format("%s · %s · %s万人气",value.getAuthorName(),value.getCatName(),value.getVisitCount() == null ? 0 : value.getVisitCount()/1000));
            mScores.setText(value.getScore()+"分");
            mRank.setText(String.valueOf(pos+1));
        }

        @Override
        public void onClick() {

        }

    }

}
