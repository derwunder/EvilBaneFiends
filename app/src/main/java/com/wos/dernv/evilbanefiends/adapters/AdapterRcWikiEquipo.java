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

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.objects.WikiEquipo;

import java.util.ArrayList;

/**
 * Created by der_w on 5/11/2016.
 */
public class AdapterRcWikiEquipo extends RecyclerView.Adapter<AdapterRcWikiEquipo.RcWikiEquipoViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<WikiEquipo> listWikiEquipo = new ArrayList<>();

    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private ProgressDialog progressDialog ;private int persistenTry=0;

    private ArrayList<Boolean> listVisorDetalle=new ArrayList<>();

    private ClickCallBack clickCallBack;
    private Context context;

    public AdapterRcWikiEquipo(Context context, ClickCallBack clickCallBack){
        inflater=LayoutInflater.from(context);
        myVolleySingleton = MyVolleySingleton.getsInstance();
        requestQueue=myVolleySingleton.getmRequestQueue();
        imageLoader = myVolleySingleton.getmImageLoader();
        this.context=context;
        this.clickCallBack=clickCallBack;
        progressDialog = new ProgressDialog(context); persistenTry=0;
    }
    public void setEqPerfectoList(ArrayList<WikiEquipo> listWikiEquipo){
        this.listWikiEquipo=listWikiEquipo;
        for (int i=0;i<listWikiEquipo.size();i++){
            listVisorDetalle.add(false);
        }
        notifyDataSetChanged();
       // notifyItemChanged(0,listWikiEquipo.size());
    }

    @Override
    public RcWikiEquipoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_rc_wiki_equipo,parent,false);
        return new RcWikiEquipoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RcWikiEquipoViewHolder holder, int position) {
        boolean visorDetalle;
        visorDetalle=listVisorDetalle.get(position);
        WikiEquipo wikiEquipo=listWikiEquipo.get(position);

        holder.equipoNombre.setText(wikiEquipo.getName());

        loadImages(wikiEquipo.getImg_ic00(),holder,"ic00");
        loadImages(wikiEquipo.getImg_ic01(),holder,"ic01");
        loadImages(wikiEquipo.getImg_ic02(),holder,"ic02");
        loadImages(wikiEquipo.getImg_ic03(),holder,"ic03");
        loadImages(wikiEquipo.getImg_ic04(),holder,"ic04");
        loadImages(wikiEquipo.getImg_ic05(),holder,"ic05");



        if(!visorDetalle){
            holder.equipoHa1.setVisibility(View.GONE);
            holder.equipoHa2.setVisibility(View.GONE);
            holder.equipoHa3.setVisibility(View.GONE);
            holder.equipoHa4.setVisibility(View.GONE);
            holder.equipoHa5.setVisibility(View.GONE);
            holder.equipoHa6.setVisibility(View.GONE);
            holder.equipoFullView.setVisibility(View.GONE);
            holder.showHideButton.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }else {

            holder.equipoHa1.setVisibility(View.VISIBLE);
            holder.equipoHa2.setVisibility(View.VISIBLE);
            holder.equipoHa3.setVisibility(View.VISIBLE);
            holder.equipoHa4.setVisibility(View.VISIBLE);
            holder.equipoHa5.setVisibility(View.VISIBLE);
            holder.equipoHa6.setVisibility(View.VISIBLE);
            holder.equipoFullView.setVisibility(View.VISIBLE);


            holder.equipoHa1.setText(wikiEquipo.getHb1());
            holder.equipoHa2.setText(wikiEquipo.getHb2());
            holder.equipoHa3.setText(wikiEquipo.getHb3());
            holder.equipoHa4.setText(wikiEquipo.getHb4());
            holder.equipoHa5.setText(wikiEquipo.getHb5());
            holder.equipoHa6.setText(wikiEquipo.getHb6());
            loadImages(wikiEquipo.getImg_view(),holder,"full");
            holder.showHideButton.setColorFilter(ContextCompat.getColor(context,R.color.colorWhite));
        }


     /*   if(wikiEquipo.getImg_ic03().equals("NA")){
            holder.equipoImage03.setVisibility(View.GONE);
        }
        if(wikiEquipo.getImg_ic04().equals("NA")){
            holder.equipoImage04.setVisibility(View.GONE);
        }
        if(wikiEquipo.getImg_ic05().equals("NA")){
            holder.equipoImage05.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return listWikiEquipo.size();
    }



    private void loadImages(String urlThumbnail, final RcWikiEquipoViewHolder holder, final String tipo) {
        if(urlThumbnail.equals("NA")){
            urlThumbnail="http://ebfiends.esy.es/public/Misc/default_equipo.png";
        }
        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if(tipo.equals("ic00")){
                        holder.equipoImage00.setImageBitmap(response.getBitmap());
                    }else if(tipo.equals("ic01")){
                        holder.equipoImage01.setImageBitmap(response.getBitmap());
                    }else if(tipo.equals("ic02")){
                        holder.equipoImage02.setImageBitmap(response.getBitmap());
                    }else if(tipo.equals("ic03")){
                        holder.equipoImage03.setImageBitmap(response.getBitmap());
                    }else if(tipo.equals("ic04")){
                        holder.equipoImage04.setImageBitmap(response.getBitmap());
                    }else if(tipo.equals("ic05")){
                        holder.equipoImage05.setImageBitmap(response.getBitmap());
                    }else if(tipo.equals("full")){
                        holder.equipoFullView.setImageBitmap(response.getBitmap());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }


    public  class RcWikiEquipoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout;
        ImageView equipoImage00,equipoImage01,equipoImage02,equipoImage03,equipoImage04,equipoImage05, equipoFullView;
        ImageView showHideButton;
        TextView equipoNombre;
        TextView equipoHa1,equipoHa2,equipoHa3,equipoHa4,equipoHa5,equipoHa6;

        public RcWikiEquipoViewHolder(View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            equipoImage00=(ImageView)itemView.findViewById(R.id.equipoImage00);
            equipoImage01=(ImageView)itemView.findViewById(R.id.equipoImage01);
            equipoImage02=(ImageView)itemView.findViewById(R.id.equipoImage02);
            equipoImage03=(ImageView)itemView.findViewById(R.id.equipoImage03);
            equipoImage04=(ImageView)itemView.findViewById(R.id.equipoImage04);
            equipoImage05=(ImageView)itemView.findViewById(R.id.equipoImage05);

            equipoFullView=(ImageView)itemView.findViewById(R.id.equipoFullView);
            showHideButton=(ImageView)itemView.findViewById(R.id.showOrHideContentButton);

            equipoNombre=(TextView)itemView.findViewById(R.id.equipoNombre);
            equipoHa1=(TextView)itemView.findViewById(R.id.equipoHa1);
            equipoHa2=(TextView)itemView.findViewById(R.id.equipoHa2);
            equipoHa3=(TextView)itemView.findViewById(R.id.equipoHa3);
            equipoHa4=(TextView)itemView.findViewById(R.id.equipoHa4);
            equipoHa5=(TextView)itemView.findViewById(R.id.equipoHa5);
            equipoHa6=(TextView)itemView.findViewById(R.id.equipoHa6);

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
