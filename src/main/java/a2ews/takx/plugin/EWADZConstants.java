package a2ews.takx.plugin;

/**
 * Plugin constants.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
public abstract class EWADZConstants
{
    /**
     * This is the family to which all the EWADZ plugins belong.
     */
    public static final String FAMILY = "a2ews.takx.plugin";

    /**
     * This is the version of the EWADZ plugins. It should be rolled each time the plugins change. By taking advantage
     * of the java-templates functionality provided by the BSP (Build Support Plugin), this will happen automatically
     * when the plugin is built.
     */
    public static final String VERSION = "1.0.0";

    // app specific constants
    public static final String TYPE = "EWADZ";


    /**
     * This is the identifier for the data format processed for this plugin family.
     */
    public static final String FORMAT = "EWADZ";

    public static final String IP_STRING = "127.0.0.1";
    public static final String PORT = "12044";

}
