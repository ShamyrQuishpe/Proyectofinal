import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Clase para mostrar todas las funciones del administrador
 * @author: Shamyr Quishpe
 * @version: 2023-B
 */

public class Admin extends JFrame{
    private JButton agregarUsuariosButton;
    private JButton agregarProductosButton;
    private JPanel adminPanel;
    private JButton revisionDeVentasButton;
    private JButton mostrarUsuariosButton;
    private JButton SALIRButton;

    public Admin(){
        super("PANTALLA ADMIN");
        setContentPane(adminPanel);
        mostrarUsuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                mostrarInformacion_tabla();
            }
        });
        agregarUsuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Adduser vent_adduser = new Adduser();
                vent_adduser.abrir();
                dispose();
            }
        });
        agregarProductosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrarProductos registroP = new RegistrarProductos();
                registroP.iniciar();
            }
        });
        SALIRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login frame = new Login();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400,400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.getContentPane().setBackground(new Color(234, 211, 186));
                dispose();
            }
        });
        revisionDeVentasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarInformacion_tablatra();
            }
        });
    }
    /**
     * Funcion para abrir el panel con sus respectivas caracteristicas
     * @author: Shamyr Quishpe
     * @version: 2023-B
     */
    public void abrir(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,400);
        setLocationRelativeTo(null);
        setVisible(true);
        this.getContentPane().setBackground(new Color(234, 211, 186));
    }
    /**
     * Funcion para mostrar la imformacion albergada en la base de datos con una tabla
     * @author: Shamyr Quishpe
     * @version: 2023-B
     */
    private void mostrarInformacion_tabla() {
        BaseDatos manejadorBD = new BaseDatos();
        Connection conexion = manejadorBD.conexionBase();
        String sql = "SELECT nombre, nombre_usuario, cedula FROM usuarios_proyecto";

        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Crear el modelo de tabla con los nombres de las columnas
            DefaultTableModel tableModel = new DefaultTableModel(
                    new String[]{"Nombre", "Nombre_Usuario", "Cedula"}, 0);

            while (resultSet.next()) {

                String nombre = resultSet.getString("Nombre");
                String nombreU = resultSet.getString("Nombre_Usuario");
                int id = resultSet.getInt("Cedula");

                // Agregar fila al modelo de tabla
                tableModel.addRow(new Object[]{nombre, nombreU, id});
            }

            // Crear la tabla con el modelo de datos
            JTable tabla = new JTable(tableModel);

            // Crear el panel que contendrá la tabla
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);

            // Crear el marco que contendrá el panel con la tabla
            JFrame frameTabla = new JFrame("Datos en Tabla");
            frameTabla.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo la ventana de la tabla
            frameTabla.getContentPane().add(panelTabla);
            frameTabla.setSize(600, 400);
            frameTabla.setLocationRelativeTo(this);
            frameTabla.setVisible(true);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener información de la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Funcion que dependiendo los campos se insertan en una tabla que sera mostrada
     * @author: Shamyr Quishpe
     * @version: 2023-B
     */

    private void mostrarInformacion_tablatra() {
        BaseDatos manejadorBD = new BaseDatos();
        Connection conexion = manejadorBD.conexionBase();
        String sql = "SELECT * FROM transferencia";

        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            DefaultTableModel tableModel = new DefaultTableModel(
                    new String[]{"id_transferencia", "id_producto", "id_cliente", "cantidad", "total"}, 0);

            while (resultSet.next()) {

                int id_t = resultSet.getInt("id_transferencia");
                int id_p = resultSet.getInt("id_producto");
                String ced = resultSet.getString("id_cliente");
                int canti = resultSet.getInt("cantidad");
                double tot = resultSet.getInt("total");


                // Agregar fila al modelo de tabla
                tableModel.addRow(new Object[]{id_t, id_p, ced, canti, tot});
            }

            // Crear la tabla con el modelo de datos
            JTable tabla = new JTable(tableModel);

            // Crear el panel que contendrá la tabla
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);

            // Crear el marco que contendrá el panel con la tabla
            JFrame frameTabla = new JFrame("Datos en Tabla");
            frameTabla.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo la ventana de la tabla
            frameTabla.getContentPane().add(panelTabla);
            frameTabla.setSize(600, 400);
            frameTabla.setLocationRelativeTo(this);
            frameTabla.setVisible(true);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener información de la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
