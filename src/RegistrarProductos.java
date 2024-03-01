import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrarProductos extends JFrame {
    private JTextField nombreP;
    private JTextField cantidadP;
    private JTextField precioP;
    private JButton subirImagenButton;
    private JButton GUARDARButton;
    private JPanel panel1;
    private JTextArea descripcionP;
    private JScrollPane scrollPane;
    private JTable tableR;
    private JPanel panelDatos;
    private JPanel panelTabla;
    private JTextField textField1;
    private JButton buscarButton;
    private JButton actualizarButton;
    private JButton eliminarButton;
    private JButton menuButton;

    private static byte[] imagen;

    public RegistrarProductos(){
        super("Registrar Productos");
        setContentPane(panel1);

        //hacer que el texto de la descripcion se adapte al tamanio de la pantalla
        descripcionP.setLineWrap(true);
        descripcionP.setWrapStyleWord(true);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //Dar tamanio al panel de los datos y al panel de la tabla
        // Establecer un tamaño específico para panelSecundario
        panelDatos.setPreferredSize(new Dimension(300, 300));
        panelDatos.setBackground(new Color(234, 211, 186));
        panelTabla.setPreferredSize(new Dimension(500, 400));


        //Cargar los repuestos ingresados
        mostrarProductos_tabla();

        subirImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

               imagen= subirImagen(false);
            }
        });
        GUARDARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ingresoFormulario();
            }
        });
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Admin admin=new Admin();
                admin.abrir();
                dispose();
            }
        });
    }

    public void iniciar(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,600);
        setLocationRelativeTo(null);
        setVisible(true);
        this.getContentPane().setBackground(new Color(234, 211, 186));
    }
    public void ingresoFormulario(){
        String nombre = nombreP.getText();
        String decripcion = descripcionP.getText();
        int cantidad=0;
        double precio=0;

        try {
             cantidad =Integer.parseInt(cantidadP.getText());
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null,"Solo puede ingresar numeros en cantidad");
            cantidadP.setText("");
            return;

        }

        try {
            precio = Double.parseDouble(precioP.getText());
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null,"Solo puede ingresar numeros en precio");
            precioP.setText("");
            return;
        }

        System.out.println(nombre);
        System.out.println(decripcion);
        System.out.println(cantidad);
        System.out.println(precio);
        System.out.println(imagen);

        if(nombre.isEmpty()||decripcion.isEmpty()||cantidadP.getText().isEmpty()||precioP.getText().isEmpty()||imagen==null){
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        }else {
            try {
                RegistrarProducto(nombre,decripcion,cantidad,precio,imagen);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



    }
    public  byte[] subirImagen(boolean exitImg ){
        if (!exitImg){
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    // Leer la imagen como un arreglo de bytes
                    FileInputStream fis = new FileInputStream(selectedFile);
                    byte[] imageData = fis.readAllBytes();
                    fis.close();

                    // Mostrar un diálogo de confirmación
                    int option = JOptionPane.showConfirmDialog(null, "¿Desea subir esta imagen?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        // Obtener direccion de la imagen
                        JOptionPane.showMessageDialog(null, "La imagen se subió correctamente" );
                        return imageData;

                    } else {
                        JOptionPane.showMessageDialog(null, "La imagen no se subió");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al subir la imagen: " + ex.getMessage());
                }
            }
            return null;
        }
        return null;
    }

    private void RegistrarProducto(String nombre, String descripcion, int cantidad, double precio,byte[] imageData) throws SQLException {

        BaseDatos manejadorBD = new BaseDatos();
        Connection conexion = manejadorBD.conexionBase();

        if(conexion != null) {
            try {
                String sql = "INSERT INTO repuestos (nombreRepuesto,descripcion,stock,precio,imagen) VALUES (?,?, ?, ?,?)";

                try(PreparedStatement stmt = conexion.prepareStatement(sql)){

                    stmt.setString(1,nombre);
                    stmt.setString(2,descripcion);
                    stmt.setInt(3,cantidad);
                    stmt.setDouble(4,precio);
                    stmt.setBytes(5, imageData);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Registro Exitoso");
                    nombreP.setText("");
                    descripcionP.setText("");
                    cantidadP.setText("");
                    precioP.setText("");
                    imagen=new byte[0];



                }
            }catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Error al agregar los datos");
            } finally {
                try{
                    conexion.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
    private void mostrarProductos_tabla() {
        BaseDatos manejadorBD = new BaseDatos();
        Connection conexion = manejadorBD.conexionBase();
        String sql = "SELECT * FROM repuestos";

        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Crear el modelo de tabla con los nombres de las columnas
            DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{},
                    new String[]{"Id", "Nombre", "Descripcion","Stock","Precio","Imagen"});

            //Limpiar la tabla
            for (int i = 0; i < tableR.getRowCount(); i++) {
                tableModel.removeRow(i);
                i -= 1;
            }

            while (resultSet.next()) {
                int id=resultSet.getInt("IdRepuestos");
                String nombre = resultSet.getString("nombreRepuesto");
                String descripcion = resultSet.getString("descripcion");
                int stock = resultSet.getInt("stock");
                double precio= resultSet.getDouble("precio");
                byte[] imagen=resultSet.getBytes("imagen");

                // Agregar fila al modelo de tabla
                tableModel.addRow(new Object[]{id,nombre,descripcion,stock,precio,imagen});
            }

            tableR.setModel(tableModel);

            // Aplicar el renderizador personalizado a la columna de imágenes
            tableR.getColumnModel().getColumn(5).setCellRenderer(new RenderImagen());


        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener información de la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

}
