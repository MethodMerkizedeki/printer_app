import javax.print.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
public class CRDBPrintApp extends JFrame {

    public CRDBPrintApp() {
        setTitle("CRDB USB Printer App");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // CRDB Brand Colors
        Color green = new Color(0, 155, 58); 
        Color white = Color.WHITE;

        // Create a top panel for the welcome message and logo
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(white);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));


        ImageIcon logoIcon = new ImageIcon("crdb_logo.png");
        JLabel logoLabel = new JLabel(logoIcon);
        topPanel.add(logoLabel, BorderLayout.WEST); 

        // Welcome Message
        JLabel titleLabel = new JLabel("WELCOME TO THE PRINT PAGE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(green);
        topPanel.add(titleLabel, BorderLayout.CENTER); // Centered text

        // Print Button
        JButton printButton = new JButton("🖨️ PRINT");
        printButton.setFont(new Font("Arial", Font.BOLD, 28));
        printButton.setForeground(white);
        printButton.setBackground(green);
        printButton.setFocusPainted(false);
        printButton.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        // Center Panel for Print Button
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(white);
        centerPanel.add(printButton);

        // Print Button Action
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = printCard("^XA^FO100,100^ADN,36,20^FDHello, CRDB!^FS^XZ");
                JOptionPane.showMessageDialog(CRDBPrintApp.this, result, "Print Status", 
                        result.contains("failed") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add Components to the Frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private static String printCard(String command) {
        try {
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            if (printServices.length == 0) {
                return "No printers found!";
            }

            PrintService usbPrinter = null;
            for (PrintService printer : printServices) {
                if (printer.getName().toLowerCase().contains("javelin")) { 
                    usbPrinter = printer;
                    break;
                }
            }

            if (usbPrinter == null) {
                return "Javelin Printer not found!";
            }

            // Sending raw print commands
            DocPrintJob printJob = usbPrinter.createPrintJob();
            byte[] commandBytes = command.getBytes(StandardCharsets.UTF_8);
            Doc doc = new SimpleDoc(commandBytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

            printJob.print(doc, null);
            return "Print command sent successfully!";

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Printing failed!";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CRDBPrintApp app = new CRDBPrintApp();
            app.setVisible(true);

            // Auto-send a test print command when the application starts
            String result = printCard("^XA^FO100,100^ADN,36,20^FDHello, CRDB!^FS^XZ");
            JOptionPane.showMessageDialog(app, result, "Print Status", 
                    result.contains("failed") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
        });
    }
}