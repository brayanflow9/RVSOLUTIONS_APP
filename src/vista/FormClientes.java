package vista;

import controlador.ClienteController;
import modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FormClientes extends JFrame {

    private JTextField txtId;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtDni;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JComboBox<String> cboEstado;

    private JButton btnNuevo;
    private JButton btnDescargar;
    private JTextField txtBuscar;
    private JComboBox<String> cboFiltroEstado;

    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private ClienteController clienteController;
    private final ImageIcon iconEditar = EstilosRV.cargarIcono("assets/ui_icons/editar_moderno.png", 22, 22);
    private final ImageIcon iconEstado = EstilosRV.cargarIcono("assets/ui_icons/check_verde.png", 22, 22);
    private final ImageIcon iconEliminar = EstilosRV.cargarIcono("assets/ui_icons/eliminar_moderno.png", 22, 22);

    public FormClientes() {
        clienteController = new ClienteController();
        setTitle("RV Solutions - Gestión de Clientes");
        setSize(1280, 760);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        EstilosRV.aplicarIconoVentana(this);
        inicializarComponentes();
        listarClientes();
    }

    private void inicializarComponentes() {
        txtId = new JTextField();
        txtNombres = new JTextField();
        txtApellidos = new JTextField();
        txtDni = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtDireccion = new JTextField();
        cboEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        BackgroundPanel fondo = new BackgroundPanel("assets/fondos/administrador.png");
        fondo.setLayout(new BorderLayout(16, 16));
        fondo.setBorder(BorderFactory.createEmptyBorder(14, 16, 16, 16));

        JPanel header = crearCabeceraClientes();
        fondo.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(16, 16));
        body.setOpaque(false);
        fondo.add(body, BorderLayout.CENTER);

        JPanel tableCard = EstilosRV.crearPanelTarjeta(new BorderLayout(0, 18));
        tableCard.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        body.add(tableCard, BorderLayout.CENTER);

        tableCard.add(crearBarraTabla(), BorderLayout.NORTH);
        crearTablaClientes();
        JScrollPane scroll = new JScrollPane(tablaClientes);
        EstilosRV.estilizarScroll(scroll);
        tableCard.add(scroll, BorderLayout.CENTER);
        tableCard.add(crearPieTabla(), BorderLayout.SOUTH);

        btnNuevo.addActionListener(e -> mostrarFormularioCliente(false));
        btnDescargar.addActionListener(e -> ReporteUtil.exportarTablaCSV(this, tablaClientes, "reporte_clientes"));
        txtBuscar.addActionListener(e -> listarClientes());
        cboFiltroEstado.addActionListener(e -> listarClientes());

        setContentPane(fondo);
    }

    private JPanel crearCabeceraClientes() {
        JPanel wrapper = new JPanel(new BorderLayout(16, 0));
        wrapper.setOpaque(false);

        JPanel banner = EstilosRV.crearPanelTarjeta(new BorderLayout(18, 0));
        banner.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        banner.setPreferredSize(new Dimension(520, 118));

        JLabel icono = new JLabel();
        icono.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon imgClientes = EstilosRV.cargarIcono("assets/banners/clientes_gestion.png", 82, 82);
        if (imgClientes == null) imgClientes = EstilosRV.cargarIcono("assets/kpi_icons/clientes.png", 70, 70);
        if (imgClientes != null) icono.setIcon(imgClientes);
        icono.setPreferredSize(new Dimension(94, 82));
        banner.add(icono, BorderLayout.WEST);

        JPanel texto = new JPanel();
        texto.setOpaque(false);
        texto.setLayout(new BoxLayout(texto, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("Gestión de Clientes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(new Color(15, 23, 42));
        JLabel subtitulo = new JLabel("Registro, consulta y exportación de clientes.");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(100, 116, 139));
        texto.add(Box.createVerticalGlue()); texto.add(titulo); texto.add(Box.createVerticalStrut(8)); texto.add(subtitulo); texto.add(Box.createVerticalGlue());
        banner.add(texto, BorderLayout.CENTER);

        ImageIcon grupo = EstilosRV.cargarIcono("assets/ui_icons/grupo_clientes.png", 38, 38);
        JLabel deco = new JLabel(grupo);
        deco.setPreferredSize(new Dimension(54, 54));
        banner.add(deco, BorderLayout.EAST);

        wrapper.add(banner, BorderLayout.CENTER);

        JPanel kpis = new JPanel(new GridLayout(1, 3, 14, 0));
        kpis.setOpaque(false);
        kpis.add(crearKpi("assets/ui_icons/usuario_rosa.png", "Total clientes", "3", "Clientes registrados"));
        kpis.add(crearKpi("assets/ui_icons/check_verde.png", "Activos", "3", "Clientes activos"));
        kpis.add(crearKpi("assets/ui_icons/calendario_lila.png", "Nuevos este mes", "1", "Clientes nuevos"));
        wrapper.add(kpis, BorderLayout.EAST);
        return wrapper;
    }

    private JPanel crearKpi(String ruta, String titulo, String valor, String subtitulo) {
        JPanel card = EstilosRV.crearPanelTarjeta(new BorderLayout(12, 0));
        card.setPreferredSize(new Dimension(210, 118));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JLabel icono = new JLabel();
        ImageIcon img = EstilosRV.cargarIcono(ruta, 48, 48);
        if (img != null) icono.setIcon(img);
        icono.setPreferredSize(new Dimension(54, 54));
        card.add(icono, BorderLayout.WEST);
        JPanel txt = new JPanel(); txt.setOpaque(false); txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        JLabel l1 = new JLabel(titulo); l1.setForeground(new Color(100,116,139)); l1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel l2 = new JLabel(valor); l2.setForeground(new Color(15,23,42)); l2.setFont(new Font("Segoe UI", Font.BOLD, 26));
        JLabel l3 = new JLabel(subtitulo); l3.setForeground(new Color(100,116,139)); l3.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txt.add(Box.createVerticalGlue()); txt.add(l1); txt.add(l2); txt.add(l3); txt.add(Box.createVerticalGlue());
        card.add(txt, BorderLayout.CENTER);
        return card;
    }

    private JPanel crearBarraTabla() {
        JPanel cont = new JPanel(new BorderLayout(12, 14));
        cont.setOpaque(false);
        JPanel top = new JPanel(new BorderLayout()); top.setOpaque(false);
        JPanel title = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); title.setOpaque(false);
        ImageIcon listIcon = EstilosRV.cargarIcono("assets/ui_icons/grupo_clientes.png", 26, 26);
        JLabel ico = new JLabel(listIcon);
        JLabel lbl = new JLabel("Lista de Clientes");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(new Color(15, 23, 42));
        title.add(ico); title.add(lbl);
        top.add(title, BorderLayout.WEST);
        btnNuevo = new JButton("+  Nuevo cliente");
        estilizarBotonRedondeado(btnNuevo, true);
        btnNuevo.setPreferredSize(new Dimension(180, 46));
        top.add(btnNuevo, BorderLayout.EAST);
        cont.add(top, BorderLayout.NORTH);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0)); filtros.setOpaque(false);
        txtBuscar = new JTextField("Buscar en la tabla...");
        EstilosRV.estilizarCampo(txtBuscar);
        txtBuscar.setPreferredSize(new Dimension(280, 42));
        cboFiltroEstado = new JComboBox<>(new String[]{"Estado: Todos", "Activo", "Inactivo"});
        EstilosRV.estilizarCombo(cboFiltroEstado);
        cboFiltroEstado.setPreferredSize(new Dimension(160, 42));
        JButton limpiar = new JButton("Limpiar filtros"); estilizarBotonRedondeado(limpiar, false);
        limpiar.addActionListener(e -> { txtBuscar.setText(""); cboFiltroEstado.setSelectedIndex(0); listarClientes(); });
        btnDescargar = new JButton("Descargar reporte"); estilizarBotonRedondeado(btnDescargar, false);
        filtros.add(txtBuscar); filtros.add(cboFiltroEstado); filtros.add(limpiar); filtros.add(Box.createHorizontalStrut(180)); filtros.add(btnDescargar);
        cont.add(filtros, BorderLayout.CENTER);
        return cont;
    }

    private JPanel crearPieTabla() {
        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        JLabel lbl = new JLabel("Mostrando 1 a 3 de 3 clientes");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(100,116,139));
        pie.add(lbl, BorderLayout.WEST);
        JPanel pag = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); pag.setOpaque(false);
        JButton prev = new JButton("‹"); JButton page = new JButton("1"); JButton next = new JButton("›");
        estilizarBotonRedondeado(prev,false); estilizarBotonRedondeado(page,true); estilizarBotonRedondeado(next,false);
        JComboBox<String> perPage = new JComboBox<>(new String[]{"10 por página"}); EstilosRV.estilizarCombo(perPage);
        pag.add(prev); pag.add(page); pag.add(next); pag.add(perPage);
        pie.add(pag, BorderLayout.EAST);
        return pie;
    }

    private void crearTablaClientes() {
        modeloTabla = new DefaultTableModel() {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        modeloTabla.addColumn("ID Cliente (DNI)");
        modeloTabla.addColumn("Nombres");
        modeloTabla.addColumn("Apellidos");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Correo");
        modeloTabla.addColumn("Dirección");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Acciones");
        modeloTabla.addColumn("ID interno");
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.removeColumn(tablaClientes.getColumnModel().getColumn(8));
        EstilosRV.estilizarTabla(tablaClientes);
        tablaClientes.setRowHeight(70);
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaClientes.getColumnModel().getColumn(6).setCellRenderer(new EstadoRenderer());
        tablaClientes.getColumnModel().getColumn(7).setCellRenderer(new AccionesRenderer());
        tablaClientes.getColumnModel().getColumn(6).setPreferredWidth(120);
        tablaClientes.getColumnModel().getColumn(7).setPreferredWidth(150);
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int filaVista = tablaClientes.rowAtPoint(e.getPoint());
                int col = tablaClientes.columnAtPoint(e.getPoint());
                if (filaVista >= 0 && col == 7) manejarClickAccion(filaVista, e.getX());
            }
        });
    }

    private void manejarClickAccion(int filaVista, int xAbsoluto) {
        Rectangle rect = tablaClientes.getCellRect(filaVista, 7, true);
        int relativo = xAbsoluto - rect.x;
        int ancho = rect.width;
        int segmento = Math.max(1, ancho / 3);
        int accion = Math.min(2, Math.max(0, relativo / segmento));
        int fila = tablaClientes.convertRowIndexToModel(filaVista);
        seleccionarFila(fila);
        if (accion == 0) {
            mostrarFormularioCliente(true);
        } else if (accion == 1) {
            cambiarEstadoCliente();
        } else {
            eliminarCliente();
        }
    }

    private void seleccionarFila(int filaModelo) {
        txtDni.setText(modeloTabla.getValueAt(filaModelo, 0).toString());
        txtNombres.setText(modeloTabla.getValueAt(filaModelo, 1).toString());
        txtApellidos.setText(modeloTabla.getValueAt(filaModelo, 2).toString());
        txtTelefono.setText(modeloTabla.getValueAt(filaModelo, 3).toString());
        txtCorreo.setText(modeloTabla.getValueAt(filaModelo, 4).toString());
        txtDireccion.setText(modeloTabla.getValueAt(filaModelo, 5).toString());
        cboEstado.setSelectedItem(modeloTabla.getValueAt(filaModelo, 6).toString());
        txtId.setText(modeloTabla.getValueAt(filaModelo, 8).toString());
    }

    private void mostrarFormularioCliente(boolean editar) {
        String idPrevio = txtId == null ? "" : txtId.getText();
        String nombresPrevio = txtNombres == null ? "" : txtNombres.getText();
        String apellidosPrevio = txtApellidos == null ? "" : txtApellidos.getText();
        String dniPrevio = txtDni == null ? "" : txtDni.getText();
        String telefonoPrevio = txtTelefono == null ? "" : txtTelefono.getText();
        String correoPrevio = txtCorreo == null ? "" : txtCorreo.getText();
        String direccionPrevio = txtDireccion == null ? "" : txtDireccion.getText();
        String estadoPrevio = cboEstado == null || cboEstado.getSelectedItem() == null ? "Activo" : cboEstado.getSelectedItem().toString();
        if (!editar) limpiarCampos();
        if (editar && idPrevio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente desde la columna Acciones.");
            return;
        }
        JDialog dialog = new JDialog(this, editar ? "Actualizar cliente" : "Nuevo cliente", true);
        dialog.setUndecorated(true);
        JPanel cont = new EstilosRV.RoundedPanel(new BorderLayout(0, 18), Color.WHITE, 28);
        cont.setBorder(BorderFactory.createEmptyBorder(26, 28, 24, 28));

        JPanel title = new JPanel(new BorderLayout()); title.setOpaque(false);
        JPanel tleft = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0)); tleft.setOpaque(false);
        ImageIcon icon = EstilosRV.cargarIcono("assets/ui_icons/usuario_agregar.png", 36, 36);
        tleft.add(new JLabel(icon));
        JLabel lbl = new JLabel(editar ? "Actualizar cliente" : "Nuevo cliente");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24)); lbl.setForeground(new Color(15,23,42));
        tleft.add(lbl); title.add(tleft, BorderLayout.WEST);
        JButton cerrar = new JButton("×"); cerrar.setFont(new Font("Segoe UI", Font.PLAIN, 24)); cerrar.setBorderPainted(false); cerrar.setContentAreaFilled(false); cerrar.setFocusPainted(false); cerrar.setCursor(new Cursor(Cursor.HAND_CURSOR)); cerrar.addActionListener(e -> dialog.dispose());
        title.add(cerrar, BorderLayout.EAST);
        cont.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout()); form.setOpaque(false);
        txtId = new JTextField(); txtId.setVisible(false);
        txtNombres = crearCampo(); txtApellidos = crearCampo(); txtDni = crearCampo(); txtTelefono = crearCampo(); txtCorreo = crearCampo(); txtDireccion = crearCampo(); cboEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"}); EstilosRV.estilizarCombo(cboEstado);
        if (editar) {
            txtId.setText(idPrevio);
            txtNombres.setText(nombresPrevio);
            txtApellidos.setText(apellidosPrevio);
            txtDni.setText(dniPrevio);
            txtTelefono.setText(telefonoPrevio);
            txtCorreo.setText(correoPrevio);
            txtDireccion.setText(direccionPrevio);
            cboEstado.setSelectedItem(estadoPrevio);
        }
        agregarCampo(form, 0, 0, "Nombres *", txtNombres); agregarCampo(form, 1, 0, "Apellidos *", txtApellidos);
        agregarCampo(form, 0, 1, "DNI / ID *", txtDni); agregarCampo(form, 1, 1, "Teléfono *", txtTelefono);
        agregarCampo(form, 0, 2, "Correo *", txtCorreo); agregarCampo(form, 1, 2, "Dirección *", txtDireccion);
        GridBagConstraints gbc = new GridBagConstraints(); gbc.gridx=0; gbc.gridy=3; gbc.gridwidth=2; gbc.weightx=1; gbc.fill=GridBagConstraints.HORIZONTAL; gbc.insets=new Insets(8,0,8,0);
        JPanel pEstado = new JPanel(new BorderLayout(0, 6)); pEstado.setOpaque(false); JLabel le = new JLabel("Estado *"); EstilosRV.estilizarEtiqueta(le); pEstado.add(le, BorderLayout.NORTH); pEstado.add(cboEstado, BorderLayout.CENTER); form.add(pEstado, gbc);
        cont.add(form, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0)); acciones.setOpaque(false);
        JButton cancelar = new JButton("Cancelar"); estilizarBotonRedondeado(cancelar, false); cancelar.setPreferredSize(new Dimension(130, 44)); cancelar.addActionListener(e -> dialog.dispose());
        JButton guardar = new JButton(editar ? "Actualizar" : "Guardar"); estilizarBotonRedondeado(guardar, true); guardar.setPreferredSize(new Dimension(130, 44));
        guardar.addActionListener(e -> { if (editar) actualizarCliente(); else guardarCliente(); dialog.dispose(); });
        acciones.add(cancelar); acciones.add(guardar); cont.add(acciones, BorderLayout.SOUTH);

        dialog.setContentPane(cont);
        dialog.setSize(640, 520);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JTextField crearCampo() { JTextField c = new JTextField(); EstilosRV.estilizarCampo(c); c.setPreferredSize(new Dimension(260, 42)); return c; }

    private void agregarCampo(JPanel form, int col, int row, String label, JComponent comp) {
        GridBagConstraints gbc = new GridBagConstraints(); gbc.gridx=col; gbc.gridy=row; gbc.weightx=1; gbc.fill=GridBagConstraints.HORIZONTAL; gbc.insets=new Insets(8, col==0?0:16, 8, col==0?16:0);
        JPanel p = new JPanel(new BorderLayout(0,6)); p.setOpaque(false); JLabel l = new JLabel(label); EstilosRV.estilizarEtiqueta(l); p.add(l, BorderLayout.NORTH); p.add(comp, BorderLayout.CENTER); form.add(p, gbc);
    }

    private void estilizarBotonRedondeado(JButton b, boolean principal) {
        b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR)); b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        if (principal) { b.setBackground(EstilosRV.ROJO); b.setForeground(Color.WHITE); }
        else { b.setBackground(Color.WHITE); b.setForeground(new Color(15,23,42)); b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(EstilosRV.BORDE_SUAVE), BorderFactory.createEmptyBorder(10,18,10,18))); }
    }

    private void guardarCliente() {
        boolean resultado = clienteController.registrarCliente(txtNombres.getText(), txtApellidos.getText(), txtDni.getText(), txtTelefono.getText(), txtCorreo.getText(), txtDireccion.getText(), cboEstado.getSelectedItem().toString());
        if (resultado) { JOptionPane.showMessageDialog(this, "Cliente registrado correctamente."); listarClientes(); }
        else JOptionPane.showMessageDialog(this, "No se pudo registrar el cliente. Verifique los datos.");
    }

    private void actualizarCliente() {
        if (txtId.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione un cliente para actualizar."); return; }
        int idCliente = Integer.parseInt(txtId.getText());
        boolean resultado = clienteController.actualizarCliente(idCliente, txtNombres.getText(), txtApellidos.getText(), txtDni.getText(), txtTelefono.getText(), txtCorreo.getText(), txtDireccion.getText(), cboEstado.getSelectedItem().toString());
        if (resultado) { JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente."); listarClientes(); }
        else JOptionPane.showMessageDialog(this, "No se pudo actualizar el cliente.");
    }

    private void cambiarEstadoCliente() {
        if (txtId.getText().isEmpty()) return;
        cboEstado.setSelectedItem("Activo".equals(cboEstado.getSelectedItem()) ? "Inactivo" : "Activo");
        actualizarCliente();
    }

    private void eliminarCliente() {
        if (txtId.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar."); return; }
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este cliente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean resultado = clienteController.eliminarCliente(Integer.parseInt(txtId.getText()));
            if (resultado) { JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente."); listarClientes(); }
            else JOptionPane.showMessageDialog(this, "No se pudo eliminar el cliente.");
        }
    }

    private void listarClientes() {
        modeloTabla.setRowCount(0);
        String filtro = txtBuscar == null ? "" : txtBuscar.getText().trim().toLowerCase();
        if ("buscar en la tabla...".equals(filtro)) filtro = "";
        String estadoFiltro = cboFiltroEstado == null ? "Estado: Todos" : cboFiltroEstado.getSelectedItem().toString();
        List<Cliente> lista = clienteController.listarClientes();
        int total = 0;
        for (Cliente cliente : lista) {
            String data = (cliente.getDni()+" "+cliente.getNombres()+" "+cliente.getApellidos()+" "+cliente.getTelefono()+" "+cliente.getCorreo()+" "+cliente.getDireccion()).toLowerCase();
            if (!filtro.isEmpty() && !data.contains(filtro)) continue;
            if (!"Estado: Todos".equals(estadoFiltro) && !estadoFiltro.equals(cliente.getEstado())) continue;
            total++;
            modeloTabla.addRow(new Object[]{cliente.getDni(), cliente.getNombres(), cliente.getApellidos(), cliente.getTelefono(), cliente.getCorreo(), cliente.getDireccion(), cliente.getEstado(), "ACCIONES", cliente.getIdCliente()});
        }
    }

    private void limpiarCampos() {
        if (txtId != null) txtId.setText("");
        if (txtNombres != null) txtNombres.setText("");
        if (txtApellidos != null) txtApellidos.setText("");
        if (txtDni != null) txtDni.setText("");
        if (txtTelefono != null) txtTelefono.setText("");
        if (txtCorreo != null) txtCorreo.setText("");
        if (txtDireccion != null) txtDireccion.setText("");
        if (cboEstado != null) cboEstado.setSelectedIndex(0);
        if (tablaClientes != null) tablaClientes.clearSelection();
    }

    private class EstadoRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int col) {
            String v = value == null ? "" : value.toString();
            Color[] colores = EstilosRV.coloresEstado(v);
            Color cellBg = selected ? table.getSelectionBackground() : Color.WHITE;
            return EstilosRV.crearBadgeEstado(v, colores[0], colores[1], cellBg);
        }
    }

    private JPanel crearPanelAccionIcono(ImageIcon icono) {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        JPanel btn = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.setColor(new Color(226, 232, 240));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(42, 42));
        JLabel lbl = new JLabel(icono);
        btn.add(lbl);
        wrap.add(btn);
        return wrap;
    }

    private class AccionesRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int col) {
            JPanel cont = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
            cont.setOpaque(true);
            cont.setBackground(selected ? table.getSelectionBackground() : Color.WHITE);
            cont.add(crearPanelAccionIcono(iconEditar));
            cont.add(crearPanelAccionIcono(iconEstado));
            cont.add(crearPanelAccionIcono(iconEliminar));
            return cont;
        }
    }

}
