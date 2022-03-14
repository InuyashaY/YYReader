package yzl.swu.yyreader.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.suke.widget.SwitchButton;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.databinding.ActivitySetupBinding;
import yzl.swu.yyreader.utils.ApiUtil;
import yzl.swu.yyreader.utils.StatusBarUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class SetupActivity extends BaseActivity<ActivitySetupBinding> {

    private ImageView setupBackBtn;
    private ImageView setupUAvatar;
    private TextView setupUName;
    private SwitchButton setupNotify;
    private Button setupAccountExit;

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);

        init();
        initListener();
    }

    private void init() {
        setupBackBtn = findViewById(R.id.setupBackBtn);
        setupUAvatar = findViewById(R.id.setupUAvatar);
        setupUName = findViewById(R.id.setupUName);
        setupNotify = findViewById(R.id.setupNotify);
        setupAccountExit = findViewById(R.id.setupAccountExit);
        Glide.with(this)
                .load(ApiUtil.USER_AVATAR)
                .apply(bitmapTransform(new CircleCrop()))
                .into(setupUAvatar);
        setupUName.setText(ApiUtil.USER_NAME);
    }

    private void initListener() {
        setupBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setupNotify.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    DialogUIUtils.showToastCenter("系统通知已打开");
                } else {
                    DialogUIUtils.showToastCenter("系统通知已关闭");
                }
            }
        });
        setupAccountExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("token", MODE_PRIVATE).edit();
                editor.putBoolean("isPermit", false);
                editor.apply();
                Intent intent = new Intent(SetupActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
