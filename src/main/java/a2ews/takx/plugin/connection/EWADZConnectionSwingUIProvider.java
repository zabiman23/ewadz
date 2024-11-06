package a2ews.takx.plugin.connection;

import gov.takx.api.plugin.annotations.ConnectionSwingUIProvider;
import gov.takx.api.plugin.connection.IConnectionPanel;
import gov.takx.api.ui.IConnectionSwingUIProvider;

/**
 * The UI provider for the connection plugin controller that provides the necessary Swing UI components for the TAKX
 * thick client.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
@SuppressWarnings("UnusedDeclaration")
@ConnectionSwingUIProvider(forPluginController = EWADZConnectionPluginController.class)
public class EWADZConnectionSwingUIProvider implements IConnectionSwingUIProvider
{
    /**
     * A reference to the panel that is displayed when the user selects a connection from the toolbar.
     */
    private EWADZConnectionPanel connectionPanel;

    /**
     * A reference to the connection plugin controller for callbacks.
     */
    private final EWADZConnectionPluginController controller;

    /**
     * Constructor.  TAKX will automatically inject the controller parameter when calling this constructor.
     * There must be exactly one constructor.
     *
     * @param controller A reference to the connection plugin controller used for callbacks.
     */
    public EWADZConnectionSwingUIProvider(EWADZConnectionPluginController controller)
    {
        this.controller = controller;
    }

    /**
     * Provides the connection panel when requested by the TAKX core.
     *
     * @return The connection panel
     */
    @Override
    public IConnectionPanel getConnectionPanel()
    {
        if (connectionPanel == null)
        {
            connectionPanel = new EWADZConnectionPanel(controller);
        }

        return connectionPanel;
    }
}
