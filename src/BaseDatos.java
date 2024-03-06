import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 * Esta es la clase en donde se realiza la conexion a la base de datos en la nube
 * @author: Shamyr Quishpe
 * @version: 2023-B
 */
public class BaseDatos {

    /**
     * Esta es una funcion de tipo conecction en donde se define la ruta, el usuario y la contrase;a de la base de datos
     * @author: Shamyr Quishpe
     * @version: 2023-B
     */
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