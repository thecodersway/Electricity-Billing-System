import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class pay_bill extends JFrame implements ActionListener {
    JTextField meterNumberField, amountField;
    JButton payButton;
    
    pay_bill() {
        setTitle("Pay Bill");

        // Creating a panel for customer details
        JPanel detailsPanel = new JPanel(new GridLayout(3, 2));
        detailsPanel.add(new JLabel("Meter Number:"));
        meterNumberField = new JTextField();
        detailsPanel.add(meterNumberField);

        detailsPanel.add(new JLabel("Amount to Pay:"));
        amountField = new JTextField();
        detailsPanel.add(amountField);

        payButton = new JButton("Pay");
        payButton.addActionListener(this);
        detailsPanel.add(payButton);

        // Adding the panel to the frame
        add(detailsPanel, BorderLayout.NORTH);

        // Adding the web browser panel to the frame
        JEditorPane j = new JEditorPane();
        j.setEditable(false);
        try {
            j.setPage("https://paytm.com/electricity-bill-payment");
        } catch (Exception e) {
            j.setContentType("text/html");
            j.setText("<html>Could not load</html>");
        }
        JScrollPane scrollPane = new JScrollPane(j);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocation(250, 120);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String meterNumber = meterNumberField.getText();
        String amountStr = amountField.getText();
        
        if (meterNumber.isEmpty() || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both meter number and amount to pay.");
            return;
        }
        
        double amountToPay;
        try {
            amountToPay = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
            return;
        }

        // Database connection and update logic
        try {
            conn c1 = new conn();
            String query = "UPDATE bill SET amount = amount - ? WHERE meter_number = ?";
            PreparedStatement pst = c1.s.prepareStatement(query);
            pst.setDouble(1, amountToPay);
            pst.setString(2, meterNumber);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Payment successful. Amount updated.");
            } else {
                JOptionPane.showMessageDialog(this, "Payment failed. Meter number not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while processing the payment.");
        }
    }

    public static void main(String[] args) {
        new pay_bill().setVisible(true);
    }
}
