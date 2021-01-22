package yzl.swu.yyreader.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class TabFragmentPageAdapter extends FragmentPagerAdapter {
    List<String> mTitleList;
    List<Fragment> mFragmentList;
    public TabFragmentPageAdapter(@NonNull FragmentManager fm, int behavior,List<String> mTitleList,List<Fragment> mFragmentList) {
        super(fm, behavior);
        this.mTitleList = mTitleList;
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}
