package uk.ac.cam.mef40.fjava.tick4star;

import uk.ac.cam.cl.fjava.messages.ChatMessage;

import javax.swing.*;
import java.awt.*;

public class ChatClientGUI extends JFrame {
    public static void main(String[] args) {


        var client = new ChatClientGUI();
    }

    public ChatClientGUI() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Chat");

        // SET UP INPUT
        var textPanel = new JPanel(new BorderLayout());
        var text = new JTextField();
        var button = new JButton("Send");
        button.addActionListener(e -> {
            System.out.println(text.getText());

            // Clear input text
            text.setText("");
        });
        textPanel.add(BorderLayout.CENTER, text);
        textPanel.add(BorderLayout.EAST, button);

        // SET UP MESSAGE DISPLAY
        var displayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        var display = new JTextArea(32,32);
        display.setLineWrap(true);
        display.setEditable(false);
        var scroll = new JScrollPane(display);
        displayPanel.add(scroll);

        // SET UP 'SETTINGS' (CHANGE NICKNAME)

        // LAYOUT PANELS
        JPanel borderPanel = new JPanel(new BorderLayout());
        borderPanel.add(BorderLayout.CENTER, displayPanel);
        borderPanel.add(BorderLayout.SOUTH, textPanel);

        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        container.add(borderPanel);
        this.getContentPane().add(container);

        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }
}
