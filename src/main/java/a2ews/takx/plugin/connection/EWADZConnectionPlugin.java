package a2ews.takx.plugin.connection;

import gov.takx.api.plugin.annotations.ConnectionPlugin;
import gov.takx.api.plugin.annotations.PluginDescriptor;
import gov.takx.api.plugin.annotations.PluginSpecification;
import gov.takx.api.plugin.connection.AConnectionPlugin;
import gov.takx.api.plugin.connection.IConnectionPluginController;

import gov.takx.api.plugin.support.SupportedPlatformVersion;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

/**
 * Specifies the {@link IConnectionPluginController} to use for this plugin.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
@ConnectionPlugin(
        spec = @PluginSpecification(
                family = EWADZConnectionConstants.FAMILY,
                type = EWADZConnectionConstants.TYPE,
                version = EWADZConnectionConstants.VERSION,
                iconPath = "ewadz.png",
                minimumSupportedVersion = SupportedPlatformVersion.V3_4 // update to Jakarta
        ),
        descriptor = @PluginDescriptor(
                name = "EWADZ"
        ),
        panelIconPath = "ewadz.png"
)
@SuppressWarnings("UnusedDeclaration")
public class EWADZConnectionPlugin extends AConnectionPlugin
{
    @Inject
    private Instance<EWADZConnectionPluginController> ewadzConnectionPluginControllerFactory;

    @Override
    public IConnectionPluginController createConnectionController()
    {
        // Because EWADZConnectionPluginController is a Dependent scoped bean a new instance will be returned for each call.
        // TODO: this causes a memory leak since the dependent bean will take on application scope. They need to be
        //  destroyed via the instance when the connection is removed.
        return ewadzConnectionPluginControllerFactory.get();
    }
}
