package com.example.manoshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.manoshop.KidWear_Tab_Fragment;
import com.example.manoshop.MenWear_Tab_Fragment;
import com.example.manoshop.WomenWear_Tab_Fragment;

public class Tab_Layout_Adapter extends FragmentPagerAdapter {


    public Tab_Layout_Adapter(FragmentManager fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MenWear_Tab_Fragment tab_men = new MenWear_Tab_Fragment();
                return tab_men;
            case 1:
                WomenWear_Tab_Fragment tab_women = new WomenWear_Tab_Fragment();
                return tab_women;
            case 2:
                KidWear_Tab_Fragment tab_kid = new KidWear_Tab_Fragment();
                return tab_kid;
            default:
                return null;
        }
    }
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Men Clothes";
                break;
            case 1:
                title = "Women Clothes";
                break;
            case 2:
                title = "Kid Clothes";
                break;
        }
        return title;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
