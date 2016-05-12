package com.wos.dernv.evilbanefiends.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.wos.dernv.evilbanefiends.adapters.AdapterRcEqPerfecto;
import com.wos.dernv.evilbanefiends.adapters.AdapterRcWikiEquipo;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.network.Key;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.network.UrlEP;
import com.wos.dernv.evilbanefiends.objects.EqPerfecto;
import com.wos.dernv.evilbanefiends.objects.WikiEquipo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by der_w on 5/11/2016.
 */
public class FrWikiEquipo extends Fragment {


    private ClickCallBack clickCallBack;

    ///vars to get data online
    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;

    private RecyclerView rcWikiEquipo;
    private AdapterRcWikiEquipo adapterRcWikiEquipo;

    private ProgressDialog progressDialog ;
    private int persistenTry=0;

    private ArrayList<WikiEquipo> listWikiEquipo= new ArrayList<>();

    public  FrWikiEquipo(){}

    public static FrWikiEquipo newInstance(){
        FrWikiEquipo frWikiEquipo= new FrWikiEquipo();
        /*Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);*/
        return frWikiEquipo;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_rc_act_main,container,false);

        rcWikiEquipo=(RecyclerView)rootView.findViewById(R.id.recycleView);
        rcWikiEquipo.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterRcWikiEquipo=new AdapterRcWikiEquipo(getContext(),clickCallBack);
        //setear lista por aca al adaptador
        enviarPeticionJson();
        OffsetDecorationRC offsetDecorationRC=
                new OffsetDecorationRC(75,35,getContext().getResources().getDisplayMetrics().density);
        rcWikiEquipo.addItemDecoration(offsetDecorationRC);
        rcWikiEquipo.setAdapter(adapterRcWikiEquipo);

        return rootView  ;
    }


    ///Llamados Equipo perfecto////
    public void enviarPeticionJson(){

        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                UrlEP.EBFIENDS_W_EQ
                , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        listWikiEquipo=parseJsonResponse(response);
                        // MiAplicativo.getWritableDatabase().insertMateriaPensumIndividual(listMateria, ma_modulo, true);//IMPORTANTE
                        adapterRcWikiEquipo.setEqPerfectoList(listWikiEquipo);

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
    public ArrayList<WikiEquipo> parseJsonResponse(JSONObject response){
        ArrayList<WikiEquipo> listWikiEquipo = new ArrayList<>();

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

                String CODENAME="NA" ;
                String NAME="NA" ;
                String CLASE="NA";
                String HB1="NA",HB2="NA",HB3="NA",HB4="NA",HB5="NA",HB6="NA" ;
                String IMG_IC00="NA",IMG_IC01="NA",IMG_IC02="NA",
                        IMG_IC03="NA",IMG_IC04="NA",IMG_IC05="NA";
                String IMG_VIEW="NA";

                JSONArray currentEqPerfecto = response.getJSONArray(Key.JsGetWikiEquipo.WIKI_EQUIPO);
                for(int i=0; i<currentEqPerfecto.length();i++){
                    JSONObject currentEQP = currentEqPerfecto.getJSONObject(i);

                    if(currentEQP.has(Key.JsGetWikiEquipo.CODENAME)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.CODENAME)){
                        CODENAME=currentEQP.getString(Key.JsGetWikiEquipo.CODENAME);
                    }
                    if(currentEQP.has(Key.JsGetWikiEquipo.NAME)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.NAME)){
                        NAME=currentEQP.getString(Key.JsGetWikiEquipo.NAME);
                    }
                    if(currentEQP.has(Key.JsGetWikiEquipo.CLASE)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.CLASE)){
                        CLASE=currentEQP.getString(Key.JsGetWikiEquipo.CLASE);
                    }

                    if(currentEQP.has(Key.JsGetWikiEquipo.HB1)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.HB1)){
                        HB1=currentEQP.getString(Key.JsGetWikiEquipo.HB1);
                    }
                    if(currentEQP.has(Key.JsGetWikiEquipo.HB2)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.HB2)){
                        HB2=currentEQP.getString(Key.JsGetWikiEquipo.HB2);
                    }
                    if(currentEQP.has(Key.JsGetWikiEquipo.HB3)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.HB3)){
                        HB3=currentEQP.getString(Key.JsGetWikiEquipo.HB3);
                    }
                    if(currentEQP.has(Key.JsGetWikiEquipo.HB4)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.HB4)){
                        HB4=currentEQP.getString(Key.JsGetWikiEquipo.HB4);
                    }
                    if(currentEQP.has(Key.JsGetWikiEquipo.HB5)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.HB5)){
                        HB5=currentEQP.getString(Key.JsGetWikiEquipo.HB5);
                    }
                    if(currentEQP.has(Key.JsGetWikiEquipo.HB6)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.HB6)){
                        HB6=currentEQP.getString(Key.JsGetWikiEquipo.HB6);
                    }

                    if(currentEQP.has(Key.JsGetWikiEquipo.IMG_IC00)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.IMG_IC00)){
                        IMG_IC00=currentEQP.getString(Key.JsGetWikiEquipo.IMG_IC00);
                    }
                    if(currentEQP.has(Key.JsGetWikiEquipo.IMG_IC01)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.IMG_IC01)){
                        IMG_IC01=currentEQP.getString(Key.JsGetWikiEquipo.IMG_IC01);
                    }
                    if(currentEQP.has(Key.JsGetWikiEquipo.IMG_IC02)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.IMG_IC02)){
                        IMG_IC02=currentEQP.getString(Key.JsGetWikiEquipo.IMG_IC02);
                    }
                    if(currentEQP.has(Key.JsGetWikiEquipo.IMG_IC03)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.IMG_IC03)){
                        IMG_IC03=currentEQP.getString(Key.JsGetWikiEquipo.IMG_IC03);
                    }
                    if(currentEQP.has(Key.JsGetWikiEquipo.IMG_IC04)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.IMG_IC04)){
                        IMG_IC04=currentEQP.getString(Key.JsGetWikiEquipo.IMG_IC04);
                    }
                    if(currentEQP.has(Key.JsGetWikiEquipo.IMG_IC05)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.IMG_IC05)){
                        IMG_IC05=currentEQP.getString(Key.JsGetWikiEquipo.IMG_IC05);
                    }
                    if(currentEQP.has(Key.JsGetWikiEquipo.IMG_VIEW)&&
                            !currentEQP.isNull(Key.JsGetWikiEquipo.IMG_VIEW)){
                        IMG_VIEW=currentEQP.getString(Key.JsGetWikiEquipo.IMG_VIEW);
                    }

                    WikiEquipo wikiEquipo=new WikiEquipo();
                    wikiEquipo.setCodeName(CODENAME);
                    wikiEquipo.setName(NAME);wikiEquipo.setClase(CLASE);
                    wikiEquipo.setHb1(HB1);wikiEquipo.setHb2(HB2);wikiEquipo.setHb3(HB3);
                    wikiEquipo.setHb4(HB4);wikiEquipo.setHb5(HB5);wikiEquipo.setHb6(HB6);
                    wikiEquipo.setImg_ic00(IMG_IC00);wikiEquipo.setImg_ic01(IMG_IC01);wikiEquipo.setImg_ic02(IMG_IC02);
                    wikiEquipo.setImg_ic03(IMG_IC03);wikiEquipo.setImg_ic04(IMG_IC04);wikiEquipo.setImg_ic05(IMG_IC05);
                    wikiEquipo.setImg_view(IMG_VIEW);


                    //Carga completa del Json
                    listWikiEquipo.add(wikiEquipo);

                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }


        return listWikiEquipo;
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
            clickCallBack=(ClickCallBack)context;
            //  mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
