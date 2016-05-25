package com.wos.dernv.evilbanefiends.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.logs.L;
import com.wos.dernv.evilbanefiends.myapp.MyApp;
import com.wos.dernv.evilbanefiends.network.Key;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.network.UrlEP;
import com.wos.dernv.evilbanefiends.objects.ClanBatalla;
import com.wos.dernv.evilbanefiends.objects.UserRegistro;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by der_w on 5/25/2016.
 */
public class FrBattleClanActMain extends Fragment {



    ///vars to get data online
    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private ClanBatalla clanBatalla;

    private ProgressDialog progressDialog ;
    private int persistenTry=0;


    //Vars Seccion Datos
    private ImageView sdImgExpand, sdImgReduce;
    private TableLayout tableLayoutSecdatos;

    private ImageView sdImgEdit, sdImgSave, sdImgCancel;
    private TextView textBuffAttValue, textBuffDefValue, textBuffCriValue, textDescValue, textVid1, textVid2, textVid3;
    private EditText editTextBuffAtt, editTextBuffDef, editTextBuffCri,
            editTextDesc, editTextVid1, editTextVid2, editTextVid3;

    private RelativeLayout rConImgFormacion;
    private  ImageView imgFormacion, saveImgFormacion;

    private LinearLayout lConVid1, lConVid2, lConVid3;
    private WebView wbVid1,wbVid2,wbVid3;
    private HorizontalScrollView horizontalScrollViewContenVids;

    private ImageView imgVid1,imgVid2,imgVid3;



    //registro
    UserRegistro userRegistro;

    public  FrBattleClanActMain(){}

    public static FrBattleClanActMain newInstance(){
        FrBattleClanActMain frBattleClanActMain= new FrBattleClanActMain();
        return frBattleClanActMain;
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
        imageLoader=myVolleySingleton.getmImageLoader();


        userRegistro= MyApp.getWritableDatabase().getUserRegistro();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_clan_battle_act_main,container,false);


        setImgFormacion(rootView);
        setSeccionDatos(rootView);
        setSeccionVideos(rootView);
        //holder.wbVid.loadUrl("http://www.youtube.com/embed/" +notifyItem.getUrl_vid() + "?autoplay=1&vq=small");


        enviarPeticionJsonDatosClanBatalla();



        return rootView; /// super.onCreateView(inflater, container, savedInstanceState);
    }


    public void setImgFormacion(final View rootView){
        rConImgFormacion=(RelativeLayout)rootView.findViewById(R.id.rConImgFormacion);
        imgFormacion=(ImageView)rootView.findViewById(R.id.imgFormacion);
        saveImgFormacion=(ImageView)rootView.findViewById(R.id.saveImgFormacion);
        saveImgFormacion.setVisibility(View.GONE);
        YoYo.with(Techniques.BounceInDown)
                .duration(1000)
                .playOn(rootView.findViewById(R.id.rConImgFormacion));

    }
    public void setSeccionDatos(final View rootView){

        sdImgExpand=(ImageView)rootView.findViewById(R.id.sdImgExpand);
        sdImgExpand.setVisibility(View.GONE);
        sdImgReduce=(ImageView)rootView.findViewById(R.id.sdImgReduce);
        sdImgReduce.setVisibility(View.GONE);
        tableLayoutSecdatos=(TableLayout) rootView.findViewById(R.id.secDatos);

        sdImgEdit =(ImageView)rootView.findViewById(R.id.sdImgEditar);
        sdImgEdit.setVisibility(View.GONE);
        sdImgSave =(ImageView)rootView.findViewById(R.id.sdImgSave);
        sdImgSave.setVisibility(View.GONE);
        sdImgCancel =(ImageView)rootView.findViewById(R.id.sdImgCancel);
        sdImgCancel.setVisibility(View.GONE);

        textBuffAttValue =(TextView)rootView.findViewById(R.id.textBuffAttValue);
        textBuffDefValue =(TextView)rootView.findViewById(R.id.textBuffDefValue);
        textBuffCriValue =(TextView)rootView.findViewById(R.id.textBuffCriValue);
        textDescValue =(TextView)rootView.findViewById(R.id.textDescValue);
        textVid1 =(TextView)rootView.findViewById(R.id.textVid1Value);
        textVid1.setVisibility(View.GONE);
        textVid2 =(TextView)rootView.findViewById(R.id.textVid2Value);
        textVid2.setVisibility(View.GONE);
        textVid3 =(TextView)rootView.findViewById(R.id.textVid3Value);
        textVid3.setVisibility(View.GONE);

        editTextBuffAtt =(EditText)rootView.findViewById(R.id.editTextBuffAtt);
        editTextBuffAtt.setVisibility(View.GONE);
        editTextBuffDef =(EditText)rootView.findViewById(R.id.editTextBuffDef);
        editTextBuffDef.setVisibility(View.GONE);
        editTextBuffCri =(EditText)rootView.findViewById(R.id.editTextBuffCri);
        editTextBuffCri.setVisibility(View.GONE);
        editTextDesc =(EditText)rootView.findViewById(R.id.editTextDesc);
        editTextDesc.setVisibility(View.GONE);
        editTextVid1 =(EditText)rootView.findViewById(R.id.editTextVid1);
        editTextVid1.setVisibility(View.GONE);
        editTextVid2 =(EditText)rootView.findViewById(R.id.editTextVid2);
        editTextVid2.setVisibility(View.GONE);
        editTextVid3 =(EditText)rootView.findViewById(R.id.editTextVid3);
        editTextVid3.setVisibility(View.GONE);

        imgVid1=(ImageView)rootView.findViewById(R.id.imgVid1);
        imgVid1.setVisibility(View.GONE);

        imgVid2=(ImageView)rootView.findViewById(R.id.imgVid2);
        imgVid2.setVisibility(View.GONE);

        imgVid3=(ImageView)rootView.findViewById(R.id.imgVid3);
        imgVid3.setVisibility(View.GONE);





    }
    public void setSeccionVideos(final View rootView){

        horizontalScrollViewContenVids=(HorizontalScrollView)rootView.findViewById(R.id.conentScrollVid);
        lConVid1=(LinearLayout)rootView.findViewById(R.id.contenedorWebViewVideo1);
        lConVid2=(LinearLayout)rootView.findViewById(R.id.contenedorWebViewVideo2);
        lConVid3=(LinearLayout)rootView.findViewById(R.id.contenedorWebViewVideo3);

        wbVid1=(WebView)rootView.findViewById(R.id.webViewVideo1);
        wbVid2=(WebView)rootView.findViewById(R.id.webViewVideo2);
        wbVid3=(WebView)rootView.findViewById(R.id.webViewVideo3);

        wbVid1.getSettings().setDomStorageEnabled(true);
        wbVid1.getSettings().setJavaScriptEnabled(true);
        wbVid1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wbVid1.getSettings().setSupportMultipleWindows(true);
        wbVid1.getSettings().setSupportZoom(true);
        wbVid1.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //progressDialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url,Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });

        wbVid2.getSettings().setDomStorageEnabled(true);
        wbVid2.getSettings().setJavaScriptEnabled(true);
        wbVid2.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wbVid2.getSettings().setSupportMultipleWindows(true);
        wbVid2.getSettings().setSupportZoom(true);
        wbVid2.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //progressDialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url,Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });

        wbVid3.getSettings().setDomStorageEnabled(true);
        wbVid3.getSettings().setJavaScriptEnabled(true);
        wbVid3.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wbVid3.getSettings().setSupportMultipleWindows(true);
        wbVid3.getSettings().setSupportZoom(true);
        wbVid3.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //progressDialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url,Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });

        horizontalScrollViewContenVids.setVisibility(View.GONE);
    }




    public void setValuesOfFragment(){
        if(userRegistro.getMiembro().equals("1")){
            textBuffAttValue.setText(clanBatalla.getBuffAtt());
            textBuffDefValue.setText(clanBatalla.getBuffDef());
            textBuffCriValue.setText(clanBatalla.getBuffCri());
            textDescValue.setText(clanBatalla.getDetalle());
        }else {
            textBuffAttValue.setText("Buff de Atk");
            textBuffDefValue.setText("Buff de Def");
            textBuffCriValue.setText("Buff de Cri");
            textDescValue.setText("Formacion - Elite: Listos para el combate, solo los mejores venceran");
        }





        if(clanBatalla.getVid1().equals("noAsig")){
            lConVid1.setVisibility(View.GONE);
            imgVid1.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorBackground));
            textVid1.setVisibility(View.GONE);editTextVid1.setVisibility(View.GONE);
            textVid1.setText("inserte ID de video Youtube");editTextVid1.setText("inserte ID de video Youtube");
        }else{
            lConVid1.setVisibility(View.VISIBLE);
            wbVid1.loadUrl("http://www.youtube.com/embed/" +clanBatalla.getVid1() + "?autoplay=1&vq=small");
            imgVid1.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorTextMenuRed));
        }
        if(clanBatalla.getVid2().equals("noAsig")){
            lConVid2.setVisibility(View.GONE);
            imgVid3.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorBackground));
            textVid2.setVisibility(View.GONE);editTextVid2.setVisibility(View.GONE);
            textVid2.setText("inserte ID de video Youtube");editTextVid2.setText("inserte ID de video Youtube");
        }else{
            lConVid2.setVisibility(View.VISIBLE);
            wbVid2.loadUrl("http://www.youtube.com/embed/" +clanBatalla.getVid2() + "?autoplay=1&vq=small");
            imgVid2.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorTextMenuRed));
        }
        if(clanBatalla.getVid3().equals("noAsig")){
            lConVid3.setVisibility(View.GONE);
            imgVid3.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorBackground));
            textVid3.setVisibility(View.GONE);editTextVid3.setVisibility(View.GONE);
            textVid2.setText("inserte ID de video Youtube");editTextVid2.setText("inserte ID de video Youtube");
        }else{
            lConVid3.setVisibility(View.VISIBLE);
            wbVid3.loadUrl("http://www.youtube.com/embed/" +clanBatalla.getVid3() + "?autoplay=1&vq=small");
            imgVid3.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorTextMenuRed));
        }
        if(clanBatalla.getVid1().equals("noAsig")&& clanBatalla.getVid2().equals("noAsig")&&
                clanBatalla.getVid3().equals("noAsig")){
            horizontalScrollViewContenVids.setVisibility(View.GONE);
        }else{
            horizontalScrollViewContenVids.setVisibility(View.VISIBLE);
        }


        loadImage(clanBatalla.getImgFormacion(),imgFormacion);



    }


    private void loadImage(String urlThumbnail , final ImageView img) {
        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                    img.setImageBitmap(response.getBitmap());

                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    loadImage("http://ebfiends.esy.es/public/Misc/Jugador/espera_pj.jpg",img);
                }
            });
        }
    }



    ///Llamados Player////
    public void enviarPeticionJsonDatosClanBatalla(){

        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                UrlEP.EBFIENDS_CLAN_BATALLA
                , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        clanBatalla=parseJsonResponse(response);
                        // MiAplicativo.getWritableDatabase().insertMateriaPensumIndividual(listMateria, ma_modulo, true);//IMPORTANTE

                        setValuesOfFragment();


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

                            enviarPeticionJsonDatosClanBatalla();
                        }
                    });
                    alertDialog.show();
                }else
                    enviarPeticionJsonDatosClanBatalla();


            }
        });

        //sin esta linea no se puede hacer la peticion al server
        requestQueue.add(request);
    }
    public ClanBatalla parseJsonResponse(JSONObject response){
        ClanBatalla clanBatalla=new ClanBatalla();
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


                String BUFF_ATT="NA",BUFF_DEF="NA",BUFF_CRI="NA";
                String DETALLE = "NA";
                String VID1 = "NA",VID2="NA",VID3="NA";
                String IMG_FORMACION="NA";

                JSONObject currentPj = response.getJSONObject(Key.JsGetClanBatalla.CLAN_BATALLA);
                if(currentPj.has(Key.JsGetClanBatalla.BUFF_ATT)&&
                        !currentPj.isNull(Key.JsGetClanBatalla.BUFF_ATT)){
                    BUFF_ATT=currentPj.getString(Key.JsGetClanBatalla.BUFF_ATT);
                }
                if(currentPj.has(Key.JsGetClanBatalla.BUFF_DEF)&&
                        !currentPj.isNull(Key.JsGetClanBatalla.BUFF_DEF)){
                    BUFF_DEF=currentPj.getString(Key.JsGetClanBatalla.BUFF_DEF);
                }
                if(currentPj.has(Key.JsGetClanBatalla.BUFF_CRI)&&
                        !currentPj.isNull(Key.JsGetClanBatalla.BUFF_CRI)){
                    BUFF_CRI=currentPj.getString(Key.JsGetClanBatalla.BUFF_CRI);
                }
                if(currentPj.has(Key.JsGetClanBatalla.DETALLE)&&
                        !currentPj.isNull(Key.JsGetClanBatalla.DETALLE)){
                    DETALLE=currentPj.getString(Key.JsGetClanBatalla.DETALLE);
                }
                if(currentPj.has(Key.JsGetClanBatalla.VID1)&&
                        !currentPj.isNull(Key.JsGetClanBatalla.VID1)){
                    VID1=currentPj.getString(Key.JsGetClanBatalla.VID1);
                }
                if(currentPj.has(Key.JsGetClanBatalla.VID2)&&
                        !currentPj.isNull(Key.JsGetClanBatalla.VID2)){
                    VID2=currentPj.getString(Key.JsGetClanBatalla.VID2);
                }
                if(currentPj.has(Key.JsGetClanBatalla.VID3)&&
                        !currentPj.isNull(Key.JsGetClanBatalla.VID3)){
                    VID3=currentPj.getString(Key.JsGetClanBatalla.VID3);
                }
                if(currentPj.has(Key.JsGetClanBatalla.IMG_FORMACION)&&
                        !currentPj.isNull(Key.JsGetClanBatalla.IMG_FORMACION)){
                    IMG_FORMACION=currentPj.getString(Key.JsGetClanBatalla.IMG_FORMACION);
                }



                clanBatalla=new ClanBatalla();
                clanBatalla.setBuffAtt(BUFF_ATT);clanBatalla.setBuffDef(BUFF_DEF);clanBatalla.setBuffCri(BUFF_CRI);
                clanBatalla.setDetalle(DETALLE);
                clanBatalla.setVid1(VID1);clanBatalla.setVid2(VID2);clanBatalla.setVid3(VID3);
                clanBatalla.setImgFormacion(IMG_FORMACION);



            }catch (JSONException e){
                e.printStackTrace();
            }
        }


        return clanBatalla;
    }






}
