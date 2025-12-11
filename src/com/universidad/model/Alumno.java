package com.universidad.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Alumno extends Persona {
    private String CUI; // Código Único de Identificación
    private List<Carrera> carrerasInscripto;
    private List<Matricula> matriculas;
    
    public Alumno(String nombre, String apellido, String DNI, 
                  LocalDate fechaNacimiento, String CUI) {
        super(nombre, apellido, DNI, fechaNacimiento);
        this.CUI = CUI;
        this.carrerasInscripto = new ArrayList<>();
        this.matriculas = new ArrayList<>();
    }
    
    @Override
    public String getTipoPersona() {
        return "Alumno";
    }
    // Inscribe al alumno en una carrera
    public void inscribirCarrera(Carrera carrera) {
        if (carrera == null) {
            throw new IllegalArgumentException("La carrera no puede ser nula");
        }
        
        if (!carrerasInscripto.contains(carrera)) {
            carrerasInscripto.add(carrera);
        }
    }
    // Matricula al alumno en un curso
    public void matricularEnCurso(Curso curso) {
        if (curso == null) {
            throw new IllegalArgumentException("El curso no puede ser nulo");
        }
        
        Matricula matricula = new Matricula(this, curso, LocalDate.now());
        this.matriculas.add(matricula);
    }
    // Verifica si el alumno está inscrito en una carrera específica
    public boolean estaInscritoEnCarrera(Carrera carrera) {
        return carrerasInscripto.contains(carrera);
    }
    // Verifica si el alumno está matriculado en un curso
    public boolean estaMatriculadoEn(String codigoCurso) {
        return matriculas.stream()
            .anyMatch(m -> m.getCurso().getCodigo().equals(codigoCurso));
    }
    // Obtiene el promedio ponderado general del alumno
    public double obtenerPromedioGeneral() {
        double sumaPonderada = 0;
        int totalCreditos = 0;
        
        for (Matricula m : matriculas) {
            if (m.getNotaAsignada() != null) {
                int creditos = m.getCurso().getCreditos();
                sumaPonderada += m.getNotaAsignada().getValor() * creditos;
                totalCreditos += creditos;
            }
        }
        
        return totalCreditos > 0 ? sumaPonderada / totalCreditos : 0.0;
    }
    // Obtiene las matrículas de una carrera específica
    public List<Matricula> getMatriculasPorCarrera(Carrera carrera) {
        if (carrera == null) return new ArrayList<>();
        
        return matriculas.stream()
            .filter(m -> {
                if (m.getCurso() instanceof CursoCarrera) {
                    CursoCarrera cc = (CursoCarrera) m.getCurso();
                    return carrera.getPlanDeEstudio().stream()
                        .anyMatch(c -> c.getCodigo().equals(cc.getCodigo()));
                }
                return false;
            })
            .collect(Collectors.toList());
    }
    // Verifica si aprobó un curso específico
    public boolean aproboCarso(String codigoCurso) {
        return matriculas.stream()
            .filter(m -> m.getCurso().getCodigo().equals(codigoCurso))
            .anyMatch(Matricula::estaAprobado);
    }
    // Obtiene el total de créditos aprobados
    public int obtenerCreditosAprobados() {
        return matriculas.stream()
            .filter(Matricula::estaAprobado)
            .mapToInt(m -> m.getCurso().getCreditos())
            .sum();
    }
    // Obtiene el total de cursos aprobados
    public int obtenerCursosAprobados() {
        return (int) matriculas.stream()
            .filter(Matricula::estaAprobado)
            .count();
    }
    // Getters
    public String obtenerCui() { return CUI; }
    public String getCUI() { return CUI; }
    public List<Carrera> getCarrerasInscripto() { return new ArrayList<>(carrerasInscripto); }
    public List<Matricula> getMatriculas() { return new ArrayList<>(matriculas); }
    @Override
    public String toString() {
        return String.format("Alumno [%s] - %s | Carreras: %d | Matrículas: %d | Promedio: %.2f",
            CUI, super.toString(), carrerasInscripto.size(), 
            matriculas.size(), obtenerPromedioGeneral());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alumno alumno = (Alumno) o;
        return Objects.equals(CUI, alumno.CUI);
    }
    @Override
    public int hashCode() {
        return Objects.hash(CUI);
    }
}
