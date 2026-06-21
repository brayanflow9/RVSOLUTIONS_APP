package controlador;

import dao.PagoDAO;
import modelo.Pago;

import java.util.List;

public class PagoController {

    private PagoDAO pagoDAO;

    public PagoController() {
        pagoDAO = new PagoDAO();
    }

    public boolean registrarPago(int idOrden, double monto, String metodoPago, String fechaPago, String estado) {
        if (idOrden <= 0) {
            return false;
        }

        if (monto <= 0) {
            return false;
        }

        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            return false;
        }

        if (fechaPago == null || fechaPago.trim().isEmpty()) {
            return false;
        }

        Pago pago = new Pago(idOrden, monto, metodoPago, fechaPago, estado);
        return pagoDAO.registrar(pago);
    }

    public List<Pago> listarPagos() {
        return pagoDAO.listar();
    }

    public boolean actualizarPago(int idPago, int idOrden, double monto, String metodoPago, String fechaPago, String estado) {
        if (idPago <= 0) {
            return false;
        }

        if (idOrden <= 0) {
            return false;
        }

        if (monto <= 0) {
            return false;
        }

        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            return false;
        }

        if (fechaPago == null || fechaPago.trim().isEmpty()) {
            return false;
        }

        Pago pago = new Pago();
        pago.setIdPago(idPago);
        pago.setIdOrden(idOrden);
        pago.setMonto(monto);
        pago.setMetodoPago(metodoPago);
        pago.setFechaPago(fechaPago);
        pago.setEstado(estado);

        return pagoDAO.actualizar(pago);
    }

    public boolean eliminarPago(int idPago) {
        if (idPago <= 0) {
            return false;
        }

        return pagoDAO.eliminar(idPago);
    }

    public Pago buscarPagoPorId(int idPago) {
        if (idPago <= 0) {
            return null;
        }

        return pagoDAO.buscarPorId(idPago);
    }
}