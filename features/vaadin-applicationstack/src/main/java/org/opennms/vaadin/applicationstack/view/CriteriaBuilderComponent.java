package org.opennms.vaadin.applicationstack.view;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.opennms.vaadin.applicationstack.model.Criteria;

import java.util.ArrayList;
import java.util.List;

public class CriteriaBuilderComponent extends Window {
    private List<CriteriaComponent> criteriaComponents = new ArrayList<CriteriaComponent>();
    private final VerticalLayout content = new VerticalLayout();

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final VerticalLayout criteriaLayout = new VerticalLayout();
    private final VerticalLayout tableLayout = new VerticalLayout();

    private final Table table = new Table();

    public CriteriaBuilderComponent(List<Criteria> criterias) {
        if (criterias == null) {
            criterias = new ArrayList<Criteria>();
        }

        if (criterias.size() == 0) {
            criterias.add(new Criteria(Criteria.EntityType.Id, Criteria.Operator.Equals, "0"));
        }

        for (Criteria criteria : criterias) {
            criteriaComponents.add(new CriteriaComponent(criteria));
        }

        setCaption("Edit criterias");
        setClosable(false);
        setModal(true);
        setResizable(false);

        setContent(content);

        Button saveButton = new Button("Save");
        saveButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                CriteriaBuilderComponent.this.close();

                for (Criteria criteria : getCriterias()) {
                    System.out.println(criteria);
                }
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                CriteriaBuilderComponent.this.close();
            }
        });

        Button previewButton = new Button("Preview");
        previewButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        table.addContainerProperty("Id", Integer.class, 0);
        table.addContainerProperty("Label", String.class, 0);

        table.setHeight(100, Unit.PIXELS);
        table.setWidth(100, Unit.PERCENTAGE);

        tableLayout.addComponent(table);

        setWidth(400, Unit.PIXELS);
        setHeight("40%");

        renderComponents();
        content.setMargin(true);
        content.setSpacing(true);

        criteriaLayout.setSpacing(true);
        buttonLayout.setSpacing(true);

        buttonLayout.addComponent(saveButton);
        buttonLayout.addComponent(cancelButton);
        buttonLayout.addComponent(previewButton);

        content.addComponent(criteriaLayout);
        content.addComponent(tableLayout);
        content.addComponent(buttonLayout);
    }

    private void renderComponents() {
        criteriaLayout.removeAllComponents();

        boolean isFirst = true;

        for (final CriteriaComponent criteriaComponent : criteriaComponents) {
            criteriaComponent.render();

            if (!isFirst) {
                Button minusButton = new Button();
                minusButton.setIcon(new ThemeResource("list-remove.png"));

                minusButton.addClickListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        criteriaComponents.remove(criteriaComponent);
                        renderComponents();
                    }
                });

                criteriaComponent.addComponent(minusButton);
            } else {
                Button plusButton = new Button();
                plusButton.setIcon(new ThemeResource("list-add.png"));

                plusButton.addClickListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        criteriaComponents.add(new CriteriaComponent(new Criteria(Criteria.EntityType.Id, Criteria.Operator.Equals, "*")));
                        renderComponents();
                    }
                });

                criteriaComponent.addComponent(plusButton);

                isFirst = false;
            }

            criteriaLayout.addComponent(criteriaComponent);
        }
    }

    public List<Criteria> getCriterias() {
        List<Criteria> criterias = new ArrayList<Criteria>();

        for (CriteriaComponent criteriaComponent : criteriaComponents) {
            criterias.add(criteriaComponent.getCriteria());
        }

        return criterias;
    }
}
