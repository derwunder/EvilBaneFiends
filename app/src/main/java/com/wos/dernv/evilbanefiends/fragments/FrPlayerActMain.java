package com.wos.dernv.evilbanefiends.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.wos.dernv.evilbanefiends.adapters.AdapterRcPlayer;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.objects.Player;

import java.util.ArrayList;

/**
 * Created by der_w on 5/10/2016.
 */
public class FrPlayerActMain extends Fragment {

    private ClickCallBack clickCallBack;

    ///vars to get data online
    private MyVolleySingleton myVolleySingleton;
    private RequestQueue requestQueue;

    private RecyclerView rcPlayerList;
    private AdapterRcPlayer adapterRcPlayer;

    private ArrayList<Player> listPlayer= new ArrayList<>();

    public  FrPlayerActMain(){}

    public static FrPlayerActMain newInstance(){
        FrPlayerActMain frPlayerActMain= new FrPlayerActMain();
        /*Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);*/
        return frPlayerActMain;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myVolleySingleton=MyVolleySingleton.getsInstance();
        requestQueue=myVolleySingleton.getmRequestQueue();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //gracias al metodo on Attach damos valor al clickCallBack evitamos Null value
            clickCallBack=(ClickCallBack)context;
            //  mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
