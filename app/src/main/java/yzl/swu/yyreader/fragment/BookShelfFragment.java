package yzl.swu.yyreader.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.activity.BookReaderActivity;
import yzl.swu.yyreader.activity.MainActivity;
import yzl.swu.yyreader.adapter.BookShelfAdapter;
import yzl.swu.yyreader.adapter.SpaceItemDecoration;
import yzl.swu.yyreader.common.ContentScaleAnimation;
import yzl.swu.yyreader.common.Rotate3DAnimation;
import yzl.swu.yyreader.models.BookModel;

public class BookShelfFragment extends Fragment implements Animation.AnimationListener,BookShelfAdapter.OnBookClickListener{

    private final String TAG = "OpenBookActivity";

    private RecyclerView mRecyclerView;
    private BookShelfAdapter mAdapter;
    // 资源文件列表
    private List<BookModel> bookModels = new ArrayList<>();
    // 记录View的位置
    private int[] location = new int[2];
    // 内容页
    private ImageView mContent;
    // 封面
    private ImageView mFirst;
    // 缩放动画
    private ContentScaleAnimation scaleAnimation;
    // 3D旋转动画
    private Rotate3DAnimation threeDAnimation;
    // 状态栏的高度
    private int statusHeight;
    //toolbar高度
    private int toolBarHeight;
    // 是否打开书籍 其实是是否离开当前界面，跳转到其他的界面
    private boolean isOpenBook = false;
    //handler消费
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 111:
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) getActivity().findViewById(R.id.mToolBar).getLayoutParams();
                    layoutParams.height = 0;
                    getActivity().findViewById(R.id.mToolBar);
                    break;
            }
        }
    };

//    public static void show(Context context) {
//        Intent intent = new Intent(context, OpenBookActivity.class);
//        context.startActivity(intent);
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载布局
        View inflateView = inflater.inflate(R.layout.book_shelf_fragment,container,false);

        initWidget(inflateView);

        return inflateView;
    }

    private void initWidget(View inflateView) {

        mRecyclerView = inflateView.findViewById(R.id.mShelfRecycleView);
        mContent = inflateView.findViewById(R.id.bookContent);
        mFirst = inflateView.findViewById(R.id.bookCover);
        // 获取状态栏高度
        statusHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusHeight = getResources().getDimensionPixelSize(resourceId);
            //statusHeight = 171;
        }
        toolBarHeight = getActivity().findViewById(R.id.mToolBar).getHeight();
        initData();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),3));
        mAdapter = new BookShelfAdapter(bookModels,this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(10));
    }


    @Override
    public void onDestroyView() {
        //mRecyclerView.setAdapter(null);
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    // 重复添加数据
    private void initData() {
        for(int i = 0;i<10;i++){
            BookModel model = new BookModel("太古神王",R.drawable.tgsw,"未读");
            bookModels.add(model);
        }
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//
//        // 当界面重新进入的时候进行合书的动画
//        if(isOpenBook) {
//            scaleAnimation.reverse();
//            threeDAnimation.reverse();
//            mFirst.clearAnimation();
//            mFirst.startAnimation(threeDAnimation);
//            mContent.clearAnimation();
//            mContent.startAnimation(scaleAnimation);
//        }
//    }


    @Override
    public void onStart() {
        super.onStart();
        // 当界面重新进入的时候进行合书的动画
        if(isOpenBook) {
            scaleAnimation.reverse();
            threeDAnimation.reverse();
            mFirst.clearAnimation();
            mFirst.startAnimation(threeDAnimation);
            mContent.clearAnimation();
            mContent.startAnimation(scaleAnimation);
            getActivity().findViewById(R.id.mToolBar).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.mBottomBar).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(scaleAnimation.hasEnded() && threeDAnimation.hasEnded()) {
            // 两个动画都结束的时候再处理后续操作
            if (!isOpenBook) {
                isOpenBook = true;
                BookReaderActivity.show(this.getContext());
                int a = getActivity().findViewById(R.id.mBottomBar).getHeight();
                Log.v("yzll","${}"+getActivity().findViewById(R.id.mBottomBar).getHeight());
            } else {
                isOpenBook = false;
                mFirst.clearAnimation();
                mContent.clearAnimation();
                mFirst.setVisibility(View.GONE);
                mContent.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onItemClick(int pos,View view) {
        mFirst.setVisibility(View.VISIBLE);
        mContent.setVisibility(View.VISIBLE);

        //隐藏toolabr和bottombar
        getActivity().findViewById(R.id.mToolBar).setVisibility(View.GONE);
        getActivity().findViewById(R.id.mBottomBar).setVisibility(View.GONE);

        // 计算当前的位置坐标
        //view.getLocationInWindow(location);
        view.getLocationOnScreen(location);
        int l = view.getLeft();
        int t = view.getTop();

        int width = view.getWidth();
        int height = view.getHeight();


        // 两个ImageView设置大小和位置
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mFirst.getLayoutParams();
        params.leftMargin = location[0];
        params.topMargin = location[1] - statusHeight - toolBarHeight;
        params.width = width;
        params.height = height;
        mFirst.setLayoutParams(params);
        mContent.setLayoutParams(params);

        //mContent = new ImageView(MainActivity.this);
        Bitmap contentBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        contentBitmap.eraseColor(getResources().getColor(R.color.read_theme_yellow));
        mContent.setImageBitmap(contentBitmap);

        // mCover = new ImageView(MainActivity.this);
        Bitmap coverBitmap = BitmapFactory.decodeResource(getResources(), bookModels.get(pos).getCoverResource());
        mFirst.setImageBitmap(coverBitmap);

        initAnimation(view);
        Log.v(TAG,"left:"+mFirst.getLeft()+"top:"+mFirst.getTop());

        mContent.clearAnimation();
        mContent.startAnimation(scaleAnimation);
        mFirst.clearAnimation();
        mFirst.startAnimation(threeDAnimation);
    }

    // 初始化动画
    private void initAnimation(View view) {
        float viewWidth = view.getWidth();
        float viewHeight = view.getHeight();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float maxWidth = displayMetrics.widthPixels;
        float maxHeight = displayMetrics.heightPixels+statusHeight+toolBarHeight;
        float horScale = maxWidth / viewWidth;
        float verScale = maxHeight / viewHeight;
        float scale = horScale > verScale ? horScale : verScale;
        int aaa = getContext().getResources().getDisplayMetrics().heightPixels;

        scaleAnimation = new ContentScaleAnimation(location[0], location[1], scale, false);
        scaleAnimation.setInterpolator(new DecelerateInterpolator());  //设置插值器
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);  //动画停留在最后一帧
        scaleAnimation.setAnimationListener(this);

        threeDAnimation = new Rotate3DAnimation(getContext(), -180, 0
                , location[0], location[1]-20, scale, true);
        threeDAnimation.setDuration(1000);                         //设置动画时长
        threeDAnimation.setFillAfter(true);                        //保持旋转后效果
        threeDAnimation.setInterpolator(new DecelerateInterpolator());
    }
}
