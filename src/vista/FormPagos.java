package vista;

import controlador.PagoController;
import modelo.Pago;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FormPagos extends JFrame {

    private JTextField txtIdPago;
    private JTextField txtIdOrden;
    private JTextField txtMonto;
    private JTextField txtFechaPago;
    private JComboBox<String> cboMetodoPago;
    private JComboBox<String> cboEstado;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnCerrar;
    private JButton btnDescargar;

    private JTable tablaPagos;
    private DefaultTableModel modeloTabla;
    private JPanel formCardModal;
    private PagoController pagoController;

    public FormPagos() {
        pagoController = new PagoController();
        setTitle("RV Solutions - Gestión de Pagos");
        setSize(1240, 740); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); setResizable(true); setExtendedState(JFrame.MAXIMIZED_BOTH); EstilosRV.aplicarIconoVentana(this);
        inicializarComponentes(); listarPagos();
    }

    private void inicializarComponentes() {
        BackgroundPanel fondo = new BackgroundPanel("assets/fondos/administrador.png"); fondo.setLayout(new BorderLayout(16,16)); fondo.setBorder(BorderFactory.createEmptyBorder(14,16,16,16));
        JPanel header = EstilosRV.crearPanelTarjeta(new BorderLayout()); header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(EstilosRV.BORDE_SUAVE), BorderFactory.createEmptyBorder(14,18,14,18)));
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT,12,0)); leftHeader.setOpaque(false); leftHeader.add(EstilosRV.crearImagenCabecera("assets/modulo_imagenes/img_pagos.png", 130, 64)); JPanel texts=new JPanel(new GridLayout(2,1)); texts.setOpaque(false); JLabel lblTitulo=new JLabel("Gestión de Pagos"); EstilosRV.estilizarTitulo(lblTitulo); JLabel lblSub=new JLabel("Control de montos, estados y reportes"); EstilosRV.estilizarSubtitulo(lblSub); texts.add(lblTitulo); texts.add(lblSub); leftHeader.add(texts); header.add(leftHeader, BorderLayout.WEST); fondo.add(header, BorderLayout.NORTH);
        JPanel body = new JPanel(new BorderLayout(16,16)); body.setOpaque(false); fondo.add(body, BorderLayout.CENTER); JPanel top = new JPanel(new BorderLayout(16,0)); top.setOpaque(false); body.add(top, BorderLayout.NORTH); top.setVisible(false);
        formCardModal = EstilosRV.crearPanelTarjeta(new GridBagLayout()); JPanel formCard = formCardModal; top.add(formCard, BorderLayout.CENTER); GridBagConstraints gbc=new GridBagConstraints(); gbc.insets=new Insets(8,8,8,8); gbc.fill=GridBagConstraints.HORIZONTAL; gbc.weightx=1;
        txtIdPago=new JTextField(); txtIdPago.setEditable(false); txtIdOrden=new JTextField(); txtMonto=new JTextField(); txtFechaPago=new JTextField(LocalDate.now().toString()); cboMetodoPago=new JComboBox<>(new String[]{"Efectivo","Yape","Plin","Transferencia","Tarjeta"}); cboEstado=new JComboBox<>(new String[]{"Pendiente","Pagado"});
        for(JTextField c:new JTextField[]{txtIdPago,txtIdOrden,txtMonto,txtFechaPago}) EstilosRV.estilizarCampo(c); EstilosRV.estilizarCombo(cboMetodoPago); EstilosRV.estilizarCombo(cboEstado);
        String[] ls={"ID Pago:","ID Orden:","Monto:","Fecha:","Método:","Estado:"}; JComponent[] cs={txtIdPago,txtIdOrden,txtMonto,txtFechaPago,cboMetodoPago,cboEstado}; for(int i=0;i<ls.length;i++){ gbc.gridx=(i%2)*2; gbc.gridy=i/2; JLabel l=new JLabel(ls[i]); EstilosRV.estilizarEtiqueta(l); formCard.add(l,gbc); gbc.gridx=(i%2)*2+1; formCard.add(cs[i],gbc); }
        JPanel actionCard = EstilosRV.crearPanelTarjeta(new GridLayout(6,1,0,12)); actionCard.setPreferredSize(new Dimension(220,10)); top.add(actionCard, BorderLayout.EAST); btnNuevo=new JButton("Nuevo"); btnGuardar=new JButton("Guardar"); btnActualizar=new JButton("Actualizar"); btnEliminar=new JButton("Eliminar"); btnDescargar=new JButton("Descargar reporte"); btnCerrar=new JButton("Cerrar"); for(JButton b:new JButton[]{btnNuevo,btnGuardar,btnActualizar,btnEliminar,btnDescargar}){EstilosRV.estilizarBoton(b); actionCard.add(b);} EstilosRV.estilizarBotonOscuro(btnCerrar); actionCard.add(btnCerrar);
        JPanel tableCard=EstilosRV.crearPanelTarjeta(new BorderLayout(0,12)); body.add(tableCard, BorderLayout.CENTER); JLabel lblTabla=new JLabel("Listado de pagos"); lblTabla.setFont(new Font("Arial", Font.BOLD,20)); lblTabla.setForeground(Color.WHITE); tableCard.add(lblTabla, BorderLayout.NORTH); tableCard.add(EstilosRV.crearCabeceraTablaModulo("Lista de Pagos", btnNuevo, btnActualizar, btnEliminar, btnDescargar), BorderLayout.NORTH);
        modeloTabla = new DefaultTableModel(); modeloTabla.addColumn("Código Pago"); modeloTabla.addColumn("Código Orden"); modeloTabla.addColumn("Orden"); modeloTabla.addColumn("Monto"); modeloTabla.addColumn("Método"); modeloTabla.addColumn("Fecha"); modeloTabla.addColumn("Estado"); modeloTabla.addColumn("ID Pago"); modeloTabla.addColumn("ID Orden"); tablaPagos=new JTable(modeloTabla); JScrollPane scrollTabla=new JScrollPane(tablaPagos); EstilosRV.estilizarScroll(scrollTabla); EstilosRV.estilizarTabla(tablaPagos); tablaPagos.getColumnModel().getColumn(6).setCellRenderer(new EstadoBadgeRenderer()); tablaPagos.removeColumn(tablaPagos.getColumnModel().getColumn(8)); tablaPagos.removeColumn(tablaPagos.getColumnModel().getColumn(7)); tablaPagos.setRowHeight(42); tableCard.add(scrollTabla, BorderLayout.CENTER);
        btnNuevo.addActionListener(e -> { limpiarCampos(); mostrarFormularioFlotante(false); }); btnGuardar.addActionListener(e -> guardarPago()); btnActualizar.addActionListener(e -> mostrarFormularioFlotante(true)); btnEliminar.addActionListener(e -> eliminarPago()); btnCerrar.addActionListener(e -> dispose()); btnDescargar.addActionListener(e -> ReporteUtil.exportarTablaCSV(this, tablaPagos, "reporte_pagos")); tablaPagos.getSelectionModel().addListSelectionListener(e -> seleccionarPago());
        setContentPane(fondo);
    }

    private void guardarPago() {
        try {
            int idOrden = Integer.parseInt(txtIdOrden.getText());
            double monto = Double.parseDouble(txtMonto.getText());

            boolean resultado = pagoController.registrarPago(
                    idOrden,
                    monto,
                    cboMetodoPago.getSelectedItem().toString(),
                    txtFechaPago.getText(),
                    cboEstado.getSelectedItem().toString()
            );

            if (resultado) {
                JOptionPane.showMessageDialog(this, "Pago registrado correctamente.");
                limpiarCampos();
                listarPagos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el pago. Verifique los datos.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Orden y Monto deben ser valores numéricos.");
        }
    }

    private void actualizarPago() {
        if (txtIdPago.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un pago para actualizar.");
            return;
        }

        try {
            int idPago = Integer.parseInt(txtIdPago.getText());
            int idOrden = Integer.parseInt(txtIdOrden.getText());
            double monto = Double.parseDouble(txtMonto.getText());

            boolean resultado = pagoController.actualizarPago(
                    idPago,
                    idOrden,
                    monto,
                    cboMetodoPago.getSelectedItem().toString(),
                    txtFechaPago.getText(),
                    cboEstado.getSelectedItem().toString()
            );

            if (resultado) {
                JOptionPane.showMessageDialog(this, "Pago actualizado correctamente.");
                limpiarCampos();
                listarPagos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el pago.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Orden y Monto deben ser valores numéricos.");
        }
    }

    private void eliminarPago() {
        if (txtIdPago.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un pago para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este pago?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            int idPago = Integer.parseInt(txtIdPago.getText());

            boolean resultado = pagoController.eliminarPago(idPago);

            if (resultado) {
                JOptionPane.showMessageDialog(this, "Pago eliminado correctamente.");
                limpiarCampos();
                listarPagos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el pago.");
            }
        }
    }

    private void listarPagos() {
        modeloTabla.setRowCount(0);

        List<Pago> pagos = pagoController.listarPagos();

        for (Pago pago : pagos) {
            Object[] fila = {
                    pago.getCodigoPago(),
                    pago.getCodigoOrden(),
                    pago.getOrdenDescripcion(),
                    pago.getMonto(),
                    pago.getMetodoPago(),
                    pago.getFechaPago(),
                    pago.getEstado(),
                    pago.getIdPago(),
                    pago.getIdOrden()
            };

            modeloTabla.addRow(fila);
        }
    }

    private void seleccionarPago() {
        int fila = tablaPagos.getSelectedRow();

        if (fila >= 0) {
            int fm = tablaPagos.convertRowIndexToModel(fila);
            txtIdPago.setText(modeloTabla.getValueAt(fm, 7).toString());
            txtIdOrden.setText(modeloTabla.getValueAt(fm, 8).toString());
            txtMonto.setText(tablaPagos.getValueAt(fila, 3).toString());
            cboMetodoPago.setSelectedItem(tablaPagos.getValueAt(fila, 4).toString());
            txtFechaPago.setText(tablaPagos.getValueAt(fila, 5).toString());
            cboEstado.setSelectedItem(tablaPagos.getValueAt(fila, 6).toString());
        }
    }

    private void limpiarCampos() {
        txtIdPago.setText("");
        txtIdOrden.setText("");
        txtMonto.setText("");
        txtFechaPago.setText(LocalDate.now().toString());
        cboMetodoPago.setSelectedIndex(0);
        cboEstado.setSelectedIndex(0);
        tablaPagos.clearSelection();
    }

    private void mostrarFormularioFlotante(boolean editar) {
        if (editar && txtIdPago.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para actualizar.");
            return;
        }
        JDialog dialog = new JDialog(this, editar ? "Actualizar Pago" : "Nuevo Pago", true);
        dialog.setUndecorated(true);
        JPanel cont = new EstilosRV.RoundedPanel(new BorderLayout(0, 16), Color.WHITE, 28);
        cont.setBorder(BorderFactory.createEmptyBorder(24, 26, 22, 26));
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        JLabel titulo = new JLabel(editar ? "Actualizar Pago" : "Nuevo Pago");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(15, 23, 42));
        cabecera.add(titulo, BorderLayout.WEST);
        JButton cerrar = new JButton("×");
        cerrar.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        cerrar.setFocusPainted(false);
        cerrar.setBorderPainted(false);
        cerrar.setContentAreaFilled(false);
        cerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cerrar.addActionListener(e -> dialog.dispose());
        cabecera.add(cerrar, BorderLayout.EAST);
        cont.add(cabecera, BorderLayout.NORTH);
        cont.add(formCardModal, BorderLayout.CENTER);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        acciones.setOpaque(false);
        JButton cancelar = EstilosRV.crearBotonRedondeado("Cancelar", false);
        JButton guardar = EstilosRV.crearBotonRedondeado(editar ? "Actualizar" : "Guardar", true);
        cancelar.addActionListener(e -> dialog.dispose());
        guardar.addActionListener(e -> { if (editar) actualizarPago(); else guardarPago(); dialog.dispose(); });
        acciones.add(cancelar);
        acciones.add(guardar);
        cont.add(acciones, BorderLayout.SOUTH);
        dialog.setContentPane(cont);
        dialog.setSize(760, 540);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }



    private class EstadoBadgeRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int col) {
            String v = value == null ? "" : value.toString();
            Color[] colores = EstilosRV.coloresEstado(v);
            Color cellBg = selected ? table.getSelectionBackground() : Color.WHITE;
            return EstilosRV.crearBadgeEstado(v, colores[0], colores[1], cellBg);
        }
    }

}
