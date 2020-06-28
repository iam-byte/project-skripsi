package com.ilham.inventoridiecut.adapter;

import android.widget.Switch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ilham.inventoridiecut.user.PenarikanFragment;
import com.ilham.inventoridiecut.user.PengambilanFragment;
import com.ilham.inventoridiecut.user.PerbaikanFragment;

public class TabAdapter extends FragmentStatePagerAdapter {
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                PerbaikanFragment perbaikanFragment  = new PerbaikanFragment();
                return perbaikanFragment;
            case 1:
                PengambilanFragment pengambilanFragment = new PengambilanFragment();
                return pengambilanFragment;
            case 2:
                PenarikanFragment penarikanFragment = new PenarikanFragment();
                return penarikanFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
