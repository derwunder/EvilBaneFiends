package com.wos.dernv.evilbanefiends.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.logs.L;
import com.wos.dernv.evilbanefiends.objects.Player;

import java.util.ArrayList;

/**
 * Created by der_w on 5/10/2016.
 */
public class AdapterRcPlayer extends RecyclerView.Adapter<AdapterRcPlayer.RcPlayerViewHolder> {


    private LayoutInflater inflater;
    private ArrayList<Player> listPlayer = new ArrayList<>();

    private ClickCallBack clickCallBack;
    private Context context;

    public AdapterRcPlayer(Context context, ClickCallBack clickCallBack){
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.clickCallBack=clickCallBack;
    }
    public void setPlayerList(ArrayList<Player> listPlayer){
        this.listPlayer=listPlayer;
        notifyItemChanged(0,listPlayer.size());
    }
    @Override
    public RcPlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_rc_fr_player,parent,false);
        return new RcPlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RcPlayerViewHolder holder, int position) {
        holder.playerNickName.setText(listPlayer.get(position).getNick_name());
    }

    @Override
    public int getItemCount() {
        return listPlayer.size();
    }

    public  class RcPlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout;
        ImageView playerImage;
        TextView playerNickName,playerInfo;

        public RcPlayerViewHolder(View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            playerImage=(ImageView)itemView.findViewById(R.id.playerImage);
            playerNickName=(TextView)itemView.findViewById(R.id.playerNickName);
            playerNickName=(TextView)itemView.findViewById(R.id.playerInfo);

            relativeLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.bodyRelative)){
                L.t(context,"Jugador: "+getAdapterPosition()+1 );
            }
            if(v==v.findViewById(R.id.subItemImage01)){

            }
            if (v==v.findViewById(R.id.subItemImage02)){

            }
        }
    }
}
