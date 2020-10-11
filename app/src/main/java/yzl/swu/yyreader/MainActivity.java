package yzl.swu.yyreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import yzl.swu.yyreader.fragment.BookShelfFragment;
import yzl.swu.yyreader.fragment.BookStoreFragment;
import yzl.swu.yyreader.fragment.SelfFragment;


public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    //底部栏
    private BottomNavigationBar bottomNavigationBar;
    //顶部栏
    private YToolBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        bottomNavigationBar  = findViewById(R.id.mBottomBar);
        toolbar = findViewById(R.id.mToolBar);
        initBottomBar();
        //设置监听者为this
        bottomNavigationBar.setTabSelectedListener(this);
        //设置菜单
        //setSupportActionBar(toolbar);
        toolbar.getImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("yzll","++++++");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    //底部栏监听事件
    @Override
    public void onTabSelected(int position) {
        Log.v("yzll",""+position);
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new BookShelfFragment();
                break;
            case 1:
                fragment = new BookStoreFragment();
                break;
            case 2:
                fragment = new SelfFragment();
                break;
            default:
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment,fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onTabUnselected(int position) {

    }
    @Override
    public void onTabReselected(int position) {

    }

    //初始化BottomBar
    private void initBottomBar(){
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        //添加Item选项
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.bookselected,"书架").setInactiveIconResource(R.drawable.books))
                .addItem(new BottomNavigationItem(R.drawable.bookstoreselect,"书城").setInactiveIconResource(R.drawable.bookstore))
                .addItem(new BottomNavigationItem(R.drawable.meselect,"我的").setInactiveIconResource(R.drawable.me))
                .setFirstSelectedPosition(0)
                .initialise();
    }

}