package org.opennms.vaadin.applicationstack.view;

import org.opennms.vaadin.applicationstack.model.ApplicationStack;

/**
 *
 * @author marskuh
 */
interface ApplicationStackChangedListener {
    void applicationStackChanged(ApplicationStack stack);
}
