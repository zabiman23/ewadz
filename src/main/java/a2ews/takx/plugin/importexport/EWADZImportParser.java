package a2ews.takx.plugin.importexport;

import a2ews.takx.plugin.device.RadarParser;
import a2ews.takx.plugin.device.EWADZDeviceConstants;
import a2ews.takx.plugin.device.target.EWADZTargetDeviceConstants;
import gov.takx.api.io.ImportExportResult;
import gov.takx.api.messages.IMapEntity;
import gov.takx.api.messages.IPrePersistRaptorDataMessage;
import gov.takx.api.messages.IRaptorDataStructure;
import gov.takx.api.plugin.io.ICancellableImportParser;
import gov.takx.api.plugin.io.IImportExportPluginDelegate;
import gov.takx.api.plugin.io.IImportMonitor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.IOException;

/**
 * Parser for parsing a single file during the import.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
public class EWADZImportParser implements ICancellableImportParser
{
    private static final String RADAR_INFO_LIST_KEY = "Radar Info List";
    private final IImportExportPluginDelegate delegate;
    private final Map<String, Object> affectedObjects;
    private boolean isCancelled = false;
    private IImportMonitor importMonitor;

    public EWADZImportParser(IImportExportPluginDelegate delegate, Map<String, Object> affectedObjects)
    {
        this.delegate = delegate;
        this.affectedObjects = affectedObjects;
    }

    /**
     * Determines if at least one line is valid in the file.
     *
     * @return Successful result if at least one line is able to be parsed
     */
    public static ImportExportResult validateFile(String filePath)
    {
        ImportExportResult result = new ImportExportResult();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                RadarParser.RadarInfo radarInfo = RadarParser.parse(line);

                if (radarInfo != null)
                {
                    result.setSuccess(true);
                    return result;
                }
            }
        } catch (Exception e)
        {
            result.setMessage(e.getMessage());
        }

        return result;
    }

    /**
     * Parses file to determine number of records. The parsed objects are stored in the affectedObjects map so that they
     * don't need to be parsed again in the parse method.
     *
     * @return Successful result if at least one line is able to be parsed
     */
    public static ImportExportResult preParseFile(String filePath)
    {
        ImportExportResult result = new ImportExportResult();

        List<RadarParser.RadarInfo> radarInfoList = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                RadarParser.RadarInfo radarInfo = RadarParser.parse(line);

                if (radarInfo != null)
                {
                    radarInfoList.add(radarInfo);
                }
            }
        } catch (Exception e)
        {
            result.setMessage(e.getMessage());
        }

        if (!radarInfoList.isEmpty())
        {
            result.setDataPointCount(radarInfoList.size());
            result.getAffectedObjects().put(RADAR_INFO_LIST_KEY, radarInfoList);
            result.setSuccess(true);
        }

        return result;
    }

    @Override
    public ImportExportResult parse()
    {
        ImportExportResult result = new ImportExportResult();

        IMapEntity ewadzMapEntity = delegate.getMapEntity(EWADZDeviceConstants.FAMILY, EWADZDeviceConstants.TYPE);
        IRaptorDataStructure statusStructure = ewadzMapEntity.getRaptorDataStructure(
                EWADZDeviceConstants.STATUS_MESSAGE);

        IMapEntity targetMapEntity = delegate.getMapEntity(EWADZTargetDeviceConstants.FAMILY, EWADZTargetDeviceConstants.TYPE);
        IRaptorDataStructure targetStructure = targetMapEntity.getRaptorDataStructure(
                EWADZTargetDeviceConstants.TARGET_MESSAGE);

        List<RadarParser.RadarInfo> radarInfoList = (List<RadarParser.RadarInfo>) affectedObjects.get(RADAR_INFO_LIST_KEY);

        // This will happen if the file provided does not contain ewadz data, so just return a result of failure.
        if (radarInfoList == null)
        {
            result.setSuccess(false);
            result.setMessage("File did not contain EWADZ radar data");
            return result;
        }

        for (RadarParser.RadarInfo radarInfo : radarInfoList)
        {
            if (isCancelled)
            {
                break;
            }

            IPrePersistRaptorDataMessage rdm = null;
            if (radarInfo.radarMessageType == RadarParser.RadarMessageType.STATUS)
            {
                rdm = statusStructure.createRaptorDataMessage(radarInfo.unitId);
                rdm.setDouble(EWADZDeviceConstants.BATTERY_LEVEL, radarInfo.batteryLevel);
            } else if (radarInfo.radarMessageType == RadarParser.RadarMessageType.TARGET)
            {
                rdm = targetStructure.createRaptorDataMessage(radarInfo.targetId);
            }

            if (rdm != null)
            {
                rdm.setImported(true);
                rdm.setLocation(radarInfo.lat, radarInfo.lon);
                rdm.setTime(radarInfo.time);

                delegate.sendRaptorDataMessage(rdm);

                result.incrementDataPointCount();
                importMonitor.onDataMessage(rdm);
            }
        }

        result.setSuccess(true);
        return result;
    }

    @Override
    public void cancel()
    {
        isCancelled = true;
    }

    @Override
    public void setMonitor(IImportMonitor iImportMonitor)
    {
        importMonitor = iImportMonitor;
    }
}
