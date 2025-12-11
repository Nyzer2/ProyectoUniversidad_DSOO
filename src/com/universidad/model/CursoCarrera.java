package com.universidad.model;

public class CursoCarrera extends Curso {
    private int semestre;
    private String areaTematica;
    
    public CursoCarrera(String nombre, String codigo, int creditos, 
                       String areaTematica, int semestre) {
        super(nombre, codigo, creditos);
        this.semestre = semestre;
        this.areaTematica = areaTematica;
    }
    @Override
    public String obtenerTipoCurso() {
        return "Curso de Carrera";
    }
    // Obtiene el área temática del curso
    public String obtenerArea() {
        return areaTematica;
    }
    // Obtiene el semestre en que se dicta
    public int obtenerSemestre() {
        return semestre;
    }
    // Cambia el semestre del curso
    public void setSemestre(int semestre) {
        if (semestre < 1 || semestre > 14) {
            throw new IllegalArgumentException("Semestre inválido: debe estar entre 1 y 14");
        }
        this.semestre = semestre;
    }
    // Getters
    public String getAreaTematica() { return areaTematica; }
    public int getSemestre() { return semestre; }
    // Setter
    public void setAreaTematica(String areaTematica) {
        this.areaTematica = areaTematica;
    }
    @Override
    public String toString() {
        return String.format("%s | Semestre: %d | Área: %s",
            super.toString(), semestre, areaTematica);
    }
}
