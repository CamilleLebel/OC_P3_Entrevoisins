package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.openclassrooms.entrevoisins.model.Neighbour;


public class ListNeighbourPagerAdapter extends FragmentPagerAdapter {

    private final Fragment[] fragments;

    public ListNeighbourPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[]{
                NeighbourFragment.newInstance(), FavoriteNeighbourFragment.newInstance()
        };
    }

    /**
     * getItem is called to instantiate the fragment for the given page.
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    /**
     * get the number of pages
     * @return
     */
    @Override
    public int getCount() {
        return fragments.length;
    }
}