package com.wos.dernv.evilbanefiends.objects;

/**
 * Created by der_w on 5/22/2016.
 */
public class NotifyItem {


    private String tipo, admin_send,mensaje,url_noti,url_vid;
    private int id;

    public NotifyItem(){

    }

    public void setId(int id){this.id=id;}

    public int getId(){return id;}

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setAdmin_send(String admin_send) {
        this.admin_send = admin_send;
    }

    public String getAdmin_send() {
        return admin_send;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setUrl_noti(String url_noti) {
        this.url_noti = url_noti;
    }

    public String getUrl_noti() {
        return url_noti;
    }

    public void setUrl_vid(String url_vid) {
        this.url_vid = url_vid;
    }

    public String getUrl_vid() {
        return url_vid;
    }
}
