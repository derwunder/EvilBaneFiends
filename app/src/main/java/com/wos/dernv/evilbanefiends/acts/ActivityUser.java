package com.wos.dernv.evilbanefiends.acts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.dialogs.DialogRegiToClan;
import com.wos.dernv.evilbanefiends.myapp.MyApp;
import com.wos.dernv.evilbanefiends.objects.UserRegistro;

public class ActivityUser extends AppCompatActivity {

    // Necesario para coordinar vistas dentro del Layout "SnackBar"
    private CoordinatorLayout mCoordinator;
    //Vars para setear el titulo del App Bar
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Grupo de Coordinacion para la actividad Base
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mAppBarLayout=(AppBarLayout)findViewById(R.id.app_bar_layout);
        // spinnerCarrera=(Spinner)findViewById(R.id.spinnerCarrera);

        iniToolBar();
       // iniNavDrawer();
        iniFab();

        mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.act_user));
        mAppBarLayout.setExpanded(true,true);

    }
    public void iniToolBar(){
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    public void iniFab(){
        mFab = (FloatingActionButton) findViewById(R.id.fab);
       // mFab.setVisibility(View.VISIBLE);
        mFab.setImageResource(R.drawable.ic_home_white_48dp);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mAppBarLayout.setExpanded(true, true);
              //  mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
                //Notice how the Coordinator Layout object is used here
                Snackbar.make(mCoordinator, getResources().getString(R.string.fab_home),
                        Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();

            }
        });

    }

}
