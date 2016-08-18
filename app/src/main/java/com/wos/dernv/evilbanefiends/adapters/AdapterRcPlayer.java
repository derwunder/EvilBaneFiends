package com.wos.dernv.evilbanefiends.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.objects.Player;
import com.wos.dernv.evilbanefiends.objects.WikiEquipo;

import java.util.ArrayList;

/**
 * Created by der_w on 5/10/2016.
 */
public class AdapterRcPlayer extends RecyclerView.Adapter<AdapterRcPlayer.RcPlayerViewHolder> {


    private LayoutInflater inflater;
    private ArrayList<Player> listPlayer = new ArrayList<>();
    private ArrayList<WikiEquipo> listWikiEquipo;

    private MyVolleySingleton myVolleySingleton;
    private ImageLoader imageLoader;
    private ProgressDialog progressDialog ;private int persistenTry=0;

    private ArrayList<Boolean> listVisorDetalle=new ArrayList<>();

    private ClickCallBack clickCallBack;
    private Context context;

    public AdapterRcPlayer(Context context, ClickCallBack clickCallBack){
        inflater=LayoutInflater.from(context);
        myVolleySingleton = MyVolleySingleton.getsInstance();
        imageLoader = myVolleySingleton.getmImageLoader();
        this.context=context;
        this.clickCallBack=clickCallBack;
        progressDialog = new ProgressDialog(context); persistenTry=0;
    }
    public void setPlayerList(ArrayList<Player> listPlayer){
        this.listPlayer=listPlayer;
        for (int i=0;i<listPlayer.size();i++){
            listVisorDetalle.add(false);
        }
        notifyDataSetChanged();
    }
    public void setWikiEquipoList(ArrayList<WikiEquipo> listWikiEquipo){
        this.listWikiEquipo=listWikiEquipo;

    }
    @Override
    public RcPlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_rc_fr_player,parent,false);
        return new RcPlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RcPlayerViewHolder holder, int position) {
        boolean visorDetalle;
        Player actPlayer= listPlayer.get(position);
        visorDetalle=listVisorDetalle.get(position);


        for(int i=0;i<listWikiEquipo.size();i++){
            if(listWikiEquipo.get(i).getCodeName().equals(actPlayer.getCasco())){
                String urlImagePrev= listWikiEquipo.get(i).getImg_ic00();
                loadImages(urlImagePrev,holder,"pic");
                i=listWikiEquipo.size();
            }
        }


        holder.playerNickName.setText(actPlayer.getNick_name());
      /*  String urlThumnail = "http://ebfiends.esy.es//public//Misc//Vango//Armadura//va_arm_02_reventador_ic_00.jpg";
        // listPlayer.get(position).getImg_perfil();
        loadImages(urlThumnail, holder,"pic");*/

        if(!visorDetalle){
            holder.playerVida.setVisibility(View.VISIBLE);
            holder.playerAtq.setVisibility(View.VISIBLE);
            holder.playerDef.setVisibility(View.VISIBLE);
            holder.playerVida.setText ("Vida:    "+actPlayer.getVida());
            holder.playerAtq.setText  ("Ataque:  "+actPlayer.getAtq());
            holder.playerDef.setText  ("Defensa: "+actPlayer.getDef());
            holder.playerNivel.setVisibility(View.GONE);
            holder.playerPais.setVisibility(View.GONE);
            holder.playerEsp.setVisibility(View.GONE);
            holder.playerImagePerfil.setVisibility(View.GONE);
            holder.playerImageInfo.setVisibility(View.GONE);
            holder.showHideButton.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }
        else{
            holder.playerVida.setVisibility(View.GONE);
            holder.playerAtq.setVisibility(View.GONE);
            holder.playerDef.setVisibility(View.GONE);


            holder.playerNivel.setVisibility(View.VISIBLE);
            holder.playerPais.setVisibility(View.VISIBLE);
            holder.playerEsp.setVisibility(View.VISIBLE);

            holder.playerNivel.setText("Nivel:   "+actPlayer.getNivel());
            holder.playerPais.setText ("Pais:    "+actPlayer.getPais());
            holder.playerEsp.setText  ("Especialidad: "+actPlayer.getEspecialidad());

            holder.playerImagePerfil.setVisibility(View.VISIBLE);
            holder.playerImageInfo.setVisibility(View.VISIBLE);
            holder.showHideButton.setColorFilter(ContextCompat.getColor(context,R.color.colorWhite));

            String urlImagePrev= actPlayer.getImg_perfil();
            loadImages(urlImagePrev,holder,"perfil");
            String urlImageInfo= actPlayer.getImg_info();
            loadImages(urlImageInfo,holder,"info");
        }

    }

    @Override
    public int getItemCount() {
        return listPlayer.size();
    }


    private void loadImages(String urlThumbnail, final RcPlayerViewHolder holder, final String tipo) {
        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                   if(tipo.equals("pic")){
                       holder.playerImage.setImageBitmap(response.getBitmap());
                   }else if(tipo.equals("perfil")){
                       holder.playerImagePerfil.setImageBitmap(response.getBitmap());
                   }else if(tipo.equals("info")){
                       holder.playerImageInfo.setImageBitmap(response.getBitmap());
                   }

                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    loadImages("http://ebfiends.esy.es/public/Misc/Jugador/espera_pj.jpg",holder,tipo);
                }
            });
        }
    }





    public  class RcPlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout;
        ImageView playerImage, playerImagePerfil,playerImageInfo;
        ImageView showHideButton;
        TextView playerNickName;
        TextView playerVida,playerAtq,playerDef,playerNivel,playerPais,playerEsp;

        public RcPlayerViewHolder(View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            playerImage=(ImageView)itemView.findViewById(R.id.playerImage);
            playerImagePerfil=(ImageView)itemView.findViewById(R.id.playerImagePerfil);
            playerImageInfo=(ImageView)itemView.findViewById(R.id.playerImageInfo);
            showHideButton=(ImageView)itemView.findViewById(R.id.showOrHideContentButton);

            playerNickName=(TextView)itemView.findViewById(R.id.playerNickName);
            playerVida=(TextView)itemView.findViewById(R.id.playerVida);
            playerAtq=(TextView)itemView.findViewById(R.id.playerAtaque);
            playerDef=(TextView)itemView.findViewById(R.id.playerDefensa);
            playerNivel=(TextView)itemView.findViewById(R.id.playerNivel);
            playerPais=(TextView)itemView.findViewById(R.id.playerPais);
            playerEsp=(TextView)itemView.findViewById(R.id.playerEspecialidad);

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
