/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.notifd;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.db.DataSourceFactory;
import org.opennms.core.test.ConfigurationTestUtils;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.test.db.MockDatabase;
import org.opennms.core.utils.TimeConverter;
import org.opennms.netmgt.config.NotificationCommandManager;
import org.opennms.netmgt.config.NotificationManager;
import org.opennms.netmgt.config.PollOutagesConfigManager;
import org.opennms.netmgt.config.groups.Group;
import org.opennms.netmgt.config.mock.MockDestinationPathManager;
import org.opennms.netmgt.config.mock.MockGroupManager;
import org.opennms.netmgt.config.mock.MockNotifdConfigManager;
import org.opennms.netmgt.config.mock.MockNotificationCommandManager;
import org.opennms.netmgt.config.mock.MockNotificationManager;
import org.opennms.netmgt.config.mock.MockNotificationStrategy;
import org.opennms.netmgt.config.mock.MockUserManager;
import org.opennms.netmgt.config.users.Contact;
import org.opennms.netmgt.config.users.User;
import org.opennms.netmgt.dao.mock.MockEventIpcManager;
import org.opennms.netmgt.mock.MockNetwork;
import org.opennms.netmgt.mock.MockNotification;
import org.opennms.netmgt.mock.MockPollerConfig;
import org.opennms.netmgt.mock.NotificationAnticipator;
import org.opennms.test.DaoTestConfigBean;
import org.opennms.test.mock.MockUtil;

/**
 * The Class NotificationsTestCase.
 */
public class NotificationsTestCase {

    /** The m_notifd. */
    protected Notifd m_notifd;

    /** The m_event mgr. */
    protected MockEventIpcManager m_eventMgr;

    /** The m_notifd config. */
    protected MockNotifdConfigManager m_notifdConfig;

    /** The m_group manager. */
    protected MockGroupManager m_groupManager;

    /** The m_user manager. */
    protected MockUserManager m_userManager;

    /** The m_notification manager. */
    protected NotificationManager m_notificationManager;

    /** The m_notification command manger. */
    protected NotificationCommandManager m_notificationCommandManger;

    /** The m_destination path manager. */
    protected MockDestinationPathManager m_destinationPathManager;

    /** The m_db. */
    protected MockDatabase m_db;

    /** The m_network. */
    protected MockNetwork m_network;

    /** The m_anticipator. */
    protected NotificationAnticipator m_anticipator;

    /** The m_poll outages config manager. */
    private PollOutagesConfigManager m_pollOutagesConfigManager;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    protected void setUp() throws Exception {
        MockUtil.println("################# Running Test ################");

        DaoTestConfigBean bean = new DaoTestConfigBean();
        bean.afterPropertiesSet();

        MockLogAppender.setupLogging();

        m_network = createMockNetwork();

        m_db = createDatabase(m_network);

        m_eventMgr = new MockEventIpcManager();
        m_eventMgr.setEventWriter(m_db);

        m_notifdConfig = new MockNotifdConfigManager(
                                                     ConfigurationTestUtils.getConfigForResourceWithReplacements(this,
                                                                                                                 "notifd-configuration.xml"));
        m_notifdConfig.setNextNotifIdSql(m_db.getNextNotifIdSql());
        m_notifdConfig.setNextUserNotifIdSql(m_db.getNextUserNotifIdSql());

        m_groupManager = createGroupManager();
        m_userManager = createUserManager(m_groupManager);

        m_destinationPathManager = new MockDestinationPathManager(
                                                                  ConfigurationTestUtils.getConfigForResourceWithReplacements(this,
                                                                                                                              "destination-paths.xml"));
        m_notificationCommandManger = new MockNotificationCommandManager(
                                                                         ConfigurationTestUtils.getConfigForResourceWithReplacements(this,
                                                                                                                                     "notification-commands.xml"));
        m_notificationManager = new MockNotificationManager(
                                                            m_notifdConfig,
                                                            m_db,
                                                            ConfigurationTestUtils.getConfigForResourceWithReplacements(this,
                                                                                                                        "notifications.xml"));
        m_pollOutagesConfigManager = new MockPollerConfig(m_network);

        m_anticipator = new NotificationAnticipator();
        MockNotificationStrategy.setAnticipator(m_anticipator);

        m_notifd = new Notifd();
        m_notifd.setEventManager(m_eventMgr);
        m_notifd.setConfigManager(m_notifdConfig);
        m_notifd.setGroupManager(m_groupManager);
        m_notifd.setUserManager(m_userManager);
        m_notifd.setDestinationPathManager(m_destinationPathManager);
        m_notifd.setNotificationCommandManager(m_notificationCommandManger);
        m_notifd.setNotificationManager(m_notificationManager);
        m_notifd.setPollOutagesConfigManager(m_pollOutagesConfigManager);

        m_notifd.init();
        m_notifd.start();

        // Date downDate = new Date();
        // anticipateNotificationsForGroup("node 2 down.",
        // "All services are down on node 2.", "InitialGroup", downDate, 0);
        //
        // //bring node down now
        // m_eventMgr.sendEventToListeners(m_network.getNode(2).createDownEvent(downDate));
        //
        // m_anticipator.waitForAnticipated(2000);
        //
        // m_anticipator.reset();

        MockUtil.println("################ Finish Setup ################");

    }

    /**
     * Creates the database.
     *
     * @param network
     *            the network
     * @return the mock database
     * @throws Exception
     *             the exception
     */
    protected MockDatabase createDatabase(MockNetwork network) throws Exception {
        MockDatabase db = new MockDatabase();
        DataSourceFactory.setInstance(db);
        db.populate(network);
        return db;
    }

    /**
     * Creates the mock network.
     *
     * @return the mock network
     */
    protected MockNetwork createMockNetwork() {
        MockNetwork network = new MockNetwork();
        network.createStandardNetwork();
        return network;
    }

    /**
     * Creates the user manager.
     *
     * @param groupManager
     *            the group manager
     * @return the mock user manager
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private MockUserManager createUserManager(MockGroupManager groupManager) throws MarshalException,
            ValidationException, IOException {
        return new MockUserManager(groupManager,
                                   ConfigurationTestUtils.getConfigForResourceWithReplacements(this, "users.xml"));
    }

    /**
     * Creates the group manager.
     *
     * @return the mock group manager
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private MockGroupManager createGroupManager() throws MarshalException, ValidationException, IOException {
        return new MockGroupManager(ConfigurationTestUtils.getConfigForResourceWithReplacements(this, "groups.xml"));
    }

    /**
     * Tear down.
     *
     * @throws Exception
     *             the exception
     */
    protected void tearDown() throws Exception {
        this.tearDown(false);
    }

    /**
     * Tear down.
     *
     * @param allowAllLogMessages
     *            the allow all log messages
     * @throws Exception
     *             the exception
     */
    protected void tearDown(boolean allowAllLogMessages) throws Exception {
        m_eventMgr.finishProcessingEvents();
        m_notifd.stop();

        // m_db.drop();
        MockNotificationStrategy.setAnticipator(null);
        if (!allowAllLogMessages) {
            MockLogAppender.assertNoWarningsOrGreater();
        }
    }

    /**
     * Test do nothing.
     */
    public void testDoNothing() {
        // this is only here to ensure that we don't get an error when running
        // AllTests
    }

    /**
     * Anticipate notifications for group.
     *
     * @param subject
     *            the subject
     * @param textMsg
     *            the text msg
     * @param groupName
     *            the group name
     * @param startTime
     *            the start time
     * @param interval
     *            the interval
     * @return the long
     * @throws Exception
     *             the exception
     */
    protected long anticipateNotificationsForGroup(String subject, String textMsg, String groupName, Date startTime,
            long interval) throws Exception {
        return anticipateNotificationsForGroup(subject, textMsg, groupName, startTime.getTime(), interval);
    }

    /**
     * Anticipate notifications for group.
     *
     * @param subject
     *            the subject
     * @param textMsg
     *            the text msg
     * @param groupName
     *            the group name
     * @param startTime
     *            the start time
     * @param interval
     *            the interval
     * @return the long
     * @throws Exception
     *             the exception
     */
    protected long anticipateNotificationsForGroup(String subject, String textMsg, String groupName, long startTime,
            long interval) throws Exception {
        Group group = m_groupManager.getGroup(groupName);
        String[] users = group.getUser();
        return anticipateNotificationsForUsers(users, subject, textMsg, startTime, interval);
    }

    /**
     * Anticipate notifications for role.
     *
     * @param subject
     *            the subject
     * @param textMsg
     *            the text msg
     * @param groupName
     *            the group name
     * @param startTime
     *            the start time
     * @param interval
     *            the interval
     * @return the long
     * @throws Exception
     *             the exception
     */
    protected long anticipateNotificationsForRole(String subject, String textMsg, String groupName, Date startTime,
            long interval) throws Exception {
        return anticipateNotificationsForRole(subject, textMsg, groupName, startTime.getTime(), interval);
    }

    /**
     * Anticipate notifications for role.
     *
     * @param subject
     *            the subject
     * @param textMsg
     *            the text msg
     * @param roleName
     *            the role name
     * @param startTime
     *            the start time
     * @param interval
     *            the interval
     * @return the long
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected long anticipateNotificationsForRole(String subject, String textMsg, String roleName, long startTime,
            long interval) throws MarshalException, ValidationException, IOException {
        String[] users = m_userManager.getUsersScheduledForRole(roleName, new Date(startTime));
        return anticipateNotificationsForUsers(users, subject, textMsg, startTime, interval);
    }

    /**
     * Anticipate notifications for users.
     *
     * @param users
     *            the users
     * @param subject
     *            the subject
     * @param textMsg
     *            the text msg
     * @param startTime
     *            the start time
     * @param interval
     *            the interval
     * @return the long
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    protected long anticipateNotificationsForUsers(String[] users, String subject, String textMsg, long startTime,
            long interval) throws IOException, MarshalException, ValidationException {
        long expectedTime = startTime;
        for (int i = 0; i < users.length; i++) {
            User user = m_userManager.getUser(users[i]);
            Contact[] contacts = user.getContact();
            for (int j = 0; j < contacts.length; j++) {
                Contact contact = contacts[j];
                if ("email".equals(contact.getType())) {
                    m_anticipator.anticipateNotification(createMockNotification(expectedTime, subject, textMsg,
                                                                                contact.getInfo()));
                }
            }
            expectedTime += interval;
        }
        return expectedTime - interval;
    }

    /**
     * Gets the users in group.
     *
     * @param groupName
     *            the group name
     * @return the users in group
     * @throws Exception
     *             the exception
     */
    protected Collection<String> getUsersInGroup(String groupName) throws Exception {
        Group group = m_groupManager.getGroup(groupName);
        String[] users = group.getUser();
        return Arrays.asList(users);

    }

    /**
     * Verify anticipated.
     *
     * @param lastNotifyTime
     *            the last notify time
     * @param waitTime
     *            the wait time
     */
    protected void verifyAnticipated(long lastNotifyTime, long waitTime) {
        verifyAnticipated(lastNotifyTime, waitTime, 1000);
    }

    /**
     * Verify anticipated.
     *
     * @param lastNotifyTime
     *            the last notify time
     * @param waitTime
     *            the wait time
     * @param sleepTime
     *            the sleep time
     */
    protected void verifyAnticipated(long lastNotifyTime, long waitTime, long sleepTime) {
        m_anticipator.verifyAnticipated(lastNotifyTime, waitTime, sleepTime);
    }

    /**
     * Sleep.
     *
     * @param millis
     *            the millis
     */
    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Creates the mock notification.
     *
     * @param expectedTime
     *            the expected time
     * @param subject
     *            the subject
     * @param textMsg
     *            the text msg
     * @param email
     *            the email
     * @return the mock notification
     */
    protected MockNotification createMockNotification(long expectedTime, String subject, String textMsg, String email) {
        MockNotification notification;
        notification = new MockNotification();
        notification.setExpectedTime(expectedTime);
        notification.setSubject(subject);
        notification.setTextMsg(textMsg);
        notification.setEmail(email);
        return notification;
    }

    /**
     * Compute interval.
     *
     * @return the long
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    protected long computeInterval() throws IOException, MarshalException, ValidationException {
        String interval = m_destinationPathManager.getPath("Intervals").getTarget(0).getInterval();
        return TimeConverter.convertToMillis(interval);
    }

}
