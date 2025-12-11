package com.universidad.dao;

import com.universidad.model.Alumno;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {
    //Inserta un nuevo alumno en la base de datos
    public boolean insertar(Alumno alumno) {
        String sql = "INSERT INTO alumnos (cui, nombre, apellido, dni, fecha_nacimiento, email) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, alumno.obtenerCui());
            pstmt.setString(2, alumno.getNombre());
            pstmt.setString(3, alumno.getApellido());
            pstmt.setString(4, alumno.getDNI());
            pstmt.setDate(5, Date.valueOf(alumno.getFechaNacimiento()));
            pstmt.setString(6, ""); // Email opcional
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar alumno: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(conn, pstmt, null);
        }
    }
    //Busca un alumno por su CUI
    public Alumno buscarPorCUI(String cui) {
        String sql = "SELECT * FROM alumnos WHERE cui = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cui);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapearAlumno(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar alumno: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, pstmt, rs);
        }
        
        return null;
    }
    //Lista todos los alumnos
    public List<Alumno> listarTodos() {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos ORDER BY apellido, nombre";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                alumnos.add(mapearAlumno(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar alumnos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        
        return alumnos;
    }
    //Lista alumnos por carrera
    public List<Alumno> listarPorCarrera(String codigoCarrera) {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT DISTINCT a.* FROM alumnos a " +
                    "INNER JOIN alumno_carrera ac ON a.cui = ac.cui_alumno " +
                    "WHERE ac.codigo_carrera = ? " +
                    "ORDER BY a.apellido, a.nombre";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codigoCarrera);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                alumnos.add(mapearAlumno(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar alumnos por carrera: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, pstmt, rs);
        }
        
        return alumnos;
    }
    //Actualiza los datos de un alumno
    public boolean actualizar(Alumno alumno) {
        String sql = "UPDATE alumnos SET nombre = ?, apellido = ?, dni = ?, " +
                    "fecha_nacimiento = ? WHERE cui = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, alumno.getNombre());
            pstmt.setString(2, alumno.getApellido());
            pstmt.setString(3, alumno.getDNI());
            pstmt.setDate(4, Date.valueOf(alumno.getFechaNacimiento()));
            pstmt.setString(5, alumno.obtenerCui());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar alumno: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(conn, pstmt, null);
        }
    }
    //Elimina un alumno
    public boolean eliminar(String cui) {
        String sql = "DELETE FROM alumnos WHERE cui = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cui);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar alumno: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(conn, pstmt, null);
        }
    }
    //Calcula el promedio de un alumno
    public double calcularPromedio(String cui) {
        String sql = "SELECT AVG(nota_valor) as promedio FROM matriculas " +
                    "WHERE cui_alumno = ? AND nota_valor IS NOT NULL";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cui);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("promedio");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al calcular promedio: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, pstmt, rs);
        }
        
        return 0.0;
    }
    //Mapea un ResultSet a un objeto Alumno
    private Alumno mapearAlumno(ResultSet rs) throws SQLException {
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String dni = rs.getString("dni");
        LocalDate fechaNac = rs.getDate("fecha_nacimiento").toLocalDate();
        String cui = rs.getString("cui");
        
        return new Alumno(nombre, apellido, dni, fechaNac, cui);
    }
    //Cierra los recursos de base de datos
    private void cerrarRecursos(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
            }
        }
        
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar Statement: " + e.getMessage());
            }
        }
        
        if (conn != null) {
            ConexionDB.cerrarConexion(conn);
        }
    }
}
