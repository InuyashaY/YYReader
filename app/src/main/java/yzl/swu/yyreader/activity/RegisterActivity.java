package yzl.swu.yyreader.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import yzl.swu.yyreader.App;
import yzl.swu.yyreader.R;
import yzl.swu.yyreader.common.Constants;
import yzl.swu.yyreader.databinding.ActivityRegristBinding;
import yzl.swu.yyreader.remote.RemoteRepository;

public class RegisterActivity extends BaseActivity<ActivityRegristBinding> {

    Button loginGoBtn;
    EditText loginAccount;
    EditText loginPassword;
    EditText loginPasswordAgain;
    private Boolean accountFlag = false;
    private Boolean passwordFlag = false;

    @Override
    protected void initWidget() {
        loginGoBtn = viewBinding.loginGoBtn;
        loginAccount = viewBinding.loginAccount;
        loginPassword = viewBinding.loginPassword;
        loginPasswordAgain = viewBinding.loginPasswordAgain;
        initListener();
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
                if (loginAccount.getText() == null || loginAccount.getText().toString().isEmpty()) {
                    Toast.makeText(activity,"用户名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (viewBinding.iphoneNumber.getText().toString() == null) {
                    Toast.makeText(activity,"手机号不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!loginPassword.getText().toString().equals(loginPasswordAgain.getText().toString())){
                    Toast.makeText(activity,"两次密码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("userId", loginAccount.getText().toString());
                hashMap.put("userPassword", loginPassword.getText().toString());
                Disposable remoteDisp = RemoteRepository.getInstance()
                        .register(viewBinding.iphoneNumber.getText().toString(),loginPassword.getText().toString(),loginAccount.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (tokeMap)-> {

                                    if(tokeMap.get("token") != null) {
                                        Toast.makeText(activity,"注册成功",Toast.LENGTH_SHORT).show();
                                        activity.finish();
                                    }else {
                                        Toast.makeText(activity,"注册成功",Toast.LENGTH_SHORT).show();
                                        activity.finish();
                                    }

                                }
                                ,
                                (e) ->{
                                    Toast.makeText(activity,"登录失败",Toast.LENGTH_SHORT).show();
                                    Log.e("Login","net error:"+e.toString());
                                }
                        );
                addDisposable(remoteDisp);

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

    }
}
