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

package org.opennms.features.topology.plugins.browsers;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.opennms.features.topology.api.HasExtraComponents;
import org.opennms.netmgt.dao.api.AlarmRepository;

import com.vaadin.data.Container;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.themes.BaseTheme;

/**
 * The Class AlarmTable.
 */
@SuppressWarnings("serial")
public class AlarmTable extends SelectionAwareTable implements HasExtraComponents {

    /** The Constant ACTION_CLEAR. */
    private static final String ACTION_CLEAR = "Clear";

    /** The Constant ACTION_ESCALATE. */
    private static final String ACTION_ESCALATE = "Escalate";

    /** The Constant ACTION_UNACKNOWLEDGE. */
    private static final String ACTION_UNACKNOWLEDGE = "Unacknowledge";

    /** The Constant ACTION_ACKNOWLEDGE. */
    private static final String ACTION_ACKNOWLEDGE = "Acknowledge";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1384405693333129773L;

    /**
     * The Class CheckboxButton.
     */
    private class CheckboxButton extends Button {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -3595363303361200441L;

        /** The m_generator. */
        private CheckboxGenerator m_generator;

        /** The m_ack combo. */
        private AbstractSelect m_ackCombo;

        /**
         * Instantiates a new checkbox button.
         *
         * @param string
         *            the string
         */
        public CheckboxButton(String string) {
            super(string);
            setColumnCollapsingAllowed(false);
            addClickListener(new ClickListener() {

                private static final long serialVersionUID = 4351558084135658129L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    Set<Integer> selected = m_generator.getSelectedIds(AlarmTable.this);
                    if (selected.size() > 0) {
                        String action = (String) m_ackCombo.getValue();
                        if (ACTION_ACKNOWLEDGE.equals(action)) {
                            m_alarmRepo.acknowledgeAlarms(ArrayUtils.toPrimitive(selected.toArray(new Integer[0])),
                                                          getUser(), new Date());
                        } else if (ACTION_UNACKNOWLEDGE.equals(action)) {
                            m_alarmRepo.unacknowledgeAlarms(ArrayUtils.toPrimitive(selected.toArray(new Integer[0])),
                                                            getUser());
                        } else if (ACTION_ESCALATE.equals(action)) {
                            m_alarmRepo.escalateAlarms(ArrayUtils.toPrimitive(selected.toArray(new Integer[0])),
                                                       getUser(), new Date());
                        } else if (ACTION_CLEAR.equals(action)) {
                            m_alarmRepo.clearAlarms(ArrayUtils.toPrimitive(selected.toArray(new Integer[0])),
                                                    getUser(), new Date());
                        }

                        // Clear the checkboxes
                        m_generator.clearSelectedIds(AlarmTable.this);

                        AlarmTable.this.containerItemSetChange(new Container.ItemSetChangeEvent() {
                            private static final long serialVersionUID = 7086486972418241175L;

                            @Override
                            public Container getContainer() {
                                return AlarmTable.this.getContainerDataSource();
                            }
                        });
                    }
                }
            });
        }

        /**
         * Sets the combo.
         *
         * @param combo
         *            the new combo
         */
        public void setCombo(final AbstractSelect combo) {
            m_ackCombo = combo;
        }

        /**
         * Sets the checkbox generator.
         *
         * @param generator
         *            the new checkbox generator
         */
        public void setCheckboxGenerator(final CheckboxGenerator generator) {
            m_generator = generator;
        }
    }

    /**
     * The Class RefreshLinkButton.
     */
    private class RefreshLinkButton extends Button {

        /**
         * Instantiates a new refresh link button.
         *
         * @param caption
         *            the caption
         */
        private RefreshLinkButton(String caption) {
            super(caption);
            setStyleName(BaseTheme.BUTTON_LINK);
            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    AlarmTable.this.refreshRowCache();
                }
            });
        }
    }

    /**
     * The Class SelectAllButton.
     */
    private class SelectAllButton extends Button {

        /** The m_generator. */
        private CheckboxGenerator m_generator;

        /**
         * Instantiates a new select all button.
         *
         * @param string
         *            the string
         */
        public SelectAllButton(String string) {
            super(string);
            setStyleName(BaseTheme.BUTTON_LINK);
            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    m_generator.selectAll(AlarmTable.this);
                }
            });
        }

        /**
         * Sets the checkbox generator.
         *
         * @param generator
         *            the new checkbox generator
         */
        public void setCheckboxGenerator(final CheckboxGenerator generator) {
            m_generator = generator;
        }
    }

    /**
     * The Class ResetSelectionButton.
     */
    private class ResetSelectionButton extends Button {

        /** The m_generator. */
        private CheckboxGenerator m_generator;

        /**
         * Instantiates a new reset selection button.
         *
         * @param string
         *            the string
         */
        public ResetSelectionButton(String string) {
            super(string);
            setStyleName(BaseTheme.BUTTON_LINK);
            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    m_generator.clearSelectedIds(AlarmTable.this);
                }
            });
        }

        /**
         * Sets the checkbox generator.
         *
         * @param generator
         *            the new checkbox generator
         */
        public void setCheckboxGenerator(final CheckboxGenerator generator) {
            m_generator = generator;
        }
    }

    /** The m_submit button. */
    private final CheckboxButton m_submitButton;

    /** The m_ack combo. */
    private final NativeSelect m_ackCombo;

    /** The m_refresh button. */
    private final Button m_refreshButton = new RefreshLinkButton("Refresh");

    /** The m_select all button. */
    private final SelectAllButton m_selectAllButton = new SelectAllButton("Select All");

    /** The m_reset button. */
    private final ResetSelectionButton m_resetButton = new ResetSelectionButton("Deselect All");

    /** The m_alarm repo. */
    private final AlarmRepository m_alarmRepo;

    /** The m_item set change listeners. */
    private Set<ItemSetChangeListener> m_itemSetChangeListeners = new HashSet<ItemSetChangeListener>();

    /**
     * Leave OnmsDaoContainer without generics; the Aries blueprint code cannot
     * match up
     * the arguments if you put the generic types in.
     *
     * @param caption
     *            the caption
     * @param container
     *            the container
     * @param alarmRepo
     *            the alarm repo
     */
    public AlarmTable(final String caption, final OnmsDaoContainer container, final AlarmRepository alarmRepo) {
        super(caption, container);
        m_alarmRepo = alarmRepo;

        m_ackCombo = new NativeSelect();
        m_ackCombo.setNullSelectionAllowed(false);
        m_ackCombo.addItem(ACTION_ACKNOWLEDGE);
        m_ackCombo.addItem(ACTION_UNACKNOWLEDGE);
        m_ackCombo.addItem(ACTION_ESCALATE);
        m_ackCombo.addItem(ACTION_CLEAR);
        m_ackCombo.setValue(ACTION_ACKNOWLEDGE); // Make "Acknowledge" the
                                                 // default value

        m_submitButton = new CheckboxButton("Submit");
        m_submitButton.setCombo(m_ackCombo);
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.Table#containerItemSetChange(com.vaadin.data.Container.ItemSetChangeEvent)
     */
    @Override
    public void containerItemSetChange(Container.ItemSetChangeEvent event) {
        for (ItemSetChangeListener listener : m_itemSetChangeListeners) {
            listener.containerItemSetChange(event);
        }
        super.containerItemSetChange(event);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.plugins.browsers.SelectionAwareTable#setColumnGenerators(java.util.Map)
     */
    @Override
    @SuppressWarnings("unchecked")
    // Because Aries Blueprint cannot handle generics
    public void setColumnGenerators(final Map generators) {
        super.setColumnGenerators(generators);
        for (final Object key : generators.keySet()) {
            // If any of the column generators are {@link CheckboxGenerator}
            // instances,
            // then connect it to the buttons.
            try {
                CheckboxGenerator generator = (CheckboxGenerator) generators.get(key);
                m_submitButton.setCheckboxGenerator(generator);
                m_selectAllButton.setCheckboxGenerator(generator);
                m_resetButton.setCheckboxGenerator(generator);

                m_itemSetChangeListeners.add(generator);
            } catch (final ClassCastException e) {
            }
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.HasExtraComponents#getExtraComponents()
     */
    @Override
    public Component[] getExtraComponents() {
        return new Component[] { m_refreshButton, m_selectAllButton, m_resetButton, m_ackCombo, m_submitButton };
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    private String getUser() {
        return "admin"; // TODO use real user name
    }
}
