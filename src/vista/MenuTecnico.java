package vista;

import controlador.ResumenController;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MenuTecnico extends JFrame {
    private final Usuario usuario;
    private final ResumenController resumenController;
    private JPanel root;
    private JPanel drawer;
    private JPanel areaPanel;
    private JScrollPane scrollActual;
    private JLabel lblModuloActual;
    private JButton botonActivo;
    private boolean menuVisible = false;

    public MenuTecnico(Usuario usuario) {
        this.usuario = usuario;
        this.resumenController = new ResumenController();
        setTitle("RV Solutions - Panel Técnico");
        setMinimumSize(new Dimension(1280, 760));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        EstilosRV.aplicarIconoVentana(this);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        BackgroundPanel fondo = new BackgroundPanel("assets/fondos/tecnico.png");
        fondo.setLayout(new BorderLayout());
        fondo.setBorder(new EmptyBorder(14, 14, 14, 14));

        root = new JPanel(new BorderLayout());
        root.setOpaque(false);
        fondo.add(root, BorderLayout.CENTER);

        drawer = crearMenuHamburguesa();
        areaPanel = new JPanel(new BorderLayout(0, 16));
        areaPanel.setOpaque(false);
        root.add(areaPanel, BorderLayout.CENTER);

        areaPanel.add(crearTopbar(), BorderLayout.NORTH);
        mostrarContenido("Dashboard", crearDashboardTecnico());

        actualizarEstadoMenu();
        setContentPane(fondo);
    }

    private JPanel crearTopbar() {
        JPanel topbar = EstilosRV.crearPanelTarjeta(new BorderLayout(18, 0));
        topbar.setBorder(new EmptyBorder(16, 26, 16, 26));
        topbar.setPreferredSize(new Dimension(10, 96));

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 4));
        izquierda.setOpaque(false);

        JButton btnMenu = crearBotonHamburguesa();
        btnMenu.addActionListener(e -> alternarMenu());
        izquierda.add(btnMenu);

        JLabel tituloSistema = new JLabel("RV Soporte Técnico");
        tituloSistema.setFont(new Font("Segoe UI Semibold", Font.BOLD, 23));
        tituloSistema.setForeground(new Color(15, 23, 42));
        izquierda.add(tituloSistema);

        lblModuloActual = new JLabel("/ Dashboard");
        lblModuloActual.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblModuloActual.setForeground(new Color(100, 116, 139));
        izquierda.add(lblModuloActual);

        topbar.add(izquierda, BorderLayout.WEST);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 4));
        derecha.setOpaque(false);
        derecha.add(crearBotonNotificaciones());

        JPanel userCard = EstilosRV.crearPanelTarjeta(new BorderLayout(10, 0));
        userCard.setBorder(new EmptyBorder(8, 12, 8, 12));
        userCard.setPreferredSize(new Dimension(270, 56));

        JPanel avatar = new AvatarCircle(obtenerNombreVisible().substring(0, 1).toUpperCase(), EstilosRV.ROJO);
        avatar.setPreferredSize(new Dimension(38, 38));
        userCard.add(avatar, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel lblNombre = new JLabel(obtenerNombreVisible());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombre.setForeground(new Color(15, 23, 42));
        JLabel lblRol = new JLabel("Técnico principal");
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRol.setForeground(new Color(100, 116, 139));
        info.add(lblNombre);
        info.add(Box.createVerticalStrut(2));
        info.add(lblRol);
        userCard.add(info, BorderLayout.CENTER);

        JLabel estado = new JLabel("En línea");
        estado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        estado.setForeground(new Color(22, 163, 74));
        userCard.add(estado, BorderLayout.EAST);

        derecha.add(userCard);
        topbar.add(derecha, BorderLayout.EAST);
        return topbar;
    }

    private JButton crearBotonHamburguesa() {
        JButton boton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(28, 38, 60) : new Color(18, 24, 38));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int x1 = 12, x2 = getWidth() - 12, y = 12;
                for (int i = 0; i < 3; i++) {
                    g2.drawLine(x1, y + (i * 8), x2, y + (i * 8));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        boton.setPreferredSize(new Dimension(50, 44));
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JButton crearBotonNotificaciones() {
        JButton boton = new JButton("Notificaciones (3)");
        ImageIcon bellIcon = EstilosRV.cargarIcono("assets/ui_icons/campana_roja.png", 18, 18);
        if (bellIcon != null) {
            boton.setIcon(bellIcon);
            boton.setIconTextGap(8);
        }
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setForeground(EstilosRV.ROJO);
        boton.setBackground(new Color(255, 255, 255, 0));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));
        boton.setContentAreaFilled(false);

        JPopupMenu menu = new JPopupMenu();
        menu.add(crearItemNotificacion("Orden pendiente", "Hay 2 órdenes pendientes por atender."));
        menu.add(crearItemNotificacion("Diagnóstico próximo", "Mañana tienes un diagnóstico programado."));
        menu.add(crearItemNotificacion("Equipo registrado", "Se registró un nuevo equipo en el sistema."));
        boton.addActionListener(e -> menu.show(boton, 0, boton.getHeight()));
        return boton;
    }

    private JMenuItem crearItemNotificacion(String titulo, String detalle) {
        JMenuItem item = new JMenuItem("<html><b>" + titulo + "</b><br><span style='color:#64748b;'>" + detalle + "</span></html>");
        item.setPreferredSize(new Dimension(320, 46));
        item.addActionListener(e -> JOptionPane.showMessageDialog(this, detalle, titulo, JOptionPane.INFORMATION_MESSAGE));
        return item;
    }

    private JPanel crearMenuHamburguesa() {
        JPanel sidebar = new JPanel(new BorderLayout(0, 18));
        sidebar.setBackground(EstilosRV.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(280, 10));
        sidebar.setBorder(new EmptyBorder(16, 14, 20, 14));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JPanel logoMarco = new JPanel(new BorderLayout());
        logoMarco.setOpaque(false);
        logoMarco.setMaximumSize(new Dimension(220, 128));
        logoMarco.setBorder(new EmptyBorder(4, 10, 10, 10));
        JLabel logo = EstilosRV.crearLogoLabel(160, 112);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoMarco.add(logo, BorderLayout.CENTER);
        top.add(logoMarco);

        JLabel nombre = new JLabel("RV Soporte Técnico", SwingConstants.CENTER);
        nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        nombre.setForeground(Color.WHITE);
        nombre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        top.add(nombre);

        top.add(Box.createVerticalStrut(4));
        JLabel sub = new JLabel("Área técnica", SwingConstants.CENTER);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setForeground(new Color(255, 90, 102));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        top.add(sub);

        top.add(Box.createVerticalStrut(18));
        sidebar.add(top, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(5, 1, 0, 10));
        menu.setOpaque(false);
        JButton btnDashboard = menuButton("Dashboard", true);
        JButton btnEquipos = menuButton("Equipos", false);
        JButton btnOrdenes = menuButton("Órdenes y Servicios", false);
        JButton btnDiagnosticos = menuButton("Diagnósticos", false);
        JButton btnConfig = menuButton("Configuración", false);

        menu.add(btnDashboard);
        menu.add(btnEquipos);
        menu.add(btnOrdenes);
        menu.add(btnDiagnosticos);
        menu.add(btnConfig);
        sidebar.add(menu, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout(0, 12));
        footer.setOpaque(false);
        footer.add(crearTarjetaTecnicoSidebar(), BorderLayout.CENTER);
        JButton btnCerrar = menuButton("Cerrar sesión", false);
        footer.add(btnCerrar, BorderLayout.SOUTH);
        sidebar.add(footer, BorderLayout.SOUTH);

        botonActivo = btnDashboard;
        btnDashboard.addActionListener(e -> { activarBoton(btnDashboard); mostrarContenido("Dashboard", crearDashboardTecnico()); });
        btnEquipos.addActionListener(e -> mostrarModulo("Equipos", new FormEquipos(), btnEquipos));
        btnOrdenes.addActionListener(e -> mostrarModulo("Órdenes y Servicios", new FormOrdenes(), btnOrdenes));
        btnDiagnosticos.addActionListener(e -> mostrarModulo("Diagnósticos", new FormDiagnosticos(), btnDiagnosticos));
        btnConfig.addActionListener(e -> { activarBoton(btnConfig); mostrarContenido("Configuración", crearPanelConfiguracion()); });
        btnCerrar.addActionListener(e -> cerrarSesion());
        return sidebar;
    }

    private JPanel crearTarjetaTecnicoSidebar() {
        SidebarStatusPanel panel = new SidebarStatusPanel();
        panel.setLayout(new BorderLayout(12, 0));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel avatar = new AvatarCircle(obtenerNombreVisible().substring(0, 1).toUpperCase(), EstilosRV.ROJO);
        avatar.setPreferredSize(new Dimension(42, 42));
        panel.add(avatar, BorderLayout.WEST);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel nombre = new JLabel(obtenerNombreVisible());
        nombre.setForeground(Color.WHITE);
        nombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel rol = new JLabel("• Técnico principal");
        rol.setForeground(new Color(34, 197, 94));
        rol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textos.add(nombre);
        textos.add(Box.createVerticalStrut(3));
        textos.add(rol);
        panel.add(textos, BorderLayout.CENTER);
        return panel;
    }

    private JButton menuButton(String text, boolean active) {
        RoundedMenuButton b = new RoundedMenuButton(text);
        b.setActive(active);
        return b;
    }

    private JPanel crearDashboardTecnico() {
        ScrollablePanel wrapper = new ScrollablePanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new GridBagLayout());
        wrapper.setBorder(new EmptyBorder(0, 0, 24, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 18, 0);

        wrapper.add(crearHero(), gbc);
        gbc.gridy++;
        wrapper.add(crearKpis(), gbc);
        gbc.gridy++;
        wrapper.add(crearZonaPrincipal(), gbc);
        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        JLabel footer = new JLabel("© 2026 RV Solutions - Área técnica con diseño mejorado.", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setForeground(new Color(226, 232, 240));
        JPanel footerWrap = new JPanel(new BorderLayout());
        footerWrap.setOpaque(false);
        footerWrap.add(footer, BorderLayout.NORTH);
        wrapper.add(footerWrap, gbc);
        return wrapper;
    }

    private JPanel crearHero() {
        JPanel hero = new JPanel(new BorderLayout(18, 0));
        hero.setOpaque(false);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("¡Bienvenido, Técnico " + obtenerNombreVisible() + "! 👋");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        textos.add(titulo);
        hero.add(textos, BorderLayout.WEST);

        DarkGlassPanel fechaPanel = new DarkGlassPanel(new FlowLayout(FlowLayout.CENTER, 10, 8), new Color(8, 20, 43, 210), new Color(255,255,255,38), 22);
        fechaPanel.setBorder(new EmptyBorder(8, 14, 8, 14));
        JLabel icon = new JLabel(EstilosRV.cargarIcono("assets/tecnico_icons/calendario.png", 22, 22));
        JLabel fecha = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-ES"))));
        fecha.setForeground(Color.WHITE);
        fecha.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fechaPanel.add(icon);
        fechaPanel.add(fecha);
        hero.add(fechaPanel, BorderLayout.EAST);
        return hero;
    }

    private JPanel crearKpis() {
        JPanel kpis = new JPanel(new GridLayout(1, 4, 16, 0));
        kpis.setOpaque(false);
        kpis.setPreferredSize(new Dimension(1240, 132));
        kpis.add(crearTarjetaKpi("assets/tecnico_icons/ordenes.png", String.valueOf(resumenController.totalOrdenes()), "Órdenes", "Servicios registrados", EstilosRV.ROJO));
        kpis.add(crearTarjetaKpi("assets/tecnico_icons/equipos.png", String.valueOf(resumenController.totalEquipos()), "Equipos", "Registrados", EstilosRV.AZUL));
        kpis.add(crearTarjetaKpi("assets/tecnico_icons/ordenes.png", String.valueOf(resumenController.ordenesPendientes()), "Pendientes", "Por atender", EstilosRV.NARANJA));
        kpis.add(crearTarjetaKpi("assets/tecnico_icons/diagnosticos.png", String.valueOf(resumenController.totalDiagnosticos()), "Diagnósticos", "Informes", new Color(147, 51, 234)));
        return kpis;
    }

    private JPanel crearTarjetaKpi(String iconoPath, String valor, String titulo, String detalle, Color color) {
        DarkGlassPanel card = new DarkGlassPanel(new BorderLayout(14, 0), new Color(8, 20, 43, 205), new Color(255, 255, 255, 30), 24);
        card.setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel icon = new JLabel();
        ImageIcon img = EstilosRV.cargarIcono(iconoPath, 54, 54);
        if (img != null) icon.setIcon(img);
        icon.setPreferredSize(new Dimension(58, 58));
        card.add(icon, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(titulo);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setForeground(new Color(203, 213, 225));
        JLabel v = new JLabel(valor);
        v.setFont(new Font("Segoe UI", Font.BOLD, 40));
        v.setForeground(Color.WHITE);
        JLabel d = new JLabel(detalle);
        d.setFont(new Font("Segoe UI", Font.BOLD, 13));
        d.setForeground(color);
        info.add(t);
        info.add(Box.createVerticalStrut(4));
        info.add(v);
        info.add(Box.createVerticalStrut(4));
        info.add(d);
        card.add(info, BorderLayout.CENTER);

        JLabel chevron = new JLabel("›", SwingConstants.CENTER);
        chevron.setForeground(new Color(203, 213, 225));
        chevron.setFont(new Font("Segoe UI", Font.BOLD, 26));
        JPanel round = new DarkGlassPanel(new GridBagLayout(), new Color(255,255,255,10), new Color(255,255,255,28), 18);
        round.setPreferredSize(new Dimension(36,36));
        round.add(chevron);
        card.add(round, BorderLayout.EAST);
        return card;
    }

    private JPanel crearZonaPrincipal() {
        JPanel zona = new JPanel(new GridBagLayout());
        zona.setOpaque(false);
        zona.setPreferredSize(new Dimension(1240, 560));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.54;
        gbc.insets = new Insets(0, 0, 0, 16);
        zona.add(crearPanelTrabajoTecnico(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.46;
        gbc.insets = new Insets(0, 0, 0, 0);
        zona.add(crearColumnaDerecha(), gbc);
        return zona;
    }

    private JPanel crearPanelTrabajoTecnico() {
        DarkGlassPanel panel = new DarkGlassPanel(new BorderLayout(0, 16), new Color(8, 20, 43, 210), new Color(255, 255, 255, 30), 28);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        izq.setOpaque(false);
        JLabel icon = new JLabel(EstilosRV.cargarIcono("assets/tecnico_icons/grafico.png", 28, 28));
        JLabel title = new JLabel("Flujo de trabajo técnico");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        JLabel sub = new JLabel("Resumen de actividad actual");
        sub.setForeground(new Color(148, 163, 184));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel texts = new JPanel();
        texts.setOpaque(false);
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.add(title);
        texts.add(sub);
        izq.add(icon);
        izq.add(texts);
        header.add(izq, BorderLayout.WEST);

        DarkGlassPanel filtro = new DarkGlassPanel(new FlowLayout(FlowLayout.CENTER, 8, 4), new Color(255,255,255,10), new Color(255,255,255,28), 18);
        filtro.setBorder(new EmptyBorder(6, 10, 6, 10));
        filtro.add(new JLabel(EstilosRV.cargarIcono("assets/tecnico_icons/calendario.png", 16, 16)));
        JLabel hoy = new JLabel("Hoy");
        hoy.setForeground(Color.WHITE);
        hoy.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filtro.add(hoy);
        header.add(filtro, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.56;
        gbc.insets = new Insets(0, 0, 0, 12);
        JPanel lista = new JPanel(new GridLayout(4, 1, 0, 14));
        lista.setOpaque(false);
        lista.add(crearLineaTrabajo("Pendientes", "órdenes por atender", resumenController.ordenesPendientes(), EstilosRV.ROJO));
        lista.add(crearLineaTrabajo("Equipos", "marca, modelo, serie y estado", resumenController.totalEquipos(), EstilosRV.AZUL));
        lista.add(crearLineaTrabajo("Órdenes", resumenController.ordenesEnProceso() + " en proceso y " + resumenController.ordenesFinalizadas() + " finalizadas", resumenController.totalOrdenes(), EstilosRV.NARANJA));
        lista.add(crearLineaTrabajo("Diagnósticos", resumenController.totalDiagnosticos() + " informes registrados", resumenController.totalDiagnosticos(), new Color(147, 51, 234)));
        center.add(lista, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.44;
        gbc.insets = new Insets(0, 12, 0, 0);
        DonutChartPanel donut = new DonutChartPanel(
                resumenController.ordenesPendientes(),
                resumenController.totalEquipos(),
                resumenController.totalOrdenes(),
                resumenController.totalDiagnosticos());
        donut.setPreferredSize(new Dimension(270, 270));
        center.add(donut, gbc);
        panel.add(center, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new GridLayout(1, 2, 14, 0));
        acciones.setOpaque(false);
        JButton verOrdenes = new RoundedActionButton("Ver órdenes  →", EstilosRV.ROJO, new Color(178, 8, 27));
        JButton verEquipos = new RoundedActionButton("Ver equipos  →", new Color(10, 27, 54), new Color(5, 18, 38));
        verOrdenes.addActionListener(e -> mostrarModulo("Órdenes y Servicios", new FormOrdenes(), botonActivo));
        verEquipos.addActionListener(e -> mostrarModulo("Equipos", new FormEquipos(), botonActivo));
        acciones.add(verOrdenes);
        acciones.add(verEquipos);
        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearLineaTrabajo(String titulo, String detalle, int total, Color color) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);

        JLabel dot = new JLabel("●");
        dot.setForeground(color);
        dot.setFont(new Font("Segoe UI", Font.BOLD, 20));
        p.add(dot, BorderLayout.WEST);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(titulo);
        t.setForeground(Color.WHITE);
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JLabel d = new JLabel(detalle);
        d.setForeground(new Color(148, 163, 184));
        d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textos.add(t);
        textos.add(Box.createVerticalStrut(3));
        textos.add(d);
        p.add(textos, BorderLayout.CENTER);

        DarkGlassPanel chip = new DarkGlassPanel(new BorderLayout(), new Color(255,255,255,8), new Color(255,255,255,25), 16);
        chip.setBorder(new EmptyBorder(8, 12, 8, 12));
        chip.setPreferredSize(new Dimension(66, 54));
        JLabel totalLbl = new JLabel("<html><center><span style='font-size:18px; font-weight:700;'>" + total + "</span><br><span style='font-size:10px;'>Total</span></center></html>", SwingConstants.CENTER);
        totalLbl.setForeground(Color.WHITE);
        chip.add(totalLbl, BorderLayout.CENTER);
        p.add(chip, BorderLayout.EAST);
        return p;
    }

    private JPanel crearColumnaDerecha() {
        JPanel col = new JPanel(new GridLayout(2, 1, 0, 16));
        col.setOpaque(false);
        col.add(crearPanelOrdenesRecientes());
        col.add(crearPanelProximosDiagnosticos());
        return col;
    }

    private JPanel crearPanelOrdenesRecientes() {
        DarkGlassPanel panel = new DarkGlassPanel(new BorderLayout(0, 12), new Color(8, 20, 43, 210), new Color(255,255,255,30), 28);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        panel.add(crearEncabezadoPanelLateral("Órdenes recientes", "assets/tecnico_icons/ordenes.png"), BorderLayout.NORTH);

        JPanel tabla = new JPanel(new GridLayout(5, 5, 0, 0));
        tabla.setOpaque(false);
        agregarCeldaLight(tabla, "ID Orden", true);
        agregarCeldaLight(tabla, "Cliente", true);
        agregarCeldaLight(tabla, "Equipo", true);
        agregarCeldaLight(tabla, "Estado", true);
        agregarCeldaLight(tabla, "Actualizado", true);

        agregarFilaOrden(tabla, "ORD-2026-0041", "Cliente Demo", "Generador RV-7500", "En proceso", new Color(217, 119, 6), "Hoy, 11:45");
        agregarFilaOrden(tabla, "ORD-2026-0040", "María López", "Inversor RV-2000", "Pendiente", new Color(245, 158, 11), "Hoy, 09:20");
        agregarFilaOrden(tabla, "ORD-2026-0039", "Servicios del Norte", "Planta RV-10000", "En proceso", new Color(217, 119, 6), "Ayer, 16:30");
        agregarFilaOrden(tabla, "ORD-2026-0038", "Constructora Alfa", "Compresor RV-300", "Pendiente", new Color(245, 158, 11), "Ayer, 10:15");
        panel.add(tabla, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelProximosDiagnosticos() {
        DarkGlassPanel panel = new DarkGlassPanel(new BorderLayout(0, 12), new Color(8, 20, 43, 210), new Color(255,255,255,30), 28);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        panel.add(crearEncabezadoPanelLateral("Próximos diagnósticos", "assets/tecnico_icons/diagnosticos.png"), BorderLayout.NORTH);

        JPanel tabla = new JPanel(new GridLayout(4, 4, 0, 0));
        tabla.setOpaque(false);
        agregarCeldaLight(tabla, "ID Diagnóstico", true);
        agregarCeldaLight(tabla, "Equipo", true);
        agregarCeldaLight(tabla, "Cliente", true);
        agregarCeldaLight(tabla, "Fecha programada", true);

        agregarFilaDiagnostico(tabla, "DIAG-2026-0015", "Generador RV-7500", "Cliente Demo", "09/06/2026 10:00");
        agregarFilaDiagnostico(tabla, "DIAG-2026-0016", "Inversor RV-2000", "María López", "10/06/2026 14:00");
        agregarFilaDiagnostico(tabla, "DIAG-2026-0017", "Planta RV-10000", "Servicios del Norte", "11/06/2026 09:00");
        panel.add(tabla, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearEncabezadoPanelLateral(String titulo, String iconoPath) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        left.add(new JLabel(EstilosRV.cargarIcono(iconoPath, 24, 24)));
        JLabel title = new JLabel(titulo);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        left.add(title);
        header.add(left, BorderLayout.WEST);

        JButton verTodas = new RoundedActionButton("Ver todas", new Color(255,255,255,18), new Color(255,255,255,10));
        verTodas.setForeground(Color.WHITE);
        verTodas.setPreferredSize(new Dimension(104, 34));
        verTodas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.add(verTodas, BorderLayout.EAST);
        return header;
    }

    private void agregarCeldaLight(JPanel tabla, String texto, boolean encabezado) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setOpaque(false);
        cell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(255, 255, 255, 18)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", encabezado ? Font.BOLD : Font.PLAIN, encabezado ? 12 : 12));
        label.setForeground(encabezado ? new Color(191, 219, 254) : Color.WHITE);
        cell.add(label, BorderLayout.CENTER);
        tabla.add(cell);
    }

    private void agregarFilaOrden(JPanel tabla, String id, String cliente, String equipo, String estado, Color colorEstado, String actualizado) {
        agregarCeldaLight(tabla, id, false);
        agregarCeldaLight(tabla, cliente, false);
        agregarCeldaLight(tabla, equipo, false);
        JPanel cell = new JPanel(new GridBagLayout());
        cell.setOpaque(false);
        cell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(255, 255, 255, 18)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        JLabel pill = new JLabel(estado, SwingConstants.CENTER);
        pill.setOpaque(true);
        pill.setFont(new Font("Segoe UI", Font.BOLD, 11));
        pill.setForeground(new Color(255, 248, 235));
        pill.setBackground(new Color(colorEstado.getRed(), colorEstado.getGreen(), colorEstado.getBlue(), 120));
        pill.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorEstado, 1),
                new EmptyBorder(4, 8, 4, 8)
        ));
        cell.add(pill);
        tabla.add(cell);
        agregarCeldaLight(tabla, actualizado, false);
    }

    private void agregarFilaDiagnostico(JPanel tabla, String id, String equipo, String cliente, String fecha) {
        agregarCeldaLight(tabla, id, false);
        agregarCeldaLight(tabla, equipo, false);
        agregarCeldaLight(tabla, cliente, false);
        agregarCeldaLight(tabla, fecha, false);
    }

    private void mostrarContenido(String titulo, JComponent contenido) {
        if (scrollActual != null) {
            areaPanel.remove(scrollActual);
        }
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setWheelScrollingEnabled(true);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(26);
        scroll.getVerticalScrollBar().setBlockIncrement(90);
        scrollActual = scroll;
        areaPanel.add(scrollActual, BorderLayout.CENTER);
        if (lblModuloActual != null) lblModuloActual.setText("/ " + titulo);
        areaPanel.revalidate();
        areaPanel.repaint();
    }

    private void mostrarModulo(String titulo, JFrame ventana, JButton botonMenu) {
        if (botonMenu != null) activarBoton(botonMenu);
        Container contenidoVentana = ventana.getContentPane();
        ventana.setContentPane(new JPanel());
        ventana.dispose();

        JPanel envoltura = new JPanel(new BorderLayout(0, 16));
        envoltura.setOpaque(false);
        envoltura.setBorder(new EmptyBorder(0, 0, 24, 0));

        JPanel cabecera = new DarkGlassPanel(new BorderLayout(), new Color(8, 20, 43, 210), new Color(255,255,255,30), 24);
        cabecera.setBorder(new EmptyBorder(18, 22, 18, 22));
        JLabel tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        tituloLbl.setForeground(Color.WHITE);
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(tituloLbl);
        cabecera.add(textos, BorderLayout.WEST);
        envoltura.add(cabecera, BorderLayout.NORTH);
        envoltura.add(contenidoVentana, BorderLayout.CENTER);

        mostrarContenido(titulo, envoltura);
    }

    private JPanel crearPanelConfiguracion() {
        DarkGlassPanel panel = new DarkGlassPanel(new BorderLayout(0, 18), new Color(8, 20, 43, 210), new Color(255,255,255,30), 28);
        panel.setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel titulo = new JLabel("Configuración del técnico");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);
        panel.add(titulo, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new GridLayout(2, 3, 16, 16));
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(18, 0, 0, 0));
        contenido.add(crearTarjetaConfig("Mi perfil", "Actualizar nombre, correo, teléfono, especialidad y estado de disponibilidad."));
        contenido.add(crearTarjetaConfig("Seguridad", "Cambiar la contraseña personal y proteger el acceso a la cuenta técnica."));
        contenido.add(crearTarjetaConfig("Apariencia", "Seleccionar tema, tamaño de texto y preferencias visuales del panel."));
        contenido.add(crearTarjetaConfig("Notificaciones", "Activar avisos de órdenes, diagnósticos y equipos pendientes de entrega."));
        contenido.add(crearTarjetaConfig("Preferencias de trabajo", "Definir la vista inicial, registros por página y estados predeterminados."));
        contenido.add(crearTarjetaConfig("Información del sistema", "Consultar versión, rol, conexión y estado general de la aplicación."));
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearTarjetaConfig(String titulo, String detalle) {
        DarkGlassPanel card = new DarkGlassPanel(new BorderLayout(0, 12), new Color(255,255,255,10), new Color(255,255,255,24), 22);
        card.setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel t = new JLabel(titulo);
        t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        t.setForeground(Color.WHITE);
        JLabel d = new JLabel("<html><div style='width:250px;'>" + detalle + "</div></html>");
        d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        d.setForeground(new Color(191, 219, 254));
        JButton abrir = new RoundedActionButton("Abrir", EstilosRV.ROJO, EstilosRV.ROJO_OSCURO);
        abrir.setPreferredSize(new Dimension(90, 36));
        abrir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        abrir.addActionListener(e -> JOptionPane.showMessageDialog(this,
                titulo + "\n\n" + detalle,
                "Configuración del técnico",
                JOptionPane.INFORMATION_MESSAGE));
        card.add(t, BorderLayout.NORTH);
        card.add(d, BorderLayout.CENTER);
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pie.setOpaque(false);
        pie.add(abrir);
        card.add(pie, BorderLayout.SOUTH);
        return card;
    }

    private void activarBoton(JButton boton) {
        if (botonActivo instanceof RoundedMenuButton) {
            ((RoundedMenuButton) botonActivo).setActive(false);
        }
        botonActivo = boton;
        if (botonActivo instanceof RoundedMenuButton) {
            ((RoundedMenuButton) botonActivo).setActive(true);
        }
    }

    private void alternarMenu() {
        menuVisible = !menuVisible;
        actualizarEstadoMenu();
        root.revalidate();
        root.repaint();
    }

    private void actualizarEstadoMenu() {
        root.remove(drawer);
        if (menuVisible) {
            root.add(drawer, BorderLayout.WEST);
            areaPanel.setBorder(new EmptyBorder(0, 18, 0, 0));
        } else {
            areaPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        }
    }

    private String obtenerNombreVisible() {
        if (usuario != null && usuario.getNombre() != null && !usuario.getNombre().trim().isEmpty()) {
            return usuario.getNombre().trim();
        }
        return "Técnico";
    }

    private void cerrarSesion() {
        dispose();
        Login login = new Login();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
    }

    static class ScrollablePanel extends JPanel implements Scrollable {
        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 24; }
        @Override public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 96; }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() { return false; }
    }

    static class RoundedMenuButton extends JButton {
        private boolean active;
        RoundedMenuButton(String text) {
            super(text);
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(new EmptyBorder(14, 18, 14, 18));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setForeground(new Color(226, 232, 240));
            setFont(new Font("Segoe UI", Font.BOLD, 13));
        }
        void setActive(boolean active) { this.active = active; repaint(); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bg = active ? EstilosRV.ROJO : (getModel().isRollover() ? new Color(17, 28, 47) : new Color(0,0,0,0));
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedActionButton extends JButton {
        private final Color top;
        private final Color bottom;
        RoundedActionButton(String text, Color top, Color bottom) {
            super(text);
            this.top = top;
            this.bottom = bottom;
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setPreferredSize(new Dimension(10, 48));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(255,255,255,28));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class DarkGlassPanel extends JPanel {
        private final Color bg;
        private final Color border;
        private final int radius;
        DarkGlassPanel(LayoutManager layout, Color bg, Color border, int radius) {
            super(layout);
            this.bg = bg;
            this.border = border;
            this.radius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 55));
            g2.fillRoundRect(4, 7, getWidth()-8, getHeight()-8, radius, radius);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth()-6, getHeight()-6, radius, radius);
            g2.setColor(border);
            g2.drawRoundRect(0, 0, getWidth()-7, getHeight()-7, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class SidebarStatusPanel extends JPanel {
        SidebarStatusPanel() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255,255,255,12));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
            g2.setColor(new Color(255,255,255,22));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 22, 22);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class AvatarCircle extends JPanel {
        private final String initial;
        private final Color color;
        AvatarCircle(String initial, Color color) {
            this.initial = initial;
            this.color = color;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillOval(0, 0, getWidth()-1, getHeight()-1);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, Math.max(16, getHeight()/2)));
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(initial)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(initial, x, y);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class DonutChartPanel extends JPanel {
        private final int clientes;
        private final int equipos;
        private final int ordenes;
        private final int diagnosticos;
        DonutChartPanel(int clientes, int equipos, int ordenes, int diagnosticos) {
            this.clientes = clientes;
            this.equipos = equipos;
            this.ordenes = ordenes;
            this.diagnosticos = diagnosticos;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int total = Math.max(1, clientes + equipos + ordenes + diagnosticos);
            int size = Math.min(getWidth(), getHeight()) - 48;
            int x = (getWidth() - size) / 2;
            int y = 18;
            int stroke = 34;
            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
            int start = 90;
            start = drawArc(g2, x, y, size, start, clientes, total, EstilosRV.ROJO);
            start = drawArc(g2, x, y, size, start, diagnosticos, total, new Color(147, 51, 234));
            start = drawArc(g2, x, y, size, start, ordenes, total, EstilosRV.NARANJA);
            drawArc(g2, x, y, size, start, equipos, total, EstilosRV.AZUL);

            int inner = size - stroke - 12;
            g2.setColor(new Color(5, 16, 34, 230));
            g2.fillOval(x + stroke/2 + 6, y + stroke/2 + 6, inner, inner);

            String totalTxt = String.valueOf(total);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 34));
            FontMetrics fm = g2.getFontMetrics();
            int tx = getWidth()/2 - fm.stringWidth(totalTxt)/2;
            int ty = y + size/2 - 4;
            g2.drawString(totalTxt, tx, ty);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            String desc = "Actividades";
            FontMetrics fm2 = g2.getFontMetrics();
            g2.drawString(desc, getWidth()/2 - fm2.stringWidth(desc)/2, ty + 24);
            String desc2 = "totales";
            g2.drawString(desc2, getWidth()/2 - fm2.stringWidth(desc2)/2, ty + 42);

            dibujarLeyenda(g2, 20, getHeight() - 34, EstilosRV.ROJO, "Pendientes");
            dibujarLeyenda(g2, 110, getHeight() - 34, EstilosRV.AZUL, "Equipos");
            dibujarLeyenda(g2, 200, getHeight() - 34, EstilosRV.NARANJA, "Órdenes");
            dibujarLeyenda(g2, 20, getHeight() - 14, new Color(147, 51, 234), "Diagnósticos");
            g2.dispose();
        }
        private int drawArc(Graphics2D g2, int x, int y, int size, int start, int value, int total, Color color) {
            int angle = Math.round((value * 360f) / total);
            g2.setColor(color);
            g2.draw(new Arc2D.Double(x, y, size, size, start, -angle, Arc2D.OPEN));
            return start - angle;
        }
        private void dibujarLeyenda(Graphics2D g2, int x, int y, Color color, String texto) {
            g2.setColor(color);
            g2.fillOval(x, y-10, 10, 10);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString(texto, x + 16, y);
        }
    }
}
