package yzl.swu.yyreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.common.Constants;
import yzl.swu.yyreader.models.BookComment;
import yzl.swu.yyreader.models.BookRankModel;
import yzl.swu.yyreader.utils.CircleTransformUtils;
import yzl.swu.yyreader.utils.StringUtils;
import yzl.swu.yyreader.views.EasyRatingBar;

public class HotCommentAdapter extends BaseListAdapter<BookComment>{
    @Override
    protected IViewHolder<BookComment> createViewHolder(int viewType) {
        return new ViewHolder();
    }

    @Override
    public void setOnItemClickListener() {

    }

    //ViewHolder
    public class ViewHolder implements IViewHolder<BookComment> {

        View itemView;
        Context context;

        private ImageView mIvPortrait;
        private TextView mTvAuthor;
        private TextView mTvLv;
        private TextView mTvTitle;
        private EasyRatingBar mErbRate;
        private TextView mTvContent;
//        private TextView mTvHelpful;
        private TextView mTvTime;

        @Override
        public View createItemView(ViewGroup parent) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_hot_comment, parent, false);
            context = parent.getContext();
            return itemView;
        }

        @Override
        public void initView() {
            mIvPortrait = itemView.findViewById(R.id.hot_comment_iv_cover);
            mTvAuthor = itemView.findViewById(R.id.hot_comment_tv_author);
            mTvLv = itemView.findViewById(R.id.hot_comment_tv_lv);
            mTvTitle = itemView.findViewById(R.id.hot_comment_title);
            mErbRate = itemView.findViewById(R.id.hot_comment_erb_rate);
            mTvContent = itemView.findViewById(R.id.hot_comment_tv_content);
//            mTvHelpful = itemView.findViewById(R.id.hot_comment_tv_helpful);
            mTvTime = itemView.findViewById(R.id.hot_comment_tv_time);
        }

        @Override
        public void onBind(BookComment value, int pos) {

            //显示数据
            //头像
            Glide.with(context)
                    .load(value.getCreateUserPhoto())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_default_portrait)
                            .error(R.drawable.ic_load_error)
                            .transform(new CircleTransformUtils(context)))
                    .into(mIvPortrait);
            //作者
            mTvAuthor.setText(value.getCreateUserName());
            //等级
//            mTvLv.setText(StringUtils.getString(context,R.string.user_lv,"5"));
            mTvLv.setText(" lv5");
            //标题
            mTvTitle.setText(value.getCommentContent());
            //评分
            mErbRate.setRating(5);
            //内容
            mTvContent.setText(value.getCommentContent());
            //点赞数
//            mTvHelpful.setText(value.getReplyCount()+"");
            //时间
            mTvTime.setText(StringUtils.dateConvert(value.getCreateTime().toString(), Constants.FORMAT_BOOK_DATE));
        }

        @Override
        public void onClick() {

        }
    }
}
