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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.wos.dernv.evilbanefiends.dialogs.DialogAdminCodeEdition;
import com.wos.dernv.evilbanefiends.dialogs.DialogRegiToClan;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.events.ClickCallBackAdmin;
import com.wos.dernv.evilbanefiends.fragments.FrUserProfileActUser;
import com.wos.dernv.evilbanefiends.fragments.FrViewPagerUserActUser;
import com.wos.dernv.evilbanefiends.fragments.FrViewPagerWikia;
import com.wos.dernv.evilbanefiends.logs.L;
import com.wos.dernv.evilbanefiends.myapp.MyApp;
import com.wos.dernv.evilbanefiends.network.Key;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.objects.UserRegistro;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityUser extends AppCompatActivity implements  ClickCallBackAdmin{

    //user Registro
    UserRegistro userRegistro;
    //Web
    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog ;
    private int persistenTry=0;

    // Necesario para coordinar vistas dentro del Layout "SnackBar"
    private CoordinatorLayout mCoordinator;
    //Vars para setear el titulo del App Bar
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;

    private String CODIGO;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //user Registro
        userRegistro=MyApp.getWritableDatabase().getUserRegistro();
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
       // iniNavDrawer();
        iniFab();


        //CODIGO
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CODIGO = extras.getString("CODIGO");
        }



        mCollapsingToolbarLayout.setTitle(userRegistro.getNick_name());
        mAppBarLayout.setExpanded(true,true);

        frChange(userRegistro.getAdmin());



    }
    public void frChange(String admin){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(admin.equals("0")){
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrUserProfileActUser.newInstance(CODIGO))
                    .commit();
        }
        else if(admin.equals("1")){
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrViewPagerUserActUser.newInstance(CODIGO))
                    .commit();
        }

    }

    public void iniToolBar(){
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    public void iniFab(){
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setVisibility(View.GONE);
      //  mFab.setImageResource(R.drawable.ic_home_white_48dp);
       /* mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mAppBarLayout.setExpanded(true, true);
              //  mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
                //Notice how the Coordinator Layout object is used here
                Snackbar.make(mCoordinator, getResources().getString(R.string.fab_home),
                        Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();

            }
        });*/

    }



    @Override
    public void onRcAdminCodeEdition(String id, String codigo, String nickName, String accion) {

        DialogAdminCodeEdition dialogAdminCodeEdition = new DialogAdminCodeEdition();
        dialogAdminCodeEdition.setData(id,codigo,nickName,accion);
        dialogAdminCodeEdition.show(getSupportFragmentManager(), "CodeEdition");

    }

    @Override
    public void onDialogSetCodeEdition(String accion, String codigoAdmin,
                                       String codigoJugadorOld, String codigoJugadorNew, String idInCatCod) {

    sendAdminCodeEditionToBackend(this,accion,codigoAdmin,codigoJugadorOld,codigoJugadorNew,idInCatCod);

    }

    @Override
    public void reCalFrProfile() {
        userRegistro=MyApp.getWritableDatabase().getUserRegistro();
        mCollapsingToolbarLayout.setTitle(userRegistro.getNick_name());
        frChange(userRegistro.getAdmin());
    }


    private void sendAdminCodeEditionToBackend(
            final Context context, final String accion, final String codigoAdmin, final String codigoJugadorOld,
            final String codigoJugadorNew, final String idInCatCod ) {
        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        String url = "http://ebfiends.esy.es/public/admin_code_edition";

        Map<String, String> map = new HashMap<>();
            map.put(accion, "1");
            map.put("codigoAdmin",codigoAdmin);
            map.put("codigoJugadorOld",codigoJugadorOld);
            map.put("codigoJugadorNew",codigoJugadorNew);
            map.put("idInCatCod",idInCatCod);



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

                            sendAdminCodeEditionToBackend(context,accion,codigoAdmin,codigoJugadorOld,
                              codigoJugadorNew,idInCatCod);
                        }
                    });
                    alertDialog.show();

                }else
                {
                    sendAdminCodeEditionToBackend(context,accion,codigoAdmin,codigoJugadorOld,
                            codigoJugadorNew,idInCatCod);
                }
            }
        });
        requestQueue.add(request);
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof FrUserProfileActUser){
            FrUserProfileActUser frUserProfileActUser=(FrUserProfileActUser)fragment;
            frUserProfileActUser.setInstanceOfFr(frUserProfileActUser);
        }
    }
}
