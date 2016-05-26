package com.wos.dernv.evilbanefiends.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.adapters.AdapterRcMenu;
import com.wos.dernv.evilbanefiends.events.ClickCallBack;

/**
 * Created by der_w on 5/10/2016.
 */
public class FrMenuActMain extends Fragment {

    private ClickCallBack clickCallBack;

    private RecyclerView rcListMenu;
    private AdapterRcMenu adapterRcMenu;

    public FrMenuActMain(){}
    public  static FrMenuActMain newInstance(){
        FrMenuActMain frMenuActMain=new FrMenuActMain();
        /*Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);*/
        return frMenuActMain;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //aca se Infla
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      //aca se trabaja el menu
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fr_rc_act_main,container,false);
        rcListMenu=(RecyclerView)rootView.findViewById(R.id.recycleView);
       adapterRcMenu=new AdapterRcMenu(getContext(),clickCallBack);

        GridLayoutManager manager = new GridLayoutManager(getActivity(),
                2,GridLayoutManager.VERTICAL,false);
        rcListMenu.setLayoutManager(manager);

        //Espacio al final de la Vista de menu (RC)
       OffsetDecorationMenu offsetDecorationMenu =
                new OffsetDecorationMenu(75,5,getContext().getResources().getDisplayMetrics().density);
        rcListMenu.addItemDecoration(offsetDecorationMenu); //pasep

        rcListMenu.setSoundEffectsEnabled(true);
        rcListMenu.setAdapter(adapterRcMenu);

        return rootView;  // super.onCreateView(inflater, container, savedInstanceState);
    }



    static class OffsetDecorationMenu extends RecyclerView.ItemDecoration {
        private int mBottomOffset;
        private int mTopOffset;

        public OffsetDecorationMenu(int bottomOffset, int topOffset, float density) {
            mBottomOffset =(int)(bottomOffset * density);
            mTopOffset = (int)(topOffset * density);
        }


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int dataSize = state.getItemCount();
            int position = parent.getChildAdapterPosition(view);
            GridLayoutManager grid = (GridLayoutManager)parent.getLayoutManager();
            /*if ((dataSize - position) <= grid.getSpanCount()) {
                outRect.set(0, 0, 0, mBottomOffset);
            } else {
                outRect.set(0, 0, 0, 0);
            }*/

            if(parent.getChildAdapterPosition(view)==4){
                outRect.set(0, 0, 0, mBottomOffset);
            }

            /*if(parent.getChildAdapterPosition(view)==0){
                outRect.set(0, 0, 0, 0);
            }*/

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            clickCallBack=(ClickCallBack)context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+
            " Must implement Fragment Interaction Listener");
        }
    }
}
