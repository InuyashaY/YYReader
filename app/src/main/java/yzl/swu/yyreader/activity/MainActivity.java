package yzl.swu.yyreader.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.common.Constants;
import yzl.swu.yyreader.databinding.ActivityMainBinding;
import yzl.swu.yyreader.fragment.BookShelfFragment;
import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.utils.FileManager;

import static yzl.swu.yyreader.common.Constants.FIELSELECTOR_RESULT_KEY;
import static yzl.swu.yyreader.common.Constants.FILESELECTOR_RESULT_CODE;
import static yzl.swu.yyreader.common.Constants.FIRST_KEY;
import static yzl.swu.yyreader.common.Constants.MAINACTIVITY_REQUEST_CODE;


public class MainActivity extends BaseActivity<ActivityMainBinding>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBottomBar();


//        this.setSupportActionBar(viewBinding.mToolBar);


        //获取存储权限
        isStoragePermissionGranted();
        //数据库 LitePal
        LitePal.initialize(this);
        SQLiteDatabase db = LitePal.getDatabase();


        //初始化默认书籍
        initDefaultBooks();
    }

    private void initDefaultBooks() {
        //定义一个setting记录APP是几次启动
        SharedPreferences setting = getSharedPreferences(Constants.IS_FIRST, 0);
        Boolean user_first = setting.getBoolean(FIRST_KEY, true);
        if (user_first) {
            // 第一次则将raw中文件写入内部存储
            setting.edit().putBoolean(FIRST_KEY, false).commit();
            generateBooks();
        }
    }

    //将raw文件写入存储
    private void generateBooks(){
        FileManager.getInstance().copyFilesFromRaw(this,R.raw.dldl,"斗罗大陆.txt",getFilesDir().getAbsolutePath() + "/" + "yzl");
        BookModel model = new BookModel("斗罗大陆","dldl","未读",getFilesDir().getAbsolutePath() + "/" + "yzl"+"/斗罗大陆.txt",true);
        FileManager.getInstance().copyFilesFromRaw(this,R.raw.dpcq,"斗破苍穹.txt",getFilesDir().getAbsolutePath() + "/" + "yzl");
        BookModel model1 = new BookModel("斗破苍穹","dpcq","未读",getFilesDir().getAbsolutePath() + "/" + "yzl"+"/斗破苍穹.txt",true);
        FileManager.getInstance().copyFilesFromRaw(this,R.raw.jl,"捡漏.txt",getFilesDir().getAbsolutePath() + "/" + "yzl");
        BookModel model2 = new BookModel("捡漏","jl","未读",getFilesDir().getAbsolutePath() + "/" + "yzl"+"/捡漏.txt",true);
        FileManager.getInstance().copyFilesFromRaw(this,R.raw.jswh,"绝世武魂.txt",getFilesDir().getAbsolutePath() + "/" + "yzl");
        BookModel model3 = new BookModel("绝世武魂","jswh","未读",getFilesDir().getAbsolutePath() + "/" + "yzl"+"/绝世武魂.txt",true);
        FileManager.getInstance().copyFilesFromRaw(this,R.raw.fts,"伏天氏.txt",getFilesDir().getAbsolutePath() + "/" + "yzl");
        BookModel model4 = new BookModel("伏天氏","fts","未读",getFilesDir().getAbsolutePath() + "/" + "yzl"+"/伏天氏.txt",true);
        FileManager.getInstance().copyFilesFromRaw(this,R.raw.dzz,"大主宰.txt",getFilesDir().getAbsolutePath() + "/" + "yzl");
        BookModel model5 = new BookModel("大主宰","dzz","未读",getFilesDir().getAbsolutePath() + "/" + "yzl"+"/大主宰.txt",true);

        BookModel model6 = new BookModel("武神霸尊","dzz","未读","book/1488053877507973120/1488058771568648192",false);
        model6.setId(1488053877507973120l);


        ArrayList<BookModel> bookModels = new ArrayList<>();
        bookModels.add(model);
        bookModels.add(model1);
        bookModels.add(model2);
        bookModels.add(model3);
        bookModels.add(model4);
        bookModels.add(model5);
        bookModels.add(model6);

        LitePal.saveAll(bookModels);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Fragment navFragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        Fragment shelfFragment = navFragment.getChildFragmentManager().getPrimaryNavigationFragment();
        if (shelfFragment instanceof BookShelfFragment) ((BookShelfFragment) shelfFragment).refreshData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

//

    //初始化BottomBar
    private void initBottomBar(){
//        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
//        //添加Item选项
//        bottomNavigationBar
//                .addItem(new BottomNavigationItem(R.drawable.bookselected,"书架").setInactiveIconResource(R.drawable.books))
//                .addItem(new BottomNavigationItem(R.drawable.bookstoreselect,"书城").setInactiveIconResource(R.drawable.bookstore))
//                .addItem(new BottomNavigationItem(R.drawable.meselect,"我的").setInactiveIconResource(R.drawable.me))
//                .setFirstSelectedPosition(0)
//                .initialise();

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.main_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(viewBinding.mBottomBar, navController);

    }




    //获取存储权限
    public boolean isStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {
            final Context context = getApplicationContext();
            int readPermissionCheck = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermissionCheck = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (readPermissionCheck == PackageManager.PERMISSION_GRANTED
                    && writePermissionCheck == PackageManager.PERMISSION_GRANTED) {
                Log.v("juno", "Permission is granted");
                return true;
            } else {
                Log.v("juno", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("juno", "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //底部栏监听事件
//    @Override
//    public void onTabSelected(int position) {
//        Log.v("yzll",""+position);
//        Fragment fragment = null;
//        switch (position){
//            case 0:
//                fragment = new BookShelfFragment();
//                break;
//            case 1:
//                fragment = new BookStoreFragment();
//                break;
//            case 2:
//                fragment = new SelfFragment();
//                break;
//            default:
//                break;
//        }
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.main_fragment,fragment);
//        fragmentTransaction.commit();
//    }
//    @Override
//    public void onTabUnselected(int position) {
//
//    }
//    @Override
//    public void onTabReselected(int position) {
//
//    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == MAINACTIVITY_REQUEST_CODE && resultCode == FILESELECTOR_RESULT_CODE) {
            List<BookModel> resultModels = data.getParcelableArrayListExtra(FIELSELECTOR_RESULT_KEY);
            if(!resultModels.isEmpty()){
                Fragment navFragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
                Fragment shelfFragment = navFragment.getChildFragmentManager().getPrimaryNavigationFragment();
                if (shelfFragment instanceof BookShelfFragment) ((BookShelfFragment) shelfFragment).addBooks(resultModels);
                //fragment.addBooks(resultModels);
                //保存添加的文件
                LitePal.saveAll(resultModels);
            }
        }
    }

}