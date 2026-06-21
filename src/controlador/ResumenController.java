package controlador;

import dao.ResumenDAO;
import java.text.DecimalFormat;

public class ResumenController {
    private ResumenDAO resumenDAO;

    public ResumenController() {
        resumenDAO = new ResumenDAO();
    }

    public int totalClientes() { return resumenDAO.contar("clientes"); }
    public int totalEquipos() { return resumenDAO.contar("equipos"); }
    public int totalOrdenes() { return resumenDAO.contar("ordenes_servicio"); }
    public int totalDiagnosticos() { return resumenDAO.contar("diagnosticos"); }
    public int totalUsuarios() { return resumenDAO.contar("usuarios"); }
    public int ordenesPendientes() { return resumenDAO.contarOrdenesPorEstado("Pendiente"); }
    public int ordenesEnProceso() { return resumenDAO.contarOrdenesPorEstado("En proceso"); }
    public int ordenesFinalizadas() { return resumenDAO.contarOrdenesPorEstado("Finalizado"); }

    public String totalIngresosPagados() {
        DecimalFormat df = new DecimalFormat("S/ #,##0.00");
        return df.format(resumenDAO.totalPagado());
    }
}
