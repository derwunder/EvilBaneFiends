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
import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.events.ClickCallBackAdmin;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.objects.AdminCodeEdition;

import java.util.ArrayList;

/**
 * Created by der_w on 5/16/2016.
 */
public class AdapterRcAdminCodeEdition extends RecyclerView.Adapter<AdapterRcAdminCodeEdition.RcAdminCodeEditionHolder> {

    private LayoutInflater inflater;
    private ArrayList<AdminCodeEdition> listAdminCodeEdition;

    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog ;private int persistenTry=0;


    private ClickCallBackAdmin clickCallBackAdmin;
    private Context context;

    public AdapterRcAdminCodeEdition(Context context, ClickCallBackAdmin clickCallBackAdmin){
        inflater=LayoutInflater.from(context);
        listAdminCodeEdition=new ArrayList<>();
        myVolleySingleton = MyVolleySingleton.getsInstance();
        requestQueue=myVolleySingleton.getmRequestQueue();
        this.context=context;
        this.clickCallBackAdmin = clickCallBackAdmin;
        progressDialog = new ProgressDialog(context); persistenTry=0;
    }
    public void setAdminCodeEditionList(ArrayList<AdminCodeEdition> listAdminCodeEdition){
        this.listAdminCodeEdition=listAdminCodeEdition;
        notifyDataSetChanged();

    }


    @Override
    public RcAdminCodeEditionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_rc_admin_code_edition,parent,false);
        return new RcAdminCodeEditionHolder(view);
    }

    @Override
    public void onBindViewHolder(RcAdminCodeEditionHolder holder, int position) {
        AdminCodeEdition aCE= listAdminCodeEdition.get(position);

        holder.pjName.setText(aCE.getNick_name());
        holder.pjCodigoHash.setText(aCE.getCodigo());

    }

    @Override
    public int getItemCount() {
        return listAdminCodeEdition.size();
    }

    public  class RcAdminCodeEditionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout;
        ImageView codeImg;
        ImageView actionRestore, actionUpdate;
        TextView pjName, pjDevice, pjCodigoHash;

        public RcAdminCodeEditionHolder(View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            codeImg=(ImageView)itemView.findViewById(R.id.codeImg);

            actionRestore =(ImageView)itemView.findViewById(R.id.actionRestore);
            actionUpdate =(ImageView)itemView.findViewById(R.id.actionUpdate);


            pjName =(TextView)itemView.findViewById(R.id.pjName);
            pjDevice =(TextView)itemView.findViewById(R.id.pjDevice);
            pjCodigoHash =(TextView)itemView.findViewById(R.id.pjCodigoHash);



            relativeLayout.setOnClickListener(this);
            actionRestore.setOnClickListener(this);
            actionUpdate.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.bodyRelative)){

            }
            if(v==v.findViewById(R.id.actionRestore)){
                AdminCodeEdition adminCodeEdition=listAdminCodeEdition.get(getAdapterPosition());
                clickCallBackAdmin.onRcAdminCodeEdition(adminCodeEdition.getId(),
                        adminCodeEdition.getCodigo(),adminCodeEdition.getNick_name(),"catCodEditionRestore");
            }
            if (v==v.findViewById(R.id.actionUpdate)){
                AdminCodeEdition adminCodeEdition=listAdminCodeEdition.get(getAdapterPosition());
                clickCallBackAdmin.onRcAdminCodeEdition(adminCodeEdition.getId(),
                        adminCodeEdition.getCodigo(),adminCodeEdition.getNick_name(),"catCodEditionUpdate");
            }

        }
    }


}
