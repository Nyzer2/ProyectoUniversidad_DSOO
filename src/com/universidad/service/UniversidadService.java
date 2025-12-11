package com.universidad.service;

import com.universidad.dao.AlumnoDAO;
import com.universidad.dao.CarreraDAO;
import com.universidad.dao.CursoDAO;
import com.universidad.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UniversidadService {
    
    private static UniversidadService instance;
    
    // DAOs
    private AlumnoDAO alumnoDAO;
    private CarreraDAO carreraDAO;
    private CursoDAO cursoDAO;
    
    // Modelo de universidad en memoria
    private Universidad universidad;
    
    // Constructor privado para Singleton
    private UniversidadService() {
        this.alumnoDAO = new AlumnoDAO();
        this.carreraDAO = new CarreraDAO();
        this.cursoDAO = new CursoDAO();
        this.universidad = new Universidad("Universidad Nacional");
        
        // Cargar datos iniciales desde la base de datos
        cargarDatosIniciales();
    }
    
    // Obtiene la instancia única del servicio
    public static synchronized UniversidadService getInstance() {
        if (instance == null) {
            instance = new UniversidadService();
        }
        return instance;
    }
    
    // Carga datos iniciales desde la base de datos
    private void cargarDatosIniciales() {
        try {
            // Cargar carreras
            List<Carrera> carreras = carreraDAO.listarTodas();
            for (Carrera c : carreras) {
                universidad.agregarCarrera(c);
            }
            
            // Cargar alumnos
            List<Alumno> alumnos = alumnoDAO.listarTodos();
            for (Alumno a : alumnos) {
                universidad.getAlumnos().add(a);
            }
            
            System.out.println("✓ Datos iniciales cargados correctamente");
            System.out.println("  - Carreras: " + carreras.size());
            System.out.println("  - Alumnos: " + alumnos.size());
            
        } catch (Exception e) {
            System.err.println("⚠ Error al cargar datos iniciales: " + e.getMessage());
        }
    }
    
    // ==================== GESTIÓN DE ALUMNOS ====================
    
    // Registra un nuevo alumno
    public boolean registrarAlumno(Alumno alumno) {
        if (alumno == null) {
            throw new IllegalArgumentException("El alumno no puede ser nulo");
        }
        // Validar que no exista el CUI
        if (buscarAlumnoPorCUI(alumno.obtenerCui()) != null) {
            System.err.println(" Ya existe un alumno con el CUI: " + alumno.obtenerCui());
            return false;
        }
        // Insertar en base de datos
        boolean insertado = alumnoDAO.insertar(alumno);
        
        if (insertado) {
            universidad.getAlumnos().add(alumno);
            System.out.println("✓ Alumno registrado: " + alumno.getNombreCompleto());
        }
        
        return insertado;
    }
    
    // Actualiza los datos de un alumno
    public boolean actualizarAlumno(Alumno alumno) {
        if (alumno == null) {
            throw new IllegalArgumentException("El alumno no puede ser nulo");
        }
        
        boolean actualizado = alumnoDAO.actualizar(alumno);
        
        if (actualizado) {
            System.out.println("✓ Alumno actualizado: " + alumno.getNombreCompleto());
        }
        
        return actualizado;
    }
    
    // Elimina un alumno por su CUI
    public boolean eliminarAlumno(String cui) {
        if (cui == null || cui.trim().isEmpty()) {
            throw new IllegalArgumentException("El CUI no puede ser vacío");
        }
        
        boolean eliminado = alumnoDAO.eliminar(cui);
        
        if (eliminado) {
            universidad.getAlumnos().removeIf(a -> a.obtenerCui().equals(cui));
            System.out.println("✓ Alumno eliminado con CUI: " + cui);
        }
        
        return eliminado;
    }
    
    // Busca un alumno por su CUI
    public Alumno buscarAlumnoPorCUI(String cui) {
        if (cui == null || cui.trim().isEmpty()) {
            return null;
        }
        
        // Buscar primero en memoria
        Alumno alumno = universidad.buscarAlumnoPorCUI(cui);
        
        // Si no está en memoria, buscar en base de datos
        if (alumno == null) {
            alumno = alumnoDAO.buscarPorCUI(cui);
            if (alumno != null) {
                universidad.getAlumnos().add(alumno);
            }
        }
        
        return alumno;
    }
    
    // Lista todos los alumnos
    public List<Alumno> listarAlumnos() {
        return new ArrayList<>(universidad.getAlumnos());
    }
    
    // Lista los alumnos inscritos en una carrera específica
    public List<Alumno> listarAlumnosPorCarrera(String codigoCarrera) {
        Carrera carrera = buscarCarreraPorCodigo(codigoCarrera);
        if (carrera == null) {
            return new ArrayList<>();
        }
        
        return universidad.obtenerAlumnosPorCarrera(carrera);
    }
    
    // Obtiene el promedio general de un alumno
    public double obtenerPromedioAlumno(String cui) {
        Alumno alumno = buscarAlumnoPorCUI(cui);
        return alumno != null ? alumno.obtenerPromedioGeneral() : 0.0;
    }
    
    // ==================== GESTIÓN DE CARRERAS ====================
    
    // Registra una nueva carrera
    public boolean registrarCarrera(Carrera carrera) {
        if (carrera == null) {
            throw new IllegalArgumentException("La carrera no puede ser nula");
        }
        
        boolean insertada = carreraDAO.insertar(carrera);
        
        if (insertada) {
            universidad.agregarCarrera(carrera);
            System.out.println("✓ Carrera registrada: " + carrera.getNombre());
        }
        
        return insertada;
    }
    
    // Busca una carrera por su código
    public Carrera buscarCarreraPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        
        // Buscar primero en memoria
        Carrera carrera = universidad.buscarCarreraPorCodigo(codigo);
        
        // Si no está en memoria, buscar en base de datos
        if (carrera == null) {
            carrera = carreraDAO.buscarPorCodigo(codigo);
            if (carrera != null) {
                universidad.agregarCarrera(carrera);
            }
        }
        
        return carrera;
    }
    
    // Lista todas las carreras
    public List<Carrera> listarCarreras() {
        return new ArrayList<>(universidad.getCarreras());
    }
    
    // Agrega un curso al plan de estudios de una carrera
    public boolean agregarCursoACarrera(String codigoCarrera, CursoCarrera curso) {
        if (codigoCarrera == null || curso == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
        }
        
        Carrera carrera = buscarCarreraPorCodigo(codigoCarrera);
        if (carrera == null) {
            System.err.println(" Carrera no encontrada: " + codigoCarrera);
            return false;
        }
        
        boolean insertado = cursoDAO.insertar(curso, codigoCarrera);
        
        if (insertado) {
            carrera.agregarCurso(curso);
            System.out.println("✓ Curso agregado a la carrera: " + curso.getNombre());
        }
        
        return insertado;
    }
    
    // ==================== GESTIÓN DE MATRÍCULAS ====================
    
    // Matricula un alumno en una carrera
    public boolean matricularAlumnoEnCarrera(String cui, String codigoCarrera) {
        Alumno alumno = buscarAlumnoPorCUI(cui);
        Carrera carrera = buscarCarreraPorCodigo(codigoCarrera);
        
        if (alumno == null || carrera == null) {
            System.err.println(" Alumno o carrera no encontrados");
            return false;
        }
        
        try {
            universidad.matricularAlumno(alumno, carrera);
            System.out.println("✓ Alumno matriculado en carrera: " + carrera.getNombre());
            return true;
        } catch (Exception e) {
            System.err.println(" Error al matricular: " + e.getMessage());
            return false;
        }
    }
    
    // Matricula un alumno en un curso de una carrera
    public boolean matricularAlumno(Alumno alumno, Carrera carrera, CursoCarrera curso) {
        if (alumno == null || carrera == null || curso == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
        }
        
        // Verificar que el alumno esté inscrito en la carrera
        if (!alumno.estaInscritoEnCarrera(carrera)) {
            System.err.println(" El alumno no está inscrito en la carrera");
            return false;
        }
        
        // Verificar que el curso pertenezca a la carrera
        if (!carrera.tieneCurso(curso.getCodigo())) {
            System.err.println(" El curso no pertenece a la carrera");
            return false;
        }
        
        // Verificar que no esté ya matriculado
        if (alumno.estaMatriculadoEn(curso.getCodigo())) {
            System.err.println(" El alumno ya está matriculado en este curso");
            return false;
        }
        
        // Validar límite de créditos (22 créditos por ciclo)
        int creditosActuales = calcularCreditosCicloActual(alumno);
        if (creditosActuales + curso.getCreditos() > 22) {
            System.err.println(" Se excede el límite de 22 créditos por ciclo");
            return false;
        }
        
        try {
            alumno.matricularEnCurso(curso);
            System.out.println("✓ Alumno matriculado en curso: " + curso.getNombre());
            return true;
        } catch (Exception e) {
            System.err.println("Error al matricular: " + e.getMessage());
            return false;
        }
    }
    
    // Calcula los créditos del ciclo actual para un alumno
    private int calcularCreditosCicloActual(Alumno alumno) {
        // Obtener matrículas del ciclo actual (últimos 6 meses)
        LocalDate inicioSemestre = LocalDate.now().minusMonths(6);
        
        return alumno.getMatriculas().stream()
            .filter(m -> m.getFechaInscripcion().isAfter(inicioSemestre))
            .filter(m -> m.estaActiva())
            .mapToInt(m -> m.getCurso().getCreditos())
            .sum();
    }
    
    // Asigna una nota a un alumno en un curso
    public boolean asignarNota(String cui, String codigoCurso, double valorNota) {
        Alumno alumno = buscarAlumnoPorCUI(cui);
        if (alumno == null) {
            System.err.println("Alumno no encontrado");
            return false;
        }
        
        // Buscar la matrícula
        Matricula matricula = alumno.getMatriculas().stream()
            .filter(m -> m.getCurso().getCodigo().equals(codigoCurso))
            .findFirst()
            .orElse(null);
        
        if (matricula == null) {
            System.err.println("Matrícula no encontrada");
            return false;
        }
        
        try {
            Nota nota = new Nota(valorNota, "Evaluación Final", LocalDate.now());
            matricula.asignarNota(nota);
            System.out.println("✓ Nota asignada: " + valorNota);
            return true;
        } catch (Exception e) {
            System.err.println("Error al asignar nota: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== ESTADÍSTICAS ====================
    
    // Obtiene estadísticas generales de la universidad
    public String obtenerEstadisticas() {
        return universidad.obtenerEstadisticas();
    }
    
    // Obtiene el total de alumnos
    public int getTotalAlumnos() {
        return universidad.getAlumnos().size();
    }
    
    // Obtiene el total de carreras
    public int getTotalCarreras() {
        return universidad.getCarreras().size();
    }
    
    // Obtiene el total de matrículas
    public int getTotalMatriculas() {
        return universidad.getAlumnos().stream()
            .mapToInt(a -> a.getMatriculas().size())
            .sum();
    }
    
    // Obtiene la universidad
    public Universidad getUniversidad() {
        return universidad;
    }
}