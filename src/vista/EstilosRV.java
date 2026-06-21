package vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EstilosRV {

    public static final Color FONDO_NEGRO = new Color(235, 238, 243);
    public static final Color FONDO_APP = new Color(235, 238, 243);
    public static final Color SIDEBAR = new Color(7, 15, 28);
    public static final Color SIDEBAR_2 = new Color(12, 22, 39);
    public static final Color PANEL_NEGRO = Color.WHITE;
    public static final Color PANEL_OSCURO = Color.WHITE;
    public static final Color PANEL_MEDIO = new Color(248, 250, 252);
    public static final Color ROJO = new Color(224, 14, 36);
    public static final Color ROJO_CLARO = new Color(255, 58, 76);
    public static final Color ROJO_OSCURO = new Color(178, 8, 27);
    public static final Color AZUL = new Color(37, 99, 235);
    public static final Color VERDE = new Color(16, 185, 129);
    public static final Color NARANJA = new Color(245, 158, 11);
    public static final Color BLANCO = new Color(18, 24, 38);
    public static final Color GRIS_CLARO = new Color(100, 116, 139);
    public static final Color TEXTO_SECUNDARIO = new Color(100, 116, 139);
    public static final Color BORDE = new Color(226, 232, 240);
    public static final Color BORDE_SUAVE = new Color(226, 232, 240);

    public static class RoundedPanel extends JPanel {
        private Color bg;
        private int radius;
        public RoundedPanel(LayoutManager layout, Color bg, int radius) {
            super(layout);
            this.bg = bg;
            this.radius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(15, 23, 42, 18));
            g2.fillRoundRect(3, 5, getWidth()-8, getHeight()-8, radius, radius);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth()-6, getHeight()-8, radius, radius);
            g2.setColor(BORDE);
            g2.drawRoundRect(0, 0, getWidth()-7, getHeight()-9, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void aplicarIconoVentana(JFrame ventana) {
        ImageIcon icono = cargarIcono("assets/logo/icon_rv.png", 64, 64);
        if (icono != null) ventana.setIconImage(icono.getImage());
    }

    public static ImageIcon cargarIcono(String ruta, int ancho, int alto) {
        try {
            File archivo = new File(ruta);
            if (!archivo.exists()) return null;
            BufferedImage img = ImageIO.read(archivo);
            Image escalada = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(escalada);
        } catch (IOException e) { return null; }
    }

    public static JLabel crearImagenCabecera(String ruta, int ancho, int alto) {
        JLabel lbl = new JLabel();
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        lbl.setPreferredSize(new Dimension(ancho, alto));
        ImageIcon icono = cargarIcono(ruta, ancho, alto);
        if (icono != null) lbl.setIcon(icono);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        return lbl;
    }

    public static JLabel crearLogoLabel(int ancho, int alto) {
        JLabel lbl = new JLabel();
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon icono = cargarIcono("assets/logo/logo_rv.png", ancho, alto);
        if (icono != null) lbl.setIcon(icono);
        else {
            lbl.setText("RV"); lbl.setFont(new Font("Arial", Font.BOLD, 42)); lbl.setForeground(ROJO);
        }
        return lbl;
    }

    public static JPanel crearPanelTarjeta() { return crearPanelTarjeta(new BorderLayout()); }

    public static JPanel crearPanelTarjeta(LayoutManager layout) {
        if (layout == null) layout = null;
        JPanel panel = new RoundedPanel(layout, Color.WHITE, 22);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        return panel;
    }

    public static JPanel crearPanelTransparente(LayoutManager layout) {
        JPanel panel = new JPanel(layout); panel.setOpaque(false); return panel;
    }

    public static Border bordeCampo() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(9, 12, 9, 12));
    }

    public static void estilizarTitulo(JLabel label) {
        label.setForeground(new Color(15, 23, 42)); label.setFont(new Font("Segoe UI", Font.BOLD, 28));
    }
    public static void estilizarSubtitulo(JLabel label) {
        label.setForeground(TEXTO_SECUNDARIO); label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
    }
    public static void estilizarEtiqueta(JLabel label) {
        label.setForeground(new Color(15, 23, 42)); label.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
    public static void estilizarCampo(JTextField campo) {
        campo.setBackground(new Color(248, 250, 252)); campo.setForeground(new Color(15, 23, 42));
        campo.setCaretColor(new Color(15, 23, 42)); campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(bordeCampo());
    }
    public static void estilizarAreaTexto(JTextArea area) {
        area.setBackground(new Color(248, 250, 252)); area.setForeground(new Color(15, 23, 42));
        area.setCaretColor(new Color(15, 23, 42)); area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 12)); area.setLineWrap(true); area.setWrapStyleWord(true);
    }
    public static void estilizarCombo(JComboBox<?> combo) {
        combo.setBackground(new Color(248, 250, 252)); combo.setForeground(new Color(15, 23, 42));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13)); combo.setBorder(bordeCampo());
    }
    public static void estilizarBoton(JButton boton) {
        boton.setBackground(ROJO); boton.setForeground(Color.WHITE); boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setFocusPainted(false); boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ROJO_OSCURO, 1), BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    public static void estilizarBotonOscuro(JButton boton) {
        boton.setBackground(new Color(15, 23, 42)); boton.setForeground(Color.WHITE); boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setFocusPainted(false); boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 41, 59), 1), BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    public static void estilizarBotonMenu(JButton boton, boolean activo) {
        boton.setOpaque(true); boton.setContentAreaFilled(true); boton.setBorderPainted(false);
        if (activo) { boton.setBackground(ROJO); boton.setForeground(Color.WHITE); }
        else { boton.setBackground(SIDEBAR); boton.setForeground(new Color(226, 232, 240)); }
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13)); boton.setFocusPainted(false);
        boton.setHorizontalAlignment(SwingConstants.LEFT); boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(13, 18, 13, 18));
    }
    public static void estilizarTabla(JTable tabla) {
        tabla.setBackground(Color.WHITE); tabla.setForeground(new Color(15, 23, 42));
        tabla.setGridColor(new Color(226, 232, 240)); tabla.setSelectionBackground(new Color(254, 226, 226));
        tabla.setSelectionForeground(new Color(127, 29, 29)); tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(32); tabla.setFillsViewportHeight(true); tabla.setShowVerticalLines(false);
        JTableHeader header = tabla.getTableHeader(); header.setBackground(new Color(248, 250, 252));
        header.setForeground(new Color(51, 65, 85)); header.setFont(new Font("Segoe UI", Font.BOLD, 12)); header.setReorderingAllowed(false);
    }
    public static void estilizarScroll(JScrollPane scroll) {
        scroll.getViewport().setBackground(Color.WHITE); scroll.setBorder(BorderFactory.createLineBorder(BORDE)); scroll.setBackground(Color.WHITE);
    }


    public static Color[] coloresEstado(String estado) {
        String v = estado == null ? "" : estado.trim().toLowerCase();
        if (v.contains("inactivo") || v.contains("anulado") || v.contains("cancelado")) {
            return new Color[]{new Color(220, 38, 38), new Color(254, 226, 226)};
        }
        if (v.contains("activo") || v.contains("pagado") || v.contains("finalizado") || v.contains("entregado") || v.contains("reparado") || v.contains("operativo")) {
            return new Color[]{new Color(22, 163, 74), new Color(220, 252, 231)};
        }
        if (v.contains("proceso") || v.contains("revisión") || v.contains("revision")) {
            return new Color[]{new Color(37, 99, 235), new Color(219, 234, 254)};
        }
        if (v.contains("pendiente")) {
            return new Color[]{new Color(217, 119, 6), new Color(254, 243, 199)};
        }
        return new Color[]{new Color(100, 116, 139), new Color(241, 245, 249)};
    }

    public static JPanel crearBadgeEstado(String texto, Color fg, Color bg, Color cellBg) {
        JPanel cont = new JPanel(new GridBagLayout());
        cont.setOpaque(true);
        cont.setBackground(cellBg);
        JPanel chip = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        chip.setOpaque(false);
        chip.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(fg);
        chip.add(lbl, BorderLayout.CENTER);
        cont.add(chip);
        return cont;
    }

    public static JPanel crearBadge(String letra, Color color) {
        JPanel badge = new RoundedPanel(new BorderLayout(), new Color(color.getRed(), color.getGreen(), color.getBlue(), 32), 18);
        badge.setPreferredSize(new Dimension(54, 54)); badge.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        JLabel lbl = new JLabel(letra, SwingConstants.CENTER); lbl.setFont(new Font("Segoe UI", Font.BOLD, 22)); lbl.setForeground(color);
        badge.add(lbl, BorderLayout.CENTER); return badge;
    }
    public static JButton crearBotonRedondeado(String texto, boolean principal) {
        JButton b = new JButton(texto);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setOpaque(true);
        if (principal) {
            b.setBackground(ROJO);
            b.setForeground(Color.WHITE);
            b.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        } else {
            b.setBackground(Color.WHITE);
            b.setForeground(new Color(15, 23, 42));
            b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE_SUAVE), BorderFactory.createEmptyBorder(10, 18, 10, 18)));
        }
        return b;
    }

    public static JPanel crearCabeceraTablaModulo(String titulo, JButton btnNuevo, JButton btnActualizar, JButton btnEliminar, JButton btnDescargar) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        JLabel lbl = new JLabel(titulo);
        lbl.setForeground(new Color(15, 23, 42));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panel.add(lbl, BorderLayout.WEST);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);
        JButton[] botones = new JButton[]{btnNuevo, btnActualizar, btnEliminar, btnDescargar};
        for (int i = 0; i < botones.length; i++) {
            JButton b = botones[i];
            b.setText(i == 0 ? "+ Nuevo" : b.getText());
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setFont(new Font("Segoe UI", Font.BOLD, 13));
            b.setPreferredSize(new Dimension(i == 0 ? 130 : 155, 40));
            if (i == 0) {
                b.setBackground(ROJO);
                b.setForeground(Color.WHITE);
                b.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
            } else {
                b.setBackground(Color.WHITE);
                b.setForeground(new Color(15, 23, 42));
                b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE_SUAVE), BorderFactory.createEmptyBorder(9, 14, 9, 14)));
            }
            acciones.add(b);
        }
        panel.add(acciones, BorderLayout.EAST);
        return panel;
    }

}
