package com.universidad.dao;

import com.universidad.model.Carrera;
import com.universidad.model.CursoCarrera;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarreraDAO {
    
    public boolean insertar(Carrera carrera) {
        String sql = "INSERT INTO carreras (codigo, nombre, duracion_anios) VALUES (?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, carrera.getCodigo());
            pstmt.setString(2, carrera.getNombre());
            pstmt.setInt(3, carrera.getDuracionAnios());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar carrera: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(conn, pstmt, null);
        }
    }
    
    public Carrera buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM carreras WHERE codigo = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codigo);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Carrera carrera = mapearCarrera(rs);
                cargarPlanEstudios(carrera);
                return carrera;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar carrera: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, pstmt, rs);
        }
        
        return null;
    }
    
    public List<Carrera> listarTodas() {
        List<Carrera> carreras = new ArrayList<>();
        String sql = "SELECT * FROM carreras ORDER BY nombre";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Carrera carrera = mapearCarrera(rs);
                cargarPlanEstudios(carrera);
                carreras.add(carrera);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar carreras: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        
        return carreras;
    }
    
    private void cargarPlanEstudios(Carrera carrera) {
        String sql = "SELECT * FROM cursos_carrera WHERE codigo_carrera = ? ORDER BY semestre";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, carrera.getCodigo());
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                CursoCarrera curso = new CursoCarrera(
                    rs.getString("nombre"),
                    rs.getString("codigo"),
                    rs.getInt("creditos"),
                    rs.getString("area_tematica"),
                    rs.getInt("semestre")
                );
                carrera.agregarCurso(curso);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al cargar plan de estudios: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, pstmt, rs);
        }
    }
    
    private Carrera mapearCarrera(ResultSet rs) throws SQLException {
        return new Carrera(
            rs.getString("nombre"),
            rs.getString("codigo"),
            rs.getInt("duracion_anios")
        );
    }
    
    private void cerrarRecursos(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) try { rs.close(); } catch (SQLException e) {}
        if (stmt != null) try { stmt.close(); } catch (SQLException e) {}
        if (conn != null) ConexionDB.cerrarConexion(conn);
    }
}
