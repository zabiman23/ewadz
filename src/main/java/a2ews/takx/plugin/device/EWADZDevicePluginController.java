package a2ews.takx.plugin.device;

import a2ews.takx.plugin.CotTypes;
import a2ews.takx.plugin.cot.CoTMessageSender;
import a2ews.takx.plugin.cot.CoTMessageReceiver;
import gov.takx.api.messages.IPrePersistRaptorCommandMessage;
import gov.takx.api.messages.IRaptorDataMessage;
import gov.takx.api.messages.IRaptorDataStructure;
import gov.takx.api.plugin.device.ADevicePluginController;
import gov.takx.commons.constants.CommServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * The representation of the device that appears on the map, processes messages, and manages the UI.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
public class EWADZDevicePluginController extends ADevicePluginController
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * The name of the connection that is providing this device controller with data, e.g. "127.0.0.1:12000".
     * It's required to send messages back out to the device.
     *
     * @see #setUpdateRate(int)
     */
    private String sourceName;

    /**
     * The connection type that is providing this device controller with data, e.g. {@link CommServiceType#CLIENT_SOCKET}.
     * It's required to send messages back out to the device.
     *
     * @see #setUpdateRate(int)
     */
    private CommServiceType sourceType;

    /**
     * The rate at which messages will be sent by the device.
     */
    private int updateRate = 1;

    private CoTMessageSender cotMessageSender;
    private CoTMessageReceiver cotMessageReceiver;
    private Thread receiverThread;

    /**
     * Constructor.
     *
     * @param family The device plugin family
     * @param type   The device plugin type
     * @param unitId The unit id of the device that this controller represents.
     */
    public EWADZDevicePluginController(String family, String type, String unitId)
    {
        super(family, type, unitId);
        initializeCoTCommunication("10.10.4.27", 8089);  // Set appropriate IP and port
    }

    @Override
    protected void onDelegateInjected()
    {
        devicePluginDelegate.getMapObject().setCotType(CotTypes.FRIENDLY_UAV);
    }


    private void initializeCoTCommunication(String ipAddress, int port) {
        cotMessageSender = new CoTMessageSender(ipAddress, port);
        cotMessageReceiver = new CoTMessageReceiver(ipAddress, port, this::handleReceivedMessage);
        receiverThread = new Thread(cotMessageReceiver);
        receiverThread.start();
    }

    private void handleReceivedMessage(byte[] message, String source) {
        String receivedText = new String(message);
        logger.info("Received message from {}: {}", source, receivedText);
    }

    public void sendCoTMessage(String message) {
        if (cotMessageSender != null) {
            cotMessageSender.sendCoTMessage(message);
            logger.info("Sending CoT message: {}", message);
        }
    }

    // Simulated method to receive a CoT message
    public String receiveCoTMessage() {
        // This is a stubbed example. In a real scenario, you would have logic to listen to incoming data
        // For example, it might come from a socket, serial port, etc.
        // Here we just return a simulated message for demonstration.
        return "Simulated CoT Message received: [Details of the message]";
    }


    @Override
    public void receiveMessage(IRaptorDataMessage rdm)
    {
        sourceName = rdm.getSourceName();
        sourceType = rdm.getSourceType();

        if (rdm.getMessageType().equals(EWADZDeviceConstants.STATUS_MESSAGE))
        {
            // This would be where you would process the message if necessary
        }
    }

    /**
     * Responds to a user's request to change the update rate by creating a command message, setting the new rate,
     * and sending it to the framer.
     *
     * @param rate The new message rate.
     */
    public void setUpdateRate(int rate)
    {
        // If the rate didn't change, we don't have to do anything
        if (updateRate != rate)
        {
            updateRate = rate;

            // Get the "Update" structure and create a command message.
            IRaptorDataStructure dataStructure = factory.getDataStructure(EWADZDeviceConstants.UPDATE_MESSAGE);
            IPrePersistRaptorCommandMessage rcm = dataStructure.createRaptorCommandMessage(unitId);

            // Set the new rate.
            rcm.setInt(EWADZDeviceConstants.RATE, updateRate);

            // Set the connection details so the core knows how to route this message.
            rcm.setSourceName(sourceName);
            rcm.setSourceType(sourceType);

            try
            {
                // Send the message to the gateway framer via the core.
                devicePluginDelegate.sendMessage(rcm);
            } catch (IOException e)
            {
                logger.error("Error sending RCM", e);
            }
        }
    }
}
