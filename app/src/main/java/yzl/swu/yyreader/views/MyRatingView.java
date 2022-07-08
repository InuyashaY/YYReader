package yzl.swu.yyreader.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hedgehog.ratingbar.RatingBar;

import yzl.swu.yyreader.R;

public class MyRatingView extends View {
    RatingBar mRatingBar;

    public MyRatingView(Context context) {
        super(context);
        initView();
    }

    public MyRatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyRatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public MyRatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        mRatingBar= (RatingBar) findViewById(R.id.rat_test);
        //设置是否可点击，在需要评分的地方要设置为可点击
        mRatingBar.setmClickable(true);
        //设置星星总数
        mRatingBar.setStarCount(5);
        //设置星星的宽度
        mRatingBar.setStarImageWidth(40f);
        //设置星星的高度
        mRatingBar.setStarImageHeight(40f);
        //设置星星之间的距离
        mRatingBar.setImagePadding(5f);
        //设置空星星
        mRatingBar.setStarEmptyDrawable(getResources()
                .getDrawable(R.drawable.ic_rating_empty));
        //设置填充的星星
        mRatingBar.setStarFillDrawable(getResources()
                .getDrawable(R.drawable.ic_rating_full));
        //设置半颗星
        mRatingBar.setStarHalfDrawable(getResources()
                .getDrawable(R.drawable.ic_rating_half));
        //设置显示的星星个数
        mRatingBar.setStar(4.5f);
        //设置评分的监听
        mRatingBar.setOnRatingChangeListener(
                new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(float RatingCount) {
                        Toast.makeText(getContext(), "你给出了"+
                                        (int)(RatingCount/5*100)+"分",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
