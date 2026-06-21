package vista;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReporteUtil {

    public static void exportarTablaCSV(JFrame ventana, JTable tabla, String nombreSugerido) {
        if (tabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(ventana, "No hay datos para descargar.");
            return;
        }

        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Guardar reporte");
        selector.setSelectedFile(new File(nombreSugerido + ".csv"));

        int opcion = selector.showSaveDialog(ventana);
        if (opcion != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File archivo = selector.getSelectedFile();
        if (!archivo.getName().toLowerCase().endsWith(".csv")) {
            archivo = new File(archivo.getAbsolutePath() + ".csv");
        }

        try (FileWriter writer = new FileWriter(archivo, StandardCharsets.UTF_8)) {
            TableModel modelo = tabla.getModel();

            for (int col = 0; col < modelo.getColumnCount(); col++) {
                writer.append(escapar(modelo.getColumnName(col)));
                if (col < modelo.getColumnCount() - 1) writer.append(';');
            }
            writer.append('\n');

            for (int fila = 0; fila < modelo.getRowCount(); fila++) {
                for (int col = 0; col < modelo.getColumnCount(); col++) {
                    Object valor = modelo.getValueAt(fila, col);
                    writer.append(escapar(valor == null ? "" : valor.toString()));
                    if (col < modelo.getColumnCount() - 1) writer.append(';');
                }
                writer.append('\n');
            }

            JOptionPane.showMessageDialog(ventana, "Reporte descargado correctamente:\n" + archivo.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(ventana, "No se pudo descargar el reporte: " + e.getMessage());
        }
    }

    private static String escapar(String texto) {
        String limpio = texto.replace("\"", "\"\"");
        return "\"" + limpio + "\"";
    }
}
