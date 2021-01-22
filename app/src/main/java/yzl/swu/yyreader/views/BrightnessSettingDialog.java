package yzl.swu.yyreader.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.databinding.ActivityBookReaderBinding;
import yzl.swu.yyreader.databinding.DialogBrightnessSettingBinding;
import yzl.swu.yyreader.databinding.DiologReadSettingBinding;
import yzl.swu.yyreader.utils.BrightnessUtils;

public class BrightnessSettingDialog extends Dialog {
    DialogBrightnessSettingBinding viewBinding;
    //Activity
    private Activity mActivity;

    public BrightnessSettingDialog(Activity activity) {
        super(activity, R.style.ReadSettingDialog);
        this.mActivity = activity;
    }

    public BrightnessSettingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DialogBrightnessSettingBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        //初始化设置
        initWindow();
        initEvents();
    }

    //事件响应
    private void initEvents() {
        //亮度减小
        viewBinding.brightnessMinus.setOnClickListener((v)->{
            int nowBrightness = viewBinding.readSettingSbBrightness.getProgress();
            setBrightness(--nowBrightness);
            viewBinding.readSettingSbBrightness.setProgress(nowBrightness);
        });
        //亮度增大
        viewBinding.brightnessPlus.setOnClickListener((v)->{
            int nowBrightness = viewBinding.readSettingSbBrightness.getProgress();
            setBrightness(++nowBrightness);
            viewBinding.readSettingSbBrightness.setProgress(nowBrightness);
        });
        //滑动选择亮度
        viewBinding.readSettingSbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                setBrightness(progress);
            }
        });

        //设置自动亮度
        viewBinding.autoBrightnessSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    BrightnessUtils.setDefaultBrightness(mActivity);
                }else {
                    int nowBrightness = viewBinding.readSettingSbBrightness.getProgress();
                    setBrightness(nowBrightness);
                }
            }
        });
        //设置护眼模式
        viewBinding.protectSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                }
            }
        });

    }

    //设置Dialog显示的位置
    private void initWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }

    //设置亮度 0~255
    private void setBrightness(int brightness){
        //取消自动亮度
        if (viewBinding.autoBrightnessSw.isChecked()){
            viewBinding.autoBrightnessSw.setChecked(false);
        }
        //设置亮度
        BrightnessUtils.setBrightness(mActivity,brightness);
    }
}
