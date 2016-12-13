package com.izv.dam.newquip.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.izv.dam.newquip.pojo.NotaMapa;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class AyudanteMapa extends OrmLiteSqliteOpenHelper {
    public static final int VERSION = 1;

    private Dao<NotaMapa,Integer> mapaDao = null;
    private RuntimeExceptionDao<NotaMapa,Integer> simpleRunTimeDao=null;

    public AyudanteMapa(Context context) {

        super(context, "googleLocalizacion", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,NotaMapa.class);
        }catch (java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, NotaMapa.class, true);
            onCreate(database,connectionSource);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<NotaMapa, Integer> getMapaDao() throws java.sql.SQLException {
        if (mapaDao == null){
            mapaDao =getDao(NotaMapa.class);
        }
        return mapaDao;
    }

    public RuntimeExceptionDao<NotaMapa, Integer> getSimpleRunTimeDao() {
        if (simpleRunTimeDao==null){
            simpleRunTimeDao=getRuntimeExceptionDao(NotaMapa.class);
        }
        return simpleRunTimeDao;
    }

    @Override
    public void close(){
        super.close();
        mapaDao = null;
        simpleRunTimeDao = null;
    }
}
