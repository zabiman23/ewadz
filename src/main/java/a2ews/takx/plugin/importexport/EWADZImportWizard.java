package a2ews.takx.plugin.importexport;

import gov.takx.api.io.ImportExportResult;
import gov.takx.api.plugin.io.ACancellableImportWizard;
import gov.takx.api.plugin.io.ICancellableImportParser;
import gov.takx.commons.wizard.IWizardProperties;

import java.util.Map;

/**
 * Import wizard which handles parsing for import.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
public class EWADZImportWizard extends ACancellableImportWizard
{
    @Override
    protected ImportExportResult validateFileForImport(String fileName)
    {
        return EWADZImportParser.validateFile(fileName);
    }

    @Override
    protected ImportExportResult preParseFile(String fileName)
    {
        return EWADZImportParser.preParseFile(fileName);
    }

    @Override
    protected ICancellableImportParser createImportParser(String filePath, Map<String, Object> affectedObjects,
                                                          IWizardProperties wizardProperties)
    {
        return new EWADZImportParser(delegate, affectedObjects);
    }

    // TODO KMB: This is necessary to help fix TAKXPLUGIN-92, but it really points out a deficiency in the API: extending
    //  ACancellableImportWizard should not imply that you are overriding the default importers. If you want to support
    //  that, you should have to explicitly override this method to return true.
    @Override
    public boolean overrideDefaultImporters()
    {
        return false;
    }
}
