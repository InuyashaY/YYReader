package yzl.swu.yyreader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import yzl.swu.yyreader.R;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    protected T viewBinding;
    protected CompositeDisposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class cls = (Class) type.getActualTypeArguments()[0];
        try {
            Method inflate = cls.getDeclaredMethod("inflate", LayoutInflater.class);
            viewBinding = (T) inflate.invoke(null, getLayoutInflater());
            setContentView(viewBinding.getRoot());
        } catch (NoSuchMethodException | IllegalAccessException| InvocationTargetException e) {
            e.printStackTrace();
        }

        initData(savedInstanceState);
        setUpToolbar();
        initWidget();
        initClick();
        processLogic();
    }


    /************************init area************************************/
    protected void addDisposable(Disposable d){
        if (mDisposable == null){
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(d);
    }

    protected void setUpToolbar(){
    }

    protected void initData(Bundle savedInstanceState){
    }
    /**
     * 初始化零件
     */
    protected void initWidget() {

    }
    /**
     * 初始化点击事件
     */
    protected void initClick(){
    }
    /**
     * 逻辑使用区
     */
    protected void processLogic(){
    }

    /*************************lifecycle area*****************************************************/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null){
            mDisposable.dispose();
        }
    }

    /**************************used method area*******************************************/

    protected void startActivity(Class<? extends AppCompatActivity> activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }

}
