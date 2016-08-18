package com.wos.dernv.evilbanefiends.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.myapp.MyApp;
import com.wos.dernv.evilbanefiends.objects.NotifyItem;

import java.util.ArrayList;

/**
 * Created by der_w on 5/10/2016.
 */
public class AdapterRcMenu extends RecyclerView.Adapter<AdapterRcMenu.RcMenuViewHolder> {

    private ArrayList<String> listTitulo = new ArrayList<>();
    private int[] listImage = new int[5];
    private int[] listcolor = new int[5];
    private LayoutInflater inflater;

    private ClickCallBack clickCallBack;
    private Context context;

    private ArrayList<NotifyItem> lisNT;

    public AdapterRcMenu(Context context, ClickCallBack clickCallBack){
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.clickCallBack=clickCallBack;

        listTitulo.add("Top 10 Jugadores\nProximamente ( ͡° ͜ʖ ͡°)");    listImage[0]=R.drawable.ic_menu_01;
        listTitulo.add("Equipamento \nPerfecto");listImage[1]=R.drawable.ic_menu_02;
        listTitulo.add("Tips \nBatalla de clan");     listImage[2]=R.drawable.ic_menu_03;
        listTitulo.add("Wiki\nLongBook");    listImage[3]=R.drawable.ic_menu_04;
        listTitulo.add("Notificaciones");      listImage[4]=R.drawable.ic_menu_05;

        lisNT= MyApp.getWritableDatabase().getAllNotiItem();
    }

    @Override
    public RcMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View root = inflater.inflate(R.layout.row_rc_fr_menu,parent,false);
       // RcMenuViewHolder holder = new RcMenuViewHolder(root);
        return new RcMenuViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RcMenuViewHolder holder, int position) {
        holder.itemTitulo.setText(listTitulo.get(position));
        holder.itemImage.setImageResource(listImage[position]);

        if(!lisNT.isEmpty() && position==4){
            holder.subItemImage02.setVisibility(View.VISIBLE);
            holder.subItemImage02.setImageResource(R.drawable.ic_visibility_black_36dp);
            holder.subItemImage02.setColorFilter(ContextCompat.getColor(context,R.color.colorAccent));
        }
    }

    @Override
    public int getItemCount() {
        return listTitulo.size();
    }

    public  class RcMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout;
        ImageView itemImage, subItemImage01, subItemImage02;
        TextView itemTitulo;

        public RcMenuViewHolder(View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            itemImage=(ImageView)itemView.findViewById(R.id.itemImagen);
            subItemImage01=(ImageView)itemView.findViewById(R.id.subItemImage01);
            subItemImage02=(ImageView)itemView.findViewById(R.id.subItemImage02);
            itemTitulo=(TextView)itemView.findViewById(R.id.itemTitulo);

            relativeLayout.setOnClickListener(this);
            subItemImage01.setOnClickListener(this);
            subItemImage02.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.bodyRelative)){
                clickCallBack.onRSCItemMenuSelected(getAdapterPosition()+1);
            }
            if(v==v.findViewById(R.id.subItemImage01)){

            }
            if (v==v.findViewById(R.id.subItemImage02)){
                if(!lisNT.isEmpty()) {
                    NotifyItem nt = lisNT.get(0);
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Ultima Notificacion");
                    alertDialog.setMessage(nt.getAdmin_send()+" - Mensaje: \n\n" + nt.getMensaje()
                            );
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });
                    alertDialog.show();
                }
            }
        }
    }
}
