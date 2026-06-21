package controlador;
import dao.UsuarioDAO; import modelo.Usuario; import java.util.List;
public class UsuarioController {
 private final UsuarioDAO usuarioDAO=new UsuarioDAO();
 public Usuario iniciarSesion(String usuario,String clave){if(usuario==null||usuario.isBlank()||clave==null||clave.isBlank())return null;return usuarioDAO.validarLogin(usuario.trim(),clave.trim());}
 public boolean registrarUsuario(String dni,String nombre,String usuario,String clave,String rol,String correo,String telefono,String estado){if(!validar(dni,nombre,usuario,rol,estado)||clave==null||clave.isBlank())return false;return usuarioDAO.registrar(new Usuario(dni.trim(),nombre.trim(),usuario.trim(),clave.trim(),rol,correo,telefono,estado));}
 public boolean registrarUsuario(String nombre,String usuario,String clave,String rol,String estado){return false;}
 public List<Usuario> listarUsuarios(){return usuarioDAO.listar();}
 public boolean actualizarUsuario(int id,String dni,String nombre,String usuario,String clave,String rol,String correo,String telefono,String estado){if(id<=0||!validar(dni,nombre,usuario,rol,estado))return false;return usuarioDAO.actualizar(new Usuario(id,dni.trim(),nombre.trim(),usuario.trim(),clave==null?"":clave.trim(),rol,correo,telefono,estado));}
 public boolean actualizarUsuario(int id,String nombre,String usuario,String clave,String rol,String estado){return false;}
 public boolean eliminarUsuario(int id,int sesion){return id>0&&id!=sesion&&usuarioDAO.eliminar(id);}
 public boolean esAdministrador(Usuario u){return u!=null&&"Administrador".equalsIgnoreCase(u.getRol());}
 public boolean esTecnico(Usuario u){return u!=null&&"Tecnico".equalsIgnoreCase(u.getRol());}
 private boolean validar(String dni,String n,String u,String r,String e){return dni!=null&&dni.matches("\\d{8}")&&n!=null&&!n.isBlank()&&u!=null&&!u.isBlank()&&r!=null&&!r.isBlank()&&e!=null&&!e.isBlank();}
}
