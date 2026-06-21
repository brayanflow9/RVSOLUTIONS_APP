package vista;

import controlador.ResumenController;
import modelo.Usuario;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MenuAdministrador extends JFrame {

    private final Usuario usuario;
    private final ResumenController resumenController;
    private JPanel root;
    private JPanel drawer;
    private JPanel areaPanel;
    private boolean menuVisible = false;
    private AvatarPanel avatarPanel;
    private JScrollPane scrollActual;
    private JButton botonActivo;
    private JLabel lblModuloActual;
    private static final String FOTO_ADMIN_RUTA = "assets/perfiles/admin.png";

    public MenuAdministrador(Usuario usuario) {
        this.usuario = usuario;
        this.resumenController = new ResumenController();
        setTitle("RV Solutions - Panel Administrador");
        setMinimumSize(new Dimension(1280, 760));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        EstilosRV.aplicarIconoVentana(this);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        BackgroundPanel fondo = new BackgroundPanel("assets/fondos/administrador.png");
        fondo.setLayout(new BorderLayout());
        fondo.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        root = new JPanel(new BorderLayout());
        root.setOpaque(false);
        fondo.add(root, BorderLayout.CENTER);

        drawer = crearMenuHamburguesa();

        areaPanel = new JPanel(new BorderLayout(0, 16));
        areaPanel.setOpaque(false);
        areaPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        root.add(areaPanel, BorderLayout.CENTER);

        areaPanel.add(crearTopbar(), BorderLayout.NORTH);
        mostrarContenido("Dashboard", crearContenidoDashboard());

        actualizarEstadoMenu();
        setContentPane(fondo);
    }

    private JPanel crearTopbar() {
        JPanel topbar = EstilosRV.crearPanelTarjeta(new BorderLayout(18, 0));
        topbar.setBorder(new EmptyBorder(18, 28, 18, 28));
        topbar.setPreferredSize(new Dimension(10, 102));

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 4));
        izquierda.setOpaque(false);

        JButton btnMenu = crearBotonHamburguesa();
        btnMenu.addActionListener(e -> alternarMenu());
        izquierda.add(btnMenu);

        JLabel tituloSistema = new JLabel("RV Soporte Técnico");
        tituloSistema.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        tituloSistema.setForeground(new Color(15, 23, 42));
        izquierda.add(tituloSistema);

        lblModuloActual = new JLabel("/ Dashboard");
        lblModuloActual.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblModuloActual.setForeground(new Color(100, 116, 139));
        izquierda.add(lblModuloActual);

        topbar.add(izquierda, BorderLayout.WEST);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 4));
        derecha.setOpaque(false);

        JButton btnNotificaciones = crearBotonNotificaciones();
        derecha.add(btnNotificaciones);

        JPanel userCard = EstilosRV.crearPanelTarjeta(new BorderLayout(12, 0));
        userCard.setBorder(new EmptyBorder(8, 12, 8, 10));
        userCard.setPreferredSize(new Dimension(305, 58));

        avatarPanel = new AvatarPanel(obtenerNombreVisible().substring(0, 1).toUpperCase());
        avatarPanel.setPreferredSize(new Dimension(42, 42));
        avatarPanel.setToolTipText("Haz clic para agregar o cambiar la foto del administrador");
        avatarPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFotoAdministrador();
            }
        });
        cargarAvatarAdministrador();
        userCard.add(avatarPanel, BorderLayout.WEST);

        JPanel userInfo = new JPanel();
        userInfo.setOpaque(false);
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));

        JLabel usuarioLbl = new JLabel(obtenerNombreVisible());
        usuarioLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usuarioLbl.setForeground(new Color(15, 23, 42));

        JLabel rolLbl = new JLabel("Administrador del sistema");
        rolLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rolLbl.setForeground(new Color(100, 116, 139));

        userInfo.add(Box.createVerticalGlue());
        userInfo.add(usuarioLbl);
        userInfo.add(Box.createVerticalStrut(2));
        userInfo.add(rolLbl);
        userInfo.add(Box.createVerticalGlue());
        userCard.add(userInfo, BorderLayout.CENTER);

        JButton btnOpciones = crearBotonChevronUsuario();
        JPopupMenu menuUsuario = new JPopupMenu();
        JMenuItem itemInfo = new JMenuItem("Ver información del usuario");
        itemInfo.addActionListener(e -> mostrarInformacionUsuario());
        menuUsuario.add(itemInfo);
        btnOpciones.addActionListener(e -> menuUsuario.show(btnOpciones, 0, btnOpciones.getHeight()));
        userCard.add(btnOpciones, BorderLayout.EAST);

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
                int x1 = 12;
                int x2 = getWidth() - 12;
                int y = 12;
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

    private JButton crearBotonChevronUsuario() {
        JButton boton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(28, 38, 60) : new Color(18, 24, 38));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = getWidth() / 2;
                int cy = getHeight() / 2 + 1;
                g2.drawLine(cx - 6, cy - 3, cx, cy + 3);
                g2.drawLine(cx, cy + 3, cx + 6, cy - 3);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        boton.setPreferredSize(new Dimension(34, 34));
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JButton crearBotonNotificaciones() {
        ImageIcon bellIcon = EstilosRV.cargarIcono("assets/ui_icons/campana_roja.png", 18, 18);
        JButton boton = new JButton("Notificaciones (3)");
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

        JPopupMenu menuNotificaciones = new JPopupMenu();
        menuNotificaciones.add(crearItemNotificacion("Entrega pendiente", "Equipo Lenovo listo para entregar mañana."));
        menuNotificaciones.add(crearItemNotificacion("Pago pendiente", "El cliente Juan Pérez aún no registra su pago."));
        menuNotificaciones.add(crearItemNotificacion("Orden próxima", "La orden ORD-2026-0156 vence en 2 días."));
        boton.addActionListener(e -> menuNotificaciones.show(boton, 0, boton.getHeight()));
        return boton;
    }

    private JMenuItem crearItemNotificacion(String titulo, String detalle) {
        JMenuItem item = new JMenuItem("<html><b>" + titulo + "</b><br><span style='color:#64748b;'>" + detalle + "</span></html>");
        item.setPreferredSize(new Dimension(320, 48));
        item.addActionListener(e -> JOptionPane.showMessageDialog(this, detalle, titulo, JOptionPane.INFORMATION_MESSAGE));
        return item;
    }

    private JPanel crearContenidoDashboard() {
        ScrollablePanel content = new ScrollablePanel();
        content.setOpaque(false);
        content.setLayout(new GridBagLayout());
        content.setBorder(new EmptyBorder(0, 0, 24, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 18, 0);

        JPanel hero = crearHeroBanner();
        content.add(hero, gbc);

        gbc.gridy++;
        JPanel kpis = crearKpis();
        content.add(kpis, gbc);

        gbc.gridy++;
        JPanel zonaCentral = new JPanel(new GridBagLayout());
        zonaCentral.setOpaque(false);
        zonaCentral.setPreferredSize(new Dimension(1250, 380));

        GridBagConstraints zbc = new GridBagConstraints();
        zbc.gridy = 0;
        zbc.fill = GridBagConstraints.BOTH;
        zbc.insets = new Insets(0, 0, 0, 16);
        zbc.weighty = 1.0;

        zbc.gridx = 0;
        zbc.weightx = 0.34;
        zonaCentral.add(crearPanelEstado(), zbc);

        zbc.gridx = 1;
        zbc.weightx = 0.66;
        zbc.insets = new Insets(0, 0, 0, 0);
        zonaCentral.add(crearOrdenesRecientesPanel(), zbc);

        content.add(zonaCentral, gbc);

        gbc.gridy++;
        JPanel resumen = crearResumenFinanciero();
        content.add(resumen, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel footerWrap = new JPanel(new BorderLayout());
        footerWrap.setOpaque(false);
        JLabel footer = new JLabel("© 2026 RV Soporte Técnico. Panel administrador con diseño mejorado.", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setForeground(new Color(100, 116, 139));
        footerWrap.add(footer, BorderLayout.NORTH);
        content.add(footerWrap, gbc);

        return content;
    }

    private JPanel crearHeroBanner() {
        HeroBannerPanel hero = new HeroBannerPanel("assets/banners/banner_admin.png");
        hero.setPreferredSize(new Dimension(10, 215));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 215));
        hero.setLayout(new BorderLayout());

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setBorder(new EmptyBorder(34, 34, 26, 34));
        textos.setPreferredSize(new Dimension(580, 215));

        JLabel fecha = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-ES"))));
        fecha.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fecha.setForeground(new Color(55, 65, 81));
        fecha.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo = new JLabel("¡Bienvenido, Administrador!");
        titulo.setFont(new Font("Georgia", Font.BOLD, 34));
        titulo.setForeground(new Color(8, 20, 43));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Panel de control general de RV Soporte Técnico");
        subtitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
        subtitulo.setForeground(new Color(71, 85, 105));
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel detalle = new JLabel("Soluciones rápidas, soporte confiable y métricas al alcance.");
        detalle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detalle.setForeground(new Color(31, 41, 55));
        detalle.setAlignmentX(Component.LEFT_ALIGNMENT);

        textos.add(fecha);
        textos.add(Box.createVerticalStrut(12));
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(8));
        textos.add(subtitulo);
        textos.add(Box.createVerticalStrut(16));
        textos.add(detalle);

        hero.add(textos, BorderLayout.WEST);
        return hero;
    }

    private JPanel crearKpis() {
        JPanel kpis = new JPanel(new GridLayout(2, 3, 14, 14));
        kpis.setOpaque(false);
        kpis.setPreferredSize(new Dimension(1250, 232));
        kpis.setMaximumSize(new Dimension(Integer.MAX_VALUE, 232));
        kpis.add(crearTarjetaKpi("assets/kpi_icons/clientes.png", "C", String.valueOf(resumenController.totalClientes()), "Clientes", "+12% vs. mes anterior", EstilosRV.ROJO));
        kpis.add(crearTarjetaKpi("assets/kpi_icons/equipos.png", "E", String.valueOf(resumenController.totalEquipos()), "Equipos", "+8% vs. mes anterior", EstilosRV.AZUL));
        kpis.add(crearTarjetaKpi("assets/kpi_icons/ordenes.png", "O", String.valueOf(resumenController.totalOrdenes()), "Órdenes", "+15% vs. mes anterior", EstilosRV.NARANJA));
        kpis.add(crearTarjetaKpi("assets/kpi_icons/diagnosticos.png", "D", String.valueOf(resumenController.totalDiagnosticos()), "Diagnósticos", "+5% vs. mes anterior", new Color(124, 58, 237)));
        kpis.add(crearTarjetaKpi("assets/kpi_icons/pagos.png", "P", resumenController.totalIngresosPagados(), "Pagos", "+18% vs. mes anterior", EstilosRV.VERDE));
        kpis.add(crearTarjetaKpi("assets/kpi_icons/usuarios.png", "U", String.valueOf(resumenController.totalUsuarios()), "Usuarios", "Sin cambios", new Color(75, 85, 99)));
        return kpis;
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

        JLabel sub = new JLabel("Sistema de Gestión", SwingConstants.CENTER);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setForeground(new Color(255, 90, 102));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        top.add(sub);

        top.add(Box.createVerticalStrut(18));
        sidebar.add(top, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(8, 1, 0, 10));
        menu.setOpaque(false);
        JButton btnControl = menuButton("Dashboard", true);
        JButton btnClientes = menuButton("Clientes", false);
        JButton btnEquipos = menuButton("Equipos", false);
        JButton btnOrdenes = menuButton("Órdenes y Servicios", false);
        JButton btnDiagnosticos = menuButton("Diagnósticos", false);
        JButton btnPagos = menuButton("Pagos", false);
        JButton btnUsuarios = menuButton("Usuarios", false);
        JButton btnConfig = menuButton("Configuración", false);

        menu.add(btnControl);
        menu.add(btnClientes);
        menu.add(btnEquipos);
        menu.add(btnOrdenes);
        menu.add(btnDiagnosticos);
        menu.add(btnPagos);
        menu.add(btnUsuarios);
        menu.add(btnConfig);
        sidebar.add(menu, BorderLayout.CENTER);

        JButton btnCerrar = menuButton("Cerrar sesión", false);
        sidebar.add(btnCerrar, BorderLayout.SOUTH);

        botonActivo = btnControl;
        btnControl.addActionListener(e -> { activarBoton(btnControl); mostrarContenido("Dashboard", crearContenidoDashboard()); });
        btnClientes.addActionListener(e -> mostrarModulo("Clientes", new FormClientes(), btnClientes));
        btnEquipos.addActionListener(e -> mostrarModulo("Equipos", new FormEquipos(), btnEquipos));
        btnOrdenes.addActionListener(e -> mostrarModulo("Órdenes y Servicios", new FormOrdenes(), btnOrdenes));
        btnDiagnosticos.addActionListener(e -> mostrarModulo("Diagnósticos", new FormDiagnosticos(), btnDiagnosticos));
        btnPagos.addActionListener(e -> mostrarModulo("Pagos", new FormPagos(), btnPagos));
        btnUsuarios.addActionListener(e -> mostrarModulo("Usuarios", new FormUsuarios(usuario), btnUsuarios));
        btnConfig.addActionListener(e -> { activarBoton(btnConfig); mostrarContenido("Configuración", crearPanelConfiguracion()); });
        btnCerrar.addActionListener(e -> cerrarSesion());
        return sidebar;
    }

    private JButton menuButton(String text, boolean active) {
        return new RoundedMenuButton(text, active);
    }

    private JPanel crearTarjetaKpi(String rutaIcono, String letraFallback, String valor, String titulo, String detalle, Color color) {
        JPanel card = EstilosRV.crearPanelTarjeta(new BorderLayout(12, 0));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel badge = crearBadgeImagen(rutaIcono, letraFallback, color);
        card.add(badge, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(titulo);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setForeground(new Color(71, 85, 105));

        JLabel v = new JLabel(valor);
        v.setFont(new Font("Segoe UI", Font.BOLD, 25));
        v.setForeground(new Color(15, 23, 42));

        JLabel d = new JLabel(detalle);
        d.setFont(new Font("Segoe UI", Font.BOLD, 11));
        d.setForeground(detalle.contains("Sin") ? new Color(100, 116, 139) : EstilosRV.VERDE);

        info.add(t);
        info.add(Box.createVerticalStrut(2));
        info.add(v);
        info.add(Box.createVerticalStrut(4));
        info.add(d);
        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private JPanel crearBadgeImagen(String rutaIcono, String letraFallback, Color color) {
        JPanel badge = new JPanel(new BorderLayout());
        badge.setOpaque(false);
        badge.setPreferredSize(new Dimension(62, 62));

        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon icono = EstilosRV.cargarIcono(rutaIcono, 54, 54);
        if (icono != null) {
            iconLabel.setIcon(icono);
        } else {
            badge = EstilosRV.crearBadge(letraFallback, color);
            badge.setPreferredSize(new Dimension(54, 54));
            return badge;
        }
        badge.add(iconLabel, BorderLayout.CENTER);
        return badge;
    }

    private JPanel crearPanelEstado() {
        JPanel panel = EstilosRV.crearPanelTarjeta(new BorderLayout(0, 12));
        panel.setPreferredSize(new Dimension(410, 380));
        panel.add(tituloPanel("Estado operativo"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(14, 0));
        content.setOpaque(false);

        DonutPanel donut = new DonutPanel(
                resumenController.ordenesPendientes(),
                resumenController.ordenesEnProceso(),
                resumenController.ordenesFinalizadas()
        );
        donut.setPreferredSize(new Dimension(190, 190));
        content.add(donut, BorderLayout.WEST);

        JPanel leyenda = new JPanel(new GridLayout(4, 1, 0, 8));
        leyenda.setOpaque(false);
        leyenda.add(lineaEstado("Pendientes", resumenController.ordenesPendientes() + " órdenes", EstilosRV.ROJO, "assets/status_icons/pendientes.png"));
        leyenda.add(lineaEstado("En proceso", resumenController.ordenesEnProceso() + " órdenes", EstilosRV.NARANJA, "assets/status_icons/proceso.png"));
        leyenda.add(lineaEstado("Finalizadas", resumenController.ordenesFinalizadas() + " órdenes", EstilosRV.AZUL, "assets/status_icons/finalizadas.png"));
        leyenda.add(lineaEstado("Ingresos", resumenController.totalIngresosPagados(), EstilosRV.VERDE, "assets/status_icons/ingresos.png"));
        content.add(leyenda, BorderLayout.CENTER);

        panel.add(content, BorderLayout.CENTER);
        JButton b = new RoundedActionButton("Ver detalles  ›", EstilosRV.ROJO, new Color(185, 0, 29));
        b.setPreferredSize(new Dimension(10, 40));
        b.addActionListener(e -> abrirVentana(new FormOrdenes()));
        panel.add(b, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearAccesosRapidos() {
        JPanel panel = EstilosRV.crearPanelTarjeta(new BorderLayout(0, 12));
        panel.setPreferredSize(new Dimension(390, 340));
        panel.add(tituloPanel("Acciones rápidas"), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3, 2, 12, 12));
        grid.setOpaque(false);
        grid.add(acceso("Nuevo cliente", "C", EstilosRV.ROJO, () -> abrirVentana(new FormClientes())));
        grid.add(acceso("Nueva orden", "O", EstilosRV.NARANJA, () -> abrirVentana(new FormOrdenes())));
        grid.add(acceso("Registrar pago", "P", EstilosRV.VERDE, () -> abrirVentana(new FormPagos())));
        grid.add(acceso("Diagnóstico", "D", EstilosRV.ROJO, () -> abrirVentana(new FormDiagnosticos())));
        grid.add(acceso("Buscar equipo", "E", EstilosRV.AZUL, () -> abrirVentana(new FormEquipos())));
        grid.add(acceso("Reportes", "R", new Color(124, 58, 237), () -> JOptionPane.showMessageDialog(this, "Los reportes se descargan desde cada módulo.")));
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JButton acceso(String texto, String letra, Color color, Runnable accion) {
        JButton b = new JButton("<html><center><span style='font-size:20px;'>" + letra + "</span><br>" + texto + "</center></html>");
        b.setBackground(new Color(248, 250, 252));
        b.setForeground(new Color(15, 23, 42));
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createLineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 90), 1));
        b.addActionListener(e -> accion.run());
        return b;
    }

    private JPanel crearActividadReciente() {
        JPanel panel = EstilosRV.crearPanelTarjeta(new BorderLayout(0, 12));
        panel.setPreferredSize(new Dimension(390, 340));
        panel.add(tituloPanel("Actividad reciente"), BorderLayout.NORTH);

        JPanel list = new JPanel(new GridLayout(5, 1, 0, 9));
        list.setOpaque(false);
        list.add(linea("Nueva orden creada", "Orden #ORD-2026-0156 creada para Juan Pérez", EstilosRV.ROJO));
        list.add(linea("Pago registrado", "Pago de S/ 1,250 registrado", EstilosRV.VERDE));
        list.add(linea("Diagnóstico completado", "Informe técnico finalizado", EstilosRV.ROJO));
        list.add(linea("Equipo actualizado", "Laptop Dell - Servicio completado", EstilosRV.AZUL));
        list.add(linea("Nuevo cliente registrado", "Cliente agregado correctamente", new Color(100, 116, 139)));
        panel.add(list, BorderLayout.CENTER);

        JButton b = new JButton("Ver toda la actividad  ›");
        EstilosRV.estilizarBotonOscuro(b);
        panel.add(b, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearOrdenesRecientesPanel() {
        JPanel panel = new DarkOrdersPanel(new BorderLayout(0, 14));
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        panel.setPreferredSize(new Dimension(760, 380));

        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setOpaque(false);

        JPanel titleWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        titleWrap.setOpaque(false);
        ImageIcon orderIcon = EstilosRV.cargarIcono("assets/ui_icons/ordenes_recientes.png", 42, 42);
        JLabel icon = new JLabel(orderIcon);
        titleWrap.add(icon);

        JPanel titleTexts = new JPanel();
        titleTexts.setOpaque(false);
        titleTexts.setLayout(new BoxLayout(titleTexts, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Órdenes recientes");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));

        JLabel subtitle = new JLabel("Resumen de las últimas órdenes registradas en el sistema.");
        subtitle.setForeground(new Color(148, 163, 184));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        titleTexts.add(title);
        titleTexts.add(Box.createVerticalStrut(2));
        titleTexts.add(subtitle);
        titleWrap.add(titleTexts);
        header.add(titleWrap, BorderLayout.WEST);

        JButton nuevaOrden = new JButton("+  Nueva orden");
        nuevaOrden.setFocusPainted(false);
        nuevaOrden.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nuevaOrden.setForeground(Color.WHITE);
        nuevaOrden.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nuevaOrden.setContentAreaFilled(false);
        nuevaOrden.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstilosRV.ROJO, 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        nuevaOrden.addActionListener(e -> abrirVentana(new FormOrdenes()));
        header.add(nuevaOrden, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        JPanel tabla = new JPanel(new GridLayout(6, 5, 0, 0));
        tabla.setOpaque(false);
        tabla.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));

        agregarCeldaTabla(tabla, "#  ID ORDEN", true, null, null);
        agregarCeldaTabla(tabla, "Cliente", true, "assets/ui_icons/cliente_dark.png", null);
        agregarCeldaTabla(tabla, "Equipo", true, "assets/ui_icons/equipo_dark.png", null);
        agregarCeldaTabla(tabla, "Estado", true, "assets/ui_icons/estado_dark.png", null);
        agregarCeldaTabla(tabla, "Fecha ingreso", true, "assets/ui_icons/fecha_dark.png", null);

        agregarFilaOrden(tabla, "ORD-2026-0156", "Juan Pérez", "Laptop Dell Inspiron 15", "En reparación", "08/06/2026 10:30", new Color(37, 99, 235));
        agregarFilaOrden(tabla, "ORD-2026-0155", "María López", "Impresora HP LaserJet", "Pendiente", "07/06/2026 14:15", new Color(245, 158, 11));
        agregarFilaOrden(tabla, "ORD-2026-0154", "Carlos Ramírez", "PC de Escritorio Intel i5", "Activa", "06/06/2026 09:45", new Color(22, 163, 74));
        agregarFilaOrden(tabla, "ORD-2026-0153", "Ana Torres", "Laptop Lenovo ThinkPad", "En reparación", "05/06/2026 16:20", new Color(37, 99, 235));
        agregarFilaOrden(tabla, "ORD-2026-0152", "Luis Gómez", "Monitor LG 24ML600M", "Pendiente", "05/06/2026 11:05", new Color(245, 158, 11));

        panel.add(tabla, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JLabel actualizado = new JLabel("  Actualizado hace 2 min");
        actualizado.setForeground(new Color(148, 163, 184));
        actualizado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ImageIcon relojIcon = EstilosRV.cargarIcono("assets/ui_icons/reloj_rojo.png", 18, 18);
        if (relojIcon != null) actualizado.setIcon(relojIcon);
        footer.add(actualizado, BorderLayout.WEST);

        JButton verTodas = new JButton("Ver todas las órdenes  ›");
        verTodas.setForeground(EstilosRV.ROJO);
        verTodas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        verTodas.setFocusPainted(false);
        verTodas.setContentAreaFilled(false);
        verTodas.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        verTodas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        verTodas.addActionListener(e -> abrirVentana(new FormOrdenes()));
        footer.add(verTodas, BorderLayout.EAST);

        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    private void agregarFilaOrden(JPanel tabla, String id, String cliente, String equipo, String estado, String fecha, Color colorEstado) {
        agregarCeldaTabla(tabla, id, false, null, EstilosRV.ROJO);
        agregarCeldaTabla(tabla, cliente, false, "assets/ui_icons/cliente_dark.png", null);
        agregarCeldaTabla(tabla, equipo, false, "assets/ui_icons/equipo_dark.png", null);
        agregarEstadoTabla(tabla, estado, colorEstado);
        agregarCeldaTabla(tabla, fecha, false, "assets/ui_icons/reloj_dark.png", null);
    }

    private void agregarCeldaTabla(JPanel tabla, String texto, boolean encabezado, String rutaIcono, Color colorTexto) {
        JPanel cell = new JPanel(new BorderLayout(8, 0));
        cell.setOpaque(false);
        cell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(51, 65, 85)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", encabezado ? Font.BOLD : Font.PLAIN, encabezado ? 12 : 13));
        label.setForeground(colorTexto != null ? colorTexto : (encabezado ? new Color(203, 213, 225) : Color.WHITE));

        if (rutaIcono != null) {
            ImageIcon icono = EstilosRV.cargarIcono(rutaIcono, 16, 16);
            if (icono != null) label.setIcon(icono);
            label.setIconTextGap(8);
        }

        cell.add(label, BorderLayout.CENTER);
        tabla.add(cell);
    }

    private void agregarEstadoTabla(JPanel tabla, String estado, Color colorEstado) {
        JPanel cell = new JPanel(new GridBagLayout());
        cell.setOpaque(false);
        cell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(51, 65, 85)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel pill = new JLabel("  ●  " + estado + "  ");
        pill.setOpaque(true);
        pill.setForeground(new Color(255, 255, 255));
        pill.setBackground(new Color(colorEstado.getRed(), colorEstado.getGreen(), colorEstado.getBlue(), 110));
        pill.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pill.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorEstado, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        cell.add(pill);
        tabla.add(cell);
    }

    private JPanel crearResumenFinanciero() {
        JPanel panel = EstilosRV.crearPanelTarjeta(new BorderLayout(22, 0));
        panel.setPreferredSize(new Dimension(1250, 430));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 430));
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);
        JPanel titleWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        titleWrap.setOpaque(false);
        ImageIcon iconHeader = EstilosRV.cargarIcono("assets/ui_icons/icono_crecimiento_dashboard.png", 54, 54);
        JLabel icon = new JLabel(iconHeader);
        titleWrap.add(icon);
        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Resumen financiero");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(15, 23, 42));
        JLabel sub = new JLabel("(últimos 6 meses)");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        sub.setForeground(new Color(100, 116, 139));
        titles.add(title);
        titles.add(Box.createVerticalStrut(2));
        titles.add(sub);
        titleWrap.add(titles);
        header.add(titleWrap, BorderLayout.WEST);

        JPanel filtro = EstilosRV.crearPanelTarjeta(new FlowLayout(FlowLayout.CENTER, 8, 0));
        filtro.setBorder(new EmptyBorder(8, 14, 8, 14));
        JLabel cal = new JLabel(EstilosRV.cargarIcono("assets/ui_icons/icono_calendario_dashboard.png", 22, 22));
        JLabel txtFiltro = new JLabel("6 meses  ▾");
        txtFiltro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtFiltro.setForeground(new Color(15, 23, 42));
        filtro.add(cal);
        filtro.add(txtFiltro);
        header.add(filtro, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        LinePanel chart = new LinePanel();
        chart.setPreferredSize(new Dimension(760, 315));
        panel.add(chart, BorderLayout.CENTER);

        JPanel datos = new JPanel(new GridLayout(3, 1, 0, 14));
        datos.setOpaque(false);
        datos.setPreferredSize(new Dimension(350, 10));
        datos.add(kpiFinanciero("assets/ui_icons/icono_tendencia_dashboard.png", "Ingresos", resumenController.totalIngresosPagados(), "+18% vs. período anterior", EstilosRV.ROJO, new Color(255, 241, 242)));
        datos.add(kpiFinanciero("assets/ui_icons/icono_billetera_dashboard.png", "Gasto operativo", "S/ 18,350", "+7% vs. período anterior", new Color(71, 85, 105), new Color(248, 250, 252)));
        JButton reporte = new RoundedActionButton("Ver reporte financiero  →", EstilosRV.ROJO, new Color(190, 0, 29));
        reporte.setFont(new Font("Segoe UI", Font.BOLD, 17));
        datos.add(reporte);
        panel.add(datos, BorderLayout.EAST);
        return panel;
    }

    private JPanel kpiFinanciero(String rutaIcono, String titulo, String valor, String detalle, Color color, Color bg) {
        JPanel card = new EstilosRV.RoundedPanel(new BorderLayout(16, 0), bg, 24);
        card.setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel icon = new JLabel(EstilosRV.cargarIcono(rutaIcono, 70, 70));
        card.add(icon, BorderLayout.WEST);
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(titulo);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        t.setForeground(new Color(15, 23, 42));
        JLabel v = new JLabel(valor);
        v.setFont(new Font("Segoe UI", Font.BOLD, 26));
        v.setForeground(new Color(15, 23, 42));
        JLabel d = new JLabel(detalle);
        d.setFont(new Font("Segoe UI", Font.BOLD, 13));
        d.setForeground(EstilosRV.VERDE);
        info.add(Box.createVerticalGlue());
        info.add(t);
        info.add(Box.createVerticalStrut(4));
        info.add(v);
        info.add(Box.createVerticalStrut(4));
        info.add(d);
        info.add(Box.createVerticalGlue());
        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private JLabel tituloPanel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 17));
        l.setForeground(new Color(15, 23, 42));
        return l;
    }

    private JPanel lineaEstado(String t, String d, Color c, String rutaIcono) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);

        JLabel iconLabel = new JLabel();
        iconLabel.setPreferredSize(new Dimension(44, 44));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon icono = EstilosRV.cargarIcono(rutaIcono, 40, 40);
        if (icono != null) iconLabel.setIcon(icono);
        p.add(iconLabel, BorderLayout.WEST);

        JPanel centro = new JPanel(new BorderLayout(8, 0));
        centro.setOpaque(false);
        JLabel dot = new JLabel("●");
        dot.setForeground(c);
        dot.setFont(new Font("Segoe UI", Font.BOLD, 16));
        centro.add(dot, BorderLayout.WEST);

        JLabel text = new JLabel("<html><b>" + t + "</b><br><span style='color:#64748b;'>" + d + "</span></html>");
        text.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        centro.add(text, BorderLayout.CENTER);
        p.add(centro, BorderLayout.CENTER);
        return p;
    }

    private JPanel linea(String t, String d, Color c) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);
        JLabel dot = new JLabel("●");
        dot.setForeground(c);
        dot.setFont(new Font("Segoe UI", Font.BOLD, 16));
        p.add(dot, BorderLayout.WEST);
        JLabel text = new JLabel("<html><b>" + t + "</b><br><span style='color:#64748b;'>" + d + "</span></html>");
        text.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        p.add(text, BorderLayout.CENTER);
        return p;
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

        JPanel cabecera = EstilosRV.crearPanelTarjeta(new BorderLayout());
        cabecera.setBorder(new EmptyBorder(18, 22, 18, 22));
        JLabel tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        tituloLbl.setForeground(new Color(15, 23, 42));
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(tituloLbl);
        cabecera.add(textos, BorderLayout.WEST);
        envoltura.add(cabecera, BorderLayout.NORTH);
        envoltura.add(contenidoVentana, BorderLayout.CENTER);

        mostrarContenido(titulo, envoltura);
    }

    private JPanel crearPanelReportes() {
        JPanel panel = EstilosRV.crearPanelTarjeta(new BorderLayout(0, 12));
        panel.setBorder(new EmptyBorder(28, 28, 28, 28));
        JLabel titulo = new JLabel("Reportes del sistema");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(15, 23, 42));
        JLabel detalle = new JLabel("Los reportes se descargan desde cada módulo operativo en formato CSV compatible con Excel.");
        detalle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        detalle.setForeground(new Color(100, 116, 139));
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(8));
        textos.add(detalle);
        panel.add(textos, BorderLayout.NORTH);
        return panel;
    }

    private JPanel crearPanelConfiguracion() {
        JPanel panel = EstilosRV.crearPanelTarjeta(new BorderLayout(0, 18));
        panel.setBorder(new EmptyBorder(28, 28, 28, 28));
        JLabel titulo = new JLabel("Configuración del administrador");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(15, 23, 42));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new GridLayout(2, 3, 16, 16));
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(18, 0, 0, 0));
        contenido.add(crearTarjetaConfiguracionAdmin("Empresa y sistema", "Datos generales, nombre comercial, rutas y parámetros globales."));
        contenido.add(crearTarjetaConfiguracionAdmin("Usuarios y roles", "Administrar usuarios, perfiles, permisos y accesos por módulo."));
        contenido.add(crearTarjetaConfiguracionAdmin("Seguridad", "Políticas de contraseña, sesiones, auditoría y protección del sistema."));
        contenido.add(crearTarjetaConfiguracionAdmin("Base de datos", "Estado de conexión, parámetros técnicos y mantenimiento controlado."));
        contenido.add(crearTarjetaConfiguracionAdmin("Copias de seguridad", "Generar, consultar y restaurar respaldos autorizados."));
        contenido.add(crearTarjetaConfiguracionAdmin("Apariencia general", "Configurar tema, identidad visual e imágenes institucionales."));
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearTarjetaConfiguracionAdmin(String titulo, String detalle) {
        JPanel card = EstilosRV.crearPanelTarjeta(new BorderLayout(0, 12));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel t = new JLabel(titulo);
        t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        t.setForeground(new Color(15, 23, 42));
        JLabel d = new JLabel("<html><div style='width:250px;'>" + detalle + "</div></html>");
        d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        d.setForeground(new Color(100, 116, 139));
        JButton abrir = EstilosRV.crearBotonRedondeado("Abrir", true);
        abrir.addActionListener(e -> JOptionPane.showMessageDialog(this,
                titulo + "\n\n" + detalle,
                "Configuración del administrador",
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

    private void abrirVentana(JFrame ventana) {
        String titulo = ventana.getTitle();
        if (titulo == null || titulo.trim().isEmpty()) titulo = "Módulo";
        titulo = titulo.replace("RV Solutions - ", "").replace("Gestión de ", "");
        mostrarModulo(titulo, ventana, null);
    }

    private void cerrarSesion() {
        dispose();
        Login login = new Login();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
    }

    private void mostrarInformacionUsuario() {
        String nombre = obtenerNombreVisible();
        String user = (usuario != null && usuario.getUsuario() != null && !usuario.getUsuario().trim().isEmpty()) ? usuario.getUsuario() : "admin";
        String rol = (usuario != null && usuario.getRol() != null && !usuario.getRol().trim().isEmpty()) ? usuario.getRol() : "Administrador";
        String estado = (usuario != null && usuario.getEstado() != null && !usuario.getEstado().trim().isEmpty()) ? usuario.getEstado() : "Activo";

        JOptionPane.showMessageDialog(this,
                "Nombre: " + nombre + "\n" +
                        "Usuario: " + user + "\n" +
                        "Rol: " + rol + "\n" +
                        "Estado: " + estado,
                "Información del usuario",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private String obtenerNombreVisible() {
        if (usuario != null && usuario.getNombre() != null && !usuario.getNombre().trim().isEmpty()) {
            return usuario.getNombre().trim();
        }
        return "Administrador";
    }

    private void cargarAvatarAdministrador() {
        File foto = new File(FOTO_ADMIN_RUTA);
        if (foto.exists()) {
            avatarPanel.setImagePath(FOTO_ADMIN_RUTA);
        }
    }

    private void seleccionarFotoAdministrador() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar foto del administrador");
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "png", "jpg", "jpeg"));
        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File origen = chooser.getSelectedFile();
            try {
                Path destinoDir = Path.of("assets", "perfiles");
                if (!Files.exists(destinoDir)) {
                    Files.createDirectories(destinoDir);
                }
                Path destino = destinoDir.resolve("admin.png");
                Files.copy(origen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
                avatarPanel.setImagePath(destino.toString());
                JOptionPane.showMessageDialog(this, "Foto del administrador actualizada correctamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo guardar la foto seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static class HeroBannerPanel extends JPanel {
        private BufferedImage image;

        public HeroBannerPanel(String ruta) {
            setOpaque(false);
            try {
                File f = new File(ruta);
                if (f.exists()) image = ImageIO.read(f);
            } catch (Exception ignored) {
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            int arc = 28;
            Shape clip = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc);
            g2.setClip(clip);

            if (image != null) {
                double panelRatio = (double) getWidth() / getHeight();
                double imageRatio = (double) image.getWidth() / image.getHeight();
                int drawW;
                int drawH;
                int drawX = 0;
                int drawY = 0;

                if (imageRatio > panelRatio) {
                    drawH = getHeight();
                    drawW = (int) (drawH * imageRatio);
                    drawX = getWidth() - drawW;
                } else {
                    drawW = getWidth();
                    drawH = (int) (drawW / imageRatio);
                    drawY = (getHeight() - drawH) / 2;
                }

                g2.drawImage(image, drawX, drawY, drawW, drawH, this);

                GradientPaint overlay = new GradientPaint(
                        0, 0, new Color(255, 255, 255, 240),
                        getWidth(), 0, new Color(255, 255, 255, 25)
                );
                g2.setPaint(overlay);
                g2.fillRect(0, 0, getWidth(), getHeight());
            } else {
                g2.setColor(new Color(245, 247, 250));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            g2.setClip(null);
            g2.setColor(new Color(203, 213, 225));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class DarkOrdersPanel extends JPanel {
        DarkOrdersPanel(LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, new Color(15, 23, 42), getWidth(), getHeight(), new Color(2, 8, 23));
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
            g2.setColor(new Color(224, 14, 36, 160));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class DonutPanel extends JPanel {
        int a, b, c;

        DonutPanel(int a, int b, int c) {
            this.a = Math.max(a, 1);
            this.b = Math.max(b, 1);
            this.c = Math.max(c, 1);
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int total = a + b + c;
            int start = 90;
            int size = Math.min(getWidth(), getHeight()) - 35;
            int x = (getWidth() - size) / 2, y = 18;
            int aa = 360 * a / total, bb = 360 * b / total;
            g2.setStroke(new BasicStroke(28, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(EstilosRV.ROJO);
            g2.drawArc(x, y, size, size, start, -aa);
            g2.setColor(EstilosRV.NARANJA);
            g2.drawArc(x, y, size, size, start - aa, -bb);
            g2.setColor(EstilosRV.AZUL);
            g2.drawArc(x, y, size, size, start - aa - bb, -(360 - aa - bb));
            g2.setColor(new Color(15, 23, 42));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2.drawString("Total", x + size / 2 - 23, y + size / 2 - 4);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
            g2.drawString(String.valueOf(a + b + c), x + size / 2 - 18, y + size / 2 + 24);
            g2.dispose();
        }
    }

    static class LinePanel extends JPanel {
        LinePanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(520, 150));
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 50, h = getHeight() - 35, x0 = 30, y0 = 15;
            g2.setColor(new Color(226, 232, 240));
            for (int i = 0; i < 4; i++) g2.drawLine(x0, y0 + i * h / 4, x0 + w, y0 + i * h / 4);
            g2.setColor(new Color(224, 14, 36, 38));
            int[] xs = {x0, x0 + w / 6, x0 + 2 * w / 6, x0 + 3 * w / 6, x0 + 4 * w / 6, x0 + 5 * w / 6, x0 + w};
            int[] ys = {y0 + h - 18, y0 + h - 28, y0 + h - 44, y0 + h - 70, y0 + h - 62, y0 + h - 92, y0 + h - 112};
            Polygon poly = new Polygon();
            for (int i = 0; i < xs.length; i++) poly.addPoint(xs[i], ys[i]);
            poly.addPoint(x0 + w, y0 + h);
            poly.addPoint(x0, y0 + h);
            g2.fillPolygon(poly);
            g2.setColor(EstilosRV.ROJO);
            g2.setStroke(new BasicStroke(3));
            for (int i = 0; i < xs.length - 1; i++) g2.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
            for (int i = 0; i < xs.length; i++) g2.fillOval(xs[i] - 4, ys[i] - 4, 8, 8);
            g2.dispose();
        }
    }

    static class RoundedMenuButton extends JButton {
        private boolean active;

        RoundedMenuButton(String text, boolean active) {
            super("   " + text);
            this.active = active;
            setForeground(active ? Color.WHITE : new Color(226, 232, 240));
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setHorizontalAlignment(SwingConstants.LEFT);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMargin(new Insets(0, 0, 0, 0));
            setBorder(new EmptyBorder(14, 18, 14, 18));
        }

        void setActive(boolean active) {
            this.active = active;
            setForeground(active ? Color.WHITE : new Color(226, 232, 240));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg;
            if (active) {
                bg = EstilosRV.ROJO;
            } else if (getModel().isRollover()) {
                bg = new Color(20, 34, 58);
            } else {
                bg = new Color(9, 18, 33);
            }

            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedActionButton extends JButton {
        private final Color bg;
        private final Color border;

        RoundedActionButton(String text, Color bg, Color border) {
            super(text);
            this.bg = bg;
            this.border = border;
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMargin(new Insets(10, 16, 10, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color fill = getModel().isRollover() ? bg.brighter() : bg;
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
            g2.setColor(border);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 22, 22);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class AvatarPanel extends JPanel {
        private BufferedImage image;
        private final String fallbackLetter;

        AvatarPanel(String fallbackLetter) {
            this.fallbackLetter = fallbackLetter;
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        void setImagePath(String path) {
            try {
                File f = new File(path);
                if (f.exists()) {
                    image = ImageIO.read(f);
                }
            } catch (IOException e) {
                image = null;
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int size = Math.min(getWidth(), getHeight()) - 2;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            Shape circle = new Ellipse2D.Double(x, y, size, size);

            g2.setColor(new Color(241, 245, 249));
            g2.fill(circle);

            if (image != null) {
                g2.setClip(circle);
                g2.drawImage(image, x, y, size, size, this);
                g2.setClip(null);
            } else {
                g2.setColor(new Color(15, 23, 42));
                g2.setFont(new Font("Segoe UI", Font.BOLD, size / 2));
                FontMetrics fm = g2.getFontMetrics();
                int tx = x + (size - fm.stringWidth(fallbackLetter)) / 2;
                int ty = y + ((size - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(fallbackLetter, tx, ty);
            }

            g2.setColor(new Color(203, 213, 225));
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(circle);
            g2.dispose();
        }
    }

    static class ScrollablePanel extends JPanel implements Scrollable {
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 26;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 90;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
}
