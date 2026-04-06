package com.example.vehiculoscrud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Configuración de la base de datos
    private static final String DATABASE_NAME = "vehiculos.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla y columnas
    public static final String TABLE_VEHICULOS   = "vehiculos";
    public static final String COL_ID            = "id";
    public static final String COL_MARCA         = "marca";
    public static final String COL_MODELO        = "modelo";
    public static final String COL_ANIO          = "anio";
    public static final String COL_PLACA         = "placa";
    public static final String COL_COLOR         = "color";

    // SQL para crear la tabla
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_VEHICULOS + " (" +
                    COL_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_MARCA  + " TEXT NOT NULL, " +
                    COL_MODELO + " TEXT NOT NULL, " +
                    COL_ANIO   + " TEXT NOT NULL, " +
                    COL_PLACA  + " TEXT NOT NULL UNIQUE, " +
                    COL_COLOR  + " TEXT NOT NULL" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICULOS);
        onCreate(db);
    }

    // ─────────────────────────────────────────
    // CREATE — Insertar vehículo
    // ─────────────────────────────────────────
    public boolean insertarVehiculo(String marca, String modelo,
                                    String anio, String placa, String color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MARCA,  marca);
        values.put(COL_MODELO, modelo);
        values.put(COL_ANIO,   anio);
        values.put(COL_PLACA,  placa);
        values.put(COL_COLOR,  color);
        long resultado = db.insert(TABLE_VEHICULOS, null, values);
        db.close();
        return resultado != -1;
    }

    // ─────────────────────────────────────────
    // READ — Obtener todos los vehículos
    // ─────────────────────────────────────────
    public List<Vehiculo> obtenerTodosLosVehiculos() {
        List<Vehiculo> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_VEHICULOS + " ORDER BY " + COL_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Vehiculo v = new Vehiculo(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MARCA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MODELO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ANIO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PLACA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_COLOR))
                );
                lista.add(v);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    // ─────────────────────────────────────────
    // UPDATE — Actualizar vehículo
    // ─────────────────────────────────────────
    public boolean actualizarVehiculo(int id, String marca, String modelo,
                                      String anio, String placa, String color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MARCA,  marca);
        values.put(COL_MODELO, modelo);
        values.put(COL_ANIO,   anio);
        values.put(COL_PLACA,  placa);
        values.put(COL_COLOR,  color);
        int filas = db.update(TABLE_VEHICULOS, values,
                COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return filas > 0;
    }

    // ─────────────────────────────────────────
    // DELETE — Eliminar vehículo
    // ─────────────────────────────────────────
    public boolean eliminarVehiculo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filas = db.delete(TABLE_VEHICULOS,
                COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return filas > 0;
    }
}