package com.universidad.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Matricula {
    private Alumno alumno;
    private Curso curso;
    private LocalDate fechaInscripcion;
    private Nota notaAsignada;
    private String estado; // ACTIVO, RETIRADO, COMPLETADO
    
    public Matricula(Alumno alumno, Curso curso, LocalDate fechaInscripcion) {
        if (alumno == null || curso == null || fechaInscripcion == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
        }
        
        this.alumno = alumno;
        this.curso = curso;
        this.fechaInscripcion = fechaInscripcion;
        this.notaAsignada = null;
        this.estado = "ACTIVO";
    }
    // Asigna una nota a la matrícula
    public void asignarNota(Nota nota) {
        if (nota == null) {
            throw new IllegalArgumentException("La nota no puede ser nula");
        }
        
        this.notaAsignada = nota;
        // Actualizar estado según la nota
        if (nota.esAprobatoria()) {
            this.estado = "COMPLETADO";
        }
    }
    
    // Verifica si el alumno aprobó el curso

    public boolean estaAprobado() {
        return notaAsignada != null && notaAsignada.esAprobatoria();
    }
    // Verifica si la matrícula está activa
    public boolean estaActiva() {
        return "ACTIVO".equals(estado);
    }
    // Retira al alumno del curso
    public void retirar() {
        this.estado = "RETIRADO";
    }
    // Completa la matrícula
    public void completar() {
        this.estado = "COMPLETADO";
    }
    // Obtiene el nombre del alumno
    public String getAlumno() {
        return alumno.getNombreCompleto();
    }
    // Getters
    public Alumno getAlumnoObj() { return alumno; }
    public Curso getCurso() { return curso; }
    public LocalDate getFechaInscripcion() { return fechaInscripcion; }
    public Nota getNotaAsignada() { return notaAsignada; }
    public String getEstado() { return estado; }
    // Setters
    public void setEstado(String estado) {
        this.estado = estado;
    }
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String notaInfo = notaAsignada != null 
            ? String.format("Nota: %.2f (%s)", 
                notaAsignada.getValor(),
                estaAprobado() ? "APROBADO" : "DESAPROBADO")
            : "Sin nota";
        
        return String.format("Matrícula: %s en %s | Fecha: %s | %s | Estado: %s",
            alumno.getNombreCompleto(), curso.getNombre(),
            fechaInscripcion.format(formatter), notaInfo, estado);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matricula matricula = (Matricula) o;
        return Objects.equals(alumno, matricula.alumno) &&
               Objects.equals(curso, matricula.curso) &&
               Objects.equals(fechaInscripcion, matricula.fechaInscripcion);
    }
    @Override
    public int hashCode() {
        return Objects.hash(alumno, curso, fechaInscripcion);
    }
}