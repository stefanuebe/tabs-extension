package org.vaadin.stefan.tabs.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.stefan.tabs.TabsExtension;

/**
 * The main view contains a button and a template element.
 */
@Route("test")
@HtmlImport("frontend://styles/shared-styles.html")
public class DemoView1 extends VerticalLayout {

	public DemoView1() {
		Tabs tabs = new Tabs();
		add(tabs);
		tabs.addSelectedChangeListener(event -> Notification.show("Selected " + tabs.getSelectedIndex()));

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		TextField textField = new TextField();
		Button add = new Button("Add");

		add.addClickListener(event -> {
			Tab tab = new Tab(textField.getValue());
			tabs.add(tab);

			tabs.getElement().insertChild(tabs.getComponentCount() - 1, tab.getElement());
			tabs.setSelectedTab(tab); // does not work?

			textField.clear();
		});

		horizontalLayout.add(textField, add);
		tabs.add(horizontalLayout);

		TabsExtension.createFilterForTabs(tabs, "vaadin-text-field", "vaadin-button");
		TabsExtension.disableKeySelectionOfTabs(tabs);

	}

}
