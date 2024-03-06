import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Esta es la clase que autentica y valida los roles de los usuarios que intentan ingresar al sistema
 * @author: Shamyr Quishpe
 * @version: 2023-B
 */
public class Login extends JFrame {
    private JButton ingresarButton;
    private JTextField userText;
    private JPasswordField passField;
    private JPanel panelito;
    private JComboBox comboBox1;

    public String rol = "user";
    public Login(){
        super("LOGIN");
        setContentPane(panelito);
        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validaciones();
            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = comboBox1.getSelectedIndex();
                // Realizar la acción correspondiente al rol seleccionado
                if (selectedIndex == 0) {
                    // Acción para Usuario
                    rol = "user";
                    System.out.println("Rol seleccionado: Usuario");
                    // Aquí puedes agregar tu lógica para el rol de usuario
                } else if (selectedIndex == 1) {
                    rol = "admin";
                    // Acción para Admin
                    System.out.println("Rol seleccionado: Admin");
                    // Aquí puedes agregar tu lógica para el rol de administrador
                }
            }
        });
    }

    /**
     * Esta es la funcion que permite validar los roles de los usuarios del sistema
     * @author: Shamyr Quishpe
     * @version: 2023-B
     */
    public void validaciones(){
        String usuario = userText.getText();
        String contrasena = new String(passField.getPassword());

        if(autenticarUsuario(usuario,contrasena, rol)){
            JOptionPane.showMessageDialog(null, "Inicio de sesion exitoso");
            Carrito.usuarioObtener(usuario);
            if(rol=="admin"){
                Admin vent_adm = new Admin();
                vent_adm.abrir();
                dispose();
            } else if (rol=="user") {
                Carrito carr = new Carrito();
                carr.iniciar();
                dispose();
            }
        }else{
            JOptionPane.showMessageDialog(null, "Las credenciales o el rol son incorrectos");
            userText.setText("");
            passField.setText("");
        }


    }
    /**
     * Esta es la funcion que permite autenticar el ingreso de los usuarios al sistema
     * @author: Shamyr Quishpe
     * @version: 2023-B
     */
    public boolean autenticarUsuario(String nombre, String contraseña, String rol){ //FALTArol
        BaseDatos manejadorBD = new BaseDatos();
        Connection conexion = manejadorBD.conexionBase();

        if(conexion != null){
            try{
                String sql = "SELECT * FROM usuarios_proyecto WHERE nombre_usuario=? AND contrasena=? AND rol=?"; //FALTArol
                try(PreparedStatement stmt = conexion.prepareStatement(sql)){
                    stmt.setString(1, nombre);
                    stmt.setString(2,contraseña);
                    stmt.setString(3, rol);//FALTArol

                    System.out.println("Consulta sql "+stmt.toString());

                    ResultSet resultSet = stmt.executeQuery();
                    return resultSet.next();
                }
            }catch (SQLException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al ejecutar la consulta");
            }finally {
                try{
                    conexion.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
