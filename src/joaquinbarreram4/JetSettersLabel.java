package joaquinbarreram4;

import javax.swing.*;
import java.awt.*;

public class JetSettersLabel extends JLabel {
    public JetSettersLabel(String text) {
        super(text, SwingConstants.CENTER);
        setFont(new Font("Dialog", Font.BOLD, 24));
        setForeground(new Color(0,48,90)); 
    }
}