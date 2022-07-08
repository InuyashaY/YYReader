package yzl.swu.yyreader.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import yzl.swu.yyreader.App;
import yzl.swu.yyreader.R;

import yzl.swu.yyreader.common.Constants;
import yzl.swu.yyreader.databinding.ActivityLoginBinding;
import yzl.swu.yyreader.models.BookCategory;
import yzl.swu.yyreader.models.User;
import yzl.swu.yyreader.models.UserEntity;
import yzl.swu.yyreader.remote.RemoteRepository;
import yzl.swu.yyreader.utils.Api;
import yzl.swu.yyreader.utils.ApiListener;
import yzl.swu.yyreader.utils.ApiUtil;
import yzl.swu.yyreader.utils.RxUtils;
import yzl.swu.yyreader.utils.StatusBarUtil;
import yzl.swu.yyreader.utils.UniteApi;


public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private EditText loginAccount;
    private EditText loginPassword;
    private Boolean accountFlag = false;
    private Boolean passwordFlag = false;
    private Button loginGoBtn;

    private LinearLayout loginQQBtn;
    private LinearLayout loginWeChatBtn;
    private LinearLayout loginLinkedInBtn;

    private Dialog dialog;

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorBack);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        init();
        initListener();
    }

    private void init() {
        loginAccount = findViewById(R.id.loginAccount);
        loginPassword = findViewById(R.id.loginPassword);
        loginGoBtn = findViewById(R.id.loginGoBtn);
        loginGoBtn.setEnabled(false);
//        loginQQBtn = findViewById(R.id.loginQQBtn);
//        loginWeChatBtn = findViewById(R.id.loginWeChatBtn);
//        loginLinkedInBtn = findViewById(R.id.loginLinkedInBtn);
    }

    private void initListener() {

        Activity activity = this;
        loginGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog = DialogUIUtils.showLoading(LoginActivity.this,
//                        "验证中...", false, false,
//                        false, true)
//                        .show();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("userId", loginAccount.getText().toString());
                hashMap.put("userPassword", loginPassword.getText().toString());
                Toast.makeText(activity,"登录成功",Toast.LENGTH_SHORT).show();
                activity.finish();
                Disposable remoteDisp = RemoteRepository.getInstance()
                        .login(loginAccount.getText().toString(),loginPassword.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (tokeMap)-> {
                                    //保存的sharepreference文件名为cookieData
                                    SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(Constants.COOKIE_DATA, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(Constants.COOKIE_KEY, tokeMap.get("token"));
                                    editor.apply();
                                    if(tokeMap.get("token") != null) {
                                        Toast.makeText(activity,"登录成功",Toast.LENGTH_SHORT).show();
                                        activity.finish();
                                    }else {
                                        Toast.makeText(activity,"登录失败",Toast.LENGTH_SHORT).show();
                                    }

                                }
                                ,
                                (e) ->{
                                    Toast.makeText(activity,"登录失败",Toast.LENGTH_SHORT).show();
                                    Log.e("Login","net error:"+e.toString());
                                }
                        );
                addDisposable(remoteDisp);
//                new UniteApi(ApiUtil.LOGIN, hashMap).post(new ApiListener() {
//                    @Override
//                    public void success(Api api) {
//                        dialog.dismiss();
//                        UniteApi uniteApi = (UniteApi) api;
//                        Gson gson = new Gson();
//                        UserEntity user = gson.fromJson(uniteApi.getJsonData().toString(), UserEntity.class);
//                        if (user.getUserId() != 0) {
//                            // 保存在设置中
//                            ApiUtil.USER_ID = String.valueOf(user.getUserId());
//                            ApiUtil.USER_AVATAR = user.getUserAvatar();
//                            ApiUtil.USER_NAME = user.getUserName();
//                            // 保存在系统文件中
//                            SharedPreferences.Editor editor = getSharedPreferences("token", MODE_PRIVATE).edit();
//                            editor.putString("userId", String.valueOf(user.getUserId()));
//                            editor.putString("userAvatar", user.getUserAvatar());
//                            editor.putString("userName", user.getUserName());
//                            editor.putBoolean("isPermit", true);
//                            editor.apply();
//                            // 登录成功进行跳转
//                            Intent intent = new Intent(LoginActivity.this, ScanActivity.class);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            DialogUIUtils.showToastCenter("账号或密码错误");
//                        }
//                    }
//
//                    @Override
//                    public void failure(Api api) {
//                        dialog.dismiss();
//                        DialogUIUtils.showToastCenter("账号或密码错误");
//                    }
//                });
            }
        });

        loginAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                accountFlag = s.length() > 0;
                if (accountFlag && passwordFlag) {
                    loginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
                    loginGoBtn.setEnabled(true);
                } else {
                    loginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_pale_circle));
                    loginGoBtn.setEnabled(false);
                }
            }
        });

        loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordFlag = s.length() > 0;
                if (accountFlag && passwordFlag) {
                    loginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
                    loginGoBtn.setTextColor(getResources().getColor(R.color.colorLight));
                    loginGoBtn.setEnabled(true);
                } else {
                    loginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_pale_circle));
                    loginGoBtn.setTextColor(getResources().getColor(R.color.colorLight));
                    loginGoBtn.setEnabled(false);
                }
            }
        });


        viewBinding.goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
//        loginQQBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Disposable remoteDisp = RemoteRepository.getInstance()
//                        .getUserInfo()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                (beans)-> {
////                            mView.finishRefresh(beans);
////                            mView.complete();
//                                    System.out.println(beans);
//                                }
//                                ,
//                                (e) ->{
//
//                                    Log.e("Login","net error:"+e.toString());
//                                }
//                        );
//                addDisposable(remoteDisp);
//            }
//        });
//        loginWeChatBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//        loginLinkedInBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
    }
}
