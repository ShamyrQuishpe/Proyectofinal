import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Esta es una clase para manejar los tamañps de las imagenes de los productos ingresados por el usuario
 * @author: Monica Jaña
 * @version: 2023-B
 */
public class RenderImagen extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof byte[]) {
            // Si el valor es un array de bytes (imagen)
            byte[] imageData = (byte[]) value;
            ImageIcon icon = new ImageIcon(imageData);
            Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImage);

            setIcon(icon);
            setText(null); // Elimina el texto en la celda
        } else {
            // Si no es un array de bytes, usar el renderizador predeterminado
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

        return this;
    }
}
