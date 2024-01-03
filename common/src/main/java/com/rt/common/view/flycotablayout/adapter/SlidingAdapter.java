package com.rt.common.view.flycotablayout.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * Created by huy  on 2023/6/16.
 */
public abstract class SlidingAdapter extends FragmentStateAdapter {
    public SlidingAdapter(FragmentActivity fm) {
        super(fm);
    }

    public SlidingAdapter(Fragment fm) {
        super(fm);
    }

    abstract public CharSequence getPageTitle(int position);
}

