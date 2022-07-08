package yzl.swu.yyreader.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.suke.widget.SwitchButton;

import yzl.swu.yyreader.common.Constants;
import yzl.swu.yyreader.databinding.ActivityCacheBinding;
import yzl.swu.yyreader.utils.FileManager;
import yzl.swu.yyreader.utils.FileUtils;

public class CacheActivity extends BaseActivity<ActivityCacheBinding>{
    @Override
    protected void initWidget() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.CACHE_PATH_KEY,0);
        String orgPath = sharedPreferences.getString(Constants.CACHE_PATH_KEY,"");
        viewBinding.defaultPathSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    viewBinding.cachePathEditText.setEnabled(false);
                }else {
                    viewBinding.cachePathEditText.setEnabled(true);
                }
            }
        });
        viewBinding.cachePathEditText.setHint(orgPath == null || orgPath.isEmpty() ? "请输入路径"  : orgPath);
        viewBinding.cachePathEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(viewBinding.sureBtn.getVisibility() == View.GONE) viewBinding.sureBtn.setVisibility(View.VISIBLE);
            }
        });
        Context context = this;
        viewBinding.sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = viewBinding.cachePathEditText.getText().toString();
                boolean isSuccess = sharedPreferences.edit().putString(Constants.CACHE_PATH_KEY,input).commit();
                viewBinding.sureBtn.setVisibility(View.GONE);
                if(isSuccess) Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
                else Toast.makeText(context,"修改失败",Toast.LENGTH_SHORT).show();
            }
        });

        viewBinding.clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"清除成功",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
