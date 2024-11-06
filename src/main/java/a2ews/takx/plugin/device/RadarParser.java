package a2ews.takx.plugin.device;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Helper class so you don't have to parse "radar" data yourself.
 */
public class RadarParser
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Radar status message type identifier.
     */
    private static final String STATUS = "Status";

    /**
     * Target message type identifier.
     */
    private static final String TARGET = "Target";

    // Indices of values in comma-delimited message String
    private static final int MESSAGE_TYPE_INDEX = 0;
    private static final int UNIT_ID_INDEX = 1;
    private static final int TIME_INDEX = 2;
    private static final int LAT_INDEX = 3;
    private static final int LON_INDEX = 4;
    private static final int BATTERY_LEVEL_INDEX = 5;
    private static final int TARGET_ID_INDEX = 5;

    /**
     * Buffers data until a complete line is received.
     */
    private final StringBuilder builder;

    /**
     * The current parsing state.
     */
    private State state = State.waiting;

    private enum State
    {
        waiting,
        collecting
    }

    /**
     * Constructor.
     */
    public RadarParser()
    {
        builder = new StringBuilder();
    }

    /**
     * Parse the next byte of data.
     *
     * @param b The next byte of data.
     * @return The updated position data, if a new complete message has been received, <code>null</code> otherwise.
     */
    public RadarInfo parse(byte b)
    {
        char next = (char) b;
        RadarInfo result = null;

        switch (state)
        {
            case waiting -> {
                if (next == '$')
                {
                    state = State.collecting;
                }
            }
            case collecting -> {
                if (next == '$')
                {
                    logger.debug("Unexpected start of message. Resetting buffer.");
                    builder.setLength(0);
                } else if (next == '*')
                {
                    try
                    {
                        result = parse(builder.toString());
                    } catch (Exception e)
                    {
                        logger.error("Error parsing message: {}", e.getMessage(), e);
                    }

                    state = State.waiting;
                    builder.setLength(0);
                } else
                {
                    builder.append(next);
                }
            }
        }

        return result;
    }

    /**
     * Parse a line of data.
     *
     * @param line The line of data to parse.
     * @return A {@link RadarInfo} message if the line parsed properly, <code>null</code> otherwise.
     */
    public static RadarInfo parse(String line)
    {
        // Remove the start marker ($) if present
        if (line.startsWith("$"))
        {
            line = line.substring(1);
        }

        // Remove the end marker (*) if present
        if (line.endsWith("*"))
        {
            line = line.substring(0, line.length() - 1);
        }

        // Split by commas.
        String[] tokens = line.split(",");

        // Verify that it's the correct type of NMEA data.
        String type = tokens[MESSAGE_TYPE_INDEX];

        // Check the line type.
        if (STATUS.equals(type) || TARGET.equals(type))
        {
            RadarInfo radarInfo = new RadarInfo();

            radarInfo.radarMessageType = TARGET.equals(type) ? RadarMessageType.TARGET : RadarMessageType.STATUS;
            radarInfo.unitId = tokens[UNIT_ID_INDEX];
            radarInfo.time = Long.parseLong(tokens[TIME_INDEX]);
            radarInfo.lat = Double.parseDouble(tokens[LAT_INDEX]);
            radarInfo.lon = Double.parseDouble(tokens[LON_INDEX]);

            // The last value in a status message is its battery level.
            if (radarInfo.radarMessageType == RadarMessageType.STATUS)
            {
                radarInfo.batteryLevel = Double.parseDouble(tokens[BATTERY_LEVEL_INDEX]);
            }
            // The last value in a target message is the target's id.
            else
            {
                radarInfo.targetId = tokens[TARGET_ID_INDEX];
            }

            return radarInfo;
        }

        return null;
    }

    /**
     * Defines the types of messages contained in radar data.
     */
    public enum RadarMessageType
    {
        STATUS,
        TARGET
    }

    /**
     * The results of parsing a line of valid radar data.
     */
    public static class RadarInfo
    {
        /**
         * The type of message this is.
         *
         * @see RadarMessageType
         */
        public RadarMessageType radarMessageType;

        /**
         * The id of the unit that generated this message.
         */
        public String unitId;

        /**
         * If this is a status message, this value will be null. If this is a "hit" message, this will be the id of the
         * target.
         */
        public String targetId;

        /**
         * The time that the message was generated by the radar.
         */
        public long time;

        /**
         * If this is a status message, the latitude of the radar device. If it's a hit message, it's the latitude where
         * the hit was detected.
         */
        public double lat;

        /**
         * If this is a status message, the longitude of the radar device. If it's a hit message, it's the longitude where
         * the hit was detected.
         */
        public double lon;

        /**
         * If this is a status message, this will be the radar device's battery level. If it's a hit message, it will be
         * null.
         */
        public double batteryLevel;
    }
}