package a2ews.takx.plugin.device;

import gov.takx.api.plugin.annotations.DevicePlugin;
import gov.takx.api.plugin.annotations.PluginDescriptor;
import gov.takx.api.plugin.annotations.PluginSpecification;
import gov.takx.api.plugin.device.ADevicePlugin;
import gov.takx.api.plugin.device.IDevicePluginController;
import gov.takx.api.plugin.gateway.IGatewayFramer;
import gov.takx.api.plugin.gateway.IGatewayFramerDelegate;
import gov.takx.api.plugin.gateway.IGatewayParser;
import gov.takx.api.plugin.gateway.IGatewayParserDelegate;
import gov.takx.api.plugin.support.SupportedPlatformVersion;
import gov.takx.commons.constants.CommServiceType;

/**
 * Specifies the {@link IDevicePluginController} to use for this plugin and optionally the {@link IGatewayParser} and
 * {@link IGatewayFramer} that will handle parsing and framing of incoming and outgoing data, respectively.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
@DevicePlugin(
        spec = @PluginSpecification(
                family = EWADZDeviceConstants.FAMILY,
                type = EWADZDeviceConstants.TYPE,
                version = EWADZDeviceConstants.VERSION,
                iconPath = "ewadz.png", // This icon will be displayed in the toolbar and on the map
                minimumSupportedVersion = SupportedPlatformVersion.V3_0
        ),
        descriptor = @PluginDescriptor(
                name = "EWADZ"
        ),
        dataStructureXml = "dataStructures.xml", // Defines the data structure(s) for RDMs
        commandStructureXml = "commandStructures.xml" // Defines the data structure(s) for RCMs
)
@SuppressWarnings("UnusedDeclaration")
public class EWADZDevicePlugin extends ADevicePlugin
{
    /**
     * When a message is sent to the plugin from the family {@link EWADZDeviceConstants#FAMILY} of type
     * {@link EWADZDeviceConstants#TYPE} with a unit id that hasn't been seen yet, this method will be called.
     *
     * @param unitId The device's unit id.
     * @return The {@link IDevicePluginController} for the device.
     */
    @Override
    protected IDevicePluginController makeDeviceController(String unitId)
    {
        return new EWADZDevicePluginController(family, type, unitId);
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
        if (sourceType == CommServiceType.CLIENT_SOCKET && format.equals(EWADZDeviceConstants.FORMAT))
        {
            return new EWADZDeviceGatewayParser(CommServiceType.CLIENT_SOCKET, sourceName, delegate);
        }

        return null;
    }

    /**
     * Provide a parser that will handle {@link gov.takx.api.messages.IRaptorCommandMessage}s, converting the data
     * to bytes that will be put onto the wire.
     *
     * @param sourceType The connection type, e.g. {@link CommServiceType#CLIENT_SOCKET} for a socket connection.
     * @param sourceName The connection name, e.g. "127.0.0.1:12001" for a socket connection.
     * @param format     The (optional) data format.
     * @param delegate   The delegate that will be used to access core functionality like sending bytes out to the connection.
     * @return A framer if the plugin is interested in sending data to the connection or <code>null</code> if it is not.
     */
    @Override
    public IGatewayFramer createGatewayFramer(CommServiceType sourceType, String sourceName, String format,
                                              IGatewayFramerDelegate delegate)
    {
        if (sourceType == CommServiceType.CLIENT_SOCKET && format.equals(EWADZDeviceConstants.FORMAT))
        {
            return new EWADZDeviceGatewayFramer(delegate);
        }

        return null;
    }
}
