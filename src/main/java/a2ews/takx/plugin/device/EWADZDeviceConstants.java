package a2ews.takx.plugin.device;

import a2ews.takx.plugin.EWADZConstants;

/**
 * Device constants. You may want to split these out into different files (e.g. a constants file for message types,
 * another for data types, etc.) or include them directly in the classes where they are used.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
public abstract class EWADZDeviceConstants extends EWADZConstants
{
    /**
     * This defines the type of the device plugin.
     */
    public static final String TYPE = "EWADZ Device";

    /**
     * This is the id for status messages.
     */
    public static final String STATUS_MESSAGE = "Status";

    /**
     * The battery level data type used in status messages.
     */
    public static final String BATTERY_LEVEL = "Battery Level";

    /**
     * This is the id for update messages.
     */
    public static final String UPDATE_MESSAGE = "Update";

    /**
     * The update rate data type used in update messages.
     */
    public static final String RATE = "Update Rate";
}
