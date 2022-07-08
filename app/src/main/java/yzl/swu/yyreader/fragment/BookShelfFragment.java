package yzl.swu.yyreader.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.hedgehog.ratingbar.RatingBar;

import org.litepal.LitePal;

import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.activity.BookReaderActivity;
import yzl.swu.yyreader.activity.FileSelectorActivity;
import yzl.swu.yyreader.adapter.BookShelfAdapter;
import yzl.swu.yyreader.adapter.SpaceItemDecoration;
import yzl.swu.yyreader.anim.ContentScaleAnimation;
import yzl.swu.yyreader.anim.Rotate3DAnimation;
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

    RatingBar mRatingBar;
    View view;
    int score = 0;
    Dialog tipDialog;
    Dialog ratingDialog;


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
//        toolBarHeight = viewBinding.mToolBar.getMeasuredHeight();
        toolBarHeight = getResources().getDimensionPixelSize(R.dimen.toolbar_height);
        initData();
        viewBinding.mShelfRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(),3));
        mAdapter = new BookShelfAdapter(bookModels,this,getContext());
        viewBinding.mShelfRecycleView.setAdapter(mAdapter);
        viewBinding.mShelfRecycleView.addItemDecoration(new SpaceItemDecoration(10));

        viewBinding.mToolBar.getImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDialog();
                Intent intent = new Intent(getActivity(), FileSelectorActivity.class);
                startActivityForResult(intent,MAINACTIVITY_REQUEST_CODE);
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

            if(!bookModels.get(selectedIndex).isLocal()){
                //评分
                prepareRating();

                //上传
            }
        }
    }

    private void prepareRating(){
        view=View.inflate(getContext(),R.layout.dialog_rating,null);
        mRatingBar = (RatingBar) view.findViewById(R.id.rat_test);
        //设置是否可点击，在需要评分的地方要设置为可点击
        mRatingBar.setmClickable(true);
        //设置星星总数
        mRatingBar.setStarCount(5);
        //设置星星的宽度
        mRatingBar.setStarImageWidth(40f);
        //设置星星的高度
        mRatingBar.setStarImageHeight(40f);
        //设置星星之间的距离
        mRatingBar.setImagePadding(5f);
        //设置空星星
        mRatingBar.setStarEmptyDrawable(getResources()
                .getDrawable(R.drawable.ic_rating_empty));
        //设置填充的星星
        mRatingBar.setStarFillDrawable(getResources()
                .getDrawable(R.drawable.ic_rating_full));
        //设置半颗星
        mRatingBar.setStarHalfDrawable(getResources()
                .getDrawable(R.drawable.ic_rating_half));
        //设置显示的星星个数
        mRatingBar.setStar(4.5f);
        //设置评分的监听
        mRatingBar.setOnRatingChangeListener(
                new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(float RatingCount) {
                        score = (int)(RatingCount/5*10);

                        Toast.makeText(getContext(), "你给出了"+
                                        score +"分",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        //提交
        view.findViewById(R.id.rating_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipDialog = DialogUIUtils.showMdAlert(getActivity(),"感谢您的评价","您的评分是："+score+"分", new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        if (tipDialog != null) tipDialog.dismiss();
                    }

                    @Override
                    public void onNegative() {
                        if (tipDialog != null) tipDialog.dismiss();
                    }
                }).show();
                if (ratingDialog != null) ratingDialog.dismiss();
            }
        });
        //q取消
        view.findViewById(R.id.rating_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingDialog != null) ratingDialog.dismiss();
            }
        });

        //弹窗
        ratingDialog = DialogUIUtils.showCustomAlert(getContext(),view).show();
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
        params.topMargin = location[1] - statusHeight;
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
                , location[0], location[1], scale, true);
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

        Dialog dialog = new Dialog(getContext(), R.style.MoreSettingDialog);
        dialog.show();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_more_setting, null);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new  ViewGroup.LayoutParams(width, height);
        dialog.setContentView(view, layoutParams);


        //整理书架
        view.findViewById(R.id.classfy).setOnClickListener((v) ->{

        });

        //下载
        view.findViewById(R.id.txtDownload).setOnClickListener((v) ->{

        });

        //本地导入
        view.findViewById(R.id.localBook).setOnClickListener((v)->{
            Intent intent = new Intent(getActivity(), FileSelectorActivity.class);
            startActivityForResult(intent,MAINACTIVITY_REQUEST_CODE);
            dialog.dismiss();
        });

        //取消
        view.findViewById(R.id.more_cancel).setOnClickListener((v)->{
            dialog.dismiss();
        });

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
