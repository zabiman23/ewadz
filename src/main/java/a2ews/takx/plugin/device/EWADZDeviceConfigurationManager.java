package a2ews.takx.plugin.device;

import gov.takx.api.plugin.IRaptorPanel;
import gov.takx.api.plugin.device.ADeviceConfigurationManager;
import gov.takx.api.plugin.device.IDevicePluginConfigurationPanel;

import java.util.List;

public class EWADZDeviceConfigurationManager extends ADeviceConfigurationManager
{
    private final EWADZDevicePluginController controller;
    private EWADZDevicePrimaryConfigPanel configPanel;

    public EWADZDeviceConfigurationManager(EWADZDevicePluginController controller)
    {
        this.controller = controller;
    }

    @Override
    public IRaptorPanel getPrimaryConfigurationPanel()
    {
        if (configPanel == null)
        {
            configPanel = new EWADZDevicePrimaryConfigPanel(controller);
        }

        return configPanel;
    }

    @Override
    public List<IDevicePluginConfigurationPanel> getCustomConfigurationPanels()
    {
        return null;
    }

    @Override
    public void onEngineeringMode(boolean b)
    {

    }
}
