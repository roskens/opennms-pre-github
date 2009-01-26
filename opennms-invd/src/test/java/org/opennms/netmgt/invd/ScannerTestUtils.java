package org.opennms.netmgt.invd;

import org.opennms.netmgt.config.invd.Package;
import org.opennms.netmgt.config.invd.Filter;
import org.opennms.netmgt.config.invd.Service;
import org.opennms.netmgt.config.invd.Parameter;
import org.opennms.netmgt.config.InvdPackage;

public class ScannerTestUtils {
    static ScannerSpecification createScannerSpec(String svcName, InventoryScanner svcCollector, String collectionName) {
        Package pkg = new Package();
        Filter filter = new Filter();
        filter.setContent("IPADDR IPLIKE *.*.*.*");
        pkg.setFilter(filter);
        Service service = new Service();
        service.setName(svcName);
        Parameter collectionParm = new Parameter();
        collectionParm.setKey("collection");
        collectionParm.setValue(collectionName);
        service.addParameter(collectionParm);
        pkg.addService(service);

        InvdPackage wpkg = new InvdPackage(pkg);        
        return new ScannerSpecification(wpkg, svcName, svcCollector);
    }
}
