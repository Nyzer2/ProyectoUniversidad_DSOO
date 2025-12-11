package com.universidad.model;

public class CursoIdioma extends Curso {
    private String idioma;
    private String nivel;
    
    public CursoIdioma(String nombre, String codigo, int creditos, 
                      String idioma, String nivel) {
        super(nombre, codigo, creditos);
        this.idioma = idioma;
        this.nivel = nivel;
    }
    @Override
    public String obtenerTipoCurso() {
        return "Curso de Idioma";
    }
    // Obtiene una descripción del tipo de curso
    public String obtenerTipo(Curso curso) {
        if (curso instanceof CursoIdioma) {
            CursoIdioma ci = (CursoIdioma) curso;
            return ci.idioma + " - " + ci.nivel;
        }
        return "Curso Regular";
    }
    // Verifica si es un nivel avanzado
    public boolean esNivelAvanzado() {
        return nivel != null && 
               (nivel.equalsIgnoreCase("Avanzado") || 
                nivel.equalsIgnoreCase("Superior"));
    }
    // Getters y Setters
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    @Override
    public String toString() {
        return String.format("%s | Idioma: %s | Nivel: %s",
            super.toString(), idioma, nivel);
    }
}
