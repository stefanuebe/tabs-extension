package org.vaadin.stefan.tabs;

import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.shared.Registration;

public class TabsExtension {
	public static Registration createFilterForTabs(Tabs tabs, String[] elements) {
		tabs.getElement().getNode().runWhenAttached(ui -> {
			ui.getPage().executeJavaScript("$0._superOnClick = $0._onClick;", tabs);
			ui.getPage().executeJavaScript("$0._onClick = function(event) {" +
					"   if(!Array.of('" + String.join("','",elements) + "').includes(event.target.tagName)) {" +
					"       $0._superOnClick(event);" +
					"   }" +
					"};", tabs);

		});

		return () -> {
			tabs.getElement().getNode().runWhenAttached(ui -> {
				ui.getPage().executeJavaScript("$0._onClick = $0._superOnClick;", tabs);
				ui.getPage().executeJavaScript("$0._superOnClick = undefined; ");
			});
		};
	}
}
