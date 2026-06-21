package modelo;
public class OrdenServicio {
    private int idOrden,idCliente,idEquipo,idTecnico; private String codigoOrden,clienteDni,clienteNombre,codigoEquipo,equipoNombre,tecnicoDni,tecnicoNombre,fechaRegistro,problemaReportado,observaciones,estado;
    public OrdenServicio(){}
    public OrdenServicio(int c,int e,int t,String f,String p,String s){idCliente=c;idEquipo=e;idTecnico=t;fechaRegistro=f;problemaReportado=p;estado=s;}
    public int getIdOrden(){return idOrden;} public void setIdOrden(int v){idOrden=v;}
    public int getIdCliente(){return idCliente;} public void setIdCliente(int v){idCliente=v;}
    public int getIdEquipo(){return idEquipo;} public void setIdEquipo(int v){idEquipo=v;}
    public int getIdTecnico(){return idTecnico;} public void setIdTecnico(int v){idTecnico=v;}
    public String getCodigoOrden(){return codigoOrden;} public void setCodigoOrden(String v){codigoOrden=v;}
    public String getClienteDni(){return clienteDni;} public void setClienteDni(String v){clienteDni=v;}
    public String getClienteNombre(){return clienteNombre;} public void setClienteNombre(String v){clienteNombre=v;}
    public String getCodigoEquipo(){return codigoEquipo;} public void setCodigoEquipo(String v){codigoEquipo=v;}
    public String getEquipoNombre(){return equipoNombre;} public void setEquipoNombre(String v){equipoNombre=v;}
    public String getTecnicoDni(){return tecnicoDni;} public void setTecnicoDni(String v){tecnicoDni=v;}
    public String getTecnicoNombre(){return tecnicoNombre;} public void setTecnicoNombre(String v){tecnicoNombre=v;}
    public String getFechaRegistro(){return fechaRegistro;} public void setFechaRegistro(String v){fechaRegistro=v;}
    public String getProblemaReportado(){return problemaReportado;} public void setProblemaReportado(String v){problemaReportado=v;}
    public String getObservaciones(){return observaciones;} public void setObservaciones(String v){observaciones=v;}
    public String getEstado(){return estado;} public void setEstado(String v){estado=v;}
}
