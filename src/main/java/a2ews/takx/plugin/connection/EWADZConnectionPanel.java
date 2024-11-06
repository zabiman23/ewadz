package a2ews.takx.plugin.connection;

import gov.takx.api.plugin.connection.AConnectionPanel;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * The main panel that is displayed to the user when the connection is selected.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
public class EWADZConnectionPanel extends AConnectionPanel
{
    private JTextField portTextField;
    private JTextField ipAddressTextField;

    /**
     * Constructor.
     *
     * @param controller A reference to the connection plugin controller used for callbacks.
     */
    public EWADZConnectionPanel(EWADZConnectionPluginController controller)
    {
        super("EWADZ Connection");
        initializeUIComponents();
        ipAddressTextField.setText(controller.getIpAddress());
        portTextField.setText(controller.getPort());
    }

    /**
     * Gets the current value of the IP address text field.
     *
     * @return Value of the IP address text field
     */
    public String getIpAddress()
    {
        return ipAddressTextField.getText();
    }

    /**
     * Gets the current value of the port text field.
     *
     * @return Value of the port text field
     */
    public String getPort()
    {
        return portTextField.getText();
    }

    /**
     * Validates the panel prior to committing the changes when the user presses connect.
     *
     * @return null if there is nothing invalid or a string containing an error message
     */
    @Override
    public String validateContents()
    {
        return null;
    }

    /**
     * Create the necessary UI components and add them to the panel. For this UI, the {@link GridBagLayout} was used due
     * to its flexibility. For more information on using this layout, see the
     * <a href="https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html">tutorial</a>.
     */
    private void initializeUIComponents()
    {
        int padding = 5;

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{0, 0};
        layout.rowHeights = new int[]{0, 0};
        layout.columnWeights = new double[]{0.0, 1.0}; // Only second column should grow
        layout.rowWeights = new double[]{0.0, 0.0}; // None of the rows need to grow
        setLayout(layout);

        // Add IP address label
        JLabel ipAddressLabel = new JLabel();
        ipAddressLabel.setText("IP Address");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 0, padding, padding);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        add(ipAddressLabel, constraints);

        // Add IP address text field
        ipAddressTextField = new JTextField();
        constraints.insets = new Insets(0, 0, padding, 0);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(ipAddressTextField, constraints);

        // Add port label
        JLabel portLabel = new JLabel();
        portLabel.setText("Port");

        constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 0, 0, padding);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        add(portLabel, constraints);

        // Add port text field
        portTextField = new JTextField();

        constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(portTextField, constraints);
    }
}
