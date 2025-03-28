import javax.print.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class CRDBPrintApp extends JFrame {

    public CRDBPrintApp() {
        setTitle("CRDB USB Printer App");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // CRDB Brand Colors
        Color green = new Color(0, 155, 58); // CRDB Green
        Color white = Color.WHITE;

        // Create a top panel for the welcome message and logo
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(white);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // CRDB Logo (Ensure "crdb_logo.png" is in the project directory)
        ImageIcon logoIcon = new ImageIcon("crdb_logo.png");
        JLabel logoLabel = new JLabel(logoIcon);
        topPanel.add(logoLabel, BorderLayout.WEST); // Logo on the left

        // Welcome Message
        JLabel titleLabel = new JLabel("WELCOME TO THE CARD PRINTING PAGE", SwingConstants.CENTER);
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
                printText("Printing from CRDB USB Printer!");
            }
        });

        // Add Components to the Frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void printText(String text) {
        try {
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            if (printServices.length == 0) {
                JOptionPane.showMessageDialog(this, "No printers found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PrintService usbPrinter = null;
            for (PrintService printer : printServices) {
                if (printer.getName().toLowerCase().contains("javelin")) { 
                    usbPrinter = printer;
                    break;
                }
            }

            if (usbPrinter == null) {
                JOptionPane.showMessageDialog(this, "Javelin Printer not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DocPrintJob printJob = usbPrinter.createPrintJob();
            byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
            Doc doc = new SimpleDoc(new ByteArrayInputStream(textBytes), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

            printJob.print(doc, null);
            JOptionPane.showMessageDialog(this, "Printing completed!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Printing failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CRDBPrintApp().setVisible(true));
    }
}
