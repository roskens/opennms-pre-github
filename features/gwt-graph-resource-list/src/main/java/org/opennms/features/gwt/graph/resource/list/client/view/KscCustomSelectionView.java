/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.features.gwt.graph.resource.list.client.view;

import org.opennms.features.gwt.graph.resource.list.client.presenter.KscCustomReportListPresenter.SelectionDisplay;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class KscCustomSelectionView.
 */
public class KscCustomSelectionView implements SelectionDisplay {

    /** The Constant VIEW. */
    public static final String VIEW = "view";

    /** The Constant CUSTOMIZE. */
    public static final String CUSTOMIZE = "customize";

    /** The Constant CREATE_NEW. */
    public static final String CREATE_NEW = "createNew";

    /** The Constant CREATE_NEW_FROM_EXISTING. */
    public static final String CREATE_NEW_FROM_EXISTING = "createNewExisting";

    /** The Constant DELETE. */
    public static final String DELETE = "delete";

    /** The m_vert panel. */
    VerticalPanel m_vertPanel;

    /** The m_submit button. */
    Button m_submitButton;

    /** The m_view rb. */
    RadioButton m_viewRB;

    /** The m_customize rb. */
    RadioButton m_customizeRB;

    /** The m_create new rb. */
    RadioButton m_createNewRB;

    /** The m_create new existing rb. */
    RadioButton m_createNewExistingRB;

    /** The m_delete rb. */
    RadioButton m_deleteRB;

    /**
     * Instantiates a new ksc custom selection view.
     */
    public KscCustomSelectionView() {
        m_vertPanel = new VerticalPanel();
        m_vertPanel.setStyleName("onms-table-no-borders-margin");
        m_submitButton = new Button("Submit");
        m_viewRB = new RadioButton("group1", "View");
        m_customizeRB = new RadioButton("group1", "Customize");
        m_createNewRB = new RadioButton("group1", "Create New");
        m_createNewExistingRB = new RadioButton("group1", "Create New from Existing");
        m_deleteRB = new RadioButton("group1", "Delete");

        m_vertPanel.add(m_viewRB);
        m_vertPanel.add(m_customizeRB);
        m_vertPanel.add(m_createNewRB);
        m_vertPanel.add(m_createNewExistingRB);
        m_vertPanel.add(m_deleteRB);
        m_vertPanel.add(m_submitButton);

    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.presenter.KscCustomReportListPresenter.SelectionDisplay#getSubmitButton()
     */
    @Override
    public HasClickHandlers getSubmitButton() {
        return m_submitButton;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.presenter.KscCustomReportListPresenter.SelectionDisplay#getSelectAction()
     */
    @Override
    public String getSelectAction() {
        if (m_viewRB.getValue()) {
            return VIEW;
        } else if (m_customizeRB.getValue()) {
            return CUSTOMIZE;
        } else if (m_createNewRB.getValue()) {
            return CREATE_NEW;
        } else if (m_createNewExistingRB.getValue()) {
            return CREATE_NEW_FROM_EXISTING;
        } else if (m_deleteRB.getValue()) {
            return DELETE;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.presenter.KscCustomReportListPresenter.SelectionDisplay#asWidget()
     */
    @Override
    public Widget asWidget() {
        return m_vertPanel.asWidget();
    }

}
