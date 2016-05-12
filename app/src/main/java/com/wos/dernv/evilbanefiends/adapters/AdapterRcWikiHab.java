package com.wos.dernv.evilbanefiends.adapters;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.wos.dernv.evilbanefiends.logs.L;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.objects.WikiHab;
import com.wos.dernv.evilbanefiends.objects.WikiPersonaje;

import java.util.ArrayList;

/**
 * Created by der_w on 5/12/2016.
 */
public class AdapterRcWikiHab extends RecyclerView.Adapter<AdapterRcWikiHab.RcWikiHabViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<WikiHab> listWikiHab = new ArrayList<>();

    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private ProgressDialog progressDialog ;private int persistenTry=0;

    private ArrayList<Boolean> listVisorDetalle=new ArrayList<>();

    private ClickCallBack clickCallBack;
    private Context context;

    public AdapterRcWikiHab(Context context, ClickCallBack clickCallBack){
        inflater=LayoutInflater.from(context);
        myVolleySingleton = MyVolleySingleton.getsInstance();
        requestQueue=myVolleySingleton.getmRequestQueue();
        imageLoader = myVolleySingleton.getmImageLoader();
        this.context=context;
        this.clickCallBack=clickCallBack;
        progressDialog = new ProgressDialog(context); persistenTry=0;
    }
    public void setWikiHabList(ArrayList<WikiHab> listWikiHab){
        this.listWikiHab =listWikiHab;
        for (int i=0;i<listWikiHab.size();i++){
            listVisorDetalle.add(false);
        }
        notifyItemChanged(0,listWikiHab.size());
    }
    @Override
    public RcWikiHabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_rc_wiki_hab,parent,false);
        return new RcWikiHabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RcWikiHabViewHolder holder, int position) {
        boolean visorDetalle;
        visorDetalle=listVisorDetalle.get(position);
        WikiHab wikiHab=listWikiHab.get(position);

        loadImages(wikiHab.getImg(),holder,"img");
        holder.habTipo.setText(wikiHab.getTipo());
        holder.habDetalle.setText(wikiHab.getDetalle());
    }

    @Override
    public int getItemCount() {
        return listWikiHab.size();
    }


    private void loadImages(String urlThumbnail, final RcWikiHabViewHolder holder, final String tipo) {
        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if(tipo.equals("img")){
                        holder.habImage.setImageBitmap(response.getBitmap());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    public  class RcWikiHabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout;
        ImageView habImage;
        ImageView showHideButton;
        TextView habTipo,habDetalle;

        public RcWikiHabViewHolder(View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            habImage=(ImageView)itemView.findViewById(R.id.habImage);

            showHideButton=(ImageView)itemView.findViewById(R.id.showOrHideContentButton);

            habTipo=(TextView)itemView.findViewById(R.id.habTipo);
            habDetalle=(TextView)itemView.findViewById(R.id.habDetalle);


            relativeLayout.setOnClickListener(this);
            showHideButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.bodyRelative)){
                L.t(context,"Jugador: "+(getAdapterPosition()+1) );
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
