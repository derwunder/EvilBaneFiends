package com.wos.dernv.evilbanefiends.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.logs.L;
import com.wos.dernv.evilbanefiends.myapp.MyApp;
import com.wos.dernv.evilbanefiends.objects.NotifyItem;
import com.wos.dernv.evilbanefiends.objects.Player;

import java.util.ArrayList;

/**
 * Created by der_w on 5/22/2016.
 */
public class AdapterRcNotify extends RecyclerView.Adapter<AdapterRcNotify.RcNotifyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<NotifyItem> listNotify = new ArrayList<>();
    private ArrayList<Boolean> listVisorDetalle=new ArrayList<>();
    private boolean flagEdition;

    Context context;

    public AdapterRcNotify(Context context){
        inflater=LayoutInflater.from(context);
        this.context=context;
    }
    public void setNotifyList(ArrayList<NotifyItem> listNotify){
        this.listNotify=listNotify;
        for (int i=0;i<listNotify.size();i++){
            listVisorDetalle.add(false);
        }
        notifyDataSetChanged();
    }
    public void setEdition(boolean flag){
        flagEdition=flag;
        notifyDataSetChanged();
    }

    @Override
    public RcNotifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_rc_fr_notify,parent,false);
        return new RcNotifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RcNotifyViewHolder holder, int position) {

        if(flagEdition) {
            holder.imgDel.setVisibility(View.VISIBLE);
            holder.imgDel.setColorFilter(ContextCompat.getColor(context,R.color.colorAccent));
        }
        else
            holder.imgDel.setVisibility(View.INVISIBLE);

//...
        boolean visorDetalle;
        NotifyItem notifyItem= listNotify.get(position);
        visorDetalle=listVisorDetalle.get(position);

        if(notifyItem.getUrl_noti().equals("noAsig")&& notifyItem.getUrl_vid().equals("noAsig")){
            visorDetalle=false;
            holder.showHideButton.setVisibility(View.GONE);
        }

        holder.adminName.setText(notifyItem.getAdmin_send());
        holder.msjText.setText(notifyItem.getMensaje());

        if(!visorDetalle){
                holder.lyWebNoti.setVisibility(View.GONE);
                holder.lyWebVid.setVisibility(View.GONE);

            holder.showHideButton.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }
        else{

            if(notifyItem.getUrl_noti().equals("noAsig")){
                holder.lyWebNoti.setVisibility(View.GONE);
            }else{
                holder.lyWebNoti.setVisibility(View.VISIBLE);
                holder.wbNoti.loadUrl(notifyItem.getUrl_noti());
            }


            if(notifyItem.getUrl_vid().equals("noAsig")){
                holder.lyWebVid.setVisibility(View.GONE);
            }else{
                holder.lyWebVid.setVisibility(View.VISIBLE);
                holder.wbVid.loadUrl("http://www.youtube.com/embed/" +notifyItem.getUrl_vid() + "?autoplay=1&vq=small");
                if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    holder.lyWebVid.setVisibility(View.GONE);
                    holder.coniVidLink.setVisibility(View.VISIBLE);
                    //holder.texiVidLink.setText(wikiPersonaje.getClase());
                    holder.iVidLink.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuRed));

                }
            }

            holder.showHideButton.setColorFilter(ContextCompat.getColor(context,R.color.colorWhite));

        }
    }

    @Override
    public int getItemCount() {
        return listNotify.size();
    }

    public  class RcNotifyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout;
        ImageView imgNoti;
        ImageView showHideButton, imgDel;
        TextView adminName;
        TextView msjText;
        WebView wbNoti, wbVid;
        LinearLayout lyWebNoti, lyWebVid;

        LinearLayout coniVidLink;
        ImageView iVidLink;
        TextView texiVidLink;

        public RcNotifyViewHolder(View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            imgNoti =(ImageView)itemView.findViewById(R.id.imgNoti);
            imgDel=(ImageView)itemView.findViewById(R.id.imgDelNoti);
            showHideButton=(ImageView)itemView.findViewById(R.id.showOrHideContentButton);

            adminName =(TextView)itemView.findViewById(R.id.adminName);
            msjText =(TextView)itemView.findViewById(R.id.msjText);

            wbNoti=(WebView)itemView.findViewById(R.id.webViewNoti);
            wbVid=(WebView)itemView.findViewById(R.id.webViewVideo);
            setWebViewNoti();setWebViewVid();

            lyWebNoti=(LinearLayout)itemView.findViewById(R.id.contenedorWebViewNoti);
            lyWebVid=(LinearLayout)itemView.findViewById(R.id.contenedorWebViewVideo);

            YoYo.with(Techniques.Shake)
                    .duration(800)
                    .playOn(itemView.findViewById(R.id.bodyRelative));

            coniVidLink=(LinearLayout)itemView.findViewById(R.id.coniVidLink);
            texiVidLink=(TextView)itemView.findViewById(R.id.texiVidLink);
            iVidLink=(ImageView)itemView.findViewById(R.id.iVidLink);
            iVidLink.setOnClickListener(this);

            relativeLayout.setOnClickListener(this);
            showHideButton.setOnClickListener(this);
            imgDel.setOnClickListener(this);

        }

        public void setWebViewNoti(){
            wbNoti.getSettings().setDomStorageEnabled(true);
            wbNoti.getSettings().setJavaScriptEnabled(true);
            wbNoti.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            wbNoti.getSettings().setSupportMultipleWindows(true);
            wbNoti.setVerticalScrollBarEnabled(true);
            wbNoti.setHorizontalScrollBarEnabled(true);
            wbNoti.getSettings().setSupportZoom(true);
            wbNoti.setWebViewClient(new WebViewClient(){
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
        }
        public void setWebViewVid(){
            wbVid.getSettings().setDomStorageEnabled(true);
            wbVid.getSettings().setJavaScriptEnabled(true);
            wbVid.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            wbVid.getSettings().setSupportMultipleWindows(true);
            wbVid.getSettings().setSupportZoom(true);
            wbVid.setWebViewClient(new WebViewClient(){
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
            if(v==v.findViewById(R.id.imgDelNoti)){
                MyApp.getWritableDatabase().deleteNotiItem(listNotify.get(getAdapterPosition()).getId());
                listNotify.remove(getAdapterPosition());notifyItemRemoved(getAdapterPosition());
            }

            if(v==v.findViewById(R.id.iVidLink)){
                context.startActivity(new Intent(Intent.ACTION_VIEW).
                        setData(Uri.parse("https://www.youtube.com/watch?v="+listNotify.get(getAdapterPosition()).getUrl_vid())));


            }
        }
    }
}
