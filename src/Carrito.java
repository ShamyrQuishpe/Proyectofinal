import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Carrito extends JFrame {
    private JButton regresarButton;
    private JButton agregarProductoButton;
    private JTextField cantidadField;
    private JTable table1;
    private JLabel mostrarTotal;
    private JLabel mostrarPrecio;
    private JLabel mostrartotal2;
    private JPanel panelcarrito;
    private JTextField idField;
    private JButton actualizarButton;
    static int cantidad = 0;
    static int id_fijo = 0;
    static double precio = 0.00;
    static double precio_final = 0.00;

    static String usuarioActual = "";

    static String cedulaUsuario = "";

    public Carrito() {
        super("Compra");
        setContentPane(panelcarrito);

        mostrarProductos_tabla();
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login frame = new Login();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.getContentPane().setBackground(new Color(234, 211, 186));
                dispose();
            }
        });
        agregarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validaciones();
            }
        });
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarProductos_tabla();
            }
        });
    }

    public void iniciar() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        this.getContentPane().setBackground(new Color(234, 211, 186));
        cedulaObtener();
    }

    private void mostrarProductos_tabla() {
        BaseDatos manejadorBD = new BaseDatos();
        Connection conexion = manejadorBD.conexionBase();
        String sql = "SELECT * FROM repuestos";

        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            DefaultTableModel tableModel = crearModeloTabla(resultSet);

            table1.setModel(tableModel);

            // Aplicar el renderizador personalizado a la columna de imágenes
            table1.getColumnModel().getColumn(5).setCellRenderer(new RenderImagen());

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener información de la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private DefaultTableModel crearModeloTabla(ResultSet resultSet) throws SQLException {
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{},
                new String[]{"Id", "Nombre", "Descripcion","Stock","Precio","Imagen"});

        //Limpiar la tabla
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(tableModel.getRowCount() - 1);
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

        return tableModel;
    }

    public void validaciones() {
        String idTexto = idField.getText().trim();
        String cantidadTexto = cantidadField.getText().trim();

        // Verificar si alguno de los campos está vacío
        if (idTexto.isEmpty() || cantidadTexto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No pueden haber campos vacíos", "Error", JOptionPane.ERROR_MESSAGE);
            idField.setText("");
            cantidadField.setText("");
            return; // Salir del método si hay campos vacíos
        }

        id_fijo = Integer.parseInt(idField.getText());
        int cantidadRecibida = Integer.parseInt(cantidadField.getText());

        if (autenticarProducto(id_fijo)) {
            cantidadActual();
            if (cantidadRecibida > cantidad) {
                JOptionPane.showMessageDialog(null, "El id es correcto, pero la cantidad excede el stock disponible");
            } else {
                JOptionPane.showMessageDialog(null, "El id es correcto, producto agregado al carrito");
                precio_final = (cantidadRecibida * precio);
                mostrarTotal.setText(String.valueOf(precio_final));
                insertarDatos(cantidadRecibida);
                actualizarRepuesto(cantidadRecibida);
                mostrarProductos_tabla();
            }
        } else {
            JOptionPane.showMessageDialog(null, "El id es incorrecto");
            idField.setText("");
            cantidadField.setText("");
        }
    }

    public boolean autenticarProducto(int id) { //FALTArol
        BaseDatos manejadorBD = new BaseDatos();
        Connection conexion = manejadorBD.conexionBase();

        if (conexion != null) {
            try {
                String sql = "SELECT * FROM repuestos WHERE IdRepuestos=?"; //FALTArol
                try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                    stmt.setInt(1, id);


                    System.out.println("Consulta sql " + stmt.toString());

                    ResultSet resultSet = stmt.executeQuery();
                    return resultSet.next();

                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al ejecutar la consulta");
            } finally {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static void cantidadActual() {
        BaseDatos manejadorBD = new BaseDatos();

        // Obtener la conexión a la base de datos
        try (Connection conexion = manejadorBD.conexionBase()) {
            // Verificar si la conexión es nula
            if (conexion == null) {
                JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Salir del método si no se pudo establecer la conexión
            }

            // Preparar la consulta SQL para obtener todos los usuarios
            String sql = "SELECT stock, precio FROM repuestos WHERE IdRepuestos=?";
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                // Ejecutar la consulta para obtener el resultado
                stmt.setInt(1, id_fijo);

                ResultSet resultSet = stmt.executeQuery();

                if (resultSet.next()) {
                    cantidad = resultSet.getInt("stock");
                    System.out.println(cantidad);
                    precio = resultSet.getDouble("precio");
                    System.out.println(precio);
                }
            }
        } catch (SQLException e) {
            // Capturar cualquier excepción SQL
            JOptionPane.showMessageDialog(null, "Error al recuperar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Imprimir la traza de la pila para diagnóstico
        }
    }

    public static void usuarioObtener(String usuario) {
        usuarioActual = usuario;
    }

    public static void cedulaObtener() {
        BaseDatos manejadorBD = new BaseDatos();

        // Obtener la conexión a la base de datos
        try (Connection conexion = manejadorBD.conexionBase()) {
            // Verificar si la conexión es nula
            if (conexion == null) {
                JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Salir del método si no se pudo establecer la conexión
            }

            // Preparar la consulta SQL para obtener todos los usuarios
            String sql = "SELECT cedula FROM usuarios_proyecto WHERE nombre_usuario=?";
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                // Ejecutar la consulta para obtener el resultado
                stmt.setString(1, usuarioActual);

                ResultSet resultSet = stmt.executeQuery();

                if (resultSet.next()) {
                    cedulaUsuario = resultSet.getString("cedula");
                    System.out.println(cedulaUsuario);
                }
            }
        } catch (SQLException e) {
            // Capturar cualquier excepción SQL
            JOptionPane.showMessageDialog(null, "Error al recuperar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Imprimir la traza de la pila para diagnóstico
        }
    }

    public static void insertarDatos(int cantidad1) {
        BaseDatos manejadorBD = new BaseDatos();

        Connection conexion = manejadorBD.conexionBase();

        if (conexion != null) {
            try {
                String sql = "INSERT INTO transferencia (id_producto, id_cliente, cantidad, total) VALUES (?,?,?,?)";

                try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                    stmt.setInt(1, id_fijo);
                    stmt.setString(2, cedulaUsuario);
                    stmt.setInt(3, cantidad1);
                    stmt.setDouble(4, precio_final);

                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Registro Exitoso");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al agregar la transaccion");
            } finally {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void actualizarRepuesto(int cantidad1) {
        BaseDatos manejadorBD = new BaseDatos();

        // Obtener la conexión a la base de datos
        try (Connection conexion = manejadorBD.conexionBase()) {
            // Verificar si la conexión es nula
            if (conexion == null) {
                JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Salir del método si no se pudo establecer la conexión
            }

            // Preparar la consulta SQL para obtener todos los usuarios
            String sql = "UPDATE repuestos SET stock = ? WHERE IdRepuestos = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                int nuevoCantidad = cantidad - cantidad1;
                // Ejecutar la consulta para obtener el resultado
                stmt.setInt(1, nuevoCantidad);
                stmt.setInt(2, id_fijo);

                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Actualizacion de cantidad");
            }
        } catch (SQLException e) {
            // Capturar cualquier excepción SQL
            JOptionPane.showMessageDialog(null, "Error al recuperar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Imprimir la traza de la pila para diagnóstico
        }
    }
}

