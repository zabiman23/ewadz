package a2ews.takx.plugin.service;

import gov.takx.api.plugin.commservices.ICommPath;

import java.util.EventListener;

/**
 * Interface to be implemented by connection monitor feature listeners. Note: the listeners don't have to implement this
 * actual interface; they simply need to implement an interface that matches this signature.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
public interface IConnectionListener extends EventListener
{
    void onConnectionAdded(ICommPath path);

    void onConnectionRemoved(ICommPath path);
}
