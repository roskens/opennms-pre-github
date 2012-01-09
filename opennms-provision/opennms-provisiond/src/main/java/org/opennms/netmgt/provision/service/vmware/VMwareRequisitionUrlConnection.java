package org.opennms.netmgt.provision.service.vmware;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import org.apache.commons.io.IOExceptionWithCause;
import org.opennms.core.utils.url.GenericURLConnection;
import org.opennms.core.xml.JaxbUtils;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.netmgt.provision.persist.requisition.RequisitionAsset;
import org.opennms.netmgt.provision.persist.requisition.RequisitionInterface;
import org.opennms.netmgt.provision.persist.requisition.RequisitionNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: indigo Date: 1/4/12 Time: 2:24 PM To change
 * this template use File | Settings | File Templates.
 */
public class VMwareRequisitionUrlConnection extends GenericURLConnection {
    private Logger logger = LoggerFactory.getLogger(VMwareRequisitionUrlConnection.class);

    private String m_hostname = null;
    private String m_username = null;
    private String m_password = null;
    private String m_foreignSource = null;

    private static Map<String, String> m_args = null;

    private ServiceInstance m_serviceInstance = null;
    private Requisition m_requisition = null;

    public VMwareRequisitionUrlConnection(URL url) throws MalformedURLException, RemoteException {
        super(url);

        m_hostname = url.getHost();
        m_username = getUsername(url);
        m_password = getPassword(url);

        m_args = getQueryArgs(url);

        m_serviceInstance = new ServiceInstance(new URL("https://" + m_hostname + "/sdk"), m_username, m_password, true);

        m_foreignSource = "vmware-" + m_hostname;
    }

    @Override
    public void connect() throws IOException {
        // To change body of implemented methods use File | Settings | File
        // Templates.
    }

    private RequisitionNode createRequisitionNode(Set<String> ipAddresses, ManagedEntity managedEntity) {
        RequisitionNode requisitionNode = new RequisitionNode();

        // Setting the node label
        requisitionNode.setNodeLabel(managedEntity.getName());

        // Foreign Id consisting of vcenter Ip and managed entity Id
        requisitionNode.setForeignId(m_hostname + "/" + managedEntity.getMOR().getVal());

        boolean primary = true;

        // add all given interfaces
        for (String ipAddress : ipAddresses) {
            RequisitionInterface requisitionInterface = new RequisitionInterface();
            requisitionInterface.setIpAddr(ipAddress);

            //  the first one will be primary
            if (primary) {
                requisitionInterface.setSnmpPrimary("P");
                primary = false;
            }

            requisitionInterface.setManaged(Boolean.TRUE);
            requisitionInterface.setStatus(Integer.valueOf(1));
            requisitionNode.putInterface(requisitionInterface);
        }
        /* For now we use displaycategory, notifycategory and pollercategory for storing
         * the vcenter Ip address, the username and the password
         */

        RequisitionAsset requisitionAssetHostname = new RequisitionAsset("displayCategory", m_hostname);
        requisitionNode.putAsset(requisitionAssetHostname);

        RequisitionAsset requisitionAssetUsername = new RequisitionAsset("notifyCategory", m_username);
        requisitionNode.putAsset(requisitionAssetUsername);

        RequisitionAsset requisitionAssetPassword = new RequisitionAsset("pollerCategory", m_password);
        requisitionNode.putAsset(requisitionAssetPassword);

        return requisitionNode;
    }

    private Requisition buildVMwareRequisition() throws UnknownHostException, RemoteException {
        // for now, set the foreign source to the specified vcenter host
        m_requisition = new Requisition(m_foreignSource);

        iterateHostSystems();
        iterateVirtualMachines();

        return m_requisition;
    }

    private void iterateHostSystems() throws RemoteException, UnknownHostException {
        ManagedEntity[] hostSystems;

        // search for host systems (esx hosts)
        hostSystems = new InventoryNavigator(m_serviceInstance.getRootFolder()).searchManagedEntities("HostSystem");

        if (hostSystems != null) {

            for (ManagedEntity managedEntity : hostSystems) {
                HostSystem hostSystem = (HostSystem) managedEntity;

                // check for correct key/value-pair
                if (checkForAttribute(hostSystem)) {
                    logger.debug("Adding Host System '{}'", hostSystem.getName());

                    // iterate over all service console networks and add interface Ip addresses
                    LinkedHashSet<String> ipAddresses = new LinkedHashSet<String>();

                    for (HostVirtualNic hostVirtualNic : hostSystem.getHostNetworkSystem().getNetworkInfo().getConsoleVnic()) {
                        ipAddresses.add(hostVirtualNic.getSpec().getIp().getIpAddress());
                    }

                    // create the new node...
                    RequisitionNode node = createRequisitionNode(ipAddresses, hostSystem);

                    // ...and add it to the requisition
                    m_requisition.insertNode(node);
                }
            }
        }
    }

    private void iterateVirtualMachines() throws RemoteException, UnknownHostException {
        ManagedEntity[] virtualMachines;

        // search for all virtual machines
        virtualMachines = new InventoryNavigator(m_serviceInstance.getRootFolder()).searchManagedEntities("VirtualMachine");

        if (virtualMachines != null) {

            // check for correct key/value-pair
            for (ManagedEntity managedEntity : virtualMachines) {
                VirtualMachine virtualMachine = (VirtualMachine) managedEntity;

                if (checkForAttribute(virtualMachine)) {
                    logger.debug("Adding Virtual Machine '{}'", virtualMachine.getName());

                    LinkedHashSet<String> ipAddresses = new LinkedHashSet<String>();

                    // add the Ip address reported by VMware tools, this should be primary
                    ipAddresses.add(virtualMachine.getGuest().getIpAddress());

                    // if possible, iterate over all virtual networks networks and add interface Ip addresses
                    if (virtualMachine.getGuest().getNet() != null) {
                        for (GuestNicInfo guestNicInfo : virtualMachine.getGuest().getNet()) {
                            for (String ipAddress : guestNicInfo.getIpAddress())
                                ipAddresses.add(ipAddress);
                        }
                    }

                    // create the new node...
                    RequisitionNode node = createRequisitionNode(ipAddresses, virtualMachine);

                    // add the operating system
                    if (virtualMachine.getGuest().getGuestFullName() != null) {
                        RequisitionAsset requisitionAsset = new RequisitionAsset("operatingSystem", virtualMachine.getGuest().getGuestFullName());
                        node.putAsset(requisitionAsset);
                    }

                    // ...and add it to the requisition
                    m_requisition.insertNode(node);
                }
            }
        }
    }

    private boolean checkForAttribute(ManagedEntity managedEntity) throws RemoteException {
        String key = m_args.get("key");
        String value = m_args.get("value");

        // if key/value is not set, return true
        if (key == null && value == null)
            return true;

        // if only key or value is set, return false
        if (key == null || value == null)
            return false;

        // get available values
        CustomFieldValue[] values = managedEntity.getCustomValue();
        // get available definitions
        CustomFieldDef[] defs = managedEntity.getAvailableField();

        // now search for the correct key/value pair
        for (int i = 0; defs != null && i < defs.length; i++) {
            if (key.equals(defs[i].getName())) {

                int targetIndex = defs[i].getKey();

                for (int j = 0; j < values.length; j++) {
                    if (targetIndex == values[j].getKey()) {
                        return value.equals(((CustomFieldStringValue) values[j]).value);
                    }
                }
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Creates a ByteArrayInputStream implementation of InputStream of the XML
     * marshaled version of the Requisition class. Calling close on this stream
     * is safe.
     */
    @Override
    public InputStream getInputStream() throws IOException {

        InputStream stream = null;

        try {
            Requisition r = buildVMwareRequisition();
            stream = new ByteArrayInputStream(jaxBMarshal(r).getBytes());
        } catch (IOException e) {
            logger.warn("getInputStream: Problem getting input stream: '{}'", e);
            throw e;
        } catch (Throwable e) {
            logger.warn("Problem getting input stream: '{}'", e);
            throw new IOExceptionWithCause("Problem getting input stream: " + e, e);
        }

        return stream;
    }

    /**
     * Utility to marshal the Requisition class into XML.
     *
     * @param r
     * @return a String of XML encoding the Requisition class
     * @throws javax.xml.bind.JAXBException
     */
    private String jaxBMarshal(Requisition r) throws JAXBException {
        return JaxbUtils.marshal(r);
    }
}
