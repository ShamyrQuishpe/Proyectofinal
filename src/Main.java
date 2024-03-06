import javax.swing.*;
import java.awt.*;

/**
 * Esta es la clase principal en la que se crea y se llama a la ventana del loguin
 * @author: Shamyr Quishpe
 * @version: 2023-B
  */
public class Main {
    public static void main(String[] args) {
        Login frame = new Login();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.getContentPane().setBackground(new Color(234, 211, 186));
    }
}