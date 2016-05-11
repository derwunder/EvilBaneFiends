package com.wos.dernv.evilbanefiends.network;

/**
 * Created by der_w on 5/11/2016.
 */
public interface Key {
    interface EndPoint{
        String Pl_nickName="?nickName=";
        String Wk_codeName="?codeName=";
    }
    interface JsGetPlayer{
        String ESTADO="estado";
        String JUDADOR="jugador";String JUDADORES="jugadores";
        String ID="id"; String NICK_NAME="nick_name";String CLASE="clase";
        String CASCO="casco";String PECHO="pecho";String BRAZO="brazo";String PIERNA="pierna";
        String ESPADA="espada";String CAPA="capa";String ACCESORIO="accesorio";
        String VIDA="vida";String ATQ="atq";String DEF="def";
        String NIVEL="nivel";String PAIS="pais";String ESPECIALIDAD="especialidad";
        String IMG_PERFIL="img_perfil";String IMG_INFO="img_info";
    }
    interface JsGetWikiEquipo{
        String ESTADO="estado";String WIKI_EQUIPO="wikiEquipo";
         String CODENAME="code_name" ;
         String NAME="name" ;
         String CLASE="clase";
         String HB1="hb1",HB2="hb2",HB3="hb3",HB4="hb4",HB5="hb5",HB6="hb6" ;
         String IMG_IC00="img_ic00",IMG_IC01="img_ic01",IMG_IC02="img_ic02",
                 IMG_IC03="img_ic03",IMG_IC04="img_ic04",IMG_IC05="img_ic05";
         String IMG_VIEW="img_view";
    }
}
