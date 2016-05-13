package com.wos.dernv.evilbanefiends.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.logs.L;
import com.wos.dernv.evilbanefiends.network.Key;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.network.UrlEP;
import com.wos.dernv.evilbanefiends.objects.EqPerfecto;
import com.wos.dernv.evilbanefiends.objects.Player;
import com.wos.dernv.evilbanefiends.objects.WikiEquipo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by der_w on 5/11/2016.
 */
public class AdapterRcEqPerfecto extends RecyclerView.Adapter<AdapterRcEqPerfecto.RcEqPerfectoViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<EqPerfecto> listEqPerfecto = new ArrayList<>();

    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private ProgressDialog progressDialog ;private int persistenTry=0;

    private ArrayList<Boolean> listVisorDetalle=new ArrayList<>();

    private ClickCallBack clickCallBack;
    private Context context;

    public AdapterRcEqPerfecto(Context context, ClickCallBack clickCallBack){
        inflater=LayoutInflater.from(context);
        myVolleySingleton = MyVolleySingleton.getsInstance();
        requestQueue=myVolleySingleton.getmRequestQueue();
        imageLoader = myVolleySingleton.getmImageLoader();
        this.context=context;
        this.clickCallBack=clickCallBack;
        progressDialog = new ProgressDialog(context); persistenTry=0;
    }
    public void setEqPerfectoList(ArrayList<EqPerfecto> listEqPerfecto){
        this.listEqPerfecto=listEqPerfecto;
        for (int i=0;i<listEqPerfecto.size();i++){
            listVisorDetalle.add(false);
        }
        notifyItemChanged(0,listEqPerfecto.size());
    }
    @Override
    public RcEqPerfectoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_rc_fr_eq_perfecto,parent,false);
        return new RcEqPerfectoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RcEqPerfectoViewHolder holder, int position) {
        boolean visorDetalle;
        visorDetalle=listVisorDetalle.get(position);



        EqPerfecto currentEQP= listEqPerfecto.get(position);
        holder.equipoNombre.setText(currentEQP.getCodeName());

        String urlFullview=currentEQP.getImg_root();
        loadImages(urlFullview,holder,"pic");
        enviarPeticionJson(currentEQP.getCodeName(),holder,"view",position);

     /*   holder.equipoAl1.setText("Hab. 01: "+currentEQP.getAl1());
        holder.equipoAl2.setText("Hab. 02: "+currentEQP.getAl2());
        holder.equipoAl3.setText("Hab. 03: "+currentEQP.getAl3());
        holder.equipoUsuario.setText("Usuario: "+currentEQP.getUsuario());
        holder.equipoOrigen.setText("Origen: "+currentEQP.getOrigen());
        holder.equipoDetalle.setText("Detalles: "+currentEQP.getDetalle());*/


        if(!visorDetalle){
            holder.equipoAl1.setText("Hab. 01: "+currentEQP.getAl1());
            holder.equipoAl2.setText("Hab. 02: "+currentEQP.getAl2());
            holder.equipoAl3.setText("Hab. 03: "+currentEQP.getAl3());

            holder.equipoUsuario.setVisibility(View.GONE);
            holder.equipoOrigen.setVisibility(View.GONE);
            holder.equipoDetalle.setVisibility(View.GONE);
            holder.equipoFullView.setVisibility(View.GONE);
            holder.showHideButton.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }else{
            holder.equipoAl1.setText("Hab. 01: "+currentEQP.getAl1());
            holder.equipoAl2.setText("Hab. 02: "+currentEQP.getAl2());
            holder.equipoAl3.setText("Hab. 03: "+currentEQP.getAl3());

            holder.equipoUsuario.setVisibility(View.VISIBLE);
            holder.equipoOrigen.setVisibility(View.VISIBLE);
            holder.equipoDetalle.setVisibility(View.VISIBLE);

            holder.equipoUsuario.setText("Usuario: "+currentEQP.getUsuario());
            holder.equipoOrigen.setText("Origen: "+currentEQP.getOrigen());
            holder.equipoDetalle.setText("Detalles: "+currentEQP.getDetalle());

            holder.equipoFullView.setVisibility(View.VISIBLE);

            holder.showHideButton.setColorFilter(ContextCompat.getColor(context,R.color.colorWhite));
        }


    }

    @Override
    public int getItemCount() {
        return listEqPerfecto.size();
    }


    private void loadImages(String urlThumbnail, final RcEqPerfectoViewHolder holder, final String tipo) {
        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if(tipo.equals("pic")){
                        holder.equipoImage.setImageBitmap(response.getBitmap());
                    }else if(tipo.equals("view")){
                        holder.equipoFullView.setImageBitmap(response.getBitmap());
                    }

                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    private  WikiEquipo wikiEquipo = new WikiEquipo();
    public void enviarPeticionJson(final String codeName,
                                   final RcEqPerfectoViewHolder holder, final String tipo, final int position){

        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                UrlEP.EBFIENDS_W_EQ+ Key.EndPoint.Wk_codeName+codeName
                , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        wikiEquipo=parseJsonResponse(response,codeName);
                        holder.equipoNombre.setText(wikiEquipo.getName());
                        if(listVisorDetalle.get(position)){
                            loadImages(wikiEquipo.getImg_view(),holder,tipo);
                        }
                        // MiAplicativo.getWritableDatabase().insertMateriaPensumIndividual(listMateria, ma_modulo, true);//IMPORTANTE


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

                        auxError = "Fuera de tiempo"; // getString(R.string.error_timeOut);
                    } else if (error instanceof AuthFailureError) {

                        auxError = "Fallo en ruta"; // getString(R.string.error_AuthFail);
                    } else if (error instanceof ServerError) {

                        auxError = "Error en la nube";//getString(R.string.error_Server);
                    } else if (error instanceof NetworkError) {

                        auxError = "Error de Red";//getString(R.string.error_NetWork);
                    } else if (error instanceof ParseError) {

                        auxError = "Error de conexion";//getString(R.string.error_NetWork);
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

                            enviarPeticionJson(codeName,holder,tipo,position);
                        }
                    });
                    alertDialog.show();
                }else
                    enviarPeticionJson(codeName,holder,tipo,position);


            }
        });

        //sin esta linea no se puede hacer la peticion al server
        requestQueue.add(request);
    }
    public WikiEquipo parseJsonResponse(JSONObject response, String codeName){
        WikiEquipo wikiEquipo = new WikiEquipo();

        //  if(response.has(Key.EndPointMateria.KEY_ESTADO)&&
        //        !response.isNull(Key.EndPointMateria.KEY_ESTADO))

        if(response==null || response.length()>0){
            try{
                progressDialog.dismiss();
                persistenTry=0;


                String CODENAME="NA" ;
                String NAME="NA" ;
                String CLASE="NA";
                String HB1="NA",HB2="NA",HB3="NA",HB4="NA",HB5="NA",HB6="NA" ;
                String IMG_IC00="NA",IMG_IC01="NA",IMG_IC02="NA",
                        IMG_IC03="NA",IMG_IC04="NA",IMG_IC05="NA";
                String IMG_VIEW="NA";

                JSONObject currentWikiEQ= response.getJSONObject(Key.JsGetWikiEquipo.WIKI_EQUIPO);
                if(currentWikiEQ.has(Key.JsGetWikiEquipo.CODENAME)&&
                        !currentWikiEQ.isNull(Key.JsGetWikiEquipo.CODENAME)){
                    CODENAME=currentWikiEQ.getString(Key.JsGetWikiEquipo.CODENAME);
                }
                if(currentWikiEQ.has(Key.JsGetWikiEquipo.NAME)&&
                        !currentWikiEQ.isNull(Key.JsGetWikiEquipo.NAME)){
                    NAME=currentWikiEQ.getString(Key.JsGetWikiEquipo.NAME);
                }
                if(currentWikiEQ.has(Key.JsGetWikiEquipo.CLASE)&&
                        !currentWikiEQ.isNull(Key.JsGetWikiEquipo.CLASE)){
                    CLASE=currentWikiEQ.getString(Key.JsGetWikiEquipo.CLASE);
                }
                if(currentWikiEQ.has(Key.JsGetWikiEquipo.HB1)&&
                        !currentWikiEQ.isNull(Key.JsGetWikiEquipo.HB1)){
                    HB1=currentWikiEQ.getString(Key.JsGetWikiEquipo.HB1);
                }
                if(currentWikiEQ.has(Key.JsGetWikiEquipo.HB2)&&
                        !currentWikiEQ.isNull(Key.JsGetWikiEquipo.HB2)){
                    HB2=currentWikiEQ.getString(Key.JsGetWikiEquipo.HB2);
                }
                if(currentWikiEQ.has(Key.JsGetWikiEquipo.HB3)&&
                        !currentWikiEQ.isNull(Key.JsGetWikiEquipo.HB3)){
                    HB3=currentWikiEQ.getString(Key.JsGetWikiEquipo.HB3);
                }



                if(currentWikiEQ.has(Key.JsGetWikiEquipo.IMG_IC00)&&
                        !currentWikiEQ.isNull(Key.JsGetWikiEquipo.IMG_IC00)){
                    IMG_IC00=currentWikiEQ.getString(Key.JsGetWikiEquipo.IMG_IC00);
                }
                if(currentWikiEQ.has(Key.JsGetWikiEquipo.IMG_IC01)&&
                        !currentWikiEQ.isNull(Key.JsGetWikiEquipo.IMG_IC01)){
                    IMG_IC01=currentWikiEQ.getString(Key.JsGetWikiEquipo.IMG_IC01);
                }
                if(currentWikiEQ.has(Key.JsGetWikiEquipo.IMG_IC02)&&
                        !currentWikiEQ.isNull(Key.JsGetWikiEquipo.IMG_IC02)){
                    IMG_IC02=currentWikiEQ.getString(Key.JsGetWikiEquipo.IMG_IC02);
                }

                if(currentWikiEQ.has(Key.JsGetWikiEquipo.IMG_IC03)&&
                        !currentWikiEQ.isNull(Key.JsGetWikiEquipo.IMG_IC03)){
                    IMG_IC03=currentWikiEQ.getString(Key.JsGetWikiEquipo.IMG_IC03);
                }
                if(currentWikiEQ.has(Key.JsGetWikiEquipo.IMG_IC04)&&
                        !currentWikiEQ.isNull(Key.JsGetWikiEquipo.IMG_IC04)){
                    IMG_IC04=currentWikiEQ.getString(Key.JsGetWikiEquipo.IMG_IC04);
                }
                if(currentWikiEQ.has(Key.JsGetWikiEquipo.IMG_IC05)&&
                        !currentWikiEQ.isNull(Key.JsGetWikiEquipo.IMG_IC05)){
                    IMG_IC05=currentWikiEQ.getString(Key.JsGetWikiEquipo.IMG_IC05);
                }
                if(currentWikiEQ.has(Key.JsGetWikiEquipo.IMG_VIEW)&&
                        !currentWikiEQ.isNull(Key.JsGetWikiEquipo.IMG_VIEW)){
                    IMG_VIEW=currentWikiEQ.getString(Key.JsGetWikiEquipo.IMG_VIEW);
                }

                wikiEquipo.setCodeName(CODENAME);
                wikiEquipo.setName(NAME);
                wikiEquipo.setClase(CLASE);

                wikiEquipo.setHb1(HB1);wikiEquipo.setHb2(HB2);wikiEquipo.setHb3(HB3);
                wikiEquipo.setHb4(HB4);wikiEquipo.setHb5(HB5);wikiEquipo.setHb6(HB6);

                wikiEquipo.setImg_ic00(IMG_IC00);wikiEquipo.setImg_ic01(IMG_IC01);wikiEquipo.setImg_ic02(IMG_IC02);
                wikiEquipo.setImg_ic03(IMG_IC03);wikiEquipo.setImg_ic04(IMG_IC04);wikiEquipo.setImg_ic05(IMG_IC05);

                wikiEquipo.setImg_view(IMG_VIEW);



            }catch (JSONException e){
                e.printStackTrace();
            }
        }


        return wikiEquipo;
    }




    public  class RcEqPerfectoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout;
        ImageView equipoImage, equipoFullView;
        ImageView showHideButton;
        TextView equipoNombre;
        TextView equipoAl1,equipoAl2,equipoAl3,equipoUsuario,equipoOrigen,equipoDetalle;

        public RcEqPerfectoViewHolder(View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            equipoImage=(ImageView)itemView.findViewById(R.id.equipoImage);
            equipoFullView=(ImageView)itemView.findViewById(R.id.equipoFullView);
            showHideButton=(ImageView)itemView.findViewById(R.id.showOrHideContentButton);

            equipoNombre=(TextView)itemView.findViewById(R.id.equipoNombre);
            equipoAl1=(TextView)itemView.findViewById(R.id.equipoAl1);
            equipoAl2=(TextView)itemView.findViewById(R.id.equipoAl2);
            equipoAl3=(TextView)itemView.findViewById(R.id.equipoAl3);
            equipoUsuario=(TextView)itemView.findViewById(R.id.equipoUsuario);
            equipoOrigen=(TextView)itemView.findViewById(R.id.equipoOrigen);
            equipoDetalle=(TextView)itemView.findViewById(R.id.equipoDetalle);

            relativeLayout.setOnClickListener(this);
            showHideButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.bodyRelative)){

            }
            if(v==v.findViewById(R.id.subItemImage01)){

            }
            if (v==v.findViewById(R.id.subItemImage02)){

            }
            if(v==v.findViewById(R.id.showOrHideContentButton)){
                boolean visorDetalle;
                visorDetalle=listVisorDetalle.get(getAdapterPosition());
                if(visorDetalle==true){
                    listVisorDetalle.set(getAdapterPosition(),false);
                }
                else if(visorDetalle==false){
                    listVisorDetalle.set(getAdapterPosition(),true);
                }
                notifyItemChanged(getAdapterPosition());
            }
        }
    }
}
