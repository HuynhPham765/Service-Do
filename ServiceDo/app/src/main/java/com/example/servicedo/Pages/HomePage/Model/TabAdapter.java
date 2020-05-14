package com.example.servicedo.Pages.HomePage.Model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.servicedo.Pages.HomePage.Controller.HomeFragment;
import com.example.servicedo.Pages.HomePage.Controller.ProfileFragment;

public class TabAdapter extends FragmentPagerAdapter {

    public TabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment frag=null;
        switch (position){
            case 0:
                frag = new HomeFragment();
                break;
            case 1:
                frag = new ProfileFragment();
                break;
//            case 2:
//                frag = new FragmentThree();
//                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Tin Đăng";
                break;
            case 1:
                title = "Cá Nhân";
                break;
//            case 2:
//                title = "Three";
//                break;
        }
        return title;
    }
}
