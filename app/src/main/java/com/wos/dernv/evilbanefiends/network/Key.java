package com.wos.dernv.evilbanefiends.network;

/**
 * Created by der_w on 5/11/2016.
 */
public interface Key {
    interface EndPoint{
        String Pl_nickName="?nickName=";
        String Wk_codeName="?codeName=";
    }
    interface Answer{
        String ESTADO="estado",MSJ="mensaje";
        String TIPO="tipo";
    }
    interface JsGetClanBatalla {
        String CLAN_BATALLA="clan_batalla";
        String BUFF_ATT="buffAtt",BUFF_DEF="buffDef",BUFF_CRI="buffCri";
        String DETALLE = "detalle";
        String VID1 = "vid1",VID2="vid2",VID3="vid3";
        String IMG_FORMACION="img_formacion";
    }
    interface JsGetCatCod {
        String CATCOD="catCod";
        String ID="id";
        String CODIGO = "codigo";
        String NICK_NAME = "nick_name";
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
    interface JsGetEqPerfecto{
        String EQUIPOPERFECTO="equipoPerfecto";
        String CODE_NAME="code_name";
        String AL1="al1",AL2="al2",AL3="al3";
        String ORIGEN="origen",USUARIO="usuario",DETALLE="detalle";
        String IMG_ROOT="img_root";
    }
    interface JsGetWikiPersonaje{
        String WIKI_PERSONAJE="wikiPersonaje";
        String CLASE="clase",VIDA="vida",ATQ="atq",DEF="def",AGI="agi";
        String DETALLE="detalle",IMG_PIC="img_pic",IMG_VIEW="img_view";
        String VIDEO_VIEW="video_view";
    }
    interface JsGetWikiHab{
        String WIKI_HABILIDAD="wikiHabilidad";
        String TIPO="tipo";
        String DETALLE="detalle",IMG="img";
    }
    interface JsGetWikiMoneda{
        String WIKI_MONEDA="wikiMoneda";
        String TIPO="tipo";
        String DETALLE="detalle",IMG="img";
    }
    interface JsGetWikiMundo{
        String WIKI_MUNDO="wikiMundo";
        String TIPO="tipo";
        String DETALLE="detalle",IMG="img";
    }
}
