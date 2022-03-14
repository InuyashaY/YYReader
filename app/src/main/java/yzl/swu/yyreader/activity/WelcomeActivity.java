package yzl.swu.yyreader.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.databinding.ActivityWelcomeBinding;
import yzl.swu.yyreader.utils.ApiUtil;
import yzl.swu.yyreader.utils.OkHttpUtil;

public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding> {
    private TextView appName;

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //初始化通讯
        OkHttpUtil.init();
        //初始化弹框
        DialogUIUtils.init(this);

        init();
        initListener();
    }

    private void init() {
        appName = findViewById(R.id.appName);
        appName.setTypeface(appName.getTypeface(), Typeface.ITALIC);
    }

    private void initListener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
                        ApiUtil.USER_ID = preferences.getString("userId", "");
                        ApiUtil.USER_AVATAR = preferences.getString("userAvatar", "");
                        ApiUtil.USER_NAME = preferences.getString("userName", "未知用户");
                        boolean isPermit = preferences.getBoolean("isPermit", false);
                        if (isPermit) {
                            startActivity(new Intent(
                                    WelcomeActivity.this, ScanActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(
                                    WelcomeActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });
            }
        }).start();
    }

}
