package com.uah.calificaciones.baseDatos;

import android.provider.BaseColumns;

public class ConfigBaseDatos {

    /* Clase para definicar la tabla y sus columnas*/
    public static class Califiaciones implements BaseColumns {
        public static final String TABLE_NAME = "calificaciones";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ASIGNATURA = "asignatura";
        public static final String COLUMN_CALIFICACION = "calificacion";
        public static final String COLUMN_FECHA_CALIFICACION = "fecha";

    }

}
