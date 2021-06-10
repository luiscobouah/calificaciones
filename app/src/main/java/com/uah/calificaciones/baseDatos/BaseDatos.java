package com.uah.calificaciones.baseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {
    /**
     * Controlador de la bd
     */
    public final static String DATABASE_NAME = "Calificaciones.db";
    public final static int DATABASE_VERSION = 3;

    String createSQL = "CREATE TABLE " + ConfigBaseDatos.Califiaciones.TABLE_NAME + " (" +
            ConfigBaseDatos.Califiaciones.COLUMN_ID + " INTEGER PRIMARY KEY," +
            ConfigBaseDatos.Califiaciones.COLUMN_ASIGNATURA + " TEXT," +
            ConfigBaseDatos.Califiaciones.COLUMN_CALIFICACION + " TEXT," +
            ConfigBaseDatos.Califiaciones.COLUMN_FECHA_CALIFICACION + " TEXT)";

    String dropSQL =  "DROP TABLE IF EXISTS " + ConfigBaseDatos.Califiaciones.TABLE_NAME;

    public BaseDatos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       //Creamos la tabla
        db.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropSQL);
        onCreate(db);
    }

    // Guarda una nueva calificacion.
    public void insertCalificacion(SQLiteDatabase db, int id, String asignatura, String calificacion, String fechaCalificacion){
        // Preparamos el registro  a insertar
        ContentValues nuevaCalificacion = new ContentValues();
        nuevaCalificacion.put(ConfigBaseDatos.Califiaciones.COLUMN_ASIGNATURA, asignatura);
        nuevaCalificacion.put(ConfigBaseDatos.Califiaciones.COLUMN_CALIFICACION, calificacion);
        nuevaCalificacion.put(ConfigBaseDatos.Califiaciones.COLUMN_FECHA_CALIFICACION, fechaCalificacion);
       // Realizamos el insert en la base de datos.
        db.insert(ConfigBaseDatos.Califiaciones.TABLE_NAME, null, nuevaCalificacion);
    }

    // Elimina una calificaciones por su ID
    public void deleteCalificacion(SQLiteDatabase db, int id){
        String[] args = new String[] {Integer.toString(id)};
        db.delete(ConfigBaseDatos.Califiaciones.TABLE_NAME, "id=?", args);
    }

    //Elimina todas la calificaciones
    public void deleteAllCalificacion(SQLiteDatabase db){
        db.execSQL("delete from "+ ConfigBaseDatos.Califiaciones.TABLE_NAME);
    }

    //Actualiza una calificacion
    public void updateCalificacion(SQLiteDatabase db, int id, String asignatura, String calificacion,String fechaCalificacion) {
        ContentValues values = new ContentValues();
        values.put(ConfigBaseDatos.Califiaciones.COLUMN_ASIGNATURA, asignatura);
        values.put(ConfigBaseDatos.Califiaciones.COLUMN_CALIFICACION, calificacion);
        values.put(ConfigBaseDatos.Califiaciones.COLUMN_FECHA_CALIFICACION, fechaCalificacion);
        String[] args = new String[]{Integer.toString(id)
        };

        db.update(ConfigBaseDatos.Califiaciones.TABLE_NAME, values, "id=?", args);
    }

    //Obtiene todas las calificacion
    public Cursor getAllCalificaciones(SQLiteDatabase db){
        String[] fields = new String[] {
                ConfigBaseDatos.Califiaciones.COLUMN_ID,
                ConfigBaseDatos.Califiaciones.COLUMN_ASIGNATURA,
                ConfigBaseDatos.Califiaciones.COLUMN_CALIFICACION,
                ConfigBaseDatos.Califiaciones.COLUMN_FECHA_CALIFICACION
        };
        String[] args = new String[] {};
        Cursor c = db.query(ConfigBaseDatos.Califiaciones.TABLE_NAME, fields, null, args, null, null, null);
        return c;
    }

    //Obtiene una calificacion por su ID
    public Cursor getCalificacionById(SQLiteDatabase db,  int id) {
        String selectQuery = "SELECT  * FROM " + ConfigBaseDatos.Califiaciones.TABLE_NAME + " Where " + ConfigBaseDatos.Califiaciones.COLUMN_ID + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
}
