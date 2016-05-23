package com.wos.dernv.evilbanefiends.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.events.ClickCallBackUserProfile;
import com.wos.dernv.evilbanefiends.fragments.FrUserProfileActUser;
import com.wos.dernv.evilbanefiends.myapp.MyApp;
import com.wos.dernv.evilbanefiends.objects.UserRegistro;

/**
 * Created by der_w on 5/21/2016.
 */
public class DialogPassUserProfile extends DialogFragment {

    private ClickCallBack clickCallBack;

    private String tipo;
    private LayoutInflater inflater;
    private TextView  textNickName, textCodigo;
    private EditText  editTextCodigo;
    private ImageView imgClan;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Layout Inflater
        inflater  = getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_pass_user_profile,null);

        textCodigo =(TextView)view.findViewById(R.id.textCodigo);
        textNickName =(TextView)view.findViewById(R.id.textNickName);
        editTextCodigo=(EditText)view.findViewById(R.id.editTextCodigo);

        UserRegistro userRegistro= MyApp.getWritableDatabase().getUserRegistro();
        textNickName.setText(userRegistro.getNick_name());
        editTextCodigo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });



        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clickCallBack.onLogInDialogSet(editTextCodigo.getText().toString());
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
        clickCallBack=(ClickCallBack)context;
    }
}
