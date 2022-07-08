package yzl.swu.yyreader.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.activity.SearchDetailActivity;
import yzl.swu.yyreader.adapter.TabFragmentPageAdapter;
import yzl.swu.yyreader.databinding.BookStoreFragmentBinding;

public class BookStoreFragment extends BaseFragment<BookStoreFragmentBinding> {
    //tab的标题
    List<String> tabTitles;
    //对应的fragment
    List<Fragment> tabFragments;

    //搜索
    MaterialSearchView mSearchView;

    @Override
    protected void initWidget() {
        mSearchView =viewBinding.searchView;
        //配置tab
        setUpTabLayout();
    }


    private void setUpTabLayout(){
        //设置标题和fragment
        tabTitles = Arrays.asList("精选","分类","榜单","定制");
        tabFragments = new ArrayList<>();

        tabFragments.add(new StoreFragment_one());
        tabFragments.add(new StoreFragment_two());
        tabFragments.add(new StoreFragment_three());
        tabFragments.add(new StoreFragment_four());


        //设置适配器 绑定tab和viewPager
        viewBinding.fileSelectViewpager.setAdapter(new TabFragmentPageAdapter(getActivity().getSupportFragmentManager(),0,tabTitles,tabFragments));
        viewBinding.fileSelectViewpager.setOffscreenPageLimit(4);
        viewBinding.tabTlIndicator.setupWithViewPager(viewBinding.fileSelectViewpager);

        //是否语言搜索
        mSearchView.setVoiceSearch(false);
//        设置光标可移动
        mSearchView.setCursorDrawable(R.drawable.color_cursor_white);
        //设置收索提示
//        mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
//        设置为查询文本侦听器
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //这把外面用EventBus分别发送消息刷新
                Intent intent = new Intent(getActivity(), SearchDetailActivity.class);
                intent.putExtra("keyWord",query);
                startActivity(intent);
                return false ;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //设置搜索视图监听器
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });
        //设置提示列表点击事件
        mSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "第" + position + "行", Toast.LENGTH_LONG).show();

            }
        });

    }


}
