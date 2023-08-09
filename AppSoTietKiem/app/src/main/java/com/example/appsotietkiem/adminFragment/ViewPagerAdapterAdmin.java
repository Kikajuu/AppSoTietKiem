package com.example.appsotietkiem.adminFragment;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.appsotietkiem.fragment.AccountFragment;
import com.example.appsotietkiem.fragment.AddFragment;
import com.example.appsotietkiem.fragment.HomeFragment;


public class ViewPagerAdapterAdmin extends FragmentStatePagerAdapter {


    public ViewPagerAdapterAdmin(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new HomeAdminFragment();
            case 1:
                return new AddAdminFragment();
            case 2:
                return new AccountAdminFragment();

            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
