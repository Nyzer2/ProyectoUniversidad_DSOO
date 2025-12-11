package com.universidad.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Profesor extends Persona {
    private String nroEmpleado;
    private String departamento;
    private List<Curso> cursosDictados;
    
    public Profesor(String nombre, String apellido, String DNI, 
                   LocalDate fechaNacimiento, String nroEmpleado, String departamento) {
        super(nombre, apellido, DNI, fechaNacimiento);
        this.nroEmpleado = nroEmpleado;
        this.departamento = departamento;
        this.cursosDictados = new ArrayList<>();
    }
    @Override
    public String getTipoPersona() {
        return "Profesor";
    }
    // Asigna un curso al profesor
    public void agregarCursoADictar(Curso curso) {
        if (curso == null) {
            throw new IllegalArgumentException("El curso no puede ser nulo");
        }
        
        if (!cursosDictados.contains(curso)) {
            cursosDictados.add(curso);
            curso.asignarProfesor(this);
        }
    }
    // Remueve un curso de la lista del profesor
    public void removerCurso(Curso curso) {
        cursosDictados.remove(curso);
    }
    // Calcula la carga horaria total del profesor(2 horas por crédito)
    public int calcularCargaHoraria() {
        return cursosDictados.stream()
            .mapToInt(c -> c.getCreditos() * 2)
            .sum();
    }
    // Obtiene el total de créditos que dicta
    public int obtenerTotalCreditos() {
        return cursosDictados.stream()
            .mapToInt(Curso::getCreditos)
            .sum();
    }
    // Getters
    public String obtenerNro() { return nroEmpleado; }
    public String getNroEmpleado() { return nroEmpleado; }
    public String getDepartamento() { return departamento; }
    public List<Curso> getCursosDictados() { return new ArrayList<>(cursosDictados); }
    // Setter
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    @Override
    public String toString() {
        return String.format("Profesor [%s] - %s | Depto: %s | Cursos: %d | Carga: %d hrs",
            nroEmpleado, super.toString(), departamento, 
            cursosDictados.size(), calcularCargaHoraria());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profesor profesor = (Profesor) o;
        return Objects.equals(nroEmpleado, profesor.nroEmpleado);
    }
    @Override
    public int hashCode() {
        return Objects.hash(nroEmpleado);
    }
}
