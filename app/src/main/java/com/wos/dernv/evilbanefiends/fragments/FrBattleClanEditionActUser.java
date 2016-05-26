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
import android.os.Build;
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
 * Created by der_w on 5/24/2016.
 */
public class FrBattleClanEditionActUser extends Fragment{


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
    private int vid1Set=0, vid2Set=0, vid3Set=0;



    //registro
    UserRegistro userRegistro;

    public  FrBattleClanEditionActUser(){}

    public static FrBattleClanEditionActUser newInstance(String CODIGO){
        FrBattleClanEditionActUser frBattleClanEditionActUser= new FrBattleClanEditionActUser();
        Bundle args = new Bundle();
        args.putString("CODIGO",CODIGO);
        frBattleClanEditionActUser.setArguments(args);
        return frBattleClanEditionActUser;
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
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_clan_battle_act_main,container,false);
        viewTest=rootView;

        setImgFormacion(rootView);
        setSeccionDatos(rootView);
        setSeccionVideos(rootView);
        //holder.wbVid.loadUrl("http://www.youtube.com/embed/" +notifyItem.getUrl_vid() + "?autoplay=1&vq=small");


        enviarPeticionJsonDatosClanBatalla();

        reduceContentSeccionDatos();


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

        imgFormacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(RESULT_LOAD_IMG);
                imgFormacion.setClickable(false);
            }
        });
        saveImgFormacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encodeImg();
               // sendImageToBackend(getContext(),encodeImageString);
            }
        });
    }
    public void setSeccionDatos(final View rootView){

        sdImgExpand=(ImageView)rootView.findViewById(R.id.sdImgExpand);
        sdImgReduce=(ImageView)rootView.findViewById(R.id.sdImgReduce);
        tableLayoutSecdatos=(TableLayout) rootView.findViewById(R.id.secDatos);

        sdImgEdit =(ImageView)rootView.findViewById(R.id.sdImgEditar);
        sdImgSave =(ImageView)rootView.findViewById(R.id.sdImgSave);
        sdImgCancel =(ImageView)rootView.findViewById(R.id.sdImgCancel);

        textBuffAttValue =(TextView)rootView.findViewById(R.id.textBuffAttValue);
        textBuffDefValue =(TextView)rootView.findViewById(R.id.textBuffDefValue);
        textBuffCriValue =(TextView)rootView.findViewById(R.id.textBuffCriValue);
        textDescValue =(TextView)rootView.findViewById(R.id.textDescValue);
        textVid1 =(TextView)rootView.findViewById(R.id.textVid1Value);
        textVid2 =(TextView)rootView.findViewById(R.id.textVid2Value);
        textVid3 =(TextView)rootView.findViewById(R.id.textVid3Value);

        editTextBuffAtt =(EditText)rootView.findViewById(R.id.editTextBuffAtt);
        editTextBuffDef =(EditText)rootView.findViewById(R.id.editTextBuffDef);
        editTextBuffCri =(EditText)rootView.findViewById(R.id.editTextBuffCri);
        editTextDesc =(EditText)rootView.findViewById(R.id.editTextDesc);
        editTextVid1 =(EditText)rootView.findViewById(R.id.editTextVid1);
        editTextVid2 =(EditText)rootView.findViewById(R.id.editTextVid2);
        editTextVid3 =(EditText)rootView.findViewById(R.id.editTextVid3);

        imgVid1=(ImageView)rootView.findViewById(R.id.imgVid1);
        imgVid1.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorBackground));
        imgVid2=(ImageView)rootView.findViewById(R.id.imgVid2);
        imgVid2.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorBackground));
        imgVid3=(ImageView)rootView.findViewById(R.id.imgVid3);
        imgVid3.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorBackground));

        imgVid1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vid1Set==0){
                    imgVid1.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorTextMenuRed));
                    vid1Set=1;
                    if(setEdOrView==1){
                        editTextVid1.setVisibility(View.VISIBLE);
                    }else if(setEdOrView==0){
                        textVid1.setVisibility(View.VISIBLE);
                    }
                }
                else if(vid1Set==1){
                    imgVid1.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorBackground));
                    vid1Set=0;
                    if(setEdOrView==1){
                        editTextVid1.setVisibility(View.GONE);
                    }else if(setEdOrView==0){
                        textVid1.setVisibility(View.GONE);
                    }
                }
            }
        });
        imgVid2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vid2Set==0){
                    imgVid2.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorTextMenuRed));
                    vid2Set=1;
                    if(setEdOrView==1){
                        editTextVid2.setVisibility(View.VISIBLE);
                    }else if(setEdOrView==0){
                        textVid2.setVisibility(View.VISIBLE);
                    }
                }
                else if(vid2Set==1){
                    imgVid2.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorBackground));
                    vid2Set=0;
                    if(setEdOrView==1){
                        editTextVid2.setVisibility(View.GONE);
                    }else if(setEdOrView==0){
                        textVid2.setVisibility(View.GONE);
                    }
                }
            }
        });
        imgVid3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vid3Set==0){
                    imgVid3.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorTextMenuRed));
                    vid3Set=1;
                    if(setEdOrView==1){
                        editTextVid3.setVisibility(View.VISIBLE);
                    }else if(setEdOrView==0){
                        textVid3.setVisibility(View.VISIBLE);
                    }
                }
                else if(vid3Set==1){
                    imgVid3.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorBackground));
                    vid3Set=0;
                    if(setEdOrView==1){
                        editTextVid3.setVisibility(View.GONE);
                    }else if(setEdOrView==0){
                        textVid3.setVisibility(View.GONE);
                    }
                }
            }
        });

        sdImgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandContentSeccionDatos(rootView);
            }
        });
        sdImgReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reduceContentSeccionDatos();
            }
        });
        sdImgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentEditionSecDatos();
            }
        });
        sdImgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentViewSecDatos();
                //L.t(getContext(),"Vida: "+editTextBuffAtt.getText().toString());
               /* sendDatosBatallaToBackend(getContext(), editTextBuffAtt.getText().toString(),
                        editTextBuffDef.getText().toString(), editTextBuffCri.getText().toString(),
                        editTextDesc.getText().toString(), editTextVid1.getText().toString(),
                        editTextVid2.getText().toString(),userRegistro.getNick_name());*/

                String auxVid1="noAsig",auxVid2="noAsig",auxVid3="noAsig";

                if(vid1Set==1){ auxVid1=editTextVid1.getText().toString();}
                else if(vid1Set==0){auxVid1="noAsig";}

                if(vid2Set==1){ auxVid2=editTextVid2.getText().toString();}
                else if(vid2Set==0){auxVid2="noAsig";}

                if(vid3Set==1){ auxVid3=editTextVid3.getText().toString();}
                else if(vid3Set==0){auxVid3="noAsig";}

                sendDatosBatallaToBackend(getContext(),editTextBuffDef.getText().toString(),
                        editTextBuffCri.getText().toString(),editTextDesc.getText().toString(),auxVid1,auxVid2,auxVid3,
                        editTextBuffAtt.getText().toString());
            }
        });
        sdImgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentViewSecDatos();
            }
        });

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

    public void expandContentSeccionDatos(View rootView){
        showContentViewSecDatos();
        YoYo.with(Techniques.BounceInDown)
                .duration(1000)
                .playOn(rootView.findViewById(R.id.secDatos));
        YoYo.with(Techniques.ZoomIn)
                .duration(1000)
                .playOn(rootView.findViewById(R.id.sdImgReduce));
        YoYo.with(Techniques.Wave)
                .duration(2000)
                .playOn(rootView.findViewById(R.id.sdImgEditar));
        tableLayoutSecdatos.setVisibility(View.VISIBLE);
        sdImgReduce.setVisibility(View.VISIBLE);
        sdImgExpand.setVisibility(View.GONE);
        sdImgEdit.setVisibility(View.VISIBLE);

    }
    public void reduceContentSeccionDatos(){
        showContentViewSecDatos();
        tableLayoutSecdatos.setVisibility(View.GONE);
        sdImgReduce.setVisibility(View.GONE);
        sdImgExpand.setVisibility(View.VISIBLE);
        sdImgEdit.setVisibility(View.GONE);
    }

    private int setEdOrView=0;
    public void showContentEditionSecDatos(){
        setEdOrView=1;
        textBuffAttValue.setVisibility(View.GONE);
        textBuffDefValue.setVisibility(View.GONE);
        textBuffCriValue.setVisibility(View.GONE);
        textDescValue.setVisibility(View.GONE);
        textVid1.setVisibility(View.GONE);
        textVid2.setVisibility(View.GONE);
        textVid3.setVisibility(View.GONE);

        editTextBuffAtt.setVisibility(View.VISIBLE);
        editTextBuffDef.setVisibility(View.VISIBLE);
        editTextBuffCri.setVisibility(View.VISIBLE);
        editTextDesc.setVisibility(View.VISIBLE);

        if(vid1Set==1)
        editTextVid1.setVisibility(View.VISIBLE);
        if (vid2Set==1)
        editTextVid2.setVisibility(View.VISIBLE);
        if(vid3Set==1)
        editTextVid3.setVisibility(View.VISIBLE);

        sdImgEdit.setVisibility(View.GONE);
        sdImgSave.setVisibility(View.VISIBLE);
        sdImgCancel.setVisibility(View.VISIBLE);

    }
    public void showContentViewSecDatos(){
        setEdOrView=0;
        textBuffAttValue.setVisibility(View.VISIBLE);
        textBuffDefValue.setVisibility(View.VISIBLE);
        textBuffCriValue.setVisibility(View.VISIBLE);
        textDescValue.setVisibility(View.VISIBLE);
        if(vid1Set==1)
        textVid1.setVisibility(View.VISIBLE);
        if(vid2Set==1)
        textVid2.setVisibility(View.VISIBLE);
        if(vid3Set==1)
        textVid3.setVisibility(View.VISIBLE);

        editTextBuffAtt.setVisibility(View.GONE);
        editTextBuffDef.setVisibility(View.GONE);
        editTextBuffCri.setVisibility(View.GONE);
        editTextDesc.setVisibility(View.GONE);
        editTextVid1.setVisibility(View.GONE);
        editTextVid2.setVisibility(View.GONE);
        editTextVid3.setVisibility(View.GONE);

        sdImgEdit.setVisibility(View.VISIBLE);
        sdImgSave.setVisibility(View.GONE);
        sdImgCancel.setVisibility(View.GONE);

    }

    private View viewTest;
    public void setValuesOfFragment(){
        textBuffAttValue.setText(clanBatalla.getBuffAtt());
        textBuffDefValue .setText(clanBatalla.getBuffDef());
        textBuffCriValue .setText(clanBatalla.getBuffCri());
        textDescValue .setText(clanBatalla.getDetalle());
        textVid1 .setText(clanBatalla.getVid1());
        textVid2 .setText(clanBatalla.getVid2());
        textVid3 .setText(clanBatalla.getVid3());

        editTextBuffAtt .setText(clanBatalla.getBuffAtt());
        editTextBuffDef.setText(clanBatalla.getBuffDef());
        editTextBuffCri .setText(clanBatalla.getBuffCri());
        editTextDesc.setText(clanBatalla.getDetalle());
        editTextVid1  .setText(clanBatalla.getVid1());
        editTextVid2 .setText(clanBatalla.getVid2());
        editTextVid3 .setText(clanBatalla.getVid3());

        if(clanBatalla.getVid1().equals("noAsig")){
            lConVid1.setVisibility(View.GONE); vid1Set=0;
            imgVid1.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorBackground));
            textVid1.setVisibility(View.GONE);editTextVid1.setVisibility(View.GONE);
            textVid1.setText("inserte ID de video Youtube");editTextVid1.setText("inserte ID de video Youtube");
        }else{
            lConVid1.setVisibility(View.VISIBLE); vid1Set=1;
            wbVid1.loadUrl("http://www.youtube.com/embed/" +clanBatalla.getVid1() + "?autoplay=1&vq=small");
            imgVid1.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorTextMenuRed));
            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                lConVid1.setVisibility(View.GONE);
                LinearLayout linearLayout=(LinearLayout)viewTest.findViewById(R.id.coniVidLink1);
                linearLayout.setVisibility(View.VISIBLE);
                ImageView ivid1link=(ImageView)viewTest.findViewById(R.id.iVidLink1);
                ivid1link.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorTextMenuRed));
                ivid1link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.youtube.com/watch?v="+clanBatalla.getVid1())));
                    }
                });
            }
        }
        if(clanBatalla.getVid2().equals("noAsig")){
            lConVid2.setVisibility(View.GONE);vid2Set=0;
            imgVid3.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorBackground));
            textVid2.setVisibility(View.GONE);editTextVid2.setVisibility(View.GONE);
            textVid2.setText("inserte ID de video Youtube");editTextVid2.setText("inserte ID de video Youtube");
        }else{
            lConVid2.setVisibility(View.VISIBLE);vid2Set=1;
            wbVid2.loadUrl("http://www.youtube.com/embed/" +clanBatalla.getVid2() + "?autoplay=1&vq=small");
            imgVid2.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorTextMenuRed));
            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                lConVid2.setVisibility(View.GONE);
                LinearLayout linearLayout=(LinearLayout)viewTest.findViewById(R.id.coniVidLink2);
                linearLayout.setVisibility(View.VISIBLE);
                ImageView ivid1link=(ImageView)viewTest.findViewById(R.id.iVidLink2);
                ivid1link.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorTextMenuRed));
                ivid1link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.youtube.com/watch?v="+clanBatalla.getVid2())));
                    }
                });
            }
        }
        if(clanBatalla.getVid3().equals("noAsig")){
            lConVid3.setVisibility(View.GONE);vid3Set=0;
            imgVid3.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorBackground));
            textVid3.setVisibility(View.GONE);editTextVid3.setVisibility(View.GONE);
            textVid2.setText("inserte ID de video Youtube");editTextVid2.setText("inserte ID de video Youtube");
        }else{
            lConVid3.setVisibility(View.VISIBLE);vid3Set=1;
            wbVid3.loadUrl("http://www.youtube.com/embed/" +clanBatalla.getVid3() + "?autoplay=1&vq=small");
            imgVid3.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorTextMenuRed));
            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                lConVid3.setVisibility(View.GONE);
                LinearLayout linearLayout=(LinearLayout)viewTest.findViewById(R.id.coniVidLink3);
                linearLayout.setVisibility(View.VISIBLE);
                ImageView ivid1link=(ImageView)viewTest.findViewById(R.id.iVidLink3);
                ivid1link.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorTextMenuRed));
                ivid1link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.youtube.com/watch?v="+clanBatalla.getVid3())));
                    }
                });
            }
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


    //Upload Datos de batalla de Clan
    private void sendDatosBatallaToBackend(
            final Context context, final String buffDef, final String buffCri,
            final String detalle, final String vid1,
            final String vid2, final String vid3, final String buffAtt) {
        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        String url = "http://ebfiends.esy.es/public/update_clan_batalla";

        Map<String, String> map = new HashMap<>();
        map.put("uploadDatosBatalla", "1");
        map.put("buffAtt",buffAtt);
        map.put("buffDef",buffDef);
        map.put("buffCri",buffCri);
        map.put("detalle",detalle);
        map.put("vid1",vid1);
        map.put("vid2",vid2);
        map.put("vid3",vid3);
        map.put("codigo",getArguments().getString("CODIGO"));




        JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                url,new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try{
                    String estado="NA",tipo="NA", msj="NA";
                    if(response.has(Key.Answer.ESTADO)&&
                            !response.isNull(Key.Answer.ESTADO)){
                        estado = response.getString(Key.Answer.ESTADO);
                    }
                    if(response.has(Key.Answer.MSJ)&&
                            !response.isNull(Key.Answer.MSJ)){
                        msj = response.getString(Key.Answer.MSJ);
                    }

                    if(estado.equals("1")){
                        L.t(context,msj);
                        enviarPeticionJsonDatosClanBatalla();

                    }else if(estado.equals("2")){
                        L.t(context,msj);
                    }else if(estado.equals("3")){
                        L.t(context,msj);
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

                            sendDatosBatallaToBackend(context,buffDef,buffCri,detalle,vid1,vid2,vid3,buffAtt);
                        }
                    });
                    alertDialog.show();

                }else
                {
                    sendDatosBatallaToBackend(context,buffDef,buffCri,detalle,vid1,vid2,vid3,buffAtt);
                }
            }
        });
        requestQueue.add(request);
    }

    private void sendImageToBackend(
            final Context context, final String imgEncode) {
        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        String url = "http://ebfiends.esy.es/public/update_clan_batalla_img";

        Map<String, String> map = new HashMap<>();
        map.put("uploadImgClan", "1");
        map.put("imgEncode",imgEncode);
        map.put("codigo",getArguments().getString("CODIGO"));




        JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                url,new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try{
                    String estado="NA",tipo="NA", msj="NA";
                    if(response.has(Key.Answer.ESTADO)&&
                            !response.isNull(Key.Answer.ESTADO)){
                        estado = response.getString(Key.Answer.ESTADO);
                    }
                    if(response.has(Key.Answer.MSJ)&&
                            !response.isNull(Key.Answer.MSJ)){
                        msj = response.getString(Key.Answer.MSJ);
                    }

                    if(estado.equals("1")){
                        L.t(context,msj);
                        saveImgFormacion.setVisibility(View.GONE);
                        enviarPeticionJsonDatosClanBatalla();
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

                            sendImageToBackend(context, imgEncode);
                        }
                    });
                    alertDialog.show();

                }else
                {
                    sendImageToBackend(context, imgEncode);
                }
            }
        });
        requestQueue.add(request);
    }




    //Sector de carga de imagen

    String imgPath,  encodeImageString;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;

    public void loadImagefromGallery(int result) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, result);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if(requestCode == RESULT_LOAD_IMG && resultCode== getActivity().RESULT_OK
                    && null != data){
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor =getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();

                //Setear imagen en el app
                imgFormacion.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));


                saveImgFormacion.setVisibility(View.VISIBLE);
                imgFormacion.setClickable(true);

            }else
            {
                imgFormacion.setClickable(true);
            }





        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void encodeImg(){
        progressDialog.setMessage("Comprimiendo imagen ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                //super.onPreExecute();

            }
            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options0 = new BitmapFactory.Options();
                options0.inSampleSize = 1;
                // options.inJustDecodeBounds = true;
                options0.inScaled = false;
                options0.inDither = false;
                options0.inPreferredConfig = Bitmap.Config.ARGB_8888;

                bitmap = BitmapFactory.decodeFile(imgPath);

                ByteArrayOutputStream baos0 = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos0);
                byte[] imageBytes0 = baos0.toByteArray();


                encodeImageString= Base64.encodeToString(imageBytes0, Base64.DEFAULT);

                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                //super.onPostExecute(s);
                checkImgEncode();


            }
        }.execute(null,null,null);
    }
    public void checkImgEncode(){
        L.t(getContext(),"IMG En: "+encodeImageString.substring(0,50))
        ;progressDialog.dismiss();
        sendImageToBackend(getContext() ,encodeImageString);
    }



}
