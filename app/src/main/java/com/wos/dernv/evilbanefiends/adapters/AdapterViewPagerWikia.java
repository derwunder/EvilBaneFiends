package com.wos.dernv.evilbanefiends.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wos.dernv.evilbanefiends.fragments.FrEqPerfectoActMain;
import com.wos.dernv.evilbanefiends.fragments.FrPlayerActMain;
import com.wos.dernv.evilbanefiends.fragments.FrWikiEquipo;

/**
 * Created by der_w on 5/11/2016.
 */
public class AdapterViewPagerWikia extends FragmentStatePagerAdapter {

   public AdapterViewPagerWikia(FragmentManager fm){
       super(fm);
   }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0: fragment = FrWikiEquipo.newInstance(); break;
            case 1: fragment = FrEqPerfectoActMain.newInstance(); break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }


}
