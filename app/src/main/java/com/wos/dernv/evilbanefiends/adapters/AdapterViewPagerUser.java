package com.wos.dernv.evilbanefiends.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wos.dernv.evilbanefiends.fragments.FrAdminCodeEditionActUser;
import com.wos.dernv.evilbanefiends.fragments.FrBattleClanEditionActUser;
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
    private String CODIGO;
    public AdapterViewPagerUser(FragmentManager fm) {
        super(fm);
    }
    public void setCODIGO(String CODIGO){
        this.CODIGO=CODIGO;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0: fragment = FrUserProfileActUser.newInstance(CODIGO); break;
            case 1: fragment = FrBattleClanEditionActUser.newInstance(CODIGO); break;
            case 2: fragment = FrAdminCodeEditionActUser.newInstance(); break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
