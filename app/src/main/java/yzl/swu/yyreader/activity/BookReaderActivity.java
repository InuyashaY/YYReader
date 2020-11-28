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
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yzl.swu.yyreader.R;
import yzl.swu.yyreader.R2;
import yzl.swu.yyreader.adapter.ReadChaptersAdapter;
import yzl.swu.yyreader.utils.FileManager;
import yzl.swu.yyreader.views.YPageView;

public class BookReaderActivity extends AppCompatActivity {
    @BindView(R.id.read_drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.mPageView)
    YPageView yReadView;
    @BindView(R.id.read_bv_category)
    TextView categoryTextView;
    @BindView(R.id.read_rv_category)
    RecyclerView chaptersRecyclerView;

    public static void show(Context context){
        Intent intent = new Intent(context,BookReaderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);
        ButterKnife.bind(this);
        FileManager.getInstance().listTxtFiles();
        try {
            File bookFile = FileManager.getInstance().getFileByFilePath("斗罗大陆.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(bookFile),"utf-8"));
            String contenet = br.readLine();
            String ttt = "";
            int lines = 100;
            while(lines>0){
                if ((contenet=br.readLine()) != null) ttt+=contenet;
                lines--;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**************************init*******************************/
    private void initChapterRecyclerView(){
        chaptersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //选中章节
        chaptersRecyclerView.setAdapter(new ReadChaptersAdapter(yReadView.getPageLoader().mChapterList, new ReadChaptersAdapter.OnChapterClickListener() {
            @Override
            public void onItemClick(int pos) {
                yReadView.getPageLoader();
            }
        }));
    }


    /**************************Event*******************************/

    //目录
    @OnClick(R.id.read_bv_category)
    public void openChaptersCategory(View view){
        initChapterRecyclerView();
        mDrawerLayout.openDrawer(Gravity.START);
    }

    //亮度
    @OnClick(R.id.read_bv_brightness)
    public void changeBrightness(){

    }

    //夜间
    @OnClick(R.id.read_bv_night_mode)
    public void changeLightMode(){

    }

    //设置
    @OnClick(R.id.read_bv_setting)
    public void openReadSetting(){

    }


    /**************************common method*******************************/

}