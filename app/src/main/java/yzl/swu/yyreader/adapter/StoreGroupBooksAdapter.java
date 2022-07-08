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

import java.util.ArrayList;
import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.models.BookRankModel;
import yzl.swu.yyreader.models.StoreBookItemDao;
import yzl.swu.yyreader.models.StoreGroupBookModel;
import yzl.swu.yyreader.utils.Utils;

public class StoreGroupBooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BookRankModel> bookModels;
    private OnBookClickListener listener;
    private Context mContext;
    private List<StoreGroupBookModel> defaultModels;

    public StoreGroupBooksAdapter(List<BookRankModel> values, Context context, OnBookClickListener listener) {
        this.bookModels = values;
        this.listener = listener;
        this.mContext = context;
        initDefaultData();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_book,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(root);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        //显示数据
        if (bookModels == null || bookModels.isEmpty()) {
            StoreGroupBookModel model = defaultModels.get(position);
            viewHolder.cover.setImageResource(model.getCoverResource());
            viewHolder.mTitle.setText(model.getTitle());
            viewHolder.mAuthor.setText(model.getAuthor());
        } else {
            //封面
            Glide.with(mContext)
                    .load(bookModels.get(position).getPicUrl())
                    .apply(
                            new RequestOptions().placeholder(R.drawable.ic_book_loading)
                                    .error(R.drawable.ic_load_error)
                                    .centerCrop()
                    )
                    .into(viewHolder.cover);
            viewHolder.mTitle.setText(bookModels.get(position).getBookName());
            viewHolder.mAuthor.setText(bookModels.get(position).getAuthorName());

            viewHolder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(viewHolder.getAdapterPosition(),viewHolder.cover);
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return bookModels == null || bookModels.isEmpty() ? defaultModels.size() : bookModels.size();
    }


    /******************inner class*********************/
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView mTitle;
        TextView mAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.iconImageView);
            mTitle = itemView.findViewById(R.id.titleTextView);
            mAuthor = itemView.findViewById(R.id.authorTextView);
        }
    }

    public interface OnBookClickListener{
        void onItemClick(int pos,View view);
    }

    public void refreshModels(List<BookRankModel> models){
        this.bookModels = models;
        notifyDataSetChanged();
    }

    public BookRankModel getItem(int position){
        return bookModels.get(position);
    }


    private void initDefaultData() {
        defaultModels = new ArrayList<>();

        StoreGroupBookModel model1 = new StoreGroupBookModel(R.drawable.tgsw,"太古神王","净无痕");
        StoreGroupBookModel model2 = new StoreGroupBookModel(R.drawable.dldl,"斗罗大陆","唐家三少");
        StoreGroupBookModel model3 = new StoreGroupBookModel(R.drawable.dzz,"大主宰","天蚕土豆");
        StoreGroupBookModel model4 = new StoreGroupBookModel(R.drawable.jl,"捡漏","净无痕");
        StoreGroupBookModel model5 = new StoreGroupBookModel(R.drawable.jswh,"绝世武魂","天逆");
        StoreGroupBookModel model6 = new StoreGroupBookModel(R.drawable.dpcq,"斗破苍穹","天蚕土豆");

        defaultModels.add(model1);
        defaultModels.add(model2);
        defaultModels.add(model3);
        defaultModels.add(model4);
        defaultModels.add(model5);
        defaultModels.add(model6);
    }
}
