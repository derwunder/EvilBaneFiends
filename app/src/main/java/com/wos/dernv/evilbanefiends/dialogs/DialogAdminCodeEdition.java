package com.wos.dernv.evilbanefiends.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.events.ClickCallBackAdmin;

/**
 * Created by der_w on 5/17/2016.
 */
public class DialogAdminCodeEdition extends DialogFragment {

    private ClickCallBackAdmin clickCallBackAdmin;
    private LayoutInflater inflater;
    private TextView textAviso, textCodigoAdmin, textCodigoUser;
    private EditText editTextCodigoAdmin, editTextCodigoUser;
    private ImageView imgAccion;

    String id,codigoOld,nickName,accion;
    public void setData(String id, String codigoOld, String nickName, String accion){
        this.id=id;this.codigoOld=codigoOld;this.nickName=nickName;this.accion=accion;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Layout Inflater
        inflater  = getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_admin_code_edition,null);

        imgAccion=(ImageView)view.findViewById(R.id.imgAccion);
        textAviso =(TextView)view.findViewById(R.id.textAviso);
        textCodigoAdmin =(TextView)view.findViewById(R.id.textCodigoAdmin);
        textCodigoUser =(TextView)view.findViewById(R.id.textCodigoUser);

        editTextCodigoAdmin =(EditText)view.findViewById(R.id.editTextCodigoAdmin);
        editTextCodigoUser=(EditText)view.findViewById(R.id.editTextCodigoUser);


        //  editTextMensaje.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });
        editTextCodigoAdmin.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });
       // editTextMensaje.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE );

       if(accion.equals("catCodEditionUpdate")){
           imgAccion.setImageResource(R.drawable.ic_vpn_key_white_36dp);
           textAviso.setText("Importante: esta accion cambiara el codigo del miembro "+nickName+
                   " \n- Cuando el miembro no tiene acceso\n- Cuando se le olvido su codigo" +
                   "\n- El codigo se mostrara encriptado");
           textAviso.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
           textCodigoUser.setText("Nuevo codigo - "+nickName);
       }
        else if(accion.equals("catCodEditionRestore")){
           imgAccion.setImageResource(R.drawable.ic_error_white_36dp);
           textAviso.setText("Importante: esta accion restaura el codigo de miembro."+
                   " \n- "+nickName+" no recibira notificacion para miembros"+
                   " \n- "+nickName+" pasa a ser usuario invitado");
           textAviso.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
           textCodigoUser.setText("Nuevo codigo - restaurar");
       }




        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       /* L.t(getContext(),"Cod Admin: "+editTextCodigoAdmin.getText().toString()+"\n"+
                                "ID:        "+id+"\n"+
                                "CodOld:    "+codigoOld+"\n"+
                                "CodNew:    "+editTextCodigoUser.getText().toString()+"\n"+
                                "Nick:      "+nickName+"\n"+
                                "Accion:    "+accion);*/

                        clickCallBackAdmin.onDialogSetCodeEdition(accion,
                                editTextCodigoAdmin.getText().toString(),
                                codigoOld,editTextCodigoUser.getText().toString(),
                                id);

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });


        return builder.create(); // super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //gracias al metodo on Attach damos valor al clickCallBackAdmin evitamos Null value
            clickCallBackAdmin =(ClickCallBackAdmin) context;
            //  mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

}
