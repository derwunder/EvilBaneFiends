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
import com.wos.dernv.evilbanefiends.objects.WikiMundo;

import java.util.ArrayList;

/**
 * Created by der_w on 5/12/2016.
 */
public class AdapterRcWikiMundo extends RecyclerView.Adapter<AdapterRcWikiMundo.RcWikiMundoViewHolder> {


    private LayoutInflater inflater;
    private ArrayList<WikiMundo> listWikiMundo = new ArrayList<>();

    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private ProgressDialog progressDialog ;private int persistenTry=0;

    private ArrayList<Boolean> listVisorDetalle=new ArrayList<>();

    private ClickCallBack clickCallBack;
    private Context context;

    public AdapterRcWikiMundo(Context context, ClickCallBack clickCallBack){
        inflater=LayoutInflater.from(context);
        myVolleySingleton = MyVolleySingleton.getsInstance();
        requestQueue=myVolleySingleton.getmRequestQueue();
        imageLoader = myVolleySingleton.getmImageLoader();
        this.context=context;
        this.clickCallBack=clickCallBack;
        progressDialog = new ProgressDialog(context); persistenTry=0;
    }
    public void setWikiMundoList(ArrayList<WikiMundo> listWikiMundo){
        this.listWikiMundo =listWikiMundo;
        for (int i=0;i<listWikiMundo.size();i++){
            listVisorDetalle.add(false);
        }
        notifyItemChanged(0,listWikiMundo.size());
    }

    @Override
    public RcWikiMundoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_rc_wiki_mundo,parent,false);
        return new RcWikiMundoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RcWikiMundoViewHolder holder, int position) {
        boolean visorDetalle;
        visorDetalle=listVisorDetalle.get(position);
        WikiMundo wikiMundo= listWikiMundo.get(position);

        loadImages(wikiMundo.getImg(),holder,"img");
        holder.mundoTipo.setText(wikiMundo.getTipo());
        holder.mundoDetalle.setText(wikiMundo.getDetalle());
    }

    @Override
    public int getItemCount() {
        return listWikiMundo.size();
    }

    private void loadImages(String urlThumbnail, final RcWikiMundoViewHolder holder, final String tipo) {
        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if(tipo.equals("img")){
                        holder.mundoImage.setImageBitmap(response.getBitmap());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }




    public  class RcWikiMundoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout;
        ImageView mundoImage;
        ImageView showHideButton;
        TextView mundoTipo, mundoDetalle;

        public RcWikiMundoViewHolder(View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            mundoImage =(ImageView)itemView.findViewById(R.id.mundoImage);

            showHideButton=(ImageView)itemView.findViewById(R.id.showOrHideContentButton);

            mundoTipo =(TextView)itemView.findViewById(R.id.mundoTipo);
            mundoDetalle =(TextView)itemView.findViewById(R.id.mundoDetalle);


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
