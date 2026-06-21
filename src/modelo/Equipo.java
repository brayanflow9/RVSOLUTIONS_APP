package modelo;
public class Equipo {
    private int idEquipo,idCliente; private String codigoEquipo,clienteDni,clienteNombre,tipoEquipo,marca,modelo,serie,descripcion,estado,fechaRegistro;
    public Equipo(){}
    public Equipo(int idCliente,String tipo,String marca,String modelo,String serie,String descripcion,String estado){this.idCliente=idCliente;this.tipoEquipo=tipo;this.marca=marca;this.modelo=modelo;this.serie=serie;this.descripcion=descripcion;this.estado=estado;}
    public int getIdEquipo(){return idEquipo;} public void setIdEquipo(int v){idEquipo=v;}
    public int getIdCliente(){return idCliente;} public void setIdCliente(int v){idCliente=v;}
    public String getCodigoEquipo(){return codigoEquipo;} public void setCodigoEquipo(String v){codigoEquipo=v;}
    public String getClienteDni(){return clienteDni;} public void setClienteDni(String v){clienteDni=v;}
    public String getClienteNombre(){return clienteNombre;} public void setClienteNombre(String v){clienteNombre=v;}
    public String getTipoEquipo(){return tipoEquipo;} public void setTipoEquipo(String v){tipoEquipo=v;}
    public String getMarca(){return marca;} public void setMarca(String v){marca=v;}
    public String getModelo(){return modelo;} public void setModelo(String v){modelo=v;}
    public String getSerie(){return serie;} public void setSerie(String v){serie=v;}
    public String getDescripcion(){return descripcion;} public void setDescripcion(String v){descripcion=v;}
    public String getEstado(){return estado;} public void setEstado(String v){estado=v;}
    public String getFechaRegistro(){return fechaRegistro;} public void setFechaRegistro(String v){fechaRegistro=v;}
}
