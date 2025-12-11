package com.universidad.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Clase abstracta que representa una persona en el sistema universitario
 */
public abstract class Persona {
    protected String nombre;
    protected String apellido;
    protected String DNI;
    protected LocalDate fechaNacimiento;
    
    public Persona(String nombre, String apellido, String DNI, LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.DNI = DNI;
        this.fechaNacimiento = fechaNacimiento;
    }
    // Getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDNI() { return DNI; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setDNI(String DNI) { this.DNI = DNI; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { 
        this.fechaNacimiento = fechaNacimiento; 
    }
    // Obtiene el nombre completo (Apellido, Nombre)
    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }
    // Calcula la edad de la persona
    public int calcularEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
    // Método abstracto que deben implementar las clases hijas
    public abstract String getTipoPersona();
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("%s | DNI: %s | Edad: %d años", 
            getNombreCompleto(), DNI, calcularEdad());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(DNI, persona.DNI);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(DNI);
    }
}
