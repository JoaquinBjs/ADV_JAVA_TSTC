package joaquinbarreram4;
import javax.swing.*;
import java.awt.*;

abstract class ViewState {
    static JFrame frame;
    static JPanel pContainer = new JPanel();;
    static CardLayout cLayout;

    // Display menu method
    abstract void enter();
    
    // Start method
    abstract void update();
    
    // Save method
    abstract void save();
    
    // Load method
    abstract void load();
    
   // Creates a JFrame ", sets its size to 1080x720 pixels, and puts basic window stuff.
    public static void initializeFrame() {
        frame = new JFrame("JetSetters");
        frame.setSize(1080, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        cLayout = new CardLayout();
        pContainer = new JPanel(cLayout);
        frame.add(pContainer);

        frame.setVisible(true);
    }
    
    
    // Adds a new view state to the card layout container.

    public static void addState(String name, ViewState state) {
        pContainer.add(state.getPanel(), name);
    }
    
    // Gets the panel with the view state.
    public abstract JPanel getPanel();
    
    // Displays the view state in the card layout.
    public static void showState(String name) {
        cLayout.show(pContainer, name);
    }
}
