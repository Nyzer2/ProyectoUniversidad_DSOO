package com.universidad.dao;

import com.universidad.model.CursoCarrera;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//DAO para gestionar cursos en la base de datos
public class CursoDAO {
    //Inserta un nuevo curso asociado a una carrera
    public boolean insertar(CursoCarrera curso, String codigoCarrera) {
        String sql = "INSERT INTO cursos_carrera " +
                    "(codigo, nombre, creditos, area_tematica, semestre, codigo_carrera) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, curso.getCodigo());
            pstmt.setString(2, curso.getNombre());
            pstmt.setInt(3, curso.getCreditos());
            pstmt.setString(4, curso.getAreaTematica());
            pstmt.setInt(5, curso.obtenerSemestre());
            pstmt.setString(6, codigoCarrera);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar curso: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(conn, pstmt, null);
        }
    }
    //Busca un curso por su código y carrera
    public CursoCarrera buscarPorCodigo(String codigoCurso, String codigoCarrera) {
        String sql = "SELECT * FROM cursos_carrera WHERE codigo = ? AND codigo_carrera = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codigoCurso);
            pstmt.setString(2, codigoCarrera);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapearCurso(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar curso: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, pstmt, rs);
        }
        
        return null;
    }
    //Lista todos los cursos de una carrera
    public List<CursoCarrera> listarPorCarrera(String codigoCarrera) {
        List<CursoCarrera> cursos = new ArrayList<>();
        String sql = "SELECT * FROM cursos_carrera WHERE codigo_carrera = ? ORDER BY semestre, nombre";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codigoCarrera);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                cursos.add(mapearCurso(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar cursos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, pstmt, rs);
        }
        
        return cursos;
    }
    //Lista cursos por semestre
    public List<CursoCarrera> listarPorSemestre(String codigoCarrera, int semestre) {
        List<CursoCarrera> cursos = new ArrayList<>();
        String sql = "SELECT * FROM cursos_carrera " +
                    "WHERE codigo_carrera = ? AND semestre = ? " +
                    "ORDER BY nombre";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codigoCarrera);
            pstmt.setInt(2, semestre);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                cursos.add(mapearCurso(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar cursos por semestre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, pstmt, rs);
        }
        
        return cursos;
    }
    //Lista cursos por área temática
    public List<CursoCarrera> listarPorArea(String codigoCarrera, String areaTematica) {
        List<CursoCarrera> cursos = new ArrayList<>();
        String sql = "SELECT * FROM cursos_carrera " +
                    "WHERE codigo_carrera = ? AND area_tematica = ? " +
                    "ORDER BY semestre, nombre";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codigoCarrera);
            pstmt.setString(2, areaTematica);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                cursos.add(mapearCurso(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar cursos por área: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, pstmt, rs);
        }
        
        return cursos;
    }
    //Actualiza los datos de un curso
    public boolean actualizar(CursoCarrera curso, String codigoCarrera) {
        String sql = "UPDATE cursos_carrera SET nombre = ?, creditos = ?, " +
                    "area_tematica = ?, semestre = ? " +
                    "WHERE codigo = ? AND codigo_carrera = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, curso.getNombre());
            pstmt.setInt(2, curso.getCreditos());
            pstmt.setString(3, curso.getAreaTematica());
            pstmt.setInt(4, curso.obtenerSemestre());
            pstmt.setString(5, curso.getCodigo());
            pstmt.setString(6, codigoCarrera);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar curso: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(conn, pstmt, null);
        }
    }
    //Elimina un curso
    public boolean eliminar(String codigoCurso, String codigoCarrera) {
        String sql = "DELETE FROM cursos_carrera WHERE codigo = ? AND codigo_carrera = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codigoCurso);
            pstmt.setString(2, codigoCarrera);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar curso: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(conn, pstmt, null);
        }
    }
    //Cuenta el total de créditos de una carrera
    public int contarCreditosTotales(String codigoCarrera) {
        String sql = "SELECT SUM(creditos) as total FROM cursos_carrera WHERE codigo_carrera = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.obtenerConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codigoCarrera);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar créditos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, pstmt, rs);
        }
        
        return 0;
    }
    //Mapea un ResultSet a un objeto CursoCarrera
    private CursoCarrera mapearCurso(ResultSet rs) throws SQLException {
        String nombre = rs.getString("nombre");
        String codigo = rs.getString("codigo");
        int creditos = rs.getInt("creditos");
        String areaTematica = rs.getString("area_tematica");
        int semestre = rs.getInt("semestre");
        
        return new CursoCarrera(nombre, codigo, creditos, areaTematica, semestre);
    }
    //Cierra recursos de base de datos de forma segura
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

