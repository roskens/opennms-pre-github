package org.opennms.features.vaadin.dashboard.ui.wallboard;

import org.opennms.features.vaadin.dashboard.config.ui.WallboardProvider;
import org.opennms.features.vaadin.dashboard.model.Wallboard;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Marcus Hellberg (marcus@vaadin.com)
 */
public class WallboardView extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;
    private final WallboardBody dashletBoardBody;

    public WallboardView() {
        setSizeFull();
        dashletBoardBody = new WallboardBody();
        addComponents(dashletBoardBody);
        setExpandRatio(dashletBoardBody, 1.0f);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() != null) {
            Wallboard wallboard = WallboardProvider.getInstance().getWallboard(event.getParameters());
            if (wallboard != null) {
                dashletBoardBody.setDashletSpecs(wallboard.getDashletSpecs());
            }
        }
    }
}
