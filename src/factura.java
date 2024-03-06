import com.lowagie.text.*;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Esta es la clase para generar la factura en un documento pdf
 * @author: Monica Jaña
 * @version: 2023-B
 */

public class factura {

    //Parametros que se imprimiran en el PDF
    String cedula;
    String nombre;
    int cantidad;
    String producto;
    double valor_a_pagar;
    Document documento;
    FileOutputStream archivo;
    Paragraph titulo;
    Paragraph linea;

    /**
     * Este es el método contructor de la clase factura con parametros
     * @author: Monica Jaña
     * @version: 2023-B
     */
    public factura(String cedula, String nombre, int cantidad, String producto, double valor_a_pagar) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.producto = producto;
        this.valor_a_pagar = valor_a_pagar;

        //Para que la pagina sea de tamaño carta
        documento = new Document(PageSize.LETTER);
        titulo = new Paragraph("FACTURA GENERADA");
        linea = new Paragraph("-------------------------------------------------------------------------------------------");

    }

    /**
     * Este es el método para generar la estructura de la factura
     * @author: Monica Jaña
     * @version: 2023-B
     */
    public void crear_factura(){

        try {

            //el archivo se guadara con el numero de cedula correspodiente de cada cliente
            String ruta= System.getProperty("user.home");
            PdfWriter.getInstance(documento,new FileOutputStream(ruta+"/OneDrive/Escritorio/Reporte_"+cedula+".pdf"));


            //abrimos el documento para que pueda ser editado
            documento.open();

            //Definimos la fuente del documento
            Font font = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            //Coloca el texto en el centro
            linea.setAlignment(0);

            //para agregar una imagen al documento, la imagen sera importada de una carpeta ya existente
            Image imagen= Image.getInstance("Logo/logoRepuestos.jpeg");

            //tamaño de la iamgen
            imagen.scaleToFit(250, 200);
            // Obtener el ancho y alto de la imagen
            float anchoImagen = imagen.getScaledWidth();
            float altoImagen = imagen.getScaledHeight();

            // Establecer la posición de la imagen en el borde superior derecho
            float x = PageSize.A4.getWidth() - anchoImagen;
            float y = PageSize.A4.getHeight() - altoImagen;

            // Posicionar la imagen en el borde superior derecho
            imagen.setAbsolutePosition(x, y);

            //los siguientes elementos sera agregados en el documento
            //Genera una linea en blanco
            documento.add(Chunk.NEWLINE);
            documento.add(titulo);
            documento.add(imagen);
            documento.add(Chunk.NEWLINE);
            documento.add(linea);
            documento.add(new Paragraph("Cliente: "+nombre));
            documento.add(new Paragraph("Cedula de identidad: "+cedula));
            documento.add(linea);
            documento.add(new Paragraph("Producto adquirido: "+producto));
            documento.add(new Paragraph("Cantidad: "+cantidad));
            documento.add(new Paragraph("Precio final: $"+valor_a_pagar));
            documento.add(linea);

            //cerramos el documento
            documento.close();
            JOptionPane.showMessageDialog(null, "Factura generada con exito!");

        }catch (FileNotFoundException e){
            System.err.println(e.getMessage());
        }
        catch (DocumentException e){
            System.err.println(e.getMessage());
        }
        catch (IOException e ){
            System.err.println(e.getMessage());
        }
    }
}
