package com.wos.dernv.evilbanefiends.acts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.dialogs.DialogMsjToNotify;
import com.wos.dernv.evilbanefiends.dialogs.DialogPassUserProfile;
import com.wos.dernv.evilbanefiends.dialogs.DialogRegiToClan;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.fragments.FrEqPerfectoActMain;
import com.wos.dernv.evilbanefiends.fragments.FrMenuActMain;
import com.wos.dernv.evilbanefiends.fragments.FrNotifyActMain;
import com.wos.dernv.evilbanefiends.fragments.FrPlayerActMain;
import com.wos.dernv.evilbanefiends.fragments.FrViewPagerWikia;
import com.wos.dernv.evilbanefiends.fragments.FrWebViewActMain;
import com.wos.dernv.evilbanefiends.logs.L;
import com.wos.dernv.evilbanefiends.myapp.MyApp;
import com.wos.dernv.evilbanefiends.network.Key;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.objects.UserRegistro;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ClickCallBack{

    //Web
    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog ;
    private int persistenTry=0;

    //Backk Press
    private int stateBackPress=0;
    // Necesario para coordinar vistas dentro del Layout "SnackBar"
    private CoordinatorLayout mCoordinator;
    //Vars para setear el titulo del App Bar
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private FloatingActionButton mFab, mFabMsj;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //web var Instance
        myVolleySingleton=MyVolleySingleton.getsInstance();
        requestQueue=myVolleySingleton.getmRequestQueue();
        progressDialog = new ProgressDialog(this);persistenTry=0;

        //Grupo de Coordinacion para la actividad Base
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mAppBarLayout=(AppBarLayout)findViewById(R.id.app_bar_layout);
        // spinnerCarrera=(Spinner)findViewById(R.id.spinnerCarrera);

        iniToolBar();
        iniNavDrawer();
        iniFab();

        mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        mAppBarLayout.setExpanded(false,false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contenedor_base, FrMenuActMain.newInstance())
                .commit();
//SHITTT
        UserRegistro userRegistro = MyApp.getWritableDatabase().getUserRegistro();

        L.t(this, "Nick: "+userRegistro.getNick_name()+
                    "\nMiem: "+userRegistro.getMiembro()+
                    "\nAdmin: "+userRegistro.getAdmin());

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

        imgNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegistro userRegistro=MyApp.getWritableDatabase().getUserRegistro();

                if(!userRegistro.getNick_name().equals("noAsig")){
                    DialogPassUserProfile dialogPassUserProfile= new DialogPassUserProfile();
                    dialogPassUserProfile.show(getSupportFragmentManager(),"Log In");
                }else {
                    DialogRegiToClan dialogRegiToClan = new DialogRegiToClan();
                    dialogRegiToClan.show(getSupportFragmentManager(), "Regi");
                }

            }
        });




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

        mFabMsj =(FloatingActionButton) findViewById(R.id.fabMsj);

        UserRegistro userRegistro =MyApp.getWritableDatabase().getUserRegistro();
        if(userRegistro.getAdmin().equals("1")){
            mFabMsj.setVisibility(View.VISIBLE);
            mFabMsj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogMsjToNotify dialogMsjToNotify = new DialogMsjToNotify();
                    dialogMsjToNotify.show(getSupportFragmentManager(), "Notify");
                }
            });
        }

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
        }else if(where.equals("notify")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrNotifyActMain.newInstance())
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
            stateBackPress=1000;
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
        if(position==5){

            mAppBarLayout.setExpanded(false,true);
            mCollapsingToolbarLayout.setTitle("Notificaciones");

            stateBackPress=1000;
            fragmentChanger("notify");
        }
    }

    @Override
    public void onRegisterDialogSet(String nickName, String codigo) {
        L.t(this,"nick: "+nickName+" cd: "+codigo);

        UserRegistro userRegistro = MyApp.getWritableDatabase().getUserRegistro();



        if(!userRegistro.getDv_regi().equals("noAsig")){
            sendRegistrationClanToBackend(this,nickName,codigo,userRegistro.getDv_regi());
        }
    }

    @Override
    public void onLogInDialogSet(String codigo) {

        sendCheckCodeBackend(this,codigo);


    }

    @Override
    public void onMsjToClanDialogSet(String codigo,String msj, String donde,String urlNoti,String urlVid) {
      /*  L.t(this,"Cod:     "+codigo+
                "\nMSJ:     "+msj+
                "\nSpinner: "+donde+
                "\nUrNt:    "+urlNoti+
                "\nUrVi:    "+urlVid);*/

        UserRegistro userRegistro=MyApp.getWritableDatabase().getUserRegistro();
        if(userRegistro.getAdmin().equals("1")){

            sendMsjForDeviceToBackend(this, userRegistro.getNick_name(),
                    codigo, donde, "msj",  msj,urlNoti,urlVid);
        }
    }

    private void changeActivity(String CODIGO){
        Intent myIntent = new Intent(ActivityMain.this, ActivityUser.class);
        myIntent.putExtra("CODIGO",CODIGO);
        startActivity(myIntent);
    }


    private void sendRegistrationClanToBackend(
            final Context context, final String nickName,final String codigo,final String regiDV) {
        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        String url = "http://ebfiends.esy.es/public/registro_al_clan";

        Map<String, String> map = new HashMap<>();
        map.put("regiClan", "1");
        map.put("nickName",nickName);
        map.put("codigo",codigo);
        map.put("regiDV",regiDV);

        JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                url,new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try{
                    String estado="NA",tipo="NA";
                    if(response.has(Key.Answer.ESTADO)&&
                            !response.isNull(Key.Answer.ESTADO)){
                        estado = response.getString(Key.Answer.ESTADO);
                    }
                    if(response.has(Key.Answer.TIPO)&&
                            !response.isNull(Key.Answer.TIPO)){
                        tipo = response.getString(Key.Answer.TIPO);
                    }

                    if(estado.equals("1")&& tipo.equals("admin")){
                        MyApp.getWritableDatabase().updateUserRegistroToAdmin(nickName,"1","1");
                        L.t(context,"Jugador admin clan activo");
                        changeActivity(codigo);
                    }else if(estado.equals("1")&& tipo.equals("jugador")){
                        MyApp.getWritableDatabase().updateUserRegistroToAdmin(nickName,"1","0");
                        L.t(context,"Jugador activo");
                        changeActivity(codigo);
                    }

                    if(estado.equals("2")){
                        L.t(context,"Fallo en el server");
                    }
                    if(estado.equals("3")){
                        L.t(context,"1- Codigo invalido\n2- muchos dispositivos asociados");
                    }



                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                persistenTry++;
                String auxError="";

                if(persistenTry>=5) {
                    persistenTry = 0;

                    error.printStackTrace();
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        L.t(context, getResources().getString(R.string.volley_error_time));

                    } else if (error instanceof AuthFailureError) {
                        L.t(context, getResources().getString(R.string.volley_error_aut));

                    } else if (error instanceof ServerError) {
                        L.t(context, getResources().getString(R.string.volley_error_serv));

                    } else if (error instanceof NetworkError) {
                        L.t(context, getResources().getString(R.string.volley_error_net));

                    } else if (error instanceof ParseError) {
                        L.t(context, getResources().getString(R.string.volley_error_par));
                    }

                    //  textViewVolleyError.setVisibility(View.VISIBLE);
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Error en la Nube");
                    alertDialog.setMessage("Error: " + auxError + "\n\n"
                            + "Reintentar Conexion?");
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            sendRegistrationClanToBackend(context, nickName,  codigo,  regiDV);
                        }
                    });
                    alertDialog.show();

                }else
                {
                    sendRegistrationClanToBackend(context, nickName,  codigo,  regiDV);
                }
            }
        });
        requestQueue.add(request);
    }

    private void sendCheckCodeBackend(
            final Context context  ,final String codigo  ) {
        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        String url = "http://ebfiends.esy.es/public/registro_al_clan";

        Map<String, String> map = new HashMap<>();
        map.put("checkCode", "1");
        map.put("codigo",codigo);


        JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                url,new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try{
                    String estado="NA",MSJ="NA";
                    if(response.has(Key.Answer.ESTADO)&&
                            !response.isNull(Key.Answer.ESTADO)){
                        estado = response.getString(Key.Answer.ESTADO);
                    }
                    if(response.has(Key.Answer.MSJ)&&
                            !response.isNull(Key.Answer.MSJ)){
                        MSJ = response.getString(Key.Answer.MSJ);
                    }

                    if(estado.equals("1")){

                        L.t(context,MSJ);
                        changeActivity(codigo);
                    }

                    if(estado.equals("2")){
                        L.t(context,MSJ);
                    }




                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                persistenTry++;
                String auxError="";

                if(persistenTry>=5) {
                    persistenTry = 0;

                    error.printStackTrace();
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        L.t(context, getResources().getString(R.string.volley_error_time));

                    } else if (error instanceof AuthFailureError) {
                        L.t(context, getResources().getString(R.string.volley_error_aut));

                    } else if (error instanceof ServerError) {
                        L.t(context, getResources().getString(R.string.volley_error_serv));

                    } else if (error instanceof NetworkError) {
                        L.t(context, getResources().getString(R.string.volley_error_net));

                    } else if (error instanceof ParseError) {
                        L.t(context, getResources().getString(R.string.volley_error_par));
                    }

                    //  textViewVolleyError.setVisibility(View.VISIBLE);
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Error en la Nube");
                    alertDialog.setMessage("Error: " + auxError + "\n\n"
                            + "Reintentar Conexion?");
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            sendCheckCodeBackend(context,   codigo);
                        }
                    });
                    alertDialog.show();

                }else
                {
                    sendCheckCodeBackend(context,   codigo);
                }
            }
        });
        requestQueue.add(request);
    }

    private void sendMsjForDeviceToBackend(
            final Context context, final String admin,final String codigo,final String donde,
            final String tipo, final String msj,final String urlNoti,final String urlVid ) {
        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        String url = "http://ebfiends.esy.es/public/msj_notify";

        Map<String, String> map = new HashMap<>();
        map.put(donde, "1");
        map.put("codigo",codigo);
        map.put("msj",msj);
        map.put("admin",admin);
        map.put("tipo",tipo);
        map.put("url_noti",urlNoti);
        map.put("url_vid",urlVid);


        JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                url,new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try{
                    String estado="NA",mensaje="NA";
                    if(response.has(Key.Answer.ESTADO)&&
                            !response.isNull(Key.Answer.ESTADO)){
                        estado = response.getString(Key.Answer.ESTADO);
                    }
                    if(response.has(Key.Answer.MSJ)&&
                            !response.isNull(Key.Answer.MSJ)){
                        mensaje = response.getString(Key.Answer.MSJ);
                    }

                    if(estado.equals("1")){
                        L.t(context,mensaje);
                    }else if(estado.equals("2")){
                        L.t(context,mensaje);
                    }else if(estado.equals("3")){
                        L.t(context,mensaje);
                    }




                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                persistenTry++;
                String auxError="";

                if(persistenTry>=5) {
                    persistenTry = 0;

                    error.printStackTrace();
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        L.t(context, getResources().getString(R.string.volley_error_time));

                    } else if (error instanceof AuthFailureError) {
                        L.t(context, getResources().getString(R.string.volley_error_aut));

                    } else if (error instanceof ServerError) {
                        L.t(context, getResources().getString(R.string.volley_error_serv));

                    } else if (error instanceof NetworkError) {
                        L.t(context, getResources().getString(R.string.volley_error_net));

                    } else if (error instanceof ParseError) {
                        L.t(context, getResources().getString(R.string.volley_error_par));
                    }

                    //  textViewVolleyError.setVisibility(View.VISIBLE);
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Error en la Nube");
                    alertDialog.setMessage("Error: " + auxError + "\n\n"
                            + "Reintentar Conexion?");
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            sendMsjForDeviceToBackend(context, admin, codigo, donde, tipo,  msj,urlNoti,urlVid);
                        }
                    });
                    alertDialog.show();

                }else
                {
                    sendMsjForDeviceToBackend(context, admin, codigo, donde, tipo,  msj,urlNoti,urlVid);
                }
            }
        });
        requestQueue.add(request);
    }
}
