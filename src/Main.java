import javax.swing.*;
import java.awt.*;

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