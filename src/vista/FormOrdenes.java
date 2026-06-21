package vista;

import controlador.OrdenServicioController;
import modelo.OrdenServicio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FormOrdenes extends JFrame {
    private static final Color FONDO = new Color(8, 26, 48);
    private static final Color PANEL = new Color(12, 34, 60);
    private static final Color BORDE = new Color(38, 65, 94);
    private static final Color TEXTO = new Color(241, 245, 249);
    private static final Color TEXTO_2 = new Color(166, 187, 210);

    private final OrdenServicioController controller = new OrdenServicioController();
    private JTextField txtIdOrden, txtIdCliente, txtIdEquipo, txtIdTecnico, txtFecha;
    private JTextArea txtProblema;
    private JComboBox<String> cboEstado;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblTotal, lblPendientes, lblProceso, lblFinalizadas, lblResultados;

    public FormOrdenes() {
        setTitle("RV Solutions - Órdenes y Servicios");
        setSize(1380, 820);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        EstilosRV.aplicarIconoVentana(this);
        inicializarCampos();
        inicializarVista();
        listar();
    }

    private void inicializarCampos() {
        txtIdOrden = new JTextField(); txtIdOrden.setEditable(false);
        txtIdCliente = new JTextField();
        txtIdEquipo = new JTextField();
        txtIdTecnico = new JTextField();
        txtFecha = new JTextField(LocalDate.now().toString());
        txtProblema = new JTextArea(5, 30);
        cboEstado = new JComboBox<>(new String[]{"Pendiente", "En proceso", "Finalizado"});
        for (JTextField c : new JTextField[]{txtIdOrden, txtIdCliente, txtIdEquipo, txtIdTecnico, txtFecha}) EstilosRV.estilizarCampo(c);
        EstilosRV.estilizarAreaTexto(txtProblema);
        EstilosRV.estilizarCombo(cboEstado);
    }

    private void inicializarVista() {
        BackgroundPanel fondo = new BackgroundPanel("assets/fondos/tecnico.png");
        fondo.setLayout(new BorderLayout());
        fondo.setBorder(new EmptyBorder(10, 12, 16, 12));
        DarkPanel principal = new DarkPanel(new BorderLayout(0, 16), FONDO, 24);
        principal.setBorder(new EmptyBorder(18, 20, 18, 20));
        fondo.add(principal, BorderLayout.CENTER);
        principal.add(crearCabecera(), BorderLayout.NORTH);
        principal.add(crearCentro(), BorderLayout.CENTER);
        setContentPane(fondo);
    }

    private JComponent crearCabecera() {
        JPanel cont = new JPanel(new BorderLayout(18, 14)); cont.setOpaque(false);
        JPanel titulo = new JPanel(new BorderLayout(12, 0)); titulo.setOpaque(false);
        JLabel icono = new JLabel(EstilosRV.cargarIcono("assets/tecnico_icons/ordenes.png", 48, 48));
        titulo.add(icono, BorderLayout.WEST);
        JPanel textos = new JPanel(); textos.setOpaque(false); textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel t = new JLabel("Órdenes y Servicios"); t.setForeground(TEXTO); t.setFont(new Font("Segoe UI", Font.BOLD, 23));
        JLabel s = new JLabel("Administra las órdenes técnicas, los equipos asignados y el avance del servicio."); s.setForeground(TEXTO_2); s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textos.add(t); textos.add(Box.createVerticalStrut(4)); textos.add(s); titulo.add(textos, BorderLayout.CENTER); cont.add(titulo, BorderLayout.WEST);
        JPanel kpis = new JPanel(new GridLayout(1, 4, 10, 0)); kpis.setOpaque(false);
        lblTotal = new JLabel("0"); lblPendientes = new JLabel("0"); lblProceso = new JLabel("0"); lblFinalizadas = new JLabel("0");
        kpis.add(kpi("assets/tecnico_icons/ordenes.png", "Total órdenes", lblTotal, "Registradas"));
        kpis.add(kpi("assets/tecnico_icons/calendario.png", "Pendientes", lblPendientes, "Por atender"));
        kpis.add(kpi("assets/tecnico_equipos/revision.png", "En proceso", lblProceso, "En ejecución"));
        kpis.add(kpi("assets/tecnico_equipos/reparado.png", "Finalizadas", lblFinalizadas, "Completadas"));
        cont.add(kpis, BorderLayout.CENTER);
        return cont;
    }

    private JPanel kpi(String ruta, String titulo, JLabel valor, String detalle) {
        DarkPanel p = new DarkPanel(new BorderLayout(12,0), PANEL, 18); p.setBorder(new EmptyBorder(12,14,12,14));
        p.add(new JLabel(EstilosRV.cargarIcono(ruta, 46, 46)), BorderLayout.WEST);
        JPanel tx = new JPanel(); tx.setOpaque(false); tx.setLayout(new BoxLayout(tx, BoxLayout.Y_AXIS));
        JLabel a=new JLabel(titulo); a.setForeground(new Color(220,230,241)); a.setFont(new Font("Segoe UI",Font.PLAIN,12));
        valor.setForeground(Color.WHITE); valor.setFont(new Font("Segoe UI",Font.BOLD,28));
        JLabel d=new JLabel(detalle); d.setForeground(TEXTO_2); d.setFont(new Font("Segoe UI",Font.PLAIN,11));
        tx.add(a); tx.add(valor); tx.add(d); p.add(tx,BorderLayout.CENTER); return p;
    }

    private JComponent crearCentro() {
        JPanel c = new JPanel(new BorderLayout(0,12)); c.setOpaque(false);
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); barra.setOpaque(false);
        JButton nuevo=boton("+ Nuevo", EstilosRV.ROJO, Color.WHITE);
        JButton editar=boton("Editar", PANEL, TEXTO);
        JButton eliminar=boton("Eliminar", PANEL, new Color(255,92,103));
        JButton exportar=boton("Exportar reporte", PANEL, TEXTO);
        nuevo.addActionListener(e->{limpiar(); mostrarFormulario(false);});
        editar.addActionListener(e->mostrarFormulario(true));
        eliminar.addActionListener(e->eliminar());
        exportar.addActionListener(e->ReporteUtil.exportarTablaCSV(this,tabla,"reporte_ordenes"));
        barra.add(nuevo); barra.add(editar); barra.add(eliminar); barra.add(exportar); c.add(barra,BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"Código orden","DNI cliente","Cliente","Código equipo","Equipo","DNI técnico","Técnico","Fecha","Problema","Estado","ID Orden","ID Cliente","ID Equipo","ID Técnico"},0){public boolean isCellEditable(int r,int col){return false;}};
        tabla = new JTable(modelo); configurarTabla();
        JScrollPane sp=new JScrollPane(tabla); sp.setBorder(BorderFactory.createLineBorder(BORDE)); sp.getViewport().setBackground(FONDO); c.add(sp,BorderLayout.CENTER);
        JPanel pie=new JPanel(new BorderLayout()); pie.setOpaque(false); lblResultados=new JLabel("Mostrando 0 órdenes"); lblResultados.setForeground(TEXTO_2); pie.add(lblResultados,BorderLayout.WEST);
        JLabel pag=new JLabel("8 por página     ‹   1   2   3   ›"); pag.setForeground(TEXTO_2); pie.add(pag,BorderLayout.EAST); c.add(pie,BorderLayout.SOUTH);
        return c;
    }

    private JButton boton(String texto, Color bg, Color fg) { return new RoundedButton(texto,bg,fg,BORDE,18); }

    private void configurarTabla() {
        tabla.setRowHeight(42); tabla.setBackground(FONDO); tabla.setForeground(TEXTO); tabla.setSelectionBackground(new Color(25,55,84)); tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(new Color(28,53,79)); tabla.setShowVerticalLines(true); tabla.setShowHorizontalLines(true); tabla.setFont(new Font("Segoe UI",Font.PLAIN,12));
        tabla.getTableHeader().setBackground(new Color(10,29,51)); tabla.getTableHeader().setForeground(Color.WHITE); tabla.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,12)); tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getColumnModel().getColumn(9).setCellRenderer(new EstadoRenderer());
        for(int i=0;i<4;i++) tabla.removeColumn(tabla.getColumnModel().getColumn(10));
        tabla.getSelectionModel().addListSelectionListener(e->{ if(!e.getValueIsAdjusting()) seleccionar(); });
    }

    private void listar() {
        modelo.setRowCount(0); List<OrdenServicio> lista=controller.listarOrdenes(); int pen=0,pro=0,fin=0;
        for(OrdenServicio o:lista){
            modelo.addRow(new Object[]{o.getCodigoOrden(),o.getClienteDni(),o.getClienteNombre(),o.getCodigoEquipo(),o.getEquipoNombre(),o.getTecnicoDni(),o.getTecnicoNombre(),o.getFechaRegistro(),o.getProblemaReportado(),o.getEstado(),o.getIdOrden(),o.getIdCliente(),o.getIdEquipo(),o.getIdTecnico()});
            String st=o.getEstado()==null?"":o.getEstado().toLowerCase(); if(st.contains("pendiente"))pen++; else if(st.contains("proceso"))pro++; else if(st.contains("final"))fin++;
        }
        lblTotal.setText(String.valueOf(lista.size())); lblPendientes.setText(String.valueOf(pen)); lblProceso.setText(String.valueOf(pro)); lblFinalizadas.setText(String.valueOf(fin)); lblResultados.setText("Mostrando "+lista.size()+" órdenes");
    }

    private void seleccionar() {
        int r=tabla.getSelectedRow(); if(r<0)return; r=tabla.convertRowIndexToModel(r);
        txtIdOrden.setText(String.valueOf(modelo.getValueAt(r,10))); txtIdCliente.setText(String.valueOf(modelo.getValueAt(r,11))); txtIdEquipo.setText(String.valueOf(modelo.getValueAt(r,12))); txtIdTecnico.setText(String.valueOf(modelo.getValueAt(r,13)));
        txtFecha.setText(String.valueOf(modelo.getValueAt(r,7))); txtProblema.setText(String.valueOf(modelo.getValueAt(r,8))); cboEstado.setSelectedItem(modelo.getValueAt(r,9));
    }

    private JPanel crearFormularioPanel() {
        JPanel p=new JPanel(new GridBagLayout()); p.setOpaque(false); GridBagConstraints g=new GridBagConstraints(); g.insets=new Insets(7,7,7,7); g.fill=GridBagConstraints.HORIZONTAL; g.weightx=1;
        String[] ls={"ID Orden:","ID Cliente:","ID Equipo:","ID Técnico:","Fecha:","Estado:"}; JComponent[] cs={txtIdOrden,txtIdCliente,txtIdEquipo,txtIdTecnico,txtFecha,cboEstado};
        for(int i=0;i<ls.length;i++){g.gridx=(i%2)*2;g.gridy=i/2;JLabel l=new JLabel(ls[i]);EstilosRV.estilizarEtiqueta(l);p.add(l,g);g.gridx++;p.add(cs[i],g);} g.gridx=0;g.gridy=3;g.gridwidth=1;JLabel lp=new JLabel("Problema reportado:");EstilosRV.estilizarEtiqueta(lp);p.add(lp,g);g.gridx=1;g.gridwidth=3;g.fill=GridBagConstraints.BOTH;g.weighty=1;JScrollPane sp=new JScrollPane(txtProblema);EstilosRV.estilizarScroll(sp);p.add(sp,g); return p;
    }

    private void mostrarFormulario(boolean editar) {
        if(editar && txtIdOrden.getText().isBlank()){JOptionPane.showMessageDialog(ventanaVisible(),"Seleccione una orden para actualizar.");return;}
        Window owner=SwingUtilities.getWindowAncestor(tabla); JDialog d=owner instanceof Frame?new JDialog((Frame)owner,editar?"Actualizar orden":"Nueva orden",true):new JDialog((Frame)null,editar?"Actualizar orden":"Nueva orden",true);
        d.setUndecorated(true); JPanel cont=new EstilosRV.RoundedPanel(new BorderLayout(0,16),Color.WHITE,28);cont.setBorder(new EmptyBorder(24,26,22,26));
        JPanel cab=new JPanel(new BorderLayout());cab.setOpaque(false);JLabel t=new JLabel(editar?"Actualizar Orden de Servicio":"Nueva Orden de Servicio");t.setFont(new Font("Segoe UI",Font.BOLD,24));t.setForeground(new Color(15,23,42));JButton x=new JButton("×");x.setFont(new Font("Segoe UI",Font.PLAIN,24));x.setBorderPainted(false);x.setContentAreaFilled(false);x.addActionListener(e->d.dispose());cab.add(t,BorderLayout.WEST);cab.add(x,BorderLayout.EAST);cont.add(cab,BorderLayout.NORTH);cont.add(crearFormularioPanel(),BorderLayout.CENTER);
        JPanel acc=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0));acc.setOpaque(false);JButton cancel=EstilosRV.crearBotonRedondeado("Cancelar",false);JButton save=EstilosRV.crearBotonRedondeado(editar?"Actualizar":"Guardar",true);cancel.addActionListener(e->d.dispose());save.addActionListener(e->{boolean ok=editar?actualizar():guardar();if(ok)d.dispose();});acc.add(cancel);acc.add(save);cont.add(acc,BorderLayout.SOUTH);
        d.setContentPane(cont);d.setSize(780,540);d.setLocationRelativeTo(owner);d.setVisible(true);
    }

    private boolean guardar(){try{boolean ok=controller.registrarOrden(Integer.parseInt(txtIdCliente.getText().trim()),Integer.parseInt(txtIdEquipo.getText().trim()),Integer.parseInt(txtIdTecnico.getText().trim()),txtFecha.getText().trim(),txtProblema.getText().trim(),String.valueOf(cboEstado.getSelectedItem()));JOptionPane.showMessageDialog(ventanaVisible(),ok?"Orden registrada correctamente.":"No se pudo registrar. Verifique los datos.");if(ok){limpiar();listar();}return ok;}catch(NumberFormatException ex){JOptionPane.showMessageDialog(ventanaVisible(),"Los ID deben ser numéricos.");return false;}}
    private boolean actualizar(){try{boolean ok=controller.actualizarOrden(Integer.parseInt(txtIdOrden.getText()),Integer.parseInt(txtIdCliente.getText().trim()),Integer.parseInt(txtIdEquipo.getText().trim()),Integer.parseInt(txtIdTecnico.getText().trim()),txtFecha.getText().trim(),txtProblema.getText().trim(),String.valueOf(cboEstado.getSelectedItem()));JOptionPane.showMessageDialog(ventanaVisible(),ok?"Orden actualizada correctamente.":"No se pudo actualizar.");if(ok){limpiar();listar();}return ok;}catch(NumberFormatException ex){JOptionPane.showMessageDialog(ventanaVisible(),"Los ID deben ser numéricos.");return false;}}
    private void eliminar(){if(txtIdOrden.getText().isBlank()){JOptionPane.showMessageDialog(ventanaVisible(),"Seleccione una orden para eliminar.");return;}if(JOptionPane.showConfirmDialog(ventanaVisible(),"¿Eliminar esta orden?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){boolean ok=controller.eliminarOrden(Integer.parseInt(txtIdOrden.getText()));JOptionPane.showMessageDialog(ventanaVisible(),ok?"Orden eliminada.":"No se pudo eliminar.");if(ok){limpiar();listar();}}}
    private void limpiar(){txtIdOrden.setText("");txtIdCliente.setText("");txtIdEquipo.setText("");txtIdTecnico.setText("");txtFecha.setText(LocalDate.now().toString());txtProblema.setText("");cboEstado.setSelectedIndex(0);if(tabla!=null)tabla.clearSelection();}
    private Component ventanaVisible(){Window w=tabla==null?null:SwingUtilities.getWindowAncestor(tabla);return w==null?this:w;}

    private static class DarkPanel extends JPanel {private final Color bg;private final int r;DarkPanel(LayoutManager l,Color bg,int r){super(l);this.bg=bg;this.r=r;setOpaque(false);}protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(new Color(0,0,0,45));g2.fillRoundRect(3,5,getWidth()-6,getHeight()-7,r,r);g2.setColor(bg);g2.fillRoundRect(0,0,getWidth()-4,getHeight()-6,r,r);g2.setColor(BORDE);g2.drawRoundRect(0,0,getWidth()-5,getHeight()-7,r,r);g2.dispose();super.paintComponent(g);}}
    private static class RoundedButton extends JButton {private final Color bg,bd;private final int r;RoundedButton(String t,Color bg,Color fg,Color bd,int r){super(t);this.bg=bg;this.bd=bd;this.r=r;setForeground(fg);setFont(new Font("Segoe UI",Font.BOLD,12));setFocusPainted(false);setContentAreaFilled(false);setBorderPainted(false);setCursor(new Cursor(Cursor.HAND_CURSOR));}public Insets getInsets(){return new Insets(10,16,10,16);}protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(getModel().isRollover()?bg.brighter():bg);g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r);g2.setColor(bd);g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,r,r);g2.dispose();super.paintComponent(g);}}
    private static class EstadoRenderer extends DefaultTableCellRenderer {public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){JPanel p=new JPanel(new GridBagLayout());p.setBackground(s?t.getSelectionBackground():FONDO);String x=v==null?"":v.toString();Color col=x.toLowerCase().contains("final")?new Color(31,167,84):x.toLowerCase().contains("proceso")?new Color(23,112,205):new Color(224,139,0);JLabel l=new JLabel(x);l.setOpaque(true);l.setBackground(col);l.setForeground(Color.WHITE);l.setFont(new Font("Segoe UI",Font.BOLD,11));l.setBorder(new EmptyBorder(6,12,6,12));p.add(l);return p;}}
}
