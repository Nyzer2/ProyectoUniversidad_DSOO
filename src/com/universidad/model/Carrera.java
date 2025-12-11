package com.universidad.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Carrera {
    private String nombre;
    private String codigo;
    private int duracionAnios;
    private List<CursoCarrera> planDeEstudio;
    
    public Carrera(String nombre, String codigo, int duracionAnios) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.duracionAnios = duracionAnios;
        this.planDeEstudio = new ArrayList<>();
    }
    // Agrega un curso al plan de estudios
    public void agregarCurso(CursoCarrera curso) {
        if (curso == null) {
            throw new IllegalArgumentException("El curso no puede ser nulo");
        }
        
        if (!planDeEstudio.contains(curso)) {
            planDeEstudio.add(curso);
        }
    }
    // Remueve un curso del plan de estudios
    public void removerCurso(CursoCarrera curso) {
        planDeEstudio.remove(curso);
    }
    // Obtiene los cursos de un semestre específico
    public List<CursoCarrera> obtenerCursosPorSemestre(int semestre) {
        return planDeEstudio.stream()
            .filter(c -> c.obtenerSemestre() == semestre)
            .collect(Collectors.toList());
    }
    // Obtiene los cursos de un área temática específica
    public List<CursoCarrera> obtenerCursosPorArea(String area) {
        if (area == null || area.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return planDeEstudio.stream()
            .filter(c -> c.getAreaTematica().equalsIgnoreCase(area.trim()))
            .collect(Collectors.toList());
    }
    // Calcula el total de créditos de la carrera
    public int obtenerTotalCreditos() {
        return planDeEstudio.stream()
            .mapToInt(CursoCarrera::getCreditos)
            .sum();
    }
    // Obtiene el total de semestres (años * 2)
    public int obtenerTotalSemestres() {
        return duracionAnios * 2;
    }
    // Busca un curso por su código
    public CursoCarrera buscarCursoPorCodigo(String codigo) {
        return planDeEstudio.stream()
            .filter(c -> c.getCodigo().equals(codigo))
            .findFirst()
            .orElse(null);
    }
    // Verifica si un curso pertenece al plan de estudios
    public boolean tieneCurso(String codigoCurso) {
        return planDeEstudio.stream()
            .anyMatch(c -> c.getCodigo().equals(codigoCurso));
    }
    // Getters
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public int getDuracionAnios() { return duracionAnios; }
    public List<CursoCarrera> getPlanDeEstudio() { 
        return new ArrayList<>(planDeEstudio); 
    }
    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDuracionAnios(int duracionAnios) { 
        this.duracionAnios = duracionAnios; 
    }
    @Override
    public String toString() {
        return String.format("Carrera [%s] %s - %d años | Cursos: %d | Créditos: %d",
            codigo, nombre, duracionAnios, 
            planDeEstudio.size(), obtenerTotalCreditos());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carrera carrera = (Carrera) o;
        return Objects.equals(codigo, carrera.codigo);
    }
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
