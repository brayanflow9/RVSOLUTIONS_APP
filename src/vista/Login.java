package vista;

import controlador.UsuarioController;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JButton btnIngresar;
    private JButton btnRestablecer;
    private JCheckBox chkRecordarme;
    private UsuarioController usuarioController;

    public Login() {
        usuarioController = new UsuarioController();
        setTitle("RV Solutions - Login");
        setMinimumSize(new Dimension(1280, 760));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        EstilosRV.aplicarIconoVentana(this);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        BackgroundPanel fondo = new BackgroundPanel("assets/fondos/login_referencia.png");
        fondo.setLayout(new GridBagLayout());

        JPanel contenedorPrincipal = EstilosRV.crearPanelTarjeta(new BorderLayout(28, 0));
        contenedorPrincipal.setPreferredSize(new Dimension(1180, 650));
        contenedorPrincipal.setBorder(new EmptyBorder(18, 18, 18, 18));
        contenedorPrincipal.setBackground(new Color(245, 247, 250));

        JPanel panelIzquierdo = crearPanelIlustracion();
        JPanel panelDerecho = crearPanelFormulario();

        contenedorPrincipal.add(panelIzquierdo, BorderLayout.CENTER);
        contenedorPrincipal.add(panelDerecho, BorderLayout.EAST);

        fondo.add(contenedorPrincipal, new GridBagConstraints());
        setContentPane(fondo);
    }

    private JPanel crearPanelIlustracion() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 8, 10, 8));

        JPanel textoSuperior = new JPanel();
        textoSuperior.setOpaque(false);
        textoSuperior.setLayout(new BoxLayout(textoSuperior, BoxLayout.Y_AXIS));
        textoSuperior.setBorder(new EmptyBorder(18, 10, 0, 10));

        JLabel titular = new JLabel("GESTIONA. SOPORTA. RESUELVE.");
        titular.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titular.setForeground(new Color(61, 61, 61));
        JLabel subtitulo = new JLabel("Soluciones eficientes para un servicio excepcional.");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(95, 95, 95));
        JLabel subtitulo2 = new JLabel("Una experiencia moderna desde el primer ingreso.");
        subtitulo2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo2.setForeground(new Color(95, 95, 95));

        textoSuperior.add(titular);
        textoSuperior.add(Box.createVerticalStrut(8));
        textoSuperior.add(subtitulo);
        textoSuperior.add(subtitulo2);
        panel.add(textoSuperior, BorderLayout.NORTH);

        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon ilustracion = EstilosRV.cargarIcono("assets/fondos/login_ilustracion.png", 610, 430);
        if (ilustracion != null) lblImagen.setIcon(ilustracion);
        panel.add(lblImagen, BorderLayout.CENTER);

        JPanel tarjetaInfo = EstilosRV.crearPanelTarjeta(new BorderLayout(12, 0));
        tarjetaInfo.setBorder(new EmptyBorder(16, 18, 16, 18));
        tarjetaInfo.setPreferredSize(new Dimension(100, 95));
        JPanel badge = EstilosRV.crearBadge("✓", EstilosRV.ROJO);
        tarjetaInfo.add(badge, BorderLayout.WEST);
        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        JLabel l1 = new JLabel("Gestión eficiente. Soporte confiable.");
        l1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        l1.setForeground(new Color(33, 33, 33));
        JLabel l2 = new JLabel("Soluciones rápidas, clientes satisfechos.");
        l2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l2.setForeground(new Color(102, 102, 102));
        txt.add(l1);
        txt.add(Box.createVerticalStrut(6));
        txt.add(l2);
        tarjetaInfo.add(txt, BorderLayout.CENTER);
        panel.add(tarjetaInfo, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel envoltura = new JPanel(new GridBagLayout());
        envoltura.setOpaque(false);
        envoltura.setPreferredSize(new Dimension(470, 0));

        JPanel card = EstilosRV.crearPanelTarjeta();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(28, 34, 28, 34));
        card.setPreferredSize(new Dimension(420, 620));

        JLabel logo = new JLabel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon logoImg = EstilosRV.cargarIcono("assets/logo/logo_rv.png", 130, 130);
        if (logoImg != null) logo.setIcon(logoImg);
        card.add(logo);

        JLabel empresa = new JLabel("RV Soporte Técnico");
        empresa.setAlignmentX(Component.CENTER_ALIGNMENT);
        empresa.setFont(new Font("Segoe UI", Font.BOLD, 24));
        empresa.setForeground(new Color(55, 55, 55));
        card.add(empresa);

        JLabel sistema = new JLabel("Sistema de Gestión");
        sistema.setAlignmentX(Component.CENTER_ALIGNMENT);
        sistema.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sistema.setForeground(EstilosRV.ROJO);
        card.add(sistema);

        JLabel linea = new JLabel("────────");
        linea.setAlignmentX(Component.CENTER_ALIGNMENT);
        linea.setForeground(EstilosRV.ROJO);
        card.add(Box.createVerticalStrut(4));
        card.add(linea);
        card.add(Box.createVerticalStrut(14));

        JLabel bienvenida = new JLabel("Bienvenido");
        bienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        bienvenida.setFont(new Font("Segoe UI", Font.BOLD, 28));
        bienvenida.setForeground(new Color(55, 55, 55));
        card.add(bienvenida);

        JLabel acceso = new JLabel("Inicia sesión para continuar");
        acceso.setAlignmentX(Component.CENTER_ALIGNMENT);
        acceso.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        acceso.setForeground(new Color(103, 103, 103));
        card.add(Box.createVerticalStrut(6));
        card.add(acceso);
        card.add(Box.createVerticalStrut(18));

        JLabel lblUsuario = new JLabel("Usuario");
        EstilosRV.estilizarEtiqueta(lblUsuario);
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblUsuario);
        card.add(Box.createVerticalStrut(6));

        txtUsuario = new JTextField();
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        EstilosRV.estilizarCampo(txtUsuario);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        card.add(txtUsuario);
        card.add(Box.createVerticalStrut(18));

        JLabel lblClave = new JLabel("Contraseña");
        EstilosRV.estilizarEtiqueta(lblClave);
        lblClave.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblClave);
        card.add(Box.createVerticalStrut(6));

        txtClave = new JPasswordField();
        txtClave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        EstilosRV.estilizarCampo(txtClave);
        txtClave.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        card.add(txtClave);
        card.add(Box.createVerticalStrut(14));

        JPanel opciones = new JPanel(new BorderLayout());
        opciones.setOpaque(false);
        chkRecordarme = new JCheckBox("Recordarme");
        chkRecordarme.setOpaque(false);
        chkRecordarme.setForeground(new Color(90, 90, 90));
        chkRecordarme.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        opciones.add(chkRecordarme, BorderLayout.WEST);

        btnRestablecer = new JButton("¿Olvidaste tu contraseña?");
        btnRestablecer.setContentAreaFilled(false);
        btnRestablecer.setBorderPainted(false);
        btnRestablecer.setForeground(EstilosRV.ROJO);
        btnRestablecer.setFocusPainted(false);
        btnRestablecer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRestablecer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        opciones.add(btnRestablecer, BorderLayout.EAST);
        card.add(opciones);
        card.add(Box.createVerticalStrut(12));

        btnIngresar = new JButton("Iniciar / Ingresar");
        EstilosRV.estilizarBoton(btnIngresar);
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnIngresar.setToolTipText("Presione para iniciar sesión en el sistema");
        card.add(btnIngresar);

        btnIngresar.addActionListener(e -> iniciarSesion());
        txtClave.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) iniciarSesion();
            }
        });

        btnRestablecer.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Comuníquese con el administrador del sistema para restablecer su contraseña.",
                "Restablecer contraseña",
                JOptionPane.INFORMATION_MESSAGE
        ));

        envoltura.add(card);
        return envoltura;
    }

    private void iniciarSesion() {
        String usuarioTexto = txtUsuario.getText().trim();
        String claveTexto = new String(txtClave.getPassword()).trim();

        if (usuarioTexto.isEmpty() || claveTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña.", "Datos requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnIngresar.setEnabled(false);
        btnIngresar.setText("Validando...");

        SwingUtilities.invokeLater(() -> {
            Usuario usuario = usuarioController.iniciarSesion(usuarioTexto, claveTexto);
            if (usuario == null) {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de acceso", JOptionPane.ERROR_MESSAGE);
                txtClave.setText("");
                txtUsuario.requestFocus();
                btnIngresar.setEnabled(true);
                btnIngresar.setText("Iniciar / Ingresar");
                return;
            }
            abrirMenuSegunRol(usuario);
        });
    }

    private void abrirMenuSegunRol(Usuario usuario) {
        String rol = normalizarRol(usuario.getRol());
        JFrame menu;
        if (rol.equals("administrador")) {
            menu = new MenuAdministrador(usuario);
        } else if (rol.equals("tecnico")) {
            menu = new MenuTecnico(usuario);
        } else {
            JOptionPane.showMessageDialog(this, "Acceso correcto, pero el rol no está configurado correctamente: " + usuario.getRol(), "Rol no reconocido", JOptionPane.ERROR_MESSAGE);
            btnIngresar.setEnabled(true);
            btnIngresar.setText("Iniciar / Ingresar");
            return;
        }
        menu.setLocationRelativeTo(null);
        aplicarTransicionDisolver(menu);
    }

    private void aplicarTransicionDisolver(JFrame menu) {
        final float[] opacidad = {1.0f};
        Timer timer = new Timer(18, null);
        timer.addActionListener(e -> {
            opacidad[0] -= 0.08f;
            if (opacidad[0] <= 0.05f) {
                timer.stop();
                dispose();
                menu.setVisible(true);
                menu.toFront();
                return;
            }
            try {
                setOpacity(Math.max(0.05f, opacidad[0]));
            } catch (Exception ex) {
                // Si el sistema no permite opacidad en ventanas decoradas, se mantiene una transición breve.
            }
        });
        timer.start();
    }

    private String normalizarRol(String rol) {
        if (rol == null) return "";
        return rol.trim().toLowerCase().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
    }
}
