package yzl.swu.yyreader.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.activity.BookReaderActivity;
import yzl.swu.yyreader.activity.FileSelectorActivity;
import yzl.swu.yyreader.activity.MainActivity;
import yzl.swu.yyreader.adapter.BookShelfAdapter;
import yzl.swu.yyreader.adapter.SpaceItemDecoration;
import yzl.swu.yyreader.common.ContentScaleAnimation;
import yzl.swu.yyreader.common.Rotate3DAnimation;
import yzl.swu.yyreader.databinding.BookShelfFragmentBinding;
import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.utils.Utils;

import static yzl.swu.yyreader.common.Constants.FIELSELECTOR_RESULT_KEY;
import static yzl.swu.yyreader.common.Constants.FILESELECTOR_RESULT_CODE;
import static yzl.swu.yyreader.common.Constants.MAINACTIVITY_REQUEST_CODE;
import static yzl.swu.yyreader.common.Constants.READBOOK_KEY;

public class BookShelfFragment extends BaseFragment<BookShelfFragmentBinding> implements Animation.AnimationListener,BookShelfAdapter.OnBookClickListener{

    private final String TAG = "OpenBookActivity";


    private BookShelfAdapter mAdapter;
    // 资源文件列表
    private List<BookModel> bookModels;
    // 记录View的位置
    private int[] location = new int[2];
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
    //选中的书籍
    private int selectedIndex = 0;
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

    protected void initWidget() {
        // 获取状态栏高度
        statusHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusHeight = getResources().getDimensionPixelSize(resourceId);
            //statusHeight = 171;
        }
        toolBarHeight = viewBinding.mToolBar.getHeight();
        initData();
        viewBinding.mShelfRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(),3));
        mAdapter = new BookShelfAdapter(bookModels,this,getContext());
        viewBinding.mShelfRecycleView.setAdapter(mAdapter);
        viewBinding.mShelfRecycleView.addItemDecoration(new SpaceItemDecoration(10));

        viewBinding.mToolBar.getImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }


    @Override
    public void onDestroyView() {
        //mRecyclerView.setAdapter(null);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // 重复添加数据
    private void initData() {
        //从数据库查询已添加的书籍
        bookModels = LitePal.findAll(BookModel.class);
        System.out.println();
//        for(int i = 0;i<10;i++){
//            BookModel model = new BookModel("斗罗大陆",R.drawable.tgsw,"未读","/storage/emulated/0/斗罗大陆.txt");
//            bookModels.add(model);
//        }
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
            viewBinding.bookCover.clearAnimation();
            viewBinding.bookCover.startAnimation(threeDAnimation);
            viewBinding.bookContent.clearAnimation();
            viewBinding.bookContent.startAnimation(scaleAnimation);
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
//                BookReaderActivity.show(this.getContext());
                Intent intent = new Intent(getActivity(),BookReaderActivity.class);
                intent.putExtra(READBOOK_KEY,bookModels.get(selectedIndex));
                startActivity(intent);
                int a = getActivity().findViewById(R.id.mBottomBar).getHeight();
                Log.v("yzll","${}"+getActivity().findViewById(R.id.mBottomBar).getHeight());
            } else {
                isOpenBook = false;
                viewBinding.bookCover.clearAnimation();
                viewBinding.bookContent.clearAnimation();
                viewBinding.bookCover.setVisibility(View.GONE);
                viewBinding.bookContent.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onItemClick(int pos,View view) {
        viewBinding.bookCover.setVisibility(View.VISIBLE);
        viewBinding.bookContent.setVisibility(View.VISIBLE);
        selectedIndex = pos;

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
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewBinding.bookCover.getLayoutParams();
        params.leftMargin = location[0];
        params.topMargin = location[1] - statusHeight - toolBarHeight;
        params.width = width;
        params.height = height;
        viewBinding.bookCover.setLayoutParams(params);
        viewBinding.bookContent.setLayoutParams(params);

        //mContent = new ImageView(MainActivity.this);
        Bitmap contentBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        contentBitmap.eraseColor(getResources().getColor(R.color.read_theme_yellow));
        viewBinding.bookContent.setImageBitmap(contentBitmap);

        // mCover = new ImageView(MainActivity.this);
        Bitmap coverBitmap = BitmapFactory.decodeResource(getResources(), Utils.getImageid(getContext(),bookModels.get(pos).getCoverResource()));
        viewBinding.bookCover.setImageBitmap(coverBitmap);

        initAnimation(view);
        Log.v(TAG,"left:"+viewBinding.bookCover.getLeft()+"top:"+viewBinding.bookCover.getTop());

        viewBinding.bookContent.clearAnimation();
        viewBinding.bookContent.startAnimation(scaleAnimation);
        viewBinding.bookCover.clearAnimation();
        viewBinding.bookCover.startAnimation(threeDAnimation);
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

    //添加新书
    public void addBooks(List<BookModel> models){
        bookModels.addAll(0,models);
        mAdapter.notifyDataSetChanged();
    }

    //刷新数据
    public void refreshData(){
        bookModels = LitePal.findAll(BookModel.class);
        mAdapter.refreshModels(bookModels);
    }


    public void showDialog(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_more_setting,null,false);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(view).create();

        //整理书架
        view.findViewById(R.id.classfy).setOnClickListener((v) ->{

        });
        //下载
        view.findViewById(R.id.txtDownload).setOnClickListener((v) ->{

        });
        //本地导入
        view.findViewById(R.id.localBook).setOnClickListener((v)->{
//            FileSelectorActivity.show(this);
            Intent intent = new Intent(getActivity(), FileSelectorActivity.class);
            startActivityForResult(intent,MAINACTIVITY_REQUEST_CODE);
            alertDialog.dismiss();
        });
        //取消
        view.findViewById(R.id.more_cancel).setOnClickListener((v)->{
            alertDialog.dismiss();
        });

        //显示弹窗
        alertDialog.show();

        //自定义的东西
        //放在show()之后，不然有些属性是没有效果的，比如height和width
        Window dialogWindow = alertDialog.getWindow();
        Display d = dialogWindow.getWindowManager().getDefaultDisplay();

        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.gravity = Gravity.BOTTOM;//设置位置
        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度

//        p.alpha = 0.8f;//设置透明度
        dialogWindow.setAttributes(p);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == MAINACTIVITY_REQUEST_CODE && resultCode == FILESELECTOR_RESULT_CODE) {
            List<BookModel> resultModels = data.getParcelableArrayListExtra(FIELSELECTOR_RESULT_KEY);
            if(!resultModels.isEmpty()){
                Fragment navFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.main_fragment);
                Fragment shelfFragment = navFragment.getChildFragmentManager().getPrimaryNavigationFragment();
                if (shelfFragment instanceof BookShelfFragment) ((BookShelfFragment) shelfFragment).addBooks(resultModels);
                //fragment.addBooks(resultModels);
                //保存添加的文件
                LitePal.saveAll(resultModels);
            }
        }
    }

}
