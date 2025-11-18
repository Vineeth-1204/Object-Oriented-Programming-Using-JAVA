import javax.swing.*;
import java.awt.*;
import org.json.JSONObject;

public class ViewUser {

    public ViewUser() {
        JFrame frame = new JFrame("LinkedOut – User Resume");
        frame.setSize(540, 680);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.getContentPane().setBackground(new Color(22, 22, 22));

        // ===== TOP SEARCH BAR =====
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(new Color(30, 30, 30));

        JTextField userIdField = new JTextField(15);
        JButton searchBtn = new JButton("Search User");

        stylizeButton(searchBtn);
        stylizeField(userIdField);

        searchPanel.add(new JLabel("Enter User ID:"));
        searchPanel.add(userIdField);
        searchPanel.add(searchBtn);

        frame.add(searchPanel, BorderLayout.NORTH);

        // ===== PROFILE CARD =====
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(40, 40, 40));
        card.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = makeTitle("User Resume");
        JLabel nameLabel = makeInfoLabel();
        JLabel eduLabel = makeInfoLabel();
        JLabel expLabel = makeInfoLabel();
        JLabel skillsLabel = makeInfoLabel();

        card.add(title);
        card.add(Box.createVerticalStrut(20));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(eduLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(expLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(skillsLabel);

        frame.add(card, BorderLayout.CENTER);

        // ===== EVENT =====
        searchBtn.addActionListener(e -> {
            try {
                String id = userIdField.getText();
                JSONObject user = HTTPClient.get("/get_user/" + id);

                // Use optString to safely handle nulls returned by the backend
                String name = user.optString("name", "(no name)");
                String education = user.optString("education", "");
                String experience = user.optString("experience", "");
                String skills = user.optString("skills", "");

                nameLabel.setText(format("Name", name));
                eduLabel.setText(format("Education", education));
                expLabel.setText(format("Experience", experience));
                skillsLabel.setText(format("Skills", skills));
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "User not found or server error");
            }
        });

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    // ===== UI HELPERS =====
    private void stylizeButton(JButton b) {
        b.setBackground(new Color(60, 60, 160));
        b.setForeground(Color.white);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void stylizeField(JTextField f) {
        f.setBackground(new Color(50, 50, 50));
        f.setForeground(Color.white);
        f.setCaretColor(Color.white);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 15));
    }

    private JLabel makeInfoLabel() {
        JLabel l = new JLabel();
        l.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        l.setForeground(Color.white);
        return l;
    }

    private JLabel makeTitle(String t) {
        JLabel l = new JLabel(t, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        l.setForeground(new Color(0, 200, 255));
        return l;
    }

    private String format(String key, String value) {
        return "<html><b style='color:#FF9900'>" + key +
               ":</b><br><span style='color:white'>" + value + "</span></html>";
    }
}
