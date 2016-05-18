package com.wos.dernv.evilbanefiends.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wos.dernv.evilbanefiends.fragments.FrAdminCodeEditionActUser;
import com.wos.dernv.evilbanefiends.fragments.FrUserProfileActUser;
import com.wos.dernv.evilbanefiends.fragments.FrWikiEquipo;
import com.wos.dernv.evilbanefiends.fragments.FrWikiHab;
import com.wos.dernv.evilbanefiends.fragments.FrWikiMoneda;
import com.wos.dernv.evilbanefiends.fragments.FrWikiMundo;
import com.wos.dernv.evilbanefiends.fragments.FrWikiPersonaje;

/**
 * Created by der_w on 5/16/2016.
 */
public class AdapterViewPagerUser extends FragmentStatePagerAdapter {

    public AdapterViewPagerUser(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0: fragment = FrUserProfileActUser.newInstance(); break;
            case 1: fragment = FrAdminCodeEditionActUser.newInstance(); break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
