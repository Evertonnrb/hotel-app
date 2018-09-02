package com.nrb.hoteis.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HotelSqLiteOpenHelper extends SQLiteOpenHelper {

    private static final String NOME_DB = "dbHotel";
    private static final int VERSAO_DB = 1;

    private static final String TABELA_HOTEL = "hotel";
    private static final String COLUNA_ID = "_id";
    private static final String COLUNA_NOME = "nome";
    private static final String COLUNA_ENDERECO = "endereco";
    private static final String COLUNA_ESTRELAS = "estrelas";

    public HotelSqLiteOpenHelper(Context context) {
        super(context, NOME_DB, null, VERSAO_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABELA_HOTEL + " " +
                        "( " + COLUNA_ID + "INTEGER PRIMARY KEY AUTOINCREMENT ," +
                               COLUNA_NOME + " TEXT NOT NULL ," +
                               COLUNA_ENDERECO + " TEXT ," +
                               COLUNA_ESTRELAS + " REAL " +
                        ")"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
