package com.lsx.finalhomework.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class UserMenuViewAdapter extends FragmentStateAdapter {
    private List<Fragment> mFragments;
    FragmentActivity fragmentActivity;
    public UserMenuViewAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> Fragments) {
        super(fragmentActivity);
        this.fragmentActivity=fragmentActivity;
        this.mFragments = Fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments == null ? null : mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}
