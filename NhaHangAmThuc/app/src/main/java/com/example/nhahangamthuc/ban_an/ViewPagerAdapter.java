package com.example.nhahangamthuc.ban_an;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

//Adapter for two ViewPager2 of TabLayout
public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new DsBanFragment();
            case 1:
                return new DsBanTrongFragment();
            default:
                return new DsBanFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
