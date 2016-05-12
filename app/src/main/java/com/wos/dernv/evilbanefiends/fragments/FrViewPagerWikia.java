package com.wos.dernv.evilbanefiends.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.adapters.AdapterViewPagerWikia;

/**
 * Created by der_w on 5/11/2016.
 */
public class FrViewPagerWikia extends Fragment {

    private ViewPager viewPager;
    private AdapterViewPagerWikia adapterViewPagerWikia;
    private TabLayout tabLayout;

    public FrViewPagerWikia() {
        // Required empty public constructor
    }

    public static FrViewPagerWikia newInstance() {
        FrViewPagerWikia fragment = new FrViewPagerWikia();
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
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fr_vp_act_main, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);

        adapterViewPagerWikia=new AdapterViewPagerWikia(getChildFragmentManager());
        viewPager.setAdapter(adapterViewPagerWikia);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("FR 1");
        tabLayout.getTabAt(1).setText("FR 2");
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return rootView; // super.onCreateView(inflater, container, savedInstanceState);
    }
}
