package modelo;
public class Usuario {
    private int idUsuario; private String dni; private String nombre; private String usuario; private String clave; private String rol; private String correo; private String telefono; private String estado;
    public Usuario() {}
    public Usuario(int idUsuario,String dni,String nombre,String usuario,String clave,String rol,String correo,String telefono,String estado){this.idUsuario=idUsuario;this.dni=dni;this.nombre=nombre;this.usuario=usuario;this.clave=clave;this.rol=rol;this.correo=correo;this.telefono=telefono;this.estado=estado;}
    public Usuario(String dni,String nombre,String usuario,String clave,String rol,String correo,String telefono,String estado){this(0,dni,nombre,usuario,clave,rol,correo,telefono,estado);}
    public Usuario(int idUsuario,String nombre,String usuario,String clave,String rol,String estado){this(idUsuario,"",nombre,usuario,clave,rol,"","",estado);}
    public Usuario(String nombre,String usuario,String clave,String rol,String estado){this(0,"",nombre,usuario,clave,rol,"","",estado);}
    public int getIdUsuario(){return idUsuario;} public void setIdUsuario(int v){idUsuario=v;}
    public String getDni(){return dni;} public void setDni(String v){dni=v;}
    public String getNombre(){return nombre;} public void setNombre(String v){nombre=v;}
    public String getUsuario(){return usuario;} public void setUsuario(String v){usuario=v;}
    public String getClave(){return clave;} public void setClave(String v){clave=v;}
    public String getRol(){return rol;} public void setRol(String v){rol=v;}
    public String getCorreo(){return correo;} public void setCorreo(String v){correo=v;}
    public String getTelefono(){return telefono;} public void setTelefono(String v){telefono=v;}
    public String getEstado(){return estado;} public void setEstado(String v){estado=v;}
}
