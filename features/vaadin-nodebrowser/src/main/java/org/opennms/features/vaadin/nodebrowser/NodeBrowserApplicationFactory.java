package org.opennms.features.vaadin.nodebrowser;

import com.vaadin.Application;
import org.opennms.netmgt.dao.NodeDao;
import org.ops4j.pax.vaadin.AbstractApplicationFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class NodeBrowserApplicationFactory extends AbstractApplicationFactory {
    private NodeDao nodeDao;

    /* (non-Javadoc)
     * @see org.ops4j.pax.vaadin.ApplicationFactory#createApplication(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public Application createApplication(HttpServletRequest request) throws ServletException {
        if (nodeDao == null) {
            throw new RuntimeException("nodeDao cannot be null.");
        }
        NodeBrowserApplication app = new NodeBrowserApplication();
        app.setNodeDao(nodeDao);
        return app;
    }

    /* (non-Javadoc)
     * @see org.ops4j.pax.vaadin.ApplicationFactory#getApplicationClass()
     */
    @Override
    public Class<? extends Application> getApplicationClass() throws ClassNotFoundException {
        return NodeBrowserApplication.class;
    }

    /**
     * Sets the OpenNMS Node DAO.
     *
     * @param nodeDao the new OpenNMS Node DAO
     */
    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }
}
