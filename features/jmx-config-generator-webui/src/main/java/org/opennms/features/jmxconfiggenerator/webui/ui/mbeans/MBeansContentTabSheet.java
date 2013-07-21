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

package org.opennms.features.jmxconfiggenerator.webui.ui.mbeans;

import org.opennms.features.jmxconfiggenerator.webui.data.ModelChangeListener;
import org.opennms.features.jmxconfiggenerator.webui.ui.UIHelper;
import org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.EditControls.ButtonHandler;
import org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.EditControls.ButtonType;
import org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.EditControls.FormButtonHandler;
import org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.EditControls.TableButtonHandler;
import org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.MBeansController.Callback;
import org.opennms.xmlns.xsd.config.jmx_datacollection.CompAttrib;
import org.opennms.xmlns.xsd.config.jmx_datacollection.Mbean;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ReadOnlyStatusChangeListener;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * The Class MBeansContentTabSheet.
 *
 * @author Markus von Rüden
 */
public class MBeansContentTabSheet extends TabSheet implements ModelChangeListener<Mbean>, ViewStateChangedListener {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The attributes layout. */
    private AttributesLayout attributesLayout;

    /** The composites layout. */
    private CompositesLayout compositesLayout;

    /** The controller. */
    private final MBeansController controller;

    /** The selected tab position. */
    private int selectedTabPosition;

    /**
     * Instantiates a new m beans content tab sheet.
     *
     * @param controller
     *            the controller
     */
    public MBeansContentTabSheet(final MBeansController controller) {
        this.controller = controller;
        setSizeFull();
        attributesLayout = new AttributesLayout();
        compositesLayout = new CompositesLayout();
        attributesLayout.setSizeFull();
        compositesLayout.setSizeFull();

        addTab(attributesLayout, "Attributes");
        addTab(compositesLayout, "Composites");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.data.ModelChangeListener#modelChanged(java.lang.Object)
     */
    @Override
    public void modelChanged(Mbean newModel) {
        // forward
        attributesLayout.modelChanged(newModel);
        compositesLayout.modelChanged(newModel);
        disableCompositesTabIfNecessary(newModel);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.ViewStateChangedListener#viewStateChanged(org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.ViewStateChangedEvent)
     */
    @Override
    public void viewStateChanged(ViewStateChangedEvent event) {
        // just forward
        attributesLayout.viewStateChanged(event);
        compositesLayout.viewStateChanged(event);
        selectedTabPosition = UIHelper.enableTabs(this, event, selectedTabPosition);
        disableCompositesTabIfNecessary(controller.getSelectedMBean());
    }

    /**
     * Disable composites tab if necessary.
     *
     * @param newModel
     *            the new model
     */
    private void disableCompositesTabIfNecessary(Mbean newModel) {
        boolean alreadyDisabled = !getTab(compositesLayout).isEnabled();
        boolean shouldDisable = newModel == null || newModel.getCompAttrib().isEmpty();
        boolean disabled = shouldDisable || alreadyDisabled; // so we do not
                                                             // overwrite
                                                             // enable/disable
                                                             // while already
                                                             // disabled due to
                                                             // another
                                                             // component
        // disable composites tab, if there are no composites
        getTab(compositesLayout).setEnabled(!disabled);
        getTab(compositesLayout).setDescription(disabled ? "no composites available" : null);
    }

    /**
     * The Class CompositesLayout.
     */
    private class CompositesLayout extends VerticalLayout implements ViewStateChangedListener,
            ModelChangeListener<Mbean> {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The tab sheet. */
        private final TabSheet tabSheet = new TabSheet();

        /** The selected composites tab position. */
        private int selectedCompositesTabPosition;

        /**
         * Instantiates a new composites layout.
         */
        public CompositesLayout() {
            setSizeFull();
            setSpacing(false);
            setMargin(false);
            tabSheet.setSizeFull();
            addComponent(tabSheet);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.ViewStateChangedListener#viewStateChanged(org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.ViewStateChangedEvent)
         */
        @Override
        public void viewStateChanged(ViewStateChangedEvent event) {
            selectedCompositesTabPosition = UIHelper.enableTabs(tabSheet, event, selectedCompositesTabPosition);
            if (tabSheet.getSelectedTab() == null)
                return;
            ((CompositeTabLayout) tabSheet.getSelectedTab()).viewStateChanged((event)); // forwared
        }

        /* (non-Javadoc)
         * @see org.opennms.features.jmxconfiggenerator.webui.data.ModelChangeListener#modelChanged(java.lang.Object)
         */
        @Override
        public void modelChanged(Mbean newModel) {
            tabSheet.removeAllComponents();
            int no = 1;
            for (CompAttrib attrib : newModel.getCompAttrib()) {
                final String tabLabel = String.format("#%d %s", no++, attrib.getName());
                final CompositeTabLayout tabContent = new CompositeTabLayout(getCompositeForm(newModel, attrib),
                                                                             getCompAttribTable(newModel, attrib));
                tabSheet.addTab(tabContent, tabLabel);
            }
        }

        /**
         * Gets the composite form.
         *
         * @param mbean
         *            the mbean
         * @param compAttrib
         *            the comp attrib
         * @return the composite form
         */
        private NameEditForm getCompositeForm(final Mbean mbean, final CompAttrib compAttrib) {
            NameEditForm form = new NameEditForm(controller, new FormParameter() {
                @Override
                public boolean hasFooter() {
                    return false;
                }

                @Override
                public String getCaption() {
                    return null;
                }

                @Override
                public String getEditablePropertyName() {
                    return "name";
                }

                @Override
                public String getNonEditablePropertyName() {
                    return "alias";
                }

                @Override
                public Object[] getVisiblePropertieNames() {
                    return new Object[] { "selected", getNonEditablePropertyName(), getEditablePropertyName() };
                }

                @Override
                public EditControls.Callback getAdditionalCallback() {
                    return null;
                }
            });
            Item item = controller.getCompositeAttributeContainer(mbean).getItem(compAttrib);
            form.setItemDataSource(item);
            return form;
        }

        /**
         * Gets the comp attrib table.
         *
         * @param mbean
         *            the mbean
         * @param attrib
         *            the attrib
         * @return the comp attrib table
         */
        private Table getCompAttribTable(final Mbean mbean, final CompAttrib attrib) {
            AttributesTable memberTable = new AttributesTable(controller, new Callback() {
                @Override
                public Container getContainer() {
                    return controller.getCompositeMemberContainer(attrib);
                }
            });
            memberTable.modelChanged(mbean);
            return memberTable;
        }

        /**
         * The Class CompositeTabLayout.
         */
        private class CompositeTabLayout extends VerticalLayout implements Property.ReadOnlyStatusChangeNotifier,
                EditControls.Callback, ViewStateChangedListener {

            /** The Constant serialVersionUID. */
            private static final long serialVersionUID = 1L;

            /** The composite form. */
            private final NameEditForm compositeForm;

            /** The composite table. */
            private final Table compositeTable;

            /** The form button handler. */
            private final FormButtonHandler<NameEditForm> formButtonHandler;

            /** The table button handler. */
            private final TableButtonHandler<Table> tableButtonHandler;

            /** The footer. */
            private final EditControls<AbstractField> footer;

            /**
             * Instantiates a new composite tab layout.
             *
             * @param compositeForm
             *            the composite form
             * @param compositeTable
             *            the composite table
             */
            private CompositeTabLayout(NameEditForm compositeForm, Table compositeTable) {
                this.compositeForm = compositeForm;
                this.compositeTable = compositeTable;
                formButtonHandler = new FormButtonHandler<NameEditForm>(compositeForm);
                tableButtonHandler = new TableButtonHandler<Table>(compositeTable);
                footer = new EditControls<AbstractField>(this, new ButtonHandler<AbstractField>() {
                    @Override
                    public void handleSave() {
                        if (formButtonHandler.getOuter().isValid() && tableButtonHandler.getOuter().isValid()) {
                            formButtonHandler.handleSave();
                            tableButtonHandler.handleSave();
                        } else {
                            UIHelper.showValidationError("There are some errors on this view. Please fix them first");
                        }
                    }

                    @Override
                    public void handleCancel() {
                        formButtonHandler.handleCancel();
                        tableButtonHandler.handleCancel();
                    }

                    @Override
                    public void handleEdit() {
                        formButtonHandler.handleEdit();
                        tableButtonHandler.handleEdit();
                    }

                    @Override
                    public AbstractField getOuter() {
                        return null;
                    }
                });
                setSizeFull();
                setSpacing(false);
                setReadOnly(true);
                addComponent(footer);
                addComponent(compositeForm);
                addComponent(compositeTable);
                addFooterHooks(footer);
                setExpandRatio(compositeTable, 1);
            }

            /* (non-Javadoc)
             * @see com.vaadin.data.Property.ReadOnlyStatusChangeNotifier#addListener(com.vaadin.data.Property.ReadOnlyStatusChangeListener)
             */
            @Override
            public void addListener(ReadOnlyStatusChangeListener listener) {
                addReadOnlyStatusChangeListener(listener);
            }

            /* (non-Javadoc)
             * @see com.vaadin.data.Property.ReadOnlyStatusChangeNotifier#addReadOnlyStatusChangeListener(com.vaadin.data.Property.ReadOnlyStatusChangeListener)
             */
            @Override
            public void addReadOnlyStatusChangeListener(ReadOnlyStatusChangeListener listener) {
                compositeForm.addListener(listener);
                compositeTable.addListener(listener);
            }

            /* (non-Javadoc)
             * @see com.vaadin.data.Property.ReadOnlyStatusChangeNotifier#removeListener(com.vaadin.data.Property.ReadOnlyStatusChangeListener)
             */
            @Override
            public void removeListener(ReadOnlyStatusChangeListener listener) {
                removeReadOnlyStatusChangeListener(listener);
            }

            /* (non-Javadoc)
             * @see com.vaadin.data.Property.ReadOnlyStatusChangeNotifier#removeReadOnlyStatusChangeListener(com.vaadin.data.Property.ReadOnlyStatusChangeListener)
             */
            @Override
            public void removeReadOnlyStatusChangeListener(ReadOnlyStatusChangeListener listener) {
                compositeForm.removeListener(listener);
                compositeTable.removeListener(listener);
            }

            /* (non-Javadoc)
             * @see com.vaadin.ui.AbstractComponent#setReadOnly(boolean)
             */
            @Override
            public void setReadOnly(boolean readOnly) {
                super.setReadOnly(readOnly);
                compositeForm.setReadOnly(readOnly);
                compositeTable.setReadOnly(readOnly);

            }

            /* (non-Javadoc)
             * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.EditControls.Callback#callback(org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.EditControls.ButtonType, com.vaadin.ui.Component)
             */
            @Override
            public void callback(ButtonType type, Component outer) {
                if (type == ButtonType.edit) {
                    controller.fireViewStateChanged(ViewState.Edit, outer);
                }
                if (type == ButtonType.cancel) {
                    controller.fireViewStateChanged(ViewState.LeafSelected, outer);
                }
                if (type == ButtonType.save && compositeForm.isValid() && compositeTable.isValid()) {
                    controller.fireViewStateChanged(ViewState.LeafSelected, CompositeTabLayout.this);
                }
            }

            /**
             * Adds the footer hooks.
             *
             * @param footer
             *            the footer
             */
            private void addFooterHooks(final EditControls footer) {
                footer.addSaveHook(this);
                footer.addCancelHook(this);
                footer.addEditHook(this);
            }

            /* (non-Javadoc)
             * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.ViewStateChangedListener#viewStateChanged(org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.ViewStateChangedEvent)
             */
            @Override
            public void viewStateChanged(ViewStateChangedEvent event) {
                switch (event.getNewState()) {
                case Init:
                case LeafSelected:
                    setEnabled(true);
                    footer.setVisible(true);
                    break;
                case NonLeafSelected:
                case Edit:
                    setEnabled(event.getSource() == this);
                    footer.setVisible(event.getSource() == this);
                    break;
                }
            }
        }
    }

    /**
     * The Class AttributesLayout.
     */
    private class AttributesLayout extends VerticalLayout implements ViewStateChangedListener,
            EditControls.Callback<Table> {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The attributes table. */
        private final AttributesTable attributesTable;

        /** The footer. */
        private final EditControls footer;

        /**
         * Instantiates a new attributes layout.
         */
        private AttributesLayout() {
            attributesTable = new AttributesTable(controller, new Callback() {
                @Override
                public Container getContainer() {
                    return controller.getAttributeContainer(controller.getSelectedMBean());
                }
            });
            footer = new EditControls(this.attributesTable);
            setSizeFull();
            addComponent(footer);
            addComponent(attributesTable);
            setSpacing(false);
            setMargin(false);
            addFooterHooks();
            setExpandRatio(attributesTable, 1);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.EditControls.Callback#callback(org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.EditControls.ButtonType, com.vaadin.ui.Component)
         */
        @Override
        public void callback(ButtonType type, Table outer) {
            if (type == ButtonType.cancel) {
                outer.discard();
                controller.fireViewStateChanged(ViewState.LeafSelected, outer);
            }
            if (type == ButtonType.edit) {
                controller.fireViewStateChanged(ViewState.Edit, outer);
            }
            if (type == ButtonType.save) {
                if (outer.isValid()) {
                    outer.commit();
                    controller.fireViewStateChanged(ViewState.LeafSelected, outer);
                } else {
                    UIHelper.showValidationError("There are errors in this view. Please fix them first or cancel.");
                }
            }
        }

        /**
         * Adds the footer hooks.
         */
        private void addFooterHooks() {
            footer.addSaveHook(this);
            footer.addCancelHook(this);
            footer.addEditHook(this);
        }

        /* (non-Javadoc)
         * @see com.vaadin.ui.AbstractComponent#setEnabled(boolean)
         */
        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            footer.setVisible(enabled);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.ViewStateChangedListener#viewStateChanged(org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.ViewStateChangedEvent)
         */
        @Override
        public void viewStateChanged(ViewStateChangedEvent event) {
            switch (event.getNewState()) {
            case Init:
            case NonLeafSelected:
            case Edit:
                footer.setVisible(event.getSource() == attributesTable);
                break;
            case LeafSelected:
                footer.setVisible(true);
                break;
            }
            attributesTable.viewStateChanged(event);// forward
        }

        /**
         * Model changed.
         *
         * @param newModel
         *            the new model
         */
        private void modelChanged(Mbean newModel) {
            attributesTable.modelChanged(newModel); // forward
        }
    }
}
