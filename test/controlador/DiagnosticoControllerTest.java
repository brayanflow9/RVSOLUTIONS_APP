package controlador;

import modelo.Diagnostico;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiagnosticoControllerTest {

    @Test
    void noDebeRegistrarDiagnosticoSinOrden() {
        DiagnosticoController controller = new DiagnosticoController();
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setIdOrden(0);
        diagnostico.setDescripcionDiagnostico("Prueba de diagnóstico");
        diagnostico.setFechaDiagnostico("2026-06-03");

        assertFalse(controller.registrarDiagnostico(diagnostico));
    }

    @Test
    void noDebeRegistrarDiagnosticoVacio() {
        DiagnosticoController controller = new DiagnosticoController();
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setIdOrden(1);
        diagnostico.setDescripcionDiagnostico("   ");
        diagnostico.setFechaDiagnostico("2026-06-03");

        assertFalse(controller.registrarDiagnostico(diagnostico));
    }
}
