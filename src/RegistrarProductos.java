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
/**
 * Esta es la clase para que el administrador pueda ingresar, buscar, eliminar y actualizar los productos.
 * @author: Monica Jaña- Mateo Tacuri
 * @version: 2023-B
 */
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
    private JTextField buscarId;
    private JButton buscarButton;
    private JButton actualizarButton;
    private JButton eliminarButton;
    private JButton menuButton;
    private JLabel imagenLabel;

    private static byte[] imagen;

    /**
     * Este es el contructor de la clase RegistroProductos para inicializar la pantalla
     * @author: Monica Jaña
     * @version: 2023-B
     */
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
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ingresoFormulario1();
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductos();

            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarRegistro();
            }
        });
    }

    /**
     * Esta es la funcion en la que se asigna el tamaño, la ubicacion y el color de la pantalla
     * @author: Monica Jaña- Mateo Tacuri
     * @version: 2023-B
     */
    public void iniciar(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,600);
        setLocationRelativeTo(null);
        setVisible(true);
        this.getContentPane().setBackground(new Color(234, 211, 186));
    }

    /**
     * Esta es la funcion para validar los campos y el ingreso del nombre, precio, descripcion, cantidad e imagen de cada producto
     * @author: Monica Jaña
     * @version: 2023-B
     */
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
    public void ingresoFormulario1(){
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
                actualizarDatos(nombre,decripcion,cantidad,precio,imagen);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Esta es la funcion para controlar la subida de las imagenes
     * @author: Monica Jaña- Mateo Tacuri
     * @version: 2023-B
     */
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

    /**
     * Esta es la funcion para subir los repuestos a la base de datos
     * @author: Monica Jaña
     * @version: 2023-B
     */
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

    /**
     * Esta es la funcion para mostrar los repuestos registrados en la base de datos
     * @author: Monica Jaña
     * @version: 2023-B
     */
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

    /**
     * Esta es la funcion que permite buscar  los repuestos a la base de datos
     * @author: Monica Jaña
     * @version: 2023-B
     */
    public void buscarProductos(){
        BaseDatos manejadorBD = new BaseDatos();
        Connection conexion = manejadorBD.conexionBase();
        int id= Integer.parseInt(buscarId.getText());

        if (conexion != null) {
            try {
                // Preparar la consulta SQL para verificar la autenticación
                String sql = "SELECT *FROM repuestos Where idRepuestos=? ";
                try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                    // Establecer los valores de los parámetros en la consulta
                    pstmt.setInt(1, id);
// Ejecutar la consulta para obtener el resultado
                    System.out.println("Consulta SQL: " + pstmt.toString()); // Imprimir la consulta SQL
                    ResultSet resultSet = pstmt.executeQuery();

                    // Procesar el resultado del ResultSet
                    while (resultSet.next()) {
                        int idProducto = resultSet.getInt("IdRepuestos");
                        String nombre = resultSet.getString("nombreRepuesto");
                        String descripcion = resultSet.getString("descripcion");
                        int stock = resultSet.getInt("stock");
                        double precio = resultSet.getDouble("precio");
                        byte[] imagen=resultSet.getBytes("imagen");

                        // Mostrar los en la interfaz gráfica
                        nombreP.setText(nombre);
                        descripcionP.setText(descripcion);
                        cantidadP.setText(String.valueOf(stock));
                        precioP.setText(String.valueOf(precio));

                        if (imagen != null) {
                            // Crear un ImageIcon a partir de los datos de la imagen
                            ImageIcon imageIcon = new ImageIcon(imagen);

                            // Escalar la imagen al tamaño de un carnet (5.5cm x 3.5cm)
                            Image image = imageIcon.getImage();
                            Image scaledImage = image.getScaledInstance(165, 150, Image.SCALE_SMOOTH);
                            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

                            // Establecer el ImageIcon escalado en el JLabel
                            imagenLabel.setIcon(scaledImageIcon);
                        } else {
                            JOptionPane.showMessageDialog(this, "El usuario no tiene una imagen asociada.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        //System.out.println("ID: " + idProducto + ", Nombre: " + nombre + ", Descripción: " + descripcion + ", Stock: " + stock + ", Precio: " + precio+", Imagen: "+imagen);

                    }

                }
            } catch (SQLException e) {
                e.printStackTrace(); // Imprimir el seguimiento de la pila para diagnóstico
                JOptionPane.showMessageDialog(null, "Error al ejecutar la consulta: " + e.getMessage());
            } finally {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Esta es la funcion en la que se actualizan los datos de los repuestos a la base de datos
     * @author: Monica Jaña
     * @version: 2023-B
     */
    private void actualizarDatos(String nombre, String descripcion, int cantidad, double precio,byte[] imageData) throws SQLException {
        BaseDatos manejadorBD = new BaseDatos();
        int id= Integer.parseInt(buscarId.getText());

        try (Connection conexion = manejadorBD.conexionBase();
             PreparedStatement statement = conexion.prepareStatement("UPDATE repuestos SET nombreRepuesto = ?, descripcion = ?, stock = ?, precio=?, imagen=? WHERE idRepuestos = ?")) {

            // Establecer los valores de los parámetros en el PreparedStatement
            statement.setString(1, nombre);
            statement.setString(2, descripcion);
            statement.setInt(3, cantidad);
            statement.setDouble(4, precio);
            statement.setBytes(5, imageData);
            statement.setInt(6,id);

            // Ejecutar la actualización
            int filasActualizadas = statement.executeUpdate();

            // Verificar si se realizaron actualizaciones y mostrar un mensaje correspondiente
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el producto con ID " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar datos en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Esta es la funcion que permite eliminar  los repuestos registrados en la base de datos
     * @author: Mateo Tacuri
     * @version: 2023-B
     */
    private void eliminarRegistro() {
        BaseDatos manejadorBD = new BaseDatos();
        int id= Integer.parseInt(buscarId.getText());
        try (Connection conexion = manejadorBD.conexionBase();
             PreparedStatement statement = conexion.prepareStatement("DELETE FROM repuestos WHERE idRepuestos = ?")) {

            statement.setInt(1, id);

            int filasEliminadas = statement.executeUpdate();

            if (filasEliminadas > 0) {
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el producto con ID " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar registro en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
