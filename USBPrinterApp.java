import javax.print.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class USBPrinterApp extends JFrame {

    public USBPrinterApp() {
        setTitle("USB Printer App");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JButton printButton = new JButton("Print");
        printButton.setFont(new Font("Arial", Font.BOLD, 16));
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printText("Printing from USB Printer!");
            }
        });

        add(printButton);
    }

    private void printText(String text) {
        try {
            // Get list of connected printers
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            if (printServices.length == 0) {
                JOptionPane.showMessageDialog(this, "No printers found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Find USB-connected Javelin printer
            PrintService usbPrinter = null;
            for (PrintService printer : printServices) {
                if (printer.getName().toLowerCase().contains("javelin")) { // Adjust name if necessary
                    usbPrinter = printer;
                    break;
                }
            }

            if (usbPrinter == null) {
                JOptionPane.showMessageDialog(this, "Javelin Printer not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create print job
            DocPrintJob printJob = usbPrinter.createPrintJob();
            byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
            Doc doc = new SimpleDoc(new ByteArrayInputStream(textBytes), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

            // Send print command
            printJob.print(doc, null);
            JOptionPane.showMessageDialog(this, "Printing completed!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Printing failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new USBPrinterApp().setVisible(true));
    }
}