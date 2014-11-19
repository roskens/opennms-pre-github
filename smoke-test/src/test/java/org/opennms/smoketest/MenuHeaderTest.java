/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2013-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.smoketest;

import java.util.concurrent.TimeUnit;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.opennms.smoketest.expectations.ExpectationBuilder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MenuHeaderTest extends OpenNMSSeleniumTestCase {
    @Test
    public void testMenuEntries() throws Exception {

        new ExpectationBuilder("link=Node List").withText("/ Node List").or().withText("Node Interfaces").check(selenium);
        new ExpectationBuilder("link=Search").withText("Search for Nodes").check(selenium);
        new ExpectationBuilder("link=Outages").withText("Outage Menu").check(selenium);
        new ExpectationBuilder("link=Path Outages")
            .withText("All Path Outages").and().withText("Critical Path Node").check(selenium);
        // Dashboards below
        new ExpectationBuilder("link=Events").withText("Event Queries").check(selenium);
        new ExpectationBuilder("link=Alarms").withText("Alarm Queries").check(selenium);
        new ExpectationBuilder("link=Notifications").withText("Notification queries").check(selenium);
        new ExpectationBuilder("link=Assets").withText("Search Asset Information").check(selenium);
        new ExpectationBuilder("link=Reports")
            .withText("Resource Graphs").and().withText("Database Reports").check(selenium);
        new ExpectationBuilder("link=Charts").withText("/ Charts").check(selenium);
        new ExpectationBuilder("link=Surveillance")
        .withText("Finding status for nodes in").or().withText("Surveillance View:").check(selenium);
        new ExpectationBuilder("link=Distributed Status")
            .withText("Distributed Status Summary").or()
            .withText("No applications have been defined for this system").check(selenium);
        // Maps below
        new ExpectationBuilder("link=Add Node").withText("Basic Attributes (required)").check(selenium);
        new ExpectationBuilder("link=Admin").withText("Node Provisioning").check(selenium);
        new ExpectationBuilder("link=Support").withText("Commercial Support").check(selenium);

        // Dashboard Menu(s)
        new ExpectationBuilder("//a[@href='dashboards.htm']")
            .withText("Dashboard").and().withText("Ops Board").check(selenium);
        new ExpectationBuilder("//div[@id='content']//a[contains(text(), 'Dashboard')]")
            .waitFor(2, TimeUnit.SECONDS).withText("Surveillance View: default").check(selenium);
        // back to dashboard to make it happy
        new ExpectationBuilder("//a[@href='dashboards.htm']")
        .withText("Dashboard").and().withText("Ops Board").check(selenium);
        new ExpectationBuilder("//div[@id='content']//a[contains(text(), 'Ops Board')]")
            .waitFor(2, TimeUnit.SECONDS).withText("Ops Panel").check(selenium);
        ExpectationBuilder.frontPage().check(selenium);

        // Map Menu(s)
        final ExpectationBuilder mapLink = new ExpectationBuilder("//a[@href='maps.htm']")
            .withText("Topology").and().withText("SVG");

        mapLink.check(selenium);
        new ExpectationBuilder("//div[@id='content']//a[contains(text(), 'Distributed')]")
            .waitFor(2, TimeUnit.SECONDS).withText("Last update:").check(selenium);
        ExpectationBuilder.frontPage().check(selenium);
        mapLink.check(selenium);
        new ExpectationBuilder("//div[@id='content']//a[contains(text(), 'Topology')]")
            .waitFor(10, TimeUnit.SECONDS).withText("Select All").check(selenium);
        mapLink.check(selenium);
        new ExpectationBuilder("//div[@id='content']//a[contains(text(), 'Geographical')]")
            .waitFor(5, TimeUnit.SECONDS).withText("Show Severity").check(selenium);
        mapLink.check(selenium);
        new ExpectationBuilder("//div[@id='content']//a[contains(text(), 'SVG')]")
            .waitFor(2, TimeUnit.SECONDS).withText("/ Network Topology Maps").check(selenium);
    }

}
