package a2ews.takx.plugin.app;

import a2ews.takx.plugin.device.target.EWADZTargetDeviceConstants;
import a2ews.takx.plugin.cot.CoTMessage;
import a2ews.takx.plugin.cot.CoTMessageSender;
import gov.takx.api.annotation.InvokeOnEDT;
import gov.takx.api.dataportal.IRaptorDataListener;
import gov.takx.api.dataportal.IRaptorDataPortalController;
import gov.takx.api.mapobject.IMapObject;
import gov.takx.api.messages.IMapEntity;
import gov.takx.api.messages.IMapObjectOfflineMessage;
import gov.takx.api.messages.IRaptorDataMessage;
import gov.takx.api.messages.IRaptorDataStructure;
import gov.takx.api.plugin.annotations.AppPlugin;
import gov.takx.api.plugin.annotations.PluginDescriptor;
import gov.takx.api.plugin.annotations.PluginSpecification;
import gov.takx.api.plugin.app.AAppPlugin;
import gov.takx.api.plugin.support.SupportedPlatformVersion;
import gov.takx.api.ui.ComponentPosition;
import gov.takx.api.ui.IUISpaceManager;
import gov.takx.platform.ui.ComponentDescriptor;
import gov.takx.platform.ui.ComponentDescriptorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Manages the lifecycle and user interface for this plugin.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
@AppPlugin(
        spec = @PluginSpecification(
                family = EWADZAppConstants.FAMILY,
                type = EWADZAppConstants.TYPE,
                version = EWADZAppConstants.VERSION,
                iconPath = "ewadz.png",
                minimumSupportedVersion = SupportedPlatformVersion.V3_4 // updated UI space manager API
        ),
        descriptor = @PluginDescriptor(
                name = "EWADZ App",
                description = "A short description of the app",
                longDescription = "A longer description of the app"
        ),
        canRunHeadless = false
)
@SuppressWarnings("UnusedDeclaration")
public class EWADZAppPlugin extends AAppPlugin implements IRaptorDataListener
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Panel for displaying list of targets.
     */
    private EWADZAppPanel panel;

    private CoTMessageSender cotMessageSender; // Added CoTMessageSender instance


    /**
     * Called when the app is started. The app may be started when TAKX starts if it is configured to auto-start, or may
     * be started later by the operator. The app may be started and stopped an arbitrary number of times during a
     * program run. At this point, the IAppPlugin will have a valid delegate and will start receiving project lifecycle
     * events.
     */
    @Override
    public void start()
    {
        setupUI();
        delegate.addDataListener(this);
    }


    // Method to send a CoT message
    public void sendCoTMessage(CoTMessage message) {
        if (cotMessageSender != null) {
            cotMessageSender.sendCoTMessage(message.getData());
        } else {
            logger.error("CoTMessageSender is not initialized.");
        }
    }

    /**
     * Sets up the UI elements for the app. This method is annotated with {@link InvokeOnEDT} to force it to execute on
     * the Event Dispatch Thread (EDT).
     */
    @SuppressWarnings("ConstantConditions") // suppresses the warning about a possible null URL for the image
    @InvokeOnEDT
    private void setupUI()
    {
        panel = new EWADZAppPanel(delegate);

        ComponentDescriptor componentDescriptor = new ComponentDescriptorBuilder(panel, "EWADZ Targets")
                .setIcon(getClass().getClassLoader().getResource("ewadz.png"))
                .setComponentPosition(ComponentPosition.RIGHT)
                .build();

        // IUISpaceManager can either be retrieved from the delegate (as shown here) or via injection by CDI
        IUISpaceManager uiSpaceManager = delegate.getUISpaceManager();
        uiSpaceManager.addComponent(componentDescriptor, panel);
        uiSpaceManager.showComponent(panel);
    }

    /**
     * Called when the app is stopped. The app will be stopped if the project is closed, TAKX is shutdown, or the user
     * chooses to stop the app. The app may be started and stopped an arbitrary number of times during a program run.
     * Note: After the stop notification, the delegate will be disposed and should no longer be used by the IAppPlugin.
     * Also, there will be no more notifications of project lifecycle events.
     */
    @Override
    public void stop()
    {
        teardownUI();

        delegate.removeDataListener(this);
    }

    /**
     * Tears down the UI elements for the app. This method is annotated with {@link InvokeOnEDT} to force it to execute
     * on the Event Dispatch Thread (EDT).
     */
    @InvokeOnEDT
    private void teardownUI()
    {
        delegate.getUISpaceManager().removeComponent(panel);
    }

    /**
     * Called when the app is shutdown because TAKX is shutting down. The app will <b>not</b> be started again after
     * this method is called.
     */
    @Override
    public void shutdown()
    {
        panel = null;
    }

    /**
     * Only matches map objects that have the same family as this plugin. This will cause this class to receive data
     * from this map object.
     *
     * @param mapObject The new map object.
     * @return true if family is same as this plugin.
     */
    @Override
    public boolean matchMapObject(IMapObject mapObject)
    {
        return mapObject.getFamily().equals(family);
    }

    /**
     * Called when a new map object is defined.
     *
     * @param mapEntity Definition of new object.
     */
    @Override
    public void onMapObjectDefined(IMapEntity mapEntity)
    {

    }

    /**
     * Called when a map object is added.
     *
     * @param mapObject        The new map object.
     * @param portalController The controller that is handling the data feed for the new map object.
     */
    @Override
    public void onMapObjectAdded(IMapObject mapObject, IRaptorDataPortalController portalController)
    {

    }

    /**
     * Called when a message is received.
     *
     * @param topic     the topic to which this message was published.
     * @param rdm       the message itself.
     * @param structure the data structure associated with the message.
     */
    @Override
    public void onDataMessage(String topic, IRaptorDataMessage rdm, IRaptorDataStructure structure)
    {
        if (EWADZTargetDeviceConstants.TARGET_MESSAGE.equals(rdm.getMessageType()))
        {
            panel.addTarget(rdm.getMapObject());
        }
    }

    /**
     * Called when a data feed is disconnected. The disconnect may be user initiated, or may be because a connection
     * was lost.
     *
     * @param portalController The controller for the feed that was disconnected.
     */
    @Override
    public void onDisconnect(IRaptorDataPortalController portalController)
    {

    }

    /**
     * Called when the Map Object is going offline (being deleted).
     *
     * @param message            Message with the relevant information about the map object
     * @param portalController   The controller that was handling the data feed for the map object.
     * @param wipeHistoricalData True if map object is being deleted.
     */
    @Override
    public void onMapObjectOffline(IMapObjectOfflineMessage message, IRaptorDataPortalController portalController,
                                   boolean wipeHistoricalData)
    {

    }
}
