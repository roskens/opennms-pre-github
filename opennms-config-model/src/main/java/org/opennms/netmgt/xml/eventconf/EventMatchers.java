/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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
package org.opennms.netmgt.xml.eventconf;

import static org.opennms.netmgt.xml.eventconf.Maskelement.TAG_HOST;
import static org.opennms.netmgt.xml.eventconf.Maskelement.TAG_INTERFACE;
import static org.opennms.netmgt.xml.eventconf.Maskelement.TAG_NODEID;
import static org.opennms.netmgt.xml.eventconf.Maskelement.TAG_SERVICE;
import static org.opennms.netmgt.xml.eventconf.Maskelement.TAG_SNMPHOST;
import static org.opennms.netmgt.xml.eventconf.Maskelement.TAG_SNMP_COMMUNITY;
import static org.opennms.netmgt.xml.eventconf.Maskelement.TAG_SNMP_EID;
import static org.opennms.netmgt.xml.eventconf.Maskelement.TAG_SNMP_GENERIC;
import static org.opennms.netmgt.xml.eventconf.Maskelement.TAG_SNMP_SPECIFIC;
import static org.opennms.netmgt.xml.eventconf.Maskelement.TAG_SOURCE;
import static org.opennms.netmgt.xml.eventconf.Maskelement.TAG_UEI;

import java.util.List;
import java.util.regex.Pattern;

import org.opennms.netmgt.eventd.datablock.EventUtil;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Parm;

/**
 * The Class EventMatchers.
 */
public abstract class EventMatchers {

    /**
     * False matcher.
     *
     * @return the event matcher
     */
    public static EventMatcher falseMatcher() {
        return new EventMatcher() {

            @Override
            public boolean matches(Event matchingEvent) {
                return false;
            }

            @Override
            public String toString() {
                return "false";
            }

        };
    }

    /**
     * True matcher.
     *
     * @return the event matcher
     */
    public static EventMatcher trueMatcher() {
        return new EventMatcher() {

            @Override
            public boolean matches(Event matchingEvent) {
                return true;
            }

            @Override
            public String toString() {
                return "true";
            }

        };
    }

    /**
     * Uei matcher.
     *
     * @param uei
     *            the uei
     * @return the event matcher
     */
    public static EventMatcher ueiMatcher(final String uei) {
        return new EventMatcher() {
            public boolean matches(org.opennms.netmgt.xml.event.Event matchingEvent) {
                String matchingUei = matchingEvent.getUei();
                return matchingUei != null && uei.equals(matchingUei);
            }

            @Override
            public String toString() {
                return "event.uei==" + uei;
            }
        };

    }

    /**
     * And.
     *
     * @param matchers
     *            the matchers
     * @return the event matcher
     */
    public static EventMatcher and(final EventMatcher... matchers) {
        return new EventMatcher() {

            @Override
            public boolean matches(Event matchingEvent) {
                for (EventMatcher matcher : matchers) {
                    if (!matcher.matches(matchingEvent)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public String toString() {
                StringBuilder buf = new StringBuilder();
                boolean first = true;
                for (EventMatcher matcher : matchers) {
                    if (first) {
                        first = false;
                    } else {
                        buf.append("&&");
                    }
                    buf.append("(").append(matcher).append(")");
                }

                return buf.toString();
            }
        };
    }

    /**
     * Or.
     *
     * @param matchers
     *            the matchers
     * @return the event matcher
     */
    public static EventMatcher or(final EventMatcher... matchers) {
        return new EventMatcher() {

            @Override
            public boolean matches(Event matchingEvent) {
                for (EventMatcher matcher : matchers) {
                    if (matcher.matches(matchingEvent)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String toString() {
                StringBuilder buf = new StringBuilder();
                boolean first = true;
                for (EventMatcher matcher : matchers) {
                    if (first) {
                        first = false;
                    } else {
                        buf.append("||");
                    }
                    buf.append("(").append(matcher).append(")");
                }

                return buf.toString();
            }

        };
    }

    /**
     * Varbind.
     *
     * @param vbnumber
     *            the vbnumber
     * @return the field
     */
    public static Field varbind(final int vbnumber) {
        if (vbnumber <= 0) {
            throw new IllegalArgumentException("Invalid varbind index " + vbnumber + " must be 1 or greater.");
        }
        return new Field() {
            public String get(Event event) {
                List<Parm> parms = event.getParmCollection();
                return vbnumber > parms.size() ? null : EventUtil.getValueAsString(parms.get(vbnumber - 1).getValue());
            }

            @Override
            public String toString() {
                return "event.varbind#" + vbnumber;
            }
        };
    }

    /**
     * The Class EventField.
     */
    private abstract static class EventField implements Field {

        /** The m_name. */
        private String m_name;

        /**
         * Instantiates a new event field.
         *
         * @param name
         *            the name
         */
        public EventField(String name) {
            m_name = name;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "event." + m_name;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.xml.eventconf.Field#get(org.opennms.netmgt.xml.event.Event)
         */
        public abstract String get(org.opennms.netmgt.xml.event.Event matchingEvent);
    }

    /**
     * Field.
     *
     * @param name
     *            the name
     * @return the field
     */
    public static Field field(String name) {
        if (name.equals(TAG_UEI)) {
            return new EventField(name) {
                public String get(Event event) {
                    return event.getUei();
                }
            };
        } else if (name.equals(TAG_SOURCE)) {
            return new EventField(name) {
                public String get(Event event) {
                    return event.getSource();
                }
            };
        } else if (name.equals(TAG_NODEID)) {
            return new EventField(name) {
                public String get(Event event) {
                    return Long.toString(event.getNodeid());
                }
            };
        } else if (name.equals(TAG_HOST)) {
            return new EventField(name) {
                public String get(Event event) {
                    return event.getHost();
                }
            };
        } else if (name.equals(TAG_INTERFACE)) {
            return new EventField(name) {
                public String get(Event event) {
                    return event.getInterface();
                }
            };
        } else if (name.equals(TAG_SNMPHOST)) {
            return new EventField(name) {
                public String get(Event event) {
                    return event.getSnmphost();
                }
            };
        } else if (name.equals(TAG_SERVICE)) {
            return new EventField(name) {
                public String get(Event event) {
                    return event.getService();
                }
            };
        } else if (name.equals(TAG_SNMP_EID)) {
            return new EventField(name) {
                public String get(Event event) {
                    return event.getSnmp() == null ? null : event.getSnmp().getId();
                }
            };
        } else if (name.equals(TAG_SNMP_COMMUNITY)) {
            return new EventField(name) {
                public String get(Event event) {
                    return event.getSnmp() == null ? null : event.getSnmp().getCommunity();
                }
            };
        } else if (name.equals(TAG_SNMP_SPECIFIC)) {
            return new EventField(name) {
                public String get(Event event) {
                    return event.getSnmp() == null || !event.getSnmp().hasSpecific() ? null
                        : Integer.toString(event.getSnmp().getSpecific());
                }
            };
        } else if (name.equals(TAG_SNMP_GENERIC)) {
            return new EventField(name) {
                public String get(Event event) {
                    return event.getSnmp() == null || !event.getSnmp().hasGeneric() ? null
                        : Integer.toString(event.getSnmp().getGeneric());
                }
            };
        } else {
            throw new IllegalStateException("Field " + name + " is not understood!");
        }
    }

    /**
     * Value starts with matcher.
     *
     * @param field
     *            the field
     * @param value
     *            the value
     * @return the event matcher
     */
    public static EventMatcher valueStartsWithMatcher(final Field field, final String value) {
        final String prefix = value.substring(0, value.length() - 1);

        return new EventMatcher() {

            @Override
            public boolean matches(Event matchingEvent) {
                String eventValue = field.get(matchingEvent);
                // we have to do equals check for compatibility with the old
                // code
                return eventValue != null && (eventValue.startsWith(prefix) || eventValue.equals(value));
            }

            @Override
            public String toString() {
                return field + ".startsWith(" + prefix + ")";
            }
        };
    }

    /**
     * Value matches regex matcher.
     *
     * @param field
     *            the field
     * @param value
     *            the value
     * @return the event matcher
     */
    public static EventMatcher valueMatchesRegexMatcher(final Field field, final String value) {
        final Pattern regex = Pattern.compile(value.startsWith("~") ? value.substring(1) : value);

        return new EventMatcher() {

            @Override
            public boolean matches(Event matchingEvent) {
                String eventValue = field.get(matchingEvent);
                // we have to do equals check for compatibility with the old
                // code
                return eventValue != null && (regex.matcher(eventValue).matches() || eventValue.equals(value));
            }

            @Override
            public String toString() {
                return field + "~" + regex;
            }
        };
    }

    /**
     * Value equals matcher.
     *
     * @param field
     *            the field
     * @param value
     *            the value
     * @return the event matcher
     */
    public static EventMatcher valueEqualsMatcher(final Field field, final String value) {
        return new EventMatcher() {

            @Override
            public boolean matches(Event matchingEvent) {
                String eventValue = field.get(matchingEvent);
                return eventValue != null && eventValue.equals(value);
            }

            @Override
            public String toString() {
                return field + "==" + value;
            }
        };
    }

}
