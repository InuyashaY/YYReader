package yzl.swu.yyreader;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class YToolBar extends Toolbar {
    //右侧图标
    private ImageButton imageButton;
    //居中标题
    private TextView centerTitle;
    private View view;
    public YToolBar(@NonNull Context context) {
        this(context,null);
        Log.v("yzll","code");
    }

    public YToolBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        Log.v("yzll","xml");

    }

    public YToolBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化View
        initViews();

        if (attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.YToolBar);
            TextView textView = view.findViewById(R.id.centerTitle);
            imageButton = view.findViewById(R.id.rightIcon);
            view.setBackgroundColor(typedArray.getColor(R.styleable.YToolBar_android_background, Color.BLACK));
            if (textView != null){
                textView.setText(typedArray.getString(R.styleable.YToolBar_centerTitle));
            }
            if (imageButton != null){
                imageButton.setImageDrawable(typedArray.getDrawable(R.styleable.YToolBar_rightIcon));
            }
        }
    }

    private void initViews(){
        if (view == null){
            view = inflate(getContext(),R.layout.ytoolbar,null);
            //view = LayoutInflater.from(getContext()).inflate(R.layout.ytoolbar,null);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL);
            addView(view,lp);
            Log.v("yzll","xmll");
        }
    }


    public ImageButton getImageButton() {
        return imageButton;
    }

    public void setImageButton(ImageButton imageButton) {
        this.imageButton = imageButton;
    }

    public TextView getCenterTitle() {
        return centerTitle;
    }

    public void setCenterTitle(TextView centerTitle) {
        this.centerTitle = centerTitle;
    }
}
