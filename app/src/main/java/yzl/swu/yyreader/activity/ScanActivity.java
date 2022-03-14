package yzl.swu.yyreader.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;

import java.util.HashMap;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import yzl.swu.yyreader.R;

import yzl.swu.yyreader.databinding.ActivityScanBinding;
import yzl.swu.yyreader.models.AuthEntity;
import yzl.swu.yyreader.utils.Api;
import yzl.swu.yyreader.utils.ApiListener;
import yzl.swu.yyreader.utils.ApiUtil;
import yzl.swu.yyreader.utils.UniteApi;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class ScanActivity extends BaseActivity<ActivityScanBinding> {

    private RelativeLayout scanUAvatarBtn;
    private ImageView scanUAvatar;
    private ZXingView scanView;

    private Dialog dialog;

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        init();
        initListener();
    }

    private void init() {
        scanUAvatarBtn = findViewById(R.id.scanUAvatarBtn);
        scanUAvatar = findViewById(R.id.scanUAvatar);
        scanView = findViewById(R.id.scanView);
        Glide.with(this)
                .load(ApiUtil.USER_AVATAR)
                .apply(bitmapTransform(new CircleCrop()))
                .into(scanUAvatar);
    }

    private void initListener() {
        scanUAvatarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, SetupActivity.class);
                startActivity(intent);
            }
        });
        scanView.setDelegate(new QRCodeView.Delegate() {
            @Override
            public void onScanQRCodeSuccess(String result) {
                vibrate(); // 震动
//                DialogUIUtils.showToastTop(result);
                dialog = DialogUIUtils.showLoading(ScanActivity.this,
                        "处理中...", false, false,
                        false, true)
                        .show();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("userId", ApiUtil.USER_ID);
                hashMap.put("isScan", "true");
                new UniteApi(ApiUtil.TOKEN_INFO + result, hashMap).post(new ApiListener() {
                    @Override
                    public void success(Api api) {
                        dialog.dismiss();
                        UniteApi uniteApi = (UniteApi) api;
                        Gson gson = new Gson();
                        AuthEntity auth = gson.fromJson(uniteApi.getJsonData().toString(), AuthEntity.class);
                        if (auth.getAuthState() == 0 || auth.getAuthState() == 2) {
                            Intent intent = new Intent(ScanActivity.this, ResultActivity.class);
                            intent.putExtra("auth", auth);
                            startActivity(intent);
                        } else if (auth.getAuthState() == 1) {
                            DialogUIUtils.showToastCenter("登录码已使用");
                        } else {
                            DialogUIUtils.showToastCenter("登录码已过期");
                        }
                        delayStartSpot();
                    }

                    @Override
                    public void failure(Api api) {
                        dialog.dismiss();
                        DialogUIUtils.showToastCenter("登录码已过期");
                        delayStartSpot();
                    }
                });

            }

            @Override
            public void onCameraAmbientBrightnessChanged(boolean isDark) {

            }

            @Override
            public void onScanQRCodeOpenCameraError() {

            }
        });
    }

    private void delayStartSpot() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                scanView.startSpot();//开始识别二维码
            }
        }).start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        scanView.startCamera();//打开相机
        scanView.showScanRect();//显示扫描框
        scanView.startSpot();//开始识别二维码
        //scanView.openFlashlight();//开灯
        //scanView.closeFlashlight();//关灯
    }

    @Override
    protected void onStop() {
        scanView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        scanView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
