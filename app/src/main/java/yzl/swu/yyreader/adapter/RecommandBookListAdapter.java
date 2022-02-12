package yzl.swu.yyreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.activity.BookDetailActivity;
import yzl.swu.yyreader.models.BookRankModel;
import yzl.swu.yyreader.models.StoreBookItemDao;

public class RecommandBookListAdapter extends BaseListAdapter<StoreBookItemDao> {
    @Override
    protected IViewHolder<StoreBookItemDao> createViewHolder(int viewType) {
        return new ViewHolder();
    }

    @Override
    public void setOnItemClickListener() {

    }

    //ViewHolder
    public class ViewHolder implements IViewHolder<StoreBookItemDao> {

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
        }

        @Override
        public void onBind(StoreBookItemDao data, int pos) {
            //显示数据
            Glide.with(context)
                    .load(data.getPicUrl())
                    .placeholder(R.drawable.ic_book_loading)
                    .error(R.drawable.ic_load_error)
                    .centerCrop()
                    .into(cover);

            mTitle.setText(data.getBookName());
            mDescribe.setText(data.getBookDesc().replace("&nbsp"," ").replace("<br/>"," "));
            mTags.setText(String.format("%s · %s · %s万人气",data.getAuthorName(),data.getCatName(),data.getVisitCount()));
            mScores.setText(data.getScore()+"分");
        }

        @Override
        public void onClick() {

        }
    }
}
