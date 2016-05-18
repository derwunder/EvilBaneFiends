package com.wos.dernv.evilbanefiends.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.logs.L;
import com.wos.dernv.evilbanefiends.myapp.MyApp;

import java.util.ArrayList;

/**
 * Created by der_w on 5/15/2016.
 */
public class DialogRegiToClan extends DialogFragment{

  private ClickCallBack clickCallBack;
  private  LayoutInflater inflater;
  private  TextView textNickName, textCodigo;
  private  EditText editTextNickName, editTextCodigo;
  private  ImageView imgClan;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Layout Inflater
        inflater  = getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_regi_to_clan,null);

        textNickName=(TextView)view.findViewById(R.id.textNickName);
        textCodigo =(TextView)view.findViewById(R.id.textCodigo);
        editTextNickName=(EditText)view.findViewById(R.id.editTextNickName);
        editTextCodigo=(EditText)view.findViewById(R.id.editTextCodigo);


        editTextNickName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });
        editTextCodigo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });




        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clickCallBack.onRegisterDialogSet(
                                editTextNickName.getText().toString(),
                                editTextCodigo.getText().toString());
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
            //gracias al metodo on Attach damos valor al clickCallBack evitamos Null value
            clickCallBack=(ClickCallBack) context;
            //  mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
