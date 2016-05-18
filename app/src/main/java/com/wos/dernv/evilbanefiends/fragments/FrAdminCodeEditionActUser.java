package com.wos.dernv.evilbanefiends.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.wos.dernv.evilbanefiends.adapters.AdapterRcAdminCodeEdition;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.events.ClickCallBackAdmin;
import com.wos.dernv.evilbanefiends.logs.L;
import com.wos.dernv.evilbanefiends.myapp.MyApp;
import com.wos.dernv.evilbanefiends.network.Key;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.network.UrlEP;
import com.wos.dernv.evilbanefiends.objects.AdminCodeEdition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by der_w on 5/16/2016.
 */
public class FrAdminCodeEditionActUser extends Fragment  {


    private ClickCallBackAdmin clickCallBackAdmin;

    ///vars to get data online
    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;

    private RecyclerView rcWikiPersonaje;
    private AdapterRcAdminCodeEdition adapterRcAdminCodeEdition;

    private ProgressDialog progressDialog ;
    private int persistenTry=0;

    private ArrayList<AdminCodeEdition> listAdminCodeEditions;

    public  FrAdminCodeEditionActUser(){}

    public static FrAdminCodeEditionActUser newInstance(){
        FrAdminCodeEditionActUser frAdminCodeEditionActUser= new FrAdminCodeEditionActUser();
        /*Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);*/
        return frAdminCodeEditionActUser;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        myVolleySingleton=MyVolleySingleton.getsInstance();
        requestQueue=myVolleySingleton.getmRequestQueue();
        listAdminCodeEditions = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_rc_act_main,container,false);

        rcWikiPersonaje=(RecyclerView)rootView.findViewById(R.id.recycleView);
        rcWikiPersonaje.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterRcAdminCodeEdition =new AdapterRcAdminCodeEdition(getContext(),clickCallBackAdmin);
        //setear lista por aca al adaptador
        enviarPeticionJson();
        OffsetDecorationRC offsetDecorationRC=
                new OffsetDecorationRC(75,35,getContext().getResources().getDisplayMetrics().density);
        rcWikiPersonaje.addItemDecoration(offsetDecorationRC);
        rcWikiPersonaje.setAdapter(adapterRcAdminCodeEdition);
        rcWikiPersonaje.setNestedScrollingEnabled(false);

        return rootView  ;
    }


    ///Llamados Equipo perfecto////
    public void enviarPeticionJson(){

        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                UrlEP.EBFIENDS_ADMIN_CODE
                , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        listAdminCodeEditions =parseJsonResponse(response);
                        // MiAplicativo.getWritableDatabase().insertMateriaPensumIndividual(listMateria, ma_modulo, true);//IMPORTANTE
                        adapterRcAdminCodeEdition.setAdminCodeEditionList(listAdminCodeEditions);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                progressDialog.dismiss();
                persistenTry++;
                String auxError="";

                if(persistenTry>=5) {
                    persistenTry = 0;

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                        auxError = getString(R.string.error_timeOut);
                    } else if (error instanceof AuthFailureError) {

                        auxError = getString(R.string.error_AuthFail);
                    } else if (error instanceof ServerError) {

                        auxError = getString(R.string.error_Server);
                    } else if (error instanceof NetworkError) {

                        auxError = getString(R.string.error_NetWork);
                    } else if (error instanceof ParseError) {

                        auxError = getString(R.string.error_NetWork);
                    }

                    //  textViewVolleyError.setVisibility(View.VISIBLE);
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
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

                            enviarPeticionJson();
                        }
                    });
                    alertDialog.show();
                }else
                    enviarPeticionJson();


            }
        });

        //sin esta linea no se puede hacer la peticion al server
        requestQueue.add(request);
    }
    public ArrayList<AdminCodeEdition> parseJsonResponse(JSONObject response){
        ArrayList<AdminCodeEdition> listAdminCodeEditions = new ArrayList<>();

        //  if(response.has(Key.EndPointMateria.KEY_ESTADO)&&
        //        !response.isNull(Key.EndPointMateria.KEY_ESTADO))



        if(response==null || response.length()>0){
            try{
                progressDialog.dismiss();
                persistenTry=0;

                String ESTADO="NA";
                if(response.has(Key.JsGetPlayer.ESTADO)&&
                        !response.isNull(Key.JsGetPlayer.ESTADO)){
                    ESTADO = response.getString(Key.JsGetPlayer.ESTADO);}


                String CODIGO = "NA";
                String NICK_NAME = "NA";
                String ID="NA";

                JSONArray currentCatCod = response.getJSONArray(Key.JsGetCatCod.CATCOD);
                for(int i=0; i<currentCatCod.length();i++){
                    JSONObject currentCC = currentCatCod.getJSONObject(i);

                    if(currentCC.has(Key.JsGetCatCod.CODIGO)&&
                            !currentCC.isNull(Key.JsGetCatCod.CODIGO)){
                        CODIGO=currentCC.getString(Key.JsGetCatCod.CODIGO);
                    }
                    if(currentCC.has(Key.JsGetCatCod.NICK_NAME)&&
                            !currentCC.isNull(Key.JsGetCatCod.NICK_NAME)){
                        NICK_NAME=currentCC.getString(Key.JsGetCatCod.NICK_NAME);
                    }
                    if(currentCC.has(Key.JsGetCatCod.ID)&&
                            !currentCC.isNull(Key.JsGetCatCod.ID)){
                        ID=currentCC.getString(Key.JsGetCatCod.ID);
                    }

                    AdminCodeEdition adminCodeEdition=new AdminCodeEdition();
                    adminCodeEdition.setCodigo(CODIGO);
                    adminCodeEdition.setNick_name(NICK_NAME);
                    adminCodeEdition.setId(ID);




                    //Carga completa del Json
                    listAdminCodeEditions.add(adminCodeEdition);

                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }


        return listAdminCodeEditions;
    }



    //CLass DECORADOR PARA LINEAR RC ESPACIO AL FINAL
    static class OffsetDecorationRC extends RecyclerView.ItemDecoration {
        private int mBottomOffset;
        private int mTopOffset;

        public OffsetDecorationRC(int bottomOffset,int topOffset, float density) {
            mBottomOffset =(int)(bottomOffset * density);
            mTopOffset = (int)(topOffset * density);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int dataSize = state.getItemCount();
            int position =  parent.getChildAdapterPosition(view);
            if (dataSize > 0 && position == dataSize - 1) {
                outRect.set(0, 0, 0, mBottomOffset);
            }else {
                outRect.set(0, 0, 0, 0);
            }

            if(parent.getChildAdapterPosition(view)==0){
                outRect.set(0, mTopOffset, 0, 0);
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //gracias al metodo on Attach damos valor al clickCallBack evitamos Null value
            clickCallBackAdmin=(ClickCallBackAdmin)context;
            //  mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


}
