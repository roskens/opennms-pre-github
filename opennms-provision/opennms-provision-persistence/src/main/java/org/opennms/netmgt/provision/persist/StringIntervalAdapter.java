/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.provision.persist;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * <p>
 * StringIntervalAdapter class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 */
public class StringIntervalAdapter extends XmlAdapter<String, Duration> {

    /** Constant <code>DEFAULT_PERIOD_FORMATTER</code>. */
    public static final PeriodFormatter DEFAULT_PERIOD_FORMATTER = new PeriodFormatterBuilder().appendWeeks().appendSuffix("w").appendSeparator(" ").appendDays().appendSuffix("d").appendSeparator(" ").appendHours().appendSuffix("h").appendSeparator(" ").appendMinutes().appendSuffix("m").appendSeparator(" ").appendSeconds().appendSuffix("s").appendSeparator(" ").appendMillis().appendSuffix("ms").toFormatter();

    /** {@inheritDoc} */
    @Override
    public String marshal(Duration v) {
        Period p = v.toPeriod().normalizedStandard();
        return DEFAULT_PERIOD_FORMATTER.print(p);
    }

    /** {@inheritDoc} */
    @Override
    public Duration unmarshal(String v) {
        return DEFAULT_PERIOD_FORMATTER.parsePeriod(v).toStandardDuration();
    }

}
