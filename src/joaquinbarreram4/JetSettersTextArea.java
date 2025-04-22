package joaquinbarreram4;

import javax.swing.*;
import java.awt.*;

public class JetSettersTextArea extends JTextArea {
    public JetSettersTextArea(int rows, int cols) {
        super(rows, cols);
        setWrapStyleWord(true);
        setLineWrap(true);
        setFont(new Font("Dialog", Font.BOLD, 14));
        setForeground(new Color(249,249,255));
        setBorder(BorderFactory.createLineBorder(new Color(81, 102, 105), 10, true));
        setEditable(false);
        setBackground(new Color(152, 172, 173)); 
    }
}
