package modelo;
public class Diagnostico {
    private int idDiagnostico,idOrden,idTecnico; private String codigoDiagnostico,codigoOrden,tecnicoDni,tecnicoNombre,descripcionDiagnostico,solucionAplicada,repuestosUtilizados,fechaDiagnostico,estado;
    public Diagnostico(){}
    public int getIdDiagnostico(){return idDiagnostico;} public void setIdDiagnostico(int v){idDiagnostico=v;}
    public int getIdOrden(){return idOrden;} public void setIdOrden(int v){idOrden=v;}
    public int getIdTecnico(){return idTecnico;} public void setIdTecnico(int v){idTecnico=v;}
    public String getCodigoDiagnostico(){return codigoDiagnostico;} public void setCodigoDiagnostico(String v){codigoDiagnostico=v;}
    public String getCodigoOrden(){return codigoOrden;} public void setCodigoOrden(String v){codigoOrden=v;}
    public String getTecnicoDni(){return tecnicoDni;} public void setTecnicoDni(String v){tecnicoDni=v;}
    public String getTecnicoNombre(){return tecnicoNombre;} public void setTecnicoNombre(String v){tecnicoNombre=v;}
    public String getDescripcionDiagnostico(){return descripcionDiagnostico;} public void setDescripcionDiagnostico(String v){descripcionDiagnostico=v;}
    public String getSolucionAplicada(){return solucionAplicada;} public void setSolucionAplicada(String v){solucionAplicada=v;}
    public String getRepuestosUtilizados(){return repuestosUtilizados;} public void setRepuestosUtilizados(String v){repuestosUtilizados=v;}
    public String getFechaDiagnostico(){return fechaDiagnostico;} public void setFechaDiagnostico(String v){fechaDiagnostico=v;}
    public String getEstado(){return estado;} public void setEstado(String v){estado=v;}
}
