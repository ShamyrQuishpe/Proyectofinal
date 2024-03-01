import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class Adduser extends JFrame {
    private JPanel adduserPanel;
    private JButton agregarButton;
    private JButton MENUButton;
    private JTextField nombreField;
    private JTextField nombreUField;
    private JTextField paswdField;
    private JTextField cedulaField;
    private JComboBox comboBox1;

    private String rol = "user";
    public Adduser(){
        super("AGREGAR USUARIO");
        setContentPane(adduserPanel);
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ingresoFormulario();
            }
        });

        MENUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Admin vent_admin1 = new Admin();
                vent_admin1.abrir();
                dispose();
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
    public void abrir(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,400);
        setLocationRelativeTo(null);
        setVisible(true);
        this.getContentPane().setBackground(new Color(234, 211, 186));
    }

    public void ingresoFormulario(){
        String nombre = nombreField.getText();
        String nusuario = nombreUField.getText();
        String contrsaña_reg = paswdField.getText();
        String cedula = cedulaField.getText();

        agregarUsuarios(nombre, nusuario, contrsaña_reg, cedula);

    }

    public void agregarUsuarios(String nombres, String usuarios, String contra, String cedula){
        BaseDatos manejadorBD = new BaseDatos();

        Connection conexion = manejadorBD.conexionBase();

        if(conexion != null){
            try{
                String sql = "INSERT INTO usuarios_proyecto (nombre, nombre_usuario, contrasena, cedula, rol) VALUES (?,?,?,?,?)";

                try(PreparedStatement stmt = conexion.prepareStatement(sql)){
                    stmt.setString(1, nombres);
                    stmt.setString(2, usuarios);
                    stmt.setString(3, contra);
                    stmt.setString(4, cedula);
                    stmt.setString(5, rol);

                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Registro Exitoso");
                }
            }catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Error al agregar los datos");
            }finally {
                try{
                    conexion.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
