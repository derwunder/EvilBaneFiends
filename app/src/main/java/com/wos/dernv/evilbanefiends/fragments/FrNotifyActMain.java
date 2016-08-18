package com.wos.dernv.evilbanefiends.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.adapters.AdapterRcNotify;
import com.wos.dernv.evilbanefiends.myapp.MyApp;
import com.wos.dernv.evilbanefiends.objects.NotifyItem;

import java.util.ArrayList;

/**
 * Created by der_w on 5/22/2016.
 */
public class FrNotifyActMain extends Fragment {


    private ProgressDialog progressDialog ;
    private ArrayList<NotifyItem> listNotify;
    private Boolean flagEdition=false;

    private RecyclerView recyclerView;
    private AdapterRcNotify adapterRcNotify;

    private TextView textEmptyList;
    private ImageView imgEmptyList;

    public static FrNotifyActMain newInstance() {
        FrNotifyActMain fragment = new FrNotifyActMain();
        return fragment;
    }


    public FrNotifyActMain() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        listNotify= MyApp.getWritableDatabase().getAllNotiItem();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notify_activity_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(flagEdition) {
            menu.findItem(R.id.action_edit).setVisible(false);
            menu.findItem(R.id.action_save).setVisible(true);
        }else{
            menu.findItem(R.id.action_edit).setVisible(true);
            menu.findItem(R.id.action_save).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_edit){
            flagEdition=true;
            adapterRcNotify.setEdition(true);
            getActivity().invalidateOptionsMenu();
        }
        else if(id==R.id.action_save){
            flagEdition=false;
            adapterRcNotify.setEdition(false);
            getActivity().invalidateOptionsMenu();
        }
        else if(id==R.id.action_delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Borrar Notificaciones");
            builder.setMessage("Seguro que desea eliminar todas las notificaciones?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyApp.getWritableDatabase().deleteAllNotiItem();
                    listNotify=new ArrayList<>();
                    adapterRcNotify.setNotifyList(listNotify);
                    textEmptyList.setText("No hay Notifiaciones");
                    imgEmptyList.setImageResource(R.drawable.ic_menu_05);

                    textEmptyList.setVisibility(View.VISIBLE);
                    imgEmptyList.setVisibility(View.VISIBLE);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_rc_act_main, container, false);

        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapterRcNotify=new AdapterRcNotify(getContext());
        adapterRcNotify.setNotifyList(listNotify);

        OffsetDecorationRC offsetDecorationRC=
                new OffsetDecorationRC(75,35,getContext().getResources().getDisplayMetrics().density);
        recyclerView.addItemDecoration(offsetDecorationRC);
        recyclerView.setAdapter(adapterRcNotify);
        recyclerView.setNestedScrollingEnabled(false);


        textEmptyList=(TextView)rootView.findViewById(R.id.textEmptyList);
        imgEmptyList=(ImageView)rootView.findViewById(R.id.imgEmptyList);


        if(listNotify.isEmpty()){
            textEmptyList.setText("No hay Notifiaciones");
            imgEmptyList.setImageResource(R.drawable.ic_menu_05);

            textEmptyList.setVisibility(View.VISIBLE);
            imgEmptyList.setVisibility(View.VISIBLE);
        }




        return rootView; //super.onCreateView(inflater, container, savedInstanceState);
    }

    //CLass DECORADOR PARA LINEAR RC ESPACIO AL FINAL
    static class OffsetDecorationRC extends RecyclerView.ItemDecoration {
        private int mBottomOffset;
        private int mTopOffset;

        public OffsetDecorationRC(int bottomOffset,int topOffset, float density) {
            mBottomOffset =(int)(bottomOffset * density);
            mTopOffset = (int)(topOffset * density);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int dataSize = state.getItemCount();
            int position =  parent.getChildAdapterPosition(view);
            if (dataSize > 0 && position == dataSize - 1) {
                outRect.set(0, 0, 0, mBottomOffset);
            }else {
                outRect.set(0, 0, 0, 0);
            }

            if(parent.getChildAdapterPosition(view)==0){
                outRect.set(0, mTopOffset, 0, 0);
            }

        }
    }
}
