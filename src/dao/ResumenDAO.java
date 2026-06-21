package dao;

import conexion.ConexionMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResumenDAO {

    public int contar(String tabla) {
        if (!tabla.matches("[a-zA-Z_]+")) {
            return 0;
        }
        String sql = "SELECT COUNT(*) AS total FROM " + tabla;
        try (Connection con = ConexionMySQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error al contar registros de " + tabla + ": " + e.getMessage());
        }
        return 0;
    }

    public int contarOrdenesPorEstado(String estado) {
        String sql = "SELECT COUNT(*) AS total FROM ordenes_servicio WHERE estado = ?";
        try (Connection con = ConexionMySQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al contar órdenes por estado: " + e.getMessage());
        }
        return 0;
    }

    public double totalPagado() {
        String sql = "SELECT COALESCE(SUM(monto),0) AS total FROM pagos WHERE estado = 'Pagado'";
        try (Connection con = ConexionMySQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error al sumar pagos: " + e.getMessage());
        }
        return 0.0;
    }
}
