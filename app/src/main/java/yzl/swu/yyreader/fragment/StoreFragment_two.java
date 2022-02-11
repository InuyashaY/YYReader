package yzl.swu.yyreader.fragment;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import yzl.swu.yyreader.R;
import yzl.swu.yyreader.activity.BookDetailActivity;
import yzl.swu.yyreader.adapter.SpaceItemDecoration;
import yzl.swu.yyreader.adapter.StoreBookListAdapter;

import yzl.swu.yyreader.databinding.FragmentStore2Binding;
import yzl.swu.yyreader.models.BookCategory;
import yzl.swu.yyreader.models.StoreBookItemDao;
import yzl.swu.yyreader.remote.RemoteRepository;
import yzl.swu.yyreader.utils.RxUtils;

public class StoreFragment_two extends BaseFragment<FragmentStore2Binding> {

    StoreBookListAdapter mStoreBookRecyclerAdapter;
    @Override
    protected void initWidget() {
        initRecyclerView();
        initSelectionTags();
    }

    //初始化RecyclerView
    private void initRecyclerView() {
        mStoreBookRecyclerAdapter = new StoreBookListAdapter(null,this.getContext(), new StoreBookListAdapter.OnBookClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                //点击小说-查看详情
                String bookId = String.valueOf(mStoreBookRecyclerAdapter.getItem(pos).getId());
                BookDetailActivity.startActivity(getContext(),bookId);

            }
        });
        viewBinding.bookListRv.setAdapter(mStoreBookRecyclerAdapter);
        viewBinding.bookListRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        viewBinding.bookListRv.addItemDecoration(new SpaceItemDecoration(5));
    }

    //初始化搜索标签
    @SuppressLint("ResourceType")
    private void initSelectionTags() {
        //男女频道
        initSelectCndition(viewBinding.llSex,99,"全部");
        initSelectCndition(viewBinding.llSex, 0, "男频");
        initSelectCndition(viewBinding.llSex, 1, "女频");
        viewBinding.llSex.check(99);

        //是否完结
        initSelectCndition(viewBinding.llOver,99,"全部");
        initSelectCndition(viewBinding.llOver, 0, "连载中");
        initSelectCndition(viewBinding.llOver, 1, "已完结");
        viewBinding.llOver.check(99);

        //字数
        initSelectCndition(viewBinding.llSize,99,"全部");
        initSelectCndition(viewBinding.llSize, 0, "30万以下");
        initSelectCndition(viewBinding.llSize, 1, "30-50万");
        initSelectCndition(viewBinding.llSize, 2, "50-100万");
        initSelectCndition(viewBinding.llSize, 3, "100万以上");
        viewBinding.llSize.check(99);

        //类型
        initSelectCndition(viewBinding.llType, 99, "全部");
        viewBinding.llSize.check(99);
        Disposable disposable = RemoteRepository.getInstance()
                .getListOfCategory()
                .doOnSuccess(new Consumer<List<BookCategory>>() {
                    @Override
                    public void accept(List<BookCategory> bookCategories) throws Exception {

                    }
                })
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        beans -> {
                            //异步请求完成
                            for (int i = 0; i < beans.size(); i++) {
                                BookCategory category = beans.get(i);
                                initSelectCndition(viewBinding.llType, category.getId(), category.getName());
                            }

                        }
                        ,
                        e -> {
                            //TODO: Haven't grate conversation method.
                            Log.e("TAG", e.toString());
                        }
                );

    }

    private void initSelectCndition(LinearLayout linearLayout, int i, String name) {

        View view = View.inflate(this.getContext(), R.layout.item_radio_btn, null);
        RadioButton rb = (RadioButton) view;

        rb.setText(name.trim());
        //设置id后这样才能选中设置为单选效果
        rb.setId(i);
        rb.setTag(i);
        //点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton rb = (RadioButton) v;
                String text = rb.getText().toString();

                int position = (int) rb.getId();
//                checkPosition(text);
                //联网更新数据
                getDataFromNet();

            }
        });
        //这里注意一定要用RadioGroup的，如果用线性的会导致参数设置无效
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        //设置边距：每个标签之间的距离
        layoutParams.setMargins(dp2px(5), 0, dp2px(5), 0);

        //添加到我们的布局中
        linearLayout.addView(view, layoutParams);
    }

    //组装请求数据
    @SuppressLint("ResourceType")
    private void getDataFromNet() {
        BookSearchParams params = new BookSearchParams();
        //男女
        if (viewBinding.llSex.getCheckedRadioButtonId() != 99){
            params.workDirection = (byte) viewBinding.llSex.getCheckedRadioButtonId();
        }
        //分类
        if (viewBinding.llType.getCheckedRadioButtonId() != 99) {
            params.catId = viewBinding.llType.getCheckedRadioButtonId();
        }
        //完结
        if (viewBinding.llOver.getCheckedRadioButtonId() != 99){
            params.bookStatus = (byte) viewBinding.llOver.getCheckedRadioButtonId();
        }
        //字数
        switch (viewBinding.llSize.getCheckedRadioButtonId()) {
            case 0:
                params.wordCountMax = 300000;
                break;
            case 1:
                params.wordCountMin = 300000;
                params.wordCountMax = 500000;
                break;
            case 2:
                params.wordCountMin = 500000;
                params.wordCountMax = 1000000;
                break;
            case 3:
                params.wordCountMin = 1000000;
                break;
            default:
                break;
        }

        Map<String, String> map = new HashMap<>();
        Field[] fieldArr = params.getClass().getDeclaredFields();
        try {
            for (Field field : fieldArr) {
                field.setAccessible(true);
                if (field.get(params) != null && !"".equals(field.get(params).toString())) {
                    map.put(field.getName(), field.get(params).toString());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Disposable disposable = RemoteRepository.getInstance()
                .getSearchTagByParams(map)
                .doOnSuccess(new Consumer<List<StoreBookItemDao>>() {
                    @Override
                    public void accept(List<StoreBookItemDao> storeBookItemDaos) throws Exception {

                    }
                })
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        beans -> {
                            //异步请求完成
                            mStoreBookRecyclerAdapter.refreshModels(beans);
                        }
                        ,
                        e -> {
                            //TODO: Haven't grate conversation method.
                            Log.e("TAG", e.toString());
                        }
                );

    }

    private int dp2px(float dp) {
        return (int) (getContext().getResources().getDisplayMetrics().density * dp);
    }


    public class BookSearchParams {

        private String keyword;

        private Byte workDirection;

        private Integer catId;

        private Byte isVip;

        private Byte bookStatus;

        private Integer wordCountMin;

        private Integer wordCountMax;

        private Date updateTimeMin;

        private Long updatePeriod;

        private String sort;

    }
}
