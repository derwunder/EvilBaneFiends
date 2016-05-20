package com.wos.dernv.evilbanefiends.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.acts.ActivityUser;
import com.wos.dernv.evilbanefiends.network.MyVolleySingleton;
import com.wos.dernv.evilbanefiends.objects.WikiEquipo;

import java.util.ArrayList;

/**
 * Created by der_w on 5/19/2016.
 */
public class AdapterSpinnerClasePj extends BaseAdapter{


    Context context;
    ArrayList<String> listName;
    ArrayList<Integer> listIMG;

    public AdapterSpinnerClasePj(Context context, ArrayList<String> listName, ArrayList<Integer> listIMG) {
        super();
        this.context = context;
        this.listName = listName;
        this.listIMG=listIMG;
    }

    @Override
    public int getCount() {
        return listName.size();
    }

    @Override
    public Object getItem(int position) {
        return listName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater;//=LayoutInflater.from(context);
        inflater=((ActivityUser)context).getLayoutInflater();
        View row = inflater.inflate(R.layout.row_spinner_clase_pj, parent, false);

        String itemNameStr=listName.get(position);
        int itemImgInt=listIMG.get(position);



        TextView itemName=(TextView)row.findViewById(R.id.itemName);
        ImageView imgSpinner=(ImageView)row.findViewById(R.id.imageSpinner);

        itemName.setText(itemNameStr);
        imgSpinner.setImageResource(itemImgInt);



        //LayoutInflater inflater = ((Activity) context).getLayoutInflater();


        return row;
    }


}
