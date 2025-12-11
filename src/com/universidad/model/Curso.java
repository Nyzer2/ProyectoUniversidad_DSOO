package com.universidad.model;

import java.util.Objects;

public abstract class Curso {
    protected String nombre;
    protected String codigo;
    protected int creditos;
    protected Profesor profesorAsignado;
    
    public Curso(String nombre, String codigo, int creditos) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.creditos = creditos;
    }
    // Asigna un profesor al curso
    public void asignarProfesor(Profesor profesor) {
        this.profesorAsignado = profesor;
    }
    // Método abstracto para obtener el tipo de curso
    public abstract String obtenerTipoCurso();
    // Verifica si el curso tiene profesor asignado
    public boolean tieneProfesor() {
        return profesorAsignado != null;
    }
    // Getters
    public String obtenerNombre() { return nombre; }
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public int getCreditos() { return creditos; }
    public Profesor getProfesorAsignado() { return profesorAsignado; }
    @Override
    public String toString() {
        String profesor = tieneProfesor() 
            ? profesorAsignado.getNombreCompleto() 
            : "Sin asignar";
        return String.format("[%s] %s - %d créditos | Profesor: %s",
            codigo, nombre, creditos, profesor);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curso curso = (Curso) o;
        return Objects.equals(codigo, curso.codigo);
    }
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}