// Original version by Tim Endres, <time@ice.com>.
//
// Re-written and fixed up by Jef Poskanzer <jef@mail.acme.com>.
//
// Copyright (C)1996 by Jef Poskanzer <jef@mail.acme.com>. All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//
// Visit the ACME Labs Java page for up-to-date versions of this and other
// fine Java utilities: http://www.acme.com/java/

package org.opennms.netmgt.syslogd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.opennms.core.utils.InetAddressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SyslogClient.
 */
public class SyslogClient {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(SyslogClient.class);

    // Priorities.
    /** The Constant LOG_EMERG. */
    public static final int LOG_EMERG = 0; // system is unusable

    /** The Constant LOG_ALERT. */
    public static final int LOG_ALERT = 1; // action must be taken immediately

    /** The Constant LOG_CRIT. */
    public static final int LOG_CRIT = 2; // critical conditions

    /** The Constant LOG_ERR. */
    public static final int LOG_ERR = 3; // error conditions

    /** The Constant LOG_WARNING. */
    public static final int LOG_WARNING = 4; // warning conditions

    /** The Constant LOG_NOTICE. */
    public static final int LOG_NOTICE = 5; // normal but significant condition

    /** The Constant LOG_INFO. */
    public static final int LOG_INFO = 6; // informational

    /** The Constant LOG_DEBUG. */
    public static final int LOG_DEBUG = 7; // debug-level messages

    /** The Constant LOG_PRIMASK. */
    public static final int LOG_PRIMASK = 0x0007; // mask to extract priority

    // Facilities.
    /** The Constant LOG_KERN. */
    public static final int LOG_KERN = (0 << 3); // kernel messages

    /** The Constant LOG_USER. */
    public static final int LOG_USER = (1 << 3); // random user-level messages

    /** The Constant LOG_MAIL. */
    public static final int LOG_MAIL = (2 << 3); // mail system

    /** The Constant LOG_DAEMON. */
    public static final int LOG_DAEMON = (3 << 3); // system daemons

    /** The Constant LOG_AUTH. */
    public static final int LOG_AUTH = (4 << 3); // security/authorization

    /** The Constant LOG_SYSLOG. */
    public static final int LOG_SYSLOG = (5 << 3); // internal syslogd use

    /** The Constant LOG_LPR. */
    public static final int LOG_LPR = (6 << 3); // line printer subsystem

    /** The Constant LOG_NEWS. */
    public static final int LOG_NEWS = (7 << 3); // network news subsystem

    /** The Constant LOG_UUCP. */
    public static final int LOG_UUCP = (8 << 3); // UUCP subsystem

    /** The Constant LOG_CRON. */
    public static final int LOG_CRON = (15 << 3); // clock daemon

    // Other codes through 15 reserved for system use.
    /** The Constant LOG_LOCAL0. */
    public static final int LOG_LOCAL0 = (16 << 3); // reserved for local use

    /** The Constant LOG_LOCAL1. */
    public static final int LOG_LOCAL1 = (17 << 3); // reserved for local use

    /** The Constant LOG_LOCAL2. */
    public static final int LOG_LOCAL2 = (18 << 3); // reserved for local use

    /** The Constant LOG_LOCAL3. */
    public static final int LOG_LOCAL3 = (19 << 3); // reserved for local use

    /** The Constant LOG_LOCAL4. */
    public static final int LOG_LOCAL4 = (20 << 3); // reserved for local use

    /** The Constant LOG_LOCAL5. */
    public static final int LOG_LOCAL5 = (21 << 3); // reserved for local use

    /** The Constant LOG_LOCAL6. */
    public static final int LOG_LOCAL6 = (22 << 3); // reserved for local use

    /** The Constant LOG_LOCAL7. */
    public static final int LOG_LOCAL7 = (23 << 3); // reserved for local use

    /** The Constant LOG_FACMASK. */
    public static final int LOG_FACMASK = 0x03F8; // mask to extract facility

    // Option flags.
    /** The Constant LOG_PID. */
    public static final int LOG_PID = 0x01; // log the pid with each message

    /** The Constant LOG_CONS. */
    public static final int LOG_CONS = 0x02; // log on the console if errors

    /** The Constant LOG_NDELAY. */
    public static final int LOG_NDELAY = 0x08; // don't delay open

    /** The Constant LOG_NOWAIT. */
    public static final int LOG_NOWAIT = 0x10; // don't wait for console forks

    /** The Constant PORT. */
    public static final int PORT = 10514;

    /** The ident. */
    private String ident;

    /** The facility. */
    private int facility;

    /** The address. */
    private InetAddress address;

    /** The socket. */
    private DatagramSocket socket;

    // / Creating a Syslog instance is equivalent of the Unix openlog() call.
    // @exception SyslogException if there was a problem
    /**
     * Instantiates a new syslog client.
     *
     * @param ident
     *            the ident
     * @param logopt
     *            the logopt
     * @param facility
     *            the facility
     * @throws UnknownHostException
     *             the unknown host exception
     */
    public SyslogClient(String ident, int logopt, int facility) throws UnknownHostException {
        if (ident == null) {
            this.ident = Thread.currentThread().getName();
        } else {
            this.ident = ident;
        }
        this.facility = facility;

        address = InetAddressUtils.getLocalHostAddress();

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            LOG.warn("Unable to create datagram socket.", e);
        }
    }

    // / Use this method to log your syslog messages. The facility and
    // level are the same as their Unix counterparts, and the Syslog
    // class provides constants for these fields. The msg is what is
    // actually logged.
    // @exception SyslogException if there was a problem
    /**
     * Syslog.
     *
     * @param priority
     *            the priority
     * @param msg
     *            the msg
     */
    public void syslog(int priority, String msg) {
        final DatagramPacket packet = getPacket(priority, msg);
        try {
            socket.send(packet);
        } catch (IOException e) {
            LOG.warn("Exception sending data.", e);
        }
    }

    /**
     * Make priority code.
     *
     * @param facility
     *            the facility
     * @param priority
     *            the priority
     * @return the int
     */
    private int MakePriorityCode(int facility, int priority) {
        return ((facility & LOG_FACMASK) | priority);
    }

    /**
     * Gets the packet.
     *
     * @param priority
     *            the priority
     * @param msg
     *            the msg
     * @return the packet
     */
    public DatagramPacket getPacket(final int priority, final String msg) {
        int pricode = MakePriorityCode(facility, priority);
        Integer priObj = Integer.valueOf(pricode);

        StringBuffer sb = new StringBuffer();

        sb.append("<").append(Integer.toString(priObj.intValue())).append(">");
        sb.append(ident).append(": ").append(msg).append("\0");

        final byte[] bytes = sb.toString().getBytes();
        return new DatagramPacket(bytes, bytes.length, address, PORT);
    }

}
