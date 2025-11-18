import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import org.json.JSONObject;

/**
 * EditUser GUI — calls POST http://127.0.0.1:8000/edit_user
 * Expected JSON body:
 * {
 *   "id": "LKO....",
 *   "password": "userpass",
 *   "name": "New name",             // optional
 *   "education": "New education",   // optional
 *   "experience": "New experience", // optional
 *   "skills": "New skills"          // optional
 * }
 *
 * Uses HTTPClient.postJSON(route, JSONObject) helper (make sure HTTPClient.java exists).
 */
public class EditUser {

    private static final String ROUTE = "/edit_user";

    public EditUser() {
        JFrame frame = new JFrame("Edit User");
        frame.setSize(460, 460);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        main.setBackground(new Color(30,30,30));

        JLabel title = new JLabel("Edit User");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(title);
        main.add(Box.createVerticalStrut(12));

        JTextField idField = styledField();
        JPasswordField passField = styledPassword();
        JTextField nameField = styledField();
        JTextField eduField = styledField();
        JTextArea expArea = new JTextArea(4, 20);
        expArea.setBackground(new Color(50,50,50));
        expArea.setForeground(Color.WHITE);
        expArea.setLineWrap(true);
        expArea.setWrapStyleWord(true);
        JTextField skillsField = styledField();

        main.add(labeled("User ID (LKO...):", idField));
        main.add(labeled("Password:", passField));
        main.add(labeled("Name (leave blank to keep):", nameField));
        main.add(labeled("Education (leave blank to keep):", eduField));
        main.add(labelWithComponent("Experience (leave blank to keep):", new JScrollPane(expArea)));
        main.add(labeled("Skills (comma separated):", skillsField));

        main.add(Box.createVerticalStrut(12));

        JButton updateBtn = new JButton("Update");
        updateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateBtn.setBackground(new Color(60,140,220));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);
        updateBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateBtn.setPreferredSize(new Dimension(140, 36));

        main.add(updateBtn);

        frame.getContentPane().add(main);
        frame.setVisible(true);

        // Action
        updateBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String password = new String(passField.getPassword());

            if (id.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter User ID and Password.", "Missing fields", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                JSONObject payload = new JSONObject();
                payload.put("id", id);
                payload.put("password", password);

                // Only include fields the user typed (avoid overwriting with empty strings)
                if (!nameField.getText().trim().isEmpty()) payload.put("name", nameField.getText().trim());
                if (!eduField.getText().trim().isEmpty()) payload.put("education", eduField.getText().trim());
                if (!expArea.getText().trim().isEmpty()) payload.put("experience", expArea.getText().trim());
                if (!skillsField.getText().trim().isEmpty()) payload.put("skills", skillsField.getText().trim());

                // Call backend (uses HTTPClient.postJSON)
                JSONObject resp = HTTPClient.postJSON(ROUTE, payload);

                // If backend returns a JSON {"status":"ok"} or similar, show success.
                if (resp.has("status") && resp.getString("status").equalsIgnoreCase("ok")) {
                    JOptionPane.showMessageDialog(frame, "User updated successfully.");
                } else {
                    // show raw response if status not ok
                    JOptionPane.showMessageDialog(frame, "Response: " + resp.toString());
                }

            } catch (IOException ioEx) {
                JOptionPane.showMessageDialog(frame, "Network error: " + ioEx.getMessage(), "Network Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                // try to extract error message from server if possible
                String msg = ex.getMessage();
                JOptionPane.showMessageDialog(frame, "Failed to update user. " + msg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // --- UI helpers ---
    private JTextField styledField() {
        JTextField tf = new JTextField();
        tf.setBackground(new Color(50,50,50));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createLineBorder(new Color(80,80,80), 1, true));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return tf;
    }

    private JPasswordField styledPassword() {
        JPasswordField pf = new JPasswordField();
        pf.setBackground(new Color(50,50,50));
        pf.setForeground(Color.WHITE);
        pf.setCaretColor(Color.WHITE);
        pf.setBorder(BorderFactory.createLineBorder(new Color(80,80,80), 1, true));
        pf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return pf;
    }

    private JPanel labeled(String labelText, JComponent comp) {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout(6,6));
        p.setBackground(new Color(30,30,30));
        JLabel l = new JLabel(labelText);
        l.setForeground(new Color(200,200,200));
        p.add(l, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        return p;
    }

    private JPanel labelWithComponent(String labelText, JComponent comp) {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout(6,6));
        p.setBackground(new Color(30,30,30));
        JLabel l = new JLabel(labelText);
        l.setForeground(new Color(200,200,200));
        p.add(l, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        return p;
    }

    // ---- main for quick debugging ----
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditUser());
    }
}
