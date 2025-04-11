// Color Palette: https://colordesigner.io/color-palette-builder#132E32-2D514E-516669-98ACAD-F9F9FF
// 132E32 - dark green
// 2D514E - deep teal
// 516669 - muted steel
// 98ACAD - misty blue
// F9F9FF - off white

package joaquinbarreram4;


public class JoaquinBarreraM4 {
    public static void main(String[] args) {
        ViewState.initializeFrame();
        // Initialize all states
        LoginState loginState = new LoginState();
        CustomerViewState customerState = new CustomerViewState(new Customer(), loginState);
        EmployeeViewState employeeState = new EmployeeViewState(null, null);
        
        // Register states
        ViewState.addState("Login", loginState);
        ViewState.addState("CustomerView", customerState);
        ViewState.addState("EmployeeView", employeeState);
        
        // Start with login screen
        ViewState.showState("Login");
    }
}
