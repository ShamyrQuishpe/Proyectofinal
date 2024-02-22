import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class BaseDatos {
    public Connection conexionBase() {
        String url = "jdbc:mysql://unkwsyn6m8q9ewqg:BR3VheGqPt7T416JvL7F@by4eyuiz64tfufcme0ay-mysql.services.clever-cloud.com:3306/by4eyuiz64tfufcme0ay";
        String usuarioDB = "unkwsyn6m8q9ewqg";
        String contrasenaDB = "BR3VheGqPt7T416JvL7F";

        Connection conexion = null;

        try{
            conexion = DriverManager.getConnection(url, usuarioDB, contrasenaDB);
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al conectar con la base");
        }
        return conexion;
    }
}