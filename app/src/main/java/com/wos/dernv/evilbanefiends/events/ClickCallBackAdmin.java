package com.wos.dernv.evilbanefiends.events;

/**
 * Created by der_w on 5/16/2016.
 */
public interface ClickCallBackAdmin {
    void onRcAdminCodeEdition(String id,String codigo, String nickName, String accion);
    void onDialogSetCodeEdition(String accion, String codigoAdmin, String codigoJugadorOld,
                                String codigoJugadorNew, String idInCatCod);
    void reCalFrProfile();
}
