package com.wos.dernv.evilbanefiends.objects;

/**
 * Created by der_w on 5/15/2016.
 */
public class UserRegistro {

    private String dv_regi;
    private String nick_name;
    private String miembro;
    private String admin;

    public UserRegistro(){}

    public String  getDv_regi() {
        return dv_regi;
    }

    public void setDv_regi(String dv_regi) {
        this.dv_regi = dv_regi;
    }

    public String  getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String  getMiembro() {
        return miembro;
    }

    public void setMiembro(String miembro) {
        this.miembro = miembro;
    }

    public String  getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
