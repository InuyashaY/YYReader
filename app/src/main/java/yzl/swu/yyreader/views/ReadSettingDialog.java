package yzl.swu.yyreader.views;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import yzl.swu.yyreader.R;
import yzl.swu.yyreader.common.AnimType;
import yzl.swu.yyreader.common.Constants;
import yzl.swu.yyreader.databinding.DiologReadSettingBinding;

public class ReadSettingDialog extends Dialog  {
    DiologReadSettingBinding viewBinding;
//    @BindView(R.id.read_setting_tv_font_minus)
//    TextView mTvFontMinus;
//    @BindView(R.id.read_setting_tv_font)
//    TextView mTvFont;
//    @BindView(R.id.read_setting_tv_font_plus)
//    TextView mTvFontPlus;
//    @BindView(R.id.read_setting_cb_font_default)
//    CheckBox mCbFontDefault;
//    @BindView(R.id.read_setting_rg_page_mode)
//    RadioGroup mRgPageMode;
//    @BindView(R.id.read_setting_rg_color)
//    RadioGroup mRgPageBgColor;

    //Activity
    private Activity mActivity;
    //mPageLoader
    private PageLoader mPageLoader;

    //设置的值
    private int mBrightness;
    private int mTextSize;
    //是否使用默认值
    private boolean isBrightnessAuto;
    private boolean isTextDefault;


    public ReadSettingDialog(Activity activity, PageLoader pageLoader) {
        super(activity, R.style.ReadSettingDialog);
        this.mActivity = activity;
        this.mPageLoader = pageLoader;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DiologReadSettingBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        //初始化设置
        initWindow();
        initEvents();
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

    private void initEvents(){
        //Page Mode 切换
        viewBinding.readSettingRgPageMode.setOnCheckedChangeListener((group, checkedId) -> {
                    AnimType pageMode;
                    switch (checkedId) {
                        case R.id.read_setting_rb_cover:
                            pageMode = AnimType.COVER;
                            break;
                        case R.id.read_setting_rb_slide:
                            pageMode = AnimType.SLIDE;
                            break;
                        case R.id.read_setting_rb_none:
                            pageMode = AnimType.NONE;
                            break;
                        default:
                            pageMode = AnimType.ALIKE;
                            break;
                    }
                    mPageLoader.setPageMode(pageMode);
                }
        );

        //背景颜色切换
        viewBinding.readSettingRgColor.setOnCheckedChangeListener((group, checkedId) -> {
                    int pageColor;
                    int textColor = Color.BLACK;
                    switch (checkedId) {

                        case R.id.read_setting_rb_color2:
                            pageColor = mActivity.getResources().getColor(R.color.readBgColor2);
                            break;
                        case R.id.read_setting_rb_color3:
                            pageColor = mActivity.getResources().getColor(R.color.readBgColor3);
                            break;
                        case R.id.read_setting_rb_color4:
                            pageColor = mActivity.getResources().getColor(R.color.readBgColor4);
                            break;
                        case R.id.read_setting_rb_color5:
                            pageColor = mActivity.getResources().getColor(R.color.readBgColor5);
                            textColor = Color.WHITE;
                            break;
                        default:
                            pageColor = mActivity.getResources().getColor(R.color.readBgColor1);
                            break;
                    }
                    mPageLoader.setPageStyle(pageColor,textColor);
                }
        );

        //字号加增大
        viewBinding.readSettingTvFontPlus.setOnClickListener((v)->{
            //获取当前字体大小
            int curTextSize = Integer.parseInt(viewBinding.readSettingTvFont.getText().toString());
            viewBinding.readSettingTvFont.setText(++curTextSize+"");
            mPageLoader.updateTextSize(curTextSize);
            if (viewBinding.readSettingCbFontDefault.isChecked()) viewBinding.readSettingCbFontDefault.setChecked(false);
        });

        //字号加减小
        viewBinding.readSettingTvFontMinus.setOnClickListener((v)->{
            //获取当前字体大小
            int curTextSize = Integer.parseInt(viewBinding.readSettingTvFont.getText().toString());
            viewBinding.readSettingTvFont.setText(--curTextSize+"");
            mPageLoader.updateTextSize(curTextSize);
            if (viewBinding.readSettingCbFontDefault.isChecked()) viewBinding.readSettingCbFontDefault.setChecked(false);
        });

        //默认字号
        viewBinding.readSettingCbFontDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    viewBinding.readSettingTvFont.setText(Constants.DEFAULT_TEXT_SIZE+"");
                    mPageLoader.updateTextSize(Constants.DEFAULT_TEXT_SIZE);
                }
            }
        });
    }


}
