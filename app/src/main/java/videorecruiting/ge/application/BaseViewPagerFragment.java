package videorecruiting.ge.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by thangtranquyet on 3/16/16.
 */
public abstract class BaseViewPagerFragment extends BaseFragment implements ViewPager.OnPageChangeListener{
    private final String Tag = BaseViewPagerFragment.class.getSimpleName();
    protected ViewPager mViewPager;
    protected MyPagerAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_layout, container, false);
        return view;
    }

    @Override
    protected void initView(View viewRoot) {
        super.initView(viewRoot);
        mViewPager = (ViewPager) viewRoot.findViewById(R.id.pager);
    }

    @Override
    protected void finishedView(boolean success) {
        super.finishedView(success);
        if(mPaused) return;
        if(success){
            if(mViewPager != null){
                mViewPager.setVisibility(View.VISIBLE);
            }
        }
        else {
            if(mViewPager != null){
                mViewPager.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    protected abstract int getCountViewPager ();
    protected abstract Fragment getFragmentItemViewPager (int position);
    protected abstract CharSequence getPageTitleViewPager (int position);

    protected class MyPagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();
        ArrayList<String> mTabTitles;

        public MyPagerAdapter(FragmentManager fm, ArrayList<String> titles) {
            super(fm);
            mTabTitles = titles;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles.get(position);
            //return getPageTitleViewPager(position);
        }

        @Override
        public Fragment getItem(int position) {
            return getFragmentItemViewPager(position);
        }

        @Override
        public int getCount() {
            return mTabTitles != null ? mTabTitles.size() : 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            mRegisteredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mRegisteredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return mRegisteredFragments.get(position);
        }
    }
}

