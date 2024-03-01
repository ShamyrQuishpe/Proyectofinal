import javax.swing.*;
import java.awt.*;

public class User extends JFrame {
    private JButton agregarAlCarritoButton;
    private JPanel userPanel;
    private JTable table1;
    private JButton menuButton;

    public User(){
        super("PANTALLA USUARIO");
        setContentPane(userPanel);
    }

    public void abrir(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,400);
        setLocationRelativeTo(null);
        setVisible(true);
        this.getContentPane().setBackground(new Color(234, 211, 186));
    }
}
