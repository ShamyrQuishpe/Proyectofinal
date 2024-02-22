import javax.swing.*;

public class User extends JFrame {
    private JButton button1;
    private JButton button2;
    private JPanel userPanel;

    public User(){
        super("PANTALLA USUARIO");
        setContentPane(userPanel);
    }

    public void abrir(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,400);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
