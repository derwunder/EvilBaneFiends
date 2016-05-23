package com.wos.dernv.evilbanefiends.events;

/**
 * Created by der_w on 5/10/2016.
 */
public interface ClickCallBack {
    void onRSCItemMenuSelected(int position);
    void onRegisterDialogSet(String nickName, String codigo);
    void onLogInDialogSet( String codigo);
    void onMsjToClanDialogSet(String codigo,String msj, String donde,String urlNoti,String urlVid);
}
