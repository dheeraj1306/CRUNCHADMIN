package CrunchAdmin;



import javax.swing.*;git
import java.awt.*;
import java.io.*;
import java.net.*;

public class CrunchClientGUI extends JFrame {
    private JTextField keywordsField, minLengthField, maxLengthField;
    private JTextArea resultArea;
    private JButton generateButton;

    public CrunchClientGUI() {
        setTitle("Crunch Client");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        keywordsField = new JTextField(30);
        minLengthField = new JTextField(5);
        maxLengthField = new JTextField(5);
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);

        generateButton = new JButton("Generate Dictionary");

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Keywords (comma separated):"));
        inputPanel.add(keywordsField);
        inputPanel.add(new JLabel("Min Length:"));
        inputPanel.add(minLengthField);
        inputPanel.add(new JLabel("Max Length:"));
        inputPanel.add(maxLengthField);
        inputPanel.add(generateButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        generateButton.addActionListener(e -> generateDictionary());
    }

    private void generateDictionary() {
        String keywords = keywordsField.getText();
        String minLength = minLengthField.getText();
        String maxLength = maxLengthField.getText();

        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out.println(keywords);
            out.println(minLength);
            out.println(maxLength);

            resultArea.setText("");
            String line;
            while (!(line = in.readLine()).equals("END")) {
                resultArea.append(line + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            resultArea.setText("Error connecting to server.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CrunchClientGUI().setVisible(true));
    }
}