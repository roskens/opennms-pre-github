package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.opennms.features.vaadin.dashboard.config.DashletSelector;
import org.opennms.features.vaadin.dashboard.model.DashletSelectorAccess;

@SuppressWarnings("serial")
@Theme("dashboard")
@Title("OpenNMS Dashboard")
public class WallboardConfigUI extends UI implements DashletSelectorAccess {
    private DashletSelector m_dashletSelector;
    private static Notification m_notification = new Notification("Message", Notification.Type.TRAY_NOTIFICATION);

    public WallboardConfigUI() {
    }

    public void setDashletSelector(DashletSelector dashletSelector) {
        this.m_dashletSelector = dashletSelector;
    }

    public DashletSelector getDashletSelector() {
        return m_dashletSelector;
    }

    @Override
    protected void init(VaadinRequest request) {
        setContent(new WallboardConfigView(m_dashletSelector));
    }

    public void notifyMessage(String message, String description) {
        m_notification.setCaption(message);
        m_notification.setDescription(description);
        m_notification.setDelayMsec(1000);
        if (getUI() != null) {
            if (getPage() != null) {
                m_notification.show(getUI().getPage());
            }
        }
    }
}
