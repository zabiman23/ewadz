package a2ews.takx.plugin.importexport;

import gov.takx.api.plugin.annotations.ImportExportSwingUIProvider;
import gov.takx.api.plugin.io.AFileImportSwingUIProvider;

/**
 * The UI provider for the import plugin that provides the necessary Swing UI components for the TAKX thick client.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
@ImportExportSwingUIProvider(forWizard = EWADZImportWizard.class)
public class EWADZImportSwingUIProvider extends AFileImportSwingUIProvider
{
    public EWADZImportSwingUIProvider(EWADZImportWizard importWizard)
    {
        super(importWizard);
    }
}
