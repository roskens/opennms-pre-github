package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.data.util.BeanItemContainer;
import org.opennms.features.vaadin.dashboard.model.Wallboard;
import org.opennms.features.vaadin.dashboard.model.Wallboards;

import javax.xml.bind.JAXB;
import java.io.File;

public class WallboardProvider {
    private static WallboardProvider m_wallboardProvider = new WallboardProvider();
    private Wallboards m_wallboards = null;
    private File m_cfgFile = new File("etc/dashboard-config.xml");
    private BeanItemContainer<Wallboard> m_beanItemContainer = new BeanItemContainer<Wallboard>(Wallboard.class);

    private WallboardProvider() {
        load();
    }

    public BeanItemContainer<Wallboard> getBeanContainer() {
        return m_beanItemContainer;
    }

    public static WallboardProvider getInstance() {
        return m_wallboardProvider;
    }

    public synchronized void save() {
        if (m_wallboards == null) {
            load();
        }

        JAXB.marshal(m_wallboards, m_cfgFile);
    }

    public synchronized void load() {
        if (!m_cfgFile.exists()) {
            m_wallboards = new Wallboards();
        } else {
            m_wallboards = JAXB.unmarshal(m_cfgFile, Wallboards.class);
        }

        updateBeanItemContainer();
    }

    private void updateBeanItemContainer() {
        m_beanItemContainer.removeAllItems();
        for (Wallboard wallboard : m_wallboards.getWallboards()) {
            m_beanItemContainer.addItem(wallboard);
        }
    }

    synchronized public boolean containsWallboard(String title) {
        for (Wallboard wallboard : m_wallboards.getWallboards()) {
            if (wallboard.getTitle().equals(title)) {
                return true;
            }
        }

        return false;
    }

    synchronized public Wallboard getWallboard(String title) {
        for (Wallboard wallboard : m_wallboards.getWallboards()) {
            if (wallboard.getTitle().equals(title)) {
                return wallboard;
            }
        }

        return null;
    }

    synchronized public boolean containsWallboard(Wallboard wallboard) {
        return m_wallboards.getWallboards().contains(wallboard);
    }

    synchronized public void addWallboard(Wallboard wallboard) {
        if (m_wallboards == null) {
            load();
        }

        m_wallboards.getWallboards().add(wallboard);

        save();

        updateBeanItemContainer();
    }

    synchronized public void removeWallboard(Wallboard wallboard) {
        if (m_wallboards == null) {
            load();
        }

        m_wallboards.getWallboards().remove(wallboard);

        save();

        updateBeanItemContainer();
    }
}
