package a2ews.takx.plugin.device.target;

import a2ews.takx.plugin.CotTypes;
import gov.takx.api.messages.IRaptorDataMessage;
import gov.takx.api.plugin.device.ADevicePluginController;

/**
 * The representation of the device that appears on the map, processes messages, and manages the UI.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
public class EWADZTargetDevicePluginController extends ADevicePluginController
{
    /**
     * Constructor.
     *
     * @param family The device plugin family
     * @param type   The device plugin type
     * @param unitId The unit id of the device that this controller represents.
     */
    protected EWADZTargetDevicePluginController(String family, String type, String unitId)
    {
        super(family, type, unitId);
    }

    @Override
    protected void onDelegateInjected()
    {
        devicePluginDelegate.getMapObject().setCotType(CotTypes.UNKNOWN);
    }

    @Override
    public void receiveMessage(IRaptorDataMessage iRaptorDataMessage)
    {
    }
}
