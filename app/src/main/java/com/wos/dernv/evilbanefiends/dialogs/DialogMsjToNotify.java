package com.wos.dernv.evilbanefiends.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
public class DialogMsjToNotify extends DialogFragment {

    private ClickCallBack clickCallBack;
    private LayoutInflater inflater;
    private TextView textMensaje, textCodigo;
    private EditText editTextMensaje, editTextCodigo;
    private ImageView imgClan;
    private Spinner spinnerDonde;
    private ArrayList<String> selectorSpinner;
    private int positionSpinnerDonde;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Layout Inflater
        inflater  = getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_msj_to_notify,null);

        textMensaje =(TextView)view.findViewById(R.id.textMensaje);
        textCodigo =(TextView)view.findViewById(R.id.textCodigo);
        editTextMensaje =(EditText)view.findViewById(R.id.editTextMensaje);
        editTextCodigo=(EditText)view.findViewById(R.id.editTextCodigo);
        spinnerDonde=(Spinner)view.findViewById(R.id.spinnerDonde);
        positionSpinnerDonde=0;

      //  editTextMensaje.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });
        editTextCodigo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });
        editTextMensaje.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE );

        selectorSpinner = new ArrayList<>();
        selectorSpinner.add("Miembros");selectorSpinner.add("Todos");

        ArrayAdapter<String> adapterSpinnerTA = new
                ArrayAdapter<>(getContext(), R.layout.dialog_msj_to_notify_spinner_aparence, selectorSpinner);
        adapterSpinnerTA.setDropDownViewResource(R.layout.dialog_msj_to_notify_spinner_aparence);
        spinnerDonde.setAdapter(adapterSpinnerTA);

        //color de la flecha del spinner
        spinnerDonde.getBackground().setColorFilter(
                ContextCompat.getColor(getContext(),R.color.colorWhite),
                PorterDuff.Mode.SRC_ATOP);


        spinnerDonde.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionSpinnerDonde=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String auxDonde="";
                        if(positionSpinnerDonde==0){auxDonde="msjToClan";}
                        else if(positionSpinnerDonde==1){auxDonde="msjToAll";}
                        clickCallBack.onMsjToClanDialogSet(editTextCodigo.getText().toString(),
                                editTextMensaje.getText().toString(),auxDonde);
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
