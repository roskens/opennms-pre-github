/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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

package org.opennms.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;
import org.opennms.netmgt.reporting.service.DefaultReportService;
import org.opennms.netmgt.reporting.service.ReportService;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * <p>OnlineReportListController class.</p>
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public class OnlineReportListController extends AbstractController {

	// FIXME: Add Spring dependency injection
	private ReportService m_reportService = new DefaultReportService();

    private int m_pageSize;
    
    /** {@inheritDoc} */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView("report/database/onlineList");
        PagedListHolder<ReportDefinition> pagedListHolder = new PagedListHolder<ReportDefinition>((List<ReportDefinition>) m_reportService.getReportDefinitions());
        pagedListHolder.setPageSize(m_pageSize);
        int page = ServletRequestUtils.getIntParameter(request, "p", 0);
        pagedListHolder.setPage(page); 
        modelAndView.addObject("pagedListHolder", pagedListHolder);  

        return modelAndView;
    }

    /**
     * <p>getReportService</p>
     *
     * @return a {@link org.opennms.netmgt.reporting.service.ReportService} object.
     */
    public ReportService getReportService() {
        return m_reportService;
    }

    /**
     * <p>setDatabaseReportListService</p>
     *
     * @param listService a {@link org.opennms.netmgt.reporting.service.ReportService} object.
     */
    public void setReportService(ReportService reportService) {
        m_reportService = reportService;
    }

    /**
     * <p>getPageSize</p>
     *
     * @return a int.
     */
    public int getPageSize() {
        return m_pageSize;
    }

    /**
     * <p>setPageSize</p>
     *
     * @param pageSize a int.
     */
    public void setPageSize(int pageSize) {
        m_pageSize = pageSize;
    }

}
