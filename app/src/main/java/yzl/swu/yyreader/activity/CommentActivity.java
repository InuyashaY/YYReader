package yzl.swu.yyreader.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.jidcoo.android.widget.commentview.CommentView;
import com.jidcoo.android.widget.commentview.callback.CustomCommentItemCallback;
import com.jidcoo.android.widget.commentview.callback.CustomReplyItemCallback;
import com.jidcoo.android.widget.commentview.callback.OnCommentLoadMoreCallback;
import com.jidcoo.android.widget.commentview.callback.OnItemClickCallback;
import com.jidcoo.android.widget.commentview.callback.OnPullRefreshCallback;
import com.jidcoo.android.widget.commentview.callback.OnReplyLoadMoreCallback;
import com.jidcoo.android.widget.commentview.defaults.DefaultCommentModel;
import com.jidcoo.android.widget.commentview.model.AbstractCommentModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import yzl.swu.yyreader.R;
import yzl.swu.yyreader.databinding.ActicityCommentBinding;
import yzl.swu.yyreader.models.BookComment;
import yzl.swu.yyreader.models.CommentReply;
import yzl.swu.yyreader.models.CustomCommentModel;
import yzl.swu.yyreader.remote.RemoteRepository;
import yzl.swu.yyreader.utils.Utils;
import yzl.swu.yyreader.viewholder.CustomCommentViewHolder;
import yzl.swu.yyreader.viewholder.CustomReplyViewHolder;
import yzl.swu.yyreader.viewholder.CustomViewStyleConfigurator;

/**
 * 对于CommentView的自定义数据类型和布局的使用实例（使用本地测试数据）
 * 使用自定义样式配置器，自定义数据模型，自定义布局
 * <u>注意：使用自定义数据类型就必须自定义布局实现，否则会抛出数据模型的java.lang.ClassCastException异常</u><br></br>
 * @author Jidcoo
 */
public class CommentActivity extends BaseActivity<ActicityCommentBinding> {
    private CommentView commentView;
    private EditText editor;
    private Gson gson;
    private boolean isReply = false;
    private String commentId;
    private String bookId = "1488053877507973120";
    private Button sureButton;

    //
    private static class ActivityHandler extends Handler {
        private final WeakReference<CommentActivity> mActivity;
        public ActivityHandler(CommentActivity activity) {
            mActivity = new WeakReference<CommentActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            CommentActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what){
                    case 1:
                        //commentView.loadFailed(true);//实际网络请求中如果加载失败调用此方法
//                        activity.commentView.loadComplete(activity.gson.fromJson((String)msg.obj,CustomCommentModel.class));
                        activity.commentView.loadComplete((CustomCommentModel) msg.obj);
                        break;
                    case 2:
                        //commentView.refreshFailed();//实际网络请求中如果加载失败调用此方法
                        activity.commentView.refreshComplete(activity.gson.fromJson((String)msg.obj, CustomCommentModel.class));
                        break;
                    case 3:
                        //commentView.loadFailed();//实际网络请求中如果加载失败调用此方法
//                        activity.commentView.loadMoreComplete(activity.gson.fromJson((String)msg.obj,CustomCommentModel.class));
                        activity.commentView.loadMoreComplete((CustomCommentModel) msg.obj);

                        break;
                    case 4:
                        //commentView.loadMoreReplyFailed();//实际网络请求中如果加载失败调用此方法
                        activity.commentView.loadMoreReplyComplete(activity.gson.fromJson((String)msg.obj,CustomCommentModel.class));
                        break;
                }
            }
        }
    }
    private final ActivityHandler activityHandler = new ActivityHandler(this);
    //


    @Override
    protected void processLogic() {
        bookId = getIntent().getStringExtra("bookId");
    }

    @Override
    protected void initWidget() {
        gson=new Gson();
//        localServer=new LocalServer(this,"api2");
        commentView = viewBinding.commentView;
        editor = findViewById(R.id.editor);
        sureButton = findViewById(R.id.reply_btn);
        //设置空视图
        //commentView.setEmptyView(view);
        //设置错误视图
        //commentView.setErrorView(view);
        //添加控件头布局
        // commentView.addHeaderView();
        commentView.setViewStyleConfigurator(new CustomViewStyleConfigurator(this));
        Context context = this;
        commentView.callbackBuilder()
                //自定义评论布局(必须使用ViewHolder机制)--CustomCommentItemCallback<C> 泛型C为自定义评论数据类
                .customCommentItem(new CustomCommentItemCallback<CustomCommentModel.CustomComment>() {
                    @Override
                    public View buildCommentItem(int groupPosition, CustomCommentModel.CustomComment comment, LayoutInflater inflater, View convertView, ViewGroup parent) {
                        //使用方法就像adapter里面的getView()方法一样
                        final CustomCommentViewHolder holder;
                        if(convertView==null){
                            //使用自定义布局
                            convertView=inflater.inflate(R.layout.custom_item_comment,parent,false);
                            holder=new CustomCommentViewHolder(convertView);
                            //必须使用ViewHolder机制
                            convertView.setTag(holder);
                        }else {
                            holder= (CustomCommentViewHolder) convertView.getTag();
                        }
                        holder.prizes.setText("100");
                        holder.userName.setText(comment.getPosterName());
                        holder.comment.setText(comment.getData());
                        if (comment.picUrl != null && comment.picUrl.startsWith("http")) {
                            Glide.with(context)
                                    .load(comment.picUrl)
                                    .apply(
                                            new RequestOptions().placeholder(R.drawable.ic_book_loading)
                                                    .error(R.drawable.ic_load_error)
                                                    .centerCrop()
                                    )
                                    .into(holder.ico);
                        } else {
                            holder.ico.setImageResource(R.drawable.ic_header);

                        }
                        return convertView;
                    }
                })
                //自定义评论布局(必须使用ViewHolder机制）
                // 并且自定义ViewHolder类必须继承自com.jidcoo.android.widget.commentview.view.ViewHolder
                // --CustomReplyItemCallback<R> 泛型R为自定义回复数据类
                .customReplyItem(new CustomReplyItemCallback<CustomCommentModel.CustomComment.CustomReply>() {
                    @Override
                    public View buildReplyItem(int groupPosition, int childPosition, boolean isLastReply, CustomCommentModel.CustomComment.CustomReply reply, LayoutInflater inflater, View convertView, ViewGroup parent) {
                        //使用方法就像adapter里面的getView()方法一样
                        //此类必须继承自com.jidcoo.android.widget.commentview.view.ViewHolder，否则报错
                        CustomReplyViewHolder holder=null;
                        //此类必须继承自com.jidcoo.android.widget.commentview.view.ViewHolder，否则报错
                        if(convertView==null){
                            //使用自定义布局
                            convertView=inflater.inflate(R.layout.custom_item_reply,parent,false);
                            holder=new CustomReplyViewHolder(convertView);
                            //必须使用ViewHolder机制
                            convertView.setTag(holder);
                        }else {
                            holder= (CustomReplyViewHolder) convertView.getTag();
                        }
                        holder.userName.setText(reply.getReplierName());
                        holder.reply.setText(reply.getData());
                        holder.prizes.setText("100");
                        return convertView;
                    }
                })
                //下拉刷新回调
                .setOnPullRefreshCallback(new MyOnPullRefreshCallback())
                //评论、回复Item的点击回调（点击事件回调）
                .setOnItemClickCallback(new MyOnItemClickCallback())
                //回复数据加载更多回调（加载更多回复）
                .setOnReplyLoadMoreCallback(new MyOnReplyLoadMoreCallback())
                //上拉加载更多回调（加载更多评论数据）
                .setOnCommentLoadMoreCallback(new MyOnCommentLoadMoreCallback())
                //设置完成后必须调用CallbackBuilder的buildCallback()方法，否则设置的回调无效
                .buildCallback();
//        load(1,1);

        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isReply){
                    //回复某条
                    Disposable disposable = RemoteRepository
                            .getInstance()
                            .addBookCommentReply(commentId,editor.getText().toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    (value) -> {
                                        load(1,1);
                                    }
                            );
                    addDisposable(disposable);
                }else {
                    //书籍评论
                    Disposable disposable = RemoteRepository
                            .getInstance()
                            .addBookComment(bookId,editor.getText().toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    (value) -> {
                                        load(1,1);
                                    }
                            );
                    addDisposable(disposable);
                }
                isReply = false;
            }
        });

        load(1,1);
    }

    private void load(int code, int handlerId){
        Disposable disposable = RemoteRepository
                .getInstance()
                .getBookCommentList(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (value) -> {
                            Message message = Message.obtain();
                            message.what = handlerId;
                            message.obj = transToModel(value);
                            activityHandler.sendMessage(message);
                        }
                );
        addDisposable(disposable);

//        localServer.get(code,activityHandler,handlerId);
    }


    /**
     * 下拉刷新回调类
     */
    class MyOnPullRefreshCallback implements OnPullRefreshCallback {

        @Override
        public void refreshing() {
            load(1,2);


        }

        @Override
        public void complete() {
            //加载完成后的操作
        }

        @Override
        public void failure(String msg) {

        }
    }




    /**
     * 上拉加载更多回调类
     */
    class MyOnCommentLoadMoreCallback implements OnCommentLoadMoreCallback {

        @Override
        public void loading(int currentPage, int willLoadPage, boolean isLoadedAllPages) {
            //因为测试数据写死了，所以这里的逻辑也是写死的
//            if (!isLoadedAllPages){
//                if(willLoadPage==2){
//                    load(2,3);
//                }else if(willLoadPage==3){
//                    load(3,3);
//                }
//            }

            Disposable disposable = RemoteRepository
                    .getInstance()
                    .getBookCommentList("1488053877507973120")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (value) -> {

                            }
                    );
            addDisposable(disposable);
        }

        @Override
        public void complete() {
            //加载完成后的操作
        }

        @Override
        public void failure(String msg) {
        }
    }

    /**
     * 回复加载更多回调类
     */
    class MyOnReplyLoadMoreCallback implements OnReplyLoadMoreCallback<CustomCommentModel.CustomComment.CustomReply> {


        @Override
        public void loading(CustomCommentModel.CustomComment.CustomReply reply, int willLoadPage) {
            if(willLoadPage==2){
                load(5,4);
            }else if(willLoadPage==3){
                load(6,4);
            }
        }

        @Override
        public void complete() {

        }

        @Override
        public void failure(String msg) {

        }
    }

    /**
     * 点击事件回调
     */
    class MyOnItemClickCallback implements OnItemClickCallback<CustomCommentModel.CustomComment, CustomCommentModel.CustomComment.CustomReply> {

        @Override
        public void commentItemOnClick(int position, CustomCommentModel.CustomComment comment, View view) {
            isReply = true;
            commentId = comment.commentId;
            editor.setHint("回复@" + comment.getPosterName() + ":");
        }

        @Override
        public void replyItemOnClick(int c_position, int r_position, CustomCommentModel.CustomComment.CustomReply reply, View view) {
            editor.setHint("回复@" + reply.getReplierName() + ":");
        }
    }

    private CustomCommentModel transToModel(List<BookComment> commentList){
        CustomCommentModel model = new CustomCommentModel();
        List<CustomCommentModel.CustomComment> comments = new ArrayList<>();
        for(int i=0; i<commentList.size(); i++) {
            CustomCommentModel.CustomComment customComment = new CustomCommentModel.CustomComment();
            customComment.data = commentList.get(i).getCommentContent();
            customComment.posterName = commentList.get(i).getCreateUserName();
            customComment.picUrl = commentList.get(i).getCreateUserPhoto();
            customComment.commentId = commentList.get(i).getId().toString();
            List<CommentReply> replyList = commentList.get(i).getReplies();
            for(int j = 0; j < replyList.size(); j++) {
                CustomCommentModel.CustomComment.CustomReply customReply = new CustomCommentModel.CustomComment.CustomReply();
                customReply.setData(replyList.get(j).getReplyContent());
                customReply.setReplierName(replyList.get(j).getNickName());
                customComment.replies.add(customReply);
            }
            comments.add(customComment);
        }
        model.comments = comments;
        return model;
    }

}

