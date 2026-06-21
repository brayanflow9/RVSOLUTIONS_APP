package dao;

import conexion.ConexionMySQL;
import modelo.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public boolean registrar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombres, apellidos, dni, telefono, correo, direccion, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conexion = ConexionMySQL.conectar();
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getApellidos());
            ps.setString(3, cliente.getDni());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getCorreo());
            ps.setString(6, cliente.getDireccion());
            ps.setString(7, cliente.getEstado());

            int filas = ps.executeUpdate();

            ps.close();
            conexion.close();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar cliente");
            e.printStackTrace();
            return false;
        }
    }

    public List<Cliente> listar() {
        List<Cliente> listaClientes = new ArrayList<>();

        String sql = "SELECT * FROM clientes ORDER BY id_cliente DESC";

        try {
            Connection conexion = ConexionMySQL.conectar();
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();

                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNombres(rs.getString("nombres"));
                cliente.setApellidos(rs.getString("apellidos"));
                cliente.setDni(rs.getString("dni"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setCorreo(rs.getString("correo"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setEstado(rs.getString("estado"));

                listaClientes.add(cliente);
            }

            rs.close();
            ps.close();
            conexion.close();

        } catch (SQLException e) {
            System.out.println("Error al listar clientes");
            e.printStackTrace();
        }

        return listaClientes;
    }

    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombres = ?, apellidos = ?, dni = ?, telefono = ?, correo = ?, direccion = ?, estado = ? WHERE id_cliente = ?";

        try {
            Connection conexion = ConexionMySQL.conectar();
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getApellidos());
            ps.setString(3, cliente.getDni());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getCorreo());
            ps.setString(6, cliente.getDireccion());
            ps.setString(7, cliente.getEstado());
            ps.setInt(8, cliente.getIdCliente());

            int filas = ps.executeUpdate();

            ps.close();
            conexion.close();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente");
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idCliente) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        try {
            Connection conexion = ConexionMySQL.conectar();
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, idCliente);

            int filas = ps.executeUpdate();

            ps.close();
            conexion.close();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente");
            e.printStackTrace();
            return false;
        }
    }

    public Cliente buscarPorId(int idCliente) {
        Cliente cliente = null;

        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";

        try {
            Connection conexion = ConexionMySQL.conectar();
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, idCliente);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cliente = new Cliente();

                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNombres(rs.getString("nombres"));
                cliente.setApellidos(rs.getString("apellidos"));
                cliente.setDni(rs.getString("dni"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setCorreo(rs.getString("correo"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setEstado(rs.getString("estado"));
            }

            rs.close();
            ps.close();
            conexion.close();

        } catch (SQLException e) {
            System.out.println("Error al buscar cliente");
            e.printStackTrace();
        }

        return cliente;
    }
}