package a2ews.takx.plugin.device.target;

import gov.takx.api.plugin.annotations.DevicePlugin;
import gov.takx.api.plugin.annotations.PluginDescriptor;
import gov.takx.api.plugin.annotations.PluginSpecification;
import gov.takx.api.plugin.device.ADevicePlugin;
import gov.takx.api.plugin.device.IDevicePluginController;
import gov.takx.api.plugin.gateway.IGatewayFramer;
import gov.takx.api.plugin.gateway.IGatewayFramerDelegate;
import gov.takx.api.plugin.gateway.IGatewayParser;
import gov.takx.api.plugin.gateway.IGatewayParserDelegate;
import gov.takx.commons.constants.CommServiceType;

/**
 * Specifies the {@link IDevicePluginController} to use for this plugin.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
@DevicePlugin(
        spec = @PluginSpecification(
                family = EWADZTargetDeviceConstants.FAMILY,
                type = EWADZTargetDeviceConstants.TYPE,
                version = EWADZTargetDeviceConstants.VERSION,
                iconPath = "target.png" // This icon will be displayed in the toolbar and on the map
        ),
        descriptor = @PluginDescriptor(
                name = "EWADZ Target"
        ),
        dataStructureXml = "targetDataStructures.xml", // Defines the data structure(s) for RDMs
        manualCreationAllowed = false
)
public class EWADZTargetDevicePlugin extends ADevicePlugin
{
    /**
     * When a message is sent to the plugin from the family {@link EWADZTargetDeviceConstants#FAMILY} of type
     * {@link EWADZTargetDeviceConstants#TYPE} with a unit id that hasn't been seen yet, this method will be called.
     *
     * @param unitId The device's unit id.
     * @return The {@link IDevicePluginController} for the device.
     */
    @Override
    protected IDevicePluginController makeDeviceController(String unitId)
    {
        return new EWADZTargetDevicePluginController(family, type, unitId);
    }

    /**
     * Returns a parser to process incoming data.
     * <p>
     * When a new connection is established (e.g. a new serial or tcp/ip connection), each device plugin will be asked
     * if it would like to provide a parser to inspect the data by a call to this method. This plugin can either return
     * a parser or <code>null</code> if it's not interested in the data.
     *
     * @param sourceType The connection type, e.g. {@link CommServiceType#CLIENT_SOCKET} for a socket connection.
     * @param sourceName The connection name, e.g. "127.0.0.1:12001" for a socket connection.
     * @param format     The format of the data. May be <code>null</code> if the connection plugin did not specify a format.
     * @param delegate   The delegate that will be used to access core functionality like sending
     *                   {@link gov.takx.api.messages.IRaptorDataMessage}s from the parser to the device controller.
     * @return A parser if the plugin is interested in parsing the data or <code>null</code> if it is not.
     */
    @Override
    public IGatewayParser createGatewayParser(CommServiceType sourceType, String sourceName, String format,
                                              IGatewayParserDelegate delegate)
    {
        if (sourceType == CommServiceType.CLIENT_SOCKET && format.equals(EWADZTargetDeviceConstants.FORMAT))
        {
            return new EWADZTargetDeviceGatewayParser(CommServiceType.CLIENT_SOCKET, sourceName, delegate);
        }

        return null;
    }

    @Override
    public IGatewayFramer createGatewayFramer(CommServiceType sourceType, String sourceName, String format,
                                              IGatewayFramerDelegate delegate)
    {
        return null;
    }
}
