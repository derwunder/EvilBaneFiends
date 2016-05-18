package com.wos.dernv.evilbanefiends.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.wos.dernv.evilbanefiends.acts.ActivityUser;
import com.wos.dernv.evilbanefiends.network.Key;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.network.UrlEP;
import com.wos.dernv.evilbanefiends.objects.Player;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    private ArrayList<Player> listPlayer= new ArrayList<>();
    private Player player;

    //Layout VARS
    private ImageView imgEdit, imgSave, imgCancel;
    private ImageView imgVida, imgAtq, imgDef, imgNivel, imgPais, imgEspecialidad;
    private ImageView imgPerfil, imgInfo;
    private TextView textVida, textAtq, textDef, textNivel,textPais, textEspecialidad;
    private EditText editTextVida,  editTextAtq, editTextDef,  editTextNivel, editTextPais,  editTextEspecialidad;


    //Especial
    private ImageView cargarImgenGaleria;





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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        myVolleySingleton=MyVolleySingleton.getsInstance();
        requestQueue=myVolleySingleton.getmRequestQueue();
        imageLoader=myVolleySingleton.getmImageLoader();
        player=new Player();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_profile_act_user,container,false);

        cargarImgenGaleria=(ImageView)rootView.findViewById(R.id.loadImg);

        imgEdit=(ImageView)rootView.findViewById(R.id.imgEditar);
        imgSave=(ImageView)rootView.findViewById(R.id.imgSave);
        imgCancel=(ImageView)rootView.findViewById(R.id.imgCancel);

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

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentEdition();
            }
        });
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentView();
            }
        });
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentView();
            }
        });


        showContentView();
        //setear lista por aca al adaptador
        enviarPeticionJsonDatosUser("Wicam");



        cargarImgenGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery();
            }
        });


        return rootView  ;// super.onCreateView(inflater, container, savedInstanceState);
    }



    String imgPath, fileName;
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
            if (requestCode == RESULT_LOAD_IMG
                    && null != data) {
                // Get the Image from data

                //&&  resultCode == RESULT_OK

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

               // ImageView imgView = (ImageView)findViewById(R.id.loadImg);
                // Set the Image in ImageView
                imgPerfil.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));

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

    public void showContentEdition(){
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

        imgEdit.setVisibility(View.GONE);
        imgSave.setVisibility(View.VISIBLE);
        imgCancel.setVisibility(View.VISIBLE);

    }
    public void showContentView(){
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

        imgEdit.setVisibility(View.VISIBLE);
        imgSave.setVisibility(View.GONE);
        imgCancel.setVisibility(View.GONE);

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
        loadImages(player.getImg_perfil(),"perfil");
        loadImages(player.getImg_info(),"info");

    }
    private void loadImages(String urlThumbnail , final String tipo) {
        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if(tipo.equals("perfil")){
                        imgPerfil.setImageBitmap(response.getBitmap());
                    }else if(tipo.equals("info")){
                        imgInfo.setImageBitmap(response.getBitmap());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
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
