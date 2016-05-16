package com.wos.dernv.evilbanefiends.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.wos.dernv.evilbanefiends.logs.L;
import com.wos.dernv.evilbanefiends.objects.UserRegistro;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by der_w on 5/15/2016.
 */
public class DBFiend {
    private FiendHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DBFiend(Context context) {
        mHelper = new FiendHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }


  /**  Notification Functions
   *
    public void insertNotiItem(NotifyItem notifyItem){

        String sql = "INSERT INTO " + PensumHelper.TABLE_NOTIFY + " VALUES (?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        statement.clearBindings();
        //for a given column index, simply bind the data to be put inside that index
        statement.bindString(2, notifyItem.getClase());
        statement.bindString(3, notifyItem.getMsj());
        statement.bindString(4, notifyItem.getMod());
        statement.execute();

        //set the transaction as successful and end the transaction

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }
    public ArrayList<NotifyItem> getAllNotiItem(){
        ArrayList<NotifyItem> listUserNoti = new ArrayList<>();

        String[] columns = {PensumHelper.COLUMN_NT_UID,
                PensumHelper.COLUMN_NT_CLASS,
                PensumHelper.COLUMN_NT_MSJ,
                PensumHelper.COLUMN_NT_MOD
        };
        Cursor cursor = mDatabase.query(PensumHelper.TABLE_NOTIFY, columns, null, null, null, null,
                PensumHelper.COLUMN_NT_UID+" DESC");

        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new materia object and retrieve the data from the cursor to be stored in this materia object
                NotifyItem notifyItem = new NotifyItem();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank materia object to contain our data
                notifyItem.setId(cursor.getInt(cursor.getColumnIndex(PensumHelper.COLUMN_NT_UID)));
                notifyItem.setClase(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_NT_CLASS)));
                notifyItem.setMsj(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_NT_MSJ)));
                notifyItem.setMod(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_NT_MOD)));


                //add the materia to the list of materia objects which we plan to return
                listUserNoti.add(notifyItem);
            }
            while (cursor.moveToNext());
        }
        return listUserNoti;
    }
    public void deleteNotiItem(int id){
        String sql = "DELETE FROM " + PensumHelper.TABLE_NOTIFY +
                " WHERE "+PensumHelper.COLUMN_NT_UID+" = ?;" ;

        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        //for a given column index, simply bind the data to be put inside that index
        statement.bindLong(1, id);
        statement.execute();

        //set the transaction as successful and end the transaction

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }
    public void deleteAllNotiItem(){
        String sql = "DELETE FROM " + PensumHelper.TABLE_NOTIFY +
                " ;" ;

        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        //for a given column index, simply bind the data to be put inside that index
        statement.execute();
        //set the transaction as successful and end the transaction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    } */

    public void updateUserRegistroGCM(String dv_regi){
        String sql = "UPDATE " + FiendHelper.TABLE_USER_REGISTRO + " SET " +
                FiendHelper.COLUMN_UR_DV_REGI+    " = ? "+
                " WHERE "+FiendHelper.COLUMN_UR_UID+" = ?;" ;

        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        //for a given column index, simply bind the data to be put inside that index
        statement.bindString(1,dv_regi);
        statement.bindLong(2, 1);
        statement.execute();

        //set the transaction as successful and end the transaction

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }
    public void updateUserRegistroNickName(String nick_name){
        String sql = "UPDATE " + FiendHelper.TABLE_USER_REGISTRO + " SET " +
                FiendHelper.COLUMN_UR_NICK_NAME+    " = ? "+
                " WHERE "+FiendHelper.COLUMN_UR_UID+" = ?;" ;

        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        //for a given column index, simply bind the data to be put inside that index
        statement.bindString(1,nick_name);
        statement.bindLong(2, 1);
        statement.execute();

        //set the transaction as successful and end the transaction

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }
    public void updateUserRegistroToAdmin(String nick_name, String miembro, String admin){
        String sql = "UPDATE " + FiendHelper.TABLE_USER_REGISTRO + " SET " +
                FiendHelper.COLUMN_UR_NICK_NAME+    " = ?," +
                FiendHelper.COLUMN_UR_MIEMBRO+   " = ?," +
                FiendHelper.COLUMN_UR_ADMIN+ " = ?;";

        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        //for a given column index, simply bind the data to be put inside that index
        statement.bindString(1,nick_name);
        statement.bindString(2,miembro);
        statement.bindString(3,admin);
        statement.execute();

        //set the transaction as successful and end the transaction

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }
    public void updateUserRegistroToMiembro(String nick_name, String miembro){
        String sql = "UPDATE " + FiendHelper.TABLE_USER_REGISTRO + " SET " +
                FiendHelper.COLUMN_UR_NICK_NAME+    " = ?," +
                FiendHelper.COLUMN_UR_MIEMBRO+   " = ?";

        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        //for a given column index, simply bind the data to be put inside that index
        statement.bindString(1,nick_name);
        statement.bindString(2,miembro);
        statement.execute();

        //set the transaction as successful and end the transaction

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }
    public UserRegistro getUserRegistro(){
        UserRegistro userRegistro = new UserRegistro();
        String[] columns = {FiendHelper.COLUMN_UR_DV_REGI,
                FiendHelper.COLUMN_UR_NICK_NAME,
                FiendHelper.COLUMN_UR_MIEMBRO,
                FiendHelper.COLUMN_UR_ADMIN
        };
        Cursor cursor = mDatabase.query(FiendHelper.TABLE_USER_REGISTRO, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));

            userRegistro.setDv_regi(cursor.getString(cursor.getColumnIndex(FiendHelper.COLUMN_UR_DV_REGI)));
            userRegistro.setNick_name(cursor.getString(cursor.getColumnIndex(FiendHelper.COLUMN_UR_NICK_NAME)));
            userRegistro.setMiembro(cursor.getString(cursor.getColumnIndex(FiendHelper.COLUMN_UR_MIEMBRO)));
            userRegistro.setAdmin(cursor.getString(cursor.getColumnIndex(FiendHelper.COLUMN_UR_ADMIN)));

          /*  long calDayTime = cursor.getLong(cursor.getColumnIndex(FiendHelper.COLUMN_UR_DAYTIME));
            userRegistro.setDayTime(calDayTime != -1 ? new Date(calDayTime) : null);*/ //FECHAS O DAYTIMER


        }
        return userRegistro;
    }

    private static class FiendHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "fiend";
        private static final int DB_VERSION = 1;

        /***********************************************************************
         * ////////// TABLA DE USER REGISTRO ////////////////////////////////////////
         ***********************************************************************/
        public static final String TABLE_USER_REGISTRO = "user_registro";
        public static final String COLUMN_UR_UID = "id";
        public static final String COLUMN_UR_DV_REGI = "dv_regi";
        public static final String COLUMN_UR_NICK_NAME = "nick_name";
        public static final String COLUMN_UR_MIEMBRO = "miembro";
        public static final String COLUMN_UR_ADMIN = "admin";

        private static final String CREATE_TABLE_USER_REGISTRO = "CREATE TABLE " + TABLE_USER_REGISTRO + " (" +
                COLUMN_UR_UID + " INTEGER DEFAULT 1," +
                COLUMN_UR_DV_REGI + " TEXT DEFAULT noAsig," +
                COLUMN_UR_NICK_NAME + " TEXT DEFAULT noAsig, " +
                COLUMN_UR_MIEMBRO + " TEXT DEFAULT 0," +
                COLUMN_UR_ADMIN + " TEXT DEFAULT 0" +
                ");";

        private static final String INSERT_USER_REGISTRO ="INSERT INTO "+TABLE_USER_REGISTRO+" ("+
                COLUMN_UR_DV_REGI +") VALUES"+
                "('noAsig');" ;

        /***********************************************************************
         * ////////// TABLA DE NOTIFICACIONES ////////////////////////////////////////
         ***********************************************************************/
        public static final String TABLE_NOTIFY = "notify";
        public static final String COLUMN_NT_UID = "nt_uid";
        public static final String COLUMN_NT_CLASS = "nt_class";
        public static final String COLUMN_NT_MSJ = "nt_msj";
        public static final String COLUMN_NT_MOD = "nt_mod";


        private static final String CREATE_TABLE_NOTIFY = "CREATE TABLE " + TABLE_NOTIFY + " (" +
                COLUMN_NT_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NT_CLASS +" TEXT ," +
                COLUMN_NT_MSJ +  " TEXT,  " +
                COLUMN_NT_MOD+   " TEXT "+

                ");";

        /******************************************************************
         * ///////// TABLA DE ESTADOS DEL MENU PRINCIPAL!////////////////
         ******************************************************************
        public static final String TABLE_MENU_STATUS = "menu_status";
        public static final String COLUMN_M_UID = "me_id";
        public static final String COLUMN_M_COD = "me_cod";
        public static final String COLUMN_M_ITEM = "me_item";
        public static final String COLUMN_M_ACTIVO = "me_activo";

        private static final String CREATE_TABLE_MENU_STATUS = "CREATE TABLE " + TABLE_MENU_STATUS + " (" +
                COLUMN_M_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_M_COD + " TEXT, " +
                COLUMN_M_ITEM + " TEXT DEFAULT 0," +
                COLUMN_M_ACTIVO + " TEXT DEFAULT 0" +
                ");";

        private static final String INSERT_MENU_STATUS ="INSERT INTO "+TABLE_MENU_STATUS+" ("+
                COLUMN_M_COD+") VALUES"+
                "('pensumSub1')," +
                "('pensumSub2')," +
                "('matSub1')," +
                "('matSub2')," +
                "('hvSub1')," +
                "('hvSub2')," +
                "('ntSub1')," +
                "('ntSub2')," +
                "('calSub1')," +
                "('calSub2')," +
                "('prfSub1')," +
                "('prfSub2');"; */




        /***********************************************************************
         * ////////// TABLA DE GUIA DE USUARIO ////////////////////////////////////////
         ***********************************************************************
        public static final String TABLE_USER_GUIDE = "user_guide";
        public static final String COLUMN_UG_UID = "ug_uid";
        public static final String COLUMN_UG_TYPE = "ug_type";
        public static final String COLUMN_UG_VALUE = "ug_value";

        private static final String CREATE_TABLE_USER_GUIDE = "CREATE TABLE " + TABLE_USER_GUIDE + " (" +
                COLUMN_UG_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_UG_TYPE +" TEXT , " +
                COLUMN_UG_VALUE +" TEXT DEFAULT 0" +
                ");";

        private static final String INSERT_USER_GUIDE_STATUS ="INSERT INTO "+TABLE_USER_GUIDE+" ("+
                COLUMN_UG_TYPE+") VALUES"+
                "('menu')," +"('materia')," +"('mm')," +
                "('mmt')," +"('hv')," +
                "('hve')," +"('noti')," +"('cal')," +
                "('prof1')," +"('prof2')," +
                "('alum');";  */



        private Context mContext;
        public FiendHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {

              //  db.execSQL(CREATE_TABLE_MENU_STATUS);
              //  db.execSQL(INSERT_MENU_STATUS);

                db.execSQL(CREATE_TABLE_USER_REGISTRO);
                db.execSQL(INSERT_USER_REGISTRO);

                db.execSQL(CREATE_TABLE_NOTIFY);

             //   db.execSQL(CREATE_TABLE_USER_GUIDE);
              //  db.execSQL(INSERT_USER_GUIDE_STATUS);

                L.m("create table box office executed");
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                L.m("upgrade table box office executed");
                db.execSQL(" DROP TABLE " + TABLE_USER_REGISTRO + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }
    }

}
