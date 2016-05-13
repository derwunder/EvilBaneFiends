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
import com.wos.dernv.evilbanefiends.objects.WikiMoneda;

import java.util.ArrayList;

/**
 * Created by der_w on 5/12/2016.
 */
public class AdapterRcWikiMoneda extends RecyclerView.Adapter<AdapterRcWikiMoneda.RcWikiMonedaViewHolder> {


    private LayoutInflater inflater;
    private ArrayList<WikiMoneda> listWikiMoneda = new ArrayList<>();

    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private ProgressDialog progressDialog ;private int persistenTry=0;

    private ArrayList<Boolean> listVisorDetalle=new ArrayList<>();

    private ClickCallBack clickCallBack;
    private Context context;

    public AdapterRcWikiMoneda(Context context, ClickCallBack clickCallBack){
        inflater=LayoutInflater.from(context);
        myVolleySingleton = MyVolleySingleton.getsInstance();
        requestQueue=myVolleySingleton.getmRequestQueue();
        imageLoader = myVolleySingleton.getmImageLoader();
        this.context=context;
        this.clickCallBack=clickCallBack;
        progressDialog = new ProgressDialog(context); persistenTry=0;
    }
    public void setWikiMonedaList(ArrayList<WikiMoneda> listWikiMoneda){
        this.listWikiMoneda =listWikiMoneda;
        for (int i=0;i<listWikiMoneda.size();i++){
            listVisorDetalle.add(false);
        }
        notifyItemChanged(0,listWikiMoneda.size());
    }

    @Override
    public RcWikiMonedaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_rc_wiki_moneda,parent,false);
        return new RcWikiMonedaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RcWikiMonedaViewHolder holder, int position) {
        boolean visorDetalle;
        visorDetalle=listVisorDetalle.get(position);
        WikiMoneda wikiMoneda=listWikiMoneda.get(position);

        loadImages(wikiMoneda.getImg(),holder,"img");
        holder.monedaTipo.setText(wikiMoneda.getTipo());
        holder.monedaDetalle.setText(wikiMoneda.getDetalle());
    }

    @Override
    public int getItemCount() {
        return listWikiMoneda.size();
    }


    private void loadImages(String urlThumbnail, final RcWikiMonedaViewHolder holder, final String tipo) {
        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if(tipo.equals("img")){
                        holder.monedaImage.setImageBitmap(response.getBitmap());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }



    public  class RcWikiMonedaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout;
        ImageView monedaImage;
        ImageView showHideButton;
        TextView monedaTipo, monedaDetalle;

        public RcWikiMonedaViewHolder(View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            monedaImage =(ImageView)itemView.findViewById(R.id.monedaImage);

            showHideButton=(ImageView)itemView.findViewById(R.id.showOrHideContentButton);

            monedaTipo =(TextView)itemView.findViewById(R.id.monedaTipo);
            monedaDetalle =(TextView)itemView.findViewById(R.id.monedaDetalle);


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
