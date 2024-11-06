package a2ews.takx.plugin.connection;

import a2ews.takx.plugin.EWADZConstants;
import a2ews.takx.plugin.service.IConnectionMonitor;
import gov.takx.api.plugin.commservices.ICommListener;
import gov.takx.api.plugin.commservices.ICommPath;
import gov.takx.api.plugin.connection.AConnectionPluginController;
import gov.takx.api.plugin.connection.ConnectionState;
import gov.takx.api.plugin.connection.ICommServiceDelegate;
import gov.takx.api.plugin.connection.IConnectionPanel;
import gov.takx.api.ui.IConnectionSwingUIConsumer;
import gov.takx.commons.constants.CommServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;

/**
 * This class represents the controller responsible for managing connection lifecycles and user interface related to connections.
 * It interacts with the communication service to establish, manage, and tear down connections.
 * It also notifies other components via a connection monitor when connection states change.
 */
@Dependent
public class EWADZConnectionPluginController extends AConnectionPluginController implements IConnectionSwingUIConsumer,
        ICommListener
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private EWADZConnectionPanel connectionPanel;
    private String ipAddress = EWADZConstants.IP_STRING;
    private String port = EWADZConstants.PORT;
    private final Instance<IConnectionMonitor> connectionMonitorInstance;

    @Inject
    public EWADZConnectionPluginController(Instance<IConnectionMonitor> connectionMonitorInstance) {
        super(EWADZConstants.FAMILY, EWADZConstants.TYPE);
        this.connectionMonitorInstance = connectionMonitorInstance;
    }

    /**
     * Handles the event triggered by the user's click on the Connect button in the UI,
     * initiating the process to establish a new communication connection based on the user-provided IP address and port.
     */
    @Override
    public void onNewConnection() {
        ipAddress = connectionPanel.getIpAddress();
        port = connectionPanel.getPort();
        connect();
    }

    /**
     * Manages automatic connections based on configuration settings provided at startup or through dynamic configuration.
     * @param configuration Configuration map containing connection parameters like IP and port.
     * @param autoStart A boolean flag to determine if the connection should be opened immediately upon configuration.
     */
    @Override
    public void onOpenConnection(Map<String, Object> configuration, boolean autoStart) {
        ipAddress = (String) configuration.get(EWADZConstants.IP_STRING);
        port = (String) configuration.get(EWADZConstants.PORT);

        if (autoStart) {
            connect();
        }
    }

    /**
     * Establishes a communication path using the communication manager. It handles conditions where the path may already exist
     * or connection parameters might lead to conflicts.
     */
    private void connect() {
        delegate.setConnectionState(ConnectionState.Connecting);

        ICommServiceDelegate commServiceDelegate = delegate.getCommServiceDelegate();
        String commPathName = ipAddress + ":" + port;

        if (commServiceDelegate.getExistingCommPath(CommServiceType.CLIENT_SOCKET, commPathName) != null) {
            connectionPanel.showErrorMessage(commPathName + " already in use");
            logger.error("{} already in use", commPathName);
            delegate.removeConnection();
            return;
        }

        Runnable runnable = () -> {
            try {
                ICommPath commPath = commServiceDelegate.createCommPath(CommServiceType.CLIENT_SOCKET, commPathName, EWADZConstants.FORMAT);
                if (connectionMonitorInstance.isResolvable()) {
                    connectionMonitorInstance.get().addConnection(commPath);
                }
                delegate.setConnectionState(ConnectionState.Connected);
                delegate.setConnectionDisplayName("EWADZ " + commPathName);
            } catch (IOException e) {
                if (connectionPanel != null) {
                    connectionPanel.showErrorMessage("Error opening connection");
                }
                logger.error("Error opening connection for {}", commPathName, e);
                delegate.removeConnection();
            }
        };

        delegate.getExecutorService().submit(runnable);
    }


    /**
     * Handles the disconnection process initiated by the user, ensuring that all resources are properly released
     * and notifying relevant components of the change in connection state.
     */
    @Override
    public void onCloseConnection() {
        disposeConnection();
        delegate.setConnectionState(ConnectionState.Disconnected);
    }

    /**
     * Manages the full shutdown of the connection, including resource cleanup and deregistration from dependency injection frameworks,
     * preventing memory leaks and other resource wastage.
     */
    @Override
    public void onShutdown() {
        disposeConnection();
        connectionPanel = null;
    }

    /**
     * Triggered when a communication path is closed, whether expectedly or unexpectedly,
     * it manages the cleanup of resources and updates internal states accordingly.
     */
    @Override
    public void commPathClosed(ICommPath commPath, boolean wasExpected) {
        
        connectionMonitorInstance.get().removeConnection(commPath);
       
        delegate.setConnectionState(ConnectionState.Disconnected);

        if (!wasExpected) {
            connectionPanel.showErrorMessage("EWADZ connection closed unexpectedly");
        }
    }

    /**
     * Responds to the stopping of a communication path, providing a placeholder for additional cleanup or state update logic as needed.
     */
    @Override
    public void commPathStopped(ICommPath commPath, boolean wasExpected) {
    }

    /**
     * Processes raw byte data received from a communication path. This method provides a basic implementation
     * that can be expanded to include data parsing, authentication, or other preprocessing before further processing.
     */
    @Override
    public void parseBytes(byte[] bytes, int length, String source) {
    }

    /**
     * Restores the previously saved state of this controller, typically used when restarting or recovering from a pause.
     */
    @Override
    public void restore(String state) {
        String[] parts = state.split(":");
        if (parts.length == 2) {
            ipAddress = parts[0];
            port = parts[1];
        }
    }

    /**
     * Saves the current state of the controller, such as connection parameters, allowing for a smooth restart or recovery.
     */
    @Override
    public String store() {
        return ipAddress + ":" + port;
    }

    /**
     * Provides access to the currently configured IP address, potentially for display in the UI or logging.
     */
    //@Override
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Provides access to the currently configured port, potentially for display in the UI or logging.
     */
    //@Override
    public String getPort() {
        return port;
    }

    /**
     * Injects the connection panel interface, allowing this controller to interact directly with the UI elements related to connection management.
     */
    @Override
    public void setConnectionPanel(IConnectionPanel connectionPanel) {
        this.connectionPanel = (EWADZConnectionPanel) connectionPanel;
    }

    /**
     * Ensures all resources related to the current connection are properly disposed of, and notifies the connection monitor of these changes.
     */
    private void disposeConnection() {
        if (connectionMonitorInstance.isResolvable() && !connectionMonitorInstance.isUnsatisfied()) {
            connectionMonitorInstance.get().removeAllConnections(); // Adjust this method to match whatever your actual method is to clear all connections or handle them appropriately.
        }
    }

}
