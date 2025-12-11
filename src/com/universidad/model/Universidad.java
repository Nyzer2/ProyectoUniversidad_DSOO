package com.universidad.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Universidad {
    private String nombre;
    private List<Carrera> carreras;
    private List<Profesor> profesores;
    private List<Alumno> alumnos;
    
    public Universidad(String nombre) {
        this.nombre = nombre;
        this.carreras = new ArrayList<>();
        this.profesores = new ArrayList<>();
        this.alumnos = new ArrayList<>();
    }
    // Agrega una carrera a la universidad
    public void agregarCarrera(Carrera carrera) {
        if (carrera == null) {
            throw new IllegalArgumentException("La carrera no puede ser nula");
        }
        
        if (!carreras.contains(carrera)) {
            carreras.add(carrera);
        }
    }
    // Contrata un profesor
    public void contratarProfesor(Profesor profesor) {
        if (profesor == null) {
            throw new IllegalArgumentException("El profesor no puede ser nulo");
        }
        
        if (!profesores.contains(profesor)) {
            profesores.add(profesor);
        }
    }
    // Matricula un alumno en una carrera
    public void matricularAlumno(Alumno alumno, Carrera carrera) {
        if (alumno == null || carrera == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
        }
        
        // Agregar alumno si no existe
        if (!alumnos.contains(alumno)) {
            alumnos.add(alumno);
        }
        
        // Inscribir en carrera
        if (!carreras.contains(carrera)) {
            throw new IllegalStateException("La carrera no existe en la universidad");
        }
        
        alumno.inscribirCarrera(carrera);
    }
    // Matricula un alumno en una carrera y curso específico
    public void matricularAlumno(Alumno alumno, Carrera carrera, Curso curso) {
        // Primero matricular en la carrera
        matricularAlumno(alumno, carrera);
        // Luego matricular en el curso
        if (curso == null) {
            throw new IllegalArgumentException("El curso no puede ser nulo");
        }
        
        alumno.matricularEnCurso(curso);
    }
    // Obtiene los alumnos de una carrera específica
    public List<Alumno> obtenerAlumnosPorCarrera(Carrera carrera) {
        if (carrera == null) return new ArrayList<>();
        
        return alumnos.stream()
            .filter(a -> a.estaInscritoEnCarrera(carrera))
            .collect(Collectors.toList());
    }
    // Obtiene los profesores de un departamento
    public List<Profesor> obtenerProfesoresPorDepartamento(String departamento) {
        if (departamento == null || departamento.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return profesores.stream()
            .filter(p -> p.getDepartamento().equalsIgnoreCase(departamento.trim()))
            .collect(Collectors.toList());
    }
    // Busca una carrera por código
    public Carrera buscarCarreraPorCodigo(String codigo) {
        return carreras.stream()
            .filter(c -> c.getCodigo().equals(codigo))
            .findFirst()
            .orElse(null);
    }
    // Busca un alumno por CUI
    public Alumno buscarAlumnoPorCUI(String cui) {
        return alumnos.stream()
            .filter(a -> a.obtenerCui().equals(cui))
            .findFirst()
            .orElse(null);
    }
    // Busca un profesor por número de empleado
    public Profesor buscarProfesorPorNumero(String nroEmpleado) {
        return profesores.stream()
            .filter(p -> p.getNroEmpleado().equals(nroEmpleado))
            .findFirst()
            .orElse(null);
    }
    // Obtiene estadísticas generales de la universidad
    public String obtenerEstadisticas() {
        int totalMatriculas = alumnos.stream()
            .mapToInt(a -> a.getMatriculas().size())
            .sum();
        
        int totalCursos = carreras.stream()
            .mapToInt(c -> c.getPlanDeEstudio().size())
            .sum();
        
        return String.format(
            "=== Estadísticas de %s ===\n" +
            "Carreras: %d\n" +
            "Profesores: %d\n" +
            "Alumnos: %d\n" +
            "Total de matrículas: %d\n" +
            "Total de cursos disponibles: %d",
            nombre, carreras.size(), profesores.size(), 
            alumnos.size(), totalMatriculas, totalCursos
        );
    }
    // Getters
    public String obtenerNombre() { return nombre; }
    public String getNombre() { return nombre; }
    public List<Carrera> getCarreras() { return new ArrayList<>(carreras); }
    public List<Profesor> getProfesores() { return new ArrayList<>(profesores); }
    public List<Alumno> getAlumnos() { return new ArrayList<>(alumnos); }
    // Setter
    public void setNombre(String nombre) { this.nombre = nombre; }
    @Override
    public String toString() {
        return String.format("Universidad: %s | Carreras: %d | Profesores: %d | Alumnos: %d",
            nombre, carreras.size(), profesores.size(), alumnos.size());
    }
}
