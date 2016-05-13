package com.wos.dernv.evilbanefiends.acts;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.fragments.FrEqPerfectoActMain;
import com.wos.dernv.evilbanefiends.fragments.FrMenuActMain;
import com.wos.dernv.evilbanefiends.fragments.FrPlayerActMain;
import com.wos.dernv.evilbanefiends.fragments.FrViewPagerWikia;
import com.wos.dernv.evilbanefiends.fragments.FrWebViewActMain;
import com.wos.dernv.evilbanefiends.logs.L;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ClickCallBack{


    //Backk Press
    private int stateBackPress=0;
    // Necesario para coordinar vistas dentro del Layout "SnackBar"
    private CoordinatorLayout mCoordinator;
    //Vars para setear el titulo del App Bar
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Grupo de Coordinacion para la actividad Base
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mAppBarLayout=(AppBarLayout)findViewById(R.id.app_bar_layout);
        // spinnerCarrera=(Spinner)findViewById(R.id.spinnerCarrera);

        iniToolBar();
        iniNavDrawer();
        iniFab();

        mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        mAppBarLayout.setExpanded(true,true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contenedor_base, FrMenuActMain.newInstance())
                .commit();

    }

    public void iniToolBar(){
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
    }
    public void iniNavDrawer(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorWhite));
        View view = navigationView.getHeaderView(0);
        ImageView imgNav=(ImageView)view.findViewById(R.id.imgNavHeader);
        TextView text1=(TextView)view.findViewById(R.id.textNavHeader1);
        TextView text2=(TextView)view.findViewById(R.id.textNavHeader2);




    }
    public void iniFab(){
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setVisibility(View.VISIBLE);
        mFab.setImageResource(R.drawable.ic_home_white_48dp);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppBarLayout.setExpanded(true, true);
                mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
                //Notice how the Coordinator Layout object is used here
                stateBackPress=0;
                Snackbar.make(mCoordinator, getResources().getString(R.string.fab_home),
                        Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();
                fragmentChanger("menu");
            }
        });

    }

    public void fragmentChanger(String where){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(where.equals("menu")){
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrMenuActMain.newInstance())
                    .commit();
        }else if(where.equals("jugador")){
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrPlayerActMain.newInstance())
                    .commit();
        }else if(where.equals("eqPerfecto")){
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrEqPerfectoActMain.newInstance())
                    .commit();
        }else if(where.equals("wikia")){
            mAppBarLayout.setExpanded(false,false);
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrViewPagerWikia.newInstance())
                    .commit();
        }else if(where.equals("web")){
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrWebViewActMain.newInstance())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else if(stateBackPress==1000){
            stateBackPress=0;
            mAppBarLayout.setExpanded(true,true);
            fragmentChanger("menu");
        }

        else{
        super.onBackPressed();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigation_item_1) {
            mAppBarLayout.setExpanded(true, true);
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
            fragmentChanger("menu");
        } else if (id == R.id.navigation_item_2) {
            mAppBarLayout.setExpanded(false, true);
            mCollapsingToolbarLayout.setTitle("Fan Page Fiends");
            fragmentChanger("web");
        }
        stateBackPress=0;
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRSCItemMenuSelected(int position) {

        if(position==1){

            mAppBarLayout.setExpanded(false,true);
            mCollapsingToolbarLayout.setTitle("Jugadores Del Clan");

            stateBackPress=1000;
            fragmentChanger("jugador");
        }
        if(position==2){

            mAppBarLayout.setExpanded(false,true);
            mCollapsingToolbarLayout.setTitle("Equipamento Perfecto");

            stateBackPress=1000;
            fragmentChanger("eqPerfecto");
        }
        if(position==4){

            mAppBarLayout.setExpanded(false,true);
            mCollapsingToolbarLayout.setTitle("Wikia Por Fiends");

            stateBackPress=1000;
            fragmentChanger("wikia");
        }
    }
}
