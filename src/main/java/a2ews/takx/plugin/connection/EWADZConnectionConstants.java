package a2ews.takx.plugin.connection;

import a2ews.takx.plugin.EWADZConstants;

/**
 * Connection constants.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
public abstract class EWADZConnectionConstants extends EWADZConstants
{
    /**
     * This defines the type of the connection plugin.
     */
    public static final String TYPE = "EWADZ Connection";

    /**
     * Key for storing IP address value in a configuration map.
     */
    public static final String IP_ADDRESS_KEY = "IP Address";

    /**
     * Key for storing port value in a configuration map.
     */
    public static final String PORT_KEY = "Port";
}
