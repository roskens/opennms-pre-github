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

package org.opennms.netmgt.snmp.mock;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.opennms.netmgt.snmp.AbstractSnmpValue;
import org.opennms.netmgt.snmp.InetAddrUtils;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpValue;

/**
 * The Class MockSnmpValue.
 */
public class MockSnmpValue extends AbstractSnmpValue {

    /**
     * The Class MockSnmpNullValue.
     */
    private static final class MockSnmpNullValue extends MockSnmpValue {

        /**
         * Instantiates a new mock snmp null value.
         *
         * @param type
         *            the type
         * @param value
         *            the value
         */
        private MockSnmpNullValue(int type, String value) {
            super(type, value);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#isNull()
         */
        @Override
        public boolean isNull() {
            return true;
        }
    }

    /**
     * The Class NetworkAddressSnmpValue.
     */
    public static class NetworkAddressSnmpValue extends MockSnmpValue {

        /**
         * Instantiates a new network address snmp value.
         *
         * @param value
         *            the value
         */
        public NetworkAddressSnmpValue(final String value) {
            super(SnmpValue.SNMP_OCTET_STRING, value);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#isDisplayable()
         */
        @Override
        public boolean isDisplayable() {
            return false;
        }

    }

    /**
     * The Class OctetStringSnmpValue.
     */
    public static class OctetStringSnmpValue extends MockSnmpValue {

        /** The m_bytes. */
        private byte[] m_bytes;

        /**
         * Instantiates a new octet string snmp value.
         *
         * @param bytes
         *            the bytes
         */
        public OctetStringSnmpValue(final byte[] bytes) {
            super(SnmpValue.SNMP_OCTET_STRING, new String(bytes));
            m_bytes = bytes;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#getBytes()
         */
        @Override
        public byte[] getBytes() {
            return m_bytes;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#toString()
         */
        @Override
        public String toString() {
            final byte[] data = getBytes();

            final byte[] results = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                results[i] = Character.isISOControl((char) data[i]) ? (byte) '.' : data[i];
            }
            return new String(results);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#toHexString()
         */
        @Override
        public String toHexString() {
            final byte[] data = getBytes();
            final StringBuffer b = new StringBuffer();
            for (int i = 0; i < data.length; ++i) {
                final int x = (int) data[i] & 0xff;
                if (x < 16)
                    b.append("0");
                b.append(Integer.toString(x, 16).toLowerCase());
            }
            return b.toString();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#isDisplayable()
         */
        @Override
        public boolean isDisplayable() {
            return allBytesDisplayable(getBytes());
        }

    }

    /**
     * The Class IpAddressSnmpValue.
     */
    public static class IpAddressSnmpValue extends MockSnmpValue {

        /**
         * Instantiates a new ip address snmp value.
         *
         * @param addr
         *            the addr
         */
        public IpAddressSnmpValue(InetAddress addr) {
            this(InetAddrUtils.str(addr));
        }

        /**
         * Instantiates a new ip address snmp value.
         *
         * @param bytes
         *            the bytes
         */
        public IpAddressSnmpValue(byte[] bytes) {
            this(addrStr(bytes));
        }

        /**
         * Addr str.
         *
         * @param bytes
         *            the bytes
         * @return the string
         */
        public static String addrStr(byte[] bytes) {
            try {
                return InetAddrUtils.str(InetAddress.getByAddress(bytes));
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Instantiates a new ip address snmp value.
         *
         * @param value
         *            the value
         */
        public IpAddressSnmpValue(String value) {
            super(SnmpValue.SNMP_IPADDRESS, value);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#toInetAddress()
         */
        @Override
        public InetAddress toInetAddress() {
            try {
                return InetAddrUtils.addr(toString());
            } catch (Exception e) {
                return super.toInetAddress();
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#getBytes()
         */
        @Override
        public byte[] getBytes() {
            return toInetAddress().getAddress();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#isDisplayable()
         */
        @Override
        public boolean isDisplayable() {
            return true;
        }

    }

    /**
     * The Class NumberSnmpValue.
     */
    public static class NumberSnmpValue extends MockSnmpValue {

        /**
         * Instantiates a new number snmp value.
         *
         * @param type
         *            the type
         * @param value
         *            the value
         */
        public NumberSnmpValue(int type, String value) {
            super(type, value);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#isNumeric()
         */
        @Override
        public boolean isNumeric() {
            return true;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#toInt()
         */
        @Override
        public int toInt() {
            return Integer.parseInt(getNumberString());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#toLong()
         */
        @Override
        public long toLong() {
            return Long.parseLong(getNumberString());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#toBigInteger()
         */
        @Override
        public BigInteger toBigInteger() {
            return new BigInteger(getNumberString());
        }

        /**
         * Gets the number string.
         *
         * @return the number string
         */
        public String getNumberString() {
            return toString();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#getBytes()
         */
        @Override
        public byte[] getBytes() {
            return toBigInteger().toByteArray();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#isDisplayable()
         */
        @Override
        public boolean isDisplayable() {
            return true;
        }
    }

    /**
     * The Class Integer32SnmpValue.
     */
    public static class Integer32SnmpValue extends NumberSnmpValue {

        /**
         * Instantiates a new integer32 snmp value.
         *
         * @param value
         *            the value
         */
        public Integer32SnmpValue(int value) {
            this(Integer.toString(value));
        }

        /**
         * Instantiates a new integer32 snmp value.
         *
         * @param value
         *            the value
         */
        public Integer32SnmpValue(String value) {
            super(SnmpValue.SNMP_INT32, value);
        }
    }

    /**
     * The Class Gauge32SnmpValue.
     */
    public static class Gauge32SnmpValue extends NumberSnmpValue {

        /**
         * Instantiates a new gauge32 snmp value.
         *
         * @param value
         *            the value
         */
        public Gauge32SnmpValue(long value) {
            this(Long.toString(value));
        }

        /**
         * Instantiates a new gauge32 snmp value.
         *
         * @param value
         *            the value
         */
        public Gauge32SnmpValue(String value) {
            super(SnmpValue.SNMP_GAUGE32, value);
        }
    }

    /**
     * The Class Counter32SnmpValue.
     */
    public static class Counter32SnmpValue extends NumberSnmpValue {

        /**
         * Instantiates a new counter32 snmp value.
         *
         * @param value
         *            the value
         */
        public Counter32SnmpValue(long value) {
            this(Long.toString(value));
        }

        /**
         * Instantiates a new counter32 snmp value.
         *
         * @param value
         *            the value
         */
        public Counter32SnmpValue(String value) {
            super(SnmpValue.SNMP_COUNTER32, value);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue.NumberSnmpValue#toBigInteger()
         */
        @Override
        public BigInteger toBigInteger() {
            return BigInteger.valueOf(toLong());
        }
    }

    /**
     * The Class Counter64SnmpValue.
     */
    public static class Counter64SnmpValue extends NumberSnmpValue {

        /**
         * Instantiates a new counter64 snmp value.
         *
         * @param value
         *            the value
         */
        public Counter64SnmpValue(BigInteger value) {
            this(value.toString());
        }

        /**
         * Instantiates a new counter64 snmp value.
         *
         * @param value
         *            the value
         */
        public Counter64SnmpValue(String value) {
            super(SnmpValue.SNMP_COUNTER64, value);
        }
    }

    /**
     * The Enum UnitOfTime.
     */
    static enum UnitOfTime {

        /** The days. */
        DAYS(4),
 /** The hours. */
 HOURS(3),
 /** The minutes. */
 MINUTES(2),
 /** The seconds. */
 SECONDS(1),
 /** The millis. */
 MILLIS(0);

        /** The m_index. */
        private int m_index;

        /**
         * Instantiates a new unit of time.
         *
         * @param index
         *            the index
         */
        private UnitOfTime(int index) {
            m_index = index;
        }

        /** The Constant s_millisPerUnit. */
        private static final long[] s_millisPerUnit = { 1L, // millis
                1000L, // seconds
                1000L * 60L, // minutes
                1000L * 60L * 60L, // hours
                1000L * 60L * 60L * 24L // days
        };

        /** The Constant s_unitName. */
        private static final String[] s_unitName = { "ms", // millis
                "s", // seconds
                "m", // minutes
                "h", // hours
                "d" // days
        };

        /**
         * Whole units.
         *
         * @param millis
         *            the millis
         * @return the long
         */
        public long wholeUnits(long millis) {
            return millis / s_millisPerUnit[m_index];
        }

        /**
         * Remaining millis.
         *
         * @param millis
         *            the millis
         * @return the long
         */
        public long remainingMillis(long millis) {
            return millis % s_millisPerUnit[m_index];
        }

        /**
         * Unit.
         *
         * @return the string
         */
        public String unit() {
            return s_unitName[m_index];
        }

    }

    /**
     * The Class TimeticksSnmpValue.
     */
    public static class TimeticksSnmpValue extends NumberSnmpValue {

        // Format of string is '(numTicks) HH:mm:ss.hh'
        /**
         * Instantiates a new timeticks snmp value.
         *
         * @param value
         *            the value
         */
        public TimeticksSnmpValue(String value) {
            super(SnmpValue.SNMP_TIMETICKS, value);
        }

        /**
         * Instantiates a new timeticks snmp value.
         *
         * @param centiSeconds
         *            the centi seconds
         */
        public TimeticksSnmpValue(long centiSeconds) {
            this("(" + centiSeconds + ")");
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue.NumberSnmpValue#getNumberString()
         */
        @Override
        public String getNumberString() {
            String str = getValue();
            int end = str.indexOf(')');
            return (end < 0 ? str : str.substring(1, end));
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#toString()
         */
        @Override
        public String toString() {
            return String.valueOf(toLong());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#toDisplayString()
         */
        @Override
        public String toDisplayString() {
            return toString();
            /*
             * long millis = toLong()*10L;
             * StringBuilder buf = new StringBuilder();
             * boolean first = true;
             * for(UnitOfTime unit : UnitOfTime.values()) {
             * if (first) {
             * first = false;
             * } else {
             * buf.append(' ');
             * }
             * buf.append(unit.wholeUnits(millis)).append(unit.unit());
             * millis = unit.remainingMillis(millis);
             * }
             * return buf.toString();
             */
        }

    }

    /**
     * The Class StringSnmpValue.
     */
    public static class StringSnmpValue extends MockSnmpValue {

        /**
         * Instantiates a new string snmp value.
         *
         * @param value
         *            the value
         */
        public StringSnmpValue(String value) {
            super(SnmpValue.SNMP_OCTET_STRING, value);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#toInt()
         */
        @Override
        public int toInt() {
            try {
                return Integer.parseInt(toString());
            } catch (NumberFormatException e) {
                return super.toInt();
            }

        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#isDisplayable()
         */
        @Override
        public boolean isDisplayable() {
            return true;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#toLong()
         */
        @Override
        public long toLong() {
            return Long.parseLong(toString());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#toHexString()
         */
        @Override
        public String toHexString() {
            StringBuffer buff = new StringBuffer();

            for (byte b : toString().getBytes()) {
                buff.append(Integer.toHexString(b));
            }

            return buff.toString();
        }
    }

    /**
     * The Class OidSnmpValue.
     */
    public static class OidSnmpValue extends MockSnmpValue {

        /**
         * Instantiates a new oid snmp value.
         *
         * @param objId
         *            the obj id
         */
        public OidSnmpValue(SnmpObjId objId) {
            this(objId.toString());
        }

        /**
         * Instantiates a new oid snmp value.
         *
         * @param value
         *            the value
         */
        public OidSnmpValue(String value) {
            super(SnmpValue.SNMP_OBJECT_IDENTIFIER, value);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#toSnmpObjId()
         */
        @Override
        public SnmpObjId toSnmpObjId() {
            return SnmpObjId.get(toString());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.mock.MockSnmpValue#isDisplayable()
         */
        @Override
        public boolean isDisplayable() {
            return true;
        }

    }

    /** The m_type. */
    private int m_type;

    /** The m_value. */
    private String m_value;

    /** The Constant NULL_VALUE. */
    public static final MockSnmpValue NULL_VALUE = new MockSnmpNullValue(SnmpValue.SNMP_NULL, null);

    /** The Constant NO_SUCH_INSTANCE. */
    public static final MockSnmpValue NO_SUCH_INSTANCE = new MockSnmpNullValue(SnmpValue.SNMP_NO_SUCH_INSTANCE,
                                                                               "noSuchInstance");

    /** The Constant NO_SUCH_OBJECT. */
    public static final MockSnmpValue NO_SUCH_OBJECT = new MockSnmpNullValue(SnmpValue.SNMP_NO_SUCH_OBJECT,
                                                                             "noSuchObject");

    /** The Constant END_OF_MIB. */
    public static final MockSnmpValue END_OF_MIB = new MockSnmpNullValue(SnmpValue.SNMP_END_OF_MIB, "endOfMibView");

    /**
     * Instantiates a new mock snmp value.
     *
     * @param type
     *            the type
     * @param value
     *            the value
     */
    public MockSnmpValue(int type, String value) {
        m_type = type;
        m_value = value;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#isEndOfMib()
     */
    @Override
    public boolean isEndOfMib() {
        return getType() == SnmpValue.SNMP_END_OF_MIB;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#getType()
     */
    @Override
    public int getType() {
        return m_type;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#toDisplayString()
     */
    @Override
    public String toDisplayString() {
        return toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return m_value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return m_value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MockSnmpValue) {
            MockSnmpValue val = (MockSnmpValue) obj;
            return (m_value == null ? val.m_value == null : m_value.equals(val.m_value));
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (m_value == null)
            return 0;
        return m_value.hashCode();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#isNumeric()
     */
    @Override
    public boolean isNumeric() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#isError()
     */
    @Override
    public boolean isError() {
        switch (getType()) {
        case SnmpValue.SNMP_NO_SUCH_INSTANCE:
        case SnmpValue.SNMP_NO_SUCH_OBJECT:
            return true;
        default:
            return false;
        }

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#toInt()
     */
    @Override
    public int toInt() {
        throw new IllegalArgumentException("Unable to convert " + this + " to an int");
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#toInetAddress()
     */
    @Override
    public InetAddress toInetAddress() {
        throw new IllegalArgumentException("Unable to convert " + this + " to an InetAddress");
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#toLong()
     */
    @Override
    public long toLong() {
        throw new IllegalArgumentException("Unable to convert " + this + " to a long");
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#toHexString()
     */
    @Override
    public String toHexString() {
        throw new IllegalArgumentException("Unable to convert " + this + " to a hex string");
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#toBigInteger()
     */
    @Override
    public BigInteger toBigInteger() {
        throw new IllegalArgumentException("Unable to convert " + this + " to a hex string");
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#toSnmpObjId()
     */
    @Override
    public SnmpObjId toSnmpObjId() {
        throw new IllegalArgumentException("Unable to convert " + this + " to an SNMP object ID");
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#getBytes()
     */
    @Override
    public byte[] getBytes() {
        return toString().getBytes();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#isDisplayable()
     */
    @Override
    public boolean isDisplayable() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValue#isNull()
     */
    @Override
    public boolean isNull() {
        return false;
    }

}
