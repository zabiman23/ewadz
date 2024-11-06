package a2ews.takx.plugin.service;

import gov.takx.api.plugin.commservices.ICommPath;

/**
 * Interface that is implemented by the connection monitor feature.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
public interface IConnectionMonitor
{
    /**
     * Registers a listener and notifies it of any existing comm paths.
     *
     * @param connectionListener The listener to register
     */
    void registerListener(IConnectionListener connectionListener);

    /**
     * Notifies all listeners that a connection was added.
     *
     * @param path The connection comm path
     */
    void addConnection(ICommPath path);

    /**
     * Notifies all listeners that a connection was removed.
     *
     * @param path The connection comm path
     */
    void removeConnection(ICommPath path);

    void removeAllConnections(); // This is the new method to be implemented
}
