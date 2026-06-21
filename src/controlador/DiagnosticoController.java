package controlador;

import dao.DiagnosticoDAO;
import modelo.Diagnostico;
import java.util.List;

public class DiagnosticoController {

    private DiagnosticoDAO diagnosticoDAO;

    public DiagnosticoController() {
        diagnosticoDAO = new DiagnosticoDAO();
    }

    public boolean registrarDiagnostico(Diagnostico diagnostico) {
        if (diagnostico == null) {
            return false;
        }

        if (diagnostico.getIdOrden() <= 0) {
            return false;
        }

        if (diagnostico.getDescripcionDiagnostico() == null || diagnostico.getDescripcionDiagnostico().trim().isEmpty()) {
            return false;
        }

        if (diagnostico.getIdTecnico() <= 0) diagnostico.setIdTecnico(2);
        if (diagnostico.getEstado() == null || diagnostico.getEstado().isBlank()) diagnostico.setEstado("Pendiente");
        return diagnosticoDAO.registrar(diagnostico);
    }

    public List<Diagnostico> listarDiagnosticos() {
        return diagnosticoDAO.listar();
    }

    public Diagnostico buscarDiagnosticoPorId(int idDiagnostico) {
        if (idDiagnostico <= 0) {
            return null;
        }

        return diagnosticoDAO.buscarPorId(idDiagnostico);
    }

    public boolean actualizarDiagnostico(Diagnostico diagnostico) {
        if (diagnostico == null) {
            return false;
        }

        if (diagnostico.getIdDiagnostico() <= 0) {
            return false;
        }

        if (diagnostico.getIdOrden() <= 0) {
            return false;
        }

        if (diagnostico.getDescripcionDiagnostico() == null || diagnostico.getDescripcionDiagnostico().trim().isEmpty()) {
            return false;
        }

        if (diagnostico.getIdTecnico() <= 0) diagnostico.setIdTecnico(2);
        if (diagnostico.getEstado() == null || diagnostico.getEstado().isBlank()) diagnostico.setEstado("Pendiente");
        return diagnosticoDAO.actualizar(diagnostico);
    }

    public boolean eliminarDiagnostico(int idDiagnostico) {
        if (idDiagnostico <= 0) {
            return false;
        }

        return diagnosticoDAO.eliminar(idDiagnostico);
    }
}