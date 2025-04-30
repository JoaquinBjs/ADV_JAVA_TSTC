package joaquinbarreram5;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.filechooser.FileSystemView;

public class ReportMaker {

    private static String getDocumentsFolderPath() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            return System.getProperty("user.home") + "/Documents";
        }
        if (os.contains("win")) {
            return FileSystemView.getFileSystemView()
                    .getDefaultDirectory().getPath();
        }
        return System.getProperty("user.home");
    }

    private static String CSS = 
            "body {font-family:'Atkinson Hyperlegible',sans-serif;margin:20px;"
                + "background:#F9F9FF;color:#516669;}"
                + "h2 {color:#132E32;}"
                + "table {width:100%;border-collapse:collapse;margin-top:20px;"
                + "background:#98ACAD;}"
                + "th, td {border:1px solid #516669;padding:8px;text-align:left;}"
                + "th {background:#2D514E;color:#F9F9FF;font-weight:bold;}"
                + "tr:nth-child(even) {background:#F9F9FF;}";
    
    public static void generateCustomerReport(String title, String rows) {
        String html = 
            "<html><head><meta charset='UTF-8'>"
                + "<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n"
                + "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n"
                + "<link href=\"https://fonts.googleapis.com/css2?family=Lato:ital,wght@0,100;0,300;0,400;0,700;0,900;1,100;1,300;1,400;1,700;1,900&family=Parkinsans:wght@300..800&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Special+Gothic&display=swap\" rel=\"stylesheet\">"
                + "<title>" + title + "</title><style>" + CSS + "</style></head><body>"
                + "<h2>" + title + "</h2><table>"
                + "<tr><th>Order Date</th><th>Lodge Name</th><th>Lodge Type</th><th>Number of Nights</th><th>Total Cost</th></tr>"
                + rows
                + "</table></body></html>";

        saveAndOpen(html, "CustomerReport");
    }
    
    public static void generateEmployeeReport(String title, String rows) {
        String html = 
            "<html><head><meta charset='UTF-8'>"
                + "<link href='<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n"
                + "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n"
                + "<link href=\"https://fonts.googleapis.com/css2?family=Lato:ital,wght@0,100;0,300;0,400;0,700;0,900;1,100;1,300;1,400;1,700;1,900&family=Parkinsans:wght@300..800&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Special+Gothic&display=swap\" rel=\"stylesheet\">' rel='stylesheet'>"
                + "<title>" + title + "</title><style>" + CSS + "</style></head><body>"
                + "<h2>" + title + "</h2><table>"
                + "<tr><th>Customer ID</th><th>Customer Name</th><th>Total Spending</th></tr>"
                + rows
                + "</table></body></html>";

        saveAndOpen(html, "EmployeeReport");
    }
    
    private static void saveAndOpen(String html, String prefix) {
        try {
            File reportsDir = new File(getDocumentsFolderPath(), "Reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }

            File file = new File(reportsDir,
                    prefix + System.currentTimeMillis() + ".html");
            try (BufferedWriter w = new BufferedWriter(new FileWriter(file))) {
                w.write(html);
            }
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(file.toURI());
            }

            System.out.println("Report saved to: " + file.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
