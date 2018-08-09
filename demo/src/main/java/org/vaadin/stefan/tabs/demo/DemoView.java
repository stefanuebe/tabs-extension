package org.vaadin.stefan.tabs.demo;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import org.vaadin.stefan.tabs.CloseableTab;
import org.vaadin.stefan.tabs.TabsExtension;

import java.util.stream.IntStream;

/**
 * The main view contains a button and a template element.
 */
@Route("")
@HtmlImport("frontend://styles/shared-styles.html")
public class DemoView extends VerticalLayout {

	public DemoView() {
		Tabs tabs = new Tabs();
		add(tabs);
		IntStream.range(0, 10)
				.mapToObj(value -> new CloseableTab("Tab " + value))
				.forEach(tabs::add);


		tabs.setSelectedIndex(0);
		tabs.addSelectedChangeListener(event -> Notification.show("Selected tab index " + tabs.getSelectedIndex()));

		Registration filterForTabs = TabsExtension.createFilterForTabs(tabs, CloseableTab.TABS_FILTER);

//		filterForTabs.remove();
	}

}
