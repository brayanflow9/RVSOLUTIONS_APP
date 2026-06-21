package controlador;

import dao.OrdenServicioDAO;
import modelo.OrdenServicio;

import java.util.List;

public class OrdenServicioController {

    private OrdenServicioDAO ordenServicioDAO;

    public OrdenServicioController() {
        ordenServicioDAO = new OrdenServicioDAO();
    }

    public boolean registrarOrden(int idCliente, int idEquipo, int idTecnico, String fechaRegistro, String problemaReportado, String estado) {
        if (idCliente <= 0) {
            return false;
        }

        if (idEquipo <= 0) {
            return false;
        }

        if (idTecnico <= 0) {
            return false;
        }

        if (fechaRegistro == null || fechaRegistro.trim().isEmpty()) {
            return false;
        }

        if (problemaReportado == null || problemaReportado.trim().isEmpty()) {
            return false;
        }

        OrdenServicio orden = new OrdenServicio(idCliente, idEquipo, idTecnico, fechaRegistro, problemaReportado, estado);
        return ordenServicioDAO.registrar(orden);
    }

    public List<OrdenServicio> listarOrdenes() {
        return ordenServicioDAO.listar();
    }

    public boolean actualizarOrden(int idOrden, int idCliente, int idEquipo, int idTecnico, String fechaRegistro, String problemaReportado, String estado) {
        if (idOrden <= 0) {
            return false;
        }

        if (idCliente <= 0) {
            return false;
        }

        if (idEquipo <= 0) {
            return false;
        }

        if (idTecnico <= 0) {
            return false;
        }

        if (fechaRegistro == null || fechaRegistro.trim().isEmpty()) {
            return false;
        }

        if (problemaReportado == null || problemaReportado.trim().isEmpty()) {
            return false;
        }

        OrdenServicio orden = new OrdenServicio();
        orden.setIdOrden(idOrden);
        orden.setIdCliente(idCliente);
        orden.setIdEquipo(idEquipo);
        orden.setIdTecnico(idTecnico);
        orden.setFechaRegistro(fechaRegistro);
        orden.setProblemaReportado(problemaReportado);
        orden.setEstado(estado);

        return ordenServicioDAO.actualizar(orden);
    }

    public boolean eliminarOrden(int idOrden) {
        if (idOrden <= 0) {
            return false;
        }

        return ordenServicioDAO.eliminar(idOrden);
    }

    public OrdenServicio buscarOrdenPorId(int idOrden) {
        if (idOrden <= 0) {
            return null;
        }

        return ordenServicioDAO.buscarPorId(idOrden);
    }
}