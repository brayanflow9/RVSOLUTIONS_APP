package controlador;

import dao.ClienteDAO;
import modelo.Cliente;

import java.util.List;

public class ClienteController {

    private ClienteDAO clienteDAO;

    public ClienteController() {
        clienteDAO = new ClienteDAO();
    }

    public boolean registrarCliente(String nombres, String apellidos, String dni, String telefono, String correo, String direccion, String estado) {
        if (nombres == null || nombres.trim().isEmpty()) {
            return false;
        }

        if (apellidos == null || apellidos.trim().isEmpty()) {
            return false;
        }

        if (dni == null || dni.trim().isEmpty()) {
            return false;
        }

        Cliente cliente = new Cliente(nombres, apellidos, dni, telefono, correo, direccion, estado);
        return clienteDAO.registrar(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listar();
    }

    public boolean actualizarCliente(int idCliente, String nombres, String apellidos, String dni, String telefono, String correo, String direccion, String estado) {
        if (idCliente <= 0) {
            return false;
        }

        if (nombres == null || nombres.trim().isEmpty()) {
            return false;
        }

        if (apellidos == null || apellidos.trim().isEmpty()) {
            return false;
        }

        if (dni == null || dni.trim().isEmpty()) {
            return false;
        }

        Cliente cliente = new Cliente(idCliente, nombres, apellidos, dni, telefono, correo, direccion, estado);
        return clienteDAO.actualizar(cliente);
    }

    public boolean eliminarCliente(int idCliente) {
        if (idCliente <= 0) {
            return false;
        }

        return clienteDAO.eliminar(idCliente);
    }

    public Cliente buscarClientePorId(int idCliente) {
        if (idCliente <= 0) {
            return null;
        }

        return clienteDAO.buscarPorId(idCliente);
    }
}