package joaquinbarreram4;

import javax.swing.*;
import java.awt.*;

public class JetSettersButton extends JButton {
    public JetSettersButton(String text) {
        super(text);
        setBackground(new Color(37, 68, 65)); 
        setForeground(new Color(249,249,255));
        setFocusPainted(false);
        setFont(new Font("Dialog", Font.BOLD, 15));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
}
