/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2008 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 * OpenNMS Licensing       <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 */
package org.opennms.netmgt.provision.schedule;

import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.quartz.CronTrigger;


/**
 * @author Donald Desloge
 *
 */
public class ScheduleProvisionAdapterTest {
    
    
    
    @Before
    public void setUp() {
        
        
    }
    
    @Test
    public void testCRONExpressionConversion() throws ParseException {
       CronTrigger trigger = new CronTrigger();
       trigger.setCronExpression("0 0 12 * * ?");
       Date d = trigger.getFireTimeAfter(new Date());
       assertNotNull(d);
       Date currentTime = new Date();
       long delta = (d.getTime() - currentTime.getTime());
       assertNotNull(delta);
       System.out.println(delta);
    }
    
    @Test
    public void testSchedulerOutput() {
       SchedulerImpl schedule = new SchedulerImpl("1s");
       
       Long interval = schedule.getSchedule();
       assertNotNull(interval);
    }
}
