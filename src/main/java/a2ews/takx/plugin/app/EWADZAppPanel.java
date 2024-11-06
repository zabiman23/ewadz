package a2ews.takx.plugin.app;

import a2ews.takx.plugin.service.IConnectionListener;
import a2ews.takx.plugin.service.IConnectionMonitor;
import gov.takx.api.annotation.PseudoBean;
import gov.takx.api.coordinatesystem.ILocation;
import gov.takx.api.globe.IGlobeDelegate;
import gov.takx.api.mapobject.IMapObject;
import gov.takx.api.plugin.app.IAppPluginDelegate;
import gov.takx.api.plugin.commservices.ICommPath;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The panel that is displayed when the app is running to allow the user to interact with targets.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 */
@PseudoBean
public class EWADZAppPanel extends JPanel implements IConnectionListener
{
    private JLabel connectionStatusLabel;

    private JList<IMapObject> targetList;

    /**
     * A reference to the delegate for accessing core TAKX functionality.
     */
    private final IAppPluginDelegate delegate;

    /**
     * Model for target list.
     */
    private final DefaultListModel<IMapObject> listModel;

    @Inject
    private IConnectionMonitor connectionMonitor;

    /**
     * Constructor.
     *
     * @param delegate A reference to the app plugin delegate
     */
    public EWADZAppPanel(IAppPluginDelegate delegate)
    {
        this.delegate = delegate;
        initializeUIComponents();

        listModel = new DefaultListModel<>();
        targetList.setModel(listModel);
        targetList.setCellRenderer(new TargetListCellRenderer());
    }

    @PostConstruct
    private void postConstruct()
    {
        connectionMonitor.registerListener(this);
    }

    @Override
    public void onConnectionAdded(ICommPath path)
    {
        connectionStatusLabel.setText("Connected");
        connectionStatusLabel.setForeground(Color.GREEN);
    }

    @Override
    public void onConnectionRemoved(ICommPath path)
    {
        connectionStatusLabel.setText("Not connected");
        connectionStatusLabel.setForeground(new JTextField().getForeground());
    }

    /**
     * Adds a target to the list.
     *
     * @param mapObject The map object which represents the target
     */
    public void addTarget(IMapObject mapObject)
    {
        listModel.addElement(mapObject);
    }

    /**
     * Called when mouse click occurs on target list.
     *
     * @param e The mouse event
     */
    private void targetListMouseClicked(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
        {
            IMapObject mapObject = targetList.getSelectedValue();
            if (mapObject != null)
            {
                ILocation mapObjectLocation = mapObject.getLocation();
                IGlobeDelegate lastFocusedGlobe = delegate.getGlobeManager().getLastFocusedGlobe();
                lastFocusedGlobe.goTo(mapObjectLocation);
            }
        }
    }

    /**
     * Create the necessary UI components and add them to the panel. For this UI, the {@link GridBagLayout} was used due
     * to its flexibility. For more information on using this layout, see the
     * <a href="https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html">tutorial</a>.
     */
    private void initializeUIComponents()
    {
        int padding = 5;

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{0, 0};
        layout.rowHeights = new int[]{0, 0};
        layout.columnWeights = new double[]{0, 1.0}; // Allow the 2nd column to grow
        layout.rowWeights = new double[]{0, 1.0}; // Allow the 2nd row to grow
        setLayout(layout);

        // Add connection label
        JLabel connectionLabel = new JLabel();
        connectionLabel.setText("EWADZ Status:");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 0, padding, padding);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        add(connectionLabel, constraints);

        // Add connection status label
        connectionStatusLabel = new JLabel();
        connectionStatusLabel.setText("Unknown");

        constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 0, padding, 0);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(connectionStatusLabel, constraints);

        // Add scroll pane with list to a panel
        targetList = new JList<>();
        targetList.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                targetListMouseClicked(e);
            }
        });

        JScrollPane targetListScrollPane = new JScrollPane();
        targetListScrollPane.setViewportView(targetList);

        JPanel targetPanel = new JPanel();
        targetPanel.setBorder(new TitledBorder("Targets"));

        layout = new GridBagLayout();
        layout.columnWidths = new int[]{0};
        layout.rowHeights = new int[]{0};
        layout.columnWeights = new double[]{1.0}; // Allow the column to grow
        layout.rowWeights = new double[]{1.0}; // Allow the row to grow
        targetPanel.setLayout(layout);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        targetPanel.add(targetListScrollPane, constraints);

        // Add target panel
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        add(targetPanel, constraints);
    }

    private static class TargetListCellRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof IMapObject mapObject) setText(mapObject.getName());

            return this;
        }
    }
}
