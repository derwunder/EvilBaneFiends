package com.wos.dernv.evilbanefiends.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.logs.L;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.objects.WikiEquipo;
import com.wos.dernv.evilbanefiends.objects.WikiPersonaje;

import java.util.ArrayList;

/**
 * Created by der_w on 5/12/2016.
 */
public class AdapterRcWikiPersonaje extends RecyclerView.Adapter<AdapterRcWikiPersonaje.RcWikiPersonajeViewHolder> {


    private LayoutInflater inflater;
    private ArrayList<WikiPersonaje> listWikiPersonaje = new ArrayList<>();

    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private ProgressDialog progressDialog ;private int persistenTry=0;

    private ArrayList<Boolean> listVisorDetalle=new ArrayList<>();

    private ClickCallBack clickCallBack;
    private Context context;

    public AdapterRcWikiPersonaje(Context context, ClickCallBack clickCallBack){
        inflater=LayoutInflater.from(context);
        myVolleySingleton = MyVolleySingleton.getsInstance();
        requestQueue=myVolleySingleton.getmRequestQueue();
        imageLoader = myVolleySingleton.getmImageLoader();
        this.context=context;
        this.clickCallBack=clickCallBack;
        progressDialog = new ProgressDialog(context); persistenTry=0;
    }
    public void setWikiPersonajeList(ArrayList<WikiPersonaje> listWikiPersonaje){
        this.listWikiPersonaje=listWikiPersonaje;
        for (int i=0;i<listWikiPersonaje.size();i++){
            listVisorDetalle.add(false);
        }
        notifyItemChanged(0,listWikiPersonaje.size());
    }

    @Override
    public RcWikiPersonajeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_rc_wiki_personaje,parent,false);
        return new RcWikiPersonajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RcWikiPersonajeViewHolder holder, int position) {
        boolean visorDetalle;
        visorDetalle=listVisorDetalle.get(position);
        WikiPersonaje wikiPersonaje=listWikiPersonaje.get(position);

        loadImages(wikiPersonaje.getImg_pic(),holder,"img");

        holder.pjNombre.setText(wikiPersonaje.getClase());
        holder.pjVida.setText(wikiPersonaje.getVida());
        holder.pjAtq.setText(wikiPersonaje.getAtq());
        holder.pjDef.setText(wikiPersonaje.getDef());
        holder.pjAgi.setText(wikiPersonaje.getAgi());


        if(!visorDetalle){

            holder.pjDetalle.setVisibility(View.GONE);
            holder.pjImageFull.setVisibility(View.GONE);
            holder.lcontentWVPj.setVisibility(View.GONE);
            holder.showHideButton.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }else {
            holder.pjDetalle.setVisibility(View.VISIBLE);
            holder.pjImageFull.setVisibility(View.VISIBLE);
            holder.lcontentWVPj.setVisibility(View.VISIBLE);

            holder.webViewPjl.loadUrl("http://www.youtube.com/embed/" +wikiPersonaje.getVideo_view()+ "?autoplay=1&vq=small");
            holder.pjDetalle.setText(wikiPersonaje.getDetalle());
            loadImages(wikiPersonaje.getImg_view(),holder,"imgFull");
            holder.showHideButton.setColorFilter(ContextCompat.getColor(context,R.color.colorWhite));
        }

    }

    @Override
    public int getItemCount() {
        return listWikiPersonaje.size();
    }

    private void loadImages(String urlThumbnail, final RcWikiPersonajeViewHolder holder, final String tipo) {
        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if(tipo.equals("img")){
                        holder.pjImage.setImageBitmap(response.getBitmap());
                    }else if(tipo.equals("imgFull")){
                        holder.pjImageFull.setImageBitmap(response.getBitmap());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }


    public  class RcWikiPersonajeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout;
        ImageView pjImage,pjImageFull;
        ImageView showHideButton;
        TextView pjNombre,pjVida,pjAtq,pjDef,pjAgi,pjDetalle;
        WebView webViewPjl;
        LinearLayout lcontentWVPj;

        public RcWikiPersonajeViewHolder(View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            pjImage=(ImageView)itemView.findViewById(R.id.pjImage);
            pjImageFull=(ImageView)itemView.findViewById(R.id.pjImageFull);

            showHideButton=(ImageView)itemView.findViewById(R.id.showOrHideContentButton);

            pjNombre=(TextView)itemView.findViewById(R.id.pjName);
            pjVida=(TextView)itemView.findViewById(R.id.pjVida);
            pjAtq=(TextView)itemView.findViewById(R.id.pjAtaque);
            pjDef=(TextView)itemView.findViewById(R.id.pjDefensa);
            pjAgi=(TextView)itemView.findViewById(R.id.pjAgilidad);
            pjDetalle=(TextView)itemView.findViewById(R.id.pjDetalle);

            lcontentWVPj=(LinearLayout)itemView.findViewById(R.id.contenedorWebViewPj);
            webViewPjl=(WebView)itemView.findViewById(R.id.webViewPj);

            webViewPjl.getSettings().setDomStorageEnabled(true);
            webViewPjl.getSettings().setJavaScriptEnabled(true);
            webViewPjl.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webViewPjl.getSettings().setSupportMultipleWindows(true);
            webViewPjl.getSettings().setSupportZoom(true);
            webViewPjl.setWebViewClient(new WebViewClient(){
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
                    //
                    super.onPageStarted(view, url, favicon);
               /*- progressDialog.setMessage("Cargando ...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();-*/
                }
            });




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
