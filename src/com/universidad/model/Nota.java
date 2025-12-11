package com.universidad.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Nota {
    private double valor;
    private String tipoEvaluacion;
    private LocalDate fechaEvaluacion;
    
    private static final double NOTA_APROBATORIA = 10.5;
    private static final double NOTA_MINIMA = 0.0;
    private static final double NOTA_MAXIMA = 20.0;
    
    public Nota(double valor, String tipoEvaluacion, LocalDate fechaEvaluacion) {
        if (valor < NOTA_MINIMA || valor > NOTA_MAXIMA) {
            throw new IllegalArgumentException(
                String.format("La nota debe estar entre %.1f y %.1f", 
                    NOTA_MINIMA, NOTA_MAXIMA)
            );
        }
        
        this.valor = valor;
        this.tipoEvaluacion = tipoEvaluacion;
        this.fechaEvaluacion = fechaEvaluacion;
    }
    //Verifica si la nota es aprobatoria
    public boolean esAprobatoria() {
        return valor >= NOTA_APROBATORIA;
    }
    // Alias para esAprobatoria (compatibilidad)
    public boolean getEsAprobable() {
        return esAprobatoria();
    }
    // Obtiene la calificación en letra (A, B, C, D, F)
    public String obtenerLetra() {
        if (valor >= 18) return "A";
        if (valor >= 15) return "B";
        if (valor >= 11) return "C";
        if (valor >= 8) return "D";
        return "F";
    }
    // Obtiene una descripción del desempeño
    public String obtenerDesempeno() {
        if (valor >= 18) return "Excelente";
        if (valor >= 15) return "Muy Bueno";
        if (valor >= 11) return "Bueno";
        if (valor >= 8) return "Regular";
        return "Deficiente";
    }
    // Obtiene el estado académico
    public String obtenerEstadoAcademico() {
        return esAprobatoria() ? "APROBADO" : "DESAPROBADO";
    }
    // Getters
    public double getValor() { return valor; }
    public String getTipoEvaluacion() { return tipoEvaluacion; }
    public LocalDate getFechaEvaluacion() { return fechaEvaluacion; }
    // Setters
    public void setValor(double valor) {
        if (valor < NOTA_MINIMA || valor > NOTA_MAXIMA) {
            throw new IllegalArgumentException(
                String.format("La nota debe estar entre %.1f y %.1f", 
                    NOTA_MINIMA, NOTA_MAXIMA)
            );
        }
        this.valor = valor;
    }
    
    public void setTipoEvaluacion(String tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
    }
    
    public void setFechaEvaluacion(LocalDate fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("Nota: %.2f (%s) | Tipo: %s | Fecha: %s | %s",
            valor, obtenerLetra(), tipoEvaluacion,
            fechaEvaluacion.format(formatter), obtenerEstadoAcademico());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nota nota = (Nota) o;
        return Double.compare(nota.valor, valor) == 0 &&
               Objects.equals(tipoEvaluacion, nota.tipoEvaluacion) &&
               Objects.equals(fechaEvaluacion, nota.fechaEvaluacion);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor, tipoEvaluacion, fechaEvaluacion);
    }
}
