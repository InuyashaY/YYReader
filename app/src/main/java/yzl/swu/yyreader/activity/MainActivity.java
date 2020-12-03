package yzl.swu.yyreader.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import yzl.swu.yyreader.R;
import yzl.swu.yyreader.common.YToolBar;
import yzl.swu.yyreader.databinding.ActivityMainBinding;


public class MainActivity extends BaseActivity<ActivityMainBinding>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBottomBar();


//        this.setSupportActionBar(toolbar);
        viewBinding.mToolBar.getImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("yzll","++++++");
            }
        });

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
}