package yzl.swu.yyreader.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.R2;
import yzl.swu.yyreader.adapter.ReadChaptersAdapter;
import yzl.swu.yyreader.databinding.ActivityBookReaderBinding;
import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.models.BookRecordModel;
import yzl.swu.yyreader.models.TxtChapterModel;
import yzl.swu.yyreader.utils.FileManager;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //LitePal.saveAll(mPageLoader.mChapterList);

    }

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
        bookRecord.setBook_id(mBookModel.getId());
        bookRecord.saveOrUpdate("book_id=?",String.valueOf(mBookModel.getId()));

        ContentValues values = new ContentValues();
        values.put("record","已阅读至第"+mPageLoader.getCurChapterIndex()+"章");
        mBookModel.setRecord("已阅读至第"+mPageLoader.getCurChapterIndex()+"章");
        LitePal.update(BookModel.class,values,mBookModel.getId());
    }

    /**************************init*******************************/
    private void initWidgets(){
        mPageLoader = viewBinding.mPageView.getPageLoader(mBookModel);

        //从数据库加载分章信息
        mPageLoader.mChapterList = LitePal.where("book_id=?",String.valueOf(mBookModel.getId()))
                .find(TxtChapterModel.class);

        //获取上次的数据
        BookRecordModel recordModel = LitePal.where("book_id=?",String.valueOf(mBookModel.getId())).findFirst(BookRecordModel.class);
        if (recordModel != null){
            mPageLoader.setCurChapterIndex(recordModel.getChapterPos());
            mPageLoader.setCurPageIndex(recordModel.getPagePos());

        }
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

        //中心区域被点击   显示菜单
        viewBinding.mPageView.setOnCenterClickListener(new YPageView.OnCenterClickListener() {
            @Override
            public void centerClicked() {
                if (!isShowSetting){
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
        //选中章节
        viewBinding.readRvCategory.setAdapter(new ReadChaptersAdapter(mPageLoader.mChapterList, new ReadChaptersAdapter.OnChapterClickListener() {
            @Override
            public void onItemClick(int pos) {
                mPageLoader.skipToChapter(pos);
            }
        }));
    }


    /**************************Event*******************************/

    //目录
    public void openChaptersCategory(){
        initChapterRecyclerView();
        viewBinding.readDrawer.openDrawer(Gravity.START);
    }

    //亮度
    public void changeBrightness(){
        brightnessSettingDialog =  new BrightnessSettingDialog(this);
        brightnessSettingDialog.show();
    }

    //夜间
    public void changeLightMode(){

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