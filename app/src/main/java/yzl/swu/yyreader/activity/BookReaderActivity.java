package yzl.swu.yyreader.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.R2;
import yzl.swu.yyreader.adapter.ReadChaptersAdapter;
import yzl.swu.yyreader.databinding.ActivityBookReaderBinding;
import yzl.swu.yyreader.utils.FileManager;
import yzl.swu.yyreader.views.BrightnessSettingDialog;
import yzl.swu.yyreader.views.ReadSettingDialog;
import yzl.swu.yyreader.views.YPageView;

public class BookReaderActivity extends BaseActivity<ActivityBookReaderBinding> {

    boolean isShowSetting = false;
    //设置Dialog
    ReadSettingDialog readSettingDialog;
    //亮度设置Dialog
    BrightnessSettingDialog brightnessSettingDialog;

    public static void show(Context context){
        Intent intent = new Intent(context,BookReaderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWidgets();
        initEvents();

    }

    /**************************init*******************************/
    private void initWidgets(){

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
                int chapterIndex = (int)(process/100f*(viewBinding.mPageView.getPageLoader().mChapterList.size()-1));
                viewBinding.mPageView.getPageLoader().skipToChapter(chapterIndex);
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
            viewBinding.mPageView.getPageLoader().nextChapter();
        });

        //上一章
        viewBinding.readBvPreChapter.setOnClickListener((v)->{

            viewBinding.mPageView.getPageLoader().preChapter();
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
        viewBinding.readRvCategory.setAdapter(new ReadChaptersAdapter(viewBinding.mPageView.getPageLoader().mChapterList, new ReadChaptersAdapter.OnChapterClickListener() {
            @Override
            public void onItemClick(int pos) {
                viewBinding.mPageView.getPageLoader().skipToChapter(pos);
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
        readSettingDialog = new ReadSettingDialog(this,viewBinding.mPageView.getPageLoader());
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