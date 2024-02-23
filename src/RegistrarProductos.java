import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

public class RegistrarProductos extends JFrame {
    private JTextField nombreP;
    private JTextField cantidadP;
    private JTextField precioP;
    private JButton subirImagenButton;
    private JButton GUARDARButton;
    private JPanel panel1;
    private JTextArea descripcionP;
    private JLabel rutaimagen;

    public RegistrarProductos(){
        super("Registrar Productos");
        setContentPane(panel1);
        subirImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subirImagen(false);
            }
        });
        GUARDARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ingresoFormulario();
            }
        });
    }

    public void iniciar(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void ingresoFormulario(){
        String nombre = nombreP.getText();
        String decripcion = descripcionP.getText();
        int cantidad=0;
        double precio=0;

        try {
             cantidad =Integer.parseInt(cantidadP.getText());
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null,"Solo puede ingresar numeros en precio y cantidad!");
            cantidadP.setText("");
            return;

        }

        try {
            precio = Double.parseDouble(precioP.getText());
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null,"Solo puede ingresar numeros en precio y cantidad!");
            precioP.setText("");
            return;
        }

        if(nombre.isEmpty()||decripcion.isEmpty()||cantidadP.getText().isEmpty()||precioP.getText().isEmpty()||subirImagen(true)==null){
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorioa");
        }else {
            try {
                RegistrarProducto(nombre,decripcion,cantidad,precio,subirImagen(true));
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
                        // Convertir los bytes a una representación de cadena (por ejemplo, Base64)
                        String imageDataAsString = Base64.getEncoder().encodeToString(imageData);
                        rutaimagen.setText(imageDataAsString);
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
                String sql = "INSERT INTO repuestos (nombre,descripcion, stock, precio, imagen) VALUES (?,?, ?, ?,?)";

                try(PreparedStatement stmt = conexion.prepareStatement(sql)){

                    stmt.setString(1,nombre);
                    stmt.setString(2,descripcion);
                    stmt.setInt(3,cantidad);
                    stmt.setDouble(4,precio);
                    stmt.setBytes(5, imageData);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Registro Exitoso");

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
}
