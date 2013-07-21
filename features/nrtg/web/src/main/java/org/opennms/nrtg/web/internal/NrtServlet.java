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

package org.opennms.nrtg.web.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The Class NrtServlet.
 */
public class NrtServlet extends HttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The m_controller. */
    private NrtController m_controller;

    /**
     * Sets the controller.
     *
     * @param controller
     *            the new controller
     */
    public void setController(NrtController controller) {
        m_controller = controller;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession httpSession = req.getSession(true);
        resp.setContentType("text/html");

        if (req.getParameter("nrtCollectionTaskId") != null) {
            m_controller.nrtCollectionJobTrigger(req.getParameter("nrtCollectionTaskId"), httpSession);

            if ("true".equals(req.getParameter("poll"))) {
                resp.getOutputStream().println(m_controller.getMeasurementSetsForDestination(req.getParameter("nrtCollectionTaskId")));
            }
        } else if (req.getParameter("resourceId") != null && req.getParameter("report") != null) {
            ModelAndView modelAndView = m_controller.nrtStart(req.getParameter("resourceId"),
                                                              req.getParameter("report"), httpSession);

            String template = getTemplateAsString(modelAndView.getViewName() + ".template");

            for (Entry<String, Object> entry : modelAndView.getModel().entrySet()) {
                template = template.replaceAll("\\$\\{" + entry.getKey() + "\\}",
                                               (entry.getValue() != null ? entry.getValue().toString() : "null"));
            }

            resp.getOutputStream().write(template.getBytes());
        } else {
            throw new ServletException("unrecognized servlet parameters");
        }
    }

    /**
     * Gets the template as string.
     *
     * @param templateName
     *            the template name
     * @return the template as string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public String getTemplateAsString(String templateName) throws IOException {

        BufferedReader r = null;
        try {
            StringBuilder results = new StringBuilder();
            r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + templateName)));

            String line;
            while ((line = r.readLine()) != null) {
                results.append(line).append('\n');
            }

            return results.toString();
        } finally {
            if (r != null) {
                r.close();
            }
        }
    }

}
