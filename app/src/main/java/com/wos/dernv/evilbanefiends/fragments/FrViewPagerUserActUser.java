package com.wos.dernv.evilbanefiends.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.adapters.AdapterViewPagerUser;
import com.wos.dernv.evilbanefiends.adapters.AdapterViewPagerWikia;

/**
 * Created by der_w on 5/16/2016.
 */
public class FrViewPagerUserActUser extends Fragment {


    private ViewPager viewPager;
    private AdapterViewPagerUser adapterViewPagerUser;
    private TabLayout tabLayout;

    public FrViewPagerUserActUser() {
        // Required empty public constructor
    }

    public static FrViewPagerUserActUser newInstance() {
        FrViewPagerUserActUser fragment = new FrViewPagerUserActUser();
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_vp_act_main, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);

        adapterViewPagerUser =new AdapterViewPagerUser(getChildFragmentManager());
        viewPager.setAdapter(adapterViewPagerUser);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Perfil");
        tabLayout.getTabAt(1).setText("Codgigo de Miembros");
       // tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        //tabLayout.getTabAt(1).setIcon(R.drawable.ic_insert_invitation_white_24dp);
        // tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        // tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return rootView; // super.onCreateView(inflater, container, savedInstanceState);
    }


}
