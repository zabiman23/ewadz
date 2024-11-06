package a2ews.takx.plugin.device;

import gov.takx.api.plugin.IRaptorPanel;
import gov.takx.api.plugin.annotations.DeviceSwingUIProvider;
import gov.takx.api.plugin.device.IDeviceConfigurationManager;
import gov.takx.api.plugin.device.IDevicePluginPreferencesPanel;
import gov.takx.api.ui.IDeviceSwingUIProvider;

import javax.swing.*;
import java.util.List;
import java.util.Collections;

/**
 * The UI provider for the device plugin controller that provides the necessary Swing UI components for the TAKX
 * thick client.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
@DeviceSwingUIProvider(forPluginController = EWADZDevicePluginController.class)
public class EWADZDeviceSwingUIProvider implements IDeviceSwingUIProvider
{
    /**
     * A reference to the device plugin controller for callbacks.
     */
    private final EWADZDevicePluginController controller;

    private EWADZDeviceConfigurationManager configManager;

    /**
     * Constructor.  TAKX will automatically inject the controller and unitID parameters when calling this
     * constructor (order does not matter).  There must be exactly one constructor.
     *
     * @param controller A reference to the device plugin controller used for callbacks.
     */
    public EWADZDeviceSwingUIProvider(EWADZDevicePluginController controller)
    {
        this.controller = controller;
    }

    @Override
    public IRaptorPanel getPrimaryPreferencePanel()
    {
        return new EWADZDevicePrimaryConfigPanel(controller);
    }

    @Override
    public List<IDevicePluginPreferencesPanel> getCustomPreferencePanels()
    {
        return Collections.emptyList();
    }

    @Override
    public JPanel getPluginHelpPanel()
    {
        return null;
    }

    @Override
    public IDeviceConfigurationManager getConfigurationManager()
    {
        if (configManager == null)
        {
            configManager = new EWADZDeviceConfigurationManager(controller);
        }

        return configManager;
    }
}
