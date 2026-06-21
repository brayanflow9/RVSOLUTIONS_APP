package vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackgroundPanel extends JPanel {
    private BufferedImage imagen;

    public BackgroundPanel(String rutaImagen) {
        setBackground(EstilosRV.FONDO_APP);
        if (rutaImagen != null && !rutaImagen.isBlank()) {
            try {
                File archivo = new File(rutaImagen);
                if (archivo.exists()) {
                    imagen = ImageIO.read(archivo);
                }
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (imagen != null) {
            g2.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            g2.setColor(new Color(235, 238, 243, 125));
            g2.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g2.setColor(new Color(221, 225, 230));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        g2.dispose();
    }
}
