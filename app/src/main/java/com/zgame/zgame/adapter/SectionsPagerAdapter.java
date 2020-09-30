package com.zgame.zgame.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zgame.zgame.fragment.ChatRoomFragment;
import com.zgame.zgame.fragment.UserFragment;
import com.zgame.zgame.fragment.UserListFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new UserListFragment();
            case 1: return new ChatRoomFragment();
            default: return new UserFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}