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
 * Created by der_w on 5/18/2016.
 */
public class AdapterSpinnerWikiEq extends BaseAdapter {

    Context context;
    ArrayList<WikiEquipo> listWikiEQ;
    private MyVolleySingleton myVolleySingleton;
    private ImageLoader imageLoader;

    public AdapterSpinnerWikiEq(Context context, ArrayList<WikiEquipo> listWikiEQ) {
        super();
        this.context = context;
        this.listWikiEQ = listWikiEQ;
        myVolleySingleton = MyVolleySingleton.getsInstance();
        imageLoader = myVolleySingleton.getmImageLoader();
    }

    @Override
    public int getCount() {
        return listWikiEQ.size();
    }

    @Override
    public Object getItem(int position) {
        return listWikiEQ.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater;//=LayoutInflater.from(context);
        inflater=((ActivityUser)context).getLayoutInflater();
        View row = inflater.inflate(R.layout.row_spinner_wiki_eq, parent, false);

        WikiEquipo WikiEQ= listWikiEQ.get(position);

        TextView itemName=(TextView)row.findViewById(R.id.itemName);
        ImageView imgSpinner=(ImageView)row.findViewById(R.id.imageSpinner);

        itemName.setText(WikiEQ.getName());
        loadImage(WikiEQ.getImg_ic00(),imgSpinner);



        //LayoutInflater inflater = ((Activity) context).getLayoutInflater();


        return row;
    }

    private void loadImage(String urlThumbnail, final  ImageView imageView) {
        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    imageView.setImageBitmap(response.getBitmap());
                }
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
        }
    }



}
