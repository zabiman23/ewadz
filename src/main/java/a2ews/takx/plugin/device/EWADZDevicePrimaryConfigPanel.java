package a2ews.takx.plugin.device;

import gov.takx.api.plugin.IRaptorPanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * The primary configuration panel that is displayed to the user when he/she right-clicks on a device and selects
 * Configure.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
public class EWADZDevicePrimaryConfigPanel extends JPanel implements IRaptorPanel
{
    private JSpinner updateRateSpinner;

    /**
     * A reference to the device plugin controller for callbacks.
     */
    private final EWADZDevicePluginController controller;
    private JTextField messageField;
    private JButton sendButton;
    private JButton receiveButton;

    /**
     * Constructor.
     *
     * @param controller A reference to the device plugin controller used for callbacks.
     */
    public EWADZDevicePrimaryConfigPanel(EWADZDevicePluginController controller)
    {
        this.controller = controller;
        initializeUIComponents();
    }

    @Override
    public JPanel getPanel()
    {
        return this;
    }

    @Override
    public String getTitle()
    {
        return "Device Config";
    }

    @Override
    public URL getIcon()
    {
        return null;
    }

    /**
     * Sets the value of the update rate spinner
     *
     * @param rate The new update rate
     */
    public void setUpdateRate(int rate)
    {
        updateRateSpinner.setValue(rate);
    }

    /**
     * Responds to user requests to update the message rate.
     */
    private void updateButtonActionPerformed()
    {
        Integer rate = (Integer) updateRateSpinner.getValue();
        controller.setUpdateRate(rate);
    }

     private JButton createCoTMessageButton() {
        JButton coTButton = new JButton("CoT Messaging");
        coTButton.addActionListener(e -> openCoTMessagingDialog());
        return coTButton;
    }

    private void openCoTMessagingDialog() {
        // Implement the dialog that handles CoT message sending/receiving
        JDialog dialog = new JDialog();
        dialog.setTitle("CoT Messaging");
        dialog.setLayout(new BorderLayout());
        dialog.add(new JLabel("Enter CoT message:"), BorderLayout.NORTH);
        JTextField messageField = new JTextField();
        dialog.add(messageField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                controller.sendCoTMessage(message);
                dialog.dispose();
            }
        });
        dialog.add(sendButton, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setVisible(true);
    }

    private void initializeUIComponents() {

        this.setLayout(new BorderLayout());

        // Panel for update rate configuration
        JPanel ratePanel = new JPanel();
        ratePanel.setLayout(new FlowLayout());
        ratePanel.add(new JLabel("Update rate (seconds):"));
        
        updateRateSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 60, 1));
        ratePanel.add(updateRateSpinner);

        JButton updateButton = new JButton("Update Rate");
        updateButton.addActionListener(e -> updateRate());
        ratePanel.add(updateButton);
        
        this.add(ratePanel, BorderLayout.NORTH);

        // Panel for CoT message configuration
        JPanel cotPanel = new JPanel();
        cotPanel.setLayout(new FlowLayout());

        messageField = new JTextField(20);
        cotPanel.add(messageField);

        sendButton = new JButton("Send CoT Message");
        sendButton.addActionListener(e -> sendCoTMessage());
        cotPanel.add(sendButton);

        receiveButton = new JButton("Receive CoT Message");
        receiveButton.addActionListener(e -> receiveCoTMessage());
        cotPanel.add(receiveButton);

        this.add(cotPanel, BorderLayout.CENTER);
    }


    private void sendCoTMessage() {
        // This method should prompt for message content and send it
        String message = messageField.getText();
        if (message != null && !message.isEmpty()) {
            controller.sendCoTMessage(message);
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a message to send.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void receiveCoTMessage() {
        // This method could display the last received CoT message or open a dialog to show messages
        String message = controller.receiveCoTMessage();
        JOptionPane.showMessageDialog(this, "Received message: " + message);
    }

    private void updateRate() {
        int rate = (Integer) updateRateSpinner.getValue();
        controller.setUpdateRate(rate);
    }


}
