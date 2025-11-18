import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class CreateUser {

    private static final String API_URL = "http://127.0.0.1:8000/create_user";

    public CreateUser() {

        // Frame Setup
        JFrame frame = new JFrame("Create LinkedOut Profile");
        frame.setSize(500,550);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Dark Theme Styling
        Color bg = new Color(28, 28, 30);
        Color fg = new Color(235, 235, 245);
        Color card = new Color(45, 45, 50);
        Color accent = new Color(0, 122, 255);

        frame.getContentPane().setBackground(bg);

        // Title
        JLabel title = new JLabel("NEW USER PROFILE", SwingConstants.CENTER);
        title.setForeground(fg);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        // Form Container (Card)
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10,1,5,5));
        panel.setBackground(card);
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JTextField name = styledField();
        JTextField education = styledField();
        JTextField experience = styledField();
        JTextField skills = styledField();
        JPasswordField password = styledPassword();

        panel.add(styledLabel("Full Name"));
        panel.add(name);

        panel.add(styledLabel("Education"));
        panel.add(education);

        panel.add(styledLabel("Experience"));
        panel.add(experience);

        panel.add(styledLabel("Skills"));
        panel.add(skills);

        panel.add(styledLabel("Password"));
        panel.add(password);

        // Submit button
        JButton submit = new JButton("Create Profile");
        submit.setBackground(accent);
        submit.setForeground(Color.white);
        submit.setFocusPainted(false);
        submit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submit.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        submit.addActionListener(e -> {
            try {
                String data = "name=" + URLEncoder.encode(name.getText(),"UTF-8")
                        + "&education=" + URLEncoder.encode(education.getText(),"UTF-8")
                        + "&experience=" + URLEncoder.encode(experience.getText(),"UTF-8")
                        + "&skills=" + URLEncoder.encode(skills.getText(),"UTF-8")
                        + "&password=" + URLEncoder.encode(new String(password.getPassword()),"UTF-8");

                URL url = new URL(API_URL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                OutputStream os = con.getOutputStream();
                os.write(data.getBytes());
                os.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder(); 
                String line;

                while((line = br.readLine()) != null) sb.append(line);

                JOptionPane.showMessageDialog(frame, "✅ User Created!\n" + sb.toString());
                frame.dispose();

            } catch(Exception ex){
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        // Layout container
        frame.add(title, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(submit, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Styled field helper
    private JTextField styledField(){
        JTextField tf = new JTextField();
        tf.setBackground(new Color(60,60,65));
        tf.setForeground(Color.white);
        tf.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return tf;
    }

    private JPasswordField styledPassword(){
        JPasswordField pf = new JPasswordField();
        pf.setBackground(new Color(60,60,65));
        pf.setForeground(Color.white);
        pf.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return pf;
    }

    private JLabel styledLabel(String text){
        JLabel lb = new JLabel(text);
        lb.setForeground(new Color(200,200,200));
        lb.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return lb;
    }
}
