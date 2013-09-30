package org.opennms.features.backup.ui;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

public class TaskEntry {
    public enum TaskState {
        RUNNING("../base/common/img/ajax-loader.gif"),
        PENDING("../reindeer/common/icons/bullet.png"),
        COMPLETE("../runo/icons/16/ok.png"),
        FAILED("../runo/icons/16/cancel.png");

        private String filename;

        TaskState(String filename) {
            this.filename = filename;
        }

        public String getFilename() {
            return filename;
        }

        public ThemeResource getResource() {
            return new ThemeResource(filename);
        }
    }

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    TaskState state = TaskState.PENDING;
    Image image = new Image(null, TaskState.PENDING.getResource());


    public TaskEntry(String label) {
        horizontalLayout.addComponent(image);
        horizontalLayout.addComponent(new Label(label));
        horizontalLayout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
        horizontalLayout.setSpacing(true);
        //changeIcon();
    }

    private void changeIcon() {
        image.setSource(state.getResource());
    }


    public HorizontalLayout getHorizontalLayout() {
        return horizontalLayout;
    }

    public void setState(TaskState state) {
        this.state = state;

        changeIcon();
    }
}
