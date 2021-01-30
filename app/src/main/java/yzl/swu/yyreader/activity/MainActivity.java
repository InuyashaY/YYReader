package yzl.swu.yyreader.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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

import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.databinding.ActivityMainBinding;
import yzl.swu.yyreader.fragment.BookShelfFragment;
import yzl.swu.yyreader.models.BookModel;

import static yzl.swu.yyreader.common.Constants.FIELSELECTOR_RESULT_KEY;
import static yzl.swu.yyreader.common.Constants.FILESELECTOR_RESULT_CODE;
import static yzl.swu.yyreader.common.Constants.MAINACTIVITY_REQUEST_CODE;


public class MainActivity extends BaseActivity<ActivityMainBinding>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBottomBar();


//        this.setSupportActionBar(viewBinding.mToolBar);
        viewBinding.mToolBar.getImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //数据库 LitePal

        LitePal.initialize(this);
        SQLiteDatabase db = LitePal.getDatabase();

        isStoragePermissionGranted();
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
    public void showDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_more_setting,null,false);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();

        //整理书架
        view.findViewById(R.id.classfy).setOnClickListener((v) ->{

        });
        //下载
        view.findViewById(R.id.txtDownload).setOnClickListener((v) ->{

        });
        //本地导入
        view.findViewById(R.id.localBook).setOnClickListener((v)->{
//            FileSelectorActivity.show(this);
            Intent intent = new Intent(MainActivity.this,FileSelectorActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == MAINACTIVITY_REQUEST_CODE && resultCode == FILESELECTOR_RESULT_CODE) {
            List<BookModel> resultModels = data.getParcelableArrayListExtra(FIELSELECTOR_RESULT_KEY);
            Fragment navFragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            Fragment shelfFragment = navFragment.getChildFragmentManager().getPrimaryNavigationFragment();
            if (shelfFragment instanceof BookShelfFragment) ((BookShelfFragment) shelfFragment).addBooks(resultModels);
            //fragment.addBooks(resultModels);
        }
    }

}