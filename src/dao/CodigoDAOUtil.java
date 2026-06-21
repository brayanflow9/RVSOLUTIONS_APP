package dao;
import java.sql.*; import java.time.Year;
final class CodigoDAOUtil {
 private CodigoDAOUtil(){}
 static String siguiente(Connection con,String tabla,String columna,String prefijo) throws SQLException {
   int anio=Year.now().getValue(); String patron=prefijo+"-"+anio+"-%";
   String sql="SELECT COALESCE(MAX(CAST(SUBSTRING_INDEX("+columna+", '-', -1) AS UNSIGNED)),0)+1 n FROM "+tabla+" WHERE "+columna+" LIKE ?";
   try(PreparedStatement ps=con.prepareStatement(sql)){ps.setString(1,patron);try(ResultSet rs=ps.executeQuery()){rs.next();return String.format("%s-%d-%06d",prefijo,anio,rs.getInt("n"));}}
 }
}
