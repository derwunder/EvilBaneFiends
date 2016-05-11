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
import com.wos.dernv.evilbanefiends.adapters.AdapterRcPlayer;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.network.Key;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.network.UrlEP;
import com.wos.dernv.evilbanefiends.objects.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by der_w on 5/10/2016.
 */
public class FrPlayerActMain extends Fragment {

    private ClickCallBack clickCallBack;

    ///vars to get data online
    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;

    private RecyclerView rcPlayerList;
    private AdapterRcPlayer adapterRcPlayer;

    private ProgressDialog progressDialog ;
    private int persistenTry=0;

    private ArrayList<Player> listPlayer= new ArrayList<>();

    public  FrPlayerActMain(){}

    public static FrPlayerActMain newInstance(){
        FrPlayerActMain frPlayerActMain= new FrPlayerActMain();
        /*Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);*/
        return frPlayerActMain;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        myVolleySingleton=MyVolleySingleton.getsInstance();
        requestQueue=myVolleySingleton.getmRequestQueue();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fr_rc_act_main,container,false);

        rcPlayerList=(RecyclerView)rootView.findViewById(R.id.recycleView);
        rcPlayerList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterRcPlayer=new AdapterRcPlayer(getContext(),clickCallBack);
        //setear lista por aca al adaptador//TODO: no se te olvide
        enviarPeticionJson();
        OffsetDecorationRC offsetDecorationRC=
                new OffsetDecorationRC(75,35,getContext().getResources().getDisplayMetrics().density);
        rcPlayerList.addItemDecoration(offsetDecorationRC);
        rcPlayerList.setAdapter(adapterRcPlayer);





        return rootView  ;// super.onCreateView(inflater, container, savedInstanceState);
    }


    ///Llamados Player////
    public void enviarPeticionJson(){

        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                UrlEP.EBFIENDS_PLAYER
                , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        listPlayer=parseJsonResponse(response);
                       // MiAplicativo.getWritableDatabase().insertMateriaPensumIndividual(listMateria, ma_modulo, true);//IMPORTANTE
                        adapterRcPlayer.setPlayerList(listPlayer);

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
    public ArrayList<Player> parseJsonResponse(JSONObject response){
        ArrayList<Player> listPlayer = new ArrayList<>();

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

                String JUDADOR="NA";String JUDADORES="NA";
                String ID="NA"; String NICK_NAME="NA";String CLASE="NA";
                String CASCO="NA";String PECHO="NA";String BRAZO="NA";String PIERNA="NA";
                String ESPADA="NA";String CAPA="NA";String ACCESORIO="NA";
                String VIDA="NA";String ATQ="NA";String DEF="NA";
                String NIVEL="NA";String PAIS="NA";String ESPECIALIDAD="NA";
                String IMG_PERFIL="NA";String IMG_INFO="NA";

                JSONArray currentPlayers = response.getJSONArray(Key.JsGetPlayer.JUDADORES);
                for(int i=0; i<currentPlayers.length();i++){
                    JSONObject currentPlayer = currentPlayers.getJSONObject(i);

                    if(currentPlayer.has(Key.JsGetPlayer.ID)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.ID)){
                        ID=currentPlayer.getString(Key.JsGetPlayer.ID);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.NICK_NAME)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.NICK_NAME)){
                        NICK_NAME=currentPlayer.getString(Key.JsGetPlayer.NICK_NAME);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.CLASE)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.CLASE)){
                        CLASE=currentPlayer.getString(Key.JsGetPlayer.CLASE);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.CASCO)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.CASCO)){
                        CASCO=currentPlayer.getString(Key.JsGetPlayer.CASCO);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.PECHO)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.PECHO)){
                        PECHO=currentPlayer.getString(Key.JsGetPlayer.PECHO);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.BRAZO)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.BRAZO)){
                        BRAZO=currentPlayer.getString(Key.JsGetPlayer.BRAZO);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.PIERNA)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.PIERNA)){
                        PIERNA=currentPlayer.getString(Key.JsGetPlayer.PIERNA);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.ESPADA)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.ESPADA)){
                        ESPADA=currentPlayer.getString(Key.JsGetPlayer.ESPADA);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.CAPA)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.CAPA)){
                        CAPA=currentPlayer.getString(Key.JsGetPlayer.CAPA);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.ACCESORIO)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.ACCESORIO)){
                        ACCESORIO=currentPlayer.getString(Key.JsGetPlayer.ACCESORIO);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.VIDA)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.VIDA)){
                        VIDA=currentPlayer.getString(Key.JsGetPlayer.VIDA);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.ATQ)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.ATQ)){
                        ATQ=currentPlayer.getString(Key.JsGetPlayer.ATQ);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.DEF)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.DEF)){
                        DEF=currentPlayer.getString(Key.JsGetPlayer.DEF);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.NIVEL)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.NIVEL)){
                        NIVEL=currentPlayer.getString(Key.JsGetPlayer.NIVEL);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.PAIS)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.PAIS)){
                        PAIS=currentPlayer.getString(Key.JsGetPlayer.PAIS);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.ESPECIALIDAD)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.ESPECIALIDAD)){
                        ESPECIALIDAD=currentPlayer.getString(Key.JsGetPlayer.ESPECIALIDAD);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.IMG_PERFIL)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.IMG_PERFIL)){
                        IMG_PERFIL=currentPlayer.getString(Key.JsGetPlayer.IMG_PERFIL);
                    }
                    if(currentPlayer.has(Key.JsGetPlayer.IMG_INFO)&&
                            !currentPlayer.isNull(Key.JsGetPlayer.IMG_INFO)){
                        IMG_INFO=currentPlayer.getString(Key.JsGetPlayer.IMG_INFO);
                    }

                    Player player=new Player();
                    player.setNick_name(NICK_NAME);player.setClase(CLASE);
                    player.setCasco(CASCO);player.setPecho(PECHO);player.setBrazo(BRAZO);player.setPierna(PIERNA);
                    player.setEspada(ESPADA);player.setCapa(CAPA);player.setAccesorio(ACCESORIO);
                    player.setVida(VIDA);player.setAtq(ATQ);player.setDef(DEF);
                    player.setNivel(NIVEL);player.setPais(PAIS);player.setEspecialidad(ESPECIALIDAD);
                    player.setImg_perfil(IMG_PERFIL);player.setImg_info(IMG_INFO);


                    //Carga completa del Json
                    listPlayer.add(player);

                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }


        return listPlayer;
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
