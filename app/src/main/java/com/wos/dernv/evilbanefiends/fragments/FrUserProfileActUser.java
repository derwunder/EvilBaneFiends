package com.wos.dernv.evilbanefiends.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.adapters.AdapterSpinnerClasePj;
import com.wos.dernv.evilbanefiends.adapters.AdapterSpinnerWikiEq;
import com.wos.dernv.evilbanefiends.logs.L;
import com.wos.dernv.evilbanefiends.myapp.MyApp;
import com.wos.dernv.evilbanefiends.network.Key;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.network.UrlEP;
import com.wos.dernv.evilbanefiends.objects.Player;
import com.wos.dernv.evilbanefiends.objects.SpinnerWikiEq;
import com.wos.dernv.evilbanefiends.objects.UserRegistro;
import com.wos.dernv.evilbanefiends.objects.WikiEquipo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by der_w on 5/17/2016.
 */
public class FrUserProfileActUser extends Fragment{



    ///vars to get data online
    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;


    private ProgressDialog progressDialog ;
    private int persistenTry=0;

    private Player player;

    //Layout VARS
    private ImageView sdImgEdit, sdImgSave, sdImgCancel;
    private ImageView imgVida, imgAtq, imgDef, imgNivel, imgPais, imgEspecialidad;
    private ImageView imgPerfil, imgInfo;
    private TextView textVida, textAtq, textDef, textNivel,textPais, textEspecialidad;
    private EditText editTextVida,  editTextAtq, editTextDef,  editTextNivel, editTextPais,  editTextEspecialidad;

    //Vars Seccion Datos
    private ImageView sdImgExpand, sdImgReduce;
    private TableLayout tableLayoutSecdatos;


    //VAR seccion EQ
    private ImageView sEQimgEdit, sEQimgSave, sEQimgCancel, sEQimgExpand, sEQimgReduce;
    private ImageView imgHelmet, imgArmor, imgHand, imgBoot, imgWeap, imgCap, imgAcc, imgTipoPj;
    private Spinner spHelmet, spArmor, spHand, spBoot,spWeap,spCap,spAcc,spTipoPj;
    private RelativeLayout secEQ;



    //Especial
    private ImageView cargarImgenGaleria;


    //Listas wiki equipo
    private ArrayList<WikiEquipo> listWikiEquipo;
    private ArrayList<WikiEquipo> listLukeArm,listLukeCap,listLukeWep;
    private ArrayList<WikiEquipo> listKharaArm,listKharaCap,listKharaWep;
    private ArrayList<WikiEquipo> listVangoArm,listVangoCap,listVangoWep;
    private ArrayList<WikiEquipo> listComun;


    public  FrUserProfileActUser(){}

    public static FrUserProfileActUser newInstance(){
        FrUserProfileActUser frUserProfileActUser= new FrUserProfileActUser();
        /*Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);*/
        return frUserProfileActUser;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    Spinner spinnerTest;
    AdapterSpinnerWikiEq adapterSpinnerWikiEq;
    ArrayList<SpinnerWikiEq> spinnerWikiEqs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        myVolleySingleton=MyVolleySingleton.getsInstance();
        requestQueue=myVolleySingleton.getmRequestQueue();
        imageLoader=myVolleySingleton.getmImageLoader();
        player=new Player();


        listWikiEquipo= new ArrayList<>();
        listLukeArm=new ArrayList<>();listLukeCap=new ArrayList<>();listLukeWep=new ArrayList<>();
        listKharaArm=new ArrayList<>();listKharaCap=new ArrayList<>();listKharaWep=new ArrayList<>();
        listVangoArm=new ArrayList<>();listVangoCap=new ArrayList<>();listVangoWep=new ArrayList<>();
        listComun=new ArrayList<>();

        spinnerWikiEqs=new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_profile_act_user,container,false);

       setSeccionDatos(rootView);
        setSeccionEQ(rootView);




        reduceContentSeccionDatos();
        reduceContentSeccionEQ();


        //setear lista por aca al adaptador
        enviarPeticionJsonDatosUser("Azreel");   //Wicam Azreel



        cargarImgenGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery();
            }
        });











        return rootView  ;// super.onCreateView(inflater, container, savedInstanceState);
    }


    public void setSeccionDatos(View rootView){
        cargarImgenGaleria=(ImageView)rootView.findViewById(R.id.loadImg);

        sdImgExpand=(ImageView)rootView.findViewById(R.id.sdImgExpand);
        sdImgReduce=(ImageView)rootView.findViewById(R.id.sdImgReduce);
        tableLayoutSecdatos=(TableLayout) rootView.findViewById(R.id.secDatos);

        sdImgEdit =(ImageView)rootView.findViewById(R.id.sdImgEditar);
        sdImgSave =(ImageView)rootView.findViewById(R.id.sdImgSave);
        sdImgCancel =(ImageView)rootView.findViewById(R.id.sdImgCancel);

        imgVida=(ImageView)rootView.findViewById(R.id.imgVida);
        imgAtq=(ImageView)rootView.findViewById(R.id.imgAtq);
        imgDef=(ImageView)rootView.findViewById(R.id.imgDef);
        imgNivel=(ImageView)rootView.findViewById(R.id.imgNivel);
        imgPais=(ImageView)rootView.findViewById(R.id.imgPais);
        imgEspecialidad=(ImageView)rootView.findViewById(R.id.imgEspecialidad);
        imgPerfil=(ImageView)rootView.findViewById(R.id.imgPerfil);
        imgInfo=(ImageView)rootView.findViewById(R.id.imgInfo);

        textVida=(TextView)rootView.findViewById(R.id.textVidaValue);
        textAtq=(TextView)rootView.findViewById(R.id.textAtqValue);
        textDef=(TextView)rootView.findViewById(R.id.textDefValue);
        textNivel=(TextView)rootView.findViewById(R.id.textNivelValue);
        textPais=(TextView)rootView.findViewById(R.id.textPaisValue);
        textEspecialidad=(TextView)rootView.findViewById(R.id.textEspecialidadValue);

        editTextVida=(EditText)rootView.findViewById(R.id.editTextVida);
        editTextAtq=(EditText)rootView.findViewById(R.id.editTextAtq);
        editTextDef=(EditText)rootView.findViewById(R.id.editTextDef);
        editTextNivel=(EditText)rootView.findViewById(R.id.editTextNivel);
        editTextPais=(EditText)rootView.findViewById(R.id.editTextPais);
        editTextEspecialidad=(EditText)rootView.findViewById(R.id.editTextEspecialidad);


        sdImgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandContentSeccionDatos();
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
            }
        });
        sdImgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentViewSecDatos();
            }
        });

    }
    public void setSeccionEQ(View rootView){
        sEQimgExpand=(ImageView)rootView.findViewById(R.id.sEQImgExpand);
        sEQimgReduce=(ImageView)rootView.findViewById(R.id.sEQImgReduce);
        secEQ=(RelativeLayout) rootView.findViewById(R.id.secEQ);

        sEQimgEdit=(ImageView)rootView.findViewById(R.id.sEQImgEditar);
        sEQimgSave=(ImageView)rootView.findViewById(R.id.sEQImgSave);
        sEQimgCancel=(ImageView)rootView.findViewById(R.id.sEQImgCancel);

        imgHelmet=(ImageView)rootView.findViewById(R.id.imgHelmetWeb);
        imgArmor=(ImageView)rootView.findViewById(R.id.imgArmortWeb);
        imgHand=(ImageView)rootView.findViewById(R.id.imgHandWeb);
        imgBoot=(ImageView)rootView.findViewById(R.id.imgBootWeb);
        imgWeap=(ImageView)rootView.findViewById(R.id.imgWeapWeb);
        imgCap=(ImageView)rootView.findViewById(R.id.imgCapWeb);
        imgAcc=(ImageView)rootView.findViewById(R.id.imgAccWep);
        imgTipoPj=(ImageView)rootView.findViewById(R.id.icPj);

        spHelmet=(Spinner) rootView.findViewById(R.id.spinnerHelmet);
        spArmor=(Spinner) rootView.findViewById(R.id.spinnerArmor);
        spHand=(Spinner) rootView.findViewById(R.id.spinnerHand);
        spBoot=(Spinner) rootView.findViewById(R.id.spinnerBoot);
        spWeap=(Spinner) rootView.findViewById(R.id.spinnerWeap);
        spCap=(Spinner) rootView.findViewById(R.id.spinnerCap);
        spAcc=(Spinner) rootView.findViewById(R.id.spinnerAcc);
        spTipoPj=(Spinner) rootView.findViewById(R.id.spinnerTipoPj);

        ArrayList<String> auxName =new ArrayList<>();
        ArrayList<Integer> auxImg=new ArrayList<>();

        auxName.add("Luke"); auxName.add("Khara");auxName.add("Vango");
        auxImg.add(R.drawable.ic_luke_00);auxImg.add(R.drawable.ic_khara_00);auxImg.add(R.drawable.ic_vango_00);

        spTipoPj.setAdapter(new AdapterSpinnerClasePj(getContext(),auxName,auxImg));
        spTipoPj.getBackground().setColorFilter(
                ContextCompat.getColor(getContext(),R.color.colorWhite),
                PorterDuff.Mode.SRC_ATOP);


        sEQimgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandContentSeccionEQ();
            }
        });
        sEQimgReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reduceContentSeccionEQ();
            }
        });

        sEQimgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentEditionSecEQ();
            }
        });
        sEQimgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentViewSecEQ();
            }
        });
        sEQimgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentViewSecEQ();
                setSpLisWikiPosition(spAcc,listComun,player.getAccesorio(),imgAcc);
                if(player.getClase().equals("luke")){
                    spTipoPj.setSelection(0);
                    setSpWikiTypeOnEdition(0);
                    setSpPositionWikiEQ(listLukeArm,listLukeWep,listLukeCap);
                }
                else if(player.getClase().equals("khara")){
                    spTipoPj.setSelection(1);
                    setSpWikiTypeOnEdition(1);
                    setSpPositionWikiEQ(listKharaArm,listKharaWep,listKharaCap);
                }
                else if(player.getClase().equals("vango")){
                    spTipoPj.setSelection(2);
                    setSpWikiTypeOnEdition(2);
                    setSpPositionWikiEQ(listVangoArm,listVangoWep,listVangoCap);
                }

            }
        });


        spTipoPj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSpWikiTypeOnEdition(position);
                setSpLisWikiPosition(spAcc,listComun,player.getAccesorio(),imgAcc);
                if(position==0 && player.getClase().equals("luke")){
                    setSpPositionWikiEQ(listLukeArm,listLukeWep,listLukeCap);}
                else if(position==1 && player.getClase().equals("khara")){
                    setSpPositionWikiEQ(listKharaArm,listKharaWep,listKharaCap);}
                else if(position==2 && player.getClase().equals("vango")){
                    setSpPositionWikiEQ(listVangoArm,listVangoWep,listVangoCap);}

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setDataPlayer(Player player){
        textVida.setText(player.getVida());
        textAtq.setText(player.getAtq());
        textDef.setText(player.getDef());
        textNivel.setText(player.getNivel());
        textPais.setText(player.getPais());
        textEspecialidad.setText(player.getEspecialidad());

        editTextVida.setText(player.getVida());
        editTextAtq.setText(player.getAtq());
        editTextDef.setText(player.getDef());
        editTextNivel.setText(player.getNivel());
        editTextPais.setText(player.getPais());
        editTextEspecialidad.setText(player.getEspecialidad());
        loadImage(player.getImg_perfil(),imgPerfil);
        loadImage(player.getImg_info(),imgInfo);

    }
    public void setEQPlayer(){
        if(player.getClase().equals("luke")){
            setSpListWiki(listLukeArm,listLukeWep,listLukeCap,listComun);
            setSpLisWikiPosition(spHelmet,listLukeArm,player.getCasco(),imgHelmet);
            setSpLisWikiPosition(spArmor,listLukeArm,player.getPecho(),imgArmor);
            setSpLisWikiPosition(spHand,listLukeArm,player.getBrazo(),imgHand);
            setSpLisWikiPosition(spBoot,listLukeArm,player.getPierna(),imgBoot);
            setSpLisWikiPosition(spWeap,listLukeWep,player.getEspada(),imgWeap);
            setSpLisWikiPosition(spCap,listLukeCap,player.getCapa(),imgCap);
            setSpLisWikiPosition(spAcc,listComun,player.getAccesorio(),imgAcc);
            spTipoPj.setSelection(0);
            imgTipoPj.setImageResource(R.drawable.ic_luke_00);

        }else if(player.getClase().equals("khara")){
            setSpListWiki(listKharaArm,listKharaWep,listKharaCap,listComun);
            setSpLisWikiPosition(spHelmet,listKharaArm,player.getCasco(),imgHelmet);
            setSpLisWikiPosition(spArmor,listKharaArm,player.getPecho(),imgArmor);
            setSpLisWikiPosition(spHand,listKharaArm,player.getBrazo(),imgHand);
            setSpLisWikiPosition(spBoot,listKharaArm,player.getPierna(),imgBoot);
            setSpLisWikiPosition(spWeap,listKharaWep,player.getEspada(),imgWeap);
            setSpLisWikiPosition(spCap,listKharaCap,player.getCapa(),imgCap);
            setSpLisWikiPosition(spAcc,listComun,player.getAccesorio(),imgAcc);
            spTipoPj.setSelection(1);
            imgTipoPj.setImageResource(R.drawable.ic_khara_00);

        }else if(player.getClase().equals("vango")){
            setSpListWiki(listVangoArm,listVangoWep,listVangoCap,listComun);
            setSpLisWikiPosition(spHelmet,listVangoArm,player.getCasco(),imgHelmet);
            setSpLisWikiPosition(spArmor,listVangoArm,player.getPecho(),imgArmor);
            setSpLisWikiPosition(spHand,listVangoArm,player.getBrazo(),imgHand);
            setSpLisWikiPosition(spBoot,listVangoArm,player.getPierna(),imgBoot);
            setSpLisWikiPosition(spWeap,listVangoWep,player.getEspada(),imgWeap);
            setSpLisWikiPosition(spCap,listVangoCap,player.getCapa(),imgCap);
            setSpLisWikiPosition(spAcc,listComun,player.getAccesorio(),imgAcc);
            spTipoPj.setSelection(2);
            imgTipoPj.setImageResource(R.drawable.ic_vango_00);
        }
    }

    public void setSpWikiTypeOnEdition(int type){
        if(type==0){
            setSpListWiki(listLukeArm,listLukeWep,listLukeCap,listComun);
            imgTipoPj.setImageResource(R.drawable.ic_luke_00);

        }else if(type==1){
            setSpListWiki(listKharaArm,listKharaWep,listKharaCap,listComun);
            imgTipoPj.setImageResource(R.drawable.ic_khara_00);

        }else if(type==2){
            setSpListWiki(listVangoArm,listVangoWep,listVangoCap,listComun);
            imgTipoPj.setImageResource(R.drawable.ic_vango_00);
        }
    }
    public void setSpPositionWikiEQ(ArrayList<WikiEquipo>lt1,ArrayList<WikiEquipo>lt2,ArrayList<WikiEquipo>lt3){
        setSpLisWikiPosition(spHelmet,lt1,player.getCasco(),imgHelmet);
        setSpLisWikiPosition(spArmor,lt1,player.getPecho(),imgArmor);
        setSpLisWikiPosition(spHand,lt1,player.getBrazo(),imgHand);
        setSpLisWikiPosition(spBoot,lt1,player.getPierna(),imgBoot);
        setSpLisWikiPosition(spWeap,lt2,player.getEspada(),imgWeap);
        setSpLisWikiPosition(spCap,lt3,player.getCapa(),imgCap);

    }


    String imgPath,  encodeImageString;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;

    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode== getActivity().RESULT_OK
                    && null != data) {
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
                imgPerfil.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));

                encodeImg();

                /*
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                params.put("filename", fileName);*/

            } else {
                Toast.makeText(getContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
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
                options0.inSampleSize = 3;
                // options.inJustDecodeBounds = true;
                options0.inScaled = false;
                options0.inDither = false;
                options0.inPreferredConfig = Bitmap.Config.ARGB_8888;

                bitmap = BitmapFactory.decodeFile(imgPath,options0);

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



    //Seccion Expandibles Funcionalidades de Vistas VISIBLE OR GONE
    public void expandContentSeccionDatos(){
        showContentViewSecDatos();
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
    public void showContentEditionSecDatos(){
        textVida.setVisibility(View.GONE);
        textAtq.setVisibility(View.GONE);
        textDef.setVisibility(View.GONE);
        textNivel.setVisibility(View.GONE);
        textPais.setVisibility(View.GONE);
        textEspecialidad.setVisibility(View.GONE);

        editTextVida.setVisibility(View.VISIBLE);
        editTextAtq.setVisibility(View.VISIBLE);
        editTextDef.setVisibility(View.VISIBLE);
        editTextNivel.setVisibility(View.VISIBLE);
        editTextPais.setVisibility(View.VISIBLE);
        editTextEspecialidad.setVisibility(View.VISIBLE);

        sdImgEdit.setVisibility(View.GONE);
        sdImgSave.setVisibility(View.VISIBLE);
        sdImgCancel.setVisibility(View.VISIBLE);

    }
    public void showContentViewSecDatos(){
        textVida.setVisibility(View.VISIBLE);
        textAtq.setVisibility(View.VISIBLE);
        textDef.setVisibility(View.VISIBLE);
        textNivel.setVisibility(View.VISIBLE);
        textPais.setVisibility(View.VISIBLE);
        textEspecialidad.setVisibility(View.VISIBLE);

        editTextVida.setVisibility(View.GONE);
        editTextAtq.setVisibility(View.GONE);
        editTextDef.setVisibility(View.GONE);
        editTextNivel.setVisibility(View.GONE);
        editTextPais.setVisibility(View.GONE);
        editTextEspecialidad.setVisibility(View.GONE);

        sdImgEdit.setVisibility(View.VISIBLE);
        sdImgSave.setVisibility(View.GONE);
        sdImgCancel.setVisibility(View.GONE);

    }

    public void expandContentSeccionEQ(){
        showContentViewSecEQ();
        secEQ.setVisibility(View.VISIBLE);
        sEQimgReduce.setVisibility(View.VISIBLE);
        sEQimgExpand.setVisibility(View.GONE);
        sEQimgEdit.setVisibility(View.VISIBLE);
    }
    public void reduceContentSeccionEQ(){
        showContentViewSecEQ();
        secEQ.setVisibility(View.GONE);
        sEQimgReduce.setVisibility(View.GONE);
        sEQimgExpand.setVisibility(View.VISIBLE);
        sEQimgEdit.setVisibility(View.GONE);
    }
    public void showContentViewSecEQ(){
        imgTipoPj.setVisibility(View.VISIBLE);
        imgHelmet.setVisibility(View.VISIBLE);
        imgArmor.setVisibility(View.VISIBLE);
        imgHand.setVisibility(View.VISIBLE);
        imgBoot.setVisibility(View.VISIBLE);
        imgWeap.setVisibility(View.VISIBLE);
        imgCap.setVisibility(View.VISIBLE);
        imgAcc.setVisibility(View.VISIBLE);

        spTipoPj.setVisibility(View.GONE);
        spHelmet.setVisibility(View.GONE);
        spArmor.setVisibility(View.GONE);
        spHand.setVisibility(View.GONE);
        spBoot.setVisibility(View.GONE);
        spWeap.setVisibility(View.GONE);
        spCap.setVisibility(View.GONE);
        spAcc.setVisibility(View.GONE);

        sEQimgEdit.setVisibility(View.VISIBLE);
        sEQimgSave.setVisibility(View.GONE);
        sEQimgCancel.setVisibility(View.GONE);
    }
    public void showContentEditionSecEQ(){
        imgTipoPj.setVisibility(View.GONE);
        imgHelmet.setVisibility(View.GONE);
        imgArmor.setVisibility(View.GONE);
        imgHand.setVisibility(View.GONE);
        imgBoot.setVisibility(View.GONE);
        imgWeap.setVisibility(View.GONE);
        imgCap.setVisibility(View.GONE);
        imgAcc.setVisibility(View.GONE);

        spTipoPj.setVisibility(View.VISIBLE);
        spHelmet.setVisibility(View.VISIBLE);
        spArmor.setVisibility(View.VISIBLE);
        spHand.setVisibility(View.VISIBLE);
        spBoot.setVisibility(View.VISIBLE);
        spWeap.setVisibility(View.VISIBLE);
        spCap.setVisibility(View.VISIBLE);
        spAcc.setVisibility(View.VISIBLE);

        sEQimgEdit.setVisibility(View.GONE);
        sEQimgSave.setVisibility(View.VISIBLE);
        sEQimgCancel.setVisibility(View.VISIBLE);
    }







    ///Llamados Player////
    public void enviarPeticionJsonDatosUser(final String nickName){

        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                UrlEP.EBFIENDS_PLAYER+Key.EndPoint.Pl_nickName+nickName
                , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        player=parseJsonResponse(response);
                        // MiAplicativo.getWritableDatabase().insertMateriaPensumIndividual(listMateria, ma_modulo, true);//IMPORTANTE

                       setDataPlayer(player);
                        peticionWikiEquipo();

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

                            enviarPeticionJsonDatosUser(nickName);
                        }
                    });
                    alertDialog.show();
                }else
                    enviarPeticionJsonDatosUser(nickName);


            }
        });

        //sin esta linea no se puede hacer la peticion al server
        requestQueue.add(request);
    }
    public Player parseJsonResponse(JSONObject response){
        Player player=new Player();
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

                String ID="NA"; String NICK_NAME="NA";String CLASE="NA";
                String CASCO="NA";String PECHO="NA";String BRAZO="NA";String PIERNA="NA";
                String ESPADA="NA";String CAPA="NA";String ACCESORIO="NA";
                String VIDA="NA";String ATQ="NA";String DEF="NA";
                String NIVEL="NA";String PAIS="NA";String ESPECIALIDAD="NA";
                String IMG_PERFIL="NA";String IMG_INFO="NA";

                JSONObject currentPj = response.getJSONObject(Key.JsGetPlayer.JUDADOR);
                if(currentPj.has(Key.JsGetPlayer.ID)&&
                        !currentPj.isNull(Key.JsGetPlayer.ID)){
                    ID=currentPj.getString(Key.JsGetPlayer.ID);
                }
                if(currentPj.has(Key.JsGetPlayer.ID)&&
                        !currentPj.isNull(Key.JsGetPlayer.ID)){
                    ID=currentPj.getString(Key.JsGetPlayer.ID);
                }
                if(currentPj.has(Key.JsGetPlayer.NICK_NAME)&&
                        !currentPj.isNull(Key.JsGetPlayer.NICK_NAME)){
                    NICK_NAME=currentPj.getString(Key.JsGetPlayer.NICK_NAME);
                }
                if(currentPj.has(Key.JsGetPlayer.CLASE)&&
                        !currentPj.isNull(Key.JsGetPlayer.CLASE)){
                    CLASE=currentPj.getString(Key.JsGetPlayer.CLASE);
                }
                if(currentPj.has(Key.JsGetPlayer.CASCO)&&
                        !currentPj.isNull(Key.JsGetPlayer.CASCO)){
                    CASCO=currentPj.getString(Key.JsGetPlayer.CASCO);
                }
                if(currentPj.has(Key.JsGetPlayer.PECHO)&&
                        !currentPj.isNull(Key.JsGetPlayer.PECHO)){
                    PECHO=currentPj.getString(Key.JsGetPlayer.PECHO);
                }
                if(currentPj.has(Key.JsGetPlayer.BRAZO)&&
                        !currentPj.isNull(Key.JsGetPlayer.BRAZO)){
                    BRAZO=currentPj.getString(Key.JsGetPlayer.BRAZO);
                }
                if(currentPj.has(Key.JsGetPlayer.PIERNA)&&
                        !currentPj.isNull(Key.JsGetPlayer.PIERNA)){
                    PIERNA=currentPj.getString(Key.JsGetPlayer.PIERNA);
                }
                if(currentPj.has(Key.JsGetPlayer.ESPADA)&&
                        !currentPj.isNull(Key.JsGetPlayer.ESPADA)){
                    ESPADA=currentPj.getString(Key.JsGetPlayer.ESPADA);
                }
                if(currentPj.has(Key.JsGetPlayer.CAPA)&&
                        !currentPj.isNull(Key.JsGetPlayer.CAPA)){
                    CAPA=currentPj.getString(Key.JsGetPlayer.CAPA);
                }
                if(currentPj.has(Key.JsGetPlayer.ACCESORIO)&&
                        !currentPj.isNull(Key.JsGetPlayer.ACCESORIO)){
                    ACCESORIO=currentPj.getString(Key.JsGetPlayer.ACCESORIO);
                }
                if(currentPj.has(Key.JsGetPlayer.VIDA)&&
                        !currentPj.isNull(Key.JsGetPlayer.VIDA)){
                    VIDA=currentPj.getString(Key.JsGetPlayer.VIDA);
                }
                if(currentPj.has(Key.JsGetPlayer.ATQ)&&
                        !currentPj.isNull(Key.JsGetPlayer.ATQ)){
                    ATQ=currentPj.getString(Key.JsGetPlayer.ATQ);
                }
                if(currentPj.has(Key.JsGetPlayer.DEF)&&
                        !currentPj.isNull(Key.JsGetPlayer.DEF)){
                    DEF=currentPj.getString(Key.JsGetPlayer.DEF);
                }
                if(currentPj.has(Key.JsGetPlayer.NIVEL)&&
                        !currentPj.isNull(Key.JsGetPlayer.NIVEL)){
                    NIVEL=currentPj.getString(Key.JsGetPlayer.NIVEL);
                }
                if(currentPj.has(Key.JsGetPlayer.PAIS)&&
                        !currentPj.isNull(Key.JsGetPlayer.PAIS)){
                    PAIS=currentPj.getString(Key.JsGetPlayer.PAIS);
                }
                if(currentPj.has(Key.JsGetPlayer.ESPECIALIDAD)&&
                        !currentPj.isNull(Key.JsGetPlayer.ESPECIALIDAD)){
                    ESPECIALIDAD=currentPj.getString(Key.JsGetPlayer.ESPECIALIDAD);
                }
                if(currentPj.has(Key.JsGetPlayer.IMG_PERFIL)&&
                        !currentPj.isNull(Key.JsGetPlayer.IMG_PERFIL)){
                    IMG_PERFIL=currentPj.getString(Key.JsGetPlayer.IMG_PERFIL);
                }
                if(currentPj.has(Key.JsGetPlayer.IMG_INFO)&&
                        !currentPj.isNull(Key.JsGetPlayer.IMG_INFO)){
                    IMG_INFO=currentPj.getString(Key.JsGetPlayer.IMG_INFO);
                }

                player=new Player();
                player.setNick_name(NICK_NAME);player.setClase(CLASE);
                player.setCasco(CASCO);player.setPecho(PECHO);player.setBrazo(BRAZO);player.setPierna(PIERNA);
                player.setEspada(ESPADA);player.setCapa(CAPA);player.setAccesorio(ACCESORIO);
                player.setVida(VIDA);player.setAtq(ATQ);player.setDef(DEF);
                player.setNivel(NIVEL);player.setPais(PAIS);player.setEspecialidad(ESPECIALIDAD);
                player.setImg_perfil(IMG_PERFIL);player.setImg_info(IMG_INFO);



            }catch (JSONException e){
                e.printStackTrace();
            }
        }


        return player;
    }

    private void sendImageToBackend(
            final Context context, final String imgEncode) {
        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        String url = "http://ebfiends.esy.es/public/upload_img_tester";

        Map<String, String> map = new HashMap<>();
        map.put("uploadImg", "1");
        map.put("imgEncode",imgEncode);


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




        //wiki equipo
    public void peticionWikiEquipo(){

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

                        listWikiEquipo=parseJsonResponseWikiequipo(response);
                        // MiAplicativo.getWritableDatabase().insertMateriaPensumIndividual(listMateria, ma_modulo, true);//IMPORTANTE
                        // adapterRcWikiEquipo.setEqPerfectoList(listWikiEquipo);
                        hacedorDeListasWikiequip(listWikiEquipo);

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

                            peticionWikiEquipo();
                        }
                    });
                    alertDialog.show();
                }else
                    peticionWikiEquipo();


            }
        });

        //sin esta linea no se puede hacer la peticion al server
        requestQueue.add(request);
    }
    public ArrayList<WikiEquipo> parseJsonResponseWikiequipo(JSONObject response){
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

    //Crear listas de la Wiki
    public void hacedorDeListasWikiequip(ArrayList<WikiEquipo> listWikiEquipo){
        listLukeArm= cicloDelHacedor("lu","arm");
        listLukeCap= cicloDelHacedor("lu","cap");
        listLukeWep= cicloDelHacedor("lu","wep");

        listKharaArm= cicloDelHacedor("kh","arm");
        listKharaCap= cicloDelHacedor("kh","cap");
        listKharaWep= cicloDelHacedor("kh","wep");

        listVangoArm= cicloDelHacedor("va","arm");
        listVangoCap= cicloDelHacedor("va","cap");
        listVangoWep= cicloDelHacedor("va","wep");

        listComun= cicloDelHacedor("co","acc");

        //UserRegistro userRegistro= MyApp.getWritableDatabase().getUserRegistro();

        setEQPlayer();




    }
    public ArrayList<WikiEquipo> cicloDelHacedor(String clase,String tipo){

        ArrayList<WikiEquipo> listReturn= new ArrayList<>();

        for(int i=0;i<listWikiEquipo.size();i++){
            String auxClase="", auxTipo="";
            String auxCodeName = listWikiEquipo.get(i).getCodeName();
            auxClase= auxCodeName.substring(0,2);
            auxTipo= auxCodeName.substring(4,7);
            if(auxClase.equals(clase)&& auxTipo.equals(tipo)){
                listReturn.add(listWikiEquipo.get(i));
            }
        }

        return listReturn;
    }

    public void setSpListWiki(ArrayList<WikiEquipo> lt1,ArrayList<WikiEquipo> lt2
            ,ArrayList<WikiEquipo> lt3,ArrayList<WikiEquipo> lt4){
        spHelmet.setAdapter(new AdapterSpinnerWikiEq(getContext(),lt1));
        spArmor.setAdapter(new AdapterSpinnerWikiEq(getContext(),lt1));
        spHand.setAdapter(new AdapterSpinnerWikiEq(getContext(),lt1));
        spBoot.setAdapter(new AdapterSpinnerWikiEq(getContext(),lt1));
        spWeap.setAdapter(new AdapterSpinnerWikiEq(getContext(),lt2));
        spCap.setAdapter(new AdapterSpinnerWikiEq(getContext(),lt3));
        spAcc.setAdapter(new AdapterSpinnerWikiEq(getContext(),lt4));
    }

    public void setSpLisWikiPosition(Spinner sp,ArrayList<WikiEquipo>ltWEQ,String codName,ImageView img){
        for(int i=0; i<ltWEQ.size();i++){
            if(ltWEQ.get(i).getCodeName().equals(codName)){
                loadImage(ltWEQ.get(i).getImg_ic00(),img);
                sp.setSelection(i);i=ltWEQ.size();
            }
        }

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

                }
            });
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //gracias al metodo on Attach damos valor al clickCallBack evitamos Null value

            //  mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
