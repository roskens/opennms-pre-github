package org.opennms.vaadin.applicationstack.view;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.vaadin.applicationstack.model.ApplicationLayer;
import org.opennms.vaadin.applicationstack.model.Criteria;
import org.opennms.vaadin.applicationstack.provider.NodeListProvider;

import java.util.ArrayList;
import java.util.List;

public class CriteriaBuilderComponent extends Window {
    BeanContainer<Integer, OnmsNode> beanContainer = new BeanContainer<Integer, OnmsNode>(OnmsNode.class);
    private List<CriteriaComponent> criteriaComponents = new ArrayList<CriteriaComponent>();
    private final VerticalLayout content = new VerticalLayout();
    private List<Criteria> criterias;

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final VerticalLayout criteriaLayout = new VerticalLayout();
    private final VerticalLayout tableLayout = new VerticalLayout();

    private final Table table = new Table();

    private ApplicationLayer applicationLayer;

    public CriteriaBuilderComponent(ApplicationLayer layer/*List<Criteria> criterias*/) {
        setCaption("Edit criterias");

        setClosable(false);
        setModal(true);
        setResizable(false);

        beanContainer.setBeanIdProperty("id");

        table.setContainerDataSource(beanContainer);
        table.setVisibleColumns(new Object[]{"id", "label"});

        setContent(content);

        Button saveButton = new Button("Save");
        saveButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                CriteriaBuilderComponent.this.close();

                if (applicationLayer != null) {
                    applicationLayer.setCriterias(new ArrayList<Criteria>(getCriterias()));
                }

                setLayer(applicationLayer);

                preview(criterias);
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                CriteriaBuilderComponent.this.close();

                setLayer(applicationLayer);

                preview(criterias);
            }
        });

        Button previewButton = new Button("Preview");
        previewButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                preview(getCriterias());
            }
        });

        table.setHeight(100, Unit.PIXELS);
        table.setWidth(100, Unit.PERCENTAGE);

        tableLayout.addComponent(table);

        setWidth(400, Unit.PIXELS);
        setHeight("40%");

        setLayer(layer);

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

    private void preview(List<Criteria> criterias) {
        NodeListProvider nodeListProvider = ((ApplicationStackUI) UI.getCurrent()).getNodeListProvider();

        beanContainer.removeAllItems();

        List<OnmsNode> onmsNodes = nodeListProvider.getNodesForCriterias(criterias);

        for (OnmsNode onmsNode : onmsNodes) {
            beanContainer.addBean(onmsNode);
        }

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

    public void setLayer(ApplicationLayer layer) {
        applicationLayer = layer;

        criterias = new ArrayList<Criteria>();

        if (applicationLayer != null) {
            if (applicationLayer.getCriterias() != null) {
                for (Criteria criteria : applicationLayer.getCriterias()) {
                    criterias.add(new Criteria(criteria));
                }
            }
        }

        if (criterias.size() == 0) {
            criterias.add(new Criteria());
        }

        criteriaComponents.clear();

        for (Criteria criteria : criterias) {
            criteriaComponents.add(new CriteriaComponent(criteria));
        }

        renderComponents();
    }
}
