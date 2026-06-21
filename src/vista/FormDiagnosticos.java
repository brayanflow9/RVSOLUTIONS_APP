package vista;

import controlador.DiagnosticoController;
import modelo.Diagnostico;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FormDiagnosticos extends JFrame {
    private static final Color FONDO = new Color(8, 26, 48);
    private static final Color PANEL = new Color(12, 34, 60);
    private static final Color BORDE = new Color(38, 65, 94);
    private static final Color TEXTO = new Color(241, 245, 249);
    private static final Color TEXTO_2 = new Color(166, 187, 210);

    private final DiagnosticoController controller = new DiagnosticoController();
    private JTextField txtIdDiagnostico, txtIdOrden, txtFecha;
    private JTextArea txtDescripcion, txtSolucion, txtRepuestos;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblTotal, lblConSolucion, lblConRepuestos, lblHoy, lblResultados;

    public FormDiagnosticos() {
        setTitle("RV Solutions - Diagnósticos Técnicos");
        setSize(1380, 820);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        EstilosRV.aplicarIconoVentana(this);
        inicializarCampos();
        inicializarVista();
        listar();
    }

    private void inicializarCampos() {
        txtIdDiagnostico = new JTextField(); txtIdDiagnostico.setEditable(false);
        txtIdOrden = new JTextField();
        txtFecha = new JTextField(LocalDate.now().toString());
        txtDescripcion = new JTextArea(4, 28);
        txtSolucion = new JTextArea(4, 28);
        txtRepuestos = new JTextArea(3, 28);
        for (JTextField c : new JTextField[]{txtIdDiagnostico, txtIdOrden, txtFecha}) EstilosRV.estilizarCampo(c);
        for (JTextArea a : new JTextArea[]{txtDescripcion, txtSolucion, txtRepuestos}) EstilosRV.estilizarAreaTexto(a);
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
        titulo.add(new JLabel(EstilosRV.cargarIcono("assets/tecnico_icons/diagnosticos.png", 48, 48)), BorderLayout.WEST);
        JPanel textos = new JPanel(); textos.setOpaque(false); textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel t = new JLabel("Diagnósticos Técnicos"); t.setForeground(TEXTO); t.setFont(new Font("Segoe UI", Font.BOLD, 23));
        JLabel s = new JLabel("Registra el análisis técnico, la solución aplicada y los repuestos utilizados."); s.setForeground(TEXTO_2); s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textos.add(t); textos.add(Box.createVerticalStrut(4)); textos.add(s); titulo.add(textos, BorderLayout.CENTER); cont.add(titulo, BorderLayout.WEST);
        JPanel kpis = new JPanel(new GridLayout(1, 4, 10, 0)); kpis.setOpaque(false);
        lblTotal = new JLabel("0"); lblConSolucion = new JLabel("0"); lblConRepuestos = new JLabel("0"); lblHoy = new JLabel("0");
        kpis.add(kpi("assets/tecnico_icons/diagnosticos.png", "Total diagnósticos", lblTotal, "Registrados"));
        kpis.add(kpi("assets/tecnico_equipos/reparado.png", "Con solución", lblConSolucion, "Solución aplicada"));
        kpis.add(kpi("assets/tecnico_equipos/revision.png", "Con repuestos", lblConRepuestos, "Material registrado"));
        kpis.add(kpi("assets/tecnico_icons/calendario.png", "Registrados hoy", lblHoy, "Fecha actual"));
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
        exportar.addActionListener(e->ReporteUtil.exportarTablaCSV(this,tabla,"reporte_diagnosticos"));
        barra.add(nuevo);barra.add(editar);barra.add(eliminar);barra.add(exportar);c.add(barra,BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"Código diagnóstico","Código orden","DNI técnico","Técnico","Diagnóstico","Solución aplicada","Repuestos utilizados","Fecha","Estado","ID Diagnóstico","ID Orden","ID Técnico"},0){public boolean isCellEditable(int r,int col){return false;}};
        tabla = new JTable(modelo); configurarTabla(); for(int i=0;i<3;i++) tabla.removeColumn(tabla.getColumnModel().getColumn(9)); JScrollPane sp=new JScrollPane(tabla); sp.setBorder(BorderFactory.createLineBorder(BORDE)); sp.getViewport().setBackground(FONDO); c.add(sp,BorderLayout.CENTER);
        JPanel pie=new JPanel(new BorderLayout());pie.setOpaque(false);lblResultados=new JLabel("Mostrando 0 diagnósticos");lblResultados.setForeground(TEXTO_2);pie.add(lblResultados,BorderLayout.WEST);JLabel pag=new JLabel("8 por página     ‹   1   2   3   ›");pag.setForeground(TEXTO_2);pie.add(pag,BorderLayout.EAST);c.add(pie,BorderLayout.SOUTH);
        return c;
    }

    private JButton boton(String texto, Color bg, Color fg) { return new RoundedButton(texto,bg,fg,BORDE,18); }

    private void configurarTabla(){tabla.setRowHeight(42);tabla.setBackground(FONDO);tabla.setForeground(TEXTO);tabla.setSelectionBackground(new Color(25,55,84));tabla.setSelectionForeground(Color.WHITE);tabla.setGridColor(new Color(28,53,79));tabla.setShowVerticalLines(true);tabla.setShowHorizontalLines(true);tabla.setFont(new Font("Segoe UI",Font.PLAIN,12));tabla.getTableHeader().setBackground(new Color(10,29,51));tabla.getTableHeader().setForeground(Color.WHITE);tabla.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,12));tabla.getTableHeader().setReorderingAllowed(false);tabla.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting())seleccionar();});}

    private void listar(){modelo.setRowCount(0);List<Diagnostico> lista=controller.listarDiagnosticos();int sol=0,rep=0,hoy=0;String fechaHoy=LocalDate.now().toString();for(Diagnostico d:lista){modelo.addRow(new Object[]{d.getCodigoDiagnostico(),d.getCodigoOrden(),d.getTecnicoDni(),d.getTecnicoNombre(),d.getDescripcionDiagnostico(),d.getSolucionAplicada(),d.getRepuestosUtilizados(),d.getFechaDiagnostico(),d.getEstado(),d.getIdDiagnostico(),d.getIdOrden(),d.getIdTecnico()});if(noVacio(d.getSolucionAplicada()))sol++;if(noVacio(d.getRepuestosUtilizados()))rep++;if(fechaHoy.equals(d.getFechaDiagnostico()))hoy++;}lblTotal.setText(String.valueOf(lista.size()));lblConSolucion.setText(String.valueOf(sol));lblConRepuestos.setText(String.valueOf(rep));lblHoy.setText(String.valueOf(hoy));lblResultados.setText("Mostrando "+lista.size()+" diagnósticos");}
    private boolean noVacio(String s){return s!=null&&!s.trim().isEmpty();}
    private void seleccionar(){int r=tabla.getSelectedRow();if(r<0)return;r=tabla.convertRowIndexToModel(r);txtIdDiagnostico.setText(valor(r,9));txtIdOrden.setText(valor(r,10));txtDescripcion.setText(valor(r,4));txtSolucion.setText(valor(r,5));txtRepuestos.setText(valor(r,6));txtFecha.setText(valor(r,7));}
    private String valor(int r,int c){Object v=modelo.getValueAt(r,c);return v==null?"":v.toString();}

    private JPanel crearFormularioPanel(){JPanel p=new JPanel(new GridBagLayout());p.setOpaque(false);GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(7,7,7,7);g.fill=GridBagConstraints.HORIZONTAL;g.weightx=1;
        JLabel l1=new JLabel("ID Diagnóstico:");EstilosRV.estilizarEtiqueta(l1);g.gridx=0;g.gridy=0;p.add(l1,g);g.gridx=1;p.add(txtIdDiagnostico,g);JLabel l2=new JLabel("ID Orden:");EstilosRV.estilizarEtiqueta(l2);g.gridx=2;p.add(l2,g);g.gridx=3;p.add(txtIdOrden,g);JLabel l3=new JLabel("Fecha:");EstilosRV.estilizarEtiqueta(l3);g.gridx=0;g.gridy=1;p.add(l3,g);g.gridx=1;p.add(txtFecha,g);
        agregarArea(p,g,2,"Diagnóstico:",txtDescripcion);agregarArea(p,g,3,"Solución aplicada:",txtSolucion);agregarArea(p,g,4,"Repuestos utilizados:",txtRepuestos);return p;}
    private void agregarArea(JPanel p,GridBagConstraints g,int fila,String texto,JTextArea area){g.gridx=0;g.gridy=fila;g.gridwidth=1;g.weighty=0;g.fill=GridBagConstraints.HORIZONTAL;JLabel l=new JLabel(texto);EstilosRV.estilizarEtiqueta(l);p.add(l,g);g.gridx=1;g.gridwidth=3;g.weighty=1;g.fill=GridBagConstraints.BOTH;JScrollPane sp=new JScrollPane(area);EstilosRV.estilizarScroll(sp);p.add(sp,g);}

    private void mostrarFormulario(boolean editar){if(editar&&txtIdDiagnostico.getText().isBlank()){JOptionPane.showMessageDialog(ventanaVisible(),"Seleccione un diagnóstico para actualizar.");return;}Window owner=SwingUtilities.getWindowAncestor(tabla);JDialog d=owner instanceof Frame?new JDialog((Frame)owner,editar?"Actualizar diagnóstico":"Nuevo diagnóstico",true):new JDialog((Frame)null,editar?"Actualizar diagnóstico":"Nuevo diagnóstico",true);d.setUndecorated(true);JPanel cont=new EstilosRV.RoundedPanel(new BorderLayout(0,16),Color.WHITE,28);cont.setBorder(new EmptyBorder(24,26,22,26));JPanel cab=new JPanel(new BorderLayout());cab.setOpaque(false);JLabel t=new JLabel(editar?"Actualizar Diagnóstico":"Nuevo Diagnóstico");t.setFont(new Font("Segoe UI",Font.BOLD,24));t.setForeground(new Color(15,23,42));JButton x=new JButton("×");x.setFont(new Font("Segoe UI",Font.PLAIN,24));x.setBorderPainted(false);x.setContentAreaFilled(false);x.addActionListener(e->d.dispose());cab.add(t,BorderLayout.WEST);cab.add(x,BorderLayout.EAST);cont.add(cab,BorderLayout.NORTH);cont.add(crearFormularioPanel(),BorderLayout.CENTER);JPanel acc=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0));acc.setOpaque(false);JButton cancel=EstilosRV.crearBotonRedondeado("Cancelar",false);JButton save=EstilosRV.crearBotonRedondeado(editar?"Actualizar":"Guardar",true);cancel.addActionListener(e->d.dispose());save.addActionListener(e->{boolean ok=editar?actualizar():guardar();if(ok)d.dispose();});acc.add(cancel);acc.add(save);cont.add(acc,BorderLayout.SOUTH);d.setContentPane(cont);d.setSize(800,620);d.setLocationRelativeTo(owner);d.setVisible(true);}

    private Diagnostico construir(boolean conId){Diagnostico d=new Diagnostico();if(conId)d.setIdDiagnostico(Integer.parseInt(txtIdDiagnostico.getText()));d.setIdOrden(Integer.parseInt(txtIdOrden.getText().trim()));d.setDescripcionDiagnostico(txtDescripcion.getText().trim());d.setSolucionAplicada(txtSolucion.getText().trim());d.setRepuestosUtilizados(txtRepuestos.getText().trim());d.setFechaDiagnostico(txtFecha.getText().trim());d.setIdTecnico(2);d.setEstado("Completado");return d;}
    private boolean guardar(){try{boolean ok=controller.registrarDiagnostico(construir(false));JOptionPane.showMessageDialog(ventanaVisible(),ok?"Diagnóstico registrado correctamente.":"No se pudo registrar. Verifique el ID de orden y la descripción.");if(ok){limpiar();listar();}return ok;}catch(NumberFormatException ex){JOptionPane.showMessageDialog(ventanaVisible(),"El ID de orden debe ser numérico.");return false;}}
    private boolean actualizar(){try{boolean ok=controller.actualizarDiagnostico(construir(true));JOptionPane.showMessageDialog(ventanaVisible(),ok?"Diagnóstico actualizado correctamente.":"No se pudo actualizar.");if(ok){limpiar();listar();}return ok;}catch(NumberFormatException ex){JOptionPane.showMessageDialog(ventanaVisible(),"Los ID deben ser numéricos.");return false;}}
    private void eliminar(){if(txtIdDiagnostico.getText().isBlank()){JOptionPane.showMessageDialog(ventanaVisible(),"Seleccione un diagnóstico para eliminar.");return;}if(JOptionPane.showConfirmDialog(ventanaVisible(),"¿Eliminar este diagnóstico?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){boolean ok=controller.eliminarDiagnostico(Integer.parseInt(txtIdDiagnostico.getText()));JOptionPane.showMessageDialog(ventanaVisible(),ok?"Diagnóstico eliminado.":"No se pudo eliminar.");if(ok){limpiar();listar();}}}
    private void limpiar(){txtIdDiagnostico.setText("");txtIdOrden.setText("");txtFecha.setText(LocalDate.now().toString());txtDescripcion.setText("");txtSolucion.setText("");txtRepuestos.setText("");if(tabla!=null)tabla.clearSelection();}
    private Component ventanaVisible(){Window w=tabla==null?null:SwingUtilities.getWindowAncestor(tabla);return w==null?this:w;}

    private static class DarkPanel extends JPanel {private final Color bg;private final int r;DarkPanel(LayoutManager l,Color bg,int r){super(l);this.bg=bg;this.r=r;setOpaque(false);}protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(new Color(0,0,0,45));g2.fillRoundRect(3,5,getWidth()-6,getHeight()-7,r,r);g2.setColor(bg);g2.fillRoundRect(0,0,getWidth()-4,getHeight()-6,r,r);g2.setColor(BORDE);g2.drawRoundRect(0,0,getWidth()-5,getHeight()-7,r,r);g2.dispose();super.paintComponent(g);}}
    private static class RoundedButton extends JButton {private final Color bg,bd;private final int r;RoundedButton(String t,Color bg,Color fg,Color bd,int r){super(t);this.bg=bg;this.bd=bd;this.r=r;setForeground(fg);setFont(new Font("Segoe UI",Font.BOLD,12));setFocusPainted(false);setContentAreaFilled(false);setBorderPainted(false);setCursor(new Cursor(Cursor.HAND_CURSOR));}public Insets getInsets(){return new Insets(10,16,10,16);}protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(getModel().isRollover()?bg.brighter():bg);g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r);g2.setColor(bd);g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,r,r);g2.dispose();super.paintComponent(g);}}
}
