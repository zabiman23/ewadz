package a2ews.takx.plugin.device;

import gov.takx.api.messages.IPrePersistRaptorDataMessage;
import gov.takx.api.messages.IRaptorDataStructure;
import gov.takx.api.plugin.gateway.AGatewayParser;
import gov.takx.api.plugin.gateway.IGatewayParserDelegate;
import gov.takx.commons.constants.CommServiceType;

/**
 * Parses data coming in from a device via a connection.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
public class EWADZDeviceGatewayParser extends AGatewayParser
{
    /**
     * Parser helper.
     */
    private final RadarParser radarParser;

    /**
     * Constructor.
     *
     * @param sourceType The connection type.
     * @param sourceName The connection name.
     * @param delegate   The delegate used to access core functionality.
     */
    protected EWADZDeviceGatewayParser(CommServiceType sourceType, String sourceName, IGatewayParserDelegate delegate)
    {
        super(sourceType, sourceName, delegate);
        radarParser = new RadarParser();
    }

    /**
     * This method will be called each time a new chunk of data is received over the connection specified by the
     * {@link #sourceType} and {@link #sourceName}. This method should be efficient, as each parser will be notified
     * of the data serially on the same thread.
     *
     * @param bytes  The next chunk of bytes.
     * @param length The number of valid bytes in the next chunk.
     * @param format The format of the data. May be <code>null</code>.
     */
    @Override
    public void parseBytes(byte[] bytes, int length, String format)
    {
        // Note: you must use the length parameter instead of the length of the bytes array, as the bytes array may not
        // be completely populated.
        for (int i = 0; i < length; i++)
        {
            // Parse the next byte. (You would normally do all this parsing yourself, extracting things like
            // time, location, unit id, battery level, etc.)
            RadarParser.RadarInfo radarInfo = radarParser.parse(bytes[i]);

            // If a new complete position message has been generated, process it.
            if (radarInfo != null)
            {
                if (radarInfo.radarMessageType == RadarParser.RadarMessageType.STATUS)
                {
                    // The message structure must be defined in the data structure xml
                    IRaptorDataStructure dataStructure = factory.getDataStructure(EWADZDeviceConstants.STATUS_MESSAGE);
                    IPrePersistRaptorDataMessage rdm = dataStructure.createRaptorDataMessage(radarInfo.unitId);

                    // Set the location latitude and longitude. They are core fields that are always available in RDMs.
                    rdm.setLocation(radarInfo.lat, radarInfo.lon);

                    // Set the device time. It's another core field that is always available in RDMs.
                    rdm.setTime(radarInfo.time);

                    // Set the custom field(s).
                    rdm.setDouble(EWADZDeviceConstants.BATTERY_LEVEL, radarInfo.batteryLevel);

                    // Set the connection type and name so that the controller knows where to send data back out to the wire.
                    rdm.setSourceName(sourceName);
                    rdm.setSourceType(sourceType);

                    // Hand the message off to the core for processing and delivery to the device plugin controller.
                    delegate.sendToController(rdm);
                }
            }
        }
    }
}
