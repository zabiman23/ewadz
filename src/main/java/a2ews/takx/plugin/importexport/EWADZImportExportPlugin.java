package a2ews.takx.plugin.importexport;

import gov.takx.api.plugin.annotations.ImportExportFormat;
import gov.takx.api.plugin.annotations.ImportExportPlugin;
import gov.takx.api.plugin.annotations.PluginDescriptor;
import gov.takx.api.plugin.annotations.PluginSpecification;
import gov.takx.api.plugin.io.AImportExportPlugin;
import gov.takx.api.plugin.io.IExportWizard;
import gov.takx.api.plugin.io.IImportWizard;
import gov.takx.api.plugin.support.SupportedPlatformVersion;

/**
 * Specifies the {@link IImportWizard} to use for this plugin.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
@ImportExportPlugin(
        spec = @PluginSpecification(
                family = EWADZImportExportConstants.FAMILY,
                type = EWADZImportExportConstants.TYPE,
                version = EWADZImportExportConstants.VERSION,
                iconPath = "ewadz.png",
                minimumSupportedVersion = SupportedPlatformVersion.V3_0
        ),
        descriptor = @PluginDescriptor(
                name = "EWADZ"
        ),
        formats = {
                @ImportExportFormat(
                        identifier = "EWADZ",
                        title = "EWADZ",
                        notes = "Import a data file in the EWADZ format",
                        fileTypes = {"csv"}
                )
        }
)
public class EWADZImportExportPlugin extends AImportExportPlugin
{
    @Override
    public IImportWizard createImporter()
    {
        // If import functionality is not required, return null.
        return new EWADZImportWizard();
    }

    @Override
    public IExportWizard createExporter()
    {
        // If export functionality is not required, return null.
        return null;
    }
}
