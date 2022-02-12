package yzl.swu.yyreader.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import org.litepal.LitePal;

import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.adapter.ReadChaptersAdapter;
import yzl.swu.yyreader.common.AnimType;
import yzl.swu.yyreader.databinding.ActivityBookReaderBinding;
import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.models.BookRecordModel;
import yzl.swu.yyreader.models.TxtChapterModel;
import yzl.swu.yyreader.views.BrightnessSettingDialog;
import yzl.swu.yyreader.views.PageLoader;
import yzl.swu.yyreader.views.ReadSettingDialog;
import yzl.swu.yyreader.views.YPageView;

import static yzl.swu.yyreader.common.Constants.READBOOK_KEY;

public class BookReaderActivity extends BaseActivity<ActivityBookReaderBinding> {

    boolean isShowSetting = false;
    //设置Dialog
    ReadSettingDialog readSettingDialog;
    //亮度设置Dialog
    BrightnessSettingDialog brightnessSettingDialog;
    //书籍
    BookModel mBookModel;
    //页面加载器
    private PageLoader mPageLoader;
    //是否为夜间模式
    private boolean isNightMode = false;
    //目录适配器
    ReadChaptersAdapter readChaptersAdapter;

//    public static void show(Context context,BookModel model){
//        Intent intent = new Intent(context,BookReaderActivity.class);
//
//        context.startActivity(intent);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mBookModel = getIntent().getParcelableExtra(READBOOK_KEY);
        initWidgets();
        initEvents();
    }


    //保存阅读记录
    @Override
    protected void onPause() {
        super.onPause();
        if (LitePal.where("book_id=?",String.valueOf(mBookModel.getId())).findFirst(TxtChapterModel.class) == null){
            LitePal.saveAll(mPageLoader.mChapterList);
        }
//        ContentValues values = new ContentValues();
//        values.put("chapterpos",mPageLoader.getCurChapterIndex());
//        values.put("pagepos",mPageLoader.getCurPageIndex());
//        LitePal.updateAll(BookRecordModel.class,values,"book_id=?",mBookModel.getId());
        BookRecordModel bookRecord = new BookRecordModel();
        bookRecord.setChapterPos(mPageLoader.getCurChapterIndex());
        bookRecord.setPagePos(mPageLoader.getCurPageIndex());
        bookRecord.setBook_id(String.valueOf(mBookModel.getId()));
        bookRecord.setPage_color(mPageLoader.getBgColor());
        bookRecord.setText_color(mPageLoader.getTextColor());
        bookRecord.setText_size(mPageLoader.getmTextSize());
        bookRecord.setAnim_type(viewBinding.mPageView.getAnimType().getType());
        bookRecord.saveOrUpdate("book_id=?",String.valueOf(mBookModel.getId()));

        ContentValues values = new ContentValues();
        values.put("record","已阅读至第"+mPageLoader.getCurChapterIndex()+"章");
        mBookModel.setRecord("已阅读至第"+mPageLoader.getCurChapterIndex()+"章");
        LitePal.update(BookModel.class,values,mBookModel.getId());
    }

    /**************************init*******************************/
    private void initWidgets(){
        int width = viewBinding.mPageView.getWidth();
        //书籍
        mPageLoader = viewBinding.mPageView.getPageLoader(mBookModel);

        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //从数据库加载分章信息
        mPageLoader.mChapterList = LitePal.where("book_id=?",String.valueOf(mBookModel.getId()))
                .find(TxtChapterModel.class);

        //获取上次的数据
        BookRecordModel recordModel = LitePal.where("book_id=?",String.valueOf(mBookModel.getId())).findFirst(BookRecordModel.class);
        if (recordModel != null){
            mPageLoader.setCurChapterIndex(recordModel.getChapterPos());
            mPageLoader.setCurPageIndex(recordModel.getPagePos());
            mPageLoader.setTextSize(recordModel.getText_size());
//            mPageLoader.setPageStyle(recordModel.getPage_color(),recordModel.getText_color());
            mPageLoader.setTextColor(recordModel.getText_color());
            mPageLoader.setPageBgColor(recordModel.getPage_color());
            switch (recordModel.getAnim_type()){
                case 1:
                    viewBinding.mPageView.setAnimType(AnimType.SLIDE);
                    break;
                case 2:
                    viewBinding.mPageView.setAnimType(AnimType.COVER);
                    break;
                case 3:
                    viewBinding.mPageView.setAnimType(AnimType.ALIKE);
                    break;
                default:
                    viewBinding.mPageView.setAnimType(AnimType.NONE);
                    break;
            }
        }

        //初始化目录
        initChapterRecyclerView();
    }

    private void initEvents(){
        //进度条
        viewBinding.readSbChapterProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int process = seekBar.getProgress();
                int chapterIndex = (int)(process/100f*(mPageLoader.mChapterList.size()-1));
                mPageLoader.skipToChapter(chapterIndex);
            }
        });

        //目录
        viewBinding.readBvCategory.setOnClickListener((v)->{
            openChaptersCategory();
        });

        //亮度
        viewBinding.readBvBrightness.setOnClickListener((v)->{
            changeBrightness();
        });

        //夜间模式
        viewBinding.readBvNightMode.setOnClickListener((v)-> {
            changeLightMode();
        });

        //设置
        viewBinding.readBvSetting.setOnClickListener((v)-> {
            openReadSetting();
        });

        //下一章
        viewBinding.readBvNextChapter.setOnClickListener((v)->{
            mPageLoader.nextChapter();
        });

        //上一章
        viewBinding.readBvPreChapter.setOnClickListener((v)->{

            mPageLoader.preChapter();
        });

        //退出
        viewBinding.readToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //中心区域被点击   显示菜单
        viewBinding.mPageView.setOnCenterClickListener(new YPageView.OnCenterClickListener() {
            @Override
            public void centerClicked() {
                if (isShowSetting){
                    viewBinding.readBottomMenu.setVisibility(View.GONE);
                    viewBinding.readTopAppBar.setVisibility(View.GONE);
                }else {
                    viewBinding.readBottomMenu.setVisibility(View.VISIBLE);
                    viewBinding.readTopAppBar.setVisibility(View.VISIBLE);
                }
                isShowSetting = !isShowSetting;
            }
        });
    }


    //目录显示准备 点击回调
    private void initChapterRecyclerView(){
        viewBinding.readRvCategory.setLayoutManager(new LinearLayoutManager(this));

        readChaptersAdapter = new ReadChaptersAdapter(mPageLoader.mChapterList,this, new ReadChaptersAdapter.OnChapterClickListener() {
            @Override
            public void onItemClick(int pos) {
                mPageLoader.skipToChapter(pos);
            }
        });

        //章节回调
        mPageLoader.setChapterChangeListener(new PageLoader.OnChapterChangeListener() {
            @Override
            public void onChapterChange(int pos) {
                readChaptersAdapter.setChapter(pos);
            }

            @Override
            public void requestChapters(List<TxtChapterModel> requestChapters) {

            }

            @Override
            public void finishedLoadChapters(List<TxtChapterModel> bookChapters) {
                mPageLoader.pageView.showCategory();
//                readChaptersAdapter.notifyDataSetChanged();
                readChaptersAdapter.setChapterModels(bookChapters);
            }
        });

        //选中章节
        viewBinding.readRvCategory.setAdapter(readChaptersAdapter);
    }


    /**************************Event*******************************/

    //目录
    public void openChaptersCategory(){
        viewBinding.readRvCategory.scrollToPosition(mPageLoader.getCurChapterIndex());
        viewBinding.readDrawer.openDrawer(Gravity.START);
    }

    //亮度
    public void changeBrightness(){
        brightnessSettingDialog =  new BrightnessSettingDialog(this,mPageLoader);
        brightnessSettingDialog.show();
    }

    //夜间
    public void changeLightMode(){
        Drawable drawable;

        if (isNightMode){
            drawable = getResources().getDrawable(R.drawable.ic_night);
            viewBinding.readBvNightMode.setText("夜间");
            mPageLoader.cancelColorChange();
        }else {
            int pageColor = Color.BLACK;
            int textColor = Color.WHITE;
            drawable = getResources().getDrawable(R.drawable.ic_daynight);
            mPageLoader.setPageStyle(pageColor,textColor);
            viewBinding.readBvNightMode.setText("日间");
        }

        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        viewBinding.readBvNightMode.setCompoundDrawables(null,drawable,null,null);

        isNightMode = !isNightMode;
    }

    //设置
    public void openReadSetting(){
        readSettingDialog = new ReadSettingDialog(this,mPageLoader);
        readSettingDialog.show();
    }


    //设置栏隐藏和显示
    private void changeMenuState(){
        if (!isShowSetting){
            viewBinding.readBottomMenu.setVisibility(View.GONE);
            viewBinding.readTopAppBar.setVisibility(View.GONE);
        }else {
            viewBinding.readBottomMenu.setVisibility(View.VISIBLE);
            viewBinding.readTopAppBar.setVisibility(View.VISIBLE);
        }
        isShowSetting = !isShowSetting;
    }

    /**************************common method*******************************/


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


}