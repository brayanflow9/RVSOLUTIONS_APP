package modelo;
public class Pago {
    private int idPago,idOrden; private String codigoPago,codigoOrden,ordenDescripcion,metodoPago,numeroOperacion,fechaPago,estado,observacion; private double monto;
    public Pago(){} public Pago(int o,double m,String mp,String f,String e){idOrden=o;monto=m;metodoPago=mp;fechaPago=f;estado=e;}
    public int getIdPago(){return idPago;} public void setIdPago(int v){idPago=v;} public int getIdOrden(){return idOrden;} public void setIdOrden(int v){idOrden=v;}
    public String getCodigoPago(){return codigoPago;} public void setCodigoPago(String v){codigoPago=v;} public String getCodigoOrden(){return codigoOrden;} public void setCodigoOrden(String v){codigoOrden=v;}
    public String getOrdenDescripcion(){return ordenDescripcion;} public void setOrdenDescripcion(String v){ordenDescripcion=v;} public double getMonto(){return monto;} public void setMonto(double v){monto=v;}
    public String getMetodoPago(){return metodoPago;} public void setMetodoPago(String v){metodoPago=v;} public String getNumeroOperacion(){return numeroOperacion;} public void setNumeroOperacion(String v){numeroOperacion=v;}
    public String getFechaPago(){return fechaPago;} public void setFechaPago(String v){fechaPago=v;} public String getEstado(){return estado;} public void setEstado(String v){estado=v;} public String getObservacion(){return observacion;} public void setObservacion(String v){observacion=v;}
}
