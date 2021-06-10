package com.uah.calificaciones.model;

public class Calificacion {

        private String id;
        private String asignatura;
        private String calificacion;
        private String fechaCalificacion;

    public Calificacion(String id, String asignatura, String calificacion, String fechaCalificacion) {
        this.id = id;
        this.asignatura = asignatura;
        this.calificacion = calificacion;
        this.fechaCalificacion = fechaCalificacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(String fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }
}
