package controlador;

import dao.EquipoDAO;
import modelo.Equipo;

import java.util.List;

public class EquipoController {

    private EquipoDAO equipoDAO;

    public EquipoController() {
        equipoDAO = new EquipoDAO();
    }

    public boolean registrarEquipo(int idCliente, String tipoEquipo, String marca, String modelo, String serie, String descripcion, String estado) {
        if (idCliente <= 0) {
            return false;
        }

        if (tipoEquipo == null || tipoEquipo.trim().isEmpty()) {
            return false;
        }

        Equipo equipo = new Equipo(idCliente, tipoEquipo, marca, modelo, serie, descripcion, estado);
        return equipoDAO.registrar(equipo);
    }

    public List<Equipo> listarEquipos() {
        return equipoDAO.listar();
    }

    public boolean actualizarEquipo(int idEquipo, int idCliente, String tipoEquipo, String marca, String modelo, String serie, String descripcion, String estado) {
        if (idEquipo <= 0) {
            return false;
        }

        if (idCliente <= 0) {
            return false;
        }

        if (tipoEquipo == null || tipoEquipo.trim().isEmpty()) {
            return false;
        }

        Equipo equipo = new Equipo();
        equipo.setIdEquipo(idEquipo);
        equipo.setIdCliente(idCliente);
        equipo.setTipoEquipo(tipoEquipo);
        equipo.setMarca(marca);
        equipo.setModelo(modelo);
        equipo.setSerie(serie);
        equipo.setDescripcion(descripcion);
        equipo.setEstado(estado);

        return equipoDAO.actualizar(equipo);
    }

    public boolean eliminarEquipo(int idEquipo) {
        if (idEquipo <= 0) {
            return false;
        }

        return equipoDAO.eliminar(idEquipo);
    }

    public Equipo buscarEquipoPorId(int idEquipo) {
        if (idEquipo <= 0) {
            return null;
        }

        return equipoDAO.buscarPorId(idEquipo);
    }
}