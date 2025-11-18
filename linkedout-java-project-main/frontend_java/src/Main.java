import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::homeScreen);
    }

    // GLOBAL THEME
    private static void applyTheme() {
        UIManager.put("Panel.background", new Color(28, 28, 28));
        UIManager.put("Button.foreground", Color.white);
        UIManager.put("Label.foreground", Color.white);
        UIManager.put("TextField.background", new Color(45, 45, 45));
        UIManager.put("TextField.foreground", Color.white);

        UIManager.put("OptionPane.background", new Color(28, 28, 28));
        UIManager.put("OptionPane.messageForeground", Color.white);
    }

    // HOME SCREEN
    private static void homeScreen() {
        applyTheme();

        JFrame frame = new JFrame("LinkedOut - Home");
        frame.setSize(450, 380);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.getContentPane().setBackground(new Color(20, 20, 20));

        // TITLE
        JLabel title = new JLabel("LinkedOut Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(new Color(0, 200, 255));
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));

        // CARD PANEL
        JPanel card = new JPanel();
        card.setBackground(new Color(35, 35, 35));
        card.setLayout(new GridLayout(3, 1, 15, 15));
        card.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JButton createBtn = modernButton("Create User");
        JButton viewBtn = modernButton("View User");
        JButton editBtn = modernButton("Edit User");

        card.add(createBtn);
        card.add(viewBtn);
        card.add(editBtn);

        // EVENTS
        createBtn.addActionListener(e -> new CreateUser());
        viewBtn.addActionListener(e -> new ViewUser());
        editBtn.addActionListener(e -> new EditUser());

        frame.add(title, BorderLayout.NORTH);
        frame.add(card, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Modern UI Button
    private static JButton modernButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(50, 110, 180));
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 200, 255), 1),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(60, 130, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(50, 110, 180));
            }
        });

        return btn;
    }
}
