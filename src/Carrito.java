import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Carrito extends JFrame {
    private JButton regresarButton;
    private JButton agregarProductoButton;
    private JComboBox comboBox2;
    private JTextField textField1;
    private JTable table1;
    private JLabel mostrarTotal;
    private JLabel mostrarPrecio;
    private JLabel mostrartotal2;
    private JPanel panelcarrito;

    public Carrito(){
        super("Compra");
        setContentPane(panelcarrito);

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
                frame.setSize(400,400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.getContentPane().setBackground(new Color(234, 211, 186));
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
}
