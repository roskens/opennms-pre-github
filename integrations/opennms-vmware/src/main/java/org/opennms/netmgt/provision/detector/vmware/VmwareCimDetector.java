package org.opennms.netmgt.provision.detector.vmware;

import org.opennms.netmgt.provision.detector.simple.HttpDetector;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
/**
 * <p>VmwareCimDetector class.</p>
 *
 * @author Christian Pape
 */
@Scope("prototype")
public class VmwareCimDetector extends HttpDetector {


    private static final String DEFAULT_SERVICE_NAME = "VMwareCim-HostSystem";
    private static final int DEFAULT_PORT = 5989;

    /**
     * <p>Constructor for HttpsDetector.</p>
     */
    public VmwareCimDetector() {
        super(DEFAULT_SERVICE_NAME, DEFAULT_PORT);
        setUseSSLFilter(true);
        setUrl("/");
        setMaxRetCode(500);
    }

    /**
     * Constructor for creating a non-default service based on this protocol
     *
     * @param serviceName a {@link java.lang.String} object.
     * @param port        a int.
     */
    public VmwareCimDetector(final String serviceName, final int port) {
        super(serviceName, port);
    }
}
