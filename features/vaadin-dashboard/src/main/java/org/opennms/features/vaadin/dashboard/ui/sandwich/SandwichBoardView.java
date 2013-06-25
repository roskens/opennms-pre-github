package org.opennms.features.vaadin.dashboard.ui.sandwich;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Marcus Hellberg (marcus@vaadin.com)
 */
public class SandwichBoardView extends VerticalLayout implements View {

    private final SandwichBoardHeader sandwichBoardHeader;
    private final SandwichBoardBody sandwichBoardBody;

    public SandwichBoardView() {
        setSizeFull();
        sandwichBoardBody = new SandwichBoardBody();
        sandwichBoardHeader = new SandwichBoardHeader(sandwichBoardBody);


        addComponents(sandwichBoardHeader, sandwichBoardBody);
        setExpandRatio(sandwichBoardBody, 1.0f);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
