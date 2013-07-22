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

package org.opennms.web.admin.schedule;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.WebSecurityUtils;
import org.opennms.core.xml.CastorUtils;
import org.opennms.netmgt.config.poller.BasicSchedule;
import org.opennms.netmgt.config.poller.Outage;
import org.opennms.netmgt.config.poller.Outages;

/**
 * <p>
 * ScheduleEditorServlet class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public class ScheduleEditorServlet extends HttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7117332637559031820L;

    /** The m_ops. */
    private Map<String, ScheduleOp> m_ops = new HashMap<String, ScheduleOp>();

    /** The m_maps. */
    private Map<String, SingleMapping> m_maps = new HashMap<String, SingleMapping>();

    /** The m_default op. */
    private ScheduleOp m_defaultOp;

    /** The m_default mapping. */
    private ScheduleMapping m_defaultMapping;

    /** The m_default view. */
    private String m_defaultView;

    /**
     * The Interface ScheduleManager.
     */
    public interface ScheduleManager {

        /**
         * Gets the file name.
         *
         * @return the file name
         */
        String getFileName();

        /**
         * Sets the file name.
         *
         * @param fileName
         *            the new file name
         */
        void setFileName(String fileName);

        /**
         * Load schedules.
         *
         * @throws ServletException
         *             the servlet exception
         */
        void loadSchedules() throws ServletException;

        /**
         * Save schedules.
         *
         * @throws ServletException
         *             the servlet exception
         */
        void saveSchedules() throws ServletException;

        /**
         * Delete schedule.
         *
         * @param index
         *            the index
         * @throws ServletException
         *             the servlet exception
         */
        void deleteSchedule(int index) throws ServletException;

        /**
         * Adds the schedule.
         *
         * @param schedule
         *            the schedule
         * @throws ServletException
         *             the servlet exception
         */
        void addSchedule(BasicSchedule schedule) throws ServletException;

        /**
         * Sets the schedule.
         *
         * @param index
         *            the index
         * @param schedule
         *            the schedule
         * @throws ServletException
         *             the servlet exception
         */
        void setSchedule(int index, BasicSchedule schedule) throws ServletException;

        /**
         * Creates the schedule.
         *
         * @param name
         *            the name
         * @param type
         *            the type
         * @return the basic schedule
         */
        BasicSchedule createSchedule(String name, String type);

        /**
         * Gets the schedule.
         *
         * @param index
         *            the index
         * @return the schedule
         */
        BasicSchedule getSchedule(int index);

        /**
         * Gets the schedule.
         *
         * @return the schedule
         */
        BasicSchedule[] getSchedule();
    }

    /**
     * The Class OutageManager.
     */
    static class OutageManager implements ScheduleManager {

        /** The m_outages. */
        private Outages m_outages;

        /** The m_file name. */
        private String m_fileName = null;

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleManager#loadSchedules()
         */
        @Override
        public void loadSchedules() throws ServletException {
            if (m_fileName == null) {
                throw new ServletException("Loading from outage factory not implemented yet!");
            } else {
                try {
                    Reader reader = new InputStreamReader(new FileInputStream(m_fileName), "UTF-8");
                    m_outages = CastorUtils.unmarshal(Outages.class, reader);
                    reader.close();
                } catch (MarshalException e) {
                    throw new ServletException("Unable to unmarshal " + m_fileName, e);
                } catch (ValidationException e) {
                    throw new ServletException("Invalid xml in file " + m_fileName, e);
                } catch (FileNotFoundException e) {
                    throw new ServletException("Unable to locate file " + m_fileName, e);
                } catch (IOException e) {
                    throw new ServletException("Error reading file " + m_fileName, e);
                }
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleManager#saveSchedules()
         */
        @Override
        public void saveSchedules() throws ServletException {
            if (m_fileName == null) {
                throw new ServletException("Saving to outage factory not implemented yet!");
            } else {
                try {
                    Writer writer = new OutputStreamWriter(new FileOutputStream(m_fileName), "UTF-8");
                    Marshaller.marshal(m_outages, writer);
                    writer.close();
                } catch (MarshalException e) {
                    throw new ServletException("Unable to unmarshal " + m_fileName, e);
                } catch (ValidationException e) {
                    throw new ServletException("Invalid xml in file " + m_fileName, e);
                } catch (FileNotFoundException e) {
                    throw new ServletException("Unable to locate file " + m_fileName, e);
                } catch (IOException e) {
                    throw new ServletException("Error reading file " + m_fileName, e);
                }
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleManager#deleteSchedule(int)
         */
        @Override
        public void deleteSchedule(int index) throws ServletException {
            List<Outage> outages = getOutages();
            outages.remove(index);
        }

        /**
         * Gets the outages.
         *
         * @return the outages
         */
        private List<Outage> getOutages() {
            return m_outages.getOutageCollection();
        }

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleManager#addSchedule(org.opennms.netmgt.config.poller.BasicSchedule)
         */
        @Override
        public void addSchedule(BasicSchedule schedule) throws ServletException {
            Outage outage = (Outage) schedule;
            m_outages.addOutage(outage);
        }

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleManager#setSchedule(int, org.opennms.netmgt.config.poller.BasicSchedule)
         */
        @Override
        public void setSchedule(int index, BasicSchedule schedule) throws ServletException {
            m_outages.setOutage(index, (Outage) schedule);
        }

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleManager#createSchedule(java.lang.String, java.lang.String)
         */
        @Override
        public BasicSchedule createSchedule(String name, String type) {
            Outage outage = new Outage();
            outage.setName(name);
            outage.setType(type);
            return outage;
        }

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleManager#getSchedule(int)
         */
        @Override
        public BasicSchedule getSchedule(int index) {
            return m_outages.getOutage(index);
        }

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleManager#getSchedule()
         */
        @Override
        public BasicSchedule[] getSchedule() {
            return m_outages.getOutage();
        }

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleManager#getFileName()
         */
        @Override
        public String getFileName() {
            return m_fileName;
        }

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleManager#setFileName(java.lang.String)
         */
        @Override
        public void setFileName(String fileName) {
            m_fileName = fileName;
        }

    }

    /**
     * The Interface ScheduleOp.
     */
    interface ScheduleOp {

        /**
         * Do op.
         *
         * @param request
         *            the request
         * @param response
         *            the response
         * @param map
         *            the map
         * @return the string
         * @throws ServletException
         *             the servlet exception
         */
        String doOp(HttpServletRequest request, HttpServletResponse response, ScheduleMapping map)
                throws ServletException;
    }

    /**
     * The Class NewScheduleOp.
     */
    class NewScheduleOp implements ScheduleOp {

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleOp#doOp(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleMapping)
         */
        @Override
        public String doOp(HttpServletRequest request, HttpServletResponse response, ScheduleMapping map)
                throws ServletException {
            ScheduleManager schedMgr = getSchedMgr(request);

            int schedIndex = WebSecurityUtils.safeParseInt(request.getParameter("scheduleIndex"));

            request.getSession().setAttribute("currentSchedIndex", request.getParameter("scheduleIndex"));
            request.getSession().setAttribute("currentSchedule", schedMgr.getSchedule(schedIndex));

            return map.get("success");
        }
    }

    /**
     * The Class EditOp.
     */
    class EditOp implements ScheduleOp {

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleOp#doOp(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleMapping)
         */
        @Override
        public String doOp(HttpServletRequest request, HttpServletResponse response, ScheduleMapping map)
                throws ServletException {
            ScheduleManager schedMgr = getSchedMgr(request);

            int schedIndex = WebSecurityUtils.safeParseInt(request.getParameter("scheduleIndex"));

            request.getSession().setAttribute("currentSchedIndex", request.getParameter("scheduleIndex"));
            request.getSession().setAttribute("currentSchedule", schedMgr.getSchedule(schedIndex));

            return map.get("success");
        }
    }

    /**
     * The Class DeleteOp.
     */
    class DeleteOp implements ScheduleOp {

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleOp#doOp(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleMapping)
         */
        @Override
        public String doOp(HttpServletRequest request, HttpServletResponse response, ScheduleMapping map)
                throws ServletException {
            ScheduleManager schedMgr = getSchedMgr(request);

            // delete the schedule and save
            int schedIndex = WebSecurityUtils.safeParseInt(request.getParameter("scheduleIndex"));
            schedMgr.deleteSchedule(schedIndex);
            schedMgr.saveSchedules();

            return map.get("success");
        }
    }

    /**
     * The Class DisplayOp.
     */
    class DisplayOp implements ScheduleOp {

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleOp#doOp(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleMapping)
         */
        @Override
        public String doOp(HttpServletRequest request, HttpServletResponse response, ScheduleMapping map)
                throws ServletException {
            // FIXME: schedMgr isn't used
            // ScheduleManager schedMgr = getSchedMgr(request);
            return map.get("success");
        }
    }

    /**
     * The Interface ScheduleMapping.
     */
    interface ScheduleMapping {

        /**
         * Gets the.
         *
         * @param result
         *            the result
         * @return the string
         */
        String get(String result);
    }

    /**
     * The Class SingleMapping.
     */
    static class SingleMapping implements ScheduleMapping {

        /** The m_view. */
        String m_view;

        /**
         * Instantiates a new single mapping.
         *
         * @param view
         *            the view
         */
        public SingleMapping(String view) {
            m_view = view;
        }

        /* (non-Javadoc)
         * @see org.opennms.web.admin.schedule.ScheduleEditorServlet.ScheduleMapping#get(java.lang.String)
         */
        @Override
        public String get(String result) {
            return m_view;
        }
    }

    /**
     * <p>
     * Constructor for ScheduleEditorServlet.
     * </p>
     */
    public ScheduleEditorServlet() {
        m_defaultOp = new DisplayOp();

        // set up operations
        m_ops.put("", m_defaultOp);
        m_ops.put("edit", new EditOp());
        m_ops.put("delete", new DeleteOp());
        m_ops.put("display", new DisplayOp());

        // set up mappings
        m_defaultMapping = new SingleMapping("/admin/schedule/displaySchedules.jsp");
        m_maps.put("", new SingleMapping("/admin/schedule/displaySchedules.jsp"));
        m_maps.put("edit", new SingleMapping("/admin/schedule/editSchedule.jsp"));

        m_defaultView = "/admin/schedule/displaySchedules.jsp";

    }

    /**
     * Gets the op.
     *
     * @param cmd
     *            the cmd
     * @return the op
     */
    ScheduleOp getOp(String cmd) {

        if (cmd == null) {
            return m_defaultOp;
        }

        ScheduleOp op = m_ops.get(cmd);
        if (op == null) {
            throw new IllegalArgumentException("Unrecognized operation " + cmd);
        }

        return op;
    }

    /**
     * Gets the map.
     *
     * @param cmd
     *            the cmd
     * @return the map
     */
    ScheduleMapping getMap(String cmd) {
        if (cmd == null) {
            return m_defaultMapping;
        }
        ScheduleMapping map = m_maps.get(cmd);
        if (map == null) {
            return m_defaultMapping;
        }
        return map;
    }

    /**
     * Show view.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param view
     *            the view
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void showView(HttpServletRequest request, HttpServletResponse response, String view) throws ServletException,
            IOException {
        String nextView = view;
        if (nextView == null) {
            nextView = m_defaultView;
        }

        // forward the request for proper display
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);

    }

    /** {@inheritDoc} */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    /** {@inheritDoc} */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        process(request, response);
    }

    /**
     * Process.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ScheduleOp op = getOp(request.getParameter("op"));
        ScheduleMapping map = getMap(request.getParameter("op"));
        String view = op.doOp(request, response, map);
        showView(request, response, view);

    }

    /**
     * Gets the sched mgr.
     *
     * @param request
     *            the request
     * @return the sched mgr
     * @throws ServletException
     *             the servlet exception
     */
    private ScheduleManager getSchedMgr(HttpServletRequest request) throws ServletException {
        ScheduleManager schedMgr = (ScheduleManager) request.getSession().getAttribute("schedMgr");
        String fileName = request.getParameter("file");
        if (schedMgr == null || (fileName != null && !fileName.equals(schedMgr.getFileName()))) {
            schedMgr = new OutageManager();
            schedMgr.setFileName(fileName);
            request.getSession().setAttribute("schedMgr", schedMgr);
        }
        schedMgr.loadSchedules();
        return schedMgr;
    }

}
