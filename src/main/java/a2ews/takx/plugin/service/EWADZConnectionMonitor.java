package a2ews.takx.plugin.service;

import gov.takx.api.plugin.commservices.ICommPath;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service that allows other plugins to register to be notified when a connection is added/removed.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
@ApplicationScoped
public class EWADZConnectionMonitor implements IConnectionMonitor
{
    private final List<ICommPath> commPaths = new ArrayList<>();

    private final Collection<IConnectionListener> connectionListeners = new CopyOnWriteArrayList<>();

    @Override
    public void registerListener(IConnectionListener connectionListener)
    {
        connectionListeners.add(connectionListener);
        commPaths.forEach(connectionListener::onConnectionAdded);
    }

    @Override
    public void addConnection(ICommPath path)
    {
        commPaths.add(path);
        connectionListeners.forEach(listener -> listener.onConnectionAdded(path));
    }

    @Override
    public void removeConnection(ICommPath path)
    {
        commPaths.remove(path);
        connectionListeners.forEach(listener -> listener.onConnectionRemoved(path));
    }

    @Override
    public void removeAllConnections() 
    {
        List<ICommPath> pathsToRemove = new ArrayList<>(commPaths);
        pathsToRemove.forEach(this::removeConnection);
    }
}
