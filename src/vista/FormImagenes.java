package vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FormImagenes extends JFrame {

    private static class ImagenModulo {
        String nombre;
        String archivo;
        String uso;
        ImagenModulo(String nombre, String archivo, String uso) {
            this.nombre = nombre;
            this.archivo = archivo;
            this.uso = uso;
        }
        @Override
        public String toString() { return nombre + "  →  " + archivo; }
    }

    private final List<ImagenModulo> imagenes = new ArrayList<>();
    private JPanel gridGaleria;
    private JComboBox<ImagenModulo> cboDestinos;
    private JTextField txtArchivoSeleccionado;
    private JLabel lblPreviewNueva;
    private File archivoTemporalSeleccionado;

    public FormImagenes() {
        registrarImagenes();
        setTitle("RV Solutions - Módulo de Imágenes");
        setSize(1280, 760);
        setMinimumSize(new Dimension(1080, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        EstilosRV.aplicarIconoVentana(this);
        inicializar();
    }

    private void registrarImagenes() {
        imagenes.add(new ImagenModulo("Logo principal", "assets/logo/logo_rv.png", "Logo principal del sistema. Se muestra en login, encabezados y menú lateral."));
        imagenes.add(new ImagenModulo("Icono de ventana", "assets/logo/icon_rv.png", "Icono pequeño que aparece en la ventana del sistema."));
        imagenes.add(new ImagenModulo("Fondo login", "assets/fondos/login.png", "Imagen de fondo para la pantalla de inicio de sesión."));
        imagenes.add(new ImagenModulo("Fondo administrador", "assets/fondos/administrador.png", "Fondo visual del panel del administrador."));
        imagenes.add(new ImagenModulo("Fondo técnico", "assets/fondos/tecnico.png", "Fondo visual del panel del técnico."));
        imagenes.add(new ImagenModulo("Banner login", "assets/modulo_imagenes/img_login_banner.png", "Banner o ilustración decorativa del login."));
        imagenes.add(new ImagenModulo("Dashboard administrador", "assets/modulo_imagenes/img_dashboard_admin.png", "Imagen de apoyo del resumen general del administrador."));
        imagenes.add(new ImagenModulo("Dashboard técnico", "assets/modulo_imagenes/img_dashboard_tecnico.png", "Imagen de apoyo del resumen operativo del técnico."));
        imagenes.add(new ImagenModulo("Clientes", "assets/modulo_imagenes/img_clientes.png", "Imagen del módulo de clientes."));
        imagenes.add(new ImagenModulo("Equipos", "assets/modulo_imagenes/img_equipos.png", "Imagen del módulo de equipos."));
        imagenes.add(new ImagenModulo("Órdenes y servicios", "assets/modulo_imagenes/img_ordenes.png", "Imagen del módulo de órdenes y servicios."));
        imagenes.add(new ImagenModulo("Diagnósticos", "assets/modulo_imagenes/img_diagnosticos.png", "Imagen del módulo de diagnósticos."));
        imagenes.add(new ImagenModulo("Pagos", "assets/modulo_imagenes/img_pagos.png", "Imagen del módulo de pagos."));
        imagenes.add(new ImagenModulo("Usuarios", "assets/modulo_imagenes/img_usuarios.png", "Imagen del módulo de usuarios."));
        imagenes.add(new ImagenModulo("Reportes", "assets/modulo_imagenes/img_reportes.png", "Imagen del módulo de reportes y exportaciones."));
        imagenes.add(new ImagenModulo("Configuración", "assets/modulo_imagenes/img_configuracion.png", "Imagen del módulo de configuración."));
        imagenes.add(new ImagenModulo("Módulo de imágenes", "assets/modulo_imagenes/img_imagenes.png", "Imagen del módulo de imágenes."));
    }

    private void inicializar() {
        JPanel root = new JPanel(new BorderLayout(18, 18));
        root.setBackground(EstilosRV.FONDO_APP);
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        root.add(crearHeader(), BorderLayout.NORTH);

        JPanel zonaSuperior = new JPanel(new GridLayout(1, 2, 18, 18));
        zonaSuperior.setOpaque(false);
        zonaSuperior.add(crearPanelDescripcion());
        zonaSuperior.add(crearPanelCarga());
        root.add(zonaSuperior, BorderLayout.CENTER);

        root.add(crearPanelGaleria(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel crearHeader() {
        JPanel header = EstilosRV.crearPanelTarjeta(new BorderLayout(16, 0));
        header.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JPanel titles = new JPanel(new GridLayout(2,1));
        titles.setOpaque(false);
        JLabel title = new JLabel("Módulo de Imágenes");
        EstilosRV.estilizarTitulo(title);
        JLabel sub = new JLabel("Carga, reemplaza y organiza las imágenes visuales del sistema desde una sola pantalla.");
        EstilosRV.estilizarSubtitulo(sub);
        titles.add(title);
        titles.add(sub);
        header.add(titles, BorderLayout.CENTER);

        header.add(EstilosRV.crearImagenCabecera("assets/modulo_imagenes/img_imagenes.png", 160, 62), BorderLayout.WEST);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.setOpaque(false);
        JButton abrirCarpeta = new JButton("Abrir carpeta assets");
        EstilosRV.estilizarBotonOscuro(abrirCarpeta);
        abrirCarpeta.addActionListener(e -> abrirCarpetaAssets());
        JButton cerrar = new JButton("Cerrar");
        EstilosRV.estilizarBoton(cerrar);
        cerrar.addActionListener(e -> dispose());
        acciones.add(abrirCarpeta);
        acciones.add(cerrar);
        header.add(acciones, BorderLayout.EAST);
        return header;
    }

    private JPanel crearPanelDescripcion() {
        JPanel panel = EstilosRV.crearPanelTarjeta(new BorderLayout(0, 14));
        panel.setPreferredSize(new Dimension(460, 250));

        JLabel titulo = new JLabel("¿Qué imágenes puedes insertar?");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(15, 23, 42));
        panel.add(titulo, BorderLayout.NORTH);

        JTextArea info = new JTextArea(
                "En este módulo puedes reemplazar o insertar imágenes para el sistema.\n\n" +
                "Imágenes principales:\n" +
                "• logo_rv.png  → logo principal\n" +
                "• icon_rv.png  → icono pequeño de la ventana\n" +
                "• login.png  → fondo del login\n" +
                "• administrador.png  → fondo del administrador\n" +
                "• tecnico.png  → fondo del técnico\n\n" +
                "Imágenes por módulo:\n" +
                "• img_login_banner.png\n" +
                "• img_dashboard_admin.png\n" +
                "• img_dashboard_tecnico.png\n" +
                "• img_clientes.png\n" +
                "• img_equipos.png\n" +
                "• img_ordenes.png\n" +
                "• img_diagnosticos.png\n" +
                "• img_pagos.png\n" +
                "• img_usuarios.png\n" +
                "• img_reportes.png\n" +
                "• img_configuracion.png\n" +
                "• img_imagenes.png\n\n" +
                "Recomendación: usa imágenes PNG o JPG con buena resolución para mantener el diseño moderno del sistema."
        );
        info.setEditable(false);
        info.setOpaque(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setForeground(new Color(71, 85, 105));
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(info, BorderLayout.CENTER);

        JLabel nota = new JLabel("Total de espacios configurados: " + imagenes.size());
        nota.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nota.setForeground(EstilosRV.ROJO);
        panel.add(nota, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelCarga() {
        JPanel panel = EstilosRV.crearPanelTarjeta(new BorderLayout(0, 14));

        JLabel titulo = new JLabel("Insertar o reemplazar imagen");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(15, 23, 42));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(16, 0));
        cuerpo.setOpaque(false);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblDestino = new JLabel("Destino");
        EstilosRV.estilizarEtiqueta(lblDestino);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formulario.add(lblDestino, gbc);

        cboDestinos = new JComboBox<>(imagenes.toArray(new ImagenModulo[0]));
        EstilosRV.estilizarCombo(cboDestinos);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        formulario.add(cboDestinos, gbc);

        JLabel lblArchivo = new JLabel("Archivo seleccionado");
        EstilosRV.estilizarEtiqueta(lblArchivo);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formulario.add(lblArchivo, gbc);

        txtArchivoSeleccionado = new JTextField();
        txtArchivoSeleccionado.setEditable(false);
        EstilosRV.estilizarCampo(txtArchivoSeleccionado);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        formulario.add(txtArchivoSeleccionado, gbc);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.setOpaque(false);
        JButton btnSeleccionar = new JButton("Seleccionar imagen");
        EstilosRV.estilizarBotonOscuro(btnSeleccionar);
        btnSeleccionar.addActionListener(e -> seleccionarImagen());
        JButton btnGuardar = new JButton("Guardar / Reemplazar");
        EstilosRV.estilizarBoton(btnGuardar);
        btnGuardar.addActionListener(e -> guardarImagenEnDestino());
        botones.add(btnSeleccionar);
        botones.add(btnGuardar);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1;
        formulario.add(botones, gbc);

        JTextArea ayuda = new JTextArea(
                "Cómo funciona:\n" +
                "1. Elige el destino (por ejemplo, Logo principal).\n" +
                "2. Selecciona una imagen desde tu computadora.\n" +
                "3. Pulsa Guardar / Reemplazar.\n" +
                "4. La imagen se copiará automáticamente en la carpeta assets con el nombre correcto."
        );
        ayuda.setEditable(false);
        ayuda.setOpaque(false);
        ayuda.setLineWrap(true);
        ayuda.setWrapStyleWord(true);
        ayuda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ayuda.setForeground(new Color(71, 85, 105));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formulario.add(ayuda, gbc);

        cuerpo.add(formulario, BorderLayout.CENTER);

        JPanel previewBox = EstilosRV.crearPanelTarjeta(new BorderLayout());
        previewBox.setPreferredSize(new Dimension(220, 220));
        JLabel tituloPrev = new JLabel("Vista previa", SwingConstants.CENTER);
        tituloPrev.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tituloPrev.setForeground(new Color(51, 65, 85));
        previewBox.add(tituloPrev, BorderLayout.NORTH);
        lblPreviewNueva = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblPreviewNueva.setForeground(EstilosRV.GRIS_CLARO);
        previewBox.add(lblPreviewNueva, BorderLayout.CENTER);
        cuerpo.add(previewBox, BorderLayout.EAST);

        panel.add(cuerpo, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelGaleria() {
        JPanel contenedor = EstilosRV.crearPanelTarjeta(new BorderLayout(0, 14));
        contenedor.setPreferredSize(new Dimension(10, 350));

        JLabel titulo = new JLabel("Galería actual del sistema");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(15, 23, 42));
        contenedor.add(titulo, BorderLayout.NORTH);

        gridGaleria = new JPanel(new GridLayout(0, 2, 16, 16));
        gridGaleria.setOpaque(false);
        reconstruirGaleria();

        JScrollPane scroll = new JScrollPane(gridGaleria);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        contenedor.add(scroll, BorderLayout.CENTER);
        return contenedor;
    }

    private void reconstruirGaleria() {
        if (gridGaleria == null) return;
        gridGaleria.removeAll();
        for (ImagenModulo img : imagenes) gridGaleria.add(crearTarjetaImagen(img));
        gridGaleria.revalidate();
        gridGaleria.repaint();
    }

    private JPanel crearTarjetaImagen(ImagenModulo img) {
        JPanel card = EstilosRV.crearPanelTarjeta(new BorderLayout(14, 0));
        card.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        card.setPreferredSize(new Dimension(420, 130));

        JLabel preview = new JLabel();
        preview.setHorizontalAlignment(SwingConstants.CENTER);
        preview.setPreferredSize(new Dimension(120, 90));
        ImageIcon icon = EstilosRV.cargarIcono(img.archivo, 112, 76);
        if (icon != null) preview.setIcon(icon);
        else {
            preview.setText("Sin imagen");
            preview.setForeground(EstilosRV.ROJO);
            preview.setBorder(BorderFactory.createLineBorder(EstilosRV.BORDE));
        }
        card.add(preview, BorderLayout.WEST);

        JPanel data = new JPanel(new GridLayout(4,1,0,3));
        data.setOpaque(false);
        JLabel nombre = new JLabel(img.nombre);
        nombre.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nombre.setForeground(new Color(15, 23, 42));
        JLabel archivo = new JLabel(img.archivo);
        archivo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        archivo.setForeground(EstilosRV.ROJO);
        JLabel uso = new JLabel("<html><span style='color:#475569;'>" + img.uso + "</span></html>");
        uso.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel estado = new JLabel(new File(img.archivo).exists() ? "Disponible" : "No encontrado");
        estado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        estado.setForeground(new File(img.archivo).exists() ? EstilosRV.VERDE : EstilosRV.ROJO);
        data.add(nombre); data.add(archivo); data.add(uso); data.add(estado);
        card.add(data, BorderLayout.CENTER);
        return card;
    }

    private void seleccionarImagen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar imagen");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "png", "jpg", "jpeg", "webp", "bmp"));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            archivoTemporalSeleccionado = chooser.getSelectedFile();
            txtArchivoSeleccionado.setText(archivoTemporalSeleccionado.getAbsolutePath());
            ImageIcon icono = cargarPreviewLibre(archivoTemporalSeleccionado, 180, 140);
            if (icono != null) {
                lblPreviewNueva.setText("");
                lblPreviewNueva.setIcon(icono);
            } else {
                lblPreviewNueva.setIcon(null);
                lblPreviewNueva.setText("Archivo no válido");
            }
        }
    }

    private ImageIcon cargarPreviewLibre(File archivo, int ancho, int alto) {
        try {
            BufferedImage img = ImageIO.read(archivo);
            if (img == null) return null;
            Image escalada = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(escalada);
        } catch (IOException e) {
            return null;
        }
    }

    private void guardarImagenEnDestino() {
        ImagenModulo destino = (ImagenModulo) cboDestinos.getSelectedItem();
        if (destino == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un destino para la imagen.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (archivoTemporalSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Primero selecciona una imagen desde tu computadora.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            BufferedImage imagen = ImageIO.read(archivoTemporalSeleccionado);
            if (imagen == null) throw new IOException("El archivo seleccionado no es una imagen compatible.");
            File archivoDestino = new File(destino.archivo);
            File directorio = archivoDestino.getParentFile();
            if (directorio != null && !directorio.exists()) directorio.mkdirs();
            ImageIO.write(imagen, "png", archivoDestino);
            JOptionPane.showMessageDialog(this,
                    "Imagen guardada correctamente en:\n" + archivoDestino.getPath() + "\n\nNombre usado: " + archivoDestino.getName(),
                    "Imagen actualizada", JOptionPane.INFORMATION_MESSAGE);
            reconstruirGaleria();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la imagen.\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirCarpetaAssets() {
        try {
            Desktop.getDesktop().open(new File("assets"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo abrir la carpeta assets automáticamente.\nRuta: assets",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
