package com.example.imageapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.imageapp.fragment.DetailFragment;

import java.util.ArrayList;
import java.util.List;

public class AdapterPager extends FragmentStatePagerAdapter {
    List<Fragment> fragments = new ArrayList<>();

    public AdapterPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public void AddFragment(Fragment fragment) {
        fragments.add(fragment);
    }
    public void AddFragment(List<DetailFragment> fragments) {
        fragments.addAll(fragments);
    }

    public List<Fragment> getFragments() {
        return fragments;
    }
    public void removeFragment(int i){
        fragments.remove(i);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
