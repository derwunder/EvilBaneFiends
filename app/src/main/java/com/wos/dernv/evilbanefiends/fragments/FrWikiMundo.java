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
import com.wos.dernv.evilbanefiends.adapters.AdapterRcWikiMundo;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.network.Key;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.network.UrlEP;
import com.wos.dernv.evilbanefiends.objects.WikiMundo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by der_w on 5/12/2016.
 */
public class FrWikiMundo extends Fragment {

    private ClickCallBack clickCallBack;

    ///vars to get data online
    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;

    private RecyclerView rcWikiMundo;
    private AdapterRcWikiMundo adapterRcWikiMundo;

    private ProgressDialog progressDialog ;
    private int persistenTry=0;

    private ArrayList<WikiMundo> listWikiMundo = new ArrayList<>();

    public  FrWikiMundo(){}

    public static FrWikiMundo newInstance(){
        FrWikiMundo frWikiMundo= new FrWikiMundo();
        /*Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);*/
        return frWikiMundo;
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

        rcWikiMundo =(RecyclerView)rootView.findViewById(R.id.recycleView);
        rcWikiMundo.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterRcWikiMundo =new AdapterRcWikiMundo(getContext(),clickCallBack);
        //setear lista por aca al adaptador
        enviarPeticionJson();
        OffsetDecorationRC offsetDecorationRC=
                new OffsetDecorationRC(75,35,getContext().getResources().getDisplayMetrics().density);
        rcWikiMundo.addItemDecoration(offsetDecorationRC);
        rcWikiMundo.setAdapter(adapterRcWikiMundo);
        rcWikiMundo.setNestedScrollingEnabled(false);

        return rootView  ;
    }


    ///Llamados Equipo perfecto////
    public void enviarPeticionJson(){

        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                UrlEP.EBFIENDS_W_MUNDO
                , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        listWikiMundo =parseJsonResponse(response);
                        // MiAplicativo.getWritableDatabase().insertMateriaPensumIndividual(listMateria, ma_modulo, true);//IMPORTANTE
                        adapterRcWikiMundo.setWikiMundoList(listWikiMundo);

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
    public ArrayList<WikiMundo> parseJsonResponse(JSONObject response){
        ArrayList<WikiMundo> listWikiMundo = new ArrayList<>();

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


                String TIPO="NA";
                String DETALLE="NA",IMG="NA";

                JSONArray currentWikiMundos = response.getJSONArray(Key.JsGetWikiMundo.WIKI_MUNDO);
                for(int i=0; i<currentWikiMundos.length();i++){
                    JSONObject currentWMundo = currentWikiMundos.getJSONObject(i);

                    if(currentWMundo.has(Key.JsGetWikiMundo.TIPO)&&
                            !currentWMundo.isNull(Key.JsGetWikiMundo.TIPO)){
                        TIPO=currentWMundo.getString(Key.JsGetWikiMundo.TIPO);
                    }
                    if(currentWMundo.has(Key.JsGetWikiMundo.DETALLE)&&
                            !currentWMundo.isNull(Key.JsGetWikiMundo.DETALLE)){
                        DETALLE=currentWMundo.getString(Key.JsGetWikiMundo.DETALLE);
                    }
                    if(currentWMundo.has(Key.JsGetWikiMundo.IMG)&&
                            !currentWMundo.isNull(Key.JsGetWikiMundo.IMG)){
                        IMG=currentWMundo.getString(Key.JsGetWikiMundo.IMG);
                    }

                    WikiMundo wikiMundo=new WikiMundo();
                    wikiMundo.setTipo(TIPO);
                    wikiMundo.setDetalle(DETALLE);
                    wikiMundo.setImg(IMG);





                    //Carga completa del Json
                    listWikiMundo.add(wikiMundo);

                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }


        return listWikiMundo;
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
