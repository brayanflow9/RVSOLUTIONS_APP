package vista;

import controlador.ClienteController;
import controlador.EquipoController;
import modelo.Cliente;
import modelo.Equipo;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FormEquipos extends JFrame {
    private static final Color FONDO_CARD = new Color(8, 26, 48);
    private static final Color FONDO_CARD_2 = new Color(12, 34, 60);
    private static final Color BORDE_DARK = new Color(38, 65, 94);
    private static final Color TEXTO = new Color(241, 245, 249);
    private static final Color TEXTO_2 = new Color(166, 187, 210);

    private final EquipoController equipoController = new EquipoController();
    private final ClienteController clienteController = new ClienteController();

    private JTextField txtIdEquipo;
    private JComboBox<ClienteItem> cboCliente;
    private JTextField txtTipoEquipo;
    private JTextField txtMarca;
    private JTextField txtModelo;
    private JTextField txtSerie;
    private JTextArea txtDescripcion;
    private JComboBox<String> cboEstado;

    private JTextField txtBuscar;
    private JComboBox<String> filtroTipo;
    private JComboBox<String> filtroEstado;
    private JLabel lblTotal;
    private JLabel lblRevision;
    private JLabel lblReparados;
    private JLabel lblEntregados;
    private JLabel lblResultados;

    private JTable tablaEquipos;
    private DefaultTableModel modeloTabla;
    private JPanel formCardModal;
    private List<Equipo> equiposCargados = new ArrayList<>();

    public FormEquipos() {
        setTitle("RV Solutions - Gestión de Equipos");
        setSize(1380, 820);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        EstilosRV.aplicarIconoVentana(this);
        inicializarComponentes();
        cargarClientes();
        listarEquipos();
    }

    private void inicializarComponentes() {
        BackgroundPanel fondo = new BackgroundPanel("assets/fondos/tecnico.png");
        fondo.setLayout(new BorderLayout());
        fondo.setBorder(new EmptyBorder(10, 12, 16, 12));

        DarkPanel principal = new DarkPanel(new BorderLayout(0, 16), FONDO_CARD, 24);
        principal.setBorder(new EmptyBorder(18, 20, 18, 20));
        fondo.add(principal, BorderLayout.CENTER);

        principal.add(crearCabeceraYKpis(), BorderLayout.NORTH);
        principal.add(crearZonaTabla(), BorderLayout.CENTER);
        prepararFormulario();
        setContentPane(fondo);
    }

    private JComponent crearCabeceraYKpis() {
        JPanel cont = new JPanel(new BorderLayout(18, 14));
        cont.setOpaque(false);

        JPanel titulo = new JPanel(new BorderLayout(12, 0));
        titulo.setOpaque(false);
        JLabel icono = new JLabel(EstilosRV.cargarIcono("assets/tecnico_equipos/monitor.png", 46, 46));
        icono.setPreferredSize(new Dimension(50, 50));
        titulo.add(icono, BorderLayout.WEST);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel t = new JLabel("Gestión de Equipos");
        t.setFont(new Font("Segoe UI", Font.BOLD, 23));
        t.setForeground(TEXTO);
        JLabel s = new JLabel("Controla el registro, seguimiento y estado de los equipos asignados al área técnica.");
        s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        s.setForeground(TEXTO_2);
        textos.add(t);
        textos.add(Box.createVerticalStrut(4));
        textos.add(s);
        titulo.add(textos, BorderLayout.CENTER);
        cont.add(titulo, BorderLayout.WEST);

        JPanel kpis = new JPanel(new GridLayout(1, 4, 10, 0));
        kpis.setOpaque(false);
        lblTotal = new JLabel("0");
        lblRevision = new JLabel("0");
        lblReparados = new JLabel("0");
        lblEntregados = new JLabel("0");
        kpis.add(crearKpi("assets/tecnico_equipos/monitor.png", "Total equipos", lblTotal, "Registrados"));
        kpis.add(crearKpi("assets/tecnico_equipos/revision.png", "En revisión", lblRevision, "En proceso"));
        kpis.add(crearKpi("assets/tecnico_equipos/reparado.png", "Reparados", lblReparados, "Listos para entrega"));
        kpis.add(crearKpi("assets/tecnico_equipos/entregado.png", "Entregados", lblEntregados, "Entregados al cliente"));
        cont.add(kpis, BorderLayout.CENTER);
        return cont;
    }

    private JPanel crearKpi(String ruta, String titulo, JLabel valor, String detalle) {
        DarkPanel card = new DarkPanel(new BorderLayout(12, 0), FONDO_CARD_2, 18);
        card.setBorder(new EmptyBorder(12, 14, 12, 14));
        JLabel icono = new JLabel(EstilosRV.cargarIcono(ruta, 48, 48));
        icono.setPreferredSize(new Dimension(52, 52));
        card.add(icono, BorderLayout.WEST);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel lt = new JLabel(titulo);
        lt.setForeground(new Color(220, 230, 241));
        lt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        valor.setForeground(Color.WHITE);
        valor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        JLabel ld = new JLabel(detalle);
        ld.setForeground(TEXTO_2);
        ld.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        textos.add(lt);
        textos.add(valor);
        textos.add(ld);
        card.add(textos, BorderLayout.CENTER);
        return card;
    }

    private JComponent crearZonaTabla() {
        JPanel cont = new JPanel(new BorderLayout(0, 12));
        cont.setOpaque(false);
        cont.add(crearBarraHerramientas(), BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"Código equipo", "DNI cliente", "Cliente", "Tipo", "Marca", "Modelo", "Serie", "Descripción", "Estado", "ID interno", "ID Cliente"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaEquipos = new JTable(modeloTabla);
        configurarTabla();
        JScrollPane scroll = new JScrollPane(tablaEquipos);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE_DARK));
        scroll.getViewport().setBackground(FONDO_CARD);
        scroll.setOpaque(false);
        cont.add(scroll, BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        lblResultados = new JLabel("Mostrando 0 equipos");
        lblResultados.setForeground(TEXTO_2);
        lblResultados.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pie.add(lblResultados, BorderLayout.WEST);
        JLabel pag = new JLabel("8 por página     ‹   1   2   3   ›");
        pag.setForeground(TEXTO_2);
        pag.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pie.add(pag, BorderLayout.EAST);
        cont.add(pie, BorderLayout.SOUTH);
        return cont;
    }

    private JPanel crearBarraHerramientas() {
        JPanel barra = new JPanel(new BorderLayout(12, 0));
        barra.setOpaque(false);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filtros.setOpaque(false);
        txtBuscar = crearCampoBusqueda();
        txtBuscar.setPreferredSize(new Dimension(280, 40));
        filtroTipo = crearFiltro(new String[]{"Todos los tipos"});
        filtroEstado = crearFiltro(new String[]{"Todos los estados", "Operativo", "En revisión", "Reparado", "Entregado"});
        filtroTipo.setPreferredSize(new Dimension(150, 40));
        filtroEstado.setPreferredSize(new Dimension(160, 40));
        filtros.add(txtBuscar);
        filtros.add(filtroTipo);
        filtros.add(filtroEstado);
        barra.add(filtros, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.setOpaque(false);
        JButton nuevo = crearBotonAccion("+ Nuevo", EstilosRV.ROJO, Color.WHITE, null);
        JButton editar = crearBotonAccion("Editar", FONDO_CARD_2, TEXTO, "assets/tecnico_equipos/editar.png");
        JButton eliminar = crearBotonAccion("Eliminar", FONDO_CARD_2, new Color(255, 92, 103), "assets/tecnico_equipos/eliminar.png");
        JButton exportar = crearBotonAccion("Exportar reporte", FONDO_CARD_2, TEXTO, null);
        nuevo.addActionListener(e -> { limpiarCampos(); mostrarFormularioFlotante(false); });
        editar.addActionListener(e -> mostrarFormularioFlotante(true));
        eliminar.addActionListener(e -> eliminarEquipo());
        exportar.addActionListener(e -> ReporteUtil.exportarTablaCSV(this, tablaEquipos, "reporte_equipos"));
        acciones.add(nuevo);
        acciones.add(editar);
        acciones.add(eliminar);
        acciones.add(exportar);
        barra.add(acciones, BorderLayout.EAST);

        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        };
        txtBuscar.getDocument().addDocumentListener(dl);
        filtroTipo.addActionListener(e -> aplicarFiltros());
        filtroEstado.addActionListener(e -> aplicarFiltros());
        return barra;
    }

    private JTextField crearCampoBusqueda() {
        JTextField campo = new JTextField();
        campo.setToolTipText("Buscar por ID, cliente, serie o modelo");
        campo.setForeground(TEXTO);
        campo.setCaretColor(Color.WHITE);
        campo.setBackground(FONDO_CARD_2);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        campo.setBorder(new RoundedBorder(BORDE_DARK, 16, new Insets(8, 12, 8, 12)));
        return campo;
    }

    private JComboBox<String> crearFiltro(String[] valores) {
        JComboBox<String> combo = new JComboBox<>(valores);
        combo.setBackground(FONDO_CARD_2);
        combo.setForeground(TEXTO);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setFocusable(false);
        combo.setBorder(new RoundedBorder(BORDE_DARK, 16, new Insets(6, 10, 6, 10)));
        ((JLabel) combo.getRenderer()).setBorder(new EmptyBorder(0, 4, 0, 4));
        return combo;
    }

    private JButton crearBotonAccion(String texto, Color fondo, Color fg, String icono) {
        RoundedButton b = new RoundedButton(texto, fondo, fg, BORDE_DARK, 18);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (icono != null) {
            b.setIcon(EstilosRV.cargarIcono(icono, 17, 17));
            b.setIconTextGap(7);
        }
        return b;
    }

    private void configurarTabla() {
        tablaEquipos.setRowHeight(41);
        tablaEquipos.setBackground(FONDO_CARD);
        tablaEquipos.setForeground(new Color(229, 236, 245));
        tablaEquipos.setSelectionBackground(new Color(25, 55, 84));
        tablaEquipos.setSelectionForeground(Color.WHITE);
        tablaEquipos.setGridColor(new Color(28, 53, 79));
        tablaEquipos.setShowVerticalLines(true);
        tablaEquipos.setShowHorizontalLines(true);
        tablaEquipos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaEquipos.getTableHeader().setBackground(new Color(10, 29, 51));
        tablaEquipos.getTableHeader().setForeground(Color.WHITE);
        tablaEquipos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaEquipos.getTableHeader().setReorderingAllowed(false);
        tablaEquipos.getColumnModel().getColumn(8).setCellRenderer(new EstadoRenderer());
        tablaEquipos.removeColumn(tablaEquipos.getColumnModel().getColumn(10));
        tablaEquipos.removeColumn(tablaEquipos.getColumnModel().getColumn(9));
        tablaEquipos.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaEquipos.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaEquipos.getColumnModel().getColumn(7).setPreferredWidth(260);
        tablaEquipos.getColumnModel().getColumn(8).setPreferredWidth(120);
        tablaEquipos.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int fila = tablaEquipos.rowAtPoint(e.getPoint());
                if (fila < 0) return;
                cargarSeleccion(fila);
                if (e.getClickCount() == 2) {
                    mostrarDetalle();
                }
            }
        });
    }

    private void prepararFormulario() {
        formCardModal = new JPanel(new GridBagLayout());
        formCardModal.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        txtIdEquipo = new JTextField(); txtIdEquipo.setEditable(false);
        cboCliente = new JComboBox<>();
        txtTipoEquipo = new JTextField(); txtMarca = new JTextField(); txtModelo = new JTextField(); txtSerie = new JTextField();
        txtDescripcion = new JTextArea(4, 20);
        cboEstado = new JComboBox<>(new String[]{"Operativo", "En revisión", "Reparado", "Entregado"});
        EstilosRV.estilizarCampo(txtIdEquipo); EstilosRV.estilizarCombo(cboCliente); EstilosRV.estilizarCampo(txtTipoEquipo);
        EstilosRV.estilizarCampo(txtMarca); EstilosRV.estilizarCampo(txtModelo); EstilosRV.estilizarCampo(txtSerie);
        EstilosRV.estilizarAreaTexto(txtDescripcion); EstilosRV.estilizarCombo(cboEstado);
        JScrollPane desc = new JScrollPane(txtDescripcion); EstilosRV.estilizarScroll(desc);

        String[] labels = {"ID:", "Cliente:", "Tipo de equipo:", "Marca:", "Modelo:", "Serie:", "Estado:"};
        JComponent[] comps = {txtIdEquipo, cboCliente, txtTipoEquipo, txtMarca, txtModelo, txtSerie, cboEstado};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = (i % 2) * 2; gbc.gridy = i / 2;
            JLabel l = new JLabel(labels[i]); EstilosRV.estilizarEtiqueta(l); formCardModal.add(l, gbc);
            gbc.gridx++; formCardModal.add(comps[i], gbc);
        }
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel ld = new JLabel("Descripción:"); EstilosRV.estilizarEtiqueta(ld); formCardModal.add(ld, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1;
        formCardModal.add(desc, gbc);
    }

    private void cargarClientes() {
        cboCliente.removeAllItems();
        for (Cliente c : clienteController.listarClientes()) {
            String nombre = c.getNombres() + " " + c.getApellidos();
            cboCliente.addItem(new ClienteItem(c.getIdCliente(), nombre));
        }
    }

    private void listarEquipos() {
        equiposCargados = equipoController.listarEquipos();
        actualizarFiltrosTipo();
        aplicarFiltros();
        actualizarKpis();
    }

    private void actualizarFiltrosTipo() {
        Object actual = filtroTipo.getSelectedItem();
        filtroTipo.removeAllItems();
        filtroTipo.addItem("Todos los tipos");
        equiposCargados.stream().map(Equipo::getTipoEquipo).filter(v -> v != null && !v.trim().isEmpty()).distinct().sorted().forEach(filtroTipo::addItem);
        if (actual != null) filtroTipo.setSelectedItem(actual);
    }

    private void aplicarFiltros() {
        if (modeloTabla == null) return;
        String q = txtBuscar == null ? "" : txtBuscar.getText().trim().toLowerCase();
        String tipo = seleccionado(filtroTipo);
        String estado = seleccionado(filtroEstado);
        modeloTabla.setRowCount(0);
        int mostrados = 0;
        for (Equipo e : equiposCargados) {
            String texto = (e.getIdEquipo() + " " + e.getClienteNombre() + " " + e.getTipoEquipo() + " " + e.getMarca() + " " + e.getModelo() + " " + e.getSerie() + " " + e.getDescripcion()).toLowerCase();
            boolean ok = (q.isEmpty() || texto.contains(q))
                    && (tipo.startsWith("Todos") || tipo.equalsIgnoreCase(nvl(e.getTipoEquipo())))
                    && (estado.startsWith("Todos") || estado.equalsIgnoreCase(nvl(e.getEstado())));
            if (!ok) continue;
            modeloTabla.addRow(new Object[]{e.getCodigoEquipo(), e.getClienteDni(), e.getClienteNombre(), e.getTipoEquipo(), e.getMarca(), e.getModelo(), e.getSerie(), e.getDescripcion(), e.getEstado(), e.getIdEquipo(), e.getIdCliente()});
            mostrados++;
        }
        lblResultados.setText("Mostrando " + mostrados + " de " + equiposCargados.size() + " equipos");
    }

    private String seleccionado(JComboBox<String> c) { return c == null || c.getSelectedItem() == null ? "Todos" : c.getSelectedItem().toString(); }
    private String nvl(String s) { return s == null ? "" : s; }

    private void actualizarKpis() {
        int revision = 0, reparados = 0, entregados = 0;
        for (Equipo e : equiposCargados) {
            String st = nvl(e.getEstado()).toLowerCase();
            if (st.contains("revisión") || st.contains("revision") || st.contains("proceso")) revision++;
            if (st.contains("reparado")) reparados++;
            if (st.contains("entregado")) entregados++;
        }
        lblTotal.setText(String.valueOf(equiposCargados.size()));
        lblRevision.setText(String.valueOf(revision));
        lblReparados.setText(String.valueOf(reparados));
        lblEntregados.setText(String.valueOf(entregados));
    }

    private void cargarSeleccion(int filaVista) {
        int fila = tablaEquipos.convertRowIndexToModel(filaVista);
        txtIdEquipo.setText(String.valueOf(modeloTabla.getValueAt(fila, 9)));
        txtTipoEquipo.setText(nvl(String.valueOf(modeloTabla.getValueAt(fila, 3))));
        txtMarca.setText(nvl(String.valueOf(modeloTabla.getValueAt(fila, 4))));
        txtModelo.setText(nvl(String.valueOf(modeloTabla.getValueAt(fila, 5))));
        txtSerie.setText(nvl(String.valueOf(modeloTabla.getValueAt(fila, 6))));
        txtDescripcion.setText(nvl(String.valueOf(modeloTabla.getValueAt(fila, 7))));
        cboEstado.setSelectedItem(modeloTabla.getValueAt(fila, 8));
        seleccionarClientePorId(Integer.parseInt(String.valueOf(modeloTabla.getValueAt(fila, 10))));
    }

    private void mostrarDetalle() {
        if (txtIdEquipo.getText().isEmpty()) return;
        JOptionPane.showMessageDialog(this,
                "Código: " + modeloTabla.getValueAt(tablaEquipos.convertRowIndexToModel(tablaEquipos.getSelectedRow()), 0) + "\nCliente: " + cboCliente.getSelectedItem() + "\nTipo: " + txtTipoEquipo.getText() + "\nMarca / modelo: " + txtMarca.getText() + " / " + txtModelo.getText() + "\nSerie: " + txtSerie.getText() + "\nEstado: " + cboEstado.getSelectedItem() + "\n\n" + txtDescripcion.getText(),
                "Detalle del equipo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void guardarEquipo() {
        if (!validarFormulario()) return;
        ClienteItem c = (ClienteItem) cboCliente.getSelectedItem();
        boolean ok = equipoController.registrarEquipo(c.idCliente, txtTipoEquipo.getText(), txtMarca.getText(), txtModelo.getText(), txtSerie.getText(), txtDescripcion.getText(), cboEstado.getSelectedItem().toString());
        JOptionPane.showMessageDialog(this, ok ? "Equipo registrado correctamente." : "No se pudo registrar el equipo.");
        if (ok) { limpiarCampos(); listarEquipos(); }
    }

    private void actualizarEquipo() {
        if (txtIdEquipo.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione un equipo para actualizar."); return; }
        if (!validarFormulario()) return;
        ClienteItem c = (ClienteItem) cboCliente.getSelectedItem();
        boolean ok = equipoController.actualizarEquipo(Integer.parseInt(txtIdEquipo.getText()), c.idCliente, txtTipoEquipo.getText(), txtMarca.getText(), txtModelo.getText(), txtSerie.getText(), txtDescripcion.getText(), cboEstado.getSelectedItem().toString());
        JOptionPane.showMessageDialog(this, ok ? "Equipo actualizado correctamente." : "No se pudo actualizar el equipo.");
        if (ok) { limpiarCampos(); listarEquipos(); }
    }

    private boolean validarFormulario() {
        if (cboCliente.getSelectedItem() == null) { JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente asociado al equipo."); return false; }
        if (txtTipoEquipo.getText().trim().isEmpty() || txtMarca.getText().trim().isEmpty() || txtModelo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete el tipo, la marca y el modelo del equipo."); return false;
        }
        return true;
    }

    private void eliminarEquipo() {
        if (txtIdEquipo.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione un equipo para eliminar."); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este equipo?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            boolean ok = equipoController.eliminarEquipo(Integer.parseInt(txtIdEquipo.getText()));
            JOptionPane.showMessageDialog(this, ok ? "Equipo eliminado correctamente." : "No se pudo eliminar el equipo.");
            if (ok) { limpiarCampos(); listarEquipos(); }
        }
    }

    private void limpiarCampos() {
        txtIdEquipo.setText(""); txtTipoEquipo.setText(""); txtMarca.setText(""); txtModelo.setText(""); txtSerie.setText(""); txtDescripcion.setText("");
        if (cboCliente.getItemCount() > 0) cboCliente.setSelectedIndex(0);
        cboEstado.setSelectedIndex(0);
        tablaEquipos.clearSelection();
    }

    private void seleccionarClientePorId(int id) {
        for (int i = 0; i < cboCliente.getItemCount(); i++) if (cboCliente.getItemAt(i).idCliente == id) { cboCliente.setSelectedIndex(i); return; }
    }

    private void mostrarFormularioFlotante(boolean editar) {
        if (editar && txtIdEquipo.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para actualizar."); return; }
        Window owner = SwingUtilities.getWindowAncestor(tablaEquipos);
        JDialog dialog = owner instanceof Frame
                ? new JDialog((Frame) owner, editar ? "Actualizar Equipo" : "Nuevo Equipo", true)
                : new JDialog((Frame) null, editar ? "Actualizar Equipo" : "Nuevo Equipo", true);
        dialog.setUndecorated(true);
        JPanel cont = new EstilosRV.RoundedPanel(new BorderLayout(0, 16), Color.WHITE, 28);
        cont.setBorder(new EmptyBorder(24, 26, 22, 26));
        JPanel cab = new JPanel(new BorderLayout()); cab.setOpaque(false);
        JLabel titulo = new JLabel(editar ? "Actualizar Equipo" : "Nuevo Equipo"); titulo.setFont(new Font("Segoe UI", Font.BOLD, 24)); titulo.setForeground(new Color(15, 23, 42));
        JButton cerrar = new JButton("×"); cerrar.setFont(new Font("Segoe UI", Font.PLAIN, 24)); cerrar.setBorderPainted(false); cerrar.setContentAreaFilled(false); cerrar.addActionListener(e -> dialog.dispose());
        cab.add(titulo, BorderLayout.WEST); cab.add(cerrar, BorderLayout.EAST); cont.add(cab, BorderLayout.NORTH); cont.add(formCardModal, BorderLayout.CENTER);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0)); acciones.setOpaque(false);
        JButton cancelar = EstilosRV.crearBotonRedondeado("Cancelar", false);
        JButton guardar = EstilosRV.crearBotonRedondeado(editar ? "Actualizar" : "Guardar", true);
        cancelar.addActionListener(e -> dialog.dispose());
        guardar.addActionListener(e -> { if (editar) actualizarEquipo(); else guardarEquipo(); dialog.dispose(); });
        acciones.add(cancelar); acciones.add(guardar); cont.add(acciones, BorderLayout.SOUTH);
        dialog.setContentPane(cont); dialog.setSize(760, 540); dialog.setLocationRelativeTo(this); dialog.setVisible(true);
    }

    private static class ClienteItem {
        private final int idCliente; private final String nombre;
        ClienteItem(int idCliente, String nombre) { this.idCliente = idCliente; this.nombre = nombre; }
        @Override public String toString() { return nombre; }
    }

    private static class DarkPanel extends JPanel {
        private final Color bg; private final int radius;
        DarkPanel(LayoutManager layout, Color bg, int radius) { super(layout); this.bg = bg; this.radius = radius; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 45)); g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 7, radius, radius);
            g2.setColor(bg); g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 6, radius, radius);
            g2.setColor(BORDE_DARK); g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 7, radius, radius);
            g2.dispose(); super.paintComponent(g);
        }
    }

    private static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;
        private final Insets insets;
        RoundedBorder(Color color, int radius, Insets insets) {
            this.color = color; this.radius = radius; this.insets = insets;
        }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return insets; }
        @Override public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = this.insets.top; insets.left = this.insets.left; insets.bottom = this.insets.bottom; insets.right = this.insets.right;
            return insets;
        }
    }

    private static class RoundedButton extends JButton {
        private final Color bg;
        private final Color fg;
        private final Color border;
        private final int radius;
        RoundedButton(String text, Color bg, Color fg, Color border, int radius) {
            super(text);
            this.bg = bg; this.fg = fg; this.border = border; this.radius = radius;
            setForeground(fg);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setMargin(new Insets(10, 15, 10, 15));
        }
        @Override public Insets getInsets() { return new Insets(10, 16, 10, 16); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color base = getModel().isRollover() ? bg.brighter() : bg;
            g2.setColor(base);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(border);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class EstadoRenderer extends DefaultTableCellRenderer {
        EstadoRenderer() { setHorizontalAlignment(SwingConstants.CENTER); }
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int col) {
            JPanel cont = new JPanel(new GridBagLayout());
            cont.setOpaque(true);
            cont.setBackground(selected ? table.getSelectionBackground() : FONDO_CARD);
            String v = value == null ? "" : value.toString();
            Color color = new Color(48, 83, 112);
            String low = v.toLowerCase();
            if (low.contains("reparado")) color = new Color(31, 167, 84);
            else if (low.contains("entregado")) color = new Color(23, 112, 205);
            else if (low.contains("revisión") || low.contains("revision")) color = new Color(224, 139, 0);
            JLabel chip = new JLabel(v, SwingConstants.CENTER);
            chip.setOpaque(true);
            chip.setFont(new Font("Segoe UI", Font.BOLD, 11));
            chip.setForeground(Color.WHITE);
            chip.setBackground(color);
            chip.setBorder(new EmptyBorder(6, 12, 6, 12));
            cont.add(chip);
            return cont;
        }
    }
}
